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

import org.jsmpp.InvalidResponseException;
import org.jsmpp.PDUException;
import org.jsmpp.bean.AlertNotification;
import org.jsmpp.bean.BindType;
import org.jsmpp.bean.DataCodings;
import org.jsmpp.bean.DataSm;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.MessageType;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
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
import org.jsmpp.util.MessageIDGenerator;
import org.jsmpp.util.MessageId;
import org.jsmpp.util.RandomMessageIDGenerator;
import org.jsmpp.util.TimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author uudashr
 *
 */
public class AsyncSubmitReceiveDeliverSmExample {
    private static final Logger log = LoggerFactory.getLogger(AsyncSubmitReceiveDeliverSmExample.class);
    private static final TimeFormatter TIME_FORMATTER = new AbsoluteTimeFormatter();
    private static final MessageIDGenerator MESSAGE_ID_GENERATOR = new RandomMessageIDGenerator();

    public static void main(String[] args) {
        final AtomicInteger counter = new AtomicInteger();

        final SMPPSession session = new SMPPSession();
        try {
            session.connectAndBind("localhost", 8056, new BindParameter(BindType.BIND_TRX, "test", "test", "cp", TypeOfNumber.UNKNOWN, NumberingPlanIndicator.UNKNOWN, null));
        } catch (IOException e) {
            log.error("Failed connect and bind to host", e);
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
                        log.info("Receiving delivery receipt for message '{}' : {}", messageId, delReceipt);
                    } catch (InvalidDeliveryReceiptException e) {
                        log.error("Failed getting delivery receipt", e);
                    }
                } else {
                    // regular short message
                    log.info("Receiving message : {}", new String(deliverSm.getShortMessage()));
                }
            }

            public void onAcceptAlertNotification(AlertNotification alertNotification) {
                log.info("Receiving alert_notification");
            }

            public DataSmResult onAcceptDataSm(DataSm dataSm, Session source)
                    throws ProcessRequestException {
                MessageId messageId = MESSAGE_ID_GENERATOR.newMessageId();
                log.info("Receiving data_sm, generated message id {}", messageId.getValue());
                return new DataSmResult(messageId, new OptionalParameter[]{});
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
                        SubmitSmResult submitSmResult = session.submitShortMessage("CMT", TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, "1616", TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, "628176504657", new ESMClass(), (byte)0, (byte)1,  TIME_FORMATTER
                            .format(new Date()), null, registeredDelivery, (byte)0, DataCodings.ZERO, (byte)0, "jSMPP simplify SMPP on Java platform".getBytes());
                        String messageId = submitSmResult.getMessageId();
                        log.info("Message submitted, message_id is {}", messageId);
                    } catch (PDUException e) {
                        log.error("Invalid PDU parameter", e);
                        counter.incrementAndGet();
                    } catch (ResponseTimeoutException e) {
                        log.error("Response timeout", e);
                        counter.incrementAndGet();
                    } catch (InvalidResponseException e) {
                        // Invalid response
                        log.error("Receive invalid response", e);
                        counter.incrementAndGet();
                    } catch (NegativeResponseException e) {
                        // Receiving negative response (non-zero command_status)
                        log.error("Receive negative response", e);
                        counter.incrementAndGet();
                    } catch (IOException e) {
                        log.error("I/O error occurred", e);
                        counter.incrementAndGet();
                    }
                }
            });
        }

        while (counter.get() != maxMessage) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                log.error("Interrupted");
                //re-interrupt the current thread
                Thread.currentThread().interrupt();
                break;
            }
        }
        session.unbindAndClose();
        execService.shutdown();
    }

}
