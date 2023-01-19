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
package org.jsmpp.examples.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import threegpp.charset.gsm.GSMCharset;

/**
 * Concatenation use 8-bit or 16-bit reference number.
 * <p>
 * Note: These functions are not supporting additional user data headers.
 */
public class Concatenation {

  private static final int MAX_SMS_OCTETS = 140;

  private static final int IE_CONCATENATED_SM_8_BIT_REFERENCE_SIZE = 6;
  private static final int IE_CONCATENATED_SM_16_BIT_REFERENCE_SIZE = 7;

  private static final Charset GSM = new GSMCharset();
  private static final Charset USC_2 = StandardCharsets.UTF_16BE;

  public static byte[][] splitGsm7bit(String message, Charset charset, int reference, boolean use16BitReference)
      throws IOException {
    return split(message, charset, GSM, true, reference, use16BitReference);
  }

  public static byte[][] splitGsm7bitWith8bitReference(String message, Charset charset, int reference)
      throws IOException {
    return split(message, charset, GSM, true, reference, false);
  }

  public static byte[][] splitGsm7bitWith16bitReference(String message, Charset charset, int reference)
      throws IOException {
    return split(message, charset, GSM, true, reference, true);
  }

  public static byte[][] splitUcs2(String message, int reference, boolean use16BitReference)
      throws IOException {
    return split(message, USC_2, USC_2, false, reference, use16BitReference);
  }

  public static byte[][] splitUcs28bit(String message, int reference)
      throws IOException {
    return split(message, USC_2, USC_2, false, reference, false);
  }

  public static byte[][] splitUcs216bit(String message, int reference)
      throws IOException {
    return split(message, USC_2, USC_2, false, reference, true);
  }

  /**
   * Split an Java string to concatenated segments using a charset. The splitting keeps the boundary in mind when the GSM 7-bit characters is packed into octets
   * by the SMSC. Each multibyte character is completely encoded into a single segment.
   *
   * @param message           The Java encoded string of characters.
   * @param charset           The charset, normally ISO Latin 1 unpacked as default SMSC alphabet or UCS2.
   * @param encode7Bit        If the SMS is encoded with 7-bit on the network, which is the case for basic set characters.
   * @param reference         The reference number, only the lower 8 or 16 bits are used.
   * @param use16bitReference If true use 16-bit reference numbers otherwise use 8-bit reference numbers.
   * @return The splitted message bytes, encoded in charset provided.
   */
  private static byte[][] split(String message, Charset charset, Charset charset2, boolean encode7Bit, int reference, boolean use16bitReference)
      throws IOException {
    int headerOctetsSize = use16bitReference ? IE_CONCATENATED_SM_16_BIT_REFERENCE_SIZE : IE_CONCATENATED_SM_8_BIT_REFERENCE_SIZE;
    int availableOctets = MAX_SMS_OCTETS - headerOctetsSize;
    int availableChars = encode7Bit ? (availableOctets * 8) / 7 : availableOctets;
    List<byte[]> byteMessagesArray = new ArrayList<>();

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    // The result is up to the maximum length, there could be one or more characters from the extension set.
    for (char c : message.toCharArray()) {
      byte[] charBytes = String.valueOf(c).getBytes(charset);
      int charLength = String.valueOf(c).getBytes(charset2).length;
      // A character represented by an escape-sequence shall not be split in the middle.
      System.out.println("size: " + baos.size() + " char:" + c + " " + charLength + " availableChars:" + availableChars);
      if (baos.size() + charLength <= availableChars) {
        baos.write(charBytes);
      } else {
        byteMessagesArray.add(baos.toByteArray());
        baos = new ByteArrayOutputStream();
        baos.write(charBytes);
      }
    }
    if (baos.size() != 0) {
      byteMessagesArray.add(baos.toByteArray());
    }
    if (baos.size() > 255) {
      throw new IllegalArgumentException("Too many (>255) segments for concatenation");
    }
    return use16bitReference ?
        concatenate16bit(byteMessagesArray.toArray(new byte[][]{}), reference) :
        concatenate8bit(byteMessagesArray.toArray(new byte[][]{}), reference);
  }

  private static byte[][] concatenate8bit(byte[][] messageParts, final int reference) {
    byte[][] segments = new byte[messageParts.length][];

    final byte UDHIE_HEADER_LENGTH = 0x05;
    final byte UDHIE_IDENTIFIER_SAR = 0x00;
    final byte UDHIE_SAR_LENGTH = 0x03;

    // use only last 8 bits of integer
    byte[] referenceNumber = new byte[]{ (byte) reference };

    // split the message adding required headers
    for (int i = 0; i < messageParts.length; i++) {

      byte[] data = messageParts[i];

      // new array to store the header
      byte[] userData = new byte[IE_CONCATENATED_SM_8_BIT_REFERENCE_SIZE + data.length];

      // UDH header
      // doesn't include itself, its header length
      userData[0] = UDHIE_HEADER_LENGTH;
      // SAR identifier
      userData[1] = UDHIE_IDENTIFIER_SAR;
      // SAR length
      userData[2] = UDHIE_SAR_LENGTH;
      // reference number (same for all messages)
      userData[3] = referenceNumber[0];
      // total number of segments
      userData[4] = (byte) messageParts.length;
      // segment number
      userData[5] = (byte) (i + 1);

      // copy the data into the array
      System.arraycopy(data, 0, userData, IE_CONCATENATED_SM_8_BIT_REFERENCE_SIZE, data.length);

      segments[i] = userData;
    }
    return segments;
  }

  private static byte[][] concatenate16bit(byte[][] messageParts, final int reference) {
    byte[][] segments = new byte[messageParts.length][];

    final byte UDHIE_HEADER_LENGTH = 0x06;
    final byte UDHIE_IDENTIFIER_SAR = 0x08;
    final byte UDHIE_SAR_LENGTH = 0x04;

    // use only last 8 bits of integer
    byte[] referenceNumber = new byte[]{ (byte) reference };

    // split the message adding required headers
    for (int i = 0; i < messageParts.length; i++) {

      byte[] data = messageParts[i];

      // new array to store the header
      byte[] userData = new byte[IE_CONCATENATED_SM_16_BIT_REFERENCE_SIZE + data.length];

      // UDH header
      // doesn't include itself, its header length
      userData[0] = UDHIE_HEADER_LENGTH;
      // SAR identifier
      userData[1] = UDHIE_IDENTIFIER_SAR;
      // SAR length
      userData[2] = UDHIE_SAR_LENGTH;
      // reference number (same for all messages)
      userData[3] = referenceNumber[0];
      // total number of segments
      userData[4] = (byte) messageParts.length;
      // segment number
      userData[5] = (byte) (i + 1);

      // copy the data into the array
      System.arraycopy(data, 0, userData, IE_CONCATENATED_SM_16_BIT_REFERENCE_SIZE, data.length);

      segments[i] = userData;
    }
    return segments;
  }

}
