package org.jsmpp.session.state.server;

import java.util.HashMap;

import org.jsmpp.session.ServerSession;
import org.jsmpp.session.state.Mode;
import org.jsmpp.session.state.States;

public class ServerStates extends HashMap<Mode, Class<? extends ServerSessionState>> implements States<ServerSessionState, ServerSession> {

    public ServerStates() {
        put(Mode.OPEN, Open.class);
        put(Mode.CLOSED, Closed.class);
        put(Mode.UNBOUND, Unbound.class);
        put(Mode.BOUND_RX, RX.class);
        put(Mode.BOUND_TX, TX.class);
        put(Mode.BOUND_TRX, TRX.class);
    }

    public ServerSessionState stateForMode(ServerSession session, Mode m) {
        try {
            return get(m).getConstructor(ServerSession.class).newInstance(session);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
