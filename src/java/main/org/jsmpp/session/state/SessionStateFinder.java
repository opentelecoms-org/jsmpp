package org.jsmpp.session.state;

import org.jsmpp.extra.SessionState;
import org.jsmpp.session.ResponseHandler;

public interface SessionStateFinder<T extends ResponseHandler> {
    public SMPPSessionState getSessionState(T responseHandler, SessionState newState);
}
