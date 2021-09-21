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


import java.util.Objects;

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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UnsuccessDelivery)) {
            return false;
        }
        final UnsuccessDelivery that = (UnsuccessDelivery) o;
        return errorStatusCode == that.errorStatusCode && Objects.equals(destinationAddress, that.destinationAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(destinationAddress, errorStatusCode);
    }
}
