package org.jsmpp.session.state.server;

import org.jsmpp.session.ServerSession;
import org.jsmpp.session.state.State;

public class Closed extends ServerSessionState {

    public Closed(ServerSession serverSession) {
        super(serverSession);
    }

    public State getState() {
        return State.CLOSED;
    }
}
