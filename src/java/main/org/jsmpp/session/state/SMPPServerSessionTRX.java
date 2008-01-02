package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.bean.Command;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.ServerResponseHandler;

/**
 * @author uudashr
 *
 */
class SMPPServerSessionTRX extends SMPPServerSessionBoundTX {
    
    @Override
    public SessionState getSessionState() {
        return SessionState.BOUND_TRX;
    }
    
    @Override
    public void processDeliverSmResp(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        SMPPServerSessionBoundRX.processDeliverSmResp0(pduHeader, pdu, responseHandler);
    }
}
