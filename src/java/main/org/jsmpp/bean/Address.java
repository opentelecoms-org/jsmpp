package org.jsmpp.bean;

/**
 * @author uudashr
 *
 */
public class Address {
    private byte typeOfNumber;
    private byte numberingPlanIndicator;
    private String value;
    
    public Address() {
    }

    public byte getTypeOfNumber() {
        return typeOfNumber;
    }

    public void setTypeOfNumber(byte typeOfNumber) {
        this.typeOfNumber = typeOfNumber;
    }

    public byte getNumberingPlanIndicator() {
        return numberingPlanIndicator;
    }

    public void setNumberingPlanIndicator(byte numberingPlanIndicator) {
        this.numberingPlanIndicator = numberingPlanIndicator;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
