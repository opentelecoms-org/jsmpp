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

/**
 * @author uudashr
 * 
 */
public class RegisteredDelivery {
    private byte value;

    public RegisteredDelivery() {
        value = 0;
    }

    public RegisteredDelivery(int value) {
        this.value = (byte)value;
    }

    public RegisteredDelivery(byte value) {
        this.value = value;
    }
    
    public RegisteredDelivery(SMSCDeliveryReceipt smscDeliveryReceipt) {
        this();
        setSMSCDeliveryReceipt(smscDeliveryReceipt);
    }
    
    public RegisteredDelivery(SMEOriginatedAcknowledgement smeOriginatedAcknowledgement) {
        this();
        setSMEOriginatedAcknowledgement(smeOriginatedAcknowledgement);
    }
    
    public byte value() {
        return value;
    }

    public RegisteredDelivery setSMSCDeliveryReceipt(
            SMSCDeliveryReceipt smscDeliveryReceipt) {
        value = SMSCDeliveryReceipt.compose(value, smscDeliveryReceipt);
        return this;
    }
    
    public RegisteredDelivery setSMEOriginatedAcknowledgement(SMEOriginatedAcknowledgement smeOriginatedAcknowledgement) {
        value = SMEOriginatedAcknowledgement.compose(value, smeOriginatedAcknowledgement);
        return this;
    }
    
    public RegisteredDelivery setIntermediateNotification(IntermediateNotification intermediateNotification) {
        value = IntermediateNotification.compose(value, intermediateNotification);
        return this;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final RegisteredDelivery other = (RegisteredDelivery)obj;
        if (value != other.value)
            return false;
        return true;
    }
    
    
}
