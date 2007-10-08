package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.extra.ProcessMessageException;
import org.jsmpp.session.SMPPSessionHandler;
import org.jsmpp.util.Decomposer;
import org.jsmpp.util.DefaultDecomposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is bound_tx state implementation of {@link SMPPSessionState}.
 * Response to receiver related transaction.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
class SMPPSessionBoundRX extends SMPPSessionBound {
    private static final Logger logger = LoggerFactory.getLogger(SMPPSessionBoundRX.class);
    private static final Decomposer pduDecomposer = new DefaultDecomposer();

    public void processDeliverSm(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        processDeliverSm0(pduHeader, pdu, smppClientProxy);
    }

    public void processSubmitSmResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        smppClientProxy.sendNegativeResponse(pduHeader.getCommandId(),
                SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                        .getSequenceNumber());
    }

    public void processQuerySmResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        smppClientProxy.sendNegativeResponse(pduHeader.getCommandId(),
                SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                        .getSequenceNumber());
    }

    static void processDeliverSm0(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        try {
            DeliverSm deliverSm = pduDecomposer.deliverSm(pdu);
            smppClientProxy.processDeliverSm(deliverSm);
            smppClientProxy.sendDeliverSmResp(pduHeader.getSequenceNumber());
        } catch (PDUStringException e) {
            logger.error("Failed decomposing deliver_sm", e);
            smppClientProxy.sendNegativeResponse(pduHeader.getCommandId(), e
                    .getErrorCode(), pduHeader.getSequenceNumber());
        } catch (ProcessMessageException e) {
            logger.error("Failed processing deliver_sm", e);
            smppClientProxy.sendNegativeResponse(pduHeader.getCommandId(), e
                    .getErrorCode(), pduHeader.getSequenceNumber());
        }
    }
}
