package org.jsmpp.session.state.server;

import org.jsmpp.extra.SessionState;
import org.jsmpp.session.ServerResponseHandler;
import org.jsmpp.session.state.SMPPSessionState;
import org.jsmpp.session.state.SessionStateFinder;

public class ServerSessionStateFinder implements SessionStateFinder<ServerResponseHandler> {

    public SMPPSessionState getSessionState(ServerResponseHandler responseHandler, SessionState newState) {
        if (newState == SessionState.OPEN) {
            return new SMPPServerSessionOpen(responseHandler);
        } else if (newState == SessionState.BOUND_RX) {
            return new SMPPServerSessionBoundRX(responseHandler);
        } else if (newState == SessionState.BOUND_TX) {
            return new SMPPServerSessionBoundTX(responseHandler);
        } else if (newState == SessionState.BOUND_TRX) {
            return new SMPPServerSessionBoundTRX(responseHandler);
        } else if (newState == SessionState.UNBOUND) {
            return new SMPPServerSessionUnbound(responseHandler);
        } else if (newState == SessionState.CLOSED) {
            return new SMPPServerSessionClosed(responseHandler);
        }
        throw new IllegalArgumentException("Invalid session state " + newState);
    }
}
