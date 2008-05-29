package org.jsmpp.util;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

/**
 * @author uudashr
 *
 */
public class COctetStringNullOrFixedLengthTest {
    
    @Test(groups="checkintest")
    public void testNull() throws Exception {
        assertTrue(StringValidator.isCOctetStringNullOrNValValid((String)null, 10));
    }
    
    @Test(groups="checkintest")
    public void testEmpty() throws Exception {
        assertTrue(StringValidator.isCOctetStringNullOrNValValid("", 10));
    }
    
    @Test(groups="checkintest")
    public void testSingleChar() throws Exception {
        assertFalse(StringValidator.isCOctetStringNullOrNValValid("1", 10));
    }
    
    @Test(groups="checkintest")
    public void testFixedLength() throws Exception {
        assertTrue(StringValidator.isCOctetStringNullOrNValValid("123456789", 10));
    }
    
    @Test(groups="checkintest")
    public void testNonExceedLength() throws Exception {
         assertFalse(StringValidator.isCOctetStringNullOrNValValid("01234567", 10));
    }
    
    @Test(groups="checkintest")
    public void testExceedLength() throws Exception {
        assertFalse(StringValidator.isCOctetStringNullOrNValValid("01234567891", 10));
    }
}
