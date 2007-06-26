package org.jsmpp.util;

import java.util.Random;

/**
 * @author uudashr
 *
 */
public class RandomSessionIDGenerator implements SessionIDGenerator<String> {
	private Random _random = new Random();
	
	/**
	 * Generate new session id. Max 64 char.
	 */
	public String newSessionId() {
		byte[] b = new byte[32];
		_random.nextBytes(b);
		return HexUtil.conventBytesToHexString(b);
	}
}
