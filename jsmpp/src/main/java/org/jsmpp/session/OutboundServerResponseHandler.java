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

import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.Outbind;
import org.jsmpp.extra.ProcessRequestException;

/**
 * {@code OutboundServerResponseHandler} provide interface to handle response of the session
 * routines.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public interface OutboundServerResponseHandler extends BaseResponseHandler {

    /**
     * Process the outbind
     *
     * @param outbind the outbind systemId and password
     * @throws ProcessRequestException if there is a failure when processing outbind
     */
    void processOutbind(Outbind outbind)
        throws ProcessRequestException;

    /**
     * Process the deliver_sm
     *
     * @param deliverSm the deliver_sm command
     * @throws ProcessRequestException if there is a failure when processing deliver_sm
     */
    void processDeliverSm(DeliverSm deliverSm)
        throws ProcessRequestException;

    /**
     * Response by sending <b>DELIVER_SM_RESP</b> to SMSC.
     *
     * @param commandStatus is the command_status.
     * @param sequenceNumber is the sequence number of original <b>DELIVER_SM</b> request.
     * @param messageId is the message_id.
     * @throws IOException if an input/output error occurs.
     */
    void sendDeliverSmResp(int commandStatus, int sequenceNumber, String messageId) throws IOException;
}
