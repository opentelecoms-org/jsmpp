package org.jsmpp.session.state;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.EnquireLinkResp;
import org.jsmpp.bean.UnbindResp;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.session.SMPPSessionHandler;
import org.jsmpp.util.Decomposer;
import org.jsmpp.util.DefaultDecomposer;
import org.jsmpp.util.IntUtil;

/**
 * This class is general bound state implementation of {@link SMPPSessionState}.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
abstract class SMPPSessionBound implements SMPPSessionState {
    private static final Logger logger = Logger
            .getLogger(SMPPSessionBound.class);
    protected static final Decomposer pduDecomposer = new DefaultDecomposer();

    public void processEnquireLink(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        smppClientProxy.sendEnquireLinkResp(pduHeader.getSequenceNumber());
    }

    public void processEnquireLinkResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        PendingResponse<Command> pendingResp = smppClientProxy
                .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            EnquireLinkResp resp = pduDecomposer.enquireLinkResp(pdu);
            pendingResp.done(resp);
        } else {
            logger.error("No request find for sequence number "
                    + pduHeader.getSequenceNumber());
            smppClientProxy.sendGenerickNack(
                    SMPPConstant.STAT_ESME_RINVDFTMSGID, pduHeader
                            .getSequenceNumber());
        }
    }

    public void processUnbind(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        logger.info("Receving unbind request");
        smppClientProxy.sendUnbindResp(pduHeader.getSequenceNumber());
    }

    public void processUnbindResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        PendingResponse<Command> pendingResp = smppClientProxy
                .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            UnbindResp resp = pduDecomposer.unbindResp(pdu);
            pendingResp.done(resp);
        } else {
            logger.error("No request find for sequence number "
                    + pduHeader.getSequenceNumber());
            smppClientProxy.sendGenerickNack(
                    SMPPConstant.STAT_ESME_RINVDFTMSGID, pduHeader
                            .getSequenceNumber());
        }
    }

    public void processUnknownCid(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        smppClientProxy.sendGenerickNack(SMPPConstant.STAT_ESME_RINVCMDID,
                pduHeader.getSequenceNumber());
    }

    public void processBindResp(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        smppClientProxy.sendNegativeResponse(pduHeader.getCommandId(),
                SMPPConstant.STAT_ESME_RALYBND, pduHeader.getSequenceNumber());
    }

    public void processGenericNack(Command pduHeader, byte[] pdu,
            SMPPSessionHandler smppClientProxy) throws IOException {
        PendingResponse<Command> pendingResp = smppClientProxy
                .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive generic_nack with command_status "
                            + pduHeader.getCommandStatusAsHex()));
        }
        logger.error("Receive generick_nack for ("
                + pendingResp.getResponse().getCommandIdAsHex() + "). "
                + "command_status=" + pduHeader.getCommandStatusAsHex()
                + ", sequence_number="
                + IntUtil.toHexString(pduHeader.getSequenceNumber()));
    }

}
