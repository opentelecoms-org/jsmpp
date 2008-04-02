package org.jsmpp.session;

import java.util.concurrent.TimeoutException;

import org.jsmpp.PDUReader;
import org.jsmpp.PDUSender;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.connection.Connection;
import org.jsmpp.session.state.SessionStateFinder;
import org.jsmpp.session.state.server.ServerSessionStateFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerSession extends BaseSession {

    protected static final Logger logger = LoggerFactory.getLogger(SMPPServerSession.class);
    protected SessionStateListener sessionStateListener;
    protected final SessionStateFinder<ServerResponseHandler> sessionStateFinder;
    protected ServerResponseHandler responseHandler;
    protected final ServerMessageReceiverListener serverMessageReceiverListener;

    public ServerSession(Connection conn, ServerResponseHandler responseHandler, SessionStateListener sessionStateListener, ServerMessageReceiverListener messageReceiverListener) {
        super(conn);
        pduSender = new PDUSender(conn.getOutputStream());
        pduReader = new PDUReader(conn.getInputStream());
        this.serverMessageReceiverListener = messageReceiverListener;
        this.responseHandler = responseHandler;
        if (responseHandler == null) {
            throw new IllegalArgumentException("ServerResponseHandler can't be null");
        }
        sessionStateFinder = new ServerSessionStateFinder();
        state = sessionStateFinder.getSessionState(responseHandler, SessionState.CLOSED);
        changeState(SessionState.OPEN);
        this.sessionStateListener = sessionStateListener;
        enquireLinkSender = new EnquireLinkSender(this, pduSender);
    }

    @Override
    protected synchronized void changeState(SessionState newState) {
        if (!state.getSessionState().equals(newState)) {
            final SessionState oldState = state.getSessionState();
            // change the session state processor
            state = sessionStateFinder.getSessionState(responseHandler, newState);
            if (newState.isBound()) {
                enquireLinkSender.start();
            }
            fireChangeState(newState, oldState);
        }
    }

    private void fireChangeState(SessionState newState, SessionState oldState) {
        if (sessionStateListener != null) {
            sessionStateListener.onStateChange(newState, oldState, this);
        } else {
            logger.warn("SessionStateListener is null");
        }
    }

    public BindRequest waitForBind(long timeout) throws IllegalStateException, TimeoutException {
        if (getSessionState().equals(SessionState.OPEN)) {
            new PDUReaderWorker(this).start();
            try {
                return responseHandler.waitForRequest(timeout);
            } catch (IllegalStateException e) {
                throw new IllegalStateException("Invocation of waitForBind() has been made", e);
            } catch (TimeoutException e) {
                close();
                throw e;
            }
        }
        throw new IllegalStateException("waitForBind() should be invoked on OPEN state, actual state is " + getSessionState());
    }
}