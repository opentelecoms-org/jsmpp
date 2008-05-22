package org.jsmpp.util;
import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
/**
 * @author uudashr
 *
 */
public class PDUByteBufferTest {
    private static final int BYTE_LENGTH = 1;
    private static final int INTEGER_LENGTH = 4;
    private static final int INITIAL_LENGTH = INTEGER_LENGTH;
    
    private PDUByteBuffer byteBuffer;
    
    @BeforeMethod
    public void setUp() {
        byteBuffer = new PDUByteBuffer(new DumbCapacityPolicy());
    }
    
    @Test(groups="checkintest")
    public void initialLengthValidity() {
        assertEquals(byteBuffer.toBytes().length, INITIAL_LENGTH);
    }
    
    @Test(groups="checkintest", dependsOnMethods="initialLengthValidity")
    public void testLengthValidityForIntAppends() {
        for (int i = 0; i < 5; i++) {
            byteBuffer.append(10);
            assertEquals(byteBuffer.toBytes().length, INITIAL_LENGTH + (INTEGER_LENGTH * (i + 1)), "Failed on iteration " + i);
        }
    }
    
    @Test(groups="checkintest", dependsOnMethods="initialLengthValidity")
    public void testLengthValidityForByteAppends() {
        for (int i = 0; i < 5; i++) {
            byteBuffer.append((byte)10);
            assertEquals(byteBuffer.toBytes().length, INITIAL_LENGTH + (BYTE_LENGTH * (i + 1)), "Failed on iteration " + i);
        }
    }
    
    @Test(groups="checkintest", dependsOnMethods="initialLengthValidity")
    public void testLengthValidityForCString() {
        String hello = "PDUByteBuffer integration test for JSMPP API";
        for (int i = 0; i < 5; i++) {
            byteBuffer.append(hello);
            assertEquals(byteBuffer.toBytes().length, INITIAL_LENGTH + (hello.length() * (i + 1)) + (i + 1), "Failed on iteration " + i);
        }
    }
    
    @Test(groups="checkintest", dependsOnMethods="initialLengthValidity")
    public void testLengthValidityForString() {
        String hello = "PDUByteBuffer integration test for JSMPP API";
        for (int i = 0; i < 5; i++) {
            byteBuffer.append(hello, false);
            assertEquals(byteBuffer.toBytes().length, INITIAL_LENGTH + (hello.length() * (i + 1)), "Failed on iteration " + i);
        }
    }
}
