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
public class MessageWaitingDataCodingDiscardMessageTest {
    
    @Test
    public void messageWaitingDiscardMessageInactiveVoicemail() {
        // 11000000
        MessageWaitingDataCoding dataCoding = new MessageWaitingDataCoding(
                IndicationSense.INACTIVE,
                IndicationType.VOICEMAIL_MESSAGE_WAITING);
        byte expected = (byte)0xc0;
        
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
        
        assertFalse(dataCoding.isStoreMessage());
    }
    
    @Test
    public void messageWaitingDiscardMessageInactiveFax() {
        // 11000001
        MessageWaitingDataCoding dataCoding = new MessageWaitingDataCoding(
                IndicationSense.INACTIVE,
                IndicationType.FAX_MESSAGE_WAITING);
        byte expected = (byte)0xc1;
        
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
        
        assertFalse(dataCoding.isStoreMessage());
    }
    
    @Test
    public void messageWaitingDiscardMessageInactiveElecmail() {
        // 11000010
        MessageWaitingDataCoding dataCoding = new MessageWaitingDataCoding(
                IndicationSense.INACTIVE,
                IndicationType.ELECTRONIC_MESSAGE_WAITING);
        byte expected = (byte)0xc2;
        
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
        
        assertFalse(dataCoding.isStoreMessage());
    }
    
    @Test
    public void messageWaitingDiscardMessageInactiveOther() {
        // 11000011
        MessageWaitingDataCoding dataCoding = new MessageWaitingDataCoding(
                IndicationSense.INACTIVE,
                IndicationType.OTHER_MESSAGE_WAITING);
        byte expected = (byte)0xc3;
        
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
        
        assertFalse(dataCoding.isStoreMessage());
    }
    
    @Test
    public void messageWaitingDiscardMessageActiveVoicemail() {
        // 11001000
        MessageWaitingDataCoding dataCoding = new MessageWaitingDataCoding(
                IndicationSense.ACTIVE,
                IndicationType.VOICEMAIL_MESSAGE_WAITING);
        byte expected = (byte)0xc8;
        
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
        
        assertFalse(dataCoding.isStoreMessage());
    }
    
    @Test
    public void messageWaitingDiscardMessageActiveFax() {
        // 11001001
        MessageWaitingDataCoding dataCoding = new MessageWaitingDataCoding(
                IndicationSense.ACTIVE,
                IndicationType.FAX_MESSAGE_WAITING);
        byte expected = (byte)0xc9;
        
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
        
        assertFalse(dataCoding.isStoreMessage());
    }
    
    @Test
    public void messageWaitingDiscardMessageActiveElecmail() {
        // 11001010
        MessageWaitingDataCoding dataCoding = new MessageWaitingDataCoding(
                IndicationSense.ACTIVE,
                IndicationType.ELECTRONIC_MESSAGE_WAITING);
        byte expected = (byte)0xca;
        
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
        
        assertFalse(dataCoding.isStoreMessage());
    }
    
    @Test
    public void messageWaitingDiscardMessageActiveOther() {
        // 11001011
        MessageWaitingDataCoding dataCoding = new MessageWaitingDataCoding(
                IndicationSense.ACTIVE,
                IndicationType.OTHER_MESSAGE_WAITING);
        byte expected = (byte)0xcb;
        
        assertEquals(dataCoding.toByte(), expected);
        
        DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
        assertEquals(buildedInstance, dataCoding);
        
        assertFalse(dataCoding.isStoreMessage());
    }
}
