package org.jsmpp.bean;

/**
 * @author uudashr
 * 
 */
public class ReplaceIfPresentFlag {
    public static final ReplaceIfPresentFlag DEFAULT = new ReplaceIfPresentFlag(
            0);
    public static final ReplaceIfPresentFlag DONT_REPLACE = DEFAULT;
    public static final ReplaceIfPresentFlag REPLACE = new ReplaceIfPresentFlag(
            1);

    private byte _value;

    public ReplaceIfPresentFlag(int value) {
        _value = (byte)value;
    }

    public ReplaceIfPresentFlag(byte value) {
        _value = value;
    }

    public byte value() {
        return _value;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof ReplaceIfPresentFlag))
            return false;
        ReplaceIfPresentFlag other = (ReplaceIfPresentFlag)obj;
        return _value == other._value;
    }
}
