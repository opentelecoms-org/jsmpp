package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.bean.Command;
import org.jsmpp.session.SMPPSessionHandler;

/**
 * This class is bound_trx state implementation of {@link SMPPSessionState}.
 * Response both to transmit and receive related transaction.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
class SMPPSessionBoundTRX extends SMPPSessionBoundTX {

    public void processDeliverSm(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        SMPPSessionBoundRX.processDeliverSm0(pduHeader, pdu, smppClientProxy);
    }
}
