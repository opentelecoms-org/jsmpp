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
import org.jsmpp.bean.SubmitMultiResult;
import org.jsmpp.bean.TypeOfNumber;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.SMPPSession;
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.TimeFormatter;

/**
 *
 * Sample example for Submit Multi SMS
 *
 */
public class SubmitMultiExample {

    private static TimeFormatter timeFormatter = new AbsoluteTimeFormatter();

    public static void main(String[] args) {

        // Create a new SMPP Session
        SMPPSession session = new SMPPSession();
        try {
            
            session.setMessageReceiverListener(new MessageReceiverListenerImpl());
            
            // Bind to the Server
            session.connectAndBind("localhost", 8056,
                                    new BindParameter(BindType.BIND_TRX, "test",
                                                        "test", "cp",
                                                        TypeOfNumber.UNKNOWN,
                                                        NumberingPlanIndicator.UNKNOWN,
                                                        null));
            
        } catch (IOException e) {
            System.err.println("Failed connect and bind to host");
            e.printStackTrace();
        }

        try {
            Address address1 = new Address(TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, "628176504657");
            Address address2 = new Address(TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, "628176504658");
            Address[] addresses = new Address[] {address1, address2};
            SubmitMultiResult result = session.submitMultiple("CMT", TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, "1616",
                                                        addresses, new ESMClass(), (byte)0, (byte)1, timeFormatter.format(new Date()), null,
                                                        new RegisteredDelivery(SMSCDeliveryReceipt.SUCCESS), ReplaceIfPresentFlag.REPLACE,
                                                        new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false), (byte)0,
                                                        "jSMPP simplify SMPP on Java platform".getBytes());
            System.out.println("Messages submitted, result is " + result);
            Thread.sleep(2000);
        } catch (PDUException e) {
            // Invalid PDU parameter
            System.err.println("Invalid PDU parameter");
            e.printStackTrace();
        } catch (ResponseTimeoutException e) {
            // Response timeout
            System.err.println("Response timeout");
            e.printStackTrace();
        } catch (InvalidResponseException e) {
            // Invalid response
            System.err.println("Receive invalid respose");
            e.printStackTrace();
        } catch (NegativeResponseException e) {
            // Receiving negative response (non-zero command_status)
            System.err.println("Receive negative response");
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("IO error occur");
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Thread interrupted");
            e.printStackTrace();
        }

        session.unbindAndClose();
    }
}
