package org.jsmpp.bean;


import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

/**
 * @author pmoerenhout
 */
public class DataCodingsTest {

  @Test
  public void testDataCodingsZero() {
    DataCoding dataCoding = DataCodings.ZERO;
    assertEquals(dataCoding.toByte(), (byte) 0x00);
  }

  @Test
  public void testDataCodingsFactories() {
    for (int i = 0; i < 256; i++) {
      byte expected = (byte) (0xff & i);
      DataCoding dataCoding = DataCodings.newInstance(expected);
      if (i < 64) {
        assertEquals(DataCodings.newInstance(expected).getClass().getName(), "org.jsmpp.bean.GeneralDataCoding");
      }
      else if (i < 192) {
        assertEquals(DataCodings.newInstance(expected).getClass().getName(), "org.jsmpp.bean.RawDataCoding");
      }
      else if (i < 240) {
        assertEquals(DataCodings.newInstance(expected).getClass().getName(), "org.jsmpp.bean.MessageWaitingDataCoding");
      }
      else {
        assertEquals(DataCodings.newInstance(expected).getClass().getName(), "org.jsmpp.bean.SimpleDataCoding");
      }

      if (i < 192) {
        assertEquals(dataCoding.toByte(), expected);
      }
    }
  }


}