package org.jsmpp.session.state.client;

import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.PDU;
import org.jsmpp.session.ClientSession;
import org.jsmpp.session.state.Mode;

/**
 * This class is unbound state implementation of {@link Mode}. This class give
 * specific response to a transmit related transaction, otherwise it always give
 * negative response.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public class TX extends TRX {

    public TX(ClientSession session) {
        super(session);
    }

    @Override
    public Mode getMode() {
        return Mode.BOUND_TX;
    }

    @Override
    public void processDeliverSm(PDU pdu) {
        Command pduHeader = pdu.getCommand();
        sendNegativeResponse(pduHeader.getCommandId(), SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader.getSequenceNumber());
    }
}
