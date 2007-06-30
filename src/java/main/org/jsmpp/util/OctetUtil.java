package org.jsmpp.util;

/**
 * The octet util.
 * 
 * @author uudashr
 * @version 1.0
 * @version 1.0
 * 
 */
public class OctetUtil {
    /**
     * Convert integer (4 octets) value to bytes.
     * 
     * @param value as 4 bytes representing integer in bytes.
     * @return
     */
    public static byte[] intToBytes(int value) {
        byte[] result = new byte[4];
        result[0] = (byte)(value >> 24 & 0xff);
        result[1] = (byte)(value >> 16 & 0xff);
        result[2] = (byte)(value >> 8 & 0xff);
        result[3] = (byte)(value & 0xff);
        return result;
    }

    /**
     * Convert integer (2 octets) value to bytes.
     * 
     * @param value
     * @return
     */
    public static byte[] shortToBytes(short value) {
        byte[] result = new byte[2];
        result[0] = (byte)(value >> 8 & 0xff);
        result[1] = (byte)(value & 0xff);
        return result;
    }

    /**
     * 32 bit.
     * 
     * @param bytes
     * @return
     */
    public static int bytesToInt(byte[] bytes) {
        return bytesToInt(bytes, 0);
    }

    /**
     * 32 bit.
     * 
     * @param bytes
     * @param offset
     * @return
     */
    public static int bytesToInt(byte[] bytes, int offset) {
        // 
        int result = 0x00000000;

        int length = 0;
        if (bytes.length - offset < 4) // maximum byte size for int data type
                                        // is 4
            length = bytes.length - offset;
        else
            length = 4;

        int end = offset + length;
        for (int i = 0; i < length; i++) {
            // result |= bytes[end - i - 1] << (8 * i);
            result |= (bytes[end - i - 1] & 0xff) << (8 * i); // TODO uud:
                                                                // CHECK FOR
                                                                // IMPROVEMENT
        }
        return result;
    }

    /**
     * 16 bit.
     * 
     * @param bytes
     * @return
     */
    public static short bytesToShort(byte[] bytes) {
        return bytesToShort(bytes, 0);
    }

    /**
     * 16 bit.
     * 
     * @param bytes
     * @param offset
     * @return
     */
    public static short bytesToShort(byte[] bytes, int offset) {
        short result = 0x0000;
        int end = offset + 2;
        for (int i = 0; i < 2; i++) {
            result |= bytes[end - i - 1] << (8 * i);
        }
        return result;
    }
}
