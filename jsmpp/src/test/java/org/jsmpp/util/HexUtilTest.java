package org.jsmpp.util;


import static org.testng.Assert.assertEquals;
import org.testng.annotations.Test;


public class HexUtilTest {
	
	@Test
	public void testEncode()
	{
		String res = HexUtil.convertStringToHexString("hello world");
		
		assertEquals("68656c6c6f20776f726c64", res);
	}
}
