package org.jsmpp.util;

import static org.testng.Assert.assertEquals;

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
import org.testng.annotations.Test;

/**
 * @author uudashr
 *
 */
public class OctetUtilTest {
    
    /**
     * Test the int2bytes and bytes2int conversion.
     */
    @Test
    public void intConversion() {
        int in = Integer.MIN_VALUE;
        byte[] bytes = OctetUtil.intToBytes(in);
        assertEquals(bytes.length, 4);
        int out = OctetUtil.bytesToInt(bytes);
        assertEquals(out, in);
    }
    
    /**
     * Test the short2bytes and bytes2short conversion.
     */
    @Test
    public void shortConversion() {
        short in = Short.MIN_VALUE;
        byte[] bytes = OctetUtil.shortToBytes(in);
        assertEquals(bytes.length, 2);
        short out = OctetUtil.bytesToShort(bytes);
        assertEquals(out, in);
    }
    
    /**
     * Test another short2bytes and bytes2short conversion. 
     */
    @Test
    public void testAnotherShortConversion() {
        byte[] bytes = new byte[] {-10, 10};
        short shortVal = OctetUtil.bytesToShort(bytes);
        byte[] anotherByte = OctetUtil.shortToBytes(shortVal);
        assertEquals(anotherByte, bytes);
        short anotherShortVal = OctetUtil.bytesToShort(anotherByte);
        assertEquals(anotherShortVal, shortVal);
    }
    
    /**
     * This is test from igor.skornyakov
     */
    @Test
    public void testShortEncodeFromIgor() {
        assertEquals(0x1D4, OctetUtil.bytesToShort(new byte[] {1, (byte)0xD4}, 0));
    }
    
    /**
     * Short encoded test from igor.skornyakov
     */
    @Test
    public void testShortEncode() {
        short val = (short)0x1d4;
        byte[] bytes = OctetUtil.shortToBytes((short)0x1d4);
        short anotherVal = OctetUtil.bytesToShort(bytes);
        assertEquals(val, anotherVal);
    }
}
