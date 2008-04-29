package org.jsmpp.session;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jsmpp.BindType;
import org.jsmpp.DefaultPDUReader;
import org.jsmpp.DefaultPDUSender;
import org.jsmpp.InterfaceVersion;
import org.jsmpp.InvalidCommandLengthException;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.NumberingPlanIndicator;
import org.jsmpp.PDUReader;
import org.jsmpp.PDUSender;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.SynchronizedPDUSender;
import org.jsmpp.TypeOfNumber;
import org.jsmpp.bean.BindResp;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.QuerySmResp;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SubmitSmResp;
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
 * @author uudashr
 * @version 2.0
 *
 */
public class SMPPSession extends AbstractSession {
	private static final Logger logger = LoggerFactory.getLogger(SMPPSession.class);

	/* Utility */
    private final PDUReader pduReader;
	
    /* Connection */
	private final ConnectionFactory connFactory;
	private Connection conn;
	private DataInputStream in;
	private OutputStream out;
	
	private final ResponseHandler responseHandler = new ResponseHandlerImpl();
	private MessageReceiverListener messageReceiverListener;
    private BoundSessionStateListener sessionStateListener = new BoundSessionStateListener();
    private SMPPSessionContext sessionContext = new SMPPSessionContext(this, sessionStateListener);
	private EnquireLinkSender enquireLinkSender;
	
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
	    addSessionStateListener(new BoundSessionStateListener());
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
		
		sessionContext.open();
		try {
			in = new DataInputStream(conn.getInputStream());
			out = conn.getOutputStream();
			
			new PDUReaderWorker().start();
			String smscSystemId = sendBind(bindParam.getBindType(), bindParam.getSystemId(), bindParam.getPassword(), bindParam.getSystemType(),
                    InterfaceVersion.IF_34, bindParam.getAddrTon(), bindParam.getAddrNpi(), bindParam.getAddressRange(), timeout);
			sessionContext.bound(bindParam.getBindType());
			
			enquireLinkSender = new EnquireLinkSender();
			enquireLinkSender.start();
			return smscSystemId;
		} catch (PDUStringException e) {
		    logger.error("Failed sending bind command", e);
		    throw new IOException("Failed sending bind since some string parameter area invalid : " + e.getMessage());
		} catch (NegativeResponseException e) {
			String message = "Receive negative bind response";
			logger.error(message, e);
			close();
			throw new IOException(message + ": " + e.getMessage());
		} catch (InvalidResponseException e) {
			String message = "Receive invalid response of bind";
			logger.error(message, e);
			close();
			throw new IOException(message + ": " + e.getMessage());
		} catch (ResponseTimeoutException e) {
			String message = "Waiting bind response take time to long";
			logger.error(message, e);
			close();
			throw new IOException(message + ": " + e.getMessage());
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
	 * @throws PDUStringException if we enter invalid bind parameter(s).
	 * @throws ResponseTimeoutException if there is no valid response after defined millisecond.
	 * @throws InvalidResponseException if there is invalid response found.
	 * @throws NegativeResponseException if we receive negative response.
	 * @throws IOException if there is an IO error occur.
	 */
	private String sendBind(BindType bindType, String systemId,
			String password, String systemType,
			InterfaceVersion interfaceVersion, TypeOfNumber addrTon,
			NumberingPlanIndicator addrNpi, String addressRange, long timeout)
			throws PDUStringException, ResponseTimeoutException,
			InvalidResponseException, NegativeResponseException, IOException {
	    
	    BindCommandTask task = new BindCommandTask(out, pduSender(), bindType, systemId, password, 
	            systemType, interfaceVersion, addrTon, addrNpi, addressRange);
	    
	    BindResp resp = (BindResp)executeSendCommand(task, timeout);
		return resp.getSystemId();
	}
	
	
	/**
	 * Submit short message with specified parameter.
	 * 
     * @param serviceType is the service_type parameter.
     * @param sourceAddrTon is the source_addr_ton parameter.
     * @param sourceAddrNpi is the source_addr_npi parameter.
     * @param sourceAddr is the source_addr parameter.
     * @param destAddrTon is the dest_addr_ton parameter.
     * @param destAddrNpi is the dest_addr_npi parameter.
     * @param destinationAddr is the destination_addr parameter.
     * @param esmClass is the esm_class parameter.
     * @param protocolId is the protocol_id parameter.
     * @param priorityFlag is the priority_flag parameter.
     * @param scheduleDeliveryTime is the schedule_delivery_time parameter. (It is time in string format).
     * @param validityPeriod is the validity_period parameter (It is time in string format).
     * @param registeredDelivery is the registered_delivery parameter.
     * @param replaceIfPresentFlag is the replace_if_present_flag parameter
     * @param dataCoding is the data_coding parameter.
     * @param smDefaultMsgId is the sm_default_message_id parameter.
     * @param shortMessage is the short message.
     * @param optionalParameters is the optional parameters.
     * @return the message id from SMSC.
     * @throws PDUStringException if we enter invalid bind parameter(s).
     * @throws ResponseTimeoutException if there is no valid response after defined millisecond.
     * @throws InvalidResponseException if there is invalid response found.
     * @throws NegativeResponseException if we receive negative response.
     * @throws IOException if there is an IO error found.
     */
    public String submitShortMessage(String serviceType,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, TypeOfNumber destAddrTon,
            NumberingPlanIndicator destAddrNpi, String destinationAddr,
            ESMClass esmClass, byte protocolId, byte priorityFlag,
            String scheduleDeliveryTime, String validityPeriod,
            RegisteredDelivery registeredDelivery, byte replaceIfPresentFlag,
            DataCoding dataCoding, byte smDefaultMsgId, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUStringException,
            ResponseTimeoutException, InvalidResponseException,
            NegativeResponseException, IOException {
    	
        SendSubmitSmCommandTask submitSmTask = new SendSubmitSmCommandTask(out,
                pduSender(), serviceType, sourceAddrTon, sourceAddrNpi,
                sourceAddr, destAddrTon, destAddrNpi, destinationAddr,
                esmClass, protocolId, priorityFlag, scheduleDeliveryTime,
                validityPeriod, registeredDelivery, replaceIfPresentFlag,
                dataCoding, smDefaultMsgId, shortMessage, optionalParameters);
    	
        SubmitSmResp resp = (SubmitSmResp)executeSendCommand(submitSmTask, getTransactionTimer());
    	return resp.getMessageId();
    }
    
    /**
     * Query short message.
     * 
     * @param messageId is the message_id parameter.
     * @param sourceAddrTon is the source_addr_ton parameter.
     * @param sourceAddrNpi is the source_addr_npi parameter.
     * @param sourceAddr is the source_addr parameter.
     * @return the query_sm response (query_sm_resp).
     * @throws PDUStringException if there is an invalid PDU String found.
     * @throws ResponseTimeoutException if the response take time too long.
     * @throws InvalidResponseException if the response is invalid.
     * @throws NegativeResponseException if the response contains non-ok command_status.
     * @throws IOException if there is an IO error found.
     */
    public QuerySmResult queryShortMessage(String messageId, TypeOfNumber sourceAddrTon,
    		NumberingPlanIndicator sourceAddrNpi, String sourceAddr)
    		throws PDUStringException, ResponseTimeoutException,
    		InvalidResponseException, NegativeResponseException, IOException {
    	
        QuerySmCommandTask task = new QuerySmCommandTask(out, pduSender(), messageId, 
                sourceAddrTon, sourceAddrNpi, sourceAddr);
        
        
    	QuerySmResp resp = (QuerySmResp)executeSendCommand(task, getTransactionTimer());
    	
    	if (resp.getMessageId().equals(messageId)) {
    		return new QuerySmResult(resp.getFinalDate(), resp.getMessageState(), resp.getErrorCode());
    	} else {
    		// message id requested not same as the returned
    		throw new InvalidResponseException("Requested message_id doesn't match with the result");
    	}
    }
    
    public void cancelShortMessage(String serviceType, String messageId,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, TypeOfNumber destAddrTon,
            NumberingPlanIndicator destAddrNpi, String destinationAddress)
            throws PDUStringException, ResponseTimeoutException,
            InvalidResponseException, NegativeResponseException, IOException {
        CancelSmCommandTask task = new CancelSmCommandTask(out, pduSender(),
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
	    return null;
	}
	
    private synchronized boolean isReadPdu() {
		return sessionContext.getSessionState().isBound() || sessionContext.getSessionState().equals(SessionState.OPEN);
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
        }
	}
	
	private class ResponseHandlerImpl implements ResponseHandler {
		
		public void processDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {
			fireAcceptDeliverSm(deliverSm);
		}
		
		public DataSmResult processDataSm(DataSm dataSm)
		        throws ProcessRequestException {
		    return fireAcceptDataSm(dataSm);
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
		
		@SuppressWarnings("unchecked")
		public PendingResponse<Command> removeSentItem(int sequenceNumber) {
			return removePendingResponse(sequenceNumber);
		}
		
		public void notifyUnbonded() {
		    sessionContext.unbound();
		}
		
		public void sendDeliverSmResp(int sequenceNumber) throws IOException {
			pduSender().sendDeliverSmResp(out, sequenceNumber);
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
	    private ExecutorService executorService = Executors.newFixedThreadPool(getPduDispatcherThreadCount());
		
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
			executorService.shutdownNow();
			close();
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
                        sessionContext.getStateProcessor(), responseHandler,
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
	            close();
	        }
	    }
		
	    /**
	     * Notify for no activity.
	     */
	    private void notifyNoActivity() {
	        logger.debug("No activity notified");
	        enquireLinkSender.enquireLink();
	    }
	}
	
	
	/**
	 * FIXME uud: we can create general class for SMPPServerSession and SMPPSession
	 * @author uudashr
	 *
	 */
	private class EnquireLinkSender extends Thread {
        private final AtomicBoolean sendingEnquireLink = new AtomicBoolean(false);
        
        @Override
        public void run() {
            logger.info("Starting EnquireLinkSender");
            while (isReadPdu()) {
                while (!sendingEnquireLink.compareAndSet(true, false) && isReadPdu()) {
                    synchronized (sendingEnquireLink) {
                        try {
                            sendingEnquireLink.wait(500);
                        } catch (InterruptedException e) {
                        }
                    }
                }
                if (!isReadPdu()) {
                    break;
                }
                try {
                    sendEnquireLink();
                } catch (ResponseTimeoutException e) {
                    close();
                } catch (InvalidResponseException e) {
                    // lets unbind gracefully
                    unbindAndClose();
                } catch (IOException e) {
                    close();
                }
            }
            logger.info("EnquireLinkSender stop");
        }
        
        /**
         * This method will send enquire link asynchronously.
         */
        public void enquireLink() {
            if (sendingEnquireLink.compareAndSet(false, true)) {
                logger.debug("Sending enquire link notify");
                synchronized (sendingEnquireLink) {
                    sendingEnquireLink.notify();
                }
            } else {
                logger.debug("Not sending enquire link notify");
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
	            Object source) {
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
	        }
	    }
	}
}