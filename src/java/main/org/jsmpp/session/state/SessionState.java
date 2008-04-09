package org.jsmpp.session.state;

import java.io.IOException;

import org.jsmpp.BindType;
import org.jsmpp.bean.PDU;

/**
 * @author uudashr
 * 
 */
public interface SessionState {
    public State getState();

    public void process(PDU pdu) throws IOException;

    public void sendNegativeResponse(int originalCommandId, int commandStatus, int sequenceNumber);

    public void sendBindResp(String systemId, BindType bindType, int sequenceNumber);

}
