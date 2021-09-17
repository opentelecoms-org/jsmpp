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

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * @author pmoerenhout
 */
public class GeneralDataCodingTest {

  @Test
  public void testGeneralAlphabetDefault() {
    DataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_DEFAULT);
    assertEquals(dataCoding.toByte(), (byte) 0x00);
  }

  @Test
  public void testGeneralAlphabetIA5() {
    DataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_IA5);
    assertEquals(dataCoding.toByte(), (byte) 0x01);
  }

  @Test
  public void testGeneralAlphabetLatin1() {
    DataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_LATIN1);
    assertEquals(dataCoding.toByte(), (byte) 0x03);
  }

  @Test
  public void testGeneralAlphabetBinary() {
    DataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_8_BIT);
    assertEquals(dataCoding.toByte(), (byte) 0x04);
  }

  @Test
  public void testGeneralAlphabetJis() {
    DataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_JIS);
    assertEquals(dataCoding.toByte(), (byte) 0x05);
  }

  @Test
  public void testGeneralAlphabetCyrillic() {
    DataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_CYRILLIC);
    assertEquals(dataCoding.toByte(), (byte) 0x06);
  }

  @Test
  public void testGeneralAlphabetLatinHebrew() {
    DataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_LATIN_HEBREW);
    assertEquals(dataCoding.toByte(), (byte) 0x07);
  }

  @Test
  public void testGeneralAlphabetUCS2() {
    DataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_UCS2);
    assertEquals(dataCoding.toByte(), (byte) 0x08);
  }

  @Test
  public void testGeneralAlphabetPictogramEncoding() {
    DataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_PICTOGRAM_ENCODING);
    assertEquals(dataCoding.toByte(), (byte) 0x09);
  }

  @Test
  public void testGeneralAlphabetIso2022JpMusicCodes() {
    DataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_ISO_2022_JP_MUSIC_CODES);
    assertEquals(dataCoding.toByte(), (byte) 0x0a);
  }

  @Test
  public void testGeneralAlphabetJisX02121990() {
    DataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_JIS_X_0212_1990);
    assertEquals(dataCoding.toByte(), (byte) 0x0d);
  }

  @Test
  public void testGeneralAlphabetKsC5601() {
    DataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_KS_C_5601);
    assertEquals(dataCoding.toByte(), (byte) 0x0e);
  }

  @Test
  public void testGeneralFlash() {
    DataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS0, false);
    assertEquals(dataCoding.toByte(), (byte) 0x10);
  }

  @Test
  public void testGeneralMeSpecific() {
    DataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false);
    assertEquals(dataCoding.toByte(), (byte) 0x11);
  }

  @Test
  public void testGeneralSimUsimSpecific() {
    DataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS2, false);
    assertEquals(dataCoding.toByte(), (byte) 0x12);
  }

  @Test
  public void testGeneralTeSpecific() {
    DataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS3, false);
    assertEquals(dataCoding.toByte(), (byte) 0x13);
  }

  @Test
  public void testGeneralAll() {
    assertEquals(GeneralDataCoding.DEFAULT.toByte(), (byte) 0x00);
    assertEquals(new GeneralDataCoding().toByte(), (byte) 0x00);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_DEFAULT).toByte(), (byte) 0x00);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_IA5).toByte(), (byte) 0x01);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UNSPECIFIED_2).toByte(), (byte) 0x02);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_LATIN1).toByte(), (byte) 0x03);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_8_BIT).toByte(), (byte) 0x04);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_JIS).toByte(), (byte) 0x05);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_CYRILLIC).toByte(), (byte) 0x06);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_LATIN_HEBREW).toByte(), (byte) 0x07);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UCS2).toByte(), (byte) 0x08);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_PICTOGRAM_ENCODING).toByte(), (byte) 0x09);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_ISO_2022_JP_MUSIC_CODES).toByte(), (byte) 0x0a);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_RESERVED_11).toByte(), (byte) 0x0b);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_RESERVED_12).toByte(), (byte) 0x0c);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_JIS_X_0212_1990).toByte(), (byte) 0x0d);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_RESERVED_15).toByte(), (byte) 0x0f);

    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS0, false).toByte(), (byte) 0x10);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false).toByte(), (byte) 0x11);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS2, false).toByte(), (byte) 0x12);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS3, false).toByte(), (byte) 0x13);

    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS0, false).toByte(), (byte) 0x14);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS1, false).toByte(), (byte) 0x15);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS2, false).toByte(), (byte) 0x16);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS3, false).toByte(), (byte) 0x17);

    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS0, false).toByte(), (byte) 0x18);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS1, false).toByte(), (byte) 0x19);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS2, false).toByte(), (byte) 0x1a);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS3, false).toByte(), (byte) 0x1b);

    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_RESERVED_12, MessageClass.CLASS0, false).toByte(), (byte) 0x1c);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_RESERVED_12, MessageClass.CLASS1, false).toByte(), (byte) 0x1d);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_RESERVED_12, MessageClass.CLASS2, false).toByte(), (byte) 0x1e);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_RESERVED_12, MessageClass.CLASS3, false).toByte(), (byte) 0x1f);

    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, null, true).toByte(), (byte) 0x20);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_8_BIT, null, true).toByte(), (byte) 0x24);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UCS2, null, true).toByte(), (byte) 0x28);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_RESERVED_12, null, true).toByte(), (byte) 0x2c);

    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS0, true).toByte(), (byte) 0x30);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, true).toByte(), (byte) 0x31);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS2, true).toByte(), (byte) 0x32);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS3, true).toByte(), (byte) 0x33);

    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS0, true).toByte(), (byte) 0x34);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS1, true).toByte(), (byte) 0x35);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS2, true).toByte(), (byte) 0x36);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS3, true).toByte(), (byte) 0x37);

    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS0, true).toByte(), (byte) 0x38);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS1, true).toByte(), (byte) 0x39);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS2, true).toByte(), (byte) 0x3a);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS3, true).toByte(), (byte) 0x3b);
  }
}