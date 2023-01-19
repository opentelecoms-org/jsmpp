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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.jsmpp.util.HexUtil;
import org.junit.Test;

import threegpp.charset.gsm.GSM7BitPackedCharset;
import threegpp.charset.gsm.GSMCharset;

/**
 * Some examples to encode GSM 7-bit, GSM 7-bit packed, Latin-1 and UCS-2 strings.
 * <p>
 * See https://github.com/brake/telecom-charsets
 */
public class CharsetsTest {

  private final Charset GSM_CHARSET = new GSMCharset();
  private final Charset GSM_PACKED_CHARSET = new GSM7BitPackedCharset();
  private final Charset ISO_LATIN_1_CHARSET = StandardCharsets.ISO_8859_1;
  private final Charset UCS_2_CHARSET = StandardCharsets.UTF_16BE;

  @Test
  public void test_gsm_encoding() throws Exception {
    assertArrayEquals(HexUtil.convertHexStringToBytes("00"), "@".getBytes(GSM_CHARSET));
    assertArrayEquals(HexUtil.convertHexStringToBytes("1b65"), "€".getBytes(GSM_CHARSET));
    assertArrayEquals(HexUtil.convertHexStringToBytes("006162631b65"), "@abc€".getBytes(GSM_CHARSET));
  }

  @Test
  public void test_gsm_packed_encoding() throws Exception {
    assertArrayEquals(HexUtil.convertHexStringToBytes("00"), "@".getBytes(GSM_PACKED_CHARSET));
    assertArrayEquals(HexUtil.convertHexStringToBytes("9b32"), "€".getBytes(GSM_PACKED_CHARSET));
    assertArrayEquals(HexUtil.convertHexStringToBytes("80b078bc2903"), "@abc€".getBytes(GSM_PACKED_CHARSET));
  }

  @Test
  public void test_gsm_packed_encoding_without_cr_padding() throws Exception {
    System.out.println(HexUtil.convertBytesToHexString("abcdefg".getBytes(GSM_PACKED_CHARSET)));
    assertArrayEquals(HexUtil.convertHexStringToBytes("61f1985c369f01"), "abcdefg".getBytes(GSM_PACKED_CHARSET));
    assertArrayEquals(HexUtil.convertHexStringToBytes("61f1985c369f01"), "abcdefg@".getBytes(GSM_PACKED_CHARSET));
  }

  @Test
  public void test_iso_latin_1_encoding() throws Exception {
    assertArrayEquals(HexUtil.convertHexStringToBytes("40"), "@".getBytes(ISO_LATIN_1_CHARSET));
    assertArrayEquals(HexUtil.convertHexStringToBytes("3f"), "€".getBytes(ISO_LATIN_1_CHARSET));
    assertArrayEquals(HexUtil.convertHexStringToBytes("406162633f"), "@abc€".getBytes(ISO_LATIN_1_CHARSET));
  }

  @Test
  public void test_ucs2_encoding() throws Exception {
    assertArrayEquals(HexUtil.convertHexStringToBytes("0040"), "@".getBytes(UCS_2_CHARSET));
    assertArrayEquals(HexUtil.convertHexStringToBytes("20ac"), "€".getBytes(UCS_2_CHARSET));
    assertArrayEquals(HexUtil.convertHexStringToBytes("004000610062006320ac"), "@abc€".getBytes(UCS_2_CHARSET));
  }

  @Test
  public void test_gsm_encode_decode() throws Exception {
    assertEncodeDecode("", GSM_CHARSET);
    assertEncodeDecode("@", GSM_CHARSET);
    assertEncodeDecode("@£$", GSM_CHARSET);
    assertEncodeDecode("abcdef@", GSM_CHARSET);
    assertEncodeDecode("abcdefg@", GSM_CHARSET);
    assertEncodeDecode("@£$¥èéùìòÇ\nØø\rÅåΔ_ΦΓΛΩΠΨΣΘΞÆæßÉ !\"#¤%&'()*+,-."
        + "/0123456789:;<=>?¡ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÑÜ§¿abcdefghijklmnopqrstuvwxyzäöñüà", GSM_CHARSET);
    assertEncodeDecode("^{}[~]|€", GSM_CHARSET);
  }

  @Test
  public void test_gsm_packed_encode_decode() throws Exception {
    assertEncodeDecode("", GSM_PACKED_CHARSET);
    assertEncodeDecode("@", GSM_PACKED_CHARSET);
    assertEncodeDecode("abcdefg", GSM_PACKED_CHARSET);
    assertEncodeDecode("€€€€€€€€", GSM_PACKED_CHARSET);
    assertEncodeDecode("@£$¥èéùìòÇ\nØø\rÅåΔ_ΦΓΛΩΠΨΣΘΞÆæßÉ !\"#¤%&'()*+,-."
        + "/0123456789:;<=>?¡ABCDEFGHIJKLMNOPQRSTUVWXYZÄÖÑÜ§¿abcdefghijklmnopqrstuvwxyzäöñüà", GSM_PACKED_CHARSET);
    assertEncodeDecode("^{}[~]|€", GSM_PACKED_CHARSET);
  }

  private void assertEncodeDecode(String s, Charset charset) {
    assertEquals(s, encodeDecode(s, charset));
  }

  private String encodeDecode(String s, Charset charset) {
    return new String(s.getBytes(charset), charset);
  }
}
