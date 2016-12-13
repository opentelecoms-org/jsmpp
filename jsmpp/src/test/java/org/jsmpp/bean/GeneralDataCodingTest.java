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
  public void testGeneralAlphabetUcs2() {
    DataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_UCS2);
    assertEquals(dataCoding.toByte(), (byte) 0x08);
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