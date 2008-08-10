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
public class UnsuccessDelivery {
    private Address destinationAddress;
    private int errorStatusCode;
    
    public UnsuccessDelivery(byte destAddrTon, byte destAddrNpi, String destAddress, int errorStatusCode) {
        this(new Address(destAddrTon, destAddrNpi, destAddress), errorStatusCode);
    }
    
    public UnsuccessDelivery(Address destinationAddress, int errorStatusCode) {
        this.destinationAddress = destinationAddress;
        this.errorStatusCode = errorStatusCode;
    }

    public Address getDestinationAddress() {
        return destinationAddress;
    }

    public int getErrorStatusCode() {
        return errorStatusCode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((destinationAddress == null) ? 0 : destinationAddress
                        .hashCode());
        result = prime * result + errorStatusCode;
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
        final UnsuccessDelivery other = (UnsuccessDelivery)obj;
        if (destinationAddress == null) {
            if (other.destinationAddress != null)
                return false;
        } else if (!destinationAddress.equals(other.destinationAddress))
            return false;
        if (errorStatusCode != other.errorStatusCode)
            return false;
        return true;
    }
    
    
}
