package org.jsmpp.util;

import java.util.Random;

/**
 * Generate random alhanumeric
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public class RandomMessageIDGenerator implements MessageIDGenerator {
    private Random random = new Random();
    private final int length;

    /**
     * Length should be not more than 64 character.
     * 
     * @param length
     */
    public RandomMessageIDGenerator(int length) {
        if (length > 64)
            this.length = 64;
        else
            this.length = length;
    }

    /**
     * Default constructor using 20 length character.
     */
    public RandomMessageIDGenerator() {
        this(20);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.MessageIDGenerator#newMessageId()
     */
    public String newMessageId() {
        /*
         * use database sequence convert into hex representation or if not using
         * database using random
         */
        byte[] b = new byte[length / 2];
        random.nextBytes(b);
        return HexUtil.conventBytesToHexString(b);
    }
}