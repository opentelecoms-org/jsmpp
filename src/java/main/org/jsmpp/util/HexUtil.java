package org.jsmpp.util;

import java.util.Arrays;

/**
 * @author uudashr
 * @version 1.00, 11/04/05
 * 
 */
public class HexUtil {

    private static final char[] hexChar = { '0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    /**
     * @param data is case sensitive.
     * @return
     */
    public static String convertStringToHexString(String data) {
        return conventBytesToHexString(data.getBytes());
    }

    public static String conventBytesToHexString(byte[] data) {
        return convertBytesToHexString(data, 0, data.length);
    }

    // Big-Endian
    public static String convertBytesToHexString(byte[] data, int offset,
            int length) {
        StringBuffer sBuf = new StringBuffer();
        // System.err.println("converBytesToHexString");
        for (int i = offset; i < length; i++) {
            /*
             * System.err.println("data[" + i + "] = " + data[i]);
             * System.err.println(hexChar[(data[i] >> 4) & 0xf]);
             * System.err.println(hexChar[data[i] & 0xf]);
             */
            sBuf.append(hexChar[(data[i] >> 4) & 0xf]);
            sBuf.append(hexChar[data[i] & 0xf]);
            /*
             * sBuf.append(hexChar[(data[i] >> 4) & 0xf]);
             * sBuf.append(hexChar[data[i]& 0xf]);
             */
            // sBuf.append(Integer.toHexString(data[i]));
        }
        return sBuf.toString();
    }

    public static String convertHexStringToString(String hexString) {
        String uHexString = hexString.toLowerCase();
        StringBuffer sBuf = new StringBuffer();
        for (int i = 0; i < uHexString.length(); i = i + 2) {
            char c = (char)Integer.parseInt(uHexString.substring(i, i + 2), 16);
            sBuf.append(c);
        }
        return sBuf.toString();
    }

    public static byte[] convertHexStringToBytes(String hexString) {
        return convertHexStringToBytes(hexString, 0, hexString.length());
    }

    public static byte[] convertHexStringToBytes(String hexString, int offset,
            int endIndex) {
        byte[] data;
        String realHexString = hexString.substring(offset, endIndex)
                .toLowerCase();
        if ((realHexString.length() % 2) == 0)
            data = new byte[realHexString.length() / 2];
        else
            data = new byte[(int)Math.ceil(realHexString.length() / 2d)];

        int j = 0;
        char[] tmp;
        for (int i = 0; i < realHexString.length(); i += 2) {
            try {
                tmp = realHexString.substring(i, i + 2).toCharArray();
            } catch (StringIndexOutOfBoundsException siob) {
                // it only contains one character, so add "0" string
                tmp = (realHexString.substring(i) + "0").toCharArray();
            }
            data[j] = (byte)((Arrays.binarySearch(hexChar, tmp[0]) & 0xf) << 4);
            data[j++] |= (byte)(Arrays.binarySearch(hexChar, tmp[1]) & 0xf);
        }

        for (int i = realHexString.length(); i > 0; i -= 2) {

        }
        return data;
    }
}
