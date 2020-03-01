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
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final Address address1 = (Address) o;
        return typeOfNumber == address1.typeOfNumber &&
            numberingPlanIndicator == address1.numberingPlanIndicator &&
            Objects.equals(address, address1.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(typeOfNumber, numberingPlanIndicator, address);
    }

}
