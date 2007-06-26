package org.jsmpp.bean;

/**
 * @author uudashr
 * 
 */
public class RegisteredDelivery {
    private final byte _value;

    public RegisteredDelivery() {
        _value = 0;
    }

    public RegisteredDelivery(int value) {
        _value = (byte)value;
    }

    public RegisteredDelivery(byte value) {
        _value = value;
    }

    public byte value() {
        return _value;
    }

    public SMSCDeliveryReceipt getSMSCDeliveryReceipt() {
        return SMSCDeliveryReceipt
                .valueOf((byte)(_value & SMSCDeliveryReceipt.MASK_BYTE));
    }

    public RegisteredDelivery composeSMSCDelReceipt(
            SMSCDeliveryReceipt smscDeliveryReceipt) {
        return new RegisteredDelivery(cleanSMSCDeliveryReceipt(_value)
                | smscDeliveryReceipt.value());
    }

    private static byte cleanSMSCDeliveryReceipt(byte value) {
        return (byte)(value & SMSCDeliveryReceipt.CLEAR_BYTE);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RegisteredDelivery))
            return false;
        RegisteredDelivery other = (RegisteredDelivery)obj;
        return _value == other._value;
    }
}
