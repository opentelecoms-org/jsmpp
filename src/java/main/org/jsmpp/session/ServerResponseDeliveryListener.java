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

import org.jsmpp.bean.SubmitMultiResult;
import org.jsmpp.util.MessageId;

/**
 * This is listener will be used by {@link SMPPServerSession} to notify a user
 * when a response has been sent.
 * <p>
 * Some of user code might be implemented on {@link SMPPServerSession} is
 * <ul>
 *  <li>{@link ServerMessageReceiverListener#onAcceptSubmitSm(org.jsmpp.bean.SubmitSm, SMPPServerSession)}</li>
 *  <li>{@link ServerMessageReceiverListener#onAcceptSubmitMulti(org.jsmpp.bean.SubmitMulti, SMPPServerSession)}</li>
 * </ul>
 * Both implementation of those method might take long time.
 * </p>
 * @author uudashr
 */
public interface ServerResponseDeliveryListener {

    /**
     * This event raised when submit_sm_resp delivery is succeed.
     * 
     * @param messageId is the message id that will be sent to client as
     *        response.
     * @param source is the session who handle this response.
     */
    void onSubmitSmRespSent(MessageId messageId, SMPPServerSession source);

    /**
     * This event raised when submit_sm_resp delivery is failed.
     * 
     * @param messageId is the message id that will be sent to client as
     *        response.
     * @param cause is the failure cause.
     * @param source is the session who handle this response.
     */
    void onSubmitSmRespError(MessageId messageId, Exception cause,
            SMPPServerSession source);
    
    /**
     * This event raised when submit_multi_resp delivery is succeed.
     * 
     * @param submitMultiResult is the result that will be sent to client as
     *        response.
     * @param source is the session who handle this response.
     */
    void onSubmitMultiRespSent(SubmitMultiResult submitMultiResult,
            SMPPServerSession source);
    
    /**
     * This event raised when submit_multi_resp delivery is failed.
     * 
     * @param submitMultiResult is the result that will be sent to client as
     *        response.
     * @param cause is the failure cause.
     * @param source is the session who handle this response.
     */
    void onSubmitMultiRespError(SubmitMultiResult submitMultiResult,
            Exception cause, SMPPServerSession source);
}
