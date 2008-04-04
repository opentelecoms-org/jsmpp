package org.jsmpp.session;

import org.jsmpp.bean.Bind;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.util.MessageId;

/**
 * @author uudashr
 * 
 */
public interface ServerResponseHandler {

    MessageId processSubmitSm(ServerSession session, SubmitSm submitSm);

    QuerySmResult processQuerySm(ServerSession session, QuerySm querySm);

    void processBind(ServerSession session, Bind bind);
}
