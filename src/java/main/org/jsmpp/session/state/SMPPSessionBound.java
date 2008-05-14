package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.session.ResponseHandler;

/**
 * This class is general bound state implementation of {@link SMPPSessionState}.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
abstract class SMPPSessionBound extends AbstractGenericSMPPSessionBound  implements SMPPSessionState {
    
    public void processBindResp(Command pduHeader, byte[] pdu,
            ResponseHandler responseHandler) throws IOException {
        responseHandler.sendNegativeResponse(pduHeader.getCommandId(),
                SMPPConstant.STAT_ESME_RALYBND, pduHeader.getSequenceNumber());
    }
}
