package org.jsmpp.session;

import org.jsmpp.session.state.Mode;

/**
 * This listener contains an event related to the session state.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public interface SessionStateListener {

    public void onStateChange(Mode newState, Mode oldState, Session<?> session);
}
