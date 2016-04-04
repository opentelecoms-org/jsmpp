package org.jsmpp.bean;

/**
 * @author uudashr
 *
 */
public enum IndicationType {
    VOICEMAIL_MESSAGE_WAITING((byte)0x00), 
    FAX_MESSAGE_WAITING((byte)0x01), 
    ELECTRONIC_MESSAGE_WAITING((byte)0x02), 
    OTHER_MESSAGE_WAITING((byte)0x03);
    
    /**
     * bin: 00000011
     */
    public static final byte MASK_INDICATION_TYPE = (byte)0x3;
    
    private final byte value;
    
    IndicationType(byte value) {
        this.value = value;
    }
    
    public byte value() {
        return value;
    }
    
    public static IndicationType valueOf(byte value) throws IllegalArgumentException {
        for (IndicationType val : values()) {
            if (val.value == value)
                return val;
        }
        throw new IllegalArgumentException("No enum const IndicationType with value "
                + value);
    }
    
    public static IndicationType parseDataCoding(byte dataCoding) throws IllegalArgumentException {
        byte value = (byte)(dataCoding & MASK_INDICATION_TYPE);
        for (IndicationType val : values()) {
            if (val.value == value)
                return val;
        }
        throw new IllegalArgumentException("No enum const IndicationType with value "
                + value + " for dataCoding " + dataCoding);
    }
}
