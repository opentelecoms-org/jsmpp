package org.jsmpp.bean;




/**
 * @author uudashr
 *
 */
public class Address {
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Address other = (Address)obj;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        if (numberingPlanIndicator == null) {
            if (other.numberingPlanIndicator != null)
                return false;
        } else if (!numberingPlanIndicator.equals(other.numberingPlanIndicator))
            return false;
        if (typeOfNumber == null) {
            if (other.typeOfNumber != null)
                return false;
        } else if (!typeOfNumber.equals(other.typeOfNumber))
            return false;
        return true;
    }
    
    
}
