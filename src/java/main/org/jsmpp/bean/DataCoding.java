package org.jsmpp.bean;

/**
 * This is class of <tt>DataCoding</tt>
 * 
 * @author uudashr
 * @version 1.0
 * 
 */
public abstract class DataCoding {

    /**
     * Create new instance of data coding with specified value.
     * 
     * @param value is the value.
     * @return the <tt>DataCoding</tt> based on specified value. 
     */
    public static final DataCoding newInstance(int value) {
        byte byteValue = (byte) value;
        if (GeneralDataCoding.isCompatible(byteValue)) {
            return new GeneralDataCoding(byteValue);
        } else if (DataCoding1111.isCompatible(byteValue)) {
            return new DataCoding1111(byteValue);
        } else {
            return null;
        }
    }

    /**
     * Create new instance of data coding with specified value.
     * 
     * @param value is the value.
     * @return <tt>DataCoding</tt> based on specified value.
     */
    public static final DataCoding newInstance(byte value) {
        if (GeneralDataCoding.isCompatible(value)) {
            return new GeneralDataCoding(value);
        } else if (DataCoding1111.isCompatible(value)) {
            return new DataCoding1111(value);
        } else {
            return null;
        }
    }

    protected byte value;

    /**
     * Default constructor.
     */
    public DataCoding() {
        value = 0;
    }

    /**
     * Construct with specified value.
     * 
     * @param value is the data coding value.
     */
    public DataCoding(int value) {
        this.value = (byte) value;
    }

    /**
     * Construct with specified value.
     * 
     * @param value is the data coding value.
     */
    public DataCoding(byte value) {
        this.value = value;
    }

    /**
     * Get the value of data coding.
     * 
     * @return the data coding value.
     */
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
        final DataCoding other = (DataCoding)obj;
        if (value != other.value)
            return false;
        return true;
    }
}
