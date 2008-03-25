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

public abstract class BaseSMPPSession {
    private static final Logger logger = LoggerFactory.getLogger(BaseSMPPSession.class);

    private static final Random random = new Random();
    Connection conn;
    PDUSender pduSender;
    PDUReader pduReader;
    final PendingResponses pendingResponses = new PendingResponses();
    protected int sessionTimer = 5000;
    private long lastActivityTimestamp;
    private String sessionId = generateSessionId();
    protected SMPPSessionState state;
    private SessionStateListener sessionStateListener;
    protected EnquireLinkSender enquireLinkSender;

    public BaseSMPPSession(Connection conn) {
        this.conn = conn;
    }

    public BaseSMPPSession() {

    }

    public static final String generateSessionId() {
        return IntUtil.toHexString(random.nextInt());
    }

    /**
     * This method provided for monitoring need.
     * 
     * @return the last activity timestamp.
     */
    public long getLastActivityTimestamp() {
        return lastActivityTimestamp;
    }

    public int getSessionTimer() {
        return sessionTimer;
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
        } catch (IOException e) {
            logger.error("IO error found ", e);
        }
        close();
    }

    public void close() {
        changeState(SessionState.CLOSED);
        try {
            conn.close();
        } catch (IOException e) {
        }
    }

    private void unbind() throws ResponseTimeoutException, InvalidResponseException, IOException {
        PendingResponse<UnbindResp> pendingResp = pendingResponses.add(UnbindResp.class);

        try {
            pduSender.sendUnbind(pendingResp.getSequenceNumber());
        } catch (IOException e) {
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

}
