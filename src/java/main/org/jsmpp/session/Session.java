package org.jsmpp.session;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Hashtable;

import org.jsmpp.BindType;
import org.jsmpp.InterfaceVersion;
import org.jsmpp.InvalidCommandLengthException;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.NumberingPlanIndicator;
import org.jsmpp.PDUReader;
import org.jsmpp.PDUSender;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.TypeOfNumber;
import org.jsmpp.bean.BindResp;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.UnbindResp;
import org.jsmpp.extra.Activity;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ProcessMessageException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.connection.Connection;
import org.jsmpp.session.connection.ConnectionFactory;
import org.jsmpp.session.state.SMPPSessionState;
import org.jsmpp.util.Sequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 *
 */
public class Session {
    private static final Logger logger = LoggerFactory.getLogger(Session.class);
    
    private final Connection conn;
    private final PDUSender pduSender;
    private final PDUReader pduReader;
    private final Sequence sequence = new Sequence(1);
    private final Hashtable<Integer, PendingResponse<? extends Command>> pendingResponse = new Hashtable<Integer, PendingResponse<? extends Command>>();
    private final SMPPSessionHandler sessionHandler = new SMPPSessionHandlerImpl();
    private final Activity activity;
    
    private SMPPSessionState stateProcessor = SMPPSessionState.CLOSED;
    private SessionStateListener sessionStateListener;
    private long transactionTimer = 2000;
    private MessageReceiverListener messageReceiverListener;
    
    public Session(String host, int port, BindType bindType,
            String systemId, String password, String systemType,
            TypeOfNumber addrTon, NumberingPlanIndicator addrNpi,
            String addressRange, PDUSender pduSender, PDUReader pduReader, ConnectionFactory connFactory, Activity activity) throws IOException {
        this.pduSender = pduSender;
        this.pduReader = pduReader;
        this.activity = activity;
        conn = connFactory.createConnection(host, port);
        changeState(SessionState.OPEN);
        
        try {
            new PDUReaderWorker().start();
            bind(bindType, systemId, password, systemType,
                    InterfaceVersion.IF_34, addrTon, addrNpi, addressRange);
            changeToBoundState(bindType);
            
        } catch (PDUStringException e) {
            logger.error("Failed sending bind command", e);
        } catch (NegativeResponseException e) {
            String message = "Receive negative bind response";
            logger.error(message, e);
            conn.close();
            throw new IOException(message + ": " + e.getMessage());
        } catch (InvalidResponseException e) {
            String message = "Receive invalid response of bind";
            logger.error(message, e);
            conn.close();
            throw new IOException(message + ": " + e.getMessage());
        } catch (ResponseTimeoutException e) {
            String message = "Waiting bind response take time to long";
            logger.error(message, e);
            conn.close();
            throw new IOException(message + ": " + e.getMessage());
        } catch (IOException e) {
            logger.error("IO Error occur", e);
            conn.close();
            throw e;
        }
    }
    
    private String bind(BindType bindType, String systemId,
            String password, String systemType,
            InterfaceVersion interfaceVersion, TypeOfNumber addrTon,
            NumberingPlanIndicator addrNpi, String addressRange)
            throws PDUStringException, ResponseTimeoutException,
            InvalidResponseException, NegativeResponseException, IOException {
        int seqNum = sequence.nextValue();
        PendingResponse<BindResp> pendingResp = new PendingResponse<BindResp>(transactionTimer);
        pendingResponse.put(seqNum, pendingResp);
        
        try {
            pduSender.sendBind(conn.getOutputStream(), bindType, seqNum,
                    systemId, password, systemType, interfaceVersion, addrTon,
                    addrNpi, addressRange);
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
        
        if (pendingResp.getResponse().getCommandStatus() != SMPPConstant.STAT_ESME_ROK) {
            throw new NegativeResponseException(pendingResp.getResponse().getCommandStatus());
        }
        
        return pendingResp.getResponse().getSystemId();
    }
    
    private void unbind() throws ResponseTimeoutException, InvalidResponseException, IOException {
        int seqNum = sequence.nextValue();
        PendingResponse<UnbindResp> pendingResp = new PendingResponse<UnbindResp>(transactionTimer);
        pendingResponse.put(seqNum, pendingResp);
        
        try {
            pduSender.sendUnbind(conn.getOutputStream(), seqNum);
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
        
        if (pendingResp.getResponse().getCommandStatus() != SMPPConstant.STAT_ESME_ROK) {
            logger.warn("Receive NON-OK response of unbind");
        }
    }
    
    public SessionState getSessionState() {
        return stateProcessor.getSessionState();
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
        conn.close();
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
    }
    
    private synchronized void changeState(SessionState newState) {
        final SessionState oldState = stateProcessor.getSessionState();
        if (!oldState.equals(newState)) {
            // change the session state processor
            if (newState == SessionState.OPEN) {
                stateProcessor = SMPPSessionState.OPEN;
            } else if (newState == SessionState.BOUND_RX) {
                stateProcessor = SMPPSessionState.BOUND_RX;
            } else if (newState == SessionState.BOUND_TX) {
                stateProcessor = SMPPSessionState.BOUND_TX;
            } else if (newState == SessionState.BOUND_TRX) {
                stateProcessor = SMPPSessionState.BOUND_TRX;
            } else if (newState == SessionState.UNBOUND) {
                stateProcessor = SMPPSessionState.UNBOUND;
            } else if (newState == SessionState.CLOSED) {
                stateProcessor = SMPPSessionState.CLOSED;
            }
        }
    }
    
    private class SMPPSessionHandlerImpl implements SMPPSessionHandler {
        private void fireAcceptDeliverSm(DeliverSm deliverSm) throws ProcessMessageException {
            if (messageReceiverListener != null) {
                messageReceiverListener.onAcceptDeliverSm(deliverSm);
            } else { 
                logger.warn("Receive deliver_sm but MessageReceiverListener is null. Short message = " + new String(deliverSm.getShortMessage()));
            }
        }
        
        public void processDeliverSm(DeliverSm deliverSm) throws ProcessMessageException {
            fireAcceptDeliverSm(deliverSm);
        }
        
        @SuppressWarnings("unchecked")
        public PendingResponse<Command> removeSentItem(int sequenceNumber) {
            return (PendingResponse<Command>)pendingResponse.remove(sequenceNumber);
        }
        
        public void sendDeliverSmResp(int sequenceNumber) throws IOException {
            try {
                pduSender.sendDeliverSmResp(conn.getOutputStream(), sequenceNumber);
                logger.debug("deliver_sm_resp with seq_number " + sequenceNumber + " has been sent");
            } catch (PDUStringException e) {
                logger.error("Failed sending deliver_sm_resp", e);
            }
        }
        
        public void sendEnquireLinkResp(int sequenceNumber) throws IOException {
            pduSender.sendEnquireLinkResp(conn.getOutputStream(), sequenceNumber);
        }
        
        public void sendGenerickNack(int commandStatus, int sequenceNumber) throws IOException {
            pduSender.sendGenericNack(conn.getOutputStream(), commandStatus, sequenceNumber);
        }
        
        public void sendNegativeResponse(int originalCommandId, int commandStatus, int sequenceNumber) throws IOException {
            pduSender.sendHeader(conn.getOutputStream(), originalCommandId | SMPPConstant.MASK_CID_RESP, commandStatus, sequenceNumber);
        }
        
        public void sendUnbindResp(int sequenceNumber) throws IOException {
            pduSender.sendUnbindResp(conn.getOutputStream(), SMPPConstant.STAT_ESME_ROK, sequenceNumber);
        }
    }
    
    private class PDUReaderWorker extends Thread {
        private final DataInputStream in = new DataInputStream(conn.getInputStream());
        @Override
        public void run() {
            logger.info("Starting PDUReaderWorker");
            while (isReadPdu()) {
                readPDU();
            }
            logger.info("PDUReaderWorker stop");
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
                    activity.notifyActivity();
                    stateProcessor.processBindResp(pduHeader, pdu, sessionHandler);
                    break;
                case SMPPConstant.CID_GENERIC_NACK:
                    activity.notifyActivity();
                    stateProcessor.processGenericNack(pduHeader, pdu, sessionHandler);
                    break;
                case SMPPConstant.CID_ENQUIRE_LINK:
                    activity.notifyActivity();
                    stateProcessor.processEnquireLink(pduHeader, pdu, sessionHandler);
                    break;
                case SMPPConstant.CID_ENQUIRE_LINK_RESP:
                    activity.notifyActivity();
                    stateProcessor.processEnquireLinkResp(pduHeader, pdu, sessionHandler);
                    break;
                case SMPPConstant.CID_SUBMIT_SM_RESP:
                    activity.notifyActivity();
                    stateProcessor.processSubmitSmResp(pduHeader, pdu, sessionHandler);
                    break;
                case SMPPConstant.CID_QUERY_SM_RESP:
                    activity.notifyActivity();
                    stateProcessor.processQuerySmResp(pduHeader, pdu, sessionHandler);
                    break;
                case SMPPConstant.CID_DELIVER_SM:
                    activity.notifyActivity();
                    stateProcessor.processDeliverSm(pduHeader, pdu, sessionHandler);
                    break;
                case SMPPConstant.CID_UNBIND:
                    activity.notifyActivity();
                    stateProcessor.processUnbind(pduHeader, pdu, sessionHandler);
                    changeState(SessionState.UNBOUND);
                    break;
                case SMPPConstant.CID_UNBIND_RESP:
                    activity.notifyActivity();
                    stateProcessor.processUnbindResp(pduHeader, pdu, sessionHandler);
                    break;
                default:
                    stateProcessor.processUnknownCid(pduHeader, pdu, sessionHandler);
                }
            } catch (InvalidCommandLengthException e) {
                logger.warn("Receive invalid command length", e);
                try {
                    pduSender.sendGenericNack(conn.getOutputStream(), SMPPConstant.STAT_ESME_RINVCMDLEN, 0);
                } catch (IOException ee) {
                    logger.warn("Failed sending generic nack", ee);
                }
                unbindAndClose();
            } catch (SocketTimeoutException e) {
                enquireLink();
            } catch (IOException e) {
                conn.close();
            }
        }
        
        private synchronized boolean isReadPdu() {
            SessionState sessionState = stateProcessor.getSessionState();
            return sessionState.isBound() || sessionState.equals(SessionState.OPEN);
        }
        
        private void enquireLink() {
         // TODO uud: send enquire link asynchronously
        }
    }
}
