package org.jsmpp.bean;

import static org.junit.Assert.*;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GSMSpecificFeature;
import org.jsmpp.bean.MessageType;
import org.junit.Before;
import org.junit.Test;

/**
 * @author uudashr
 *
 */
public class ESMClassTest {
	private ESMClass esmClass;
	
    @Before
    public void setUp() {
        esmClass = new ESMClass();
    }
    
    @Test
	public void testSpecificFeature() {
		assertTrue(GSMSpecificFeature.DEFAULT.containedIn(esmClass));
		
		
		esmClass.setSpecificFeature(GSMSpecificFeature.UDHI);
		assertTrue(GSMSpecificFeature.UDHI.containedIn(esmClass));
	}
	
    @Test
	public void testMessageType() {
		assertTrue(MessageType.DEFAULT.containedIn(esmClass));
		
		esmClass.setMessageType(MessageType.SMSC_DEL_RECEIPT);
		assertTrue(MessageType.SMSC_DEL_RECEIPT.containedIn(esmClass));
	}
}
