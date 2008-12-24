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
 * Raw data coding is intended for reserved coding groups.
 * 
 * @author uudashr
 * 
 */
public class RawDataCoding implements DataCoding {
    private final byte value;
    
    /**
     * Construct with specified value from PDU.
     * 
     * @param value is the byte value from PDU.
     */
    public RawDataCoding(byte value) {
        this.value = value;
    }
    
    public byte toByte() {
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + value;
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
        RawDataCoding other = (RawDataCoding)obj;
        if (value != other.value)
            return false;
        return true;
    }
    
    @Override
    public String toString() {
        return "DataCoding:" + (0xff & toByte());
    }
}
