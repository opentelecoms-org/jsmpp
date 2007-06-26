package org.jsmpp.bean;

/**
 * @author uudashr
 * 
 */
public class ESMClass {
    private final byte _value;

    public ESMClass() {
        _value = 0;
    }

    public ESMClass(int value) {
        _value = (byte) value;
    }

    public ESMClass(byte value) {
        _value = value;
    }

    public byte value() {
        return _value;
    }

    public GSMSpecificFeature getSpecificFeature() {
        return GSMSpecificFeature
                .valueOf((byte) (_value & GSMSpecificFeature.MASK_BYTE));
    }

    public ESMClass composeSpecificFeature(GSMSpecificFeature specificFeature) {
        return new ESMClass(cleanSpecificFeature(_value)
                | specificFeature.value());
    }

    private static byte cleanSpecificFeature(byte value) {
        return (byte) (value & GSMSpecificFeature.CLEAR_BYTE);
    }

    public MessageType getMessageType() {
        return MessageType.valueOf((byte) (_value & MessageType.MASK_BYTE));
    }

    public ESMClass composeMessageType(MessageType messageType) {
        return new ESMClass(cleanMessageType(_value) | messageType.value());
    }

    private static byte cleanMessageType(byte value) {
        return (byte) (value & MessageType.CLEAR_BYTE);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ESMClass))
            return false;
        ESMClass other = (ESMClass) obj;
        return _value == other._value;
    }
}
