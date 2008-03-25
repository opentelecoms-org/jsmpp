package org.jsmpp.session.state.client;

import java.io.IOException;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.BindResp;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.PDU;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.ClientResponseHandler;
import org.jsmpp.session.state.SMPPSessionState;
import org.jsmpp.util.IntUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is open state implementation of {@link SMPPSessionState}. When
 * the session state is open, we only give positive response to bind related
 * intention.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public class SMPPSessionOpen extends ClientSessionState {
    public SMPPSessionOpen(ClientResponseHandler responseHandler) {
        super(responseHandler);
    }

    private static final Logger logger = LoggerFactory.getLogger(SMPPSessionOpen.class);

    public SessionState getSessionState() {
        return SessionState.OPEN;
    }

    @Override
    public void processBindResp(PDU pdu) throws IOException {
        Command pduHeader = pdu.getCommand();
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(pduHeader.getSequenceNumber());
        if (pendingResp != null) {
            try {
                logger.debug("Bind Response header (" + pduHeader.getCommandLength() + ", " + pduHeader.getCommandIdAsHex() + ", " + IntUtil.toHexString(pduHeader.getCommandStatus()) + ", " + pduHeader.getSequenceNumber() + ")");
                BindResp resp = pduDecomposer.bindResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                String message = "Failed decomposing submit_sm_resp";
                logger.error(message, e);
                responseHandler.sendGenerickNack(e.getErrorCode(), pduHeader.getSequenceNumber());
                pendingResp.doneWithInvalidResponse(new InvalidResponseException(message, e));
            }
        } else {
            logger.error("No request with sequence number " + pduHeader.getSequenceNumber() + " found");
            responseHandler.sendGenerickNack(SMPPConstant.STAT_ESME_RINVDFTMSGID, pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processDeliverSm(PDU pdu) {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException("Receive unexpected deliver_sm"));
        }
    }

    @Override
    public void processEnquireLink(PDU pdu) {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException("Receive unexpected enquire_link"));
        }
    }

    @Override
    public void processEnquireLinkResp(PDU pdu) {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException("Receive unexpected enquire_link_resp"));
        }
    }

    @Override
    public void processGenericNack(PDU pdu) {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException("Receive unexpected generic_nack"));
        }
    }

    @Override
    public void processSubmitSmResp(PDU pdu) {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException("Receive unexpected submit_sm_resp"));
        }
    }

    @Override
    public void processUnbind(PDU pdu) {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException("Receive unexpected unbind"));
        }
    }

    @Override
    public void processUnbindResp(PDU pdu) {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException("Receive unexpected unbind_resp"));
        }
    }

    @Override
    public void processUnknownCid(PDU pdu) {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(1);
        // FIXME uud: pending response might be null
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException("Receive unknown command_id"));
        }
    }

    @Override
    public void processQuerySmResp(PDU pdu) {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(1);
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException("Receive unexpected query_sm"));
        }
    }
}
