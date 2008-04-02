package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.Assert;
import org.jsmpp.BindType;
import org.jsmpp.PDUReader;
import org.jsmpp.PDUSender;
import org.jsmpp.InterfaceVersion;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.NumberingPlanIndicator;
import org.jsmpp.PDUStringException;
import org.jsmpp.TypeOfNumber;
import org.jsmpp.bean.BindResp;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.connection.ConnectionFactory;
import org.jsmpp.session.connection.socket.SocketConnectionFactory;
import org.jsmpp.session.state.SessionStateFinder;
import org.jsmpp.session.state.client.ClientSessionStateFinder;
import org.jsmpp.util.PDUComposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientSession extends BaseSession {
    protected final MessageReceiverListener messageReceiverListener;
    protected static final Logger logger = LoggerFactory.getLogger(SMPPSession.class);
    protected final ConnectionFactory connFactory;
    protected final SessionStateFinder<ClientResponseHandler> sessionStateFinder;
    protected final ClientResponseHandler responseHandler;

    public ClientSession(MessageReceiverListener messageReceiverListener) {
        this(new SMPPSessionResponseHandler(messageReceiverListener), messageReceiverListener);
    }

    public ClientSession(ClientResponseHandler responseHandler, MessageReceiverListener messageReceiverListener) {
        this(SocketConnectionFactory.getInstance(), messageReceiverListener, responseHandler);
    }

    public ClientSession(ConnectionFactory connFactory, MessageReceiverListener messageReceiverListener, ClientResponseHandler responseHandler) {
        Assert.notNull(responseHandler);
        Assert.notNull(connFactory);
        Assert.notNull(messageReceiverListener);
        this.responseHandler = responseHandler;
        this.connFactory = connFactory;
        this.messageReceiverListener = messageReceiverListener;
        sessionStateFinder = new ClientSessionStateFinder();
        state = sessionStateFinder.getSessionState(responseHandler, SessionState.CLOSED);
    }

    public ClientSession(String host, int port, BindParameter bindParam, ConnectionFactory connFactory, MessageReceiverListener messageReceiverListener, ClientResponseHandler responseHandler) throws IOException {
        this(connFactory, messageReceiverListener, responseHandler);
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
            pduSender = new PDUSender(conn.getOutputStream(), new PDUComposer());
            pduReader = new PDUReader(conn.getInputStream());
            responseHandler.init(pduSender, pendingResponses, this);

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
        } catch (RuntimeException e) {
            logger.error("IO Error occured", e);
            close();
            throw e;
        }
    }

    private String sendBind(BindType bindType, String systemId, String password, String systemType, InterfaceVersion interfaceVersion, TypeOfNumber addrTon, NumberingPlanIndicator addrNpi, String addressRange) throws PDUStringException, ResponseTimeoutException, InvalidResponseException, NegativeResponseException {
        return sendBind(bindType, systemId, password, systemType, interfaceVersion, addrTon, addrNpi, addressRange, 60000);
    }

    private String sendBind(BindType bindType, String systemId, String password, String systemType, InterfaceVersion interfaceVersion, TypeOfNumber addrTon, NumberingPlanIndicator addrNpi, String addressRange, long timeout) throws PDUStringException, ResponseTimeoutException, InvalidResponseException, NegativeResponseException {
        logger.info("Binding to " + systemId);
        PendingResponse<BindResp> pendingResp = pendingResponses.add(BindResp.class);
        try {
            pduSender.sendBind(bindType, pendingResp.getSequenceNumber(), systemId, password, systemType, interfaceVersion, addrTon, addrNpi, addressRange);
        } catch (RuntimeException e) {
            logger.error("Failed sending bind command", e);
            pendingResponses.remove(pendingResp);
            throw e;
        }
        pendingResponses.wait(pendingResp);
        logger.info("Bind response received");
        return pendingResp.getResponse().getSystemId();
    }

    protected void changeToBoundState(BindType bindType) {
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
    protected void changeState(SessionState newState) {
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

}