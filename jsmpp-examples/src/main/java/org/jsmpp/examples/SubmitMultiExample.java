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
import org.jsmpp.bean.Address;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.ReplaceIfPresentFlag;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.session.SubmitMultiResult;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.bean.UnsuccessDelivery;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.TimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Example for Submit Multi SMS
 *
 */
public class SubmitMultiExample {
    private static final Logger LOGGER = LoggerFactory.getLogger(SubmitMultiExample.class);
    private static final TimeFormatter TIME_FORMATTER = new AbsoluteTimeFormatter();

    public static void main(String[] args) {

        // Create a new SMPP Session
        SMPPSession session = new SMPPSession();
        try {
            session.setMessageReceiverListener(new MessageReceiverListenerImpl());
            
            // Bind to the Server
            String systemId = session.connectAndBind("localhost", 8056,
                                    new BindParameter(BindType.BIND_TRX, "test",
                                                        "test", "cp",
                                                        TypeOfNumber.UNKNOWN,
                                                        NumberingPlanIndicator.UNKNOWN,
                                                        null));
            LOGGER.info("Connected with SMSC with system id {}", systemId);

            try {
                Address address1 = new Address(TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, "628176504657");
                Address address2 = new Address(TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, "628176504658");
                Address[] addresses = new Address[] {address1, address2};
                SubmitMultiResult result = session.submitMultiple("CMT", TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, "1616",
                    addresses, new ESMClass(), (byte)0, (byte)1, TIME_FORMATTER.format(new Date()), null,
                    new RegisteredDelivery(SMSCDeliveryReceipt.FAILURE), ReplaceIfPresentFlag.REPLACE,
                    new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false), (byte)0,
                    "jSMPP simplifies SMPP on Java platform".getBytes());
                LOGGER.info("{} messages submitted, result message id {}", addresses.length, result.getMessageId());
                for (UnsuccessDelivery unsuccessDelivery: result.getUnsuccessDeliveries()){
                    LOGGER.info("Unsuccessful delivery to {}: {}", unsuccessDelivery.getDestinationAddress(), unsuccessDelivery.getErrorStatusCode());
                }
                Thread.sleep(2000);
            } catch (PDUException e) {
                // Invalid PDU parameter
                LOGGER.error("Invalid PDU parameter", e);
            } catch (ResponseTimeoutException e) {
                // Response timeout
                LOGGER.error("Response timeout", e);
            } catch (InvalidResponseException e) {
                // Invalid response
                LOGGER.error("Receive invalid response", e);
            } catch (NegativeResponseException e) {
                // Receiving negative response (non-zero command_status)
                LOGGER.error("Receive negative response", e);
            } catch (IOException e) {
                LOGGER.error("I/O error occurred", e);
            } catch (InterruptedException e) {
                LOGGER.error("Thread interrupted", e);
            }

            session.unbindAndClose();
            
        } catch (IOException e) {
            LOGGER.error("Failed connect and bind to host", e);
        }
    }
}
