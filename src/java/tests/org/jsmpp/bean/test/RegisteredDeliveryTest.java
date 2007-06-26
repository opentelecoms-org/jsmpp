package org.jsmpp.bean.test;

import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.SMSCDeliveryReceipt;

import junit.framework.TestCase;

/**
 * @author uudashr
 *
 */
public class RegisteredDeliveryTest extends TestCase {
	
	@Override
	protected void setUp() throws Exception {
	}
	
	@Override
	protected void tearDown() throws Exception {
	}
	
	public void testSMSCDeliveryReceipt() {
		RegisteredDelivery regDel = new RegisteredDelivery();
		RegisteredDelivery regDelSuccessFailure = regDel.composeSMSCDelReceipt(SMSCDeliveryReceipt.SUCCESS_FAILURE);
		RegisteredDelivery regDelSuccess = regDelSuccessFailure.composeSMSCDelReceipt(SMSCDeliveryReceipt.SUCCESS);
		
		assertEquals(SMSCDeliveryReceipt.DEFAULT, regDel.getSMSCDeliveryReceipt());
		assertEquals(SMSCDeliveryReceipt.SUCCESS_FAILURE, regDelSuccessFailure.getSMSCDeliveryReceipt());
		assertEquals(SMSCDeliveryReceipt.SUCCESS, regDelSuccess.getSMSCDeliveryReceipt());
	}
}
