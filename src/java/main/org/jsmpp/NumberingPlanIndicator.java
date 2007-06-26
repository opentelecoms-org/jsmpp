package org.jsmpp;


/**
 * This enum is defined Numbering Plan Indicator.
 * 
 * @author uudashr
 * @version 1.0
 *
 */
public enum NumberingPlanIndicator {
	UNKNOWN(SMPPConstant.NPI_UNKNOWN), 
	ISDN(SMPPConstant.NPI_ISDN), 
	DATA(SMPPConstant.NPI_DATA), 
	TELEX(SMPPConstant.NPI_TELEX), 
	LAND_MOBILE(SMPPConstant.NPI_LAND_MOBILE), 
	NATIONAL(SMPPConstant.NPI_NATIONAL), 
	PRIVATE(SMPPConstant.NPI_PRIVATE), 
	ERMES(SMPPConstant.NPI_ERMES), 
	INTERNET(SMPPConstant.NPI_INTERNET), 
	WAP(SMPPConstant.NPI_WAP);
	
	private byte value;
	private NumberingPlanIndicator(byte value) {
		this.value = value;
	}
	
	/**
     * Return the value of NPI.
	 * @return the value of NPI.
	 */
	public byte value() {
		return value;
	}
	
	/**
     * Get the associated <tt>NumberingPlanIndicator</tt> by it's value.
     * 
	 * @param value is the value.
	 * @return the associated enum const for specified value.
     * @throws IllegalArgumentException if there is no enum const associated 
     *      with specified <tt>value</tt>.
	 */
	public static NumberingPlanIndicator valueOf(byte value)
            throws IllegalArgumentException {
		for (NumberingPlanIndicator val : values()) {
			if (val.value == value)
				return val;
		}
		
		throw new IllegalArgumentException(
	            "No enum const NumberingPlanIndicator with value " + value);
	}
}
