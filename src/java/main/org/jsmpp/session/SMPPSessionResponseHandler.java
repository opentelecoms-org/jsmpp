package org.jsmpp.session;

import org.jsmpp.bean.DeliverSm;
import org.jsmpp.extra.ProcessRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SMPPSessionResponseHandler extends BaseResponseHandler implements ClientResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(SMPPSessionResponseHandler.class);

    private final SMPPSession session;

    public SMPPSessionResponseHandler(SMPPSession session) {
        super(session);
        this.session = session;
    }

    public void processDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {
        fireAcceptDeliverSm(deliverSm);
    }

    void fireAcceptDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {
        if (session.messageReceiverListener != null) {
            session.messageReceiverListener.onAcceptDeliverSm(deliverSm);
        } else {
            logger.warn("Receive deliver_sm but MessageReceiverListener is null. Short message = " + new String(deliverSm.getShortMessage()));
        }
    }

}