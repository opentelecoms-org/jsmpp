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
package org.jsmpp.examples;

import java.io.IOException;
import java.util.Date;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.MessageType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.DataSmResult;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.session.Session;
import org.jsmpp.session.SubmitSmResult;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.InvalidDeliveryReceiptException;
import org.jsmpp.util.TimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author terukizm
 * 
 */
public class SimpleSubmitSimpleReceiveExample {
    private static final Logger log = LoggerFactory.getLogger(SimpleSubmitSimpleReceiveExample.class);
    private static final TimeFormatter TIME_FORMATTER = new AbsoluteTimeFormatter();

    public static void main(String[] args) {
        String server = "localhost";
        int port = 8056;
        String message = "jSMPP simplify SMPP on Java platform";

        SMPPSession session = new SMPPSession();
        // Set listener to receive deliver_sm
        session.setMessageReceiverListener(new MessageReceiverListener() {

            public void onAcceptDeliverSm(DeliverSm deliverSm) throws ProcessRequestException {
                if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass())) {
                    // delivery receipt
                    try {
                        DeliveryReceipt delReceipt = deliverSm.getShortMessageAsDeliveryReceipt();
                        long id = Long.parseLong(delReceipt.getId()) & 0xffffffff;
                        String messageId = Long.toString(id, 16).toUpperCase();
                        log.info("received '{}' : {}", messageId, delReceipt);
                    } catch (InvalidDeliveryReceiptException e) {
                        log.error("receive failed, e");
                    }
                } else {
                    // regular short message
                    log.info("Receiving message : {}", new String(deliverSm.getShortMessage()));
                }
            }

            public void onAcceptAlertNotification(AlertNotification alertNotification) {
                log.info("Receiving alert for {} from {}", alertNotification.getSourceAddr(), alertNotification.getEsmeAddr());
            }

            public DataSmResult onAcceptDataSm(DataSm dataSm, Session source) throws ProcessRequestException {
                log.info("onAcceptDataSm");
                return null;
            }
        });
        try {

            String systemId = session.connectAndBind(server, port, new BindParameter(BindType.BIND_TRX, "test", "test", "cp",
                    TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));
            log.info("Connected with SMSC with system id {}", systemId);

            // send Message
            try {
                // set RegisteredDelivery
                final RegisteredDelivery registeredDelivery = new RegisteredDelivery();
                registeredDelivery.setSMSCDeliveryReceipt(SMSCDeliveryReceipt.SUCCESS_FAILURE);

                SubmitSmResult submitSmResult = session.submitShortMessage("CMT", TypeOfNumber.INTERNATIONAL,
                    NumberingPlanIndicator.UNKNOWN, "1616", TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN,
                    "628176504657", new ESMClass(), (byte)0, (byte)1, TIME_FORMATTER.format(new Date()), null,
                    registeredDelivery, (byte)0, new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1,
                        false), (byte)0, message.getBytes());

                String messageId = submitSmResult.getMessageId();
                log.info("Message submitted, message_id is {}", messageId);

            } catch (PDUException e) {
                // Invalid PDU parameter
                log.error("Invalid PDU parameter", e);
            } catch (ResponseTimeoutException e) {
                // Response timeout
                log.error("Response timeout", e);
            } catch (InvalidResponseException e) {
                // Invalid response
                log.error("Receive invalid response", e);
            } catch (NegativeResponseException e) {
                // Receiving negative response (non-zero command_status)
                log.error("Receive negative response", e);
            } catch (IOException e) {
                log.error("I/O error occurred", e);
            }

            // wait 3 second
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                log.info("Interrupted exception", e);
            }

            // unbind(disconnect)
            session.unbindAndClose();

        } catch (IOException e) {
            log.error("Failed connect and bind to host", e);
        }

        log.info("Finish!");
    }

}
