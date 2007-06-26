package org.jsmpp.bean;

/**
 * @author uudashr
 * 
 */
public enum MessageClass {
    CLASS0((byte) 0x00), 
    CLASS1((byte) 0x01), 
    CLASS2((byte) 0x02), 
    CLASS3((byte) 0x03);

    public static final byte MASK_MESSAGE_CLASS = 0x03; // bin: 00000011

    private final byte _value;

    private MessageClass(byte value) {
        _value = value;
    }

    public byte value() {
        return _value;
    }

    public static MessageClass valueOf(byte value) {
        for (MessageClass val : values()) {
            if (val._value == value)
                return val;
        }
        throw new IllegalArgumentException(
                "No enum const MessageClass with value " + value);
    }
}
