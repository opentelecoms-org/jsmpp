package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.bean.Command;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.ResponseHandler;

/**
 * This class is bound_trx state implementation of {@link SMPPSessionState}.
 * Response both to transmit and receive related transaction.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
class SMPPSessionBoundTRX extends SMPPSessionBoundTX implements SMPPSessionState {
    
    @Override
    public SessionState getSessionState() {
        return SessionState.BOUND_TRX;
    }
    
    @Override
    public void processDeliverSm(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        SMPPSessionBoundRX.processDeliverSm0(pduHeader, pdu, responseHandler);
    }
}
