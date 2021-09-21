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
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.DataCodings;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.session.SubmitSmResult;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.TimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 *
 */
public class SimpleSubmitRegisteredExample {
    private static final Logger log = LoggerFactory.getLogger(SimpleSubmitRegisteredExample.class);
    private static final TimeFormatter TIME_FORMATTER = new AbsoluteTimeFormatter();
    
    public static void main(String[] args) {
        SMPPSession session = new SMPPSession();
        // Set listener to receive deliver_sm
        session.setMessageReceiverListener(new MessageReceiverListenerImpl());
        
        try {
            String systemId = session.connectAndBind("localhost", 8056, new BindParameter(BindType.BIND_TRX, "test", "test", "cp", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));
            log.info("Connected with SMSC with system id {}", systemId);

            try {
                SubmitSmResult submitSmResult = session.submitShortMessage("CMT",
                    TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, "1616",
                    TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, "628176504657",
                    new ESMClass(), (byte)0, (byte)1,  TIME_FORMATTER.format(new Date()), null,
                    new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS_FAILURE), (byte)0, DataCodings.ZERO, (byte)0, "jSMPP simplify SMPP on Java platform".getBytes());

                String messageId = submitSmResult.getMessageId();
                /*
                 * you can save the submitted message to database.
                 */
                log.info("Message submitted, message_id is {}", messageId);
                Thread.sleep(2000);
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
            } catch (InterruptedException e) {
                log.error("Thread interrupted", e);
            }

            session.unbindAndClose();

        } catch (IOException e) {
            log.error("Failed connect and bind to host", e);
        }

    }
    
    
}
