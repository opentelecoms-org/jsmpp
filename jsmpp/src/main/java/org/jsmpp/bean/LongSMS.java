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

import org.jsmpp.util.HexUtil;
import org.jsmpp.util.OctetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LongSMS {
    private static final Logger logger = LoggerFactory.getLogger(LongSMS.class);
    
    private final static int MAX_MESSAGE_7BIT = 160;
    private final static int MAX_MESSAGE_8BIT = 140;
    private final static int MAX_MESSAGE_SEGMENT_8BIT = 133; // 140-7
    private final static int MAX_MESSAGE_SEGMENT_7BIT = 152;
    private final static byte UDHIE_IDENTIFIER_SAR = 0x08;
    private final static byte UDHIE_SAR_LENGTH = 0x04;
    private static int referenceNumber = 0;

    private static synchronized int getReferenceNumber() {
        referenceNumber++;
        if (referenceNumber >= 65536)
            referenceNumber = 0;
        return referenceNumber;
    }

    private static byte[] copyShort2Bytes(int integer) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) ((integer >> 8) & 0x0000ff);
        bytes[1] = (byte) (integer & 0x000000ff);

        return bytes;
    }

    public static byte[][] splitMessage8Bit(byte[] aMessage) {
        // determine how many messages
        int segmentNum = aMessage.length / MAX_MESSAGE_SEGMENT_8BIT;
        int messageLength = aMessage.length;
        if (segmentNum > 255) {
            // this is too long, can't fit, so chop
            segmentNum = 255;
            messageLength = segmentNum * MAX_MESSAGE_SEGMENT_8BIT;
        }
        if ((messageLength % MAX_MESSAGE_SEGMENT_8BIT) > 0)
            segmentNum++;

        byte[][] segments = new byte[segmentNum][];

        int lengthOfData;
        byte[] referenceNumber = copyShort2Bytes(getReferenceNumber());
        for (int i = 0; i < segmentNum; i++) {
            logger.debug("i = " + i);
            if (segmentNum - i == 1)
                lengthOfData = messageLength - i * MAX_MESSAGE_SEGMENT_8BIT;
            else
                lengthOfData = MAX_MESSAGE_SEGMENT_8BIT;
            logger.debug("Length of data = " + lengthOfData);

            segments[i] = new byte[7 + lengthOfData];
            logger.debug("segments[" + i + "].length = "
                    + segments[i].length);

            segments[i][0] = 6; // doesn't include itself, is header length
            // SAR identifier
            segments[i][1] = UDHIE_IDENTIFIER_SAR;
            // SAR length
            segments[i][2] = UDHIE_SAR_LENGTH;
            // DATAGRAM REFERENCE NUMBER
            System.arraycopy(referenceNumber, 0, segments[i], 3, 2);
            // total number of segments
            segments[i][5] = (byte) segmentNum;
            // segment #
            segments[i][6] = (byte) (i + 1);
            // now copy the data
            System.arraycopy(aMessage, i * MAX_MESSAGE_SEGMENT_8BIT,
                    segments[i], 7, lengthOfData);
        }

        return segments;

    }

    private static byte[][] splitMessage7Bit(byte[] aMessage) {
        // determine how many messages
        int segmentNum = aMessage.length / MAX_MESSAGE_SEGMENT_7BIT;
        int messageLength = aMessage.length;
        if (segmentNum > 255) {
            // this is too long, can't fit, so chop
            segmentNum = 255;
            messageLength = segmentNum * MAX_MESSAGE_SEGMENT_7BIT;
        }
        if ((messageLength % MAX_MESSAGE_SEGMENT_7BIT) > 0)
            segmentNum++;

        byte[][] segments = new byte[segmentNum][];

        int lengthOfData;
        byte[] data7Bit;
        byte[] tempBytes = new byte[MAX_MESSAGE_SEGMENT_7BIT];
        byte[] referenceNumber = copyShort2Bytes(getReferenceNumber());
        for (int i = 0; i < segmentNum; i++) {
            if (segmentNum - i == 1)
                lengthOfData = messageLength - i * MAX_MESSAGE_SEGMENT_7BIT;
            else
                lengthOfData = MAX_MESSAGE_SEGMENT_7BIT;
            System.arraycopy(aMessage, i * MAX_MESSAGE_SEGMENT_7BIT, tempBytes,
                    0, lengthOfData);
            data7Bit = encode7Bit(new String(tempBytes, 0, lengthOfData));
            segments[i] = new byte[7 + data7Bit.length];

            segments[i][0] = 6; // doesn't include itself
            // SAR identifier
            segments[i][1] = UDHIE_IDENTIFIER_SAR;
            // SAR length
            segments[i][2] = UDHIE_SAR_LENGTH;
            // DATAGRAM REFERENCE NUMBER
            System.arraycopy(referenceNumber, 0, segments[i], 3, 2);
            // total number of segments
            segments[i][5] = (byte) segmentNum;
            // segment #
            segments[i][6] = (byte) (i + 1);
            // now copy the data
            System.arraycopy(data7Bit, 0, segments[i], 7, data7Bit.length);
        }

        return segments;
    }

    private static byte[] encode7Bit(String aString) {
        int i, j, power;
        int length = aString.length();
        char[] tempChars = new char[length + 1];
        byte[] tempBytes = new byte[length];

        aString.getChars(0, length, tempChars, 0);
        tempChars[length] = 0;

        for (i = 0, j = 0, power = 1; i < length; i++, j++, power++) {
            if (power == 8) {
                i++;
                if (i >= length)
                    break;
                power = 1;
            }
            tempBytes[j] = (byte) ((tempChars[i] & ((1 << (8 - power)) - 1)) | ((tempChars[i + 1] & ((1 << power) - 1)) << (8 - power)));
            tempChars[i + 1] = (char) (tempChars[i + 1] >> power);
        }

        byte[] bytes = new byte[j];
        System.arraycopy(tempBytes, 0, bytes, 0, j);

        return bytes;
    }

    private static byte[][] smsg(byte[] data) {
        return null;
    }

    public static void main(String[] args) {
        String hexMessage = "5465737420736D732067617465776179206C6F6E6720736D73732C205465737420736D732067617465776179206C6F6E6720736D73732C205465737420736D732067617465776179206C6F6E6720736D73732C205465737420736D732067617465776179206C6F6E6720736D73732C205465737420736D732067617465776179206C6F6E6720736D73732C205465737420736D732067617465776179206C6F6E6720736D73732C205465737420736D732067617465776179206C6F6E6720736D73732C205465737420736D732067617465776179206C6F6E6720736D73732C205465737420736D732067617465776179206C6F6E6720736D73732C205465737420736D732067617465776179206C6F6E6720736D73732020";
        String message = "Test sms gateway long smss, Test sms gateway long smss, Test sms gateway long smss, Test sms gateway long smss, Test sms gateway long smss, Test sms gateway long smss, Test sms gateway long smss, Test sms gateway long smss, Test sms gateway long smss, Test sms gatew";

        byte[][] splittedMsg = splitMessage8Bit(message.getBytes());
        for (int i = 0; i < splittedMsg.length; i++) {
            logger.debug("splittedMsg[i].length = " + splittedMsg[i].length);
            logger.debug(new String(splittedMsg[i]));
            logger.debug("sar_msg_refnum tag: "
                    + HexUtil.convertBytesToHexString(splittedMsg[i], 0, 2));
            logger.debug("sar_msg_refnum length: "
                    + OctetUtil.bytesToShort(splittedMsg[i], 2));
            logger.debug("sar_msg_refnum value: "
                    + OctetUtil.bytesToShort(splittedMsg[i], 4));
        }
    }
}
