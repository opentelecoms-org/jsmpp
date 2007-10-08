package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.QuerySmResp;
import org.jsmpp.bean.SubmitSmResp;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.session.SMPPSessionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is unbound state implementation of {@link SMPPSessionState}. This
 * class give spesific response to a transmit related transaction, otherwise
 * it always give negative response.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
class SMPPSessionBoundTX extends SMPPSessionBound {
    private static final Logger logger = LoggerFactory.getLogger(SMPPSessionBoundTX.class);

    public void processSubmitSmResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {

        PendingResponse<Command> pendingResp = smppClientProxy
                .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            try {
                SubmitSmResp resp = pduDecomposer.submitSmResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                logger.error("Failed decomposing submit_sm_resp", e);
                smppClientProxy.sendGenerickNack(e.getErrorCode(), pduHeader
                        .getSequenceNumber());
            }
        } else {
            logger.error("No request find for sequence number "
                    + pduHeader.getSequenceNumber());
            smppClientProxy.sendGenerickNack(
                    SMPPConstant.STAT_ESME_RINVDFTMSGID, pduHeader
                            .getSequenceNumber());
        }
    }

    public void processQuerySmResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {

        PendingResponse<Command> pendingResp = smppClientProxy
                .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            try {
                QuerySmResp resp = pduDecomposer.querySmResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                logger.error("Failed decomposing submit_sm_resp", e);
                smppClientProxy.sendGenerickNack(e.getErrorCode(), pduHeader
                        .getSequenceNumber());
            }
        } else {
            logger.error("No request find for sequence number "
                    + pduHeader.getSequenceNumber());
            smppClientProxy.sendGenerickNack(
                    SMPPConstant.STAT_ESME_RINVDFTMSGID, pduHeader
                            .getSequenceNumber());
        }
    }

    public void processDeliverSm(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        smppClientProxy.sendNegativeResponse(pduHeader.getCommandId(),
                SMPPConstant.STAT_ESME_RINVBNDSTS, pduHeader
                        .getSequenceNumber());
    }
}
