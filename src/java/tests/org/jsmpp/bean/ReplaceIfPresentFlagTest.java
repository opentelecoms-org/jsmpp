package org.jsmpp.bean;

import static org.junit.Assert.*;
import org.jsmpp.bean.ReplaceIfPresentFlag;
import org.junit.Test;

/**
 * @author uudashr
 *
 */
public class ReplaceIfPresentFlagTest {
	
    @Test
	public void testEquality() {
		ReplaceIfPresentFlag replaceIfPresentFlag = new ReplaceIfPresentFlag(1);
		
		assertEquals(new ReplaceIfPresentFlag(1), replaceIfPresentFlag);
	}
}
