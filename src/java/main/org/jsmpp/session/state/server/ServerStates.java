package org.jsmpp.session.state.server;

import java.util.HashMap;

import org.jsmpp.session.ServerSession;
import org.jsmpp.session.state.State;
import org.jsmpp.session.state.States;

public class ServerStates extends HashMap<State, Class<? extends ServerSessionState>> implements States<ServerSessionState, ServerSession> {

    public ServerStates() {
        put(State.OPEN, Open.class);
        put(State.CLOSED, Closed.class);
        put(State.UNBOUND, Unbound.class);
        put(State.BOUND_RX, RX.class);
        put(State.BOUND_TX, TX.class);
        put(State.BOUND_TRX, TRX.class);
    }

    public ServerSessionState sessionStateForState(ServerSession session, State state) {
        try {
            return get(state).getConstructor(ServerSession.class).newInstance(session);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
