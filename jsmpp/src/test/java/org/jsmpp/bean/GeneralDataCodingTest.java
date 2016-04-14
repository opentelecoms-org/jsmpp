package org.jsmpp.bean;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * @author pmoerenhout
 */
public class GeneralDataCodingTest {

  @Test
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
  }
}