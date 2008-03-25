package org.jsmpp.session.state.client;

import java.io.IOException;

import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.PDU;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.ClientResponseHandler;
import org.jsmpp.session.state.SMPPSessionState;

/**
 * This class is unbound state implementation of {@link SMPPSessionState}. This
 * class give specific response to a transmit related transaction, otherwise it
 * always give negative response.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public class SMPPSessionBoundTX extends SMPPSessionBoundTRX {
    public SMPPSessionBoundTX(ClientResponseHandler responseHandler) {
        super(responseHandler);
    }

    @Override
    public SessionState getSessionState() {
        return SessionState.BOUND_TX;
    }

    @Override
    public void processDeliverSm(PDU pdu) throws IOException {
        Command pduHeader = pdu.getCommand();
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(), SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader.getSequenceNumber());
    }
}
