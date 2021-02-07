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

import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.extra.ProcessRequestException;

/**
 * {@code ResponseHandler} provide an interface to handle responses of the session routines.
 *
 * @author uudashr
 * @version 1.0
 * @since 2.0
 *
 */
public interface ResponseHandler extends BaseResponseHandler {

    /**
     * Process the deliver sm
     * 
     * @param deliverSm the deliver short message object
     * @throws ProcessRequestException if there is a failure when processing the deliver_sm
     */
    void processDeliverSm(DeliverSm deliverSm)
            throws ProcessRequestException;

    /**
     * Response by sending <b>DELIVER_SM_RESP</b> to SMSC.
     *
     * @param commandStatus the command status
     * @param sequenceNumber the sequence number of original <b>DELIVER_SM</b> request
     * @param messageId the message_id
     * @throws IOException if an input or output error occurred
     */
    void sendDeliverSmResp(int commandStatus, int sequenceNumber, String messageId) throws IOException;

    /**
     * Process the alert notification
     *
     * @param alertNotification the alert_notification object
     */
    void processAlertNotification(AlertNotification alertNotification);
}
