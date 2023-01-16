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
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.NumberingPlanIndicator;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.RegisteredDelivery;
import org.jsmpp.bean.TypeOfNumber;

/**
 * @author uudashr
 *
 */
public class DeliverSmCommandTask extends AbstractSendCommandTask {
    public static final String COMMAND_NAME_DELIVER_SM = "deliver_sm";
    private final String serviceType;
    private final TypeOfNumber sourceAddrTon;
    private final NumberingPlanIndicator sourceAddrNpi;
    private final String sourceAddr;
    private final TypeOfNumber destAddrTon;
    private final NumberingPlanIndicator destAddrNpi;
    private final String destinationAddr;
    private final ESMClass esmClass;
    private final byte protocolId;
    private final byte priorityFlag;
    private final RegisteredDelivery registeredDelivery;
    private final DataCoding dataCoding;
    private final byte[] shortMessage;
    private final OptionalParameter[] optionalParameters;
    
    public DeliverSmCommandTask(PDUSender pduSender,
            String serviceType, TypeOfNumber sourceAddrTon,
            NumberingPlanIndicator sourceAddrNpi, String sourceAddr,
            TypeOfNumber destAddrTon, NumberingPlanIndicator destAddrNpi,
            String destinationAddr, ESMClass esmClass, byte protocolId,
            byte priorityFlag, RegisteredDelivery registeredDelivery,
            DataCoding dataCoding, byte[] shortMessage,
            OptionalParameter[] optionalParameters) {
        
        super(pduSender);
        this.serviceType = serviceType;
        this.sourceAddrTon = sourceAddrTon;
        this.sourceAddrNpi = sourceAddrNpi;
        this.sourceAddr = sourceAddr;
        this.destAddrTon = destAddrTon;
        this.destAddrNpi = destAddrNpi;
        this.destinationAddr = destinationAddr;
        this.esmClass = esmClass;
        this.protocolId = protocolId;
        this.priorityFlag = priorityFlag;
        this.registeredDelivery = registeredDelivery;
        this.dataCoding = dataCoding;
        this.shortMessage = shortMessage;
        this.optionalParameters = optionalParameters;
    }
    
    public void executeTask(OutputStream out, int sequenceNumber)
            throws PDUStringException, IOException {
        
        pduSender.sendDeliverSm(out, sequenceNumber, serviceType,
                sourceAddrTon, sourceAddrNpi, sourceAddr, destAddrTon,
                destAddrNpi, destinationAddr, esmClass, protocolId,
                priorityFlag, registeredDelivery, dataCoding, shortMessage,
                optionalParameters);
    }
    
    public String getCommandName() {
        return COMMAND_NAME_DELIVER_SM;
    }
}
