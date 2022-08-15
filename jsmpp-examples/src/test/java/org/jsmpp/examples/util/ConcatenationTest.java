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

import static org.junit.Assert.assertEquals;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.jsmpp.util.HexUtil;
import org.junit.Assert;
import org.junit.Test;

public class ConcatenationTest {

  private Charset ISO_LATIN_1 = StandardCharsets.ISO_8859_1;
  private Charset USC_2 = StandardCharsets.UTF_16BE;

  private String message = "Lorem ipsum dolor sit amet enim. Etiam ullamcorper. " +
      "Suspendisse a pellentesque dui, non felis. Maecenas malesuada elit lectus felis, malesuada ultricies." +
      "Curabitur et ligula. Ut molestie a, ultricies porta urna.";

  @Test
  public void test_split_8_bit_iso_latin_1_characters_without_data() throws Exception {
    byte[][] segments = Concatenation.splitGsm7bitWith8bitReference("", ISO_LATIN_1, 1234);
    assertEquals(0, segments.length);
  }

  @Test
  public void test_split_8_bit_usc_2_characters_without_data() throws Exception {
    byte[][] segments = Concatenation.splitUcs28bit("", 1234);
    assertEquals(0, segments.length);
  }

  @Test
  public void test_split_8_bit_iso_latin_1_characters() throws Exception {
    byte[][] segments = Concatenation.splitGsm7bitWith8bitReference(message, ISO_LATIN_1, 1234);

    Assert.assertEquals(2, segments.length);
    Assert.assertEquals(159, segments[0].length);
    Assert.assertEquals(63, segments[1].length);
    Assert.assertEquals(
        "050003d202014c6f72656d20697073756d20646f6c6f722073697420616d657420656e696d2e20457469616d20756c6c616d636f727065722e2053757370656e646973736520612070656c6c656e746573717565206475692c206e6f6e2066656c69732e204d616563656e6173206d616c65737561646120656c6974206c65637475732066656c69732c206d616c65737561646120756c747269636965732e",
        HexUtil.convertBytesToHexString(segments[0]));
    Assert.assertEquals("050003d20202437572616269747572206574206c6967756c612e205574206d6f6c657374696520612c20756c7472696369657320706f7274612075726e612e",
        HexUtil.convertBytesToHexString(segments[1]));
    Assert.assertEquals(
        "Lorem ipsum dolor sit amet enim. Etiam ullamcorper. Suspendisse a pellentesque dui, non felis. Maecenas malesuada elit lectus felis, malesuada ultricies.",
        new String(subarray(segments[0], 6), ISO_LATIN_1));
    Assert.assertEquals("Curabitur et ligula. Ut molestie a, ultricies porta urna.",
        new String(subarray(segments[1], 6), ISO_LATIN_1));
  }

  @Test
  public void test_split_16_bit_iso_latin_1_characters() throws Exception {
    byte[][] segments = Concatenation.splitGsm7bitWith16bitReference(message, ISO_LATIN_1, 1234);

    Assert.assertEquals(2, segments.length);
    Assert.assertEquals(159, segments[0].length);
    Assert.assertEquals(65, segments[1].length);
    Assert.assertEquals(
        "060804d20201004c6f72656d20697073756d20646f6c6f722073697420616d657420656e696d2e20457469616d20756c6c616d636f727065722e2053757370656e646973736520612070656c6c656e746573717565206475692c206e6f6e2066656c69732e204d616563656e6173206d616c65737561646120656c6974206c65637475732066656c69732c206d616c65737561646120756c74726963696573",
        HexUtil.convertBytesToHexString(segments[0]));
    Assert.assertEquals("060804d20202002e437572616269747572206574206c6967756c612e205574206d6f6c657374696520612c20756c7472696369657320706f7274612075726e612e",
        HexUtil.convertBytesToHexString(segments[1]));
    Assert.assertEquals(
        "Lorem ipsum dolor sit amet enim. Etiam ullamcorper. Suspendisse a pellentesque dui, non felis. Maecenas malesuada elit lectus felis, malesuada ultricies",
        new String(subarray(segments[0], 7), ISO_LATIN_1));
    Assert.assertEquals(".Curabitur et ligula. Ut molestie a, ultricies porta urna.",
        new String(subarray(segments[1], 7), ISO_LATIN_1));
  }

  @Test
  public void test_split_8_bit_ucs2_characters() throws Exception {
    byte[][] segments = Concatenation.splitUcs28bit(message, 1234);

    Assert.assertEquals(4, segments.length);
    Assert.assertEquals(140, segments[0].length);
    Assert.assertEquals(140, segments[1].length);
    Assert.assertEquals(140, segments[2].length);
    Assert.assertEquals(24, segments[3].length);
    Assert.assertEquals(
        "050003d20401004c006f00720065006d00200069007000730075006d00200064006f006c006f0072002000730069007400200061006d0065007400200065006e0069006d002e00200045007400690061006d00200075006c006c0061006d0063006f0072007000650072002e002000530075007300700065006e006400690073007300650020006100200070",
        HexUtil.convertBytesToHexString(segments[0]));
    Assert.assertEquals(
        "050003d204020065006c006c0065006e0074006500730071007500650020006400750069002c0020006e006f006e002000660065006c00690073002e0020004d0061006500630065006e006100730020006d0061006c00650073007500610064006100200065006c006900740020006c00650063007400750073002000660065006c00690073002c0020006d",
        HexUtil.convertBytesToHexString(segments[1]));
    Assert.assertEquals(
        "050003d204030061006c00650073007500610064006100200075006c0074007200690063006900650073002e0043007500720061006200690074007500720020006500740020006c006900670075006c0061002e0020005500740020006d006f006c0065007300740069006500200061002c00200075006c007400720069006300690065007300200070006f",
        HexUtil.convertBytesToHexString(segments[2]));
    Assert.assertEquals("050003d20404007200740061002000750072006e0061002e",
        HexUtil.convertBytesToHexString(segments[3]));
    Assert.assertEquals(
        "Lorem ipsum dolor sit amet enim. Etiam ullamcorper. Suspendisse a p",
        new String(subarray(segments[0], 6), USC_2));
    Assert.assertEquals("ellentesque dui, non felis. Maecenas malesuada elit lectus felis, m",
        new String(subarray(segments[1], 6), USC_2));
    Assert.assertEquals("alesuada ultricies.Curabitur et ligula. Ut molestie a, ultricies po",
        new String(subarray(segments[2], 6), USC_2));
    Assert.assertEquals("rta urna.",
        new String(subarray(segments[3], 6), USC_2));
  }

  @Test
  public void test_split_16_bit_ucs2_characters() throws Exception {
    byte[][] segments = Concatenation.splitUcs216bit(message, 1234);

    Assert.assertEquals(4, segments.length);
    Assert.assertEquals(139, segments[0].length);
    Assert.assertEquals(139, segments[1].length);
    Assert.assertEquals(139, segments[2].length);
    Assert.assertEquals(31, segments[3].length);
    Assert.assertEquals(
        "060804d2040100004c006f00720065006d00200069007000730075006d00200064006f006c006f0072002000730069007400200061006d0065007400200065006e0069006d002e00200045007400690061006d00200075006c006c0061006d0063006f0072007000650072002e002000530075007300700065006e00640069007300730065002000610020",
        HexUtil.convertBytesToHexString(segments[0]));
    Assert.assertEquals(
        "060804d204020000700065006c006c0065006e0074006500730071007500650020006400750069002c0020006e006f006e002000660065006c00690073002e0020004d0061006500630065006e006100730020006d0061006c00650073007500610064006100200065006c006900740020006c00650063007400750073002000660065006c00690073002c",
        HexUtil.convertBytesToHexString(segments[1]));
    Assert.assertEquals(
        "060804d20403000020006d0061006c00650073007500610064006100200075006c0074007200690063006900650073002e0043007500720061006200690074007500720020006500740020006c006900670075006c0061002e0020005500740020006d006f006c0065007300740069006500200061002c00200075006c0074007200690063006900650073",
        HexUtil.convertBytesToHexString(segments[2]));
    Assert.assertEquals("060804d204040000200070006f007200740061002000750072006e0061002e",
        HexUtil.convertBytesToHexString(segments[3]));
    Assert.assertEquals(
        "Lorem ipsum dolor sit amet enim. Etiam ullamcorper. Suspendisse a ",
        new String(subarray(segments[0], 7), USC_2));
    Assert.assertEquals("pellentesque dui, non felis. Maecenas malesuada elit lectus felis,",
        new String(subarray(segments[1], 7), USC_2));
    Assert.assertEquals(" malesuada ultricies.Curabitur et ligula. Ut molestie a, ultricies",
        new String(subarray(segments[2], 7), USC_2));
    Assert.assertEquals(" porta urna.",
        new String(subarray(segments[3], 7), USC_2));
  }

  @Test
  public void test_split_gsm_0338_basic_characters_with_extension_char_on_split_in_the_middle() throws Exception {
    String message = "This is a message which fits in 154 septets but the last character is 2 septets. This would fit into one packed message of 153 septets or 134 octets....€";

    byte[][] segments = Concatenation.splitGsm7bitWith8bitReference(message, ISO_LATIN_1, 1234);

    assertEquals(2, segments.length);
    assertEquals(
        "050003d20201546869732069732061206d657373616765207768696368206669747320696e2031353420736570746574732062757420746865206c61737420636861726163746572206973203220736570746574732e205468697320776f756c642066697420696e746f206f6e65207061636b6564206d657373616765206f66203135332073657074657473206f7220313334206f63746574732e2e2e2e",
        HexUtil.convertBytesToHexString(segments[0]));
    assertEquals(
        "050003d202023f",
        HexUtil.convertBytesToHexString(segments[1]));
    assertEquals(158, segments[0].length);
    assertEquals(7, segments[1].length);

  }

  private byte[] subarray(byte[] data, int offset) {
    byte[] b = new byte[data.length - offset];
    System.arraycopy(data, offset, b, 0, b.length);
    return b;
  }

}