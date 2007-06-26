package org.jsmpp;



/**
 * @author uudashr
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
	
	private byte _value;
	private TypeOfNumber(byte value) {
		_value = value;
	}
	
	public byte value() {
		return _value;
	}
	
	public static TypeOfNumber valueOf(byte value) {
		for (TypeOfNumber val : values()) {
			if (val._value == value)
				return val;
		}
		
		throw new IllegalArgumentException(
	            "No enum const TypeOfNumber with value " + value);
	}
}
