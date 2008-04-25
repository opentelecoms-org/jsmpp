package org.jsmpp.session;

import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.util.MessageId;

/**
 * @author uudashr
 *
 */
public interface ServerMessageReceiverListener extends GenericMessageReceiverListener {
    MessageId onAcceptSubmitSm(SubmitSm submitSm, SMPPServerSession source) throws ProcessRequestException;
    
    QuerySmResult onAcceptQuerySm(QuerySm querySm, SMPPServerSession source) throws ProcessRequestException;
}
