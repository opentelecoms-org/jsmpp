package org.jsmpp.session.state.client;

import java.util.HashMap;

import org.jsmpp.session.ClientSession;
import org.jsmpp.session.state.State;
import org.jsmpp.session.state.States;

public class ClientStates extends HashMap<State, Class<? extends ClientSessionState>> implements States<ClientSessionState, ClientSession> {
    public ClientStates() {
        put(State.OPEN, Open.class);
        put(State.CLOSED, Closed.class);
        put(State.UNBOUND, Unbound.class);
        put(State.BOUND_RX, RX.class);
        put(State.BOUND_TX, TX.class);
        put(State.BOUND_TRX, TRX.class);
    }

    public ClientSessionState sessionStateForState(ClientSession session, State state) {
        try {
            return get(state).getConstructor(ClientSession.class).newInstance(session);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
