package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.EnquireLinkResp;
import org.jsmpp.bean.UnbindResp;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.session.BaseResponseHandler;
import org.jsmpp.util.DefaultDecomposer;
import org.jsmpp.util.IntUtil;
import org.jsmpp.util.PDUDecomposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 *
 */
abstract class AbstractGenericSMPPSessionBound implements GenericSMPPSessionState {
    protected static final PDUDecomposer pduDecomposer = new DefaultDecomposer();
    private static final Logger logger = LoggerFactory.getLogger(AbstractGenericSMPPSessionBound.class);
    
    public void processEnquireLink(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        responseHandler.sendEnquireLinkResp(pduHeader.getSequenceNumber());
    }

    public void processEnquireLinkResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            EnquireLinkResp resp = pduDecomposer.enquireLinkResp(pdu);
            pendingResp.done(resp);
        } else {
            logger.error("No request found for " + pduHeader);
        }
    }

    public void processUnbind(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        logger.info("Receving unbind request");
        try {
            responseHandler.sendUnbindResp(pduHeader.getSequenceNumber());
        } finally {
            responseHandler.notifyUnbonded();
        }
    }

    public void processUnbindResp(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            UnbindResp resp = pduDecomposer.unbindResp(pdu);
            pendingResp.done(resp);
        } else {
            logger.error("No request found for " + pduHeader);
        }
    }

    public void processUnknownCid(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        responseHandler.sendGenerickNack(SMPPConstant.STAT_ESME_RINVCMDID,
                pduHeader.getSequenceNumber());
    }
    
    public void processGenericNack(Command pduHeader, byte[] pdu,
            BaseResponseHandler responseHandler) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler
                .removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException(
                    "Receive generic_nack with command_status "
                            + pduHeader.getCommandStatusAsHex()));
            logger.error("Receive generick_nack for ("
                    + pendingResp.getResponse().getCommandIdAsHex() + "). "
                    + "command_status=" + pduHeader.getCommandStatusAsHex()
                    + ", sequence_number="
                    + IntUtil.toHexString(pduHeader.getSequenceNumber()));
        }
    }
}
