package org.jsmpp.bean;

/**
 * @author uudashr
 *
 */
public enum MessageMode {
    DEFAULT((byte) 0x00), 
    DATAGRAM((byte)0x01), 
    TRANSACTION((byte)0x02), 
    STORE_AND_FORWARD((byte)0x03);
    
    public static final byte CLEAR_BYTE = (byte) 0xfc; // 11111100
    public static final byte MASK_BYTE = 0x03; // 00000011
    
    private final byte value;

    private MessageMode(byte value) {
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
    
    public static byte compose(byte esmClass, MessageMode messageMode) {
        return (byte)(clean(esmClass) | messageMode.value());
    }
    
    public static byte clean(byte esmClass) {
        return (byte) (esmClass & CLEAR_BYTE);
    }
}
