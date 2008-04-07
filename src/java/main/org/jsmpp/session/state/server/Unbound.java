package org.jsmpp.session.state.server;

import org.jsmpp.session.ServerSession;
import org.jsmpp.session.state.State;

/**
 * @author uudashr
 * 
 */
public class Unbound extends ServerSessionState {

    public Unbound(ServerSession serverSession) {
        super(serverSession);
    }

    public State getState() {
        return State.UNBOUND;
    }
}
