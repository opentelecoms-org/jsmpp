package org.jsmpp.bean;

/**
 * @author uudashr
 *
 */
public enum IndicationSense {
    INACTIVE((byte)0x00), 
    ACTIVE((byte)0x08);
    
    /**
     * bin: 00001000
     */
    public static final byte MASK_INDICATION_SENSE = 0x08;
    
    private final byte value;
    
    IndicationSense(byte value) {
        this.value = value;
    }
    
    public byte value() {
        return value;
    }
    
    public static IndicationSense valueOf(byte value) throws IllegalArgumentException {
        for (IndicationSense val : values()) {
            if (val.value == value)
                return val;
        }
        throw new IllegalArgumentException("No enum const IndicationSense with value "
                + value);
    }
    
    public static IndicationSense parseDataCoding(byte dataCoding) throws IllegalArgumentException {
        byte value = (byte)(dataCoding & MASK_INDICATION_SENSE);
        for (IndicationSense val : values()) {
            if (val.value == value)
                return val;
        }
        throw new IllegalArgumentException("No enum const IndicationSense with value "
                + value + " for dataCoding " + dataCoding);
    }
    
}
