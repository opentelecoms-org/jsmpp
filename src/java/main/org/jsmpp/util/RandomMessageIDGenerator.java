package org.jsmpp.util;

import java.util.Random;

/**
 * Generate random alhanumeric
 * 
 * @author uudashr
 * 
 */
public class RandomMessageIDGenerator implements MessageIDGenerator {
	private Random _random = new Random();
	private final int _length;

	/**
	 * Length should be not more than 64 character.
	 * 
	 * @param length
	 */
	public RandomMessageIDGenerator(int length) {
		if (length > 64)
			_length = 64;
		else
			_length = length;
	}

	/**
	 * Default constructor using 20 length character.
	 */
	public RandomMessageIDGenerator() {
		this(20);
	}

	/* (non-Javadoc)
	 * @see org.jsmpp.util.MessageIDGenerator#newMessageId()
	 */
	public String newMessageId() {
		/*
		 * use database sequence convert into hex representation or if not using
		 * database using random
		 */
		byte[] b = new byte[_length / 2];
		_random.nextBytes(b);
		return HexUtil.conventBytesToHexString(b);
	}
}