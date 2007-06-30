package org.jsmpp.session.state;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.BindResp;
import org.jsmpp.bean.Command;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.session.SMPPSessionHandler;
import org.jsmpp.util.Decomposer;
import org.jsmpp.util.DefaultDecomposer;
import org.jsmpp.util.IntUtil;

/**
 * This class is open state implementation of {@link SMPPSessionState}. When
 * the session state is open, we only give positive response to bind related intention.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
class SMPPSessionOpen implements SMPPSessionState {
    private static final Logger logger = Logger
            .getLogger(SMPPSessionOpen.class);
    private static final Decomposer pduDecomposer = new DefaultDecomposer();

    public void processBindResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        PendingResponse<Command> pendingResp = smppClientProxy
                .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            try {
                logger.debug("Bind Response header ("
                        + pduHeader.getCommandLength() + ", "
                        + pduHeader.getCommandIdAsHex() + ", "
                        + IntUtil.toHexString(pduHeader.getCommandStatus())
                        + ", " + pduHeader.getSequenceNumber() + ")");
                BindResp resp = pduDecomposer.bindResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                String message = "Failed decomposing submit_sm_resp";
                logger.error(message, e);
                smppClientProxy.sendGenerickNack(e.getErrorCode(), pduHeader
                        .getSequenceNumber());
                pendingResp
                        .doneWithInvalidResponse(new InvalidResponseException(
                                message, e));
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
        PendingResponse<Command> pendingResp = smppClientProxy
                .removeSentItem(1);
        pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                "Receive unexpected deliver_sm"));
    }

    public void processEnquireLink(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        PendingResponse<Command> pendingResp = smppClientProxy
                .removeSentItem(1);
        pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                "Receive unexpected enquire_link"));
    }

    public void processEnquireLinkResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        PendingResponse<Command> pendingResp = smppClientProxy
                .removeSentItem(1);
        pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                "Receive unexpected enquire_link_resp"));
    }

    public void processGenericNack(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) {
        PendingResponse<Command> pendingResp = smppClientProxy
                .removeSentItem(1);
        pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                "Receive unexpected generic_nack"));
    }

    public void processSubmitSmResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        PendingResponse<Command> pendingResp = smppClientProxy
                .removeSentItem(1);
        pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                "Receive unexpected submit_sm_resp"));
    }

    public void processUnbind(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        PendingResponse<Command> pendingResp = smppClientProxy
                .removeSentItem(1);
        pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                "Receive unexpected unbind"));
    }

    public void processUnbindResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        PendingResponse<Command> pendingResp = smppClientProxy
                .removeSentItem(1);
        pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                "Receive unexpected unbind_resp"));

    }

    public void processUnknownCid(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        PendingResponse<Command> pendingResp = smppClientProxy
                .removeSentItem(1);
        pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                "Receive unknown cid " + pduHeader.getCommandIdAsHex()));
    }

    public void processQuerySmResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        PendingResponse<Command> pendingResp = smppClientProxy
                .removeSentItem(1);
        pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                "Receive unexpected generic_nack"));
    }
}
