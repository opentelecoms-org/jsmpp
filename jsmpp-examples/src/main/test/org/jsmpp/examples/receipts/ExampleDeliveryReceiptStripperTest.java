package org.jsmpp.examples.receipts;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;

import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.ESMClass;
import org.jsmpp.bean.GSMSpecificFeature;
import org.jsmpp.bean.MessageMode;
import org.jsmpp.bean.MessageType;
import org.jsmpp.util.InvalidDeliveryReceiptException;
import org.junit.Before;
import org.junit.Test;

public class ExampleDeliveryReceiptStripperTest {

  private ExampleDeliveryReceiptStripper exampleDeliveryReceiptStripper;

  @Before
  public void setUp() throws Exception {
    exampleDeliveryReceiptStripper = new ExampleDeliveryReceiptStripper();
  }

  @Test
  public void testParseValid() {
    try {
      DeliverSm deliverSm = new DeliverSm();
      deliverSm.setEsmClass(new ESMClass(MessageMode.DEFAULT, MessageType.SMSC_DEL_RECEIPT, GSMSpecificFeature.DEFAULT).value());
      deliverSm.setShortMessage(
          ("id:0123456789 sub:001 dlvrd:001 submit date:0809011130 done date:0809021131 stat:DELIVRD").getBytes());
      DeliveryReceipt delReceipt = exampleDeliveryReceiptStripper.strip(deliverSm);
      assertEquals("", delReceipt.getText());

      assertEquals(1, delReceipt.getSubmitted());

      Date submitDate = delReceipt.getSubmitDate();
      Date expectedSubmitDate = createDate(2008, 9, 1, 11, 30);
      assertEquals(expectedSubmitDate, submitDate);

      Date doneDate = delReceipt.getDoneDate();
      Date expectedDoneDate = createDate(2008, 9, 2, 11, 31);
      assertEquals(expectedDoneDate, doneDate);
    } catch (InvalidDeliveryReceiptException e) {
      fail("Failed parsing delivery receipt:" + e.getMessage());
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