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
package org.jsmpp.bean;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * @author uudashr
 *
 */
public class SimpleDataCodingTest {

    @Test
    public void testDefault() {
        // 11110001
        SimpleDataCoding dataCoding = new SimpleDataCoding();
        byte expected = (byte)0xf1;
        assertEquals(dataCoding.toByte(), expected);

        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
    }
    
    @Test
    public void alphaDefaultClass0() {
        // 11110000
        SimpleDataCoding dataCoding = new SimpleDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS0);
        byte expected = (byte)0xf0;
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
    }
    
    @Test
    public void alphaDefaultClass1() {
        // 11110001
        SimpleDataCoding dataCoding = new SimpleDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1);
        byte expected = (byte)0xf1;
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
    }
    
    @Test
    public void alphaDefaultClass2() {
        // 11110010
        SimpleDataCoding dataCoding = new SimpleDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS2);
        byte expected = (byte)0xf2;
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
    }
    
    @Test
    public void alphaDefaultClass3() {
        // 11110011
        SimpleDataCoding dataCoding = new SimpleDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS3);
        byte expected = (byte)0xf3;
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
    }
    
    @Test
    public void alpha8BitClass0() {
        // 11110100
        SimpleDataCoding dataCoding = new SimpleDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS0);
        byte expected = (byte)0xf4;
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
    }
    
    @Test
    public void alpha8BitClass1() {
        // 11110101
        SimpleDataCoding dataCoding = new SimpleDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS1);
        byte expected = (byte)0xf5;
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
    }
    
    @Test
    public void alpha8BitClass2() {
        // 11110110
        SimpleDataCoding dataCoding = new SimpleDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS2);
        byte expected = (byte)0xf6;
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
    }
    
    @Test
    public void alpha8BitClass3() {
        // 11110111
        SimpleDataCoding dataCoding = new SimpleDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS3);
        byte expected = (byte)0xf7;
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testIllegalAlphabetUcs2() {
        new SimpleDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS1);
    }
}
