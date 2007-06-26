package org.jsmpp.bean;

/**
 * @author uudashr
 * 
 */
public enum MessageType {
    DEFAULT((byte) 0x00), 
    SMSC_DEL_RECEIPT((byte) 0x04);

    public static final byte CLEAR_BYTE = (byte) 0xc3;
    public static final byte MASK_BYTE = 0x3c;

    private final byte _value;

    private MessageType(byte value) {
        _value = value;
    }

    public byte value() {
        return _value;
    }

    public static MessageType valueOf(byte value) {
        for (MessageType val : values()) {
            if (val.value() == value)
                return val;
        }

        throw new IllegalArgumentException(
                "No enum const MessageType with value " + value);
    }
}
