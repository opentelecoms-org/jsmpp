package org.jsmpp.bean;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author pmoerenhout
 */
public class DataCodingFactory1100Test {

  DataCodingFactory1100 factory;

  @BeforeMethod
  public void setUp() throws Exception {
    factory = new DataCodingFactory1100();
  }

  @Test
  public void testDataCodings1100() {
    for (byte dataCodingByte = (byte) 0xc0; dataCodingByte < (byte) 0xd0; dataCodingByte++) {
      DataCoding dataCoding = factory.newInstance(dataCodingByte);
      assertEquals(dataCoding.getClass(), MessageWaitingDataCoding.class);
      // assertEquals(Alphabet.parseDataCoding(dataCoding.toByte()), Alphabet.ALPHA_DEFAULT);
    }
  }
}