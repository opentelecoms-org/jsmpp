package org.jsmpp.session;

import org.jsmpp.BindType;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.state.SMPPSessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 *
 */
public class SMPPSessionContext implements SessionContext {
    private static final Logger logger = LoggerFactory.getLogger(SMPPSessionContext.class);
    
    private final SMPPSession smppSession;
    private SMPPSessionState stateProcessor = SMPPSessionState.CLOSED;
    private SessionStateListener sessionStateListener;
    private long lastActivityTimestamp;
    
    public SMPPSessionContext(SMPPSession smppSession, SessionStateListener sessionStateListener) {
        this.smppSession = smppSession;
        this.sessionStateListener = sessionStateListener;
    }
    
    public SMPPSessionContext(SMPPSession smppSession) {
        this(smppSession, null);
    }
    
    public synchronized void open() {
        changeState(SessionState.OPEN);
    }
    
    public synchronized void bound(BindType bindType) {
        if (bindType.equals(BindType.BIND_TX)) {
            changeState(SessionState.BOUND_TX);
        } else if (bindType.equals(BindType.BIND_RX)) {
            changeState(SessionState.BOUND_RX);
        } else if (bindType.equals(BindType.BIND_TRX)) {
            changeState(SessionState.BOUND_TRX);
        } else {
            throw new IllegalArgumentException("Bind type " + bindType + " not supported");
        }
    }
    
    public synchronized void unbound() {
        changeState(SessionState.UNBOUND);
    }
    
    public synchronized void close() {
        changeState(SessionState.CLOSED);
    }
    
    public synchronized SMPPSessionState getStateProcessor() {
        return stateProcessor;
    }
    
    public synchronized SessionState getSessionState() {
        return stateProcessor.getSessionState();
    }
    
    public void notifyActivity() {
        logger.debug("Activity notified");
        lastActivityTimestamp = System.currentTimeMillis();
    }
    
    public long getLastActivityTimestamp() {
        return lastActivityTimestamp;
    }
    
    public void setSessionStateListener(
            SessionStateListener sessionStateListener) {
        this.sessionStateListener = sessionStateListener;
    }
    
    public SessionStateListener getSessionStateListener() {
        return sessionStateListener;
    }
    
    private void changeState(SessionState newState) {
        if (!stateProcessor.getSessionState().equals(newState)) {
            final SessionState oldState = stateProcessor.getSessionState();
            
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
            fireChangeState(newState, oldState);
        }
    }
    
    private void fireChangeState(SessionState newState, SessionState oldState) {
        if (sessionStateListener != null) {
            sessionStateListener.onStateChange(newState, oldState, smppSession);
        } else {
            logger.warn("SessionStateListener is never been set");
        }
    }
}
