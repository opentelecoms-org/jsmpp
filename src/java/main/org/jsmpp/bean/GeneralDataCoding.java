package org.jsmpp.bean;

/**
 * General Data Coding with coding group bits 7..4 -> 00xx
 * 
 * @author uudashr
 * @version 1.0
 * 
 */
public class GeneralDataCoding extends DataCoding1111 {
    /**
     * 11000000
     */
    private static final byte MASK_CODING_GROUP = (byte) 0xc0;
    /**
     * bin: 00100000
     */
    private static final byte MASK_COMPRESSED = 0x20;
    /**
     * bin: 00010000
     */
    private static final byte MASK_CONTAIN_MESSAGE_CLASS = 0x10;

    public GeneralDataCoding() {
        super();
    }

    public GeneralDataCoding(byte value) {
        super(value);
    }

    public GeneralDataCoding(int value) {
        super(value);
    }

    public boolean isCompressed() {
        return (value & MASK_COMPRESSED) == MASK_COMPRESSED;
    }

    public GeneralDataCoding composeCompressed(boolean compressed) {
        if (compressed) {
            return new GeneralDataCoding(value | MASK_COMPRESSED);
        } else {
            return new GeneralDataCoding(value & (MASK_COMPRESSED ^ 0xff));
        }
    }

    /**
     * Indicate that the <tt>DataCoding</tt> have meaning message class
     * 
     * @return
     */
    public boolean isContainMessageClass() {
        return (value & MASK_CONTAIN_MESSAGE_CLASS) == MASK_CONTAIN_MESSAGE_CLASS;
    }

    public GeneralDataCoding composeContainMessageClass(
            boolean containsMessageClass) {
        if (containsMessageClass) {
            return new GeneralDataCoding(value | MASK_CONTAIN_MESSAGE_CLASS);
        } else {
            return new GeneralDataCoding(value
                    & (MASK_CONTAIN_MESSAGE_CLASS ^ 0xff));
        }
    }

    public static boolean isCompatible(byte dataCodingValue) {
        return (dataCodingValue & MASK_CODING_GROUP) == 0x00;
    }
}
