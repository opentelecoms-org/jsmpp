package org.jsmpp.session.state.client;

import org.jsmpp.extra.SessionState;
import org.jsmpp.session.ClientResponseHandler;
import org.jsmpp.session.state.SMPPSessionState;
import org.jsmpp.session.state.SessionStateFinder;

public class ClientSessionStateFinder implements SessionStateFinder<ClientResponseHandler> {

    public SMPPSessionState getSessionState(ClientResponseHandler responseHandler, SessionState newState) {
        if (newState == SessionState.OPEN) {
            return new SMPPSessionOpen(responseHandler);
        } else if (newState == SessionState.BOUND_RX) {
            return new SMPPSessionBoundRX(responseHandler);
        } else if (newState == SessionState.BOUND_TX) {
            return new SMPPSessionBoundTX(responseHandler);
        } else if (newState == SessionState.BOUND_TRX) {
            return new SMPPSessionBoundTRX(responseHandler);
        } else if (newState == SessionState.UNBOUND) {
            return new SMPPSessionUnbound(responseHandler);
        } else if (newState == SessionState.CLOSED) {
            return new SMPPSessionClosed(responseHandler);
        }
        throw new IllegalArgumentException("Invalid session state " + newState);
    }
}
