package org.jsmpp.session;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Hashtable;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
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
import org.jsmpp.SynchronizedPDUReader;
import org.jsmpp.SynchronizedPDUSender;
import org.jsmpp.TypeOfNumber;
import org.jsmpp.bean.BindResp;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.EnquireLinkResp;
import org.jsmpp.bean.QuerySmResp;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SubmitSmResp;
import org.jsmpp.bean.UnbindResp;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ProcessMessageException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.state.SMPPSessionState;
import org.jsmpp.util.DefaultComposer;
import org.jsmpp.util.Sequence;


/**
 * @author uudashr
 * @version 2.0
 *
 */
public class SMPPSession {
	private static final Logger logger = Logger.getLogger(SMPPSession.class);
	private static final PDUSender pduSender = new SynchronizedPDUSender(new DefaultPDUSender(new DefaultComposer()));
	private static final PDUReader pduReader = new SynchronizedPDUReader(new DefaultPDUReader());
	private static final AtomicInteger sessionIdSequence = new AtomicInteger();
	
	private Socket socket = new Socket();
	private DataInputStream in;
	private OutputStream out;
	private SessionState sessionState = SessionState.CLOSED;
	private SMPPSessionState stateProcessor = SMPPSessionState.CLOSED;
	private final Sequence sequence = new Sequence();
	private final SMPPSessionHandler sessionHandler = new SMPPSessionHandlerImpl();
	private final Hashtable<Integer, PendingResponse<? extends Command>> pendingResponse = new Hashtable<Integer, PendingResponse<? extends Command>>();
	private int sessionTimer = 5000;
	private long transactionTimer = 2000;
	private int enquireLinkToSend;
	private long lastActivityTimestamp;
	private MessageReceiverListener messageReceiverListener;
	private SessionStateListener sessionStateListener;
    
	private IdleActivityChecker idleActivityChecker;
	//private PDUReaderWorker _pduReaderWorker;
	private EnquireLinkSender enquireLinkSender;
	private int sessionId = sessionIdSequence.incrementAndGet();
	
	public SMPPSession() {
		
	}
	
	public int getSessionId() {
		return sessionId;
	}
	
	public void connectAndBind(String host, int port, BindType bindType,
            String systemId, String password, String systemType,
            TypeOfNumber addrTon, NumberingPlanIndicator addrNpi,
            String addressRange) throws IOException {
		if (sequence.currentValue() != 0)
			throw new IOException("Failed connecting");
		
		socket.connect(new InetSocketAddress(host, port));
		if (socket.getInputStream() == null) {
			logger.fatal("InputStream is null");
		} else if (socket.isInputShutdown()) {
			logger.fatal("Input shutdown");
		}
		logger.info("Connected");
		
		new PDUReaderWorker().start();
		new PDUReaderWorker().start();
		new PDUReaderWorker().start();
		
		changeState(SessionState.OPEN);
		try {
			in = new DataInputStream(socket.getInputStream());
			out = socket.getOutputStream();
			sendBind(bindType, systemId, password, systemType,
                    InterfaceVersion.IF_34, addrTon, addrNpi, addressRange);
			changeToBoundState(bindType);
			socket.setSoTimeout(sessionTimer);
			
			enquireLinkSender = new EnquireLinkSender();
			enquireLinkSender.start();
			
			idleActivityChecker = new IdleActivityChecker();
			idleActivityChecker.start();
			
		} catch (PDUStringException e) {
			logger.error("Failed sending bind command", e);
		} catch (NegativeResponseException e) {
			String message = "Receive negative bind response";
			logger.error(message, e);
			closeSocket();
			throw new IOException(message + ": " + e.getMessage());
		} catch (InvalidResponseException e) {
			String message = "Receive invalid response of bind";
			logger.error(message, e);
			closeSocket();
			throw new IOException(message + ": " + e.getMessage());
		} catch (ResponseTimeoutException e) {
			String message = "Waiting bind response take time to long";
			logger.error(message, e);
			closeSocket();
			throw new IOException(message + ": " + e.getMessage());
		} catch (IOException e) {
			logger.error("IO Error occure", e);
			closeSocket();
			throw e;
		}
	}
	
	/**
	 * @param bindType
	 * @param systemId
	 * @param password
	 * @param systemType
	 * @param interfaceVersion
	 * @param addrTon
	 * @param addrNpi
	 * @param addressRange
	 * @return SMSC system id.
	 * @throws PDUStringException if we enter invalid bind parameter(s).
	 * @throws ResponseTimeoutException if there is no valid response after defined millis.
	 * @throws InvalidResponseException if there is invalid response found.
	 * @throws NegativeResponseException if we receive negative response.
	 * @throws IOException
	 */
	private String sendBind(BindType bindType, String systemId,
			String password, String systemType,
			InterfaceVersion interfaceVersion, TypeOfNumber addrTon,
			NumberingPlanIndicator addrNpi, String addressRange)
			throws PDUStringException, ResponseTimeoutException,
			InvalidResponseException, NegativeResponseException, IOException {
		int seqNum = sequence.nextValue();
		PendingResponse<BindResp> pendingResp = new PendingResponse<BindResp>(transactionTimer);
		pendingResponse.put(seqNum, pendingResp);
		
		try {
			pduSender.sendBind(out, bindType, seqNum, systemId, password, systemType, interfaceVersion, addrTon, addrNpi, addressRange);
		} catch (IOException e) {
			logger.error("Failed sending bind command", e);
			pendingResponse.remove(seqNum);
			throw e;
		}
		
		try {
			pendingResp.waitDone();
			logger.info("Bind response received");
		} catch (ResponseTimeoutException e) {
			pendingResponse.remove(seqNum);
			throw e;
		} catch (InvalidResponseException e) {
			pendingResponse.remove(seqNum);
			throw e;
		}
		
		if (pendingResp.getResponse().getCommandStatus() != SMPPConstant.STAT_ESME_ROK)
			throw new NegativeResponseException(pendingResp.getResponse().getCommandStatus());
		
		return pendingResp.getResponse().getSystemId();
	}

	/**
	 * Ensure we have proper link.
	 * @throws ResponseTimeoutException if there is no valid response after defined millis.
	 * @throws InvalidResponseException if there is invalid response found.
	 * @throws IOException
	 */
	private void enquireLink() throws ResponseTimeoutException, InvalidResponseException, IOException {
		int seqNum = sequence.nextValue();
		PendingResponse<EnquireLinkResp> pendingResp = new PendingResponse<EnquireLinkResp>(transactionTimer);
		pendingResponse.put(seqNum, pendingResp);
		
		try {
			pduSender.sendEnquireLink(out, seqNum);
		} catch (IOException e) {
			logger.error("Failed sending enquire link", e);
			pendingResponse.remove(seqNum);
			throw e;
		}
		
		try {
			pendingResp.waitDone();
			logger.info("Enquire link response received");
		} catch (ResponseTimeoutException e) {
			pendingResponse.remove(seqNum);
			throw e;
		} catch (InvalidResponseException e) {
			pendingResponse.remove(seqNum);
			throw e;
		}
		
		if (pendingResp.getResponse().getCommandStatus() != SMPPConstant.STAT_ESME_ROK) {
			// this is ok
			logger.warn("Receive NON-OK response of enquire link");
		}
	}

	public void unbindAndClose() {
		try {
			unbind();
		} catch (ResponseTimeoutException e) {
			logger.error("Timeout waiting unbind response", e);
		} catch (InvalidResponseException e) {
			logger.error("Receive invalid unbind response", e);
		} catch (IOException e) {
			logger.error("IO error found ", e);
		}
		closeSocket();
	}

	private void unbind() throws ResponseTimeoutException, InvalidResponseException, IOException {
		int seqNum = sequence.nextValue();
		PendingResponse<UnbindResp> pendingResp = new PendingResponse<UnbindResp>(transactionTimer);
		pendingResponse.put(seqNum, pendingResp);
		
		try {
			pduSender.sendUnbind(out, seqNum);
		} catch (IOException e) {
			logger.error("Failed sending unbind", e);
			pendingResponse.remove(seqNum);
			throw e;
		}
		
		try {
			pendingResp.waitDone();
			logger.info("Unbind response received");
			changeState(SessionState.UNBOUND);
		} catch (ResponseTimeoutException e) {
			pendingResponse.remove(seqNum);
			throw e;
		} catch (InvalidResponseException e) {
			pendingResponse.remove(seqNum);
			throw e;
		}
		
		if (pendingResp.getResponse().getCommandStatus() != SMPPConstant.STAT_ESME_ROK)
			logger.warn("Receive NON-OK response of unbind");
	}

	/**
	 * @param serviceType
	 * @param sourceAddrTon
	 * @param sourceAddrNpi
	 * @param sourceAddr
	 * @param destAddrTon
	 * @param destAddrNpi
	 * @param destinationAddr
	 * @param esmClass
	 * @param protocoId
	 * @param priorityFlag
	 * @param scheduleDeliveryTime
	 * @param validityPeriod
	 * @param registeredDelivery
	 * @param replaceIfPresent
	 * @param dataCoding
	 * @param smDefaultMsgId
	 * @param shortMessage
	 * @return message id.
	 * @throws PDUStringException if we enter invalid bind parameter(s).
	 * @throws ResponseTimeoutException if there is no valid response after defined millis.
	 * @throws InvalidResponseException if there is invalid response found.
	 * @throws NegativeResponseException if we receive negative response.
	 * @throws IOException
	 */
	public String submitShortMessage(String serviceType,
			TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
			String sourceAddr, TypeOfNumber destAddrTon,
			NumberingPlanIndicator destAddrNpi, String destinationAddr,
			ESMClass esmClass, byte protocoId, byte priorityFlag,
			String scheduleDeliveryTime, String validityPeriod,
			RegisteredDelivery registeredDelivery, byte replaceIfPresent,
			DataCoding dataCoding, byte smDefaultMsgId, byte[] shortMessage)
			throws PDUStringException, ResponseTimeoutException,
			InvalidResponseException, NegativeResponseException, IOException {
		
		int seqNum = sequence.nextValue();
		PendingResponse<SubmitSmResp> pendingResp = new PendingResponse<SubmitSmResp>(transactionTimer);
		pendingResponse.put(seqNum, pendingResp);
		try {
			pduSender.sendSubmitSm(out, seqNum, serviceType, sourceAddrTon,
					sourceAddrNpi, sourceAddr, destAddrTon, destAddrNpi,
					destinationAddr, esmClass, protocoId, priorityFlag,
					scheduleDeliveryTime, validityPeriod, registeredDelivery,
					replaceIfPresent, dataCoding, smDefaultMsgId, shortMessage);
			
		} catch (IOException e) {
			logger.error("Failed submit short message", e);
			pendingResponse.remove(seqNum);
			closeSocket();
			throw e;
		}
		
		try {
			pendingResp.waitDone();
			logger.debug("Submit sm response received");
		} catch (ResponseTimeoutException e) {
			pendingResponse.remove(seqNum);
			logger.debug("Response timeout for submit_sm with sessionIdSequence number " + seqNum);
			throw e;
		} catch (InvalidResponseException e) {
			pendingResponse.remove(seqNum);
			throw e;
		}
		
		if (pendingResp.getResponse().getCommandStatus() != SMPPConstant.STAT_ESME_ROK)
			throw new NegativeResponseException(pendingResp.getResponse().getCommandStatus());
		
		return pendingResp.getResponse().getMessageId();
	}
	
	public QuerySmResult queryShortMessage(String messageId, TypeOfNumber sourceAddrTon,
			NumberingPlanIndicator sourceAddrNpi, String sourceAddr)
			throws PDUStringException, ResponseTimeoutException,
			InvalidResponseException, NegativeResponseException, IOException {
		
		int seqNum = sequence.nextValue();
		PendingResponse<QuerySmResp> pendingResp = new PendingResponse<QuerySmResp>(transactionTimer);
		pendingResponse.put(seqNum, pendingResp);
		try {
			pduSender.sendQuerySm(out, seqNum, messageId, sourceAddrTon, sourceAddrNpi, sourceAddr);
			
		} catch (IOException e) {
			logger.error("Failed submit short message", e);
			pendingResponse.remove(seqNum);
			closeSocket();
			throw e;
		}
		
		try {
			pendingResp.waitDone();
			logger.info("Query sm response received");
		} catch (ResponseTimeoutException e) {
			pendingResponse.remove(seqNum);
			throw e;
		} catch (InvalidResponseException e) {
			pendingResponse.remove(seqNum);
			throw e;
		}
		
		QuerySmResp resp = pendingResp.getResponse();
		if (resp.getCommandStatus() != SMPPConstant.STAT_ESME_ROK)
			throw new NegativeResponseException(resp.getCommandStatus());
		
		if (resp.getMessageId().equals(messageId)) {
			return new QuerySmResult(resp.getFinalDate(), resp.getMessageState(), resp.getErrorCode());
		} else {
			// message id requested not same as the returned
			throw new InvalidResponseException("Requested message_id doesn't match with the result");
		}
	}
	
	private void changeToBoundState(BindType bindType) {
		if (bindType.equals(BindType.BIND_TX)) {
			changeState(SessionState.BOUND_TX);
		} else if (bindType.equals(BindType.BIND_RX)) {
			changeState(SessionState.BOUND_RX);
		} else if (bindType.equals(BindType.BIND_TRX)){
			changeState(SessionState.BOUND_TRX);
		} else {
			throw new IllegalArgumentException("Bind type " + bindType + " not supported");
		}
		
		try {
			socket.setSoTimeout(sessionTimer);
		} catch (SocketException e) {
			logger.error("Failed setting so_timeout for session timer", e);
		}
	}

	private synchronized void changeState(SessionState newState) {
        if (sessionState != newState) {
            final SessionState oldState = sessionState;
    		sessionState = newState;
    		
    		// change the session state processor
    		if (sessionState == SessionState.OPEN) {
    			stateProcessor = SMPPSessionState.OPEN;
    		} else if (sessionState == SessionState.BOUND_RX) {
    			stateProcessor = SMPPSessionState.BOUND_RX;
    		} else if (sessionState == SessionState.BOUND_TX) {
    			stateProcessor = SMPPSessionState.BOUND_TX;
    		} else if (sessionState == SessionState.BOUND_TRX) {
    			stateProcessor = SMPPSessionState.BOUND_TRX;
    		} else if (sessionState == SessionState.UNBOUND) {
    			stateProcessor = SMPPSessionState.UNBOUND;
    		} else if (sessionState == SessionState.CLOSED) {
    			stateProcessor = SMPPSessionState.CLOSED;
    		}
            fireChangeState(newState, oldState);
        }
	}

	private void updateActivityTimestamp() {
		lastActivityTimestamp = System.currentTimeMillis();
	}

	private void addEnquireLinkJob() {
		enquireLinkToSend++;
	}
	
	public int getSessionTimer() {
		return sessionTimer;
	}
	
	public void setSessionTimer(int sessionTimer) {
		this.sessionTimer = sessionTimer;
		if (sessionState.isBound()) {
			try {
				socket.setSoTimeout(sessionTimer);
			} catch (SocketException e) {
				logger.error("Failed setting so_timeout for session timer", e);
			}
		}
	}
	
	public long getTransactionTimer() {
		return transactionTimer;
	}
	
	public void setTransactionTimer(long transactionTimer) {
		this.transactionTimer = transactionTimer;
	}
	
	public synchronized SessionState getSessionState() {
		return sessionState;
	}
	
    public void setSessionStateListener(
            SessionStateListener sessionStateListener) {
        this.sessionStateListener = sessionStateListener;
    }
    
	public void setMessageReceiverListener(
			MessageReceiverListener messageReceiverListener) {
		this.messageReceiverListener = messageReceiverListener;
	}
	
	private void closeSocket() {
		changeState(SessionState.CLOSED);
		if (!socket.isClosed()) {
			try { 
				socket.close(); 
			} catch (IOException e) {
				logger.warn("Failed closing socket", e);
			}
		}
	}
	
	private synchronized boolean isReadPdu() {
		return sessionState.isBound() || sessionState.equals(SessionState.OPEN);
	}
	
	private void readPDU() {
		try {
			Command pduHeader = null;
			byte[] pdu = null;
			synchronized (in) {
				pduHeader = pduReader.readPDUHeader(in);
				pdu = pduReader.readPDU(in, pduHeader);
			}
			switch (pduHeader.getCommandId()) {
			case SMPPConstant.CID_BIND_RECEIVER_RESP:
			case SMPPConstant.CID_BIND_TRANSMITTER_RESP:
			case SMPPConstant.CID_BIND_TRANSCEIVER_RESP:
				updateActivityTimestamp();
				stateProcessor.processBindResp(pduHeader, pdu, sessionHandler);
				break;
			case SMPPConstant.CID_GENERIC_NACK:
				updateActivityTimestamp();
				stateProcessor.processGenericNack(pduHeader, pdu, sessionHandler);
				break;
			case SMPPConstant.CID_ENQUIRE_LINK:
				updateActivityTimestamp();
				stateProcessor.processEnquireLink(pduHeader, pdu, sessionHandler);
				break;
			case SMPPConstant.CID_ENQUIRE_LINK_RESP:
				updateActivityTimestamp();
				stateProcessor.processEnquireLinkResp(pduHeader, pdu, sessionHandler);
				break;
			case SMPPConstant.CID_SUBMIT_SM_RESP:
				updateActivityTimestamp();
				stateProcessor.processSubmitSmResp(pduHeader, pdu, sessionHandler);
				break;
			case SMPPConstant.CID_QUERY_SM_RESP:
				updateActivityTimestamp();
				stateProcessor.processQuerySmResp(pduHeader, pdu, sessionHandler);
				break;
			case SMPPConstant.CID_DELIVER_SM:
				updateActivityTimestamp();
				stateProcessor.processDeliverSm(pduHeader, pdu, sessionHandler);
				break;
			case SMPPConstant.CID_UNBIND:
				updateActivityTimestamp();
				stateProcessor.processUnbind(pduHeader, pdu, sessionHandler);
				changeState(SessionState.UNBOUND);
				break;
			case SMPPConstant.CID_UNBIND_RESP:
				updateActivityTimestamp();
				stateProcessor.processUnbindResp(pduHeader, pdu, sessionHandler);
				break;
			default:
				stateProcessor.processUnknownCid(pduHeader, pdu, sessionHandler);
			}
		} catch (InvalidCommandLengthException e) {
			logger.warn("Receive invalid command length", e);
			// FIXME uud: response to this error, generick nack or close socket
		} catch (SocketTimeoutException e) {
			addEnquireLinkJob();
			try { Thread.sleep(1); } catch (InterruptedException ee) {}
		} catch (IOException e) {
			closeSocket();
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		closeSocket();
	}
	
	private void fireAcceptDeliverSm(DeliverSm deliverSm) throws ProcessMessageException {
		if (messageReceiverListener != null) {
			messageReceiverListener.onAcceptDeliverSm(deliverSm);
        } else { 
			logger.warn("Receive deliver_sm but MessageReceiverListener is null. Short message = " + new String(deliverSm.getShortMessage()));
        }
	}
	
    private void fireChangeState(SessionState newState, SessionState oldState) {
        if (sessionStateListener != null) {
            sessionStateListener.onStateChange(newState, oldState, this);
        } else {
            logger.warn("SessionStateListener is null");
        }
    }
    
	private class SMPPSessionHandlerImpl implements SMPPSessionHandler {
		
		public void processDeliverSm(DeliverSm deliverSm) throws ProcessMessageException {
			fireAcceptDeliverSm(deliverSm);
		}
		
		@SuppressWarnings("unchecked")
		public PendingResponse<Command> removeSentItem(int sequenceNumber) {
			return (PendingResponse<Command>)pendingResponse.remove(sequenceNumber);
		}
		
		public void sendDeliverSmResp(int sequenceNumber) throws IOException {
			try {
				pduSender.sendDeliverSmResp(out, sequenceNumber);
				// FIXME uud: delete this log
				logger.debug("deliver_sm_resp with seq_number " + sequenceNumber + " has been sent");
			} catch (PDUStringException e) {
				logger.fatal("Failed sending deliver_sm_resp", e);
			}
		}
		
		public void sendEnquireLinkResp(int sequenceNumber) throws IOException {
			pduSender.sendEnquireLinkResp(out, sequenceNumber);
		}
		
		public void sendGenerickNack(int commandStatus, int sequenceNumber) throws IOException {
			pduSender.sendGenericNack(out, commandStatus, sequenceNumber);
		}
		
		public void sendNegativeResponse(int originalCommandId, int commandStatus, int sequenceNumber) throws IOException {
			pduSender.sendHeader(out, originalCommandId | SMPPConstant.MASK_CID_RESP, commandStatus, sequenceNumber);
		}
		
		public void sendUnbindResp(int sequenceNumber) throws IOException {
			pduSender.sendUnbindResp(out, SMPPConstant.STAT_ESME_ROK, sequenceNumber);
		}
	}
	
	private class PDUReaderWorker extends Thread {
		@Override
		public void run() {
			logger.info("Starting PDUReaderWorker");
			while (isReadPdu()) {
				readPDU();
			}
			logger.info("PDUReaderWorker stop");
		}
	}
	
	private class EnquireLinkSender extends Thread {
		@Override
		public void run() {
			logger.info("Starting EnquireLinkSender");
			while (isReadPdu()) {
				long sleepTime = 1000;
				
				if (enquireLinkToSend > 0) {
					enquireLinkToSend--;
					try {
						enquireLink();
					} catch (ResponseTimeoutException e) {
						closeSocket();
					} catch (InvalidResponseException e) {
						// lets unbind gracefully
						unbindAndClose();
					} catch (IOException e) {
						closeSocket();
					}
				}
				
				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
				}
			}
			logger.info("EnquireLinkSender stop");
		}
	}
	
	private class IdleActivityChecker extends Thread {
		@Override
		public void run() {
			logger.info("Starting IdleActivityChecker");
			while (isReadPdu()) {
				long timeLeftToEnquire = lastActivityTimestamp + sessionTimer - System.currentTimeMillis();
				if (timeLeftToEnquire <= 0) {
					try {
						enquireLink();
					} catch (ResponseTimeoutException e) {
						closeSocket();
					} catch (InvalidResponseException e) {
						// lets unbind gracefully
						unbindAndClose();
					} catch (IOException e) {
						closeSocket();
					}
				} else {
					try {
						Thread.sleep(timeLeftToEnquire);
					} catch (InterruptedException e) {
					}
				}
			}
			logger.info("IdleActivityChecker stop");
		}
	}
}
