package org.jsmpp.bean.test;

import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GSMSpecificFeature;
import org.jsmpp.bean.MessageType;

import junit.framework.TestCase;

/**
 * @author uudashr
 *
 */
public class ESMClassTest extends TestCase {
	
	@Override
	protected void setUp() throws Exception {
	}
	
	@Override
	protected void tearDown() throws Exception {
	}
	
	public void testSpecificFeature() {
		ESMClass esmClass = new ESMClass();
		ESMClass udhiProduct = esmClass.composeSpecificFeature(GSMSpecificFeature.UDHI);
		
		assertEquals(GSMSpecificFeature.DEFAULT, esmClass.getSpecificFeature());
		assertEquals(GSMSpecificFeature.UDHI, udhiProduct.getSpecificFeature());
	}
	
	public void testMessageType() {
		ESMClass esmClass = new ESMClass();
		ESMClass product = esmClass.composeMessageType(MessageType.SMSC_DEL_RECEIPT);
		
		assertEquals(MessageType.DEFAULT, esmClass.getMessageType());
		assertEquals(MessageType.SMSC_DEL_RECEIPT, product.getMessageType());
	}
}
