package org.jsmpp.bean;

/**
 * @author uudashr
 * 
 */
public enum GSMSpecificFeature {

    // TODO uud: complete the specific feature
    DEFAULT((byte) 0x00), 
    /**
     * User data header indicator.
     */
    UDHI((byte) 0xc0);

    public static final byte CLEAR_BYTE = 0x3f;
    public static final byte MASK_BYTE = (byte) 0xc0;

    private final byte _value;

    private GSMSpecificFeature(byte value) {
        _value = value;
    }

    public byte value() {
        return _value;
    }

    public static GSMSpecificFeature valueOf(byte value) {
        for (GSMSpecificFeature val : values()) {
            if (val._value == value)
                return val;
        }

        throw new IllegalArgumentException(
                "No enum const GSMSpecificFeature with value " + value);
    }
}
