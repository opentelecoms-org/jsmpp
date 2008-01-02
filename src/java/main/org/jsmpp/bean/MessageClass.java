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

    private final byte value;

    private MessageClass(byte value) {
        this.value = value;
    }

    public byte value() {
        return value;
    }

    public static MessageClass valueOf(byte value) {
        for (MessageClass val : values()) {
            if (val.value == value)
                return val;
        }
        throw new IllegalArgumentException(
                "No enum const MessageClass with value " + value);
    }
}
