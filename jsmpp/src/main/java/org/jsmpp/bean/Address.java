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

import org.jsmpp.util.ObjectUtil;

/**
 * This are SME (Short Message Entity) Address.
 * 
 * @author uudashr
 *
 */
public class Address implements DestinationAddress {
    private TypeOfNumber typeOfNumber;
    private NumberingPlanIndicator numberingPlanIndicator;
    private String address;
    
    public Address(TypeOfNumber typeOfNumber,
            NumberingPlanIndicator numberingPlanIndicator, String address) {
        this.typeOfNumber = typeOfNumber;
        this.numberingPlanIndicator = numberingPlanIndicator;
        this.address = address;
    }
    
    public Address(byte typeOfNumber, byte numberingPlanIndicator, String address) {
        this(TypeOfNumber.valueOf(typeOfNumber), 
                NumberingPlanIndicator.valueOf(numberingPlanIndicator), 
                address);
    }

    @Override
    public Flag getFlag() {
        return Flag.SME_ADDRESS;
    }
    
    public TypeOfNumber getTypeOfNumber() {
        return typeOfNumber;
    }
    
    public byte getTon() {
        return typeOfNumber.value();
    }
    
    public NumberingPlanIndicator getNumberingPlanIndicator() {
        return numberingPlanIndicator;
    }
    
    public byte getNpi() {
        return numberingPlanIndicator.value();
    }

    public String getAddress() {
        return address;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        result = prime
                * result
                + ((numberingPlanIndicator == null) ? 0
                        : numberingPlanIndicator.hashCode());
        result = prime * result
                + ((typeOfNumber == null) ? 0 : typeOfNumber.hashCode());
        return result;
    }
    
    private boolean hasEqualAddress(Address other) {
        return ObjectUtil.equals(address, other.address);
    }
    
    private boolean hasEqualNumberingPlanIndicator(Address other) {
        return ObjectUtil.equals(numberingPlanIndicator, other.numberingPlanIndicator);
    }
    
    private boolean hasEqualTypeOfNumber(Address other) {
        return ObjectUtil.equals(typeOfNumber , other.typeOfNumber);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Address other = (Address)obj;
        if (!hasEqualAddress(other)) {
            return false;
        }
        if (!hasEqualNumberingPlanIndicator(other)) {
            return false;
        }
        if (!hasEqualTypeOfNumber(other)) {
            return false;
        }
        return true;
    }
    
    
}
