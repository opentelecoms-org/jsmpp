package org.jsmpp.session.state.server;

import java.io.IOException;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Bind;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.PDU;
import org.jsmpp.extra.SessionState;
import org.jsmpp.session.ServerResponseHandler;

/**
 * @author uudashr
 * 
 */
public class SMPPServerSessionOpen extends ServerSessionState {

    public SMPPServerSessionOpen(ServerResponseHandler responseHandler) {
        super(responseHandler);
    }

    public SessionState getSessionState() {
        return SessionState.OPEN;
    }

    @Override
    public void processBind(PDU pdu) throws IOException {
        Command pduHeader = pdu.getCommand();
        try {
            Bind bind = pduDecomposer.bind(pdu);
            responseHandler.processBind(bind);
            // responseHandler.sendBindResp(BindType.valueOf(pduHeader.getCommandId()),
            // pduHeader.getSequenceNumber());
        } catch (PDUStringException e) {
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        } catch (IllegalArgumentException e) {
            // FIXME uud: might not need anymore
            responseHandler.sendNegativeResponse(pduHeader.getCommandId(), SMPPConstant.STAT_ESME_RINVCMDID, pduHeader.getSequenceNumber());
        }
    }

    @Override
    public void processDeliverSmResp(PDU pdu) throws IOException {
        throw new IOException("Invalid process for open session state");
    }

    @Override
    public void processQuerySm(PDU pdu) throws IOException {
        throw new IOException("Invalid process for open session state");
    }

    @Override
    public void processSubmitSm(PDU pdu) throws IOException {
        throw new IOException("Invalid process for open session state");
    }

    @Override
    public void processEnquireLink(PDU pdu) throws IOException {
        throw new IOException("Invalid process for open session state");
    }

    @Override
    public void processEnquireLinkResp(PDU pdu) throws IOException {
        throw new IOException("Invalid process for open session state");
    }

    @Override
    public void processGenericNack(PDU pdu) throws IOException {
        throw new IOException("Invalid process for open session state");
    }

    @Override
    public void processUnbind(PDU pdu) throws IOException {
        throw new IOException("Invalid process for open session state");
    }

    @Override
    public void processUnbindResp(PDU pdu) throws IOException {
        throw new IOException("Invalid process for open session state");
    }

    @Override
    public void processUnknownCid(PDU pdu) throws IOException {
        throw new IOException("Invalid process for open session state");
    }
}
