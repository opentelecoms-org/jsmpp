package org.jsmpp.session;

import org.jsmpp.bean.DeliverSm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ClientResponseHandler.class);

    public void processDeliverSm(ClientSession session, DeliverSm deliverSm) {
        if (session.getMessageReceiverListener() != null) {
            session.getMessageReceiverListener().onAcceptDeliverSm(deliverSm);
            session.getPDUSender().sendDeliverSmResp(deliverSm);
        } else {
            logger.info("Message received to ClientResponseHandler, but MessageReceiverListener is not set");
        }
    }
}