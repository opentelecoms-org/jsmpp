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
 * It's abstract adapter class that receive event of response delivery, an
 * implementation of {@link ServerResponseDeliveryListener}.
 * <p>
 * This is alternative from implementing {@link ServerResponseDeliveryListener}.
 * User only have to create subclass of this class and doesn't have to implement
 * all method declared on {@link ServerResponseDeliveryListener}.
 * </p>
 * 
 * @author uudashr
 * 
 */
public abstract class ServerResponseDeliveryAdapter implements
        ServerResponseDeliveryListener {
    
    /* (non-Javadoc)
     * @see org.jsmpp.session.ServerResponseDeliveryListener#onSubmitSmResponseSent(org.jsmpp.util.MessageId, org.jsmpp.session.SMPPServerSession)
     */
    public void onSubmitSmRespSent(MessageId messageId,
            SMPPServerSession source) {}
    
    /* (non-Javadoc)
     * @see org.jsmpp.session.ServerResponseDeliveryListener#onSubmitSmResponseError(org.jsmpp.util.MessageId, java.lang.Exception, org.jsmpp.session.SMPPServerSession)
     */
    public void onSubmitSmRespError(MessageId messageId, Exception e,
            SMPPServerSession source) {}
    
    /* (non-Javadoc)
     * @see org.jsmpp.session.ServerResponseDeliveryListener#onSubmitMultiResponseSent(org.jsmpp.bean.SubmitMultiResult, org.jsmpp.session.SMPPServerSession)
     */
    public void onSubmitMultiRespSent(SubmitMultiResult submitMultiResult,
            SMPPServerSession source) {}
    
    /* (non-Javadoc)
     * @see org.jsmpp.session.ServerResponseDeliveryListener#onSubmitMultiResposnseError(org.jsmpp.bean.SubmitMultiResult, java.lang.Exception, org.jsmpp.session.SMPPServerSession)
     */
    public void onSubmitMultiRespError(
            SubmitMultiResult submitMultiResult, Exception e,
            SMPPServerSession source) {}
}
