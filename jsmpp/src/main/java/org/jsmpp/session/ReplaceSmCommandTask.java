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
package org.jsmpp.session;

import java.io.IOException;
import java.io.OutputStream;

import org.jsmpp.PDUSender;
import org.jsmpp.PDUStringException;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.TypeOfNumber;

public class ReplaceSmCommandTask extends AbstractSendCommandTask {
    public static final String COMMAND_NAME_REPLACE_SM = "replace_sm";
    private String messageId;
    private TypeOfNumber sourceAddrTon;
    private NumberingPlanIndicator sourceAddrNpi;
    private String sourceAddr;
    private String scheduleDeliveryTime;
    private String validityPeriod;
    private RegisteredDelivery registeredDelivery;
    private byte smDefaultMsgId;
    private byte[] shortMessage;
    
    public ReplaceSmCommandTask(PDUSender pduSender, String messageId,
            TypeOfNumber sourceAddrTon, NumberingPlanIndicator sourceAddrNpi,
            String sourceAddr, String scheduleDeliveryTime,
            String validityPeriod, RegisteredDelivery registeredDelivery,
            byte smDefaultMsgId, byte[] shortMessage) {
        super(pduSender);
        this.messageId = messageId;
        this.sourceAddrTon = sourceAddrTon;
        this.sourceAddrNpi = sourceAddrNpi;
        this.sourceAddr = sourceAddr;
        this.scheduleDeliveryTime = scheduleDeliveryTime;
        this.validityPeriod = validityPeriod;
        this.registeredDelivery = registeredDelivery;
        this.smDefaultMsgId = smDefaultMsgId;
        this.shortMessage = shortMessage;
    }
    
    public void executeTask(OutputStream out, int sequenceNumber)
            throws PDUStringException, IOException {
        pduSender.sendReplaceSm(out, sequenceNumber, messageId, sourceAddrTon,
                sourceAddrNpi, sourceAddr, scheduleDeliveryTime,
                validityPeriod, registeredDelivery, smDefaultMsgId,
                shortMessage);
    }
    
    public String getCommandName() {
        return COMMAND_NAME_REPLACE_SM;
    }
}
