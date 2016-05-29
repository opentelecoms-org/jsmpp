package org.jsmpp.bean;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author pmoerenhout
 */
public class DataCodingFactory00xxTest {

  DataCodingFactory00xx factory;

  @BeforeMethod
  public void setUp() throws Exception {
    factory = new DataCodingFactory00xx();
  }

  @Test
  public void testDataCodings00xx() {
    for (byte dataCodingByte = (byte) 0x00; dataCodingByte < (byte) 0xf0; dataCodingByte++) {
      DataCoding dataCoding = factory.newInstance(dataCodingByte);
      assertEquals(dataCoding.toByte(), dataCodingByte);
      assertEquals(dataCoding.getClass(), GeneralDataCoding.class);
    }
  }

  @Test
  public void testDataCodingFactory00xxStandardAlphabet() {
    byte expected = (byte) 0x00;
    DataCoding dataCoding = factory.newInstance(expected);

    assertEquals(dataCoding.toByte(), expected);
    assertEquals(Alphabet.parseDataCoding(dataCoding.toByte()), Alphabet.ALPHA_DEFAULT);
  }

  @Test
  public void testDataCodingFactory00xxIA5() {
    byte expected = (byte) 0x01;
    DataCoding dataCoding = factory.newInstance(expected);

    assertEquals(dataCoding.toByte(), expected);
    assertEquals(Alphabet.parseDataCoding(dataCoding.toByte()), Alphabet.ALPHA_IA5);
  }

  @Test
  public void testDataCodingFactory00xxUCS2() {
    byte expected = (byte) 0x08;
    DataCoding dataCoding = factory.newInstance(expected);

    assertEquals(dataCoding.toByte(), expected);
    assertEquals(Alphabet.parseDataCoding(dataCoding.toByte()), Alphabet.ALPHA_UCS2);
  }
}