package org.jsmpp.session.state.client;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.EnquireLinkResp;
import org.jsmpp.bean.PDU;
import org.jsmpp.bean.UnbindResp;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.session.ClientSession;
import org.jsmpp.session.state.BaseSessionState;
import org.jsmpp.util.IntUtil;
import org.jsmpp.util.PDUDecomposer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ClientSessionState extends BaseSessionState<ClientSession> {

    private static final Logger logger = LoggerFactory.getLogger(Closed.class);

    protected PDUDecomposer pduDecomposer = PDUDecomposer.getInstance();

    public ClientSessionState(ClientSession session) {
        super(session);
    }

    public void process(PDU pdu) {
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

    public void processEnquireLink(PDU pdu) {
        sendEnquireLinkResp(pdu.getCommand().getSequenceNumber());
    }

    public void processEnquireLinkResp(PDU pdu) {
        PendingResponse<Command> pendingResp = pendingResponses().remove(pdu.getCommand());
        if (pendingResp != null) {
            EnquireLinkResp resp = pduDecomposer.enquireLinkResp(pdu);
            pendingResp.done(resp);
        } else {
            logger.error("No request found for " + pdu.getCommand());
        }
    }

    public void processUnbind(PDU pdu) {
        Command pduHeader = pdu.getCommand();
        logger.info("Receving unbind request");
        try {
            sendUnbindResp(pduHeader.getSequenceNumber());
        } finally {
            notifyUnbonded();
        }
    }

    public void processUnbindResp(PDU pdu) {
        PendingResponse<Command> pendingResp = pendingResponses().remove(pdu.getCommand());
        if (pendingResp != null) {
            UnbindResp resp = pduDecomposer.unbindResp(pdu);
            pendingResp.done(resp);
        } else {
            logger.error("No request found for " + pdu);
        }
    }

    public void processUnknownCid(PDU pdu) {
        logger.warn("Received unknown command " + pdu.getCommand());
        sendGenerickNack(SMPPConstant.STAT_ESME_RINVCMDID, pdu.getCommand().getSequenceNumber());
    }

    public void processGenericNack(PDU pdu) {
        PendingResponse<Command> pendingResp = pendingResponses().remove(pdu.getCommand());
        if (pendingResp != null) {
            pendingResp.doneWithInvalidResponse(new InvalidResponseException("Receive generic_nack with command_status " + pdu.getCommand().getCommandStatusAsHex()));
            logger.error("Receive generick_nack for (" + pendingResp.getResponse().getCommandIdAsHex() + "). " + "command_status=" + pdu.getCommand().getCommandStatusAsHex() + ", sequence_number=" + IntUtil.toHexString(pdu.getCommand().getSequenceNumber()));
        }
    }

    public void processBindResp(PDU pdu) {
        throw new ProcessRequestException("Invalid operation for " + getState() + " session state");
    }

    public void processDeliverSm(PDU pdu) {
        throw new ProcessRequestException("Invalid operation for " + getState() + " session state");
    }

    public void processQuerySmResp(PDU pdu) {
        throw new ProcessRequestException("Invalid operation for " + getState() + " session state");
    }

    public void processSubmitSmResp(PDU pdu) {
        throw new ProcessRequestException("Invalid operation for " + getState() + " session state");
    }
}
