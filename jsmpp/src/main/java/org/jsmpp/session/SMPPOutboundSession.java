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
import java.util.concurrent.Executors;
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
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.connection.Connection;
import org.jsmpp.session.connection.ConnectionFactory;
import org.jsmpp.session.connection.socket.SocketConnectionFactory;
import org.jsmpp.util.DefaultComposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is an object that used to communicate with an ESME. It hide
 * all un-needed SMPP operation that might harm if the user code use it such as :
 * <ul>
 * <li>DELIVER_SM_RESP, should be called only as response to DELIVER_SM</li>
 * <li>UNBIND_RESP, should be called only as response to UNBIND_RESP</li>
 * <li>DATA_SM_RESP, should be called only as response to DATA_SM</li>
 * <li>ENQUIRE_LINK_RESP, should be called only as response to ENQUIRE_LINK</li>
 * <li>GENERIC_NACK, should be called only as response to GENERIC_NACK</li>
 * </ul>
 *
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
  private static final Logger logger = LoggerFactory.getLogger(SMPPOutboundSession.class);

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
   * Default constructor of {@link SMPPOutboundSession}. The next action might be
   * connect and bind to a destination message center.
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
    logger.debug("Connect and bind to {} port {}", host, port);
    if (getSessionState() != SessionState.CLOSED) {
      throw new IOException("Session state is not closed");
    }

    conn = connFactory.createConnection(host, port);
    logger.info("Connected to {}", conn.getInetAddress());

    conn.setSoTimeout(getEnquireLinkTimer());

    sessionContext.open();
    try {
      in = new DataInputStream(conn.getInputStream());
      out = conn.getOutputStream();

      pduReaderWorker = new PDUReaderWorker();
      pduReaderWorker.start();
      sendOutbind(outbindParameter.getSystemId(), outbindParameter.getPassword());
    }
    catch (IOException e) {
      logger.error("IO error occurred", e);
      close();
      throw e;
    }

    try {
      BindRequest bindRequest = waitForBind(timeout);

      logger.info("Start enquireLinkSender");
      enquireLinkSender = new EnquireLinkSender();
      enquireLinkSender.start();

      return bindRequest;
    }
    catch (IllegalStateException e) {
      String message = "System error";
      logger.error(message, e);
      close();
      throw new IOException(message + ": " + e.getMessage(), e);
    }
    catch (TimeoutException e) {
      String message = "Waiting bind response take time too long";
      logger.error(message, e);
      throw new IOException(message + ": " + e.getMessage(), e);
    }
  }

  /**
   * Wait for bind request.
   *
   * @param timeout is the timeout.
   * @return the {@link BindRequest}.
   * @throws IllegalStateException if this invocation of this method has been made or invoke when state is not OPEN.
   * @throws TimeoutException      if the timeout has been reach and {@link SMPPServerSession} are no more valid because
   *                               the connection will be close automatically.
   */
  private BindRequest waitForBind(long timeout)
      throws IllegalStateException, TimeoutException {
    SessionState currentSessionState = getSessionState();
    if (currentSessionState.equals(SessionState.OPEN)) {
      try {
        return bindRequestReceiver.waitForRequest(timeout);
      }
      catch (IllegalStateException e) {
        throw new IllegalStateException(
            "Invocation of waitForBind() has been made", e);
      }
      catch (TimeoutException e) {
        close();
        throw e;
      }
    }
    else {
      throw new IllegalStateException(
          "waitForBind() should be invoked on OPEN state, actual state is "
              + currentSessionState);
    }
  }

  public void deliverShortMessage(String serviceType,
                                  TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
                                  String sourceAddr, TypeOfNumber destAddrTon,
                                  NumberingPlanIndicator destAddrNpi, String destinationAddr,
                                  ESMClass esmClass, byte protocoId, byte priorityFlag,
                                  RegisteredDelivery registeredDelivery, DataCoding dataCoding,
                                  byte[] shortMessage, OptionalParameter... optionalParameters)
      throws PDUException, ResponseTimeoutException,
      InvalidResponseException, NegativeResponseException, IOException {

    ensureReceivable("deliverShortMessage");

    DeliverSmCommandTask task = new DeliverSmCommandTask(pduSender(),
        serviceType, sourceAddrTon, sourceAddrNpi, sourceAddr,
        destAddrTon, destAddrNpi, destinationAddr, esmClass, protocoId,
        protocoId, registeredDelivery, dataCoding, shortMessage,
        optionalParameters);

    executeSendCommand(task, getTransactionTimer());
  }

  public MessageReceiverListener getMessageReceiverListener() {
    return messageReceiverListener;
  }

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
      SMPPOutboundSession.this.sessionContext.bound(bindType);
      try {
        pduSender().sendBindResp(SMPPOutboundSession.this.out, bindType.responseCommandId(), sequenceNumber, systemId,
            interfaceVersion);
      }
      catch (PDUStringException e) {
        logger.error("Failed sending bind response", e);
        // TODO uudashr: we have double checking when accept the bind request
      }
    }

    public DataSmResult processDataSm(DataSm dataSm)
        throws ProcessRequestException {
      try {
        return fireAcceptDataSm(dataSm);
      }
      catch (ProcessRequestException e) {
        throw e;
      }
      catch (Exception e) {
        String msg = "Invalid runtime exception thrown when processing data_sm";
        logger.error(msg, e);
        throw new ProcessRequestException(msg, SMPPConstant.STAT_ESME_RX_T_APPN);
      }
    }

    public void sendDataSmResp(DataSmResult dataSmResult, int sequenceNumber)
        throws IOException {
      try {
        pduSender().sendDataSmResp(out, sequenceNumber,
            dataSmResult.getMessageId(),
            dataSmResult.getOptionalParameters());
      }
      catch (PDUStringException e) {
        /*
         * There should be no PDUStringException thrown since creation
         * of MessageId should be save.
         */
        logger.error("SYSTEM ERROR. Failed sending dataSmResp", e);
      }
    }

    public PendingResponse<Command> removeSentItem(int sequenceNumber) {
      return removePendingResponse(sequenceNumber);
    }

    public void notifyUnbonded() {
      sessionContext.unbound();
    }

    @Override
    public void sendDeliverSmResp(int commandStatus, int sequenceNumber, String messageId) throws IOException {
      pduSender().sendDeliverSmResp(out, commandStatus, sequenceNumber, messageId);
      logger.debug("deliver_sm_resp with seq_number {} has been sent", sequenceNumber);
    }

    public void sendEnquireLinkResp(int sequenceNumber) throws IOException {
      logger.debug("Sending enquire_link_resp");
      pduSender().sendEnquireLinkResp(out, sequenceNumber);
    }

    public void sendGenerickNack(int commandStatus, int sequenceNumber) throws IOException {
      pduSender().sendGenericNack(out, commandStatus, sequenceNumber);
    }

    public void sendNegativeResponse(int originalCommandId, int commandStatus, int sequenceNumber) throws IOException {
      pduSender().sendHeader(out, originalCommandId | SMPPConstant.MASK_CID_RESP, commandStatus, sequenceNumber);
    }

    public void sendUnbindResp(int sequenceNumber) throws IOException {
      pduSender().sendUnbindResp(out, SMPPConstant.STAT_ESME_ROK, sequenceNumber);
    }

    public void processBind(Bind bind) {
      SMPPOutboundSession.this.bindRequestReceiver.notifyAcceptBind(bind);
    }
  }

  /**
   * Worker to read the PDU.
   *
   * @author uudashr
   */
  private class PDUReaderWorker extends Thread {
    // start with serial execution of pdu processing, when the session is bound the pool will be enlarge up to the PduProcessorDegree
    private ExecutorService executorService = Executors.newFixedThreadPool(1);
    private Runnable onIOExceptionTask = new Runnable() {
      @Override
      public void run() {
        close();
      }
    };

    private PDUReaderWorker() {
      super("PDUReaderWorker: " + SMPPOutboundSession.this);
    }

    @Override
    public void run() {
      logger.info("Starting PDUReaderWorker");
      while (isReadPdu()) {
        readPDU();
      }
      close();
      executorService.shutdown();
      try {
        executorService.awaitTermination(getTransactionTimer(), TimeUnit.MILLISECONDS);
      }
      catch (InterruptedException e) {
        logger.warn("interrupted while waiting for executor service pool to finish");
        Thread.currentThread().interrupt();
        throw new RuntimeException("Interrupted");
      }
      logger.info("PDUReaderWorker stop");
    }

    private void readPDU() {
      try {
        Command pduHeader = pduReader.readPDUHeader(in);
        byte[] pdu = pduReader.readPDU(in, pduHeader);
                /*
                 * When the processing PDU is need user interaction via event,
                 * the code on event might take non-short time, so we need to
                 * process it concurrently.
                 */
        PDUProcessOutboundTask task = new PDUProcessOutboundTask(pduHeader, pdu,
            sessionContext, responseHandler,
            sessionContext, onIOExceptionTask);
        executorService.execute(task);

      }
      catch (InvalidCommandLengthException e) {
        logger.warn("Receive invalid command length", e);
        try {
          pduSender().sendGenericNack(out, SMPPConstant.STAT_ESME_RINVCMDLEN, 0);
        }
        catch (IOException ee) {
          logger.warn("Failed sending generic nack", ee);
        }
        unbindAndClose();
      }
      catch (SocketTimeoutException e) {
        notifyNoActivity();
      }
      catch (IOException e) {
        logger.warn("IOException while reading:", e);
        close();
      }
      catch (RuntimeException e) {
        logger.warn("RuntimeException:", e);
        unbindAndClose();
      }
    }

    /**
     * Notify for no activity.
     */
    private void notifyNoActivity() {
      logger.debug("No activity notified, sending enquireLink");
      if (sessionContext().getSessionState().isBound()) {
        enquireLinkSender.enquireLink();
      }
    }
  }

  /**
   * Session state listener for internal class use.
   *
   * @author uudashr
   */
  private class BoundSessionStateListener implements SessionStateListener {
    public void onStateChange(SessionState newState, SessionState oldState,
                              Session source) {
      /**
      * We need to set SO_TIMEOUT to sessionTimer so when timeout occur,
	    * a SocketTimeoutException will be raised. When Exception raised we
	    * can send an enquireLinkCommand.
	    */
      if (newState.isBound()) {
        try {
          conn.setSoTimeout(getEnquireLinkTimer());
        }
        catch (IOException e) {
          logger.error("Failed setting so_timeout for session timer", e);
        }

        logger.info("Changing processor degree to {}", getPduProcessorDegree());
        ((ThreadPoolExecutor) pduReaderWorker.executorService).setMaximumPoolSize(getPduProcessorDegree());
        ((ThreadPoolExecutor) pduReaderWorker.executorService).setCorePoolSize(getPduProcessorDegree());
      }
    }
  }
}
