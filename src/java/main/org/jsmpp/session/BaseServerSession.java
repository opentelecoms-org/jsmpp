package org.jsmpp.session;

import java.util.concurrent.TimeoutException;

import org.jsmpp.DefaultPDUReader;
import org.jsmpp.DefaultPDUSender;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.connection.Connection;
import org.jsmpp.session.state.SessionStateFinder;
import org.jsmpp.session.state.server.ServerSessionStateFinder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseServerSession extends BaseSMPPSession {

    static final Logger logger = LoggerFactory.getLogger(BaseServerSession.class);

    private SessionStateListener sessionStateListener;

    private final SessionStateFinder<ServerResponseHandler> sessionStateFinder;

    final SMPPServerSessionResponseHandler responseHandler;
    ServerMessageReceiverListener messageReceiverListener;
    BindRequestReceiver bindRequestReceiver;

    public BaseServerSession(Connection conn, SessionStateListener sessionStateListener, ServerMessageReceiverListener messageReceiverListener) {
        super(conn);
        this.responseHandler = new SMPPServerSessionResponseHandler(this);
        pduSender = new DefaultPDUSender(conn.getOutputStream());
        pduReader = new DefaultPDUReader(conn.getInputStream());

        sessionStateFinder = new ServerSessionStateFinder();
        state = sessionStateFinder.getSessionState(responseHandler, SessionState.CLOSED);
        changeState(SessionState.OPEN);
        this.sessionStateListener = sessionStateListener;
        this.messageReceiverListener = messageReceiverListener;
        enquireLinkSender = new EnquireLinkSender(this, pduSender);
        bindRequestReceiver = new BindRequestReceiver(responseHandler);
    }

    @Override
    synchronized void changeState(SessionState newState) {
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

    public ServerMessageReceiverListener getMessageReceiverListener() {
        return messageReceiverListener;
    }

    public void setMessageReceiverListener(ServerMessageReceiverListener messageReceiverListener) {
        this.messageReceiverListener = messageReceiverListener;
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
                return bindRequestReceiver.waitForRequest(timeout);
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
