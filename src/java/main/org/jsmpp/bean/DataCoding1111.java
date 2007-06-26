package org.jsmpp.bean;

/**
 * Data Coding with coding group bits 7..4 -> 1111
 * 
 * @author uudashr
 * @version 1.0
 * 
 */
public class DataCoding1111 extends DataCoding {
    private static final byte MASK_CODING_GROUP = (byte)0xf0; // 11110000
    private static final byte MASK_MESSAGE_CLASS = MessageClass.MASK_MESSAGE_CLASS;
    private static final byte MASK_ALPHABET = Alphabet.MASK_ALPHABET;

    /**
     * Default constructor.
     */
    public DataCoding1111() {
        super();
    }

    /**
     * Construct with specified value.
     * 
     * @param value is the value.
     */
    public DataCoding1111(byte value) {
        super(value);
    }

    /**
     * Construct with specified value.
     * 
     * @param value is the value.
     */
    public DataCoding1111(int value) {
        super(value);
    }

    /**
     * Get the message class of current data coding.
     * 
     * @return the {@link MessageClass}.
     */
    public MessageClass getMessageClass() {
        return MessageClass.valueOf((byte)(value & MASK_MESSAGE_CLASS));
    }

    /**
     * Compose data coding with specified message class.
     * 
     * @param messageClass is the message class.
     * @return the <tt>GeneralDataCoding</tt>.
     */
    public GeneralDataCoding composeMessageClass(MessageClass messageClass) {
        byte tmp = cleanMessageClass(value);
        return new GeneralDataCoding(tmp | messageClass.value());
    }

    /**
     * Clean the message class bytes.
     * 
     * @param value is the source of message class to clean.
     * @return the value without message class bytes.
     */
    private static final byte cleanMessageClass(byte value) {
        return (byte)(value & (MASK_MESSAGE_CLASS ^ 0xff));
    }

    /**
     * Get the alphanet value.
     * 
     * @return the alphabet value.
     */
    public Alphabet getAlphabet() {
        return Alphabet.valueOf((byte)(value & MASK_ALPHABET));
    }

    /**
     * Compose data coding with specified alphabet.
     * 
     * @param alpha is the {@link Alphabet}
     * @return the {@link GeneralDataCoding} that contains specified
     *         {@link Alphabet}
     */
    public GeneralDataCoding composeAlphabet(Alphabet alpha) {
        byte tmp = cleanAlphabet(value);
        return new GeneralDataCoding(tmp | alpha.value());
    }

    /**
     * Clean the alphabet bytes.
     * 
     * @param value is the value of data coding to clean.
     * @return the clean data coding.
     */
    private static final byte cleanAlphabet(byte value) {
        return (byte)(value & (MASK_ALPHABET ^ 0xff));
    }

    public static boolean isCompatible(byte dataCodingValue) {
        return (dataCodingValue & MASK_CODING_GROUP) == MASK_CODING_GROUP;
    }
}
