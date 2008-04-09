package org.jsmpp.session.state;

import org.jsmpp.session.Session;

public interface States<T extends SessionState, V extends Session<T>> {
    public T sessionStateForState(V session, State m);
}
