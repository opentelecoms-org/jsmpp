package org.jsmpp.session;

import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.util.MessageId;

/**
 * @author uudashr
 *
 */
public interface ServerMessageReceiverListener {
    MessageId onAcceptSubmitSm(SubmitSm submitSm) throws ProcessRequestException;

    QuerySmResult onAcceptQuerySm(QuerySm querySm) throws ProcessRequestException;
}
