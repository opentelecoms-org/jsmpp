package org.jsmpp.session;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.SocketTimeoutException;
import java.util.Hashtable;
import java.util.Random;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.jsmpp.BindType;
import org.jsmpp.DefaultPDUReader;
import org.jsmpp.DefaultPDUSender;
import org.jsmpp.InvalidCommandLengthException;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.NumberingPlanIndicator;
import org.jsmpp.PDUReader;
import org.jsmpp.PDUSender;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.SynchronizedPDUSender;
import org.jsmpp.TypeOfNumber;
import org.jsmpp.bean.Bind;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.DeliverSmResp;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.EnquireLinkResp;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.bean.UnbindResp;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.connection.Connection;
import org.jsmpp.session.state.SMPPServerSessionState;
import org.jsmpp.util.IntUtil;
import org.jsmpp.util.MessageId;
import org.jsmpp.util.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 *
 */
public class SMPPServerSession {
    private static final Logger logger = LoggerFactory.getLogger(SMPPServerSession.class);
    private static final Random random = new Random();
    
    private final Connection conn;
    private final DataInputStream in;
    private final OutputStream out;
    
    private long lastActivityTimestamp;
    
    private final Sequence sequence = new Sequence(1);
    private final Hashtable<Integer, PendingResponse<? extends Command>> pendingResponse = new Hashtable<Integer, PendingResponse<? extends Command>>();
    
    private final PDUSender pduSender;
    private final PDUReader pduReader;
    
    private int sessionTimer = 5000;
    private long transactionTimer = 2000;
    
    private SessionStateListener sessionStateListener;
    private final ServerResponseHandler responseHandler = new ResponseHandlerImpl();
    
    private SMPPServerSessionState stateProcessor = SMPPServerSessionState.CLOSED;
    
    private ServerMessageReceiverListener messageReceiverListener;
    private String sessionId = generateSessionId();
    private final EnquireLinkSender enquireLinkSender;
    private BindRequestReceiver bindRequestReceiver = new BindRequestReceiver(responseHandler);
    
    public SMPPServerSession(Connection conn,
            SessionStateListener sessionStateListener,
            ServerMessageReceiverListener messageReceiverListener) {
        this(conn, sessionStateListener, messageReceiverListener, new SynchronizedPDUSender(new DefaultPDUSender()), new DefaultPDUReader());
    }
    
    public SMPPServerSession(Connection conn,
            SessionStateListener sessionStateListener,
            ServerMessageReceiverListener messageReceiverListener,
            PDUSender pduSender, PDUReader pduReader) {
        changeState(SessionState.OPEN);
        this.conn = conn;
        this.sessionStateListener = sessionStateListener;
        this.messageReceiverListener = messageReceiverListener;
        this.pduSender = pduSender;
        this.pduReader = pduReader;
        this.in = new DataInputStream(conn.getInputStream());
        this.out = conn.getOutputStream();
        enquireLinkSender = new EnquireLinkSender();
    }
    
    /**
     * Wait for bind request.
     * 
     * @param timeout is the timeout.
     * @return the {@link BindRequest}.
     * @throws IllegalStateException if this invocation of this method has been
     *         made or invoke when state is not OPEN.
     * @throws TimeoutException if the timeout has been reach and
     *         {@link SMPPServerSession} are no more valid because the
     *         connection will be close automatically.
     */
    public BindRequest waitForBind(long timeout) throws IllegalStateException, TimeoutException {
        if (getSessionState().equals(SessionState.OPEN)) {
            new PDUReaderWorker().start();
            try {
                return bindRequestReceiver.waitForRequest(timeout);
            } catch (IllegalStateException e) {
                throw new IllegalStateException("Invocation of waitForBind() has been made", e);
            } catch (TimeoutException e) {
                close();
                throw e;
            }
        } else {
            throw new IllegalStateException("waitForBind() should be invoked on OPEN state, actual state is " + SessionState.OPEN);
        }
    }
    
    private synchronized boolean isReadPdu() {
        SessionState sessionState = stateProcessor.getSessionState();
        return sessionState.isBound() || sessionState.equals(SessionState.OPEN);
    }
    
    public void deliverShortMessage(String serviceType,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, TypeOfNumber destAddrTon,
            NumberingPlanIndicator destAddrNpi, String destinationAddr,
            ESMClass esmClass, byte protocoId, byte priorityFlag,
            String scheduleDeliveryTime, String validityPeriod,
            RegisteredDelivery registeredDelivery, byte replaceIfPresent,
            DataCoding dataCoding, byte smDefaultMsgId, byte[] shortMessage,
            OptionalParameter... optionalParameters) throws PDUStringException,
            ResponseTimeoutException, InvalidResponseException,
            NegativeResponseException, IOException {
        
        int seqNum = sequence.nextValue();
        PendingResponse<DeliverSmResp> pendingResp = new PendingResponse<DeliverSmResp>(transactionTimer);
        pendingResponse.put(seqNum, pendingResp);
        try {
            pduSender.sendDeliverSm(out, seqNum, serviceType, sourceAddrTon,
                    sourceAddrNpi, sourceAddr, destAddrTon, destAddrNpi,
                    destinationAddr, esmClass, protocoId, priorityFlag,
                    registeredDelivery, dataCoding, shortMessage, optionalParameters);
            
        } catch (IOException e) {
            logger.error("Failed deliver short message", e);
            pendingResponse.remove(seqNum);
            close();
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
        
        if (pendingResp.getResponse().getCommandStatus() != SMPPConstant.STAT_ESME_ROK) {
            throw new NegativeResponseException(pendingResp.getResponse().getCommandStatus());
        }
    }
    
    private MessageId fireAcceptSubmitSm(SubmitSm submitSm) throws ProcessRequestException {
        if (messageReceiverListener != null) {
            return messageReceiverListener.onAcceptSubmitSm(submitSm, this);
        }
        throw new ProcessRequestException("MessageReceveiverListener hasn't been set yet", SMPPConstant.STAT_ESME_RX_R_APPN);
    }
    
    private QuerySmResult fireAcceptQuerySm(QuerySm querySm) throws ProcessRequestException {
        if (messageReceiverListener != null) {
            return messageReceiverListener.onAcceptQuerySm(querySm, this);
        }
        throw new ProcessRequestException("MessageReceveiverListener hasn't been set yet", SMPPConstant.STAT_ESME_RINVDFTMSGID);
    }
    
    private synchronized void changeState(SessionState newState) {
        if (!stateProcessor.getSessionState().equals(newState)) {
            final SessionState oldState = stateProcessor.getSessionState();
            
            // change the session state processor
            if (newState == SessionState.OPEN) {
                stateProcessor = SMPPServerSessionState.OPEN;
            } else if (newState == SessionState.BOUND_RX) {
                stateProcessor = SMPPServerSessionState.BOUND_RX;
            } else if (newState == SessionState.BOUND_TX) {
                stateProcessor = SMPPServerSessionState.BOUND_TX;
            } else if (newState == SessionState.BOUND_TRX) {
                stateProcessor = SMPPServerSessionState.BOUND_TRX;
            } else if (newState == SessionState.UNBOUND) {
                stateProcessor = SMPPServerSessionState.UNBOUND;
            } else if (newState == SessionState.CLOSED) {
                stateProcessor = SMPPServerSessionState.CLOSED;
            }
            if (newState.isBound()) {
                enquireLinkSender.start();
            }
            fireChangeState(newState, oldState);
        }
    }
    
    public synchronized SessionState getSessionState() {
        return stateProcessor.getSessionState();
    }
    
    public String getSessionId() {
        return sessionId;
    }
    
    public int getSessionTimer() {
        return sessionTimer;
    }
    
    public void setSessionTimer(int sessionTimer) {
        this.sessionTimer = sessionTimer;
        if (stateProcessor.getSessionState().isBound()) {
            try {
                conn.setSoTimeout(sessionTimer);
            } catch (IOException e) {
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
    
    public ServerMessageReceiverListener getMessageReceiverListener() {
        return messageReceiverListener;
    }
    
    public void setMessageReceiverListener(
            ServerMessageReceiverListener messageReceiverListener) {
        this.messageReceiverListener = messageReceiverListener;
    }
    
    public SessionStateListener getSessionStateListener() {
        return sessionStateListener;
    }
    
    public void setSessionStateListener(
            SessionStateListener sessionStateListener) {
        this.sessionStateListener = sessionStateListener;
    }
    
    /**
     * This method provided for monitoring need.
     * 
     * @return the last activity timestamp.
     */
    public long getLastActivityTimestamp() {
        return lastActivityTimestamp;
    }
    
    private void fireChangeState(SessionState newState, SessionState oldState) {
        if (sessionStateListener != null) {
            sessionStateListener.onStateChange(newState, oldState, this);
        } else {
            logger.warn("SessionStateListener is null");
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
        close();
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
     * Close the connection.
     */
    public void close() {
        changeState(SessionState.CLOSED);
        try {
            conn.close();
        } catch (IOException e) {
        }
    }
    
    private synchronized static final String generateSessionId() {
        return IntUtil.toHexString(random.nextInt());
    }
    
    private class ResponseHandlerImpl implements ServerResponseHandler {
        
        
        
        @SuppressWarnings("unchecked")
        public PendingResponse<Command> removeSentItem(int sequenceNumber) {
            return (PendingResponse<Command>)pendingResponse.remove(sequenceNumber);
        }
        
        public void notifyUnbonded() {
            changeState(SessionState.UNBOUND);
        }
        
        public void sendEnquireLinkResp(int sequenceNumber) throws IOException {
            logger.debug("Sending enquire_link_resp");
            pduSender.sendEnquireLinkResp(out, sequenceNumber);
        }

        public void sendGenerickNack(int commandStatus, int sequenceNumber)
                throws IOException {
            pduSender.sendGenericNack(out, commandStatus, sequenceNumber);
        }

        public void sendNegativeResponse(int originalCommandId,
                int commandStatus, int sequenceNumber) throws IOException {
            pduSender.sendHeader(out, originalCommandId | SMPPConstant.MASK_CID_RESP, commandStatus, sequenceNumber);
        }

        public void sendUnbindResp(int sequenceNumber) throws IOException {
            pduSender.sendUnbindResp(out, SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        }
        
        public void sendBindResp(String systemId, BindType bindType, int sequenceNumber) throws IOException {
            if (bindType.equals(BindType.BIND_RX)) {
                changeState(SessionState.BOUND_RX);
            } else if (bindType.equals(BindType.BIND_TX)) {
                changeState(SessionState.BOUND_TX);
            } else if (bindType.equals(BindType.BIND_TRX)) {
                changeState(SessionState.BOUND_TRX);
            }
            try {
                pduSender.sendBindResp(out, bindType.commandId() | SMPPConstant.MASK_CID_RESP, sequenceNumber, systemId);
            } catch (PDUStringException e) {
                logger.error("Failed sending bind response", e);
                // FIXME uud: validate the systemId when the setting up the value, so it never throws PDUStringException on above block
            }
        }
        
        public void sendSubmitSmResponse(MessageId messageId, int sequenceNumber)
                throws IOException {
            try {
                pduSender.sendSubmitSmResp(out, sequenceNumber, messageId.getValue());
            } catch (PDUStringException e) {
                /*
                 * There should be no PDUStringException thrown since creation
                 * of MessageId should be save.
                 */
                logger.error("SYSTEM ERROR. Failed sending submitSmResp", e);
            }
        }
        
        public void processBind(Bind bind) {
            bindRequestReceiver.notifyAcceptBind(bind);
        }
        
        public MessageId processSubmitSm(SubmitSm submitSm)
                throws ProcessRequestException {
            return fireAcceptSubmitSm(submitSm);
        }
        
        public QuerySmResult processQuerySm(QuerySm querySm)
                throws ProcessRequestException {
            return fireAcceptQuerySm(querySm);
        }
    }
    
    private class PDUReaderWorker extends Thread {
        @Override
        public void run() {
            logger.info("Starting PDUReaderWorker");
            while (isReadPdu()) {
                readPDU();
            }
            close();
            logger.info("PDUReaderWorker stop");
        }
        
        private void readPDU() {
            try {
                Command pduHeader = null;
                byte[] pdu = null;

                pduHeader = pduReader.readPDUHeader(in);
                pdu = pduReader.readPDU(in, pduHeader);
                
                switch (pduHeader.getCommandId()) {
                case SMPPConstant.CID_BIND_RECEIVER:
                case SMPPConstant.CID_BIND_TRANSMITTER:
                case SMPPConstant.CID_BIND_TRANSCEIVER:
                    notifyActivity();
                    stateProcessor.processBind(pduHeader, pdu, responseHandler);
                    break;
                case SMPPConstant.CID_GENERIC_NACK:
                    notifyActivity();
                    stateProcessor.processGenericNack(pduHeader, pdu, responseHandler);
                    break;
                case SMPPConstant.CID_ENQUIRE_LINK:
                    notifyActivity();
                    stateProcessor.processEnquireLink(pduHeader, pdu, responseHandler);
                    break;
                case SMPPConstant.CID_ENQUIRE_LINK_RESP:
                    notifyActivity();
                    stateProcessor.processEnquireLinkResp(pduHeader, pdu, responseHandler);
                    break;
                case SMPPConstant.CID_SUBMIT_SM:
                    notifyActivity();
                    stateProcessor.processSubmitSm(pduHeader, pdu, responseHandler);
                    break;
                case SMPPConstant.CID_QUERY_SM_RESP:
                    notifyActivity();
                    stateProcessor.processQuerySm(pduHeader, pdu, responseHandler);
                    break;
                case SMPPConstant.CID_DELIVER_SM_RESP:
                    notifyActivity();
                    stateProcessor.processDeliverSmResp(pduHeader, pdu, responseHandler);
                    break;
                case SMPPConstant.CID_UNBIND:
                    notifyActivity();
                    stateProcessor.processUnbind(pduHeader, pdu, responseHandler);
                    break;
                case SMPPConstant.CID_UNBIND_RESP:
                    notifyActivity();
                    stateProcessor.processUnbindResp(pduHeader, pdu, responseHandler);
                    break;
                default:
                    stateProcessor.processUnknownCid(pduHeader, pdu, responseHandler);
                }
            } catch (InvalidCommandLengthException e) {
                logger.warn("Receive invalid command length", e);
                try {
                    pduSender.sendGenericNack(out, SMPPConstant.STAT_ESME_RINVCMDLEN, 0);
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
         * This method provided for monitoring need.
         */
        private void notifyActivity() {
            logger.debug("Activity notified");
            lastActivityTimestamp = System.currentTimeMillis();
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
                synchronized (sendingEnquireLink) {
                    sendingEnquireLink.notify();
                }
            }
        }
        
        /**
         * Ensure we have proper link.
         * 
         * @throws ResponseTimeoutException if there is no valid response after defined millisecond.
         * @throws InvalidResponseException if there is invalid response found.
         * @throws IOException if there is an IO error found.
         */
        private void sendEnquireLink() throws ResponseTimeoutException, InvalidResponseException, IOException {
            int seqNum = sequence.nextValue();
            PendingResponse<EnquireLinkResp> pendingResp = new PendingResponse<EnquireLinkResp>(transactionTimer);
            pendingResponse.put(seqNum, pendingResp);
            
            try {
                logger.debug("Sending enquire_link");
                pduSender.sendEnquireLink(out, seqNum);
            } catch (IOException e) {
                logger.error("Failed sending enquire link", e);
                pendingResponse.remove(seqNum);
                throw e;
            }
            
            try {
                pendingResp.waitDone();
                logger.debug("Enquire link response received");
            } catch (ResponseTimeoutException e) {
                pendingResponse.remove(seqNum);
                throw e;
            } catch (InvalidResponseException e) {
                pendingResponse.remove(seqNum);
                throw e;
            }
            
            if (pendingResp.getResponse().getCommandStatus() != SMPPConstant.STAT_ESME_ROK) {
                // this is ok, we just want to enquire we have proper link.
                logger.warn("Receive NON-OK response of enquire link: " + pendingResp.getResponse().getCommandIdAsHex());
            }
        }
    }
}
