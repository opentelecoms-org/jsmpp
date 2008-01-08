package org.jsmpp.bean;

import static org.testng.Assert.*;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * @author uudashr
 *
 */
public class ESMClassTest {
	private ESMClass esmClass;
	
    @BeforeTest
    public void setUp() {
        esmClass = new ESMClass();
    }
    
    @Test(groups="checkintest")
	public void testSpecificFeature() {
		assertTrue(GSMSpecificFeature.DEFAULT.containedIn(esmClass));
		
		
		esmClass.setSpecificFeature(GSMSpecificFeature.UDHI);
		assertTrue(GSMSpecificFeature.UDHI.containedIn(esmClass));
	}
	
    @Test(groups="checkintest")
	public void testMessageType() {
		assertTrue(MessageType.DEFAULT.containedIn(esmClass));
		
		esmClass.setMessageType(MessageType.SMSC_DEL_RECEIPT);
		assertTrue(MessageType.SMSC_DEL_RECEIPT.containedIn(esmClass));
	}
}
