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
package org.jsmpp.bean;

import org.jsmpp.util.DefaultDecomposer;
import org.jsmpp.util.InvalidDeliveryReceiptException;

/**
 * These are default delivery receipt stripper. It uses a typical format defined on
 * SMPP v3.4 from short_message parameter.
 *
 * @author uudashr
 *
 */
public class DefaultDeliveryReceiptStripper implements DeliveryReceiptStrip<DeliveryReceipt> {

    private static final DefaultDeliveryReceiptStripper instance = new DefaultDeliveryReceiptStripper();

    public static DefaultDeliveryReceiptStripper getInstance() {
        return instance;
    }

    @Override
    public DeliveryReceipt strip(DeliverSm deliverSm) throws InvalidDeliveryReceiptException {
        if (MessageType.SMSC_DEL_RECEIPT.containedIn(deliverSm.getEsmClass())) {
            return DefaultDecomposer.getInstance().deliveryReceipt(deliverSm.getShortMessage());
        } else {
            throw new InvalidDeliveryReceiptException("deliver_sm is not a delivery receipt since esm_class value = " + deliverSm.getEsmClass());
        }
    }

}
