package org.jsmpp.session;

import org.jsmpp.bean.Bind;
import org.jsmpp.bean.DeliverSmResp;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.util.MessageId;

/**
 * @author uudashr
 * 
 */
public interface ServerResponseHandler extends ResponseHandler {
    
    void processBind(Bind bind);

    MessageId processSubmitSm(SubmitSm submitSm) throws ProcessRequestException;

    QuerySmResult processQuerySm(QuerySm querySm) throws ProcessRequestException;

    void processDeliverSmResp(DeliverSmResp resp);
}
