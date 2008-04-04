package org.jsmpp.session.state.client;

import java.util.HashMap;

import org.jsmpp.session.ClientSession;
import org.jsmpp.session.state.Mode;
import org.jsmpp.session.state.States;

public class ClientStates extends HashMap<Mode, Class<? extends ClientSessionState>> implements States<ClientSessionState, ClientSession> {
    public ClientStates() {
        put(Mode.OPEN, Open.class);
        put(Mode.CLOSED, Closed.class);
        put(Mode.UNBOUND, Unbound.class);
        put(Mode.BOUND_RX, RX.class);
        put(Mode.BOUND_TX, TX.class);
        put(Mode.BOUND_TRX, TRX.class);
    }

    public ClientSessionState stateForMode(ClientSession session, Mode m) {
        try {
            return get(m).getConstructor(ClientSession.class).newInstance(session);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
