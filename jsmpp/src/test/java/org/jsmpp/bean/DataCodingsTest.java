package org.jsmpp.bean;


import static org.testng.Assert.assertEquals;

import org.jsmpp.util.HexUtil;
import org.testng.annotations.Test;

/**
 * @author pmoerenhout
 */
public class DataCodingsTest {

  @Test
  public void testDataCodingsZero() {
    byte expected = (byte) (0x00);
    DataCoding dataCoding = DataCodings.ZERO;
    assertEquals(dataCoding.toByte(), expected);
    assertEquals(new GeneralDataCoding(), dataCoding);
  }

  @Test
  public void testDataCodings00() {
    byte expected = (byte) 0x00;
    DataCoding dataCoding = DataCodings.newInstance(expected);
    assertEquals(dataCoding.toByte(), expected);

    assertEquals(new GeneralDataCoding(), dataCoding);
  }

  @Test
  public void testDataCodings11() {
    byte expected = (byte) 0x11;
    DataCoding dataCoding = DataCodings.newInstance(expected);
    assertEquals(dataCoding.toByte(), expected);

    DataCoding buildedInstance = DataCodings.newInstance(dataCoding.toByte());
    assertEquals(buildedInstance, dataCoding);
  }

  @Test
  public void testAllDataCodings() {
    for (int i = 0; i < 256; i++) {
      byte expected = (byte) (0xff & i);
      DataCoding dataCoding = DataCodings.newInstance(expected);
      System.out.println("byte " + expected + " " + HexUtil
          .conventBytesToHexString(new byte[]{ expected }) + " " + (0xff & expected) + " => " + dataCoding);
      if (i < 64) {
        assertEquals("org.jsmpp.bean.GeneralDataCoding", DataCodings.newInstance(expected).getClass().getName());
      }
      else if (i < 192) {
        assertEquals("org.jsmpp.bean.RawDataCoding", DataCodings.newInstance(expected).getClass().getName());
      }
      else if (i < 240) {
        assertEquals("org.jsmpp.bean.MessageWaitingDataCoding", DataCodings.newInstance(expected).getClass().getName());
      }
      else {
        assertEquals("org.jsmpp.bean.SimpleDataCoding", DataCodings.newInstance(expected).getClass().getName());
      }

      if (i < 192) {
        assertEquals(dataCoding.toByte(), expected);
      }
      assertEquals(dataCoding, DataCodings.newInstance(expected));
    }
  }


}