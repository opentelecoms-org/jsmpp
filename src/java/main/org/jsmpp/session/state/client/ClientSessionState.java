package org.jsmpp.session.state.client;

import java.io.IOException;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.EnquireLinkResp;
import org.jsmpp.bean.PDU;
import org.jsmpp.bean.UnbindResp;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.session.ClientResponseHandler;
import org.jsmpp.session.state.SMPPSessionState;
import org.jsmpp.util.DefaultDecomposer;
import org.jsmpp.util.IntUtil;
import org.jsmpp.util.PDUDecomposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ClientSessionState implements SMPPSessionState {
    private static final Logger logger = LoggerFactory.getLogger(ClientSessionState.class);
    protected PDUDecomposer pduDecomposer = DefaultDecomposer.getInstance();
    final protected ClientResponseHandler responseHandler;

    public ClientSessionState(ClientResponseHandler responseHandler) {
        this.responseHandler = responseHandler;
    }

    public void process(PDU pdu) throws IOException {
        switch (pdu.getCommandId()) {
        case SMPPConstant.CID_BIND_RECEIVER_RESP:
        case SMPPConstant.CID_BIND_TRANSMITTER_RESP:
        case SMPPConstant.CID_BIND_TRANSCEIVER_RESP:
            processBindResp(pdu);
            break;
        case SMPPConstant.CID_GENERIC_NACK:
            processGenericNack(pdu);
            break;
        case SMPPConstant.CID_ENQUIRE_LINK:
            processEnquireLink(pdu);
            break;
        case SMPPConstant.CID_ENQUIRE_LINK_RESP:
            processEnquireLinkResp(pdu);
            break;
        case SMPPConstant.CID_SUBMIT_SM_RESP:
            processSubmitSmResp(pdu);
            break;
        case SMPPConstant.CID_QUERY_SM_RESP:
            processQuerySmResp(pdu);
            break;
        case SMPPConstant.CID_DELIVER_SM:
            processDeliverSm(pdu);
            break;
        case SMPPConstant.CID_UNBIND:
            processUnbind(pdu);
            break;
        case SMPPConstant.CID_UNBIND_RESP:
            processUnbindResp(pdu);
            break;
        default:
            processUnknownCid(pdu);
        }
    }

    public void processEnquireLink(PDU pdu) throws IOException {
        responseHandler.sendEnquireLinkResp(pdu.getCommand().getSequenceNumber());
    }

    public void processEnquireLinkResp(PDU pdu) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(pdu.getCommand().getSequenceNumber());
        if (pendingResp != null) {
            EnquireLinkResp resp = pduDecomposer.enquireLinkResp(pdu);
            pendingResp.done(resp);
        } else {
            logger.error("No request found for " + pdu.getCommand());
        }
    }

    public void processUnbind(PDU pdu) throws IOException {
        Command pduHeader = pdu.getCommand();
        logger.info("Receving unbind request");
        try {
            responseHandler.sendUnbindResp(pduHeader.getSequenceNumber());
        } finally {
            responseHandler.notifyUnbonded();
        }
    }

    public void processUnbindResp(PDU pdu) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(pdu.getCommand().getSequenceNumber());
        if (pendingResp != null) {
            UnbindResp resp = pduDecomposer.unbindResp(pdu);
            pendingResp.done(resp);
        } else {
            logger.error("No request found for " + pdu);
        }
    }

    public void processUnknownCid(PDU pdu) throws IOException {
        logger.warn("Received unknown command " + pdu.getCommand());
        responseHandler.sendGenerickNack(SMPPConstant.STAT_ESME_RINVCMDID, pdu.getCommand().getSequenceNumber());
    }

    public void processGenericNack(PDU pdu) throws IOException {
        PendingResponse<Command> pendingResp = responseHandler.removeSentItem(pdu.getCommand().getSequenceNumber());
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException("Receive generic_nack with command_status " + pdu.getCommand().getCommandStatusAsHex()));
            logger.error("Receive generick_nack for (" + pendingResp.getResponse().getCommandIdAsHex() + "). " + "command_status=" + pdu.getCommand().getCommandStatusAsHex() + ", sequence_number=" + IntUtil.toHexString(pdu.getCommand().getSequenceNumber()));
        }
    }

    public abstract void processDeliverSm(PDU pdu) throws IOException;

    public abstract void processQuerySmResp(PDU pdu) throws IOException;

    public abstract void processSubmitSmResp(PDU pdu) throws IOException;

    public abstract void processBindResp(PDU pdu) throws IOException;
}
