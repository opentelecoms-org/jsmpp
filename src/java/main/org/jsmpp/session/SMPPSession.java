package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.BindType;
import org.jsmpp.DefaultPDUReader;
import org.jsmpp.DefaultPDUSender;
import org.jsmpp.InterfaceVersion;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.NumberingPlanIndicator;
import org.jsmpp.PDUStringException;
import org.jsmpp.TypeOfNumber;
import org.jsmpp.bean.BindResp;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.QuerySmResp;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SubmitSmResp;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.connection.ConnectionFactory;
import org.jsmpp.session.connection.socket.SocketConnectionFactory;
import org.jsmpp.session.state.SessionStateFinder;
import org.jsmpp.session.state.client.ClientSessionStateFinder;
import org.jsmpp.util.DefaultComposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 * @version 2.0
 * 
 */
public class SMPPSession extends BaseSMPPSession {
    static final Logger logger = LoggerFactory.getLogger(SMPPSession.class);

    private final ConnectionFactory connFactory;
    private final SMPPSessionResponseHandler responseHandler = new SMPPSessionResponseHandler(this);

    private final SessionStateFinder<ClientResponseHandler> sessionStateFinder;
    MessageReceiverListener messageReceiverListener;

    public SMPPSession() {
        this(SocketConnectionFactory.getInstance());
    }

    public SMPPSession(ConnectionFactory connFactory) {
        this.connFactory = connFactory;
        sessionStateFinder = new ClientSessionStateFinder();
        state = sessionStateFinder.getSessionState(responseHandler, SessionState.CLOSED);
    }

    public SMPPSession(String host, int port, BindParameter bindParam, ConnectionFactory connFactory) throws IOException {
        this(connFactory);
        connectAndBind(host, port, bindParam);
    }

    public SMPPSession(String host, int port, BindParameter bindParam) throws IOException {
        this();
        connectAndBind(host, port, bindParam);
    }

    public void connectAndBind(String host, int port, BindType bindType, String systemId, String password, String systemType, TypeOfNumber addrTon, NumberingPlanIndicator addrNpi, String addressRange) throws IOException {
        connectAndBind(host, port, new BindParameter(bindType, systemId, password, systemType, addrTon, addrNpi, addressRange));
    }

    public String connectAndBind(String host, int port, BindParameter bindParam) throws IOException {
        logger.debug("Connect and bind to " + host + " port " + port);
        if (pendingResponses.currentSequenceValue() != 1) {
            throw new IOException("Failed connecting");
        }

        conn = connFactory.createConnection(host, port);
        logger.info("Connected");

        changeState(SessionState.OPEN);
        try {
            pduSender = new DefaultPDUSender(conn.getOutputStream(), new DefaultComposer());
            pduReader = new DefaultPDUReader(conn.getInputStream());

            new PDUReaderWorker(this).start();
            String smscSystemId = sendBind(bindParam.getBindType(), bindParam.getSystemId(), bindParam.getPassword(), bindParam.getSystemType(), InterfaceVersion.IF_34, bindParam.getAddrTon(), bindParam.getAddrNpi(), bindParam.getAddressRange());
            changeToBoundState(bindParam.getBindType());

            enquireLinkSender = new EnquireLinkSender(this, pduSender);
            enquireLinkSender.start();
            return smscSystemId;
        } catch (PDUStringException e) {
            throw new IOException("Failed sending bind: some string parameters are invalid ");
        } catch (NegativeResponseException e) {
            String message = "Negative bind response received";
            logger.error(message, e);
            close();
            throw new IOException(message);
        } catch (InvalidResponseException e) {
            String message = "Invalid bind response received";
            logger.error(message, e);
            close();
            throw new IOException(message);
        } catch (ResponseTimeoutException e) {
            String message = "Bind timed out";
            logger.error(message, e);
            close();
            throw new IOException(message);
        } catch (IOException e) {
            logger.error("IO Error occured", e);
            close();
            throw e;
        }
    }

    private String sendBind(BindType bindType, String systemId, String password, String systemType, InterfaceVersion interfaceVersion, TypeOfNumber addrTon, NumberingPlanIndicator addrNpi, String addressRange) throws PDUStringException, ResponseTimeoutException, InvalidResponseException, NegativeResponseException, IOException {
        return sendBind(bindType, systemId, password, systemType, interfaceVersion, addrTon, addrNpi, addressRange, 60000);
    }

    private String sendBind(BindType bindType, String systemId, String password, String systemType, InterfaceVersion interfaceVersion, TypeOfNumber addrTon, NumberingPlanIndicator addrNpi, String addressRange, long timeout) throws PDUStringException, ResponseTimeoutException, InvalidResponseException, NegativeResponseException, IOException {
        PendingResponse<BindResp> pendingResp = pendingResponses.add(BindResp.class);
        try {
            pduSender.sendBind(bindType, pendingResp.getSequenceNumber(), systemId, password, systemType, interfaceVersion, addrTon, addrNpi, addressRange);
        } catch (IOException e) {
            logger.error("Failed sending bind command", e);
            pendingResponses.remove(pendingResp);
            throw e;
        }
        pendingResponses.wait(pendingResp);
        logger.info("Bind response received");
        return pendingResp.getResponse().getSystemId();
    }

    public String submitShortMessage(String serviceType, TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr, TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi, String destinationAddr, ESMClass esmClass, byte protocoId, byte priorityFlag, String scheduleDeliveryTime, String validityPeriod, RegisteredDelivery registeredDelivery, byte replaceIfPresentFlag, DataCoding dataCoding, byte smDefaultMsgId, byte[] shortMessage, OptionalParameter... params) throws PDUStringException, ResponseTimeoutException, InvalidResponseException, NegativeResponseException, IOException {
        PendingResponse<SubmitSmResp> pendingResp = pendingResponses.add(SubmitSmResp.class);
        try {
            pduSender.sendSubmitSm(pendingResp.getSequenceNumber(), serviceType, sourceAddrTon, sourceAddrNpi, sourceAddr, destAddrTon, destAddrNpi, destinationAddr, esmClass, protocoId, priorityFlag, scheduleDeliveryTime, validityPeriod, registeredDelivery, replaceIfPresentFlag, dataCoding, smDefaultMsgId, shortMessage, params);
        } catch (IOException e) {
            logger.error("Failed submit short message", e);
            pendingResponses.remove(pendingResp);
            close();
            throw e;
        }

        pendingResponses.wait(pendingResp);
        logger.debug("Submit sm response received");
        return pendingResp.getResponse().getMessageId();
    }

    public QuerySmResult queryShortMessage(String messageId, TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi, String sourceAddr) throws PDUStringException, ResponseTimeoutException, InvalidResponseException, NegativeResponseException, IOException {
        PendingResponse<QuerySmResp> pendingResp = pendingResponses.add(QuerySmResp.class);
        try {
            pduSender.sendQuerySm(pendingResp.getSequenceNumber(), messageId, sourceAddrTon, sourceAddrNpi, sourceAddr);
        } catch (IOException e) {
            logger.error("Failed to submit short message", e);
            pendingResponses.remove(pendingResp);
            close();
            throw e;
        }

        pendingResponses.wait(pendingResp);
        logger.info("Query sm response received");

        QuerySmResp resp = pendingResp.getResponse();
        if (resp.getMessageId().equals(messageId)) {
            return new QuerySmResult(resp.getFinalDate(), resp.getMessageState(), resp.getErrorCode());
        }
        // message id requested not same as the returned
        throw new InvalidResponseException("Requested message_id doesn't match with the result");
    }

    private void changeToBoundState(BindType bindType) {
        if (bindType.equals(BindType.BIND_TX)) {
            changeState(SessionState.BOUND_TX);
        } else if (bindType.equals(BindType.BIND_RX)) {
            changeState(SessionState.BOUND_RX);
        } else if (bindType.equals(BindType.BIND_TRX)) {
            changeState(SessionState.BOUND_TRX);
        } else {
            throw new IllegalArgumentException("Bind type " + bindType + " not supported");
        }

        try {
            conn.setSoTimeout(sessionTimer);
        } catch (IOException e) {
            logger.error("Failed setting so_timeout for session timer", e);
        }
    }

    @Override
    synchronized void changeState(SessionState newState) {
        if (!state.getSessionState().equals(newState)) {
            final SessionState oldState = state.getSessionState();
            // change the session state processor
            state = sessionStateFinder.getSessionState(responseHandler, newState);
            if (getSessionStateListener() != null) {
                getSessionStateListener().onStateChange(newState, oldState, this);
            } else {
                logger.warn("SessionStateListener is null");
            }
        }
    }

    public MessageReceiverListener getMessageReceiverListener() {
        return messageReceiverListener;
    }

    public void setMessageReceiverListener(MessageReceiverListener messageReceiverListener) {
        this.messageReceiverListener = messageReceiverListener;
    }

    public void setSessionTimer(int sessionTimer) {
        this.sessionTimer = sessionTimer;
        if (state.getSessionState().isBound()) {
            try {
                conn.setSoTimeout(sessionTimer);
            } catch (IOException e) {
                logger.error("Failed setting so_timeout for session timer", e);
            }
        }
    }

    @Override
    protected void finalize() throws Throwable {
        close();
    }
}