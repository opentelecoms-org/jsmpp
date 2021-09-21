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
package org.jsmpp.util;

/**
 * The octet util.
 * 
 * @author uudashr
 * @version 1.0
 */
public class OctetUtil {

    private OctetUtil() {
        throw new InstantiationError("This class must not be instantiated");
    }

    /**
     * Convert integer value to bytes (4 octets).
     * 
     * @param value as 4 bytes representing integer in bytes.
     * @return bytes in big endian format
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
     * Convert short value to bytes (2 octets) .
     * 
     * @param value as 2 bytes representing short in bytes.
     * @return bytes in big endian format
     */
    public static byte[] shortToBytes(short value) {
        byte[] result = new byte[2];
        result[0] = (byte)(value >> 8 & 0xff);
        result[1] = (byte)(value & 0xff);
        return result;
    }

    /**
     * Construct an int from 32 bit.
     * 
     * @param bytes in big endian format
     * @return integer
     */
    public static int bytesToInt(byte[] bytes) {
        return bytesToInt(bytes, 0);
    }

    /**
     * Construct an int from 32 bit (4 octets).
     * 
     * @param bytes in big endian format
     * @param offset the offset in bytes
     * @return integer
     */
    public static int bytesToInt(byte[] bytes, int offset) {
        int result = 0;
        // maximum byte size for int data type is 4
        int length = Math.min(bytes.length - offset, 4);
        int end = offset + length - 1;
        for (int i = 0; i < length; i++) {
            // TODO uudashr: CHECK FOR IMPROVEMENT
            result |= (bytes[end - i] & 0xff) << (8 * i);
        }
        return result;
    }

    /**
     * Construct a short from 16 bit (2 octets).
     * 
     * @param bytes in big endian format
     * @return short
     */
    public static short bytesToShort(byte[] bytes) {
        return bytesToShort(bytes, 0);
    }

    /**
     * Construct a short from 16 bit (2 octets).
     * 
     * @param bytes in big endian format
     * @param offset the offset in bytes
     * @return short
     */
    public static short bytesToShort(byte[] bytes, int offset) {
        short result = 0;
        int end = offset + 1;
        for (int i = 0; i < 2; i++) {
            result |= (bytes[end - i] & 0xff) << (8 * i);
        }
        return result;
    }
}
