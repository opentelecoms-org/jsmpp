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

    private byte value;

    public ReplaceIfPresentFlag(int value) {
        this.value = (byte)value;
    }

    public ReplaceIfPresentFlag(byte value) {
        this.value = value;
    }

    public byte value() {
        return value;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final ReplaceIfPresentFlag other = (ReplaceIfPresentFlag)obj;
        if (value != other.value)
            return false;
        return true;
    }
}
