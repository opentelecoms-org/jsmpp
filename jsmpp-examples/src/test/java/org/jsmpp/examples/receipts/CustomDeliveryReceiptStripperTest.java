package org.jsmpp.examples.receipts;

import static org.jsmpp.examples.receipts.CustomDeliveryReceiptState.DND_REJECTED;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Date;

import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GSMSpecificFeature;
import org.jsmpp.bean.MessageMode;
import org.jsmpp.bean.MessageType;
import org.jsmpp.util.InvalidDeliveryReceiptException;
import org.junit.Before;
import org.junit.Test;

public class CustomDeliveryReceiptStripperTest {

  private CustomDeliveryReceiptStripper customDeliveryReceiptStripper;

  @Before
  public void setUp() throws Exception {
    customDeliveryReceiptStripper = new CustomDeliveryReceiptStripper();
  }

  @Test
  public void testParseValid() {
    try {
      DeliverSm deliverSm = new DeliverSm();
      deliverSm.setEsmClass(new ESMClass(MessageMode.DEFAULT, MessageType.SMSC_DEL_RECEIPT, GSMSpecificFeature.DEFAULT).value());
      deliverSm.setShortMessage(
          ("id:0123456789 sub:002 dlvrd:003 submit date:0809011130 done date:0809021131 err:123 stat:DND_REJECTED text:Hello World")
              .getBytes(StandardCharsets.US_ASCII));
      CustomDeliveryReceipt delReceipt = customDeliveryReceiptStripper.strip(deliverSm);
      assertEquals("0123456789", delReceipt.getId());
      assertEquals(2, delReceipt.getSubmitted());
      assertEquals(3, delReceipt.getDelivered());
      assertEquals(createDate(2008, 9, 1, 11, 30), delReceipt.getSubmitDate());
      assertEquals(createDate(2008, 9, 2, 11, 31), delReceipt.getDoneDate());
      assertEquals("123", delReceipt.getError());
      assertEquals(DND_REJECTED, delReceipt.getFinalStatus());
      assertEquals("Hello World", delReceipt.getText());
    } catch (InvalidDeliveryReceiptException e) {
      fail("Failed parsing delivery receipt: " + e.getMessage());
    }
  }

  private Date createDate(int year, int month, int day, int hour, int minute) {
    return createDate(year, month, day, hour, minute, 0);
  }

  private Date createDate(int year, int month, int day, int hour, int minute, int second) {
    Calendar cal = Calendar.getInstance();
    cal.set(year, month - 1, day, hour, minute, second);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }

}