package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.bean.PDU;
import org.jsmpp.extra.SessionState;

/**
 * @author uudashr
 * 
 */
public interface SMPPSessionState {

    SessionState getSessionState();

    public void process(PDU pdu) throws IOException;
}
