/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.jsmpp.session;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.jsmpp.DefaultPDUReader;
import org.jsmpp.DefaultPDUSender;
import org.jsmpp.InvalidCommandLengthException;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.PDUReader;
import org.jsmpp.PDUSender;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.SynchronizedPDUSender;
import org.jsmpp.bean.Bind;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.EnquireLink;
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.QueueMaxException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.connection.Connection;
import org.jsmpp.session.connection.ConnectionFactory;
import org.jsmpp.session.connection.socket.SocketConnectionFactory;
import org.jsmpp.util.DefaultComposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is an object that used to communicate with an ESME. It hides all un-needed SMPP operation that might harm if the user code use it such as:
 *
 * <ul>
 * <li>DELIVER_SM_RESP, should be called only as response to DELIVER_SM</li>
 * <li>UNBIND_RESP, should be called only as response to UNBIND_RESP</li>
 * <li>DATA_SM_RESP, should be called only as response to DATA_SM</li>
 * <li>ENQUIRE_LINK_RESP, should be called only as response to ENQUIRE_LINK</li>
 * <li>GENERIC_NACK, should be called only as response to GENERIC_NACK</li>
 * </ul>
 * <p>
 * All SMPP operations (request-response) are blocking, for an example: DELIVER_SM
 * will be blocked until DELIVER_SM_RESP received or timeout. This looks like
 * synchronous communication, but the {@link SMPPOutboundSession} implementation give
 * ability to the asynchronous way by executing the DELIVER_SM operation parallel
 * on a different thread. The very simple implementation by using Thread pool,
 * {@link ExecutorService} will do.
 *
 * @author pmoerenhout
 */
public class SMPPOutboundSession extends AbstractSession implements OutboundClientSession {
  private static final Logger log = LoggerFactory.getLogger(SMPPOutboundSession.class);

  /* Utility */
  private final PDUReader pduReader;

  /* Connection */
  private final ConnectionFactory connFactory;
  private final OutboundResponseHandler responseHandler = new ResponseHandlerImpl();

  private Connection conn;
  private DataInputStream in;
  private OutputStream out;
  private PDUReaderWorker pduReaderWorker;

  private MessageReceiverListener messageReceiverListener;
  private BoundSessionStateListener sessionStateListener = new BoundSessionStateListener();
  private SMPPOutboundSessionContext sessionContext = new SMPPOutboundSessionContext(this, sessionStateListener);
  private BindRequestReceiver bindRequestReceiver = new BindRequestReceiver(responseHandler);

  /**
   * Default constructor of {@link SMPPOutboundSession}. The next action might be, connect and bind to a destination message center.
   *
   * @see #connectAndOutbind(String, int, String, String)
   */
  public SMPPOutboundSession() {
    this(new SynchronizedPDUSender(new DefaultPDUSender(new DefaultComposer())),
        new DefaultPDUReader(),
        SocketConnectionFactory.getInstance());
  }

  public SMPPOutboundSession(PDUSender pduSender, PDUReader pduReader,
                             ConnectionFactory connFactory) {
    super(pduSender);
    this.pduReader = pduReader;
    this.connFactory = connFactory;
  }

  public SMPPOutboundSession(String host, int port, OutbindParameter outbindParam,
                             PDUSender pduSender, PDUReader pduReader,
                             ConnectionFactory connFactory) throws IOException {
    this(pduSender, pduReader, connFactory);
    connectAndOutbind(host, port, outbindParam);
  }

  @Override
  public BindRequest connectAndOutbind(String host, int port, OutbindParameter outbindParam)
      throws IOException {
    return connectAndOutbind(host, port, outbindParam, 60000);
  }

  /**
   * Open connection and outbind immediately. The default timeout is 60 seconds.
   *
   * @param host     is the ESME host address.
   * @param port     is the ESME listen port.
   * @param systemId is the system id.
   * @param password is the password.
   * @return the received bind request
   * @throws IOException if there is an IO error found.
   */
  @Override
  public BindRequest connectAndOutbind(String host, int port,
                                       String systemId, String password) throws IOException {
    return connectAndOutbind(host, port, new OutbindParameter(systemId, password), 60000);
  }

  /**
   * Open connection and outbind immediately with specified timeout. The default timeout is 60 seconds.
   *
   * @param host     is the ESME host address.
   * @param port     is the ESME listen port.
   * @param systemId is the system id.
   * @param password is the password.
   * @param timeout  is the timeout.
   * @return the received bind request
   * @throws IOException if there is an IO error found.
   */
  @Override
  public BindRequest connectAndOutbind(String host, int port,
                                       String systemId, String password, long timeout) throws IOException {
    return connectAndOutbind(host, port, new OutbindParameter(systemId, password), timeout);
  }

  /**
   * Open connection and outbind immediately.
   *
   * @param host             is the ESME host address.
   * @param port             is the ESME listen port.
   * @param outbindParameter is the bind parameters.
   * @param timeout          is the timeout.
   * @return the SMSC system id.
   * @throws IOException if there is an IO error found.
   */
  public BindRequest connectAndOutbind(String host, int port, OutbindParameter outbindParameter, long timeout)
      throws IOException {
    log.debug("Connect and bind to {} port {}", host, port);
    if (getSessionState() != SessionState.CLOSED) {
      throw new IOException("Session state is not closed");
    }

    conn = connFactory.createConnection(host, port);
    log.info("Connected to {}", conn.getInetAddress());

    conn.setSoTimeout(getEnquireLinkTimer());

    sessionContext.open();
    try {
      in = new DataInputStream(conn.getInputStream());
      out = conn.getOutputStream();

      pduReaderWorker = new PDUReaderWorker(getPduProcessorDegree(), getQueueCapacity());
      pduReaderWorker.start();
      sendOutbind(outbindParameter.getSystemId(), outbindParameter.getPassword());
    } catch (IOException e) {
      log.error("I/O error occurred", e);
      close();
      throw e;
    }

    try {
      BindRequest bindRequest = waitForBind(timeout);

      enquireLinkSender = new EnquireLinkSender();
      enquireLinkSender.start();

      return bindRequest;
    } catch (IllegalStateException e) {
      String message = "System error";
      log.error(message, e);
      close();
      throw new IOException(message + ": " + e.getMessage(), e);
    } catch (TimeoutException e) {
      String message = "Wait for bind response timed out";
      log.error(message, e);
      throw new IOException(message + ": " + e.getMessage(), e);
    }
  }

  /**
   * Wait for bind request.
   *
   * @param timeout is the timeout in milliseconds.
   * @return the {@link BindRequest}.
   * @throws IllegalStateException if this invocation of this method has been made or invoke when state is not OPEN.
   * @throws TimeoutException      if the timeout has been reach and {@link SMPPServerSession} are no more valid because the connection will be close
   *                               automatically.
   */
  private BindRequest waitForBind(long timeout)
      throws IllegalStateException, TimeoutException {
    SessionState currentSessionState = getSessionState();
    if (currentSessionState.equals(SessionState.OPEN)) {
      try {
        return bindRequestReceiver.waitForRequest(timeout);
      } catch (IllegalStateException e) {
        throw new IllegalStateException(
            "Invocation of waitForBind() has been made", e);
      } catch (TimeoutException e) {
        close();
        throw e;
      }
    } else {
      throw new IllegalStateException(
          "waitForBind() should be invoked on OPEN state, actual state is "
              + currentSessionState);
    }
  }

  @Override
  public void deliverShortMessage(String serviceType,
                                  TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
                                  String sourceAddr, TypeOfNumber destAddrTon,
                                  NumberingPlanIndicator destAddrNpi, String destinationAddr,
                                  ESMClass esmClass, byte protocolId, byte priorityFlag,
                                  RegisteredDelivery registeredDelivery, DataCoding dataCoding,
                                  byte[] shortMessage, OptionalParameter... optionalParameters)
      throws PDUException, ResponseTimeoutException,
      InvalidResponseException, NegativeResponseException, IOException {

    ensureReceivable(DeliverSmCommandTask.COMMAND_NAME_DELIVER_SM);

    DeliverSmCommandTask task = new DeliverSmCommandTask(pduSender(),
        serviceType, sourceAddrTon, sourceAddrNpi, sourceAddr,
        destAddrTon, destAddrNpi, destinationAddr, esmClass, protocolId,
        priorityFlag, registeredDelivery, dataCoding, shortMessage,
        optionalParameters);

    executeSendCommand(task, getTransactionTimer());
  }

  @Override
  public MessageReceiverListener getMessageReceiverListener() {
    return messageReceiverListener;
  }

  @Override
  public void setMessageReceiverListener(
      MessageReceiverListener messageReceiverListener) {
    this.messageReceiverListener = messageReceiverListener;
  }

  @Override
  protected Connection connection() {
    return conn;
  }

  @Override
  protected AbstractSessionContext sessionContext() {
    return sessionContext;
  }

  @Override
  protected GenericMessageReceiverListener messageReceiverListener() {
    return messageReceiverListener;
  }

  @Override
  protected void finalize() throws Throwable {
    close();
    super.finalize();
  }

  private class ResponseHandlerImpl implements OutboundResponseHandler {

    public void sendBindResp(String systemId, InterfaceVersion interfaceVersion, BindType bindType, int sequenceNumber)
        throws IOException {
      sessionContext.bound(bindType, interfaceVersion);
      try {
        pduSender().sendBindResp(out, bindType.responseCommandId(), sequenceNumber, systemId, interfaceVersion);
      } catch (PDUStringException e) {
        log.error("Failed sending bind response", e);
        // TODO uudashr: we have double checking when accept the bind request
      }
    }

    public DataSmResult processDataSm(DataSm dataSm)
        throws ProcessRequestException {
      try {
        return fireAcceptDataSm(dataSm);
      } catch (ProcessRequestException e) {
        throw e;
      } catch (Exception e) {
        String msg = "Invalid runtime exception thrown when processing data_sm";
        log.error(msg, e);
        throw new ProcessRequestException(msg, SMPPConstant.STAT_ESME_RX_T_APPN);
      }
    }

    @Override
    public void sendDataSmResp(DataSmResult dataSmResult, int sequenceNumber)
        throws IOException {
      try {
        pduSender().sendDataSmResp(out, sequenceNumber,
            dataSmResult.getMessageId(),
            dataSmResult.getOptionalParameters());
      } catch (PDUStringException e) {
        /*
         * There should be no PDUStringException thrown since creation
         * of MessageId should be safe.
         */
        log.error("Failed sending data_sm_resp", e);
      }
    }

    @Override
    public PendingResponse<Command> removeSentItem(int sequenceNumber) {
      return removePendingResponse(sequenceNumber);
    }

    @Override
    public void notifyUnbonded() {
      sessionContext.unbound();
    }

    @Override
    public void sendDeliverSmResp(int commandStatus, int sequenceNumber, String messageId) throws IOException {
      pduSender().sendDeliverSmResp(out, commandStatus, sequenceNumber, messageId);
    }

    @Override
    public void sendEnquireLinkResp(int sequenceNumber) throws IOException {
      pduSender().sendEnquireLinkResp(out, sequenceNumber);
    }

    @Override
    public void sendGenericNack(int commandStatus, int sequenceNumber) throws IOException {
      pduSender().sendGenericNack(out, commandStatus, sequenceNumber);
    }

    @Override
    public void sendNegativeResponse(int originalCommandId, int commandStatus, int sequenceNumber) throws IOException {
      pduSender().sendHeader(out, originalCommandId | SMPPConstant.MASK_CID_RESP, commandStatus, sequenceNumber);
    }

    @Override
    public void sendUnbindResp(int sequenceNumber) throws IOException {
      pduSender().sendUnbindResp(out, SMPPConstant.STAT_ESME_ROK, sequenceNumber);
    }

    @Override
    public void processBind(Bind bind) {
      SMPPOutboundSession.this.bindRequestReceiver.notifyAcceptBind(bind);
    }

    @Override
    public void processEnquireLink(final EnquireLink enquireLink) {

    }
  }

  /**
   * Worker to read the PDU.
   *
   * @author uudashr
   */
  private class PDUReaderWorker extends Thread {
    // start with serial execution of pdu processing, when the session is bound the pool will be enlarged up to the PduProcessorDegree
    private ThreadPoolExecutor pduExecutor;
    private LinkedBlockingQueue<Runnable> workQueue;
    private int queueCapacity;
    private Runnable onIOExceptionTask = () -> close();

    private PDUReaderWorker(final int pduProcessorDegree, final int queueCapacity) {
      super("PDUReaderWorker-" + getSessionId());
      this.queueCapacity = queueCapacity;
      workQueue = new LinkedBlockingQueue<>(queueCapacity);
      pduExecutor = new ThreadPoolExecutor(pduProcessorDegree, pduProcessorDegree,
          0L, TimeUnit.MILLISECONDS, workQueue, (runnable, executor) -> {
            log.info("Receiving queue is full, please increasing receive queue capacity, and/or let other side obey the window size");
            Command pduHeader = ((PDUProcessTask) runnable).getPduHeader();
            if ((pduHeader.getCommandId() & SMPPConstant.MASK_CID_RESP) == SMPPConstant.MASK_CID_RESP) {
              try {
                boolean success = executor.getQueue().offer(runnable, 60000, TimeUnit.MILLISECONDS);
                if (!success) {
                  log.warn("Offer to receive queue failed for {}", pduHeader);
                }
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException(e);
              }
            } else {
              throw new QueueMaxException("Receiving queue capacity " + queueCapacity + " exceeded");
            }
          });
    }

    @Override
    public void run() {
      while (isReadPdu()) {
        readPDU();
      }
      close();
      pduExecutor.shutdown();
      try {
        pduExecutor.awaitTermination(getTransactionTimer(), TimeUnit.MILLISECONDS);
      } catch (InterruptedException e) {
        log.warn("Interrupted while waiting for PDU executor pool to finish");
        Thread.currentThread().interrupt();
      }
      log.debug("{} stopped", getName());
    }

    private void readPDU() {
      Command pduHeader = null;
      try {
        pduHeader = pduReader.readPDUHeader(in);
        byte[] pdu = pduReader.readPDU(in, pduHeader);
        /*
         * When the processing PDU is need user interaction via event,
         * the code on event might take non-short time, so we need to
         * process it concurrently.
         */
        PDUProcessOutboundTask task = new PDUProcessOutboundTask(pduHeader, pdu,
            sessionContext, responseHandler,
            sessionContext, onIOExceptionTask);
        pduExecutor.execute(task);
      } catch (QueueMaxException e) {
        log.info("Notify other side to throttle: {} ({} threads active)", e.getMessage(), pduExecutor.getActiveCount());
        try {
          responseHandler.sendNegativeResponse(pduHeader.getCommandId(), SMPPConstant.STAT_ESME_RTHROTTLED, pduHeader.getSequenceNumber());
        } catch (IOException ioe) {
          log.warn("Failed sending negative response: {}", ioe.getMessage());
          close();
        }
      } catch (InvalidCommandLengthException e) {
        log.warn("Received invalid command length: {}", e.getMessage());
        try {
          pduSender().sendGenericNack(out, SMPPConstant.STAT_ESME_RINVCMDLEN, 0);
        } catch (IOException ee) {
          log.warn("Failed sending generic nack", ee);
        }
        unbindAndClose();
      } catch (SocketTimeoutException e) {
        notifyNoActivity();
      } catch (IOException e) {
        log.info("Reading PDU session {} in state {}: {}", getSessionId(), getSessionState(), e.getMessage());
        close();
      } catch (RuntimeException e) {
        log.warn("Runtime error while reading", e);
        close();
      }
    }

    /**
     * Notify for no activity.
     */
    private void notifyNoActivity() {
      SessionState sessionState = sessionContext().getSessionState();
      if ((getInterfaceVersion().compareTo(InterfaceVersion.IF_34) > 0 && sessionState.isNotClosed()) ||
          sessionState.isBound()) {
        log.trace("No activity notified, sending enquire_link");
        enquireLinkSender.enquireLink();
      }
    }

    /*
     * Return an integer between 0 (Idle) and 100 (Congested/Maximum Load). Only used for SMPP 5.0.
     */
    public int getCongestionRatio() {
      return ((80 * pduExecutor.getActiveCount()) / pduExecutor.getMaximumPoolSize()) +
          ((20 * workQueue.size()) / queueCapacity);
    }
  }

  /**
   * Session state listener for internal class use.
   *
   * @author uudashr
   */
  private class BoundSessionStateListener implements SessionStateListener {

    @Override
    public void onStateChange(SessionState newState, SessionState oldState,
                              Session source) {
      if (newState.isBound()) {
        /**
         * We need to set SO_TIMEOUT to session timer so when timeout occurs,
         * a SocketTimeoutException will be raised. When Exception raised we
         * can send an enquireLinkCommand.
         */
        try {
          conn.setSoTimeout(getEnquireLinkTimer());
        } catch (IOException e) {
          log.error("Failed setting so_timeout for enquire link timer", e);
        }

        int pduProcessorDegree = getPduProcessorDegree();
        log.debug("Changing processor degree to {}", pduProcessorDegree);
        pduReaderWorker.pduExecutor.setMaximumPoolSize(pduProcessorDegree);
        pduReaderWorker.pduExecutor.setCorePoolSize(pduProcessorDegree);
      }
    }
  }
}
