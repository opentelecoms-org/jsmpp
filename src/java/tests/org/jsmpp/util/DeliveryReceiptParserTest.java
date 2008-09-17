package org.jsmpp.util;

import static org.testng.Assert.*;
import org.jsmpp.bean.DeliveryReceipt;
import org.testng.annotations.Test;

/**
 * @author uudashr
 *
 */
public class DeliveryReceiptParserTest {
    private PDUDecomposer decomposer = new DefaultDecomposer();
    private static final String ORIGINAL_MESSAGE = "testing jsmpp bow";
    
    @Test
    public void parseTextWithLowerCase() {
        try {
            DeliveryReceipt delReceipt = decomposer.deliveryReceipt("id:0123456789 sub:001 dlvrd:001 submit date:200809011130 done date:200809011131 stat:DELIVRD err:000 text:" + ORIGINAL_MESSAGE);
            assertEquals(delReceipt.getText(), ORIGINAL_MESSAGE);
        } catch (InvalidDeliveryReceiptException e) {
            fail("Failed parsing delivery receipt:" + e.getMessage());
        }
    }
    
    @Test
    public void parsingTextWithUpperCase() {
        try {
            DeliveryReceipt delReceipt = decomposer.deliveryReceipt("id:0123456789 sub:001 dlvrd:001 submit date:200809011130 done date:200809011131 stat:DELIVRD err:000 Text:" + ORIGINAL_MESSAGE);
            assertEquals(delReceipt.getText(), ORIGINAL_MESSAGE);
        } catch (InvalidDeliveryReceiptException e) {
            fail("Failed parsing delivery receipt:" + e.getMessage());
        }
    }
    
    @Test
    public void parseWithNoTextAttribute() {
        try {
            DeliveryReceipt delReceipt = decomposer.deliveryReceipt("id:0123456789 sub:001 dlvrd:001 submit date:200809011130 done date:200809011131 stat:DELIVRD err:000 " + ORIGINAL_MESSAGE);
            assertEquals(delReceipt.getText(), null);
        } catch (InvalidDeliveryReceiptException e) {
            e.printStackTrace();
            fail("Failed parsing delivery receipt:" + e.getMessage());
        }
    }
}
