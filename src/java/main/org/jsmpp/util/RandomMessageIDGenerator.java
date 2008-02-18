package org.jsmpp.util;

import java.util.Random;

import org.jsmpp.PDUStringException;

/**
 * Generate random alphanumeric
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public class RandomMessageIDGenerator implements MessageIDGenerator {
    private Random random = new Random();

    /* (non-Javadoc)
     * @see org.jsmpp.util.MessageIDGenerator#newMessageId()
     */
    public MessageId newMessageId() {
        /*
         * use database sequence convert into hex representation or if not using
         * database using random
         */
        try {
            return new MessageId(Integer.toString(random.nextInt(Integer.MAX_VALUE), 16));
        } catch (PDUStringException e) {
            throw new RuntimeException("Failed creating message id", e);
        }
    }
}