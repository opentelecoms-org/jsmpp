package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.session.ServerResponseHandler;

/**
 * @author uudashr
 * 
 */
abstract class SMPPServerSessionBound extends
        AbstractGenericSMPPSessionBound implements SMPPServerSessionState {
    
    public void processBind(Command pduHeader, byte[] pdu,
            ServerResponseHandler responseHandler) throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
                SMPPConstant.STAT_ESME_RALYBND, pduHeader.getSequenceNumber());
    }
}
