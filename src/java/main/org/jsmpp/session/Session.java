package org.jsmpp.session;

import org.jsmpp.extra.SessionState;


/**
 * @author uudashr
 *
 */
public interface Session {
    /**
     * Get session id.
     * @return the session id.
     */
    String getSessionId();
    void setEnquireLinkTimer(int enquireLinkTimer);
    int getEnquireLinkTimer();
    void setTransactionTimer(long transactionTimer);
    long getTransactionTimer();
    SessionState getSessionState();
    void addSessionStateListener(SessionStateListener l);
    void removeSessionStateListener(SessionStateListener l);
    long getLastActivityTimestamp();
    void close();
    void unbindAndClose();
    
}
