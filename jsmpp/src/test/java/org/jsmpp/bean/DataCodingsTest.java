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
      } else if (i < 192) {
        assertEquals(DataCodings.newInstance(expected).getClass().getName(), "org.jsmpp.bean.RawDataCoding");
      } else if (i < 240) {
        assertEquals(DataCodings.newInstance(expected).getClass().getName(), "org.jsmpp.bean.MessageWaitingDataCoding");
      } else {
        assertEquals(DataCodings.newInstance(expected).getClass().getName(), "org.jsmpp.bean.SimpleDataCoding");
      }

      if (i < 192) {
        assertEquals(dataCoding.toByte(), expected);
      }
    }
  }

  @Test
  public void testAllDataCodings() {
    assertEquals(DataCodings.ZERO.toByte(), (byte) 0x00);
    assertEquals(GeneralDataCoding.DEFAULT.toByte(), (byte) 0x00);
    assertEquals(new GeneralDataCoding().toByte(), (byte) 0x00);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_DEFAULT).toByte(), (byte) 0x00);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_IA5).toByte(), (byte) 0x01);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UNSPECIFIED_2).toByte(), (byte) 0x02);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_LATIN1).toByte(), (byte) 0x03);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_8_BIT).toByte(), (byte) 0x04);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_JIS).toByte(), (byte) 0x05);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_CYRILLIC).toByte(), (byte) 0x06);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_LATIN_HEBREW).toByte(), (byte) 0x07);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UCS2).toByte(), (byte) 0x08);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_PICTOGRAM_ENCODING).toByte(), (byte) 0x09);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_ISO_2022_JP_MUSIC_CODES).toByte(), (byte) 0x0a);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_RESERVED_11).toByte(), (byte) 0x0b);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_RESERVED_12).toByte(), (byte) 0x0c);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_JIS_X_0212_1990).toByte(), (byte) 0x0d);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_RESERVED_15).toByte(), (byte) 0x0f);

    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS0, false).toByte(), (byte) 0x10);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, false).toByte(), (byte) 0x11);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS2, false).toByte(), (byte) 0x12);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS3, false).toByte(), (byte) 0x13);

    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS0, false).toByte(), (byte) 0x14);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS1, false).toByte(), (byte) 0x15);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS2, false).toByte(), (byte) 0x16);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS3, false).toByte(), (byte) 0x17);

    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS0, false).toByte(), (byte) 0x18);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS1, false).toByte(), (byte) 0x19);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS2, false).toByte(), (byte) 0x1a);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS3, false).toByte(), (byte) 0x1b);

    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_RESERVED_12, MessageClass.CLASS0, false).toByte(), (byte) 0x1c);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_RESERVED_12, MessageClass.CLASS1, false).toByte(), (byte) 0x1d);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_RESERVED_12, MessageClass.CLASS2, false).toByte(), (byte) 0x1e);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_RESERVED_12, MessageClass.CLASS3, false).toByte(), (byte) 0x1f);

    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, null, true).toByte(), (byte) 0x20);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_8_BIT, null, true).toByte(), (byte) 0x24);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UCS2, null, true).toByte(), (byte) 0x28);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_RESERVED_12, null, true).toByte(), (byte) 0x2c);

    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS0, true).toByte(), (byte) 0x30);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1, true).toByte(), (byte) 0x31);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS2, true).toByte(), (byte) 0x32);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS3, true).toByte(), (byte) 0x33);

    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS0, true).toByte(), (byte) 0x34);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS1, true).toByte(), (byte) 0x35);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS2, true).toByte(), (byte) 0x36);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS3, true).toByte(), (byte) 0x37);

    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS0, true).toByte(), (byte) 0x38);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS1, true).toByte(), (byte) 0x39);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS2, true).toByte(), (byte) 0x3a);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_UCS2, MessageClass.CLASS3, true).toByte(), (byte) 0x3b);

    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_RESERVED_12, MessageClass.CLASS0, true).toByte(), (byte) 0x3c);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_RESERVED_12, MessageClass.CLASS1, true).toByte(), (byte) 0x3d);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_RESERVED_12, MessageClass.CLASS2, true).toByte(), (byte) 0x3e);
    assertEquals(new GeneralDataCoding(Alphabet.ALPHA_RESERVED_12, MessageClass.CLASS3, true).toByte(), (byte) 0x3f);

    assertEquals(new MessageWaitingDataCoding(IndicationSense.INACTIVE, IndicationType.VOICEMAIL_MESSAGE_WAITING).toByte(), (byte) 0xc0);
    assertEquals(new MessageWaitingDataCoding(IndicationSense.INACTIVE, IndicationType.FAX_MESSAGE_WAITING).toByte(), (byte) 0xc1);
    assertEquals(new MessageWaitingDataCoding(IndicationSense.INACTIVE, IndicationType.ELECTRONIC_MESSAGE_WAITING).toByte(), (byte) 0xc2);
    assertEquals(new MessageWaitingDataCoding(IndicationSense.INACTIVE, IndicationType.OTHER_MESSAGE_WAITING).toByte(), (byte) 0xc3);

    assertEquals(new MessageWaitingDataCoding(IndicationSense.ACTIVE, IndicationType.VOICEMAIL_MESSAGE_WAITING).toByte(), (byte) 0xc8);
    assertEquals(new MessageWaitingDataCoding(IndicationSense.ACTIVE, IndicationType.FAX_MESSAGE_WAITING).toByte(), (byte) 0xc9);
    assertEquals(new MessageWaitingDataCoding(IndicationSense.ACTIVE, IndicationType.ELECTRONIC_MESSAGE_WAITING).toByte(), (byte) 0xca);
    assertEquals(new MessageWaitingDataCoding(IndicationSense.ACTIVE, IndicationType.OTHER_MESSAGE_WAITING).toByte(), (byte) 0xcb);

    assertEquals(new MessageWaitingDataCoding(IndicationSense.INACTIVE, IndicationType.VOICEMAIL_MESSAGE_WAITING, Alphabet.ALPHA_DEFAULT).toByte(),
        (byte) 0xd0);
    assertEquals(new MessageWaitingDataCoding(IndicationSense.INACTIVE, IndicationType.FAX_MESSAGE_WAITING, Alphabet.ALPHA_DEFAULT).toByte(), (byte) 0xd1);
    assertEquals(new MessageWaitingDataCoding(IndicationSense.INACTIVE, IndicationType.ELECTRONIC_MESSAGE_WAITING, Alphabet.ALPHA_DEFAULT).toByte(),
        (byte) 0xd2);
    assertEquals(new MessageWaitingDataCoding(IndicationSense.INACTIVE, IndicationType.OTHER_MESSAGE_WAITING, Alphabet.ALPHA_DEFAULT).toByte(), (byte) 0xd3);

    assertEquals(new MessageWaitingDataCoding(IndicationSense.ACTIVE, IndicationType.VOICEMAIL_MESSAGE_WAITING, Alphabet.ALPHA_DEFAULT).toByte(),
        (byte) 0xd8);
    assertEquals(new MessageWaitingDataCoding(IndicationSense.ACTIVE, IndicationType.FAX_MESSAGE_WAITING, Alphabet.ALPHA_DEFAULT).toByte(), (byte) 0xd9);
    assertEquals(new MessageWaitingDataCoding(IndicationSense.ACTIVE, IndicationType.ELECTRONIC_MESSAGE_WAITING, Alphabet.ALPHA_DEFAULT).toByte(),
        (byte) 0xda);
    assertEquals(new MessageWaitingDataCoding(IndicationSense.ACTIVE, IndicationType.OTHER_MESSAGE_WAITING, Alphabet.ALPHA_DEFAULT).toByte(), (byte) 0xdb);

    assertEquals(new MessageWaitingDataCoding(IndicationSense.INACTIVE, IndicationType.VOICEMAIL_MESSAGE_WAITING, Alphabet.ALPHA_UCS2).toByte(),
        (byte) 0xe0);
    assertEquals(new MessageWaitingDataCoding(IndicationSense.INACTIVE, IndicationType.FAX_MESSAGE_WAITING, Alphabet.ALPHA_UCS2).toByte(), (byte) 0xe1);
    assertEquals(new MessageWaitingDataCoding(IndicationSense.INACTIVE, IndicationType.ELECTRONIC_MESSAGE_WAITING, Alphabet.ALPHA_UCS2).toByte(),
        (byte) 0xe2);
    assertEquals(new MessageWaitingDataCoding(IndicationSense.INACTIVE, IndicationType.OTHER_MESSAGE_WAITING, Alphabet.ALPHA_UCS2).toByte(), (byte) 0xe3);

    assertEquals(new MessageWaitingDataCoding(IndicationSense.ACTIVE, IndicationType.VOICEMAIL_MESSAGE_WAITING, Alphabet.ALPHA_UCS2).toByte(),
        (byte) 0xe8);
    assertEquals(new MessageWaitingDataCoding(IndicationSense.ACTIVE, IndicationType.FAX_MESSAGE_WAITING, Alphabet.ALPHA_UCS2).toByte(), (byte) 0xe9);
    assertEquals(new MessageWaitingDataCoding(IndicationSense.ACTIVE, IndicationType.ELECTRONIC_MESSAGE_WAITING, Alphabet.ALPHA_UCS2).toByte(),
        (byte) 0xea);
    assertEquals(new MessageWaitingDataCoding(IndicationSense.ACTIVE, IndicationType.OTHER_MESSAGE_WAITING, Alphabet.ALPHA_UCS2).toByte(), (byte) 0xeb);

    assertEquals(new SimpleDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS0).toByte(), (byte) 0xf0);
    assertEquals(new SimpleDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS1).toByte(), (byte) 0xf1);
    assertEquals(new SimpleDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS2).toByte(), (byte) 0xf2);
    assertEquals(new SimpleDataCoding(Alphabet.ALPHA_DEFAULT, MessageClass.CLASS3).toByte(), (byte) 0xf3);

    assertEquals(new SimpleDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS0).toByte(), (byte) 0xf4);
    assertEquals(new SimpleDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS1).toByte(), (byte) 0xf5);
    assertEquals(new SimpleDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS2).toByte(), (byte) 0xf6);
    assertEquals(new SimpleDataCoding(Alphabet.ALPHA_8_BIT, MessageClass.CLASS3).toByte(), (byte) 0xf7);
  }
}