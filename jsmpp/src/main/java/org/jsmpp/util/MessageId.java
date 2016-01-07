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

import org.jsmpp.PDUStringException;

/**
 * This class wraps a simple {@link String} value as a message_id. The main 
 * purpose is the creation of the value will be validated as proper message_id.
 *  
 * @author uudashr
 *
 */
public class MessageId {
    private final String value;
    
    /**
     * Construct <code>MessageId</code> with specified <code>value</code>.
     * 
     * @param value is the message_id as <code>String</code>.
     * @throws PDUStringException 
     */
    public MessageId(String value) throws PDUStringException {
        StringValidator.validateString(value, StringParameter.MESSAGE_ID);
        this.value = value;
    }
    
    /**
     * Get the message_id value.
     * 
     * @return the value of message_id.
     */
    public String getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((value == null) ? 0 : value.hashCode());
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
        final MessageId other = (MessageId)obj;
        if (value == null) {
            if (other.value != null)
                return false;
        } else if (!value.equals(other.value))
            return false;
        return true;
    }
    
    
}
