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

import java.nio.charset.StandardCharsets;

import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.DeliveryReceiptStrip;
import org.jsmpp.bean.MessageType;
import org.jsmpp.util.InvalidDeliveryReceiptException;

/**
 * This is an example delivery receipt stripper. It also parses the custom final status. Use with DeliverSm.getDeliveryReceipt()
 *
 * @author pmoerenhout
 */
public class CustomDeliveryReceiptStripper implements DeliveryReceiptStrip<CustomDeliveryReceipt> {

  private static final CustomDeliveryReceiptStripper instance = new CustomDeliveryReceiptStripper();

  public static CustomDeliveryReceiptStripper getInstance() {
    return instance;
  }

  @Override
  public CustomDeliveryReceipt strip(DeliverSm deliverSm) throws InvalidDeliveryReceiptException {
    if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass()) || MessageType.SME_DEL_ACK.containedIn(deliverSm.getEsmClass())) {
      return new CustomDeliveryReceipt(new String(deliverSm.getShortMessage(), StandardCharsets.US_ASCII));
    } else {
      throw new InvalidDeliveryReceiptException(
          "deliver_sm is not a delivery receipt since esm_class value = " + deliverSm.getEsmClass());
    }
  }

}
