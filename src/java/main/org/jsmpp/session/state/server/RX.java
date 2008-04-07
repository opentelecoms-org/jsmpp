package org.jsmpp.session.state.server;

import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.PDU;
import org.jsmpp.session.ServerSession;
import org.jsmpp.session.state.State;

/**
 * @author uudashr
 * 
 */
public class RX extends TRX {

    public RX(ServerSession serverSession) {
        super(serverSession);
    }

    @Override
    public State getState() {
        return State.BOUND_RX;
    }

    @Override
    public void processQuerySm(PDU pdu) {
        sendNegativeResponse(pdu.getCommandId(), SMPPConstant.STAT_ESME_RINVBNDSTS, pdu.getCommand().getSequenceNumber());
    }

    @Override
    public void processSubmitSm(PDU pdu) {
        sendNegativeResponse(pdu.getCommandId(), SMPPConstant.STAT_ESME_RINVBNDSTS, pdu.getCommand().getSequenceNumber());
    }
}
