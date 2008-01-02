package org.jsmpp.bean;

import org.jsmpp.NumberingPlanIndicator;
import org.jsmpp.TypeOfNumber;

/**
 * FIXME uud: do this class is useful?
 * @author uudashr
 *
 */
public class Address {
    private TypeOfNumber typeOfNumber;
    private NumberingPlanIndicator numberingPlanIndicator;
    private String value;
    
    public Address(TypeOfNumber typeOfNumber,
            NumberingPlanIndicator numberingPlanIndicator, String value) {
        this.typeOfNumber = typeOfNumber;
        this.numberingPlanIndicator = numberingPlanIndicator;
        this.value = value;
    }

    public TypeOfNumber getTypeOfNumber() {
        return typeOfNumber;
    }

    public NumberingPlanIndicator getNumberingPlanIndicator() {
        return numberingPlanIndicator;
    }

    public String getValue() {
        return value;
    }
    
}
