package org.jsmpp.session;

import org.jsmpp.BindType;
import org.jsmpp.extra.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 *
 */
public abstract class AbstractSessionContext implements SessionContext {
    private static final Logger logger = LoggerFactory.getLogger(AbstractSessionContext.class);
    private long lastActivityTimestamp;
    private SessionStateListener sessionStateListener;
    
    public AbstractSessionContext() {
    }
    
    public AbstractSessionContext(SessionStateListener sessionStateListener) {
        this.sessionStateListener = sessionStateListener;
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
    
    public void setSessionStateListener(
            SessionStateListener sessionStateListener) {
        this.sessionStateListener = sessionStateListener;
    }
    
    public SessionStateListener getSessionStateListener() {
        return sessionStateListener;
    }
    
    protected void fireStateChanged(SessionState newState, SessionState oldState, Object source) {
        if (sessionStateListener != null) {
            sessionStateListener.onStateChange(newState, oldState, source);
        } else {
            logger.warn("SessionStateListener is never been set");
        }
    }
    
    public void notifyActivity() {
        logger.debug("Activity notified");
        lastActivityTimestamp = System.currentTimeMillis();
    }
    
    public long getLastActivityTimestamp() {
        return lastActivityTimestamp;
    }
    
    protected abstract void changeState(SessionState newState);
}
