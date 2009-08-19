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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.BasicConfigurator;
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
import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.InvalidDeliveryReceiptException;
import org.jsmpp.util.TimeFormatter;

/**
 * @author uudashr
 *
 */
public class AsyncSubmitReceiveDeliverSmExample {
    private static TimeFormatter timeFormatter = new AbsoluteTimeFormatter();;
    public static void main(String[] args) {
        final AtomicInteger counter = new AtomicInteger();
        
        BasicConfigurator.configure();
        final SMPPSession session = new SMPPSession();
        try {
            session.connectAndBind("localhost", 8056, new BindParameter(BindType.BIND_TRX, "test", "test", "cp", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));
        } catch (IOException e) {
            System.err.println("Failed connect and bind to host");
            e.printStackTrace();
        }
        
        // Set listener to receive deliver_sm
        session.setMessageReceiverListener(new MessageReceiverListener() {
            public void onAcceptDeliverSm(DeliverSm deliverSm)
                    throws ProcessRequestException {
                if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass())) {
                    counter.incrementAndGet();
                    // delivery receipt
                    try {
                        DeliveryReceipt delReceipt = deliverSm.getShortMessageAsDeliveryReceipt();
                        long id = Long.parseLong(delReceipt.getId()) & 0xffffffff;
                        String messageId = Long.toString(id, 16).toUpperCase();
                        System.out.println("Receiving delivery receipt for message '" + messageId + "' : " + delReceipt);
                    } catch (InvalidDeliveryReceiptException e) {
                        System.err.println("Failed getting delivery receipt");
                        e.printStackTrace();
                    }
                } else {
                    // regular short message
                    System.out.println("Receiving message : " + new String(deliverSm.getShortMessage()));
                }
            }
            
            public DataSmResult onAcceptDataSm(DataSm dataSm, Session source)
                    throws ProcessRequestException {
                // TODO Auto-generated method stub
                return null;
            }
            
            public void onAcceptAlertNotification(
                    AlertNotification alertNotification) {
            }
        });
        
        // Now we will send 50 message asynchronously with max outstanding messages 10.
        ExecutorService execService = Executors.newFixedThreadPool(10);
        
        // requesting delivery report
        final RegisteredDelivery registeredDelivery = new RegisteredDelivery();
        registeredDelivery.setSMSCDeliveryReceipt(SMSCDeliveryReceipt.SUCCESS_FAILURE);
        final int maxMessage = 50;
        for (int i = 0; i < maxMessage; i++) {
            
            execService.execute(new Runnable() {
                public void run() {
                    try {
                        String messageId = session.submitShortMessage("CMT", TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, "1616", TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, "628176504657", new ESMClass(), (byte)0, (byte)1,  timeFormatter.format(new Date()), null, registeredDelivery, (byte)0, new GeneralDataCoding(false, true, MessageClass.CLASS1, Alphabet.ALPHA_DEFAULT), (byte)0, "jSMPP simplify SMPP on Java platform".getBytes());
                        System.out.println("Message submitted, message_id is " + messageId);
                    } catch (PDUException e) {
                        System.err.println("Invalid PDU parameter");
                        e.printStackTrace();
                        counter.incrementAndGet();
                    } catch (ResponseTimeoutException e) {
                        System.err.println("Response timeout");
                        e.printStackTrace();
                        counter.incrementAndGet();
                    } catch (InvalidResponseException e) {
                        // Invalid response
                        System.err.println("Receive invalid respose");
                        e.printStackTrace();
                        counter.incrementAndGet();
                    } catch (NegativeResponseException e) {
                        // Receiving negative response (non-zero command_status)
                        System.err.println("Receive negative response");
                        e.printStackTrace();
                        counter.incrementAndGet();
                    } catch (IOException e) {
                        System.err.println("IO error occur");
                        e.printStackTrace();
                        counter.incrementAndGet();
                    }
                }
            });
        }
        
        while (counter.get() != maxMessage) {
            try { Thread.sleep(1000); } catch (InterruptedException e) { }
        }
        session.unbindAndClose();
        execService.shutdown();
    }
    
}
