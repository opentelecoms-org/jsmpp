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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LongSMS {
    private static final Logger log = LoggerFactory.getLogger(LongSMS.class);
    
    private final static int MAX_MESSAGE_SEGMENT_8BIT = 133; // 140-7
    private final static byte UDHIE_IDENTIFIER_SAR = 0x08;
    private final static byte UDHIE_SAR_LENGTH = 0x04;
    private static int referenceNumber = 0;

    private LongSMS() {
        throw new InstantiationError("This class must not be instantiated");
    }

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
            log.debug("i = {}", i);
            if (segmentNum - i == 1)
                lengthOfData = messageLength - i * MAX_MESSAGE_SEGMENT_8BIT;
            else
                lengthOfData = MAX_MESSAGE_SEGMENT_8BIT;
            log.debug("Length of data = {}", lengthOfData);

            segments[i] = new byte[7 + lengthOfData];
            log.debug("segments[{}].length = {}", i, segments[i].length);

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
}
