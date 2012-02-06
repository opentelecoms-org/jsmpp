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
 * The int util.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
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
