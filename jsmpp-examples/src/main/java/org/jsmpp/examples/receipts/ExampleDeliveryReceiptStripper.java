/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.jsmpp.examples.receipts;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.DeliveryReceiptStrip;
import org.jsmpp.bean.MessageType;
import org.jsmpp.util.DeliveryReceiptState;
import org.jsmpp.util.InvalidDeliveryReceiptException;

/**
 * This is an example delivery receipt stripper for the default DeliveryReceipt.
 *
 * @author uudashr
 */
public class ExampleDeliveryReceiptStripper implements DeliveryReceiptStrip<DeliveryReceipt> {

  private static final Pattern PATTERN = Pattern.compile("^id:([0-9]*) sub:([0-9]{3}) dlvrd:([0-9]{3}) submit date:([0-9]{10}) done date:([0-9]{10}) stat:([a-zA-Z]*)$");

  private static final ExampleDeliveryReceiptStripper instance = new ExampleDeliveryReceiptStripper();

  public static ExampleDeliveryReceiptStripper getInstance() {
    return instance;
  }

  @Override
  public DeliveryReceipt strip(DeliverSm deliverSm) throws InvalidDeliveryReceiptException {
    if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass()) || MessageType.SME_DEL_ACK.containedIn(deliverSm.getEsmClass())) {
      return deliveryReceipt(deliverSm.getShortMessage());
    }
    else {
      throw new InvalidDeliveryReceiptException(
          "deliver_sm is not a delivery receipt since esm_class value = " + deliverSm.getEsmClass());
    }
  }

  private DeliveryReceipt deliveryReceipt(byte[] shortMessage){
    DeliveryReceipt deliveryReceipt = new DeliveryReceipt();
    Matcher matcher = PATTERN.matcher(new String(shortMessage));
    if (matcher.find()){
      final String id = matcher.group(1);
      final String submitted = matcher.group(2);
      final String delivered = matcher.group(3);
      final String submitDate = matcher.group(4);
      final String doneDate = matcher.group(5);
      final String status = matcher.group(6);

      deliveryReceipt.setId(id);
      deliveryReceipt.setSubmitted(Integer.parseInt(submitted));
      deliveryReceipt.setDelivered(Integer.parseInt(delivered));
      deliveryReceipt.setSubmitDate(string2Date(submitDate));
      deliveryReceipt.setDoneDate(string2Date(doneDate));
      deliveryReceipt.setError("000");
      deliveryReceipt.setText("");
      if ("Successful".equals(status)){
        deliveryReceipt.setFinalStatus(DeliveryReceiptState.DELIVRD);
      }
      else {
        deliveryReceipt.setFinalStatus(DeliveryReceiptState.valueOf(status));
      }
    }
    return deliveryReceipt;
  }

  private static Date string2Date(String date) {

    int year = Integer.parseInt(date.substring(0, 2));
    int month = Integer.parseInt(date.substring(2, 4) + 1);
    int day = Integer.parseInt(date.substring(4, 6));
    int hour = Integer.parseInt(date.substring(6, 8));
    int minute = Integer.parseInt(date.substring(8, 10));
    Calendar cal = Calendar.getInstance();
    cal.set(convertTwoDigitYear(year), month - 1, day, hour, minute, 0);
    cal.set(Calendar.MILLISECOND, 0);
    return cal.getTime();
  }

  private static int convertTwoDigitYear(int year) {
    if (year >=0 && year <= 37) {
      return 2000 + year;
    } else if (year >= 38 && year <= 99) {
      return 1900 + year;
    } else {
      // should never happen
      return year;
    }
  }

}
