package org.jsmpp.session.state;

import org.jsmpp.session.Session;

public interface States<T extends SessionState<V>, V extends Session<T>> {
    public T stateForMode(V session, Mode m);
}
