package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.BindType;
import org.jsmpp.bean.Bind;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.util.MessageId;

/**
 * @author uudashr
 *
 */
public interface ServerResponseHandler extends BaseResponseHandler {
    void sendBindResp(String systemId, BindType bindType, int sequenceNumber) throws IOException;
    void sendSubmitSmResponse(MessageId messageId, int sequenceNumber) throws IOException;
    
    void processBind(Bind bind);
    
    MessageId processSubmitSm(SubmitSm submitSm) throws ProcessRequestException;
    
    QuerySmResult processQuerySm(QuerySm querySm) throws ProcessRequestException;
}
