/*
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
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
        byteBuffer = new PDUByteBuffer(new SimpleCapacityPolicy());
    }
    
    @Test(groups="checkintest")
    public void initialLengthValidity() {
        byte[] pdu = byteBuffer.toBytes();
        assertEquals(pdu.length, INITIAL_LENGTH);
        assertEquals(pdu.length, byteBuffer.getCommandLengthValue());
        assertEquals(pdu.length, byteBuffer.getBytesLength());
    }
    
    @Test(groups="checkintest", dependsOnMethods="initialLengthValidity")
    public void testLengthValidityForIntAppends() {
        for (int i = 0; i < 5; i++) {
            byteBuffer.append(10);
            byte[] pdu = byteBuffer.toBytes();
            assertEquals(pdu.length, INITIAL_LENGTH + (INTEGER_LENGTH * (i + 1)), "Failed on iteration " + i);
            assertEquals(pdu.length, byteBuffer.getCommandLengthValue());
            assertEquals(pdu.length, byteBuffer.getBytesLength());
        }
    }
    
    @Test(groups="checkintest", dependsOnMethods="initialLengthValidity")
    public void testLengthValidityForByteAppends() {
        for (int i = 0; i < 5; i++) {
            byteBuffer.append((byte)10);
            byte[] pdu = byteBuffer.toBytes();
            assertEquals(pdu.length, INITIAL_LENGTH + (BYTE_LENGTH * (i + 1)), "Failed on iteration " + i);
            assertEquals(pdu.length, byteBuffer.getCommandLengthValue(), "Failed on iteration " + i);
            assertEquals(pdu.length, byteBuffer.getBytesLength(), "Failed on iteration " + i);
        }
    }
    
    @Test(groups="checkintest", dependsOnMethods="initialLengthValidity")
    public void testLengthValidityForCString() {
        String hello = "PDUByteBuffer integration test for JSMPP API";
        for (int i = 0; i < 5; i++) {
            byteBuffer.append(hello);
            byte[] pdu = byteBuffer.toBytes();
            assertEquals(pdu.length, INITIAL_LENGTH + (hello.length() * (i + 1)) + (i + 1), "Failed on iteration " + i);
            assertEquals(pdu.length, byteBuffer.getCommandLengthValue(), "Failed on iteration " + i);
            assertEquals(pdu.length, byteBuffer.getBytesLength(), "Failed on iteration " + i);
        }
    }
    
    @Test(groups="checkintest", dependsOnMethods="initialLengthValidity")
    public void testLengthValidityForString() {
        String hello = "PDUByteBuffer integration test for JSMPP API";
        for (int i = 0; i < 5; i++) {
            byteBuffer.append(hello, false);
            byte[] pdu = byteBuffer.toBytes();
            assertEquals(pdu.length, INITIAL_LENGTH + (hello.length() * (i + 1)), "Failed on iteration " + i);
            assertEquals(pdu.length, byteBuffer.getCommandLengthValue(), "Failed on iteration " + i);
            assertEquals(pdu.length, byteBuffer.getBytesLength(), "Failed on iteration " + i);
        }
    }
}
