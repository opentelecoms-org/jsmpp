package org.jsmpp.examples;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.jsmpp.DefaultPDUSender;
import org.jsmpp.NumberingPlanIndicator;
import org.jsmpp.PDUSender;
import org.jsmpp.PDUStringException;
import org.jsmpp.TypeOfNumber;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.jsmpp.util.DefaultComposer;


/**
 * This is example of how to submit message using submit_sm command to SMSC.
 * 
 * @author uudashr
 * @version 1.0
 * 
 */
public class SubmitMessageExample {
    private int sequenceNumber;
    
    /**
     * Send simple text short message.
     * 
     * @param out is the output stream.
     * @param in is the input stream.
     * @param sourceAddr is the source address.
     * @param destinationAddr is the destination address.
     * @param message is the text message.
     */
    public void submitMessage(OutputStream out, InputStream in, String sourceAddr, String destinationAddr, String message) {
        // we need DefaultPDUSender to send SMPP Commands.
        PDUSender pduSender = new DefaultPDUSender(new DefaultComposer());
        try {
            pduSender.sendSubmitSm(out, ++sequenceNumber, null, TypeOfNumber.UNKNOWN, 
                    NumberingPlanIndicator.UNKNOWN, sourceAddr, TypeOfNumber.UNKNOWN, 
                    NumberingPlanIndicator.UNKNOWN, destinationAddr, new ESMClass(), 
                    (byte)0, (byte)0, null, null, 
                    new RegisteredDelivery().composeSMSCDelReceipt(SMSCDeliveryReceipt.SUCCESS_FAILURE), 
                    (byte)0, 
                    new GeneralDataCoding().composeCompressed(false).composeContainMessageClass(true).composeAlphabet(Alphabet.ALPHA_DEFAULT).composeMessageClass(MessageClass.CLASS1), 
                    (byte)0, message.getBytes());
        } catch (PDUStringException e) {
            // we have an invalid string parameter
            System.err.println("Invalid PDU String parameter found");
            e.printStackTrace();
        } catch (IOException e) {
            // there is an IO error found
            System.err.println("There is an IO error occur");
            e.printStackTrace();
        }
    }
}
