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

import org.jsmpp.bean.Command;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.EnquireLink;
import org.jsmpp.extra.PendingResponse;
import org.jsmpp.extra.ProcessRequestException;

/**
 * @author uudashr
 *
 */
public interface BaseResponseHandler {

    /**
     * Remove the previously {@link PendingResponse} that set when the request
     * was sent.
     * 
     * @param sequenceNumber the sequence number of the request.
     * @return the {@link PendingResponse} correspond to specified
     *         sequenceNumber. Return {@code null} if the the mapped
     *         sequenceNumber is not found
     */
    PendingResponse<Command> removeSentItem(int sequenceNumber);
    
    /**
     * Response by sending <b>GENERICK_NACK</b>.
     * 
     * @param commandStatus is the command status.
     * @param sequenceNumber is the sequence number original PDU if can be decoded.
     * @throws IOException if an IO error occur.
     */
    void sendGenericNack(int commandStatus, int sequenceNumber)
            throws IOException;
    /**
     * Response by sending negative response.
     * 
     * @param originalCommandId is the original command id.
     * @param commandStatus is the command status.
     * @param sequenceNumber is the sequence number of original PDU request.
     * @throws IOException if an IO error occur.
     */
    void sendNegativeResponse(int originalCommandId, int commandStatus,
            int sequenceNumber) throws IOException;
    
    /**
     * Response by sending <b>ENQUIRE_LINK_RESP</b>.
     * 
     * @param sequenceNumber is the sequence number of original <b>ENQUIRE_LINK</b>
     *          request.
     * @throws IOException if an IO error occur.
     */
    void sendEnquireLinkResp(int sequenceNumber) throws IOException;
    
    /**
     * Response by send <b>UNBIND_RESP</b>.
     * 
     * @param sequenceNumber is the sequence number of original <b>UNBIND</b> request.
     * @throws IOException if an IO error occur.
     */
    void sendUnbindResp(int sequenceNumber) throws IOException;

    void processEnquireLink(EnquireLink enquireLink);
    
    /**
     * Process the data short message.
     * 
     * @param dataSm is the data short message.
     * @return the result for data_sm_resp.
     * @throws ProcessRequestException if there is a failure when processing
     *         data_sm.
     */
    DataSmResult processDataSm(DataSm dataSm) throws ProcessRequestException;
    
    /**
     * Response by sending <b>DATA_SM_RESP</b> to SMSC.
     * 
     * @param dataSmResult is the result of data_sm.
     * @param sequenceNumber is the sequence number of original <b>DATA_SM</b>
     *        request.
     * @throws IOException if an IO error occur.
     */
    void sendDataSmResp(DataSmResult dataSmResult, int sequenceNumber) throws IOException;
    
    /**
     * Notify for unbind.
     */
    void notifyUnbonded();

}
