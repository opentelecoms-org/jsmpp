package org.jsmpp.session.state.server;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Bind;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.PDU;
import org.jsmpp.session.ServerSession;
import org.jsmpp.session.state.State;

/**
 * @author uudashr
 * 
 */
public class Open extends ServerSessionState {

    public Open(ServerSession serverSession) {
        super(serverSession);
    }

    public State getState() {
        return State.OPEN;
    }

    @Override
    public void processBind(PDU pdu) {
        Command pduHeader = pdu.getCommand();
        try {
            Bind bind = pduDecomposer.bind(pdu);
            getSession().getResponseHandler().processBind(getSession(), bind);
        } catch (PDUStringException e) {
            sendNegativeResponse(pduHeader.getCommandId(), e.getErrorCode(), pduHeader.getSequenceNumber());
        } catch (IllegalArgumentException e) {
            // FIXME uud: might not need anymore
            sendNegativeResponse(pduHeader.getCommandId(), SMPPConstant.STAT_ESME_RINVCMDID, pduHeader.getSequenceNumber());
        }
    }
}
