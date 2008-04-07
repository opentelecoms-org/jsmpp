package org.jsmpp.session.state.client;

import java.io.IOException;

import org.jsmpp.session.ClientSession;
import org.jsmpp.session.state.State;

/**
 * This class is unbound state implementation of {@link State}. All this method
 * is throw {@link IOException} since when the state is unbound we should not
 * give any positive response.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public class Unbound extends ClientSessionState {

    public Unbound(ClientSession session) {
        super(session);
    }

    public State getState() {
        return State.UNBOUND;
    }
}
