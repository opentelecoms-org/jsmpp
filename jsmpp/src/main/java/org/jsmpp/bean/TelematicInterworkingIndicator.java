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

/**
 * Telematic Interworking Indicator.
 * <p/>
 * bit 5 indicates telematic interworking:
 * value = 0 : no interworking, but SME-to-SME protocol
 * value = 1 : telematic interworking
 *
 * @author <a href="mailto:kohme@gigsky.com">Karsten Ohme
 *         (kohme@gigsky.com)</a>
 */
public enum TelematicInterworkingIndicator {
    /**
     * No interworking, but SME-to-SME protocol
     */
    NO_INTERWORKING((byte) 0),
    /**
     * Telematic interworking
     */
    INTERWORKING((byte) 0b100000);

    /**
     * The byte encoding of the structure.
     */
    private byte encoding;

    private TelematicInterworkingIndicator(byte encoding) {
        this.encoding = encoding;
    }

    /**
     * Get the byte encoding of the structure.
     *
     * @return the byte encoding of the structure.
     */
    public byte value() { return encoding; }

    /**
     * Get the telematic interworking from the byte encoding.
     * You can pass the full first octet to this.
     *
     * @param encoding The encoding.
     * @return ehe telematic interworking.
     */
    public static TelematicInterworkingIndicator valueOf(byte encoding) {
        for (TelematicInterworkingIndicator sri : TelematicInterworkingIndicator.values()) {
            if (sri.encoding == (encoding & 0x00100000)) {
                return sri;
            }
        }
        throw new IllegalArgumentException("Could not recognize telematic interworking from encoding '" + encoding + "'.");
    }
}
