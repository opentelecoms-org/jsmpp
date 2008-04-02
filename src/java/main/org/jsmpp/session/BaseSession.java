package org.jsmpp.session;

import java.io.IOException;
import java.util.Random;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUReader;
import org.jsmpp.PDUSender;
import org.jsmpp.bean.UnbindResp;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.connection.Connection;
import org.jsmpp.session.state.SMPPSessionState;
import org.jsmpp.util.IntUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseSession {
    private static final Logger logger = LoggerFactory.getLogger(BaseSession.class);

    private static final Random random = new Random();
    Connection conn;
    PDUSender pduSender;
    PDUReader pduReader;
    final PendingResponses pendingResponses = new PendingResponses();
    private long lastActivityTimestamp;
    private String sessionId = generateSessionId();
    protected SMPPSessionState state;
    private SessionStateListener sessionStateListener;
    protected EnquireLinkSender enquireLinkSender;
    protected int sessionTimer = 5000;

    public BaseSession(Connection conn) {
        this.conn = conn;
    }

    public BaseSession() {

    }

    public static final String generateSessionId() {
        return IntUtil.toHexString(random.nextInt());
    }

    public int getSessionTimer() {
        return sessionTimer;
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

    synchronized boolean isConnected() {
        return state.getSessionState().isBound() || state.getSessionState().equals(SessionState.OPEN);
    }

    public SessionState getSessionState() {
        return state.getSessionState();
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
        changeState(SessionState.CLOSED);
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
        changeState(SessionState.UNBOUND);
    }

    abstract void changeState(SessionState state);

    public String getSessionId() {
        return sessionId;
    }

    public PDUSender getPDUSender() {
        return pduSender;
    }
}
