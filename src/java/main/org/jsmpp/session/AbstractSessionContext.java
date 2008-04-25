package org.jsmpp.session;

import java.util.ArrayList;
import java.util.List;

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
    private List<SessionStateListener> sessionStateListeners = new ArrayList<SessionStateListener>();
    
    public AbstractSessionContext() {
    }
    
    public AbstractSessionContext(SessionStateListener sessionStateListener) {
        sessionStateListeners.add(sessionStateListener);
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
    
    public void addSessionStateListener(
            SessionStateListener l) {
        sessionStateListeners.add(l);
    }
    
    public void removeSessionStateListener(SessionStateListener l) {
        sessionStateListeners.remove(l);
    }
    
    protected void fireStateChanged(SessionState newState,
            SessionState oldState, Object source) {
        for (SessionStateListener l : sessionStateListeners) {
            l.onStateChange(newState, oldState, source);
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
