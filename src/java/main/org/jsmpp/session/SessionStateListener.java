package org.jsmpp.session;

import org.jsmpp.extra.SessionState;

/**
 * This listener contains an event related to the session state.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public interface SessionStateListener {

    /**
     * Raised when the session state changed.
     * 
     * @param newState is the new state.
     * @param oldState is the old state.
     * @param source is source of changed state.
     */
    public void onStateChange(SessionState newState, SessionState oldState,
            Object source);
}
