package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUReader;
import org.jsmpp.PDUSender;
import org.jsmpp.bean.UnbindResp;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.connection.Connection;
import org.jsmpp.session.state.Mode;
import org.jsmpp.session.state.SessionState;
import org.jsmpp.util.RandomSessionIDGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Session<T extends SessionState<?>> {
    private static final Logger logger = LoggerFactory.getLogger(Session.class);

    Connection conn;
    PDUSender pduSender;
    PDUReader pduReader;
    final PendingResponses pendingResponses = new PendingResponses();
    private long lastActivityTimestamp;
    protected T state;
    private SessionStateListener sessionStateListener;
    protected EnquireLinkSender enquireLinkSender = new EnquireLinkSender(this);

    protected int sessionTimer = 5000;
    private String sessionId = new RandomSessionIDGenerator().newSessionId();

    public Session(Connection conn) {
        this();
        this.conn = conn;
    }

    public Session() {
        enquireLinkSender.start();
    }

    public String getSessionId() {
        return sessionId;
    }

    public int getSessionTimer() {
        return sessionTimer;
    }

    public void setSessionTimer(int sessionTimer) {
        this.sessionTimer = sessionTimer;
        if (state.getMode().isBound()) {
            try {
                conn.setSoTimeout(sessionTimer);
            } catch (IOException e) {
                logger.error("Failed setting so_timeout for session timer", e);
            }
        }
    }

    /**
     * This method provided for monitoring need.
     * 
     * @return the last activity timestamp.
     */
    public long getLastActivityTimestamp() {
        return lastActivityTimestamp;
    }

    public long getTransactionTimer() {
        return pendingResponses.getTransactionTimer();
    }

    public void setTransactionTimer(long transactionTimer) {
        pendingResponses.setTransactionTimer(transactionTimer);
    }

    public SessionStateListener getSessionStateListener() {
        return sessionStateListener;
    }

    public void setSessionStateListener(SessionStateListener sessionStateListener) {
        this.sessionStateListener = sessionStateListener;
    }

    /**
     * This method provided for monitoring need.
     */
    protected void notifyActivity() {
        logger.debug("Activity notified");
        lastActivityTimestamp = System.currentTimeMillis();
    }

    public void unbindAndClose() {
        try {
            unbind();
        } catch (ResponseTimeoutException e) {
            logger.error("Timeout waiting unbind response", e);
        } catch (InvalidResponseException e) {
            logger.error("Receive invalid unbind response", e);
        } catch (RuntimeException e) {
            logger.error("IO error found ", e);
        }
        close();
    }

    @Override
    protected void finalize() throws Throwable {
        close();
    }

    public void close() {
        enquireLinkSender.shutdown();
        changeState(Mode.CLOSED);
        try {
            conn.close();
        } catch (IOException e) {
        }
    }

    private void unbind() throws ResponseTimeoutException, InvalidResponseException {
        PendingResponse<UnbindResp> pendingResp = pendingResponses.add(UnbindResp.class);

        try {
            pduSender.sendUnbind(pendingResp.getSequenceNumber());
        } catch (RuntimeException e) {
            logger.error("Failed sending unbind", e);
            pendingResponses.remove(pendingResp);
            throw e;
        }

        pendingResponses.tryWait(pendingResp);
        logger.info("Unbind response received");
        changeState(Mode.UNBOUND);
    }

    public void changeState(Mode mode) {
        if (!state.getMode().equals(mode)) {
            Mode oldState = state.getMode();
            state = getState(mode);
            fireChangeState(mode, oldState);
        }
    }

    protected abstract T getState(Mode mode);

    private void fireChangeState(Mode newState, Mode oldState) {
        if (sessionStateListener != null) {
            sessionStateListener.onStateChange(newState, oldState, this);
        } else {
            logger.warn("SessionStateListener is null");
        }
    }

    public PDUSender getPDUSender() {
        return pduSender;
    }

    public boolean isConnected() {
        return conn.isOpen();
    }

    public boolean isBound() {
        return state.getMode().isBound();
    }

    public PendingResponses getPendingResponses() {
        return pendingResponses;
    }

    public T getSessionState() {
        return state;
    }

}
