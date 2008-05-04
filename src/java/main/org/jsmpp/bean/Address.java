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
}
