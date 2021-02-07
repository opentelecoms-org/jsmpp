/*
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.jsmpp.bean;

import org.jsmpp.SMPPConstant;


/**
 * This enum is defined Numbering Plan Indicator.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
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

	NumberingPlanIndicator(byte value) {
		this.value = value;
	}
	
	/**
	 * Return the value of Number Plan Indicator.
	 *
	 * @return the value of NPI.
	 */
	public byte value() {
		return value;
	}
	
	/**
	 * Get the associated {@code NumberingPlanIndicator} by it's value.
	 *
	 * @param value is the value.
	 * @return the associated enum const for specified value.
	 * @throws IllegalArgumentException if there is no enum const associated
	 *      with specified {@code value}.
	 */
	public static NumberingPlanIndicator valueOf(byte value)
            throws IllegalArgumentException {
		for (NumberingPlanIndicator val : values()) {
			if (val.value == value) {
				return val;
			}
		}
		
		throw new IllegalArgumentException(
	            "No enum const NumberingPlanIndicator with value " + value);
	}
}
