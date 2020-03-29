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
}