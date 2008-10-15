package org.jsmpp.util;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.Calendar;
import java.util.Date;

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
            DeliveryReceipt delReceipt = decomposer.deliveryReceipt("id:0123456789 sub:001 dlvrd:001 submit date:0809011130 done date:0809011131 stat:DELIVRD err:000 text:" + ORIGINAL_MESSAGE);
            assertEquals(delReceipt.getText(), ORIGINAL_MESSAGE);
            
            Date submitDate = delReceipt.getSubmitDate();
            Date expectedSubmitDate = createDate(2008, 9, 1, 11, 30);
            assertEquals(submitDate, expectedSubmitDate);
            
            Date doneDate = delReceipt.getDoneDate();
            Date expectedDoneDate = createDate(2008, 9, 1, 11, 31);
            assertEquals(doneDate, expectedDoneDate);
        } catch (InvalidDeliveryReceiptException e) {
            fail("Failed parsing delivery receipt:" + e.getMessage());
        }
    }
    
    @Test
    public void parsingTextWithUpperCase() {
        try {
            DeliveryReceipt delReceipt = decomposer.deliveryReceipt("id:0123456789 sub:001 dlvrd:001 submit date:0809011130 done date:0809011131 stat:DELIVRD err:000 Text:" + ORIGINAL_MESSAGE);
            assertEquals(delReceipt.getText(), ORIGINAL_MESSAGE);
            
            Date submitDate = delReceipt.getSubmitDate();
            Date expectedSubmitDate = createDate(2008, 9, 1, 11, 30);
            assertEquals(submitDate, expectedSubmitDate);
            
            Date doneDate = delReceipt.getDoneDate();
            Date expectedDoneDate = createDate(2008, 9, 1, 11, 31);
            assertEquals(doneDate, expectedDoneDate);
        } catch (InvalidDeliveryReceiptException e) {
            fail("Failed parsing delivery receipt:" + e.getMessage());
        }
    }
    
    @Test
    public void parseWithNoTextAttribute() {
        try {
            DeliveryReceipt delReceipt = decomposer.deliveryReceipt("id:0123456789 sub:001 dlvrd:001 submit date:0809011130 done date:0809011131 stat:DELIVRD err:000 " + ORIGINAL_MESSAGE);
            assertEquals(delReceipt.getText(), null);
            
            Date submitDate = delReceipt.getSubmitDate();
            Date expectedSubmitDate = createDate(2008, 9, 1, 11, 30);
            assertEquals(submitDate, expectedSubmitDate);
            
            Date doneDate = delReceipt.getDoneDate();
            Date expectedDoneDate = createDate(2008, 9, 1, 11, 31);
            assertEquals(doneDate, expectedDoneDate);
        } catch (InvalidDeliveryReceiptException e) {
            e.printStackTrace();
            fail("Failed parsing delivery receipt:" + e.getMessage());
        }
    }
    
    private static Date createDate(int year, int month, int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day, hour, minute, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }
}
