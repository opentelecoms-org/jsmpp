package org.jsmpp.session;

import java.util.concurrent.TimeoutException;

import org.jsmpp.PDUReader;
import org.jsmpp.PDUSender;
import org.jsmpp.session.connection.Connection;
import org.jsmpp.session.state.Mode;
import org.jsmpp.session.state.server.Closed;
import org.jsmpp.session.state.server.ServerSessionState;
import org.jsmpp.session.state.server.ServerStates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerSession extends Session<ServerSessionState> {

    protected static final Logger logger = LoggerFactory.getLogger(ServerSession.class);
    protected ServerResponseHandler responseHandler;
    protected ServerMessageReceiverListener serverMessageReceiverListener;
    private BindRequestReceiver bindRequestReceiver = new BindRequestReceiver();
    protected ServerStates states;

    public ServerSession(Connection conn, ServerStates states, ServerResponseHandler responseHandler, SessionStateListener sessionStateListener) {
        super(conn);
        this.states = states;
        pduSender = new PDUSender(conn.getOutputStream());
        pduReader = new PDUReader(conn.getInputStream());
        this.responseHandler = responseHandler;
        if (responseHandler == null) {
            throw new IllegalArgumentException("ServerResponseHandler can't be null");
        }

        state = new Closed(this);
        changeState(Mode.OPEN);
        setSessionStateListener(sessionStateListener);
    }

    public void setServerMessageReceiverListener(ServerMessageReceiverListener listener) {
        this.serverMessageReceiverListener = listener;
    }

    public BindRequest waitForBind(long timeout) throws IllegalStateException {
        if (state.getMode().equals(Mode.OPEN)) {
            new PDUReaderWorker(this).start();
            try {
                return bindRequestReceiver.waitForRequest(timeout);
            } catch (IllegalStateException e) {
                throw new IllegalStateException("Invocation of waitForBind() has been made", e);
            } catch (TimeoutException e) {
                close();
                throw new RuntimeException(e);
            }
        }
        throw new IllegalStateException("waitForBind() should be invoked on OPEN state, actual state is " + state);
    }

    public ServerResponseHandler getResponseHandler() {
        return responseHandler;
    }

    public BindRequestReceiver getBindRequestReceiver() {
        return bindRequestReceiver;
    }

    @Override
    protected ServerSessionState getState(Mode mode) {
        return states.stateForMode(this, mode);
    }

    public ServerMessageReceiverListener getMessageReceiverListener() {
        return serverMessageReceiverListener;
    }
}