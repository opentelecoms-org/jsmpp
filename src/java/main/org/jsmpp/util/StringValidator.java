package org.jsmpp.util;

import org.jsmpp.PDUStringException;

/**
 * String validator for the SMPP PDU string types.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public final class StringValidator {
    private StringValidator() {
    }

    public static void validateString(String value, StringParameter param)
            throws PDUStringException {
        if (param.getType() == StringType.C_OCTEC_STRING) {
            if (param.isRangeMinAndMax()) {
                if (!isCOctetStringValid(value, param.getMax())) {
                    throw new PDUStringException("C-Octet String value '"
                            + value + "' cannot more than " + param.getMax()
                            + ". Actual length of string is " + value.length(),
                            param);
                }
            } else if (!isCOctetStringNullOrNValValid(value, param.getMax())) {
                throw new PDUStringException(
                        "C-Octet String value should be 1 or " + param.getMax()
                                + ". The actual length of string is "
                                + value.length(), param);
            }
        } else if (param.getType() == StringType.OCTET_STRING
                && !isOctetStringValid(value, param.getMax())) {
            throw new PDUStringException("Octet String value '" + value
                    + "' cannot more than " + param.getMax()
                    + ". Actual length of string is " + value.length(), param);
        }
    }

    public static void validateString(byte[] value, StringParameter param)
            throws PDUStringException {
        if (param.getType() == StringType.C_OCTEC_STRING) {
            if (param.isRangeMinAndMax()) {
                if (!isCOctetStringValid(value, param.getMax())) {
                    throw new PDUStringException("C-Octet String value '"
                            + new String(value) + "' cannot more than "
                            + param.getMax() + ". Actual length of string is "
                            + value.length, param);
                }
            } else if (!isCOctetStringNullOrNValValid(value, param.getMax())) {
                throw new PDUStringException(
                        "C-Octet String value should be 1 or " + param.getMax()
                                + ". The actual length of string is "
                                + value.length, param);
            }
        } else if (param.getType() == StringType.OCTET_STRING
                && !isOctetStringValid(value, param.getMax())) {
            throw new PDUStringException("Octet String value '"
                    + new String(value) + "' cannot more than "
                    + param.getMax() + ". Actual length of string is "
                    + value.length, param);
        }
    }

    /**
     * Validate the C-Octet String.
     * 
     * @param value
     * @param maxLength
     * @return
     */
    private static boolean isCOctetStringValid(String value, int maxLength) {
        if (value == null)
            return true;
        if (value.length() >= maxLength)
            return false;
        return true;

    }

    private static boolean isCOctetStringValid(byte[] value, int maxLength) {
        if (value == null)
            return true;
        if (value.length >= maxLength)
            return false;
        return true;

    }

    /**
     * Validate the C-Octet String
     * 
     * @param value
     * @param length
     * @return
     */
    private static boolean isCOctetStringNullOrNValValid(String value,
            int length) {
        if (value == null)
            return true;
        if (value.length() == 0)
            return true;
        if (value.length() != length)
            return false;
        return true;
    }

    private static boolean isCOctetStringNullOrNValValid(byte[] value,
            int length) {
        if (value == null)
            return true;
        if (value.length == 0)
            return true;
        if (value.length != length)
            return false;
        return true;
    }

    /**
     * Validate the Octet String
     * 
     * @param value
     * @param maxLength
     * @return
     */
    private static boolean isOctetStringValid(String value, int maxLength) {
        if (value == null)
            return true;
        if (value.length() > maxLength)
            return false;
        return true;
    }

    private static boolean isOctetStringValid(byte[] value, int maxLength) {
        if (value == null)
            return true;
        if (value.length > maxLength)
            return false;
        return true;
    }
}
