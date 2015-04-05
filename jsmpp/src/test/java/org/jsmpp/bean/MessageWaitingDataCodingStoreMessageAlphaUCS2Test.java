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

import static org.testng.Assert.*;

import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.DataCodings;
import org.jsmpp.bean.IndicationSense;
import org.jsmpp.bean.IndicationType;
import org.jsmpp.bean.MessageWaitingDataCoding;
import org.testng.annotations.Test;

/**
 * @author uudashr
 *
 */
public class MessageWaitingDataCodingStoreMessageAlphaUCS2Test {
    
    @Test
    public void messageWaitingDiscardMessageInactiveVoicemail() {
        // 11100000
        MessageWaitingDataCoding dataCoding = new MessageWaitingDataCoding(
                IndicationSense.INACTIVE,
                IndicationType.VOICEMAIL_MESSAGE_WAITING,
                Alphabet.ALPHA_UCS2);
        byte expected = (byte)0xe0;

        assertEquals(dataCoding.toByte(), expected, "Datacoding differs");
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
        
        assertTrue(dataCoding.isStoreMessage());
    }
    
    @Test
    public void messageWaitingDiscardMessageInactiveFax() {
        // 11100001
        MessageWaitingDataCoding dataCoding = new MessageWaitingDataCoding(
                IndicationSense.INACTIVE,
                IndicationType.FAX_MESSAGE_WAITING, 
                Alphabet.ALPHA_UCS2);
        byte expected = (byte)0xe1;
        
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
        
        assertTrue(dataCoding.isStoreMessage());
    }
    
    @Test
    public void messageWaitingDiscardMessageInactiveElecmail() {
        // 11100010
        MessageWaitingDataCoding dataCoding = new MessageWaitingDataCoding(
                IndicationSense.INACTIVE,
                IndicationType.ELECTRONIC_MESSAGE_WAITING, 
                Alphabet.ALPHA_UCS2);
        byte expected = (byte)0xe2;
        
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
        
        assertTrue(dataCoding.isStoreMessage());
    }
    
    @Test
    public void messageWaitingDiscardMessageInactiveOther() {
        // 11100011
        MessageWaitingDataCoding dataCoding = new MessageWaitingDataCoding(
                IndicationSense.INACTIVE,
                IndicationType.OTHER_MESSAGE_WAITING, 
                Alphabet.ALPHA_UCS2);
        byte expected = (byte)0xe3;
        
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
        
        assertTrue(dataCoding.isStoreMessage());
    }
    
    @Test
    public void messageWaitingDiscardMessageActiveVoicemail() {
        // 11101000
        MessageWaitingDataCoding dataCoding = new MessageWaitingDataCoding(
                IndicationSense.ACTIVE,
                IndicationType.VOICEMAIL_MESSAGE_WAITING, 
                Alphabet.ALPHA_UCS2);
        byte expected = (byte)0xe8;
        
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
        
        assertTrue(dataCoding.isStoreMessage());
    }
    
    @Test
    public void messageWaitingDiscardMessageActiveFax() {
        // 11101001
        MessageWaitingDataCoding dataCoding = new MessageWaitingDataCoding(
                IndicationSense.ACTIVE,
                IndicationType.FAX_MESSAGE_WAITING, 
                Alphabet.ALPHA_UCS2);
        byte expected = (byte)0xe9;
        
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
        
        assertTrue(dataCoding.isStoreMessage());
    }
    
    @Test
    public void messageWaitingDiscardMessageActiveElecmail() {
        // 11101010
        MessageWaitingDataCoding dataCoding = new MessageWaitingDataCoding(
                IndicationSense.ACTIVE,
                IndicationType.ELECTRONIC_MESSAGE_WAITING, 
                Alphabet.ALPHA_UCS2);
        byte expected = (byte)0xea;
        
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
        
        assertTrue(dataCoding.isStoreMessage());
    }
    
    @Test
    public void messageWaitingDiscardMessageActiveOther() {
        // 11101011
        MessageWaitingDataCoding dataCoding = new MessageWaitingDataCoding(
                IndicationSense.ACTIVE,
                IndicationType.OTHER_MESSAGE_WAITING, 
                Alphabet.ALPHA_UCS2);
        byte expected = (byte)0xeb;
        
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
        
        assertTrue(dataCoding.isStoreMessage());
    }
}