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

/**
 * @author pmoerenhout *
 */
public enum CustomDeliveryReceiptState {

  /* the value could be use to store in database */
  /**
   * ENROUTE
   */
  ENROUTE(0),
  /**
   * DELIVERED
   */
  DELIVRD(1),
  /**
   * EXPIRED
   */
  EXPIRED(2),
  /**
   * DELETED
   */
  DELETED(3),
  /**
   * UNDELIVERABLE
   */
  UNDELIV(4),
  /**
   * ACCEPTED
   */
  ACCEPTD(5),
  /**
   * UNKNOWN
   */
  UNKNOWN(6),
  /**
   * REJECTED
   */
  REJECTD(7),
  /**
   * DO NOT DISTURB REJECTED
   */
  DND_REJECTED(9);

  private int value;

  CustomDeliveryReceiptState(int value) {
    this.value = value;
  }

  public static CustomDeliveryReceiptState getByName(String name) {
    return valueOf(CustomDeliveryReceiptState.class, name.toUpperCase());
  }

  public static CustomDeliveryReceiptState valueOf(int value)
      throws IllegalArgumentException {
    for (CustomDeliveryReceiptState item : values()) {
      if (item.value() == value) {
        return item;
      }
    }
    throw new IllegalArgumentException(
        "No enum const CustomDeliveryReceiptState with value " + value);
  }

  public int value() {
    return value;
  }
}
