package org.jsmpp.bean;


import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * @author pmoerenhout
 */
public class RawDataCodingTest {

  @Test
  public void testAllRawDataCodings() {
    for (int i = 0; i < 256; i++) {
      byte expected = (byte) (0xff & i);
      RawDataCoding dataCoding = new RawDataCoding(expected);
      assertEquals(dataCoding.toByte(), expected);
      assertEquals(dataCoding, new RawDataCoding(expected));
    }
  }
}