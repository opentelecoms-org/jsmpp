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
package org.jsmpp.composing;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;

import org.jsmpp.DefaultPDUReader;
import org.jsmpp.DefaultPDUSender;
import org.jsmpp.PDUSender;
import org.jsmpp.SMPPConstant;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.GenericNack;
import org.jsmpp.util.DefaultDecomposer;
import org.jsmpp.util.PDUDecomposer;
import org.testng.annotations.Test;

/**
 * Simulating sending and receive data from client to server (vice versa) by
 * simple compose and decomposing the PDU.
 * 
 * @author uudashr
 * 
 */
public class ComposeDecompose {
    private PDUSender pduSender = new DefaultPDUSender();
    private DefaultPDUReader pduReader = new DefaultPDUReader();
    private PDUDecomposer decomposer = new DefaultDecomposer();
    
    /**
     * Test the composed PDU can decomposed properly.
     * 
     * @throws Exception if an unexpected error found.
     */
    @Test(groups="checkintest")
    public void sendGenericNack() throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        
        pduSender.sendGenericNack(out, SMPPConstant.STAT_ESME_RINVBNDSTS, 10);
        
        
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(out.toByteArray()));
        Command header = pduReader.readPDUHeader(in);
        assertEquals(header.getCommandId(), SMPPConstant.CID_GENERIC_NACK, "Unexpected command_id");
        
        byte[] pdu = pduReader.readPDU(in, header);
        GenericNack nack = decomposer.genericNack(pdu);
        assertNotNull(nack);
    }
    
}
