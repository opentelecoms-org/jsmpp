package org.jsmpp.util;


/**
 * @author uudashr
 *
 */
public interface MessageIDGenerator {
	
	/**
	 * Generate message-id, max 65 C-Octet String.
	 * @return
	 */
	public String newMessageId();
}
