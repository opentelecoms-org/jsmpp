package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.BindType;
import org.jsmpp.bean.Bind;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.util.MessageId;

/**
 * @author uudashr
 *
 */
class DummyResponseHandler implements ServerResponseHandler {
    private boolean connectionClosed;
    
    public void notifyUnbonded() {
        // TODO Auto-generated method stub

    }

    public PendingResponse<Command> removeSentItem(int sequenceNumber) {
        // TODO Auto-generated method stub
        return null;
    }

    public void sendEnquireLinkResp(int sequenceNumber) throws IOException {
        // TODO Auto-generated method stub

    }

    public void sendGenerickNack(int commandStatus, int sequenceNumber)
            throws IOException {
        // TODO Auto-generated method stub

    }

    public void sendNegativeResponse(int originalCommandId, int commandStatus,
            int sequenceNumber) throws IOException {
        if (connectionClosed) {
            throw new IOException("Connection closed");
        }
    }

    public void sendUnbindResp(int sequenceNumber) throws IOException {
        // TODO Auto-generated method stub

    }
    
    public void processBind(Bind bind) {
        // TODO Auto-generated method stub
        
    }

    public QuerySmResult processQuerySm(QuerySm querySm)
            throws ProcessRequestException {
        // TODO Auto-generated method stub
        return null;
    }

    public MessageId processSubmitSm(SubmitSm submitSm)
            throws ProcessRequestException {
        // TODO Auto-generated method stub
        return null;
    }

    public void sendBindResp(String systemId, BindType bindType, int sequenceNumber)
            throws IOException {
        if (connectionClosed) {
            throw new IOException("Connection closed");
        }
    }

    public void sendSubmitSmResponse(MessageId messageId, int sequenceNumber)
            throws IOException {
        // TODO Auto-generated method stub

    }
    
    public void closeConnection() {
        connectionClosed = true;
    }
}