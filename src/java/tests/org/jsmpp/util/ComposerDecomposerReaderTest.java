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

import static org.testng.Assert.*;

import org.jsmpp.PDUStringException;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.BindResp;
import org.jsmpp.bean.BindType;
import org.testng.annotations.Test;

/**
 * Test composing PDU, decompose and read.
 * @author uudashr
 *
 */
public class ComposerDecomposerReaderTest {
    private static final boolean DEBUG = false;
    
    @Test(groups="checkintest")
    public void lowLevel() {
        byte[] b = null;
        String systemId = "smsc";
        
        PDUByteBuffer buf = new PDUByteBuffer(BindType.BIND_TRX.responseCommandId(), 0, 1);
        buf.append(systemId);
        b = buf.toBytes();
        assertEquals(b.length, 16 + systemId.length() + 1);
        printLog("Length of bytes : " + b.length);
        
        
        SequentialBytesReader reader = new SequentialBytesReader(b);
        assertEquals(b.length, reader.remainBytesLength());
        
        int commandLength =  reader.readInt();
        assertEquals(commandLength, b.length);
        assertEquals(4, reader.cursor);
        assertEquals(b.length - 4, reader.remainBytesLength());
        
        int commandId =  reader.readInt();
        assertEquals(commandId, BindType.BIND_TRX.responseCommandId());
        assertEquals(8, reader.cursor);
        assertEquals(b.length - 8, reader.remainBytesLength());
        
        int commandStatus = reader.readInt();
        assertEquals(commandStatus, SMPPConstant.STAT_ESME_ROK);
        assertEquals(12, reader.cursor);
        assertEquals(b.length - 12, reader.remainBytesLength());
        
        int sequenceNumber = reader.readInt();
        assertEquals(sequenceNumber, 1);
        assertEquals(16, reader.cursor);
        assertEquals(b.length - 16, reader.remainBytesLength());
        
        String readedSystemId = reader.readCString();
        assertEquals(readedSystemId, systemId);
    }
    
    @Test(groups="checkintest")
    public void highLevel() {
        PDUComposer composer = new DefaultComposer();
        PDUDecomposer decomposer = new DefaultDecomposer();
        
        byte[] b = null;
        String systemId = "smsc";
        BindType bindType = BindType.BIND_TRX;
        try {
            b = composer.bindResp(bindType.responseCommandId(), 1, systemId);
            assertEquals(b.length, 16 + systemId.length() + 1);
            printLog("Length of bytes : " + b.length);
        } catch (PDUStringException e) {
            fail("Failed composing bind response", e);
        }
        
        try {
            BindResp resp = decomposer.bindResp(b);
            assertEquals(resp.getCommandLength(), b.length);
            assertEquals(resp.getCommandId(), bindType.responseCommandId());
            assertEquals(resp.getCommandStatus(), SMPPConstant.STAT_ESME_ROK);
            assertEquals(resp.getSequenceNumber(), 1);
            assertEquals(resp.getSystemId(), systemId);
        } catch (PDUStringException e) {
            fail("Failed decomposing bind response", e);
        }
    }
    
    private static void printLog(String message) {
        if (DEBUG) {
            System.out.println(message);
        }
    }
}
