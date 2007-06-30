package org.jsmpp;

/**
 * Type of Number based on SMPP version 3.4.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 *
 */
public enum TypeOfNumber {
	UNKNOWN(SMPPConstant.TON_UNKNOWN), 
	INTERNATIONAL(SMPPConstant.TON_INTERNATIONAL), 
	NATIONAL(SMPPConstant.TON_NATIONAL), 
	NETWORK_SPECIFIC(SMPPConstant.TON_NETWORK_SPECIFIC), 
	SUBSCRIBER_NUMBER(SMPPConstant.TON_SUBSCRIBER_NUMBER), 
	ALPHANUMERIC(SMPPConstant.TON_ALPHANUMERIC), 
	ABBREVIATED(SMPPConstant.TON_ABBREVIATED);
	
	private byte value;
    
	private TypeOfNumber(byte value) {
		this.value = value;
	}
	
	/**
     * Get the byte value of the enum constant.
     * 
	 * @return the byte value.
	 */
	public byte value() {
		return value;
	}
	
	/**
     * Get the <tt>TypeOfNumber</tt> based on the specified byte value
     * representation.
     * 
     * @param value is the byte value representation.
     * @return is the enum const related to the specified byte value.
     * @throws IllegalArgumentException if there is no enum const associated
     *         with specified byte value.
     */
	public static TypeOfNumber valueOf(byte value) {
		for (TypeOfNumber val : values()) {
			if (val.value == value)
				return val;
		}
		
		throw new IllegalArgumentException(
	            "No enum const TypeOfNumber with value " + value);
	}
}
