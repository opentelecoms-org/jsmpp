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

import org.jsmpp.util.InvalidDeliveryReceiptException;

/**
 * The information of delivery receipt may insert on short_message parameter or on optional parameter and
 * the format of typically like:
 * <code>id:IIIIIIIIII sub:SSS dlvrd:DDD submit date:YYMMDDhhmm done date:YYMMDDhhmm stat:DDDDDDD err:E Text: ........</code>
 * but SMSC vendor specific may applicable. So we can define our own strategy to
 * strip off the delivery receipt from {@link DeliverSm}.
 *
 * @author uudashr
 * @see DefaultDeliveryReceiptStripper
 *
 * @param <T> the returned class by strip method
 */
public interface DeliveryReceiptStrip<T> {

    /**
     * Strip and return the delivery receipt as an object of class T.
     *
     * @param deliverSm the deliver_sm
     * @return delivery receipt object of class T
     * @throws InvalidDeliveryReceiptException if there is an error found while
     *         stripping the delivery receipt from the deliverSm parameter
     */
    T strip(DeliverSm deliverSm) throws InvalidDeliveryReceiptException;
}
