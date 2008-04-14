package org.jsmpp.bean;

/**
 * The enumerated state for short message.
 * 
 * @author uudashr
 * 
 */
public enum MessageState {
    /**
     * The message is in enroute state.
     */
    ENROUTE((byte) 0x01),
    /**
     * Message is delivered to destination.
     */
    DELIVERED((byte) 0x02),
    /**
     * Message validity period has expired.
     */
    EXPIRED((byte) 0x03),
    /**
     * Message has been deleted.
     */
    DELETED((byte) 0x04),
    /**
     * Message is undeliverable.
     */
    UNDELIVERABLE((byte) 0x05),
    /**
     * Message is in accepted state.
     */
    ACCEPTED((byte) 0x06),
    /**
     * Message is in invalid state.
     */
    UNKNOWN((byte) 0x07),
    /**
     * Message is in rejected state.
     */
    REJECTED((byte) 0x08);

    private final byte value;

    private MessageState(byte value) {
        this.value = value;
    }

    public byte value() {
        return value;
    }

    public static MessageState valueOf(byte value) {
        for (MessageState val : values()) {
            if (val.value() == value)
                return val;
        }

        throw new IllegalArgumentException(
                "No enum const MessageStatte with value " + value);
    }
}
