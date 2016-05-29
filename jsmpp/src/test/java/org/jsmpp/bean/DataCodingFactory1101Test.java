package org.jsmpp.bean;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * @author pmoerenhout
 */
public class DataCodingFactory1101Test {

  DataCodingFactory1101 factory;

  @BeforeMethod
  public void setUp() throws Exception {
    factory = new DataCodingFactory1101();
  }

  @Test
  public void testDataCodings1101() {
    for (byte dataCodingByte = (byte) 0xd0; dataCodingByte < (byte) 0xe0; dataCodingByte++) {
      DataCoding dataCoding = factory.newInstance(dataCodingByte);
      assertEquals(dataCoding.getClass(), MessageWaitingDataCoding.class);
      // assertEquals(Alphabet.parseDataCoding(dataCoding.toByte()), Alphabet.ALPHA_DEFAULT);
    }
  }

  @Test
  public void testDataCodingVoicemailInactive() {
    byte dataCodingByte = (byte) 0xd0;
    DataCoding dataCoding = factory.newInstance(dataCodingByte);
    assertEquals(dataCoding.getClass(), MessageWaitingDataCoding.class);
    assertEquals(IndicationSense.parseDataCoding(dataCoding.toByte()), IndicationSense.INACTIVE);
    assertEquals(IndicationType.parseDataCoding(dataCoding.toByte()), IndicationType.VOICEMAIL_MESSAGE_WAITING);
    // assertEquals(Alphabet.parseDataCoding(dataCoding.toByte()), Alphabet.ALPHA_DEFAULT);
  }

  @Test
  public void testDataCodingVoicemailActive() {
    byte dataCodingByte = (byte) 0xd8;
    DataCoding dataCoding = factory.newInstance(dataCodingByte);
    assertEquals(dataCoding.getClass(), MessageWaitingDataCoding.class);
    assertEquals(IndicationSense.parseDataCoding(dataCoding.toByte()), IndicationSense.ACTIVE);
    assertEquals(IndicationType.parseDataCoding(dataCoding.toByte()), IndicationType.VOICEMAIL_MESSAGE_WAITING);
    // assertEquals(Alphabet.parseDataCoding(dataCoding.toByte()), Alphabet.ALPHA_DEFAULT);
  }
}