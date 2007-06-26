package org.jsmpp.bean.test;

import org.jsmpp.bean.ReplaceIfPresentFlag;

import junit.framework.TestCase;

/**
 * @author uudashr
 *
 */
public class ReplaceIfPresentFlagTest extends TestCase {
	@Override
	protected void setUp() throws Exception {
	}
	
	@Override
	protected void tearDown() throws Exception {
	}
	
	public void testEquality() {
		ReplaceIfPresentFlag replaceIfPresentFlag = new ReplaceIfPresentFlag(1);
		
		assertEquals(new ReplaceIfPresentFlag(1), replaceIfPresentFlag);
	}
}
