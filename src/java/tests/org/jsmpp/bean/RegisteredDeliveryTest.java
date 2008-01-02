package org.jsmpp.bean;

import static org.junit.Assert.*;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;
import org.junit.Test;


/**
 * @author uudashr
 *
 */
public class RegisteredDeliveryTest {
	
    @Test
	public void testSMSCDeliveryReceipt() {
		RegisteredDelivery regDel = new RegisteredDelivery();
		assertTrue(SMSCDeliveryReceipt.DEFAULT.containedIn(regDel));
		
		regDel.setSMSCDeliveryReceipt(SMSCDeliveryReceipt.SUCCESS_FAILURE);
		assertTrue(SMSCDeliveryReceipt.SUCCESS_FAILURE.containedIn(regDel));
		
		regDel.setSMSCDeliveryReceipt(SMSCDeliveryReceipt.SUCCESS);
		assertTrue(SMSCDeliveryReceipt.SUCCESS.containedIn(regDel));
	}
}
