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

/**
 * {@code ResponseHandler} provide interface to handle response of the session
 * routines.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 */
public interface OutboundResponseHandler extends GenericServerResponseHandler {

    void processBind(Bind bind);

    /**
     * Response by sending <b>DELIVER_SM_RESP</b> to SMSC.
     *
     * @param commandStatus the command status.
     * @param sequenceNumber is the sequence number of original <b>DELIVER_SM</b> request.
     * @param messageId the message_id.
     * @throws IOException if an IO error occur.
     */
    void sendDeliverSmResp(int commandStatus, int sequenceNumber, String messageId) throws IOException;


}
