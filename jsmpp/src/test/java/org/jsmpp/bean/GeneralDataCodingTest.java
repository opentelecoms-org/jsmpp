package org.jsmpp.bean;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * @author pmoerenhout
 */
public class GeneralDataCodingTest {

  @Test
<<<<<<< HEAD
  public void testGeneral() {
    byte expected = (byte) 0x00;

    DataCoding dataCoding = new GeneralDataCoding();
    assertEquals(dataCoding.toByte(), expected);

    DataCoding dataCodingWithDefaultAlphabet = new GeneralDataCoding(Alphabet.ALPHA_DEFAULT);
    assertEquals(dataCodingWithDefaultAlphabet.toByte(), expected);

    DataCoding buildedInstance = new GeneralDataCoding();
    assertEquals(buildedInstance, dataCoding);
    assertEquals(buildedInstance, dataCodingWithDefaultAlphabet);
  }

  @Test
  public void testGeneralAlphabetDefault() {
    byte expected = (byte) 0x00;
    DataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_DEFAULT);
    assertEquals(dataCoding.toByte(), expected);

    DataCoding buildedInstance = new GeneralDataCoding();
    assertEquals(buildedInstance, dataCoding);
  }

  @Test
  public void testGeneral11() {
    DataCoding dataCoding = new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false);
    byte expected = (byte) 0x11;
    assertEquals(dataCoding.toByte(), expected);

    DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
    assertEquals(buildedInstance, dataCoding);
=======
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
>>>>>>> opentelecoms-org/master
  }
}