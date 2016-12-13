/*
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
package org.jsmpp.session;

import java.io.IOException;

import org.jsmpp.bean.Bind;
import org.jsmpp.bean.CancelSm;
import org.jsmpp.bean.MessageState;
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
public interface ServerResponseHandler extends GenericServerResponseHandler {

    void sendSubmitSmResponse(MessageId messageId, int sequenceNumber)
            throws IOException;

    void processBind(Bind bind);

    MessageId processSubmitSm(SubmitSm submitSm) throws ProcessRequestException;
    
    SubmitMultiResult processSubmitMulti(SubmitMulti submitMulti) throws ProcessRequestException;
    
    void sendSubmitMultiResponse(SubmitMultiResult submiitMultiResult,
            int sequenceNumber) throws IOException;
    
    QuerySmResult processQuerySm(QuerySm querySm)
            throws ProcessRequestException;

    void sendQuerySmResp(String messageId, String finalDate,
            MessageState messageState, byte errorCode, int sequenceNumber)
            throws IOException;

    void processCancelSm(CancelSm cancelSm) throws ProcessRequestException;
    
    void sendCancelSmResp(int sequenceNumber) throws IOException;
    
    void processReplaceSm(ReplaceSm replaceSm) throws ProcessRequestException;
    
    void sendReplaceSmResp(int sequenceNumber) throws IOException;
}
