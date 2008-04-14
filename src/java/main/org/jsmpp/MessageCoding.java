package org.jsmpp;

/**
 * This is enum const that specified the message coding (see smpp spesification).
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 *
 */
public enum MessageCoding {
	/**
	 * Coding 7-bit.
	 */
	CODING_7_BIT,
    
    /**
     * Coding 8-bit.
     */
    CODING_8_BIT,
    
    /**
     * Coding 16-bit.
     */
    CODING_16_BIT;
}
