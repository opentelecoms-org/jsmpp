package org.jsmpp.session;

import org.jsmpp.bean.DeliverSm;

public class SMPPSessionResponseHandler implements ClientResponseHandler {

    public void processDeliverSm(ClientSession session, DeliverSm deliverSm) {
        session.getMessageReceiverListener().onAcceptDeliverSm(deliverSm);
        session.getPDUSender().sendDeliverSmResp(deliverSm);
    }
}