package org.jsmpp.bean;

import static org.testng.Assert.*;
import org.testng.annotations.Test;


/**
 * @author uudashr
 *
 */
public class RegisteredDeliveryTest {
	
    @Test(groups="checkintest")
	public void testSMSCDeliveryReceipt() {
		RegisteredDelivery regDel = new RegisteredDelivery();
		assertTrue(SMSCDeliveryReceipt.DEFAULT.containedIn(regDel));
		
		regDel.setSMSCDeliveryReceipt(SMSCDeliveryReceipt.SUCCESS_FAILURE);
		assertTrue(SMSCDeliveryReceipt.SUCCESS_FAILURE.containedIn(regDel));
		
		regDel.setSMSCDeliveryReceipt(SMSCDeliveryReceipt.SUCCESS);
		assertTrue(SMSCDeliveryReceipt.SUCCESS.containedIn(regDel));
	}
}
