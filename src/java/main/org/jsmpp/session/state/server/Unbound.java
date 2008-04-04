package org.jsmpp.session.state.server;

import org.jsmpp.session.ServerSession;
import org.jsmpp.session.state.Mode;

/**
 * @author uudashr
 * 
 */
public class Unbound extends ServerSessionState {

    public Unbound(ServerSession serverSession) {
        super(serverSession);
    }

    public Mode getMode() {
        return Mode.UNBOUND;
    }
}
