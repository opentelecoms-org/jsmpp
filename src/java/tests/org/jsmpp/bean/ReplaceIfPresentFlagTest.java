package org.jsmpp.bean;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * @author uudashr
 *
 */
public class ReplaceIfPresentFlagTest {
	
    @Test(groups="checkintest")
	public void testEquality() {
		ReplaceIfPresentFlag replaceIfPresentFlag = new ReplaceIfPresentFlag(1);
		
		assertEquals(replaceIfPresentFlag, new ReplaceIfPresentFlag(1));
	}
}
