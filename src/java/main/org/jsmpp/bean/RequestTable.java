package org.jsmpp.bean;

import java.util.Hashtable;

/**
 * @author uudashr
 * 
 */
public class RequestTable {
    private Hashtable<Integer, Command> _requestTable;

    public RequestTable() {
        _requestTable = new Hashtable<Integer, Command>();
    }

    public void putRequest(Command pdu) {
        _requestTable.put(pdu.getSequenceNumber(), pdu);
    }

    public Command pull(Integer sequenceNumber) {
        return _requestTable.remove(sequenceNumber);
    }
}
