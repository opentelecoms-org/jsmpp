package org.jsmpp.session.state.server;

import org.jsmpp.extra.SessionState;
import org.jsmpp.session.ServerResponseHandler;

/**
 * @author uudashr
 * 
 */
public class SMPPServerSessionUnbound extends SMPPServerSessionClosed {

    public SMPPServerSessionUnbound(ServerResponseHandler responseHandler) {
        super(responseHandler);
    }

    @Override
    public SessionState getSessionState() {
        return SessionState.UNBOUND;
    }
}
