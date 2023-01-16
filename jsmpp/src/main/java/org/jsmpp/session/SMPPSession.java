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
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
import org.jsmpp.bean.Address;
import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.BindResp;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.EnquireLink;
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.OptionalParameter.Sc_interface_version;
import org.jsmpp.bean.QuerySmResp;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.ReplaceIfPresentFlag;
import org.jsmpp.bean.SubmitMultiResp;
import org.jsmpp.bean.SubmitSmResp;
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
import org.jsmpp.util.HexUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * This is an object that used to communicate with SMPP Server or SMSC. It hides all un-needed SMPP operation that might harm if the user code use it such as :
 * <ul>
 * <li>DELIVER_SM_RESP, should be called only as response to DELIVER_SM</li>
 * <li>UNBIND_RESP, should be called only as response to UNBIND_RESP</li>
 * <li>DATA_SM_RESP, should be called only as response to DATA_SM</li>
 * <li>ENQUIRE_LINK_RESP, should be called only as response to ENQUIRE_LINK</li>
 * <li>GENERIC_NACK, should be called only as response to GENERIC_NACK</li>
 * </ul>
 * <p>
 * All SMPP operations (request-response) are blocking, for an example: SUBMIT_SM
 * will be blocked until SUBMIT_SM_RESP received or timeout. This looks like
 * synchronous communication, but the {@link SMPPSession} implementation give
 * ability to the asynchronous way by executing the SUBMIT_SM operation parallel
 * on a different thread. The very simple implementation by using Thread pool,
 * {@link ExecutorService} will do.
 * <p>
 * To receive the incoming message such as DELIVER_SM or DATA_SM will be managed
 * by internal thread. User code only have to set listener
 * {@link MessageReceiverListener}.
 *
 * @author uudashr
 */
public class SMPPSession extends AbstractSession implements ClientSession {
  private static final Logger log = LoggerFactory.getLogger(SMPPSession.class);
  private static final String MESSAGE_RECEIVER_LISTENER_IS_NULL = "Received {} but message receiver listener is null";

  /* Utility */
  private final PDUReader pduReader;

  /* Connection */
  private final ConnectionFactory connFactory;
  private final ResponseHandler responseHandler = new ResponseHandlerImpl();
  private Connection conn;
  private DataInputStream in;
  private OutputStream out;
  private PDUReaderWorker pduReaderWorker;
  private MessageReceiverListener messageReceiverListener;
  private BoundSessionStateListener sessionStateListener = new BoundSessionStateListener();
  private SMPPSessionContext sessionContext = new SMPPSessionContext(this, sessionStateListener);

  /**
   * Default constructor of {@link SMPPSession}. The next action might be, connect and bind to a destination message center.
   *
   * @see #connectAndBind(String, int, BindType, String, String, String, TypeOfNumber, NumberingPlanIndicator, String)
   */
  public SMPPSession() {
    this(new SynchronizedPDUSender(new DefaultPDUSender(new DefaultComposer())),
        new DefaultPDUReader(),
        SocketConnectionFactory.getInstance());
  }

  public SMPPSession(ConnectionFactory connFactory) {
    this(new SynchronizedPDUSender(new DefaultPDUSender(new DefaultComposer())),
        new DefaultPDUReader(),
        connFactory);
  }

  public SMPPSession(PDUSender pduSender, PDUReader pduReader,
                     ConnectionFactory connFactory) {
    super(pduSender);
    this.pduReader = pduReader;
    this.connFactory = connFactory;
  }

  public SMPPSession(String host, int port, BindParameter bindParam,
                     PDUSender pduSender, PDUReader pduReader,
                     ConnectionFactory connFactory) throws IOException {
    this(pduSender, pduReader, connFactory);
    connectAndBind(host, port, bindParam);
  }

  public SMPPSession(String host, int port, BindParameter bindParam) throws IOException {
    this();
    connectAndBind(host, port, bindParam);
  }

  public int getLocalPort() {
    return conn.getLocalPort();
  }

  /**
   * Open connection and bind immediately.
   *
   * @param host         is the SMSC host address.
   * @param port         is the SMSC listen port.
   * @param bindType     is the bind type.
   * @param systemId     is the system id.
   * @param password     is the password.
   * @param systemType   is the system type.
   * @param addrTon      is the address TON.
   * @param addrNpi      is the address NPI.
   * @param addressRange is the address range.
   * @throws IOException if there is an IO error found.
   */
  @Override
  public void connectAndBind(String host, int port, BindType bindType,
                             String systemId, String password, String systemType,
                             TypeOfNumber addrTon, NumberingPlanIndicator addrNpi,
                             String addressRange) throws IOException {
    connectAndBind(host, port, new BindParameter(bindType, systemId,
        password, systemType, addrTon, addrNpi, addressRange), 60000);
  }

  /**
   * Open connection and bind immediately with specified timeout.
   *
   * @param host         is the SMSC host address.
   * @param port         is the SMSC listen port.
   * @param bindType     is the bind type.
   * @param systemId     is the system id.
   * @param password     is the password.
   * @param systemType   is the system type.
   * @param addrTon      is the address TON.
   * @param addrNpi      is the address NPI.
   * @param addressRange is the address range.
   * @param timeout      is the timeout.
   * @throws IOException if there is an IO error found.
   */
  @Override
  public void connectAndBind(String host, int port, BindType bindType,
                             String systemId, String password, String systemType,
                             TypeOfNumber addrTon, NumberingPlanIndicator addrNpi,
                             String addressRange, long timeout) throws IOException {
    connectAndBind(host, port, new BindParameter(bindType, systemId,
        password, systemType, addrTon, addrNpi, addressRange), timeout);
  }

  /**
   * Open connection and bind immediately with timeout of 1 minute.
   *
   * @param host      is the SMSC host address.
   * @param port      is the SMSC listen port.
   * @param bindParam is the bind parameters.
   * @return the SMSC system id.
   * @throws IOException if there is an IO error found.
   */
  @Override
  public String connectAndBind(String host, int port,
                               BindParameter bindParam)
      throws IOException {
    return connectAndBind(host, port, bindParam, 60000);
  }

  /**
   * Open connection.
   *
   * @param host is the SMSC host address.
   * @param port is the SMSC listen port.
   * @throws IOException if there is an IO error found.
   */
  public void connect(String host, int port)
      throws IOException {
    log.debug("Connect to {} port {}", host, port);
    if (getSessionState().isNotClosed()) {
      throw new IOException("Session state is not closed");
    }

    conn = connFactory.createConnection(host, port);
    log.info("Connected to {}", conn.getInetAddress());

    conn.setSoTimeout(getEnquireLinkTimer());

    sessionContext.open();
    in = new DataInputStream(conn.getInputStream());
    out = conn.getOutputStream();

    pduReaderWorker = new PDUReaderWorker(getQueueCapacity());
    pduReaderWorker.start();

    enquireLinkSender = new EnquireLinkSender();
    enquireLinkSender.start();
  }

  /**
   * Open connection and bind immediately.
   *
   * @param host      is the SMSC host address.
   * @param port      is the SMSC listen port.
   * @param bindParam is the bind parameters.
   * @param timeout   is the timeout.
   * @return the SMSC system id.
   * @throws IOException if there is an IO error found.
   */
  @Override
  public String connectAndBind(String host, int port,
                               BindParameter bindParam, long timeout)
      throws IOException {
    log.debug("Connect and bind to {} port {}", host, port);
    if (getSessionState() != SessionState.CLOSED) {
      throw new IOException("Session state is not closed");
    }

    conn = connFactory.createConnection(host, port);
    log.info("Connected from port {} to {}:{}", conn.getLocalPort(), conn.getInetAddress(), conn.getPort());

    conn.setSoTimeout(getEnquireLinkTimer());

    sessionContext.open();
    try {
      in = new DataInputStream(conn.getInputStream());
      out = conn.getOutputStream();

      pduReaderWorker = new PDUReaderWorker(getQueueCapacity());
      pduReaderWorker.start();
      String smscSystemId = sendBind(bindParam.getBindType(), bindParam.getSystemId(), bindParam.getPassword(), bindParam.getSystemType(),
          bindParam.getInterfaceVersion(), bindParam.getAddrTon(), bindParam.getAddrNpi(), bindParam.getAddressRange(), timeout);
      sessionContext.bound(bindParam.getBindType(), bindParam.getInterfaceVersion());

      enquireLinkSender = new EnquireLinkSender();
      enquireLinkSender.start();
      return smscSystemId;
    } catch (PDUException e) {
      log.error("Failed sending bind command", e);
      throw new IOException("Failed sending bind since some string parameter area invalid: " + e.getMessage(), e);
    } catch (NegativeResponseException e) {
      String message = "Receive negative bind response";
      log.error(message, e);
      close();
      throw new IOException(message + ": " + e.getMessage(), e);
    } catch (InvalidResponseException e) {
      String message = "Receive invalid bind response";
      log.error(message, e);
      close();
      throw new IOException(message + ": " + e.getMessage(), e);
    } catch (ResponseTimeoutException e) {
      String message = "Time out waiting for bind response";
      log.error(message, e);
      close();
      throw new IOException(message + ": " + e.getMessage(), e);
    } catch (IOException e) {
      log.error("I/O error occurred", e);
      close();
      throw e;
    }
  }

  /**
   * Sending bind.
   *
   * @param bindType         is the bind type.
   * @param systemId         is the system id.
   * @param password         is the password.
   * @param systemType       is the system type.
   * @param interfaceVersion is the interface version.
   * @param addrTon          is the address TON.
   * @param addrNpi          is the address NPI.
   * @param addressRange     is the address range.
   * @param timeout          is the max time waiting for bind response.
   * @return SMSC system id.
   * @throws PDUException              if we enter invalid bind parameter(s).
   * @throws ResponseTimeoutException  if there is no valid response after defined millisecond.
   * @throws InvalidResponseException  if there is invalid response found.
   * @throws NegativeResponseException if we receive negative response.
   * @throws IOException               if there is an IO error occur.
   */
  private String sendBind(BindType bindType, String systemId,
                          String password, String systemType,
                          InterfaceVersion interfaceVersion, TypeOfNumber addrTon,
                          NumberingPlanIndicator addrNpi, String addressRange, long timeout)
      throws PDUException, ResponseTimeoutException,
      InvalidResponseException, NegativeResponseException, IOException {

    BindCommandTask task = new BindCommandTask(pduSender(), bindType,
        systemId, password, systemType, interfaceVersion, addrTon,
        addrNpi, addressRange);

    BindResp resp = (BindResp) executeSendCommand(task, timeout);
    OptionalParameter.Sc_interface_version scVersion = resp.getOptionalParameter(Sc_interface_version.class);
    if (scVersion != null) {
      log.info("Other side reports SMPP interface version {}", scVersion);
    }
    InterfaceVersion commonInterfaceVersion = scVersion != null ?
        InterfaceVersion.IF_50.min(InterfaceVersion.valueOf(scVersion.getValue())) :
        InterfaceVersion.IF_34;

    sessionContext.bound(bindType, commonInterfaceVersion);

    return resp.getSystemId();
  }

  /* (non-Javadoc)
   * @see org.jsmpp.session.ClientSession#submitShortMessage(java.lang.String, org.jsmpp.bean.TypeOfNumber, org.jsmpp.bean.NumberingPlanIndicator, java.lang.String, org.jsmpp.bean.TypeOfNumber, org.jsmpp.bean.NumberingPlanIndicator, java.lang.String, org.jsmpp.bean.ESMClass, byte, byte, java.lang.String, java.lang.String, org.jsmpp.bean.RegisteredDelivery, byte, org.jsmpp.bean.DataCoding, byte, byte[], org.jsmpp.bean.OptionalParameter[])
   */
  @Override
  public SubmitSmResult submitShortMessage(String serviceType,
                                           TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
                                           String sourceAddr, TypeOfNumber destAddrTon,
                                           NumberingPlanIndicator destAddrNpi, String destinationAddr,
                                           ESMClass esmClass, byte protocolId, byte priorityFlag,
                                           String scheduleDeliveryTime, String validityPeriod,
                                           RegisteredDelivery registeredDelivery, byte replaceIfPresentFlag,
                                           DataCoding dataCoding, byte smDefaultMsgId, byte[] shortMessage,
                                           OptionalParameter... optionalParameters) throws PDUException,
      ResponseTimeoutException, InvalidResponseException,
      NegativeResponseException, IOException {

    ensureTransmittable(SubmitSmCommandTask.COMMAND_NAME_SUBMIT_SM);

    SubmitSmCommandTask submitSmTask = new SubmitSmCommandTask(
        pduSender(), serviceType, sourceAddrTon, sourceAddrNpi,
        sourceAddr, destAddrTon, destAddrNpi, destinationAddr,
        esmClass, protocolId, priorityFlag, scheduleDeliveryTime,
        validityPeriod, registeredDelivery, replaceIfPresentFlag,
        dataCoding, smDefaultMsgId, shortMessage, optionalParameters);

    SubmitSmResp resp = (SubmitSmResp) executeSendCommand(submitSmTask, getTransactionTimer());

    return new SubmitSmResult(resp.getMessageId(), resp.getOptionalParameters());
  }

  /* (non-Javadoc)
   * @see org.jsmpp.session.ClientSession#submitMultiple(java.lang.String, org.jsmpp.bean.TypeOfNumber, org.jsmpp.bean.NumberingPlanIndicator, java.lang.String, org.jsmpp.bean.Address[], org.jsmpp.bean.ESMClass, byte, byte, java.lang.String, java.lang.String, org.jsmpp.bean.RegisteredDelivery, org.jsmpp.bean.ReplaceIfPresentFlag, org.jsmpp.bean.DataCoding, byte, byte[], org.jsmpp.bean.OptionalParameter[])
   */
  @Override
  public SubmitMultiResult submitMultiple(String serviceType,
                                          TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
                                          String sourceAddr, Address[] destinationAddresses,
                                          ESMClass esmClass, byte protocolId, byte priorityFlag,
                                          String scheduleDeliveryTime, String validityPeriod,
                                          RegisteredDelivery registeredDelivery,
                                          ReplaceIfPresentFlag replaceIfPresentFlag, DataCoding dataCoding,
                                          byte smDefaultMsgId, byte[] shortMessage,
                                          OptionalParameter... optionalParameters) throws PDUException,
      ResponseTimeoutException, InvalidResponseException,
      NegativeResponseException, IOException {

    ensureTransmittable(SubmitMultiCommandTask.COMMAND_NAME_SUBMIT_MULTI);

    SubmitMultiCommandTask task = new SubmitMultiCommandTask(pduSender(),
        serviceType, sourceAddrTon, sourceAddrNpi, sourceAddr,
        destinationAddresses, esmClass, protocolId, priorityFlag,
        scheduleDeliveryTime, validityPeriod, registeredDelivery,
        replaceIfPresentFlag, dataCoding, smDefaultMsgId, shortMessage,
        optionalParameters);

    SubmitMultiResp resp = (SubmitMultiResp) executeSendCommand(task,
        getTransactionTimer());

    return new SubmitMultiResult(resp.getMessageId(), resp.getUnsuccessSmes(), resp.getOptionalParameters());
  }

  /* (non-Javadoc)
   * @see org.jsmpp.session.ClientSession#queryShortMessage(java.lang.String, org.jsmpp.bean.TypeOfNumber, org.jsmpp.bean.NumberingPlanIndicator, java.lang.String)
   */
  @Override
  public QuerySmResult queryShortMessage(String messageId,
                                         TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
                                         String sourceAddr) throws PDUException, ResponseTimeoutException,
      InvalidResponseException, NegativeResponseException, IOException {

    ensureTransmittable(QuerySmCommandTask.COMMAND_NAME_QUERY_SM);

    QuerySmCommandTask task = new QuerySmCommandTask(pduSender(),
        messageId, sourceAddrTon, sourceAddrNpi, sourceAddr);

    QuerySmResp resp = (QuerySmResp) executeSendCommand(task,
        getTransactionTimer());

    if (resp.getMessageId() != null && resp.getMessageId().equals(messageId)) {
      return new QuerySmResult(resp.getFinalDate(), resp
          .getMessageState(), resp.getErrorCode());
    } else {
      // message id requested not the same as the returned
      throw new InvalidResponseException(
          "Requested message_id doesn't match with the result");
    }
  }

  /* (non-Javadoc)
   * @see org.jsmpp.session.ClientSession#replaceShortMessage(java.lang.String, org.jsmpp.bean.TypeOfNumber, org.jsmpp.bean.NumberingPlanIndicator, java.lang.String, java.lang.String, java.lang.String, org.jsmpp.bean.RegisteredDelivery, byte, byte[])
   */
  @Override
  public void replaceShortMessage(String messageId,
                                  TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
                                  String sourceAddr, String scheduleDeliveryTime,
                                  String validityPeriod, RegisteredDelivery registeredDelivery,
                                  byte smDefaultMsgId, byte[] shortMessage) throws PDUException,
      ResponseTimeoutException, InvalidResponseException, NegativeResponseException, IOException {

    ensureTransmittable(ReplaceSmCommandTask.COMMAND_NAME_REPLACE_SM);

    ReplaceSmCommandTask replaceSmTask = new ReplaceSmCommandTask(
        pduSender(), messageId, sourceAddrTon, sourceAddrNpi,
        sourceAddr, scheduleDeliveryTime, validityPeriod,
        registeredDelivery, smDefaultMsgId, shortMessage);

    executeSendCommand(replaceSmTask, getTransactionTimer());
  }

  /* (non-Javadoc)
   * @see org.jsmpp.session.ClientSession#cancelShortMessage(java.lang.String, java.lang.String, org.jsmpp.bean.TypeOfNumber, org.jsmpp.bean.NumberingPlanIndicator, java.lang.String, org.jsmpp.bean.TypeOfNumber, org.jsmpp.bean.NumberingPlanIndicator, java.lang.String)
   */
  @Override
  public void cancelShortMessage(String serviceType, String messageId,
                                 TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
                                 String sourceAddr, TypeOfNumber destAddrTon,
                                 NumberingPlanIndicator destAddrNpi, String destinationAddress)
      throws PDUException, ResponseTimeoutException,
      InvalidResponseException, NegativeResponseException, IOException {

    ensureTransmittable(CancelSmCommandTask.COMMAND_NAME_CANCEL_SM);

    CancelSmCommandTask task = new CancelSmCommandTask(pduSender(),
        serviceType, messageId, sourceAddrTon, sourceAddrNpi,
        sourceAddr, destAddrTon, destAddrNpi, destinationAddress);

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

  private void fireAcceptDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {
    if (messageReceiverListener != null) {
      messageReceiverListener.onAcceptDeliverSm(deliverSm);
    } else {
      log.warn("Received deliver_sm but message receiver listener is null. Short message = {}",
          new String(deliverSm.getShortMessage(), StandardCharsets.ISO_8859_1));
      throw new ProcessRequestException("No message receiver listener registered", SMPPConstant.STAT_ESME_RX_T_APPN);
    }
  }

  private void fireAcceptAlertNotification(AlertNotification alertNotification) {
    if (messageReceiverListener != null) {
      messageReceiverListener.onAcceptAlertNotification(alertNotification);
    } else {
      log.warn(MESSAGE_RECEIVER_LISTENER_IS_NULL, "alert_notification");
    }
  }

  private class ResponseHandlerImpl implements ResponseHandler {

    @Override
    public void processEnquireLink(EnquireLink enquireLink) {
      try {
        fireAcceptEnquirelink(enquireLink);
      } catch (Exception e) {
        log.error("Invalid runtime exception thrown when processing enquire_link", e);
      }
    }

    @Override
    public void processDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {
      try {
        fireAcceptDeliverSm(deliverSm);
      } catch (ProcessRequestException e) {
        throw e;
      } catch (Exception e) {
        String msg = "Invalid runtime exception thrown when processing deliver_sm";
        log.error(msg, e);
        throw new ProcessRequestException(msg, SMPPConstant.STAT_ESME_RX_T_APPN);
      }
    }

    @Override
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
    public void processAlertNotification(AlertNotification alertNotification) {
      try {
        fireAcceptAlertNotification(alertNotification);
      } catch (Exception e) {
        log.error("Invalid runtime exception thrown when processing alert_notification", e);
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
      log.debug("Sending deliver_sm_resp with sequence_number {}", sequenceNumber);
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
      log.info("Send unbind {}", sequenceNumber);
      pduSender().sendUnbindResp(out, SMPPConstant.STAT_ESME_ROK, sequenceNumber);
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

    private PDUReaderWorker(final int queueCapacity) {
      super("PDUReaderWorker-" + getSessionId());
      this.queueCapacity = queueCapacity;
      workQueue = new LinkedBlockingQueue<>(queueCapacity);
      pduExecutor = new ThreadPoolExecutor(1, 1,
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
      log.info("Starting PDUReaderWorker");
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
      log.debug("{} stopped", this.getName());
    }

    private void readPDU() {
      Command pduHeader = null;
      try {
        pduHeader = pduReader.readPDUHeader(in);
        byte[] pdu = pduReader.readPDU(in, pduHeader);
        log.debug("Received PDU in session {} in state {}: {}", getSessionId(), getSessionState(), HexUtil.convertBytesToHexString(pdu));

        /*
         * When the processing PDU is need user interaction via event,
         * the code on event might take non-short time, so we need to
         * process it concurrently.
         */
        PDUProcessTask task = new PDUProcessTask(pduHeader, pdu,
            sessionContext, responseHandler,
            sessionContext, onIOExceptionTask);
        pduExecutor.execute(task);
      } catch (QueueMaxException e) {
        log.info("Notify other side to throttle: {} ({} threads active)", e.getMessage(), pduExecutor.getActiveCount());
        try {
          responseHandler.sendNegativeResponse(pduHeader.getCommandId(), SMPPConstant.STAT_ESME_RTHROTTLED, pduHeader.getSequenceNumber());
        } catch (IOException ioe) {
          log.warn("Failed sending negative resp: {}", ioe.getMessage());
          close();
        }
      } catch (InvalidCommandLengthException e) {
        log.warn("Received invalid command length: {}", e.getMessage());
        try {
          pduSender().sendGenericNack(out, SMPPConstant.STAT_ESME_RINVCMDLEN, 0);
        } catch (IOException ee) {
          log.warn("Failed sending generic_nack", ee);
        }
        unbindAndClose();
      } catch (SocketTimeoutException e) {
        notifyNoActivity();
      } catch (IOException e) {
        log.info("Reading PDU session {} in state {}: {}", getSessionId(), getSessionState(), e.getMessage());
        close();
      } catch (RuntimeException e) {
        log.warn("Runtime error while reading PDU", e);
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
      /*
       * We need to set SO_TIMEOUT to sessionTimer so when timeout occur,
       * a SocketTimeoutException will be raised. When Exception raised we
       * can send an enquireLinkCommand.
       */
      if (newState.equals(SessionState.OPEN)) {
        try {
          conn.setSoTimeout(getEnquireLinkTimer());
        } catch (IOException e) {
          log.error("Failed setting so_timeout for session timer", e);
        }
      }
      if (newState.isBound()) {
        int pduProcessorDegree = getPduProcessorDegree();
        log.debug("Changing processor degree to {}", pduProcessorDegree);
        pduReaderWorker.pduExecutor.setMaximumPoolSize(pduProcessorDegree);
        pduReaderWorker.pduExecutor.setCorePoolSize(pduProcessorDegree);
      }
    }
  }
}
