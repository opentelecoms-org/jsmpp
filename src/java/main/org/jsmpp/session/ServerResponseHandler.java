package org.jsmpp.session;

import org.jsmpp.bean.Bind;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.util.MessageId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerResponseHandler {
    private static final Logger logger = LoggerFactory.getLogger(ServerResponseHandler.class);

    public MessageId processSubmitSm(ServerSession session, SubmitSm submitSm) throws ProcessRequestException {
        ServerMessageReceiverListener messageReceiverListener = session.getMessageReceiverListener();
        if (messageReceiverListener != null) {
            MessageId messageId = messageReceiverListener.onAcceptSubmitSm(submitSm);
            session.getPDUSender().sendSubmitSmResp(submitSm, messageId);
            return messageId;
        }
        logger.debug("ServerResponseHandler received submit_sm, but ServerMessageReceiverListener is not set");
        return null;
    }

    public QuerySmResult processQuerySm(ServerSession session, QuerySm querySm) throws ProcessRequestException {
        ServerMessageReceiverListener messageReceiverListener = session.getMessageReceiverListener();
        if (messageReceiverListener != null) {
            QuerySmResult res = messageReceiverListener.onAcceptQuerySm(querySm);
            session.getPDUSender().sendQuerySmResp(querySm, res);
            return res;
        }
        logger.debug("ServerResponseHandler received query_sm, but ServerMessageReceiverListener is not set");
        return null;
    }

    public void processBind(ServerSession session, Bind bind) {
        session.getBindRequestReceiver().notifyAcceptBind(session.getSessionState(), bind);
    }
}