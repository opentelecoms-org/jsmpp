package org.jsmpp.session;

import org.jsmpp.bean.CancelSm;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.ReplaceSm;
import org.jsmpp.bean.SubmitMulti;
import org.jsmpp.bean.SubmitMultiResult;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.util.MessageId;

/**
 * @author uudashr
 *
 */
public interface ServerMessageReceiverListener extends GenericMessageReceiverListener {
    MessageId onAcceptSubmitSm(SubmitSm submitSm, SMPPServerSession source)
            throws ProcessRequestException;
    
    SubmitMultiResult onAcceptSubmitMulti(SubmitMulti submitMulti,
            SMPPServerSession source) throws ProcessRequestException;
    
    QuerySmResult onAcceptQuerySm(QuerySm querySm, SMPPServerSession source)
            throws ProcessRequestException;
    
    void onAcceptReplaceSm(ReplaceSm replaceSm, SMPPServerSession source)
            throws ProcessRequestException;
    
    void onAcceptCancelSm(CancelSm cancelSm, SMPPServerSession source)
            throws ProcessRequestException; 
}
