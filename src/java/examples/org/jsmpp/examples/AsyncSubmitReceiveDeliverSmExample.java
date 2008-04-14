package org.jsmpp.examples;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jsmpp.BindType;
import org.jsmpp.InvalidResponseException;
import org.jsmpp.NumberingPlanIndicator;
import org.jsmpp.PDUStringException;
import org.jsmpp.TypeOfNumber;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.MessageType;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.extra.NegativeResponseException;
import org.jsmpp.extra.ProcessRequestException;
import org.jsmpp.extra.ResponseTimeoutException;
import org.jsmpp.session.BindParameter;
import org.jsmpp.session.MessageReceiverListener;
import org.jsmpp.session.SMPPSession;
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
        });
        
        // Now we will send 50 message asynchronously with max outstanding messages 10.
        ExecutorService execService = Executors.newFixedThreadPool(10);
        
        // requesting delivery report
        final RegisteredDelivery registeredDelivery = new RegisteredDelivery();
        registeredDelivery.setSMSCDeliveryReceipt(SMSCDeliveryReceipt.SUCCESS_FAILURE);
        
        for (int i = 0; i < 50; i++) {
            
            execService.execute(new Runnable() {
                public void run() {
                    try {
                        String messageId = session.submitShortMessage("CMT", TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, "1616", TypeOfNumber.INTERNATIONAL, NumberingPlanIndicator.UNKNOWN, "628176504657", new ESMClass(), (byte)0, (byte)1,  timeFormatter.format(new Date()), null, registeredDelivery, (byte)0, new GeneralDataCoding(false, true, MessageClass.CLASS1, Alphabet.ALPHA_DEFAULT), (byte)0, "jSMPP simplify SMPP on Java platform".getBytes());
                        System.out.println("Message submitted, message_id is " + messageId);
                    } catch (PDUStringException e) {
                        System.err.println("Invalid string parameter");
                        e.printStackTrace();
                    } catch (ResponseTimeoutException e) {
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
                    }
                }
            });
        }
        
        
        session.unbindAndClose();
    }
    
}
