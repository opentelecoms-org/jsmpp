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

import java.util.Arrays;

/**
 * @author uudashr
 *
 */
public class SubmitMultiResult {
    private String messageId;
    private UnsuccessDelivery[] unsuccessDeliveries;
    
    // TODO uudashr: make sure message_id and destination_addr has valid length
    public SubmitMultiResult(String messageId,
            UnsuccessDelivery... unsuccessDeliveries) {
        this.messageId = messageId;
        this.unsuccessDeliveries = unsuccessDeliveries;
    }

    public String getMessageId() {
        return messageId;
    }

    public UnsuccessDelivery[] getUnsuccessDeliveries() {
        return unsuccessDeliveries;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((messageId == null) ? 0 : messageId.hashCode());
        result = prime * result + Arrays.hashCode(unsuccessDeliveries);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SubmitMultiResult other = (SubmitMultiResult)obj;
        if (messageId == null) {
            if (other.messageId != null)
                return false;
        } else if (!messageId.equals(other.messageId))
            return false;
        if (!Arrays.equals(unsuccessDeliveries, other.unsuccessDeliveries))
            return false;
        return true;
    }
    
    
}
