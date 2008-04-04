package org.jsmpp.session.state.client;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.BindResp;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.PDU;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.session.ClientSession;
import org.jsmpp.session.state.Mode;
import org.jsmpp.util.IntUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class is open state implementation of {@link Mode}. When the session
 * state is open, we only give positive response to bind related intention.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public class Open extends ClientSessionState {
    private static final Logger logger = LoggerFactory.getLogger(Open.class);

    public Open(ClientSession session) {
        super(session);
    }

    public Mode getMode() {
        return Mode.OPEN;
    }

    @Override
    public void processBindResp(PDU pdu) {
        Command pduHeader = pdu.getCommand();
        PendingResponse<Command> pendingResp = pendingResponses().remove(pduHeader);
        if (pendingResp != null) {
            try {
                logger.debug("Bind Response header (" + pduHeader.getCommandLength() + ", " + pduHeader.getCommandIdAsHex() + ", " + IntUtil.toHexString(pduHeader.getCommandStatus()) + ", " + pduHeader.getSequenceNumber() + ")");
                BindResp resp = pduDecomposer.bindResp(pdu);
                pendingResp.done(resp);
            } catch (PDUStringException e) {
                String message = "Failed decomposing submit_sm_resp";
                logger.error(message, e);
                sendGenerickNack(e.getErrorCode(), pduHeader.getSequenceNumber());
                pendingResp.doneWithInvalidResponse(new InvalidResponseException(message, e));
            }
        } else {
            logger.error("No request with sequence number " + pduHeader.getSequenceNumber() + " found");
            sendGenerickNack(SMPPConstant.STAT_ESME_RINVDFTMSGID, pduHeader.getSequenceNumber());
        }
    }
}
