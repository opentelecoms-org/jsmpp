package org.jsmpp.bean;

/**
 * This is enum of the alphabet type.
 * 
 * @author uudashr
 * @version 1.0
 * 
 */
public enum Alphabet {

    /**
     * SMSC alphabet default
     */
    ALPHA_DEFAULT((byte)0x00),

    /**
     * The -bit aphabet coding.
     */
    ALPHA_8_BIT((byte)0x04),

    /**
     * UCS2 alphabet coding (16-bit)
     */
    ALPHA_UCS2((byte)0x08),

    /**
     * Unused.
     */
    ALPHA_RESERVED((byte)0x0c);

    /**
     * Is the MASK of alphabet (00001100).
     */
    public static final byte MASK_ALPHABET = 0x0c; // bin: 00001100

    private final byte value;

    /**
     * Default constructor.
     * 
     * @param value is the alphabet value.
     */
    private Alphabet(byte value) {
        this.value = value;
    }

    /**
     * Get the alphabet value.
     * 
     * @return the alphabet value.
     */
    public byte value() {
        return value;
    }

    /**
     * Get the enum constant associated with specified value.
     * 
     * @param value is the value associated with the <tt>Alphabet</tt> enum
     *        constant.
     * @return the associated enum constant.
     * @throws IllegalArgumentException if there is no associated enum constant
     *         for given value.
     */
    public static Alphabet valueOf(byte value) throws IllegalArgumentException {
        for (Alphabet val : values()) {
            if (val.value == value)
                return val;
        }
        throw new IllegalArgumentException("No enum const Alphabet with value "
                + value);
    }

}
