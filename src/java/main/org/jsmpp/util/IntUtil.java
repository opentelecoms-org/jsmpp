package org.jsmpp.util;

/**
 * @author uudashr
 * @version 1.00, 11/04/05
 * 
 */
public class IntUtil {

    public static String to4DigitString(int value) {
        return toNDigitString(value, 4);
    }

    public static String to2DigitString(int value) {
        return toNDigitString(value, 2);
    }

    public static String toNDigitString(int value, int digitLength) {
        StringBuffer sBuf = new StringBuffer(String.valueOf(value));
        while (sBuf.length() < digitLength)
            sBuf.insert(0, "0");
        return sBuf.toString();
    }

    public static final String toHexString(int value) {
        return HexUtil.conventBytesToHexString(OctetUtil.intToBytes(value));
    }
}
