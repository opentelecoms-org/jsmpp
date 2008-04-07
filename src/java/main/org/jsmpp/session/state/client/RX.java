package org.jsmpp.session.state.client;

import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.PDU;
import org.jsmpp.session.ClientSession;
import org.jsmpp.session.state.State;

/**
 * This class is bound_tx state implementation of {@link State}. Response to
 * receiver related transaction.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public class RX extends TRX {

    public RX(ClientSession session) {
        super(session);
    }

    @Override
    public State getState() {
        return State.BOUND_RX;
    }

    @Override
    public void processSubmitSmResp(PDU pdu) {
        sendNegativeResponse(pdu.getCommandId(), SMPPConstant.STAT_ESME_RINVBNDSTS, pdu.getCommand().getSequenceNumber());
    }

    @Override
    public void processQuerySmResp(PDU pdu) {
        sendNegativeResponse(pdu.getCommandId(), SMPPConstant.STAT_ESME_RINVBNDSTS, pdu.getCommand().getSequenceNumber());
    }
}
