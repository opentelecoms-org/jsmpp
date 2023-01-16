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
 */
package org.jsmpp.examples;

import java.io.IOException;
import java.util.Date;
import java.util.Random;

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.DataCodings;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.OptionalParameters;
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
public class SubmitLongMessageExample {
    private static final Logger log = LoggerFactory.getLogger(SubmitLongMessageExample.class);
    private static final TimeFormatter TIME_FORMATTER = new AbsoluteTimeFormatter();
    private static final Random RANDOM = new Random();
    
    public static void main(String[] args) {
        try (SMPPSession session = new SMPPSession()) {
            session.connectAndBind("localhost", 8056, new BindParameter(BindType.BIND_TX, "test", "test", "cp", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));

            final int totalSegments = 3;
            OptionalParameter sarMsgRefNum = OptionalParameters.newSarMsgRefNum((short)RANDOM.nextInt());
            OptionalParameter sarTotalSegments = OptionalParameters.newSarTotalSegments(totalSegments);

            for (int i = 0; i < totalSegments; i++) {
                final int seqNum = i + 1;
                String message = "Message part " + seqNum + " of " + totalSegments + " ";
                OptionalParameter sarSegmentSeqnum = OptionalParameters.newSarSegmentSeqnum(seqNum);
                String messageId = submitMessage(session, message, sarMsgRefNum, sarSegmentSeqnum, sarTotalSegments);
                log.info("Message submitted, message_id is {}", messageId);
            }

            session.unbind();

        } catch (IOException e) {
            log.error("Failed connect and bind to host", e);
        }
    }
    
    public static String submitMessage(SMPPSession session, String message, OptionalParameter sarMsgRefNum, OptionalParameter sarSegmentSeqnum, OptionalParameter sarTotalSegments) {
        String messageId = null;
        try {
            SubmitSmResult submitSmResult = session.submitShortMessage("CMT", TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, "1616", TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, "628176504657", new ESMClass(), (byte)0, (byte)1,  TIME_FORMATTER
                .format(new Date()), null, new RegisteredDelivery(SMSCDeliveryReceipt.DEFAULT), (byte)0, DataCodings.ZERO, (byte)0, message.getBytes(), sarMsgRefNum, sarSegmentSeqnum, sarTotalSegments);
            messageId = submitSmResult.getMessageId();
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
        return messageId;
    }
}
