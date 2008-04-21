package org.jsmpp.session;

import org.jsmpp.BindType;
import org.jsmpp.extra.SessionState;

/**
 * Context defined session life cycle.<br/>
 * OPEN -> BOUND_TX | BOUND_RX | BOUND_TRX -> UNBOUND -> CLOSE.
 * 
 * @author uudashr
 *
 */
public interface SessionContext extends ActivityNotifier {
    /**
     * Change state to open.
     */
    void open();
    
    /**
     * Change state to bound state.
     * @param bindType
     */
    void bound(BindType bindType);
    
    /**
     * Change state to unbound.
     */
    void unbound();
    
    /**
     * Change state to close.
     */
    void close();
    
    /**
     * Get current session state.
     * 
     * @return the current session state.
     */
    SessionState getSessionState();
    
    /**
     * Get the last activity of a session.
     * 
     * @return the last activity timestamp. 
     */
    long getLastActivityTimestamp();
}