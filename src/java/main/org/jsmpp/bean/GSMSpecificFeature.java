package org.jsmpp.bean;

/**
 * @author uudashr
 * 
 */
public enum GSMSpecificFeature {

    // TODO uudashr: complete the specific feature
    DEFAULT((byte) 0x00), 
    /**
     * User data header indicator.
     */
    UDHI((byte) 0xc0);

    public static final byte CLEAR_BYTE = 0x3f;
    public static final byte MASK_BYTE = (byte) 0xc0;

    private final byte value;

    private GSMSpecificFeature(byte value) {
        this.value = value;
    }

    public byte value() {
        return value;
    }
    
    public boolean containedIn(ESMClass esmClass) {
        return containedIn(esmClass.value());
    }
    
    public boolean containedIn(byte esmClass) {
        return this.value == (byte)(esmClass & MASK_BYTE);
    }
    
    public static byte compose(byte esmClass, GSMSpecificFeature specificFeature) {
        return (byte)(clean(esmClass) | specificFeature.value());
    }
    
    public static byte clean(byte esmClass) {
        return (byte) (esmClass & CLEAR_BYTE);
    }
}
