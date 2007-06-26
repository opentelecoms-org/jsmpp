package org.jsmpp.bean;

/**
 * @author uudashr
 * 
 */
public enum SMSCDeliveryReceipt {
    DEFAULT((byte)0x00), SUCCESS_FAILURE((byte)0x01), SUCCESS((byte)0x02);

    public static final byte CLEAR_BYTE = (byte)0xfc;
    public static final byte MASK_BYTE = 0x03;

    private final byte _value;

    private SMSCDeliveryReceipt(byte value) {
        _value = value;
    }

    public byte value() {
        return _value;
    }

    public static SMSCDeliveryReceipt valueOf(byte value) {
        for (SMSCDeliveryReceipt val : values()) {
            if (val._value == value)
                return val;
        }

        throw new IllegalArgumentException(
                "No enum const SMSCDeliveryReceipt with value " + value);
    }
}
