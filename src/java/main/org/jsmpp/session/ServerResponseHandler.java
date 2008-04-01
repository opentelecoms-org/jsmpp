package org.jsmpp.session;

import java.util.concurrent.TimeoutException;

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

    MessageId processSubmitSm(SubmitSm submitSm) throws ProcessRequestException;

    QuerySmResult processQuerySm(QuerySm querySm) throws ProcessRequestException;

    void processDeliverSmResp(DeliverSmResp resp);

    void processBind(Bind bind);

    BindRequest waitForRequest(long timeout) throws TimeoutException;
}
