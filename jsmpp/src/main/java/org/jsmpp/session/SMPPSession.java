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
import org.jsmpp.bean.InterfaceVersion;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.OptionalParameter.Sc_interface_version;
import org.jsmpp.bean.QuerySmResp;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.ReplaceIfPresentFlag;
import org.jsmpp.bean.SubmitMultiResp;
import org.jsmpp.bean.SubmitMultiResult;
import org.jsmpp.bean.SubmitSmResp;
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
 * This is an object that used to communicate with SMPP Server or SMSC. It hide
 * all un-needed SMPP operation that might harm if the user code use it such as :
 * <ul>
 * <li>DELIVER_SM_RESP, should be called only as response to DELIVER_SM</li>
 * <li>UNBIND_RESP, should be called only as response to UNBIND_RESP</li>
 * <li>DATA_SM_RESP, should be called only as response to DATA_SM</li>
 * <li>ENQUIRE_LINK_RESP, should be called only as response to ENQUIRE_LINK</li>
 * <li>GENERIC_NACK, should be called only as response to GENERIC_NACK</li>
 * </ul>
 * 
 * All SMPP operation (request-response) is blocking, for an example: SUBMIT_SM
 * will be blocked until SUBMIT_SM_RESP received or timeout. This looks like
 * synchronous communication, but the {@link SMPPClient} implementation give
 * ability to the asynchronous way by executing the SUBMIT_SM operation parallel
 * on a different thread. The very simple implementation by using Thread pool,
 * {@link ExecutorService} will do.
 * 
 * To receive the incoming message such as DELIVER_SM or DATA_SM will be managed
 * by internal thread. User code only have to set listener
 * {@link MessageReceiverListener}.
 * 
 * @author uudashr
 * 
 */
public class SMPPSession extends AbstractSession implements ClientSession {
	private static final Logger logger = LoggerFactory.getLogger(SMPPSession.class);

	/* Utility */
    private final PDUReader pduReader;
	
    /* Connection */
	private final ConnectionFactory connFactory;
	private Connection conn;
	private DataInputStream in;
	private OutputStream out;
	
	private PDUReaderWorker pduReaderWorker;
	private final ResponseHandler responseHandler = new ResponseHandlerImpl();
	private MessageReceiverListener messageReceiverListener;
    private BoundSessionStateListener sessionStateListener = new BoundSessionStateListener();
    private SMPPSessionContext sessionContext = new SMPPSessionContext(this, sessionStateListener);
	
	/**
     * Default constructor of {@link SMPPSession}. The next action might be
     * connect and bind to a destination message center.
     * 
     * @see #connectAndBind(String, int, BindType, String, String, String, TypeOfNumber, NumberingPlanIndicator, String)
     */
	public SMPPSession() {
        this(new SynchronizedPDUSender(new DefaultPDUSender(new DefaultComposer())), 
                new DefaultPDUReader(), 
                SocketConnectionFactory.getInstance());
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
	
	/**
     * Open connection and bind immediately.
     * 
     * @param host is the SMSC host address.
     * @param port is the SMSC listen port.
     * @param bindType is the bind type.
     * @param systemId is the system id.
     * @param password is the password.
     * @param systemType is the system type.
     * @param addrTon is the address TON.
     * @param addrNpi is the address NPI.
     * @param addressRange is the address range.
     * @throws IOException if there is an IO error found.
     */
    public void connectAndBind(String host, int port, BindType bindType,
            String systemId, String password, String systemType,
            TypeOfNumber addrTon, NumberingPlanIndicator addrNpi,
            String addressRange) throws IOException {
        connectAndBind(host, port, new BindParameter(bindType, systemId,
                password, systemType, addrTon, addrNpi, addressRange), 60000);
    }
	
	/**
     * Open connection and bind immediately with specified timeout. The default
     * timeout is 1 minutes.
     * 
     * @param host is the SMSC host address.
     * @param port is the SMSC listen port.
     * @param bindType is the bind type.
     * @param systemId is the system id.
     * @param password is the password.
     * @param systemType is the system type.
     * @param addrTon is the address TON.
     * @param addrNpi is the address NPI.
     * @param addressRange is the address range.
     * @param timeout is the timeout.
     * @throws IOException if there is an IO error found.
     */
	public void connectAndBind(String host, int port, BindType bindType,
            String systemId, String password, String systemType,
            TypeOfNumber addrTon, NumberingPlanIndicator addrNpi,
            String addressRange, long timeout) throws IOException {
	    connectAndBind(host, port, new BindParameter(bindType, systemId,
                password, systemType, addrTon, addrNpi, addressRange), timeout);
	}
	
	/**
     * Open connection and bind immediately.
     * 
     * @param host is the SMSC host address.
     * @param port is the SMSC listen port.
     * @param bindParam is the bind parameters.
     * @return the SMSC system id.
     * @throws IOException if there is an IO error found.
     */
    public String connectAndBind(String host, int port,
            BindParameter bindParam) 
            throws IOException {
        return connectAndBind(host, port, bindParam, 60000);
    }
	
	/**
	 * Open connection and bind immediately.
	 * 
	 * @param host is the SMSC host address.
	 * @param port is the SMSC listen port.
	 * @param bindParam is the bind parameters.
	 * @param timeout is the timeout.
	 * @return the SMSC system id.
	 * @throws IOException if there is an IO error found.
	 */
	public String connectAndBind(String host, int port,
            BindParameter bindParam, long timeout) 
	        throws IOException {
	    logger.debug("Connect and bind to {} port {}", host, port);
		if (sequence().currentValue() != 1) {
			throw new IOException("Failed connecting");
		}
		
		conn = connFactory.createConnection(host, port);
		logger.info("Connected");
		
		conn.setSoTimeout(getEnquireLinkTimer());
		
		sessionContext.open();
		try {
			in = new DataInputStream(conn.getInputStream());
			out = conn.getOutputStream();
			
			pduReaderWorker = new PDUReaderWorker();
			pduReaderWorker.start();
			String smscSystemId = sendBind(bindParam.getBindType(), bindParam.getSystemId(), bindParam.getPassword(), bindParam.getSystemType(),
                    bindParam.getInterfaceVersion(), bindParam.getAddrTon(), bindParam.getAddrNpi(), bindParam.getAddressRange(), timeout);
			sessionContext.bound(bindParam.getBindType());
			
			enquireLinkSender = new EnquireLinkSender();
			enquireLinkSender.start();
			return smscSystemId;
		} catch (PDUException e) {
		    logger.error("Failed sending bind command", e);
		    throw new IOException("Failed sending bind since some string parameter area invalid : " + e.getMessage(), e);
		} catch (NegativeResponseException e) {
			String message = "Receive negative bind response";
			logger.error(message, e);
			close();
			throw new IOException(message + ": " + e.getMessage(), e);
		} catch (InvalidResponseException e) {
			String message = "Receive invalid response of bind";
			logger.error(message, e);
			close();
			throw new IOException(message + ": " + e.getMessage(), e);
		} catch (ResponseTimeoutException e) {
			String message = "Waiting bind response take time to long";
			logger.error(message, e);
			close();
			throw new IOException(message + ": " + e.getMessage(), e);
		} catch (IOException e) {
			logger.error("IO Error occur", e);
			close();
			throw e;
		}
	}
	
	/**
	 * Sending bind.
	 * 
	 * @param bindType is the bind type.
	 * @param systemId is the system id.
	 * @param password is the password.
	 * @param systemTypeis the system type.
	 * @param interfaceVersion is the interface version.
	 * @param addrTon is the address TON.
	 * @param addrNpi is the address NPI.
	 * @param addressRange is the address range.
	 * @param timeout is the max time waiting for bind response. 
	 * @return SMSC system id.
	 * @throws PDUException if we enter invalid bind parameter(s).
	 * @throws ResponseTimeoutException if there is no valid response after defined millisecond.
	 * @throws InvalidResponseException if there is invalid response found.
	 * @throws NegativeResponseException if we receive negative response.
	 * @throws IOException if there is an IO error occur.
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
	    
	    BindResp resp = (BindResp)executeSendCommand(task, timeout);
	    OptionalParameter.Sc_interface_version sc_version = resp.getOptionalParameter(Sc_interface_version.class);
	    if(sc_version != null) {
		    logger.info("Other side reports smpp interface version {}", sc_version);
	    }
        
		return resp.getSystemId();
	}
	
    /* (non-Javadoc)
     * @see org.jsmpp.session.ClientSession#submitShortMessage(java.lang.String, org.jsmpp.bean.TypeOfNumber, org.jsmpp.bean.NumberingPlanIndicator, java.lang.String, org.jsmpp.bean.TypeOfNumber, org.jsmpp.bean.NumberingPlanIndicator, java.lang.String, org.jsmpp.bean.ESMClass, byte, byte, java.lang.String, java.lang.String, org.jsmpp.bean.RegisteredDelivery, byte, org.jsmpp.bean.DataCoding, byte, byte[], org.jsmpp.bean.OptionalParameter[])
     */
    public String submitShortMessage(String serviceType,
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
    	
        ensureTransmittable("submitShortMessage");
    	
        SubmitSmCommandTask submitSmTask = new SubmitSmCommandTask(
                pduSender(), serviceType, sourceAddrTon, sourceAddrNpi,
                sourceAddr, destAddrTon, destAddrNpi, destinationAddr,
                esmClass, protocolId, priorityFlag, scheduleDeliveryTime,
                validityPeriod, registeredDelivery, replaceIfPresentFlag,
                dataCoding, smDefaultMsgId, shortMessage, optionalParameters);
    	
        SubmitSmResp resp = (SubmitSmResp)executeSendCommand(submitSmTask, getTransactionTimer());
    	return resp.getMessageId();
    }
    
    /* (non-Javadoc)
     * @see org.jsmpp.session.ClientSession#submitMultiple(java.lang.String, org.jsmpp.bean.TypeOfNumber, org.jsmpp.bean.NumberingPlanIndicator, java.lang.String, org.jsmpp.bean.Address[], org.jsmpp.bean.ESMClass, byte, byte, java.lang.String, java.lang.String, org.jsmpp.bean.RegisteredDelivery, org.jsmpp.bean.ReplaceIfPresentFlag, org.jsmpp.bean.DataCoding, byte, byte[], org.jsmpp.bean.OptionalParameter[])
     */
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
        
        ensureTransmittable("submitMultiple");
        
        SubmitMultiCommandTask task = new SubmitMultiCommandTask(pduSender(),
                serviceType, sourceAddrTon, sourceAddrNpi, sourceAddr,
                destinationAddresses, esmClass, protocolId, priorityFlag,
                scheduleDeliveryTime, validityPeriod, registeredDelivery,
                replaceIfPresentFlag, dataCoding, smDefaultMsgId, shortMessage,
                optionalParameters);

        SubmitMultiResp resp = (SubmitMultiResp)executeSendCommand(task,
                getTransactionTimer());

        return new SubmitMultiResult(resp.getMessageId(), resp
                .getUnsuccessSmes());
    }
    
    /* (non-Javadoc)
     * @see org.jsmpp.session.ClientSession#queryShortMessage(java.lang.String, org.jsmpp.bean.TypeOfNumber, org.jsmpp.bean.NumberingPlanIndicator, java.lang.String)
     */
    public QuerySmResult queryShortMessage(String messageId,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr) throws PDUException, ResponseTimeoutException,
            InvalidResponseException, NegativeResponseException, IOException {
        
        ensureTransmittable("queryShortMessage");
        
        QuerySmCommandTask task = new QuerySmCommandTask(pduSender(),
                messageId, sourceAddrTon, sourceAddrNpi, sourceAddr);
        
        QuerySmResp resp = (QuerySmResp)executeSendCommand(task,
                getTransactionTimer());

        if (resp.getMessageId().equals(messageId)) {
            return new QuerySmResult(resp.getFinalDate(), resp
                    .getMessageState(), resp.getErrorCode());
        } else {
            // message id requested not same as the returned
            throw new InvalidResponseException(
                    "Requested message_id doesn't match with the result");
        }
    }
    
    /* (non-Javadoc)
     * @see org.jsmpp.session.ClientSession#replaceShortMessage(java.lang.String, org.jsmpp.bean.TypeOfNumber, org.jsmpp.bean.NumberingPlanIndicator, java.lang.String, java.lang.String, java.lang.String, org.jsmpp.bean.RegisteredDelivery, byte, byte[])
     */
    public void replaceShortMessage(String messageId,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, String scheduleDeliveryTime,
            String validityPeriod, RegisteredDelivery registeredDelivery,
            byte smDefaultMsgId, byte[] shortMessage) throws PDUException,
            ResponseTimeoutException, InvalidResponseException,
            NegativeResponseException, IOException {
        
        ensureTransmittable("replaceShortMessage", true);
        
        ReplaceSmCommandTask replaceSmTask = new ReplaceSmCommandTask(
                pduSender(), messageId, sourceAddrTon, sourceAddrNpi,
                sourceAddr, scheduleDeliveryTime, validityPeriod,
                registeredDelivery, smDefaultMsgId, shortMessage);

        executeSendCommand(replaceSmTask, getTransactionTimer());
    }
    
    /* (non-Javadoc)
     * @see org.jsmpp.session.ClientSession#cancelShortMessage(java.lang.String, java.lang.String, org.jsmpp.bean.TypeOfNumber, org.jsmpp.bean.NumberingPlanIndicator, java.lang.String, org.jsmpp.bean.TypeOfNumber, org.jsmpp.bean.NumberingPlanIndicator, java.lang.String)
     */
    public void cancelShortMessage(String serviceType, String messageId,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, TypeOfNumber destAddrTon,
            NumberingPlanIndicator destAddrNpi, String destinationAddress)
            throws PDUException, ResponseTimeoutException,
            InvalidResponseException, NegativeResponseException, IOException {
        
        ensureTransmittable("cancelShortMessage");
        
        CancelSmCommandTask task = new CancelSmCommandTask(pduSender(),
                serviceType, messageId, sourceAddrTon, sourceAddrNpi,
                sourceAddr, destAddrTon, destAddrNpi, destinationAddress);
        
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
	public void close()
	{
		super.close();

		if(Thread.currentThread() != pduReaderWorker) {
			try {
				if(pduReaderWorker != null) {
					pduReaderWorker.join();
				}
			} catch (InterruptedException e) {
				logger.warn("Interrupted while waiting for pduReaderWorker thread to exit");
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {
		close();
	}
	
	private void fireAcceptDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {
		if (messageReceiverListener != null) {
			messageReceiverListener.onAcceptDeliverSm(deliverSm);
        } else { 
			logger.warn("Receive deliver_sm but MessageReceiverListener is null. Short message = " + new String(deliverSm.getShortMessage()));
			throw new ProcessRequestException("No message receiver listener registered", SMPPConstant.STAT_ESME_RX_T_APPN);
        }
	}
	
	private void fireAcceptAlertNotification(AlertNotification alertNotification) {
	    if (messageReceiverListener != null) {
	        messageReceiverListener.onAcceptAlertNotification(alertNotification);
	    } else {
	        logger.warn("Receive alert_notification but MessageReceiverListener is null");
	    }
	}
	
	private class ResponseHandlerImpl implements ResponseHandler {
		
		public void processDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {
			try {
				fireAcceptDeliverSm(deliverSm);
			} catch(Exception e) {
				String msg = "Invalid runtime exception thrown when processing DeliverSm";
				logger.error(msg, e);
				throw new ProcessRequestException(msg, SMPPConstant.STAT_ESME_RX_T_APPN);
			}
		}
		
		public DataSmResult processDataSm(DataSm dataSm)
		        throws ProcessRequestException {
			try {
				return fireAcceptDataSm(dataSm);
			} catch(Exception e) {
				String msg = "Invalid runtime exception thrown when processing DataSm";
				logger.error(msg, e);
				throw new ProcessRequestException(msg, SMPPConstant.STAT_ESME_RX_T_APPN);
			}
		}
		
		public void processAlertNotification(AlertNotification alertNotification) {
			try {
				fireAcceptAlertNotification(alertNotification);
			} catch(Exception e) {
				String msg = "Invalid runtime exception thrown when processing AlertSm";
				logger.error(msg, e);
			}
		}
		
		public void sendDataSmResp(DataSmResult dataSmResult, int sequenceNumber)
		        throws IOException {
		    try {
                pduSender().sendDataSmResp(out, sequenceNumber,
                        dataSmResult.getMessageId(),
                        dataSmResult.getOptionalParameters());
            } catch (PDUStringException e) {
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
		
		public void sendDeliverSmResp(int commandStatus, int sequenceNumber) throws IOException {
			pduSender().sendDeliverSmResp(out, commandStatus, sequenceNumber);
			logger.debug("deliver_sm_resp with seq_number " + sequenceNumber + " has been sent");
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
	}
	
	/**
	 * Worker to read the PDU.
	 * 
	 * @author uudashr
	 *
	 */
	private class PDUReaderWorker extends Thread {
		// start with serial execution of pdu processing, when the session is bound the pool will be enlarge up to the PduProcessorDegree
	    private ExecutorService executorService = Executors.newFixedThreadPool(1); 
		
	    public PDUReaderWorker() {
        	super("PDUReaderWorker: " + SMPPSession.this);
	    }
	    
	    private Runnable onIOExceptionTask = new Runnable() {
		    public void run() {
		        close();
		    };
		};
		
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
			} catch (InterruptedException e) {
				logger.warn("interrupted while waiting for executor service pool to finish");
			}
			logger.info("PDUReaderWorker stop");
		}
		
		private void readPDU() {
	        try {
	            Command pduHeader = null;
	            byte[] pdu = null;
	            
                pduHeader = pduReader.readPDUHeader(in);
                pdu = pduReader.readPDU(in, pduHeader);
	            
                /*
                 * When the processing PDU is need user interaction via event,
                 * the code on event might take non-short time, so we need to
                 * process it concurrently.
                 */
                PDUProcessTask task = new PDUProcessTask(pduHeader, pdu,
                        sessionContext, responseHandler,
                        sessionContext, onIOExceptionTask);
	            executorService.execute(task);
	            
	        } catch (InvalidCommandLengthException e) {
	            logger.warn("Receive invalid command length", e);
	            try {
	                pduSender().sendGenericNack(out, SMPPConstant.STAT_ESME_RINVCMDLEN, 0);
	            } catch (IOException ee) {
	                logger.warn("Failed sending generic nack", ee);
	            }
	            unbindAndClose();
	        } catch (SocketTimeoutException e) {
	            notifyNoActivity();
	        } catch (IOException e) {
	            logger.warn("IOException while reading: {}", e.getMessage());
	            close();
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
	 *
	 */
	private class BoundSessionStateListener implements SessionStateListener {
	    public void onStateChange(SessionState newState, SessionState oldState,
	    		Session source) {
	        /*
	         * We need to set SO_TIMEOUT to sessionTimer so when timeout occur, 
	         * a SocketTimeoutException will be raised. When Exception raised we
	         * can send an enquireLinkCommand.
	         */
	        if (newState.isBound()) {
	            try {
                    conn.setSoTimeout(getEnquireLinkTimer());
                } catch (IOException e) {
                    logger.error("Failed setting so_timeout for session timer", e);
                }
    	        
               	logger.info("Changing processor degree to {}", getPduProcessorDegree());
               	((ThreadPoolExecutor)pduReaderWorker.executorService).setCorePoolSize(getPduProcessorDegree());
               	((ThreadPoolExecutor)pduReaderWorker.executorService).setMaximumPoolSize(getPduProcessorDegree());
	        }
	    }
	}
}