package org.jsmpp.session.state.server;

import org.jsmpp.session.ServerSession;
import org.jsmpp.session.state.Mode;

public class Closed extends ServerSessionState {

    public Closed(ServerSession serverSession) {
        super(serverSession);
    }

    public Mode getMode() {
        return Mode.CLOSED;
    }
}
