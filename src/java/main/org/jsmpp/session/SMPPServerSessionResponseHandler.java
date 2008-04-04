package org.jsmpp.session;

import org.jsmpp.bean.Bind;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.util.MessageId;

public class SMPPServerSessionResponseHandler implements ServerResponseHandler {

    public MessageId processSubmitSm(ServerSession session, SubmitSm submitSm) throws ProcessRequestException {
        MessageId messageId = session.getMessageReceiverListener().onAcceptSubmitSm(submitSm);
        session.getPDUSender().sendSubmitSmResp(submitSm, messageId);
        return messageId;
    }

    public QuerySmResult processQuerySm(ServerSession session, QuerySm querySm) throws ProcessRequestException {
        QuerySmResult res = messageReceiverListener.onAcceptQuerySm(querySm);
        session.getPDUSender().sendQuerySmResp(querySm, res);
        return res;
    }

    public void processBind(ServerSession session, Bind bind) {
        session.getBindRequestReceiver().notifyAcceptBind(session.getSessionState(), bind);
    }

}