package org.jsmpp.examples.receipts;

import java.io.UnsupportedEncodingException;

import org.apache.log4j.BasicConfigurator;
import org.jsmpp.bean.DefaultDeliveryReceiptStripper;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.util.InvalidDeliveryReceiptException;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class ExampleDeliveryReceiptStripperTest {

  private ExampleDeliveryReceiptStripper deliveryReceiptStripper;
  private DefaultDeliveryReceiptStripper defaultDeliveryReceiptStripper;

  @BeforeMethod
  public void setUp() throws Exception {

    BasicConfigurator.configure();
    deliveryReceiptStripper = new ExampleDeliveryReceiptStripper();
    defaultDeliveryReceiptStripper = new DefaultDeliveryReceiptStripper();
  }

  @Test
  public void testDeliveryReceiptStripper() {
    final String message = "id:123456 sub:234 dlvrd:000 submit date:1604052345 done date:1605021145 stat:DELIVRD";
    DeliveryReceipt deliveryReceipt = null;
    try {
      DeliverSm deliverSm = new DeliverSm();
      // Short Message contains SMSC Delivery Receipt
      deliverSm.setEsmClass((byte)0x04);
      deliverSm.setShortMessage(message.getBytes("ASCII"));
      deliveryReceipt = deliveryReceiptStripper.strip(deliverSm);
      System.out.println(deliveryReceipt);
    }
    catch (InvalidDeliveryReceiptException e){
      System.out.println("InvalidDeliveryReceiptException " + e.getMessage());
    }catch (UnsupportedEncodingException e){
      System.out.println("UnsupportedEncodingException " + e.getMessage());
    }
    Assert.assertEquals("123456", deliveryReceipt.getId());
    Assert.assertEquals(234, deliveryReceipt.getSubmitted());
  }
}