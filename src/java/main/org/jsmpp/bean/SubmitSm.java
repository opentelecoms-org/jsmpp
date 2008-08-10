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

import org.jsmpp.SMPPConstant;

/**
 * @author uudashr
 * 
 */
public class SubmitSm extends MessageRequest {

    public SubmitSm() {
        super();
    }

    /**
     * Messaging Mode.
     * 
     * @return
     */
    public boolean isDefaultMode() {
        return isDefaultMode(esmClass);
    }

    /**
     * Messaging Mode.
     */
    public void setDefaultMode() {
        esmClass = composeDefaultMode(esmClass);
    }

    /**
     * Messaging Mode.
     * 
     * @return
     */
    public boolean isDatagramMode() {
        return isDatagramMode(esmClass);
    }

    /**
     * Messaging Mode.
     */
    public void setDatagramMode() {
        esmClass = composeDatagramMode(esmClass);
    }

    /**
     * Messaging Mode.
     * 
     * @return
     */
    public boolean isForwardMode() {
        return isForwardMode(esmClass);
    }

    /**
     * Messaging Mode.
     */
    public void setForwardMode() {
        esmClass = composeForwardMode(esmClass);
    }

    /**
     * Messaging Mode.
     * 
     * @return
     */
    public boolean isStoreAndForwardMode() {
        return isStoreAndForwardMode(esmClass);
    }

    /**
     * Messaging Mode.
     */
    public void setStoreAndForwardMode() {
        composeStoreAndForwardMode(esmClass);
    }

    /**
     * Message Type.
     * 
     * @return
     */
    public boolean isEsmeDeliveryAcknowledgement() {
        return isEsmeDeliveryAcknowledgement(esmClass);
    }

    /**
     * Message Type.
     */
    public void setEsmeDelivertAcknowledgement() {
        esmClass = composeEsmeDeliveryAcknowledgement(esmClass);
    }

    /**
     * Message Type.
     * 
     * @return
     */
    public boolean isEsmeManualAcknowledgement() {
        return isEsmeManualAcknowledgement(esmClass);
    }

    /**
     * Message Type.
     */
    public void setEsmeManualAcknowledgement() {
        esmClass = composeEsmeManualAcknowledgement(esmClass);
    }

    /**
     * SMSC Delivery Receipt.
     * 
     * @return
     */
    public boolean isSmscDelReceiptNotRequested() {
        return isSmscDelNotRequested(registeredDelivery);
    }

    /**
     * SMSC Delivery Receipt.
     */
    public void setSmscDelReceiptNotRequested() {
        registeredDelivery = composeSmscDelReceiptNotRequested(registeredDelivery);
    }

    /**
     * SMSC Delivery Receipt.
     * 
     * @return
     */
    public boolean isSmscDelReceiptSuccessAndFailureRequested() {
        return isSmscDelReceiptSuccessAndFailureRequested(registeredDelivery);
    }

    /**
     * SMSC Delivery Receipt.
     */
    public void setSmscDelReceiptSuccessAndFailureRequested() {
        registeredDelivery = composeSmscDelReceiptSuccessAndFailureRequested(registeredDelivery);
    }

    /**
     * SMSC Delivery Receipt.
     * 
     * @return
     */
    public boolean isSmscDelReceiptFailureRequested() {
        return isSmscDelReceiptFailureRequested(registeredDelivery);
    }

    /**
     * SMSC Delivery Receipt.
     */
    public void setSmscDelReceiptFailureRequested() {
        registeredDelivery = composeSmscDelReceiptFailureRequested(registeredDelivery);
    }

    /**
     * Messaging Mode.
     * 
     * @param esmClass
     * @return
     * @deprecated see {@link MessageMode#containedIn(byte)}
     */
    public static final boolean isDefaultMode(byte esmClass) {
        return isMessagingMode(esmClass, SMPPConstant.ESMCLS_DEFAULT_MODE);
    }

    /**
     * Messaging Mode.
     * 
     * @param esmClass
     * @return
     * @deprecated use {@link MessageMode#compose(byte, MessageMode)} 
     */
    public static final byte composeDefaultMode(byte esmClass) {
        return composeMessagingMode(esmClass, SMPPConstant.ESMCLS_DEFAULT_MODE);
    }

    /**
     * Messaging Mode.
     * 
     * @param esmClass
     * @return
     */
    public static final boolean isDatagramMode(byte esmClass) {
        return isMessagingMode(esmClass, SMPPConstant.ESMCLS_DATAGRAM_MODE);
    }

    /**
     * Messaging Mode.
     * 
     * @param esmClass
     * @return
     */
    public static final byte composeDatagramMode(byte esmClass) {
        return composeMessagingMode(esmClass, SMPPConstant.ESMCLS_DATAGRAM_MODE);
    }

    /**
     * Messaging Mode.
     * 
     * @param esmClass
     * @return
     */
    public static final boolean isForwardMode(byte esmClass) {
        return isMessagingMode(esmClass, SMPPConstant.ESMCLS_FORWARD_MODE);
    }

    /**
     * Messaging Mode.
     * 
     * @param esmClass
     * @return
     */
    public static final byte composeForwardMode(byte esmClass) {
        return composeMessagingMode(esmClass, SMPPConstant.ESMCLS_FORWARD_MODE);
    }

    /**
     * Messaging Mode.
     * 
     * @param esmClass
     * @return
     */
    public static final boolean isStoreAndForwardMode(byte esmClass) {
        return isMessagingMode(esmClass, SMPPConstant.ESMCLS_STORE_FORWARD);
    }

    /**
     * Messaging Mode.
     * 
     * @param esmClass
     * @return
     */
    public static final byte composeStoreAndForwardMode(byte esmClass) {
        return composeMessagingMode(esmClass, SMPPConstant.ESMCLS_STORE_FORWARD);
    }

    /**
     * Message Type.
     * 
     * @param esmClass
     * @return
     */
    public static final boolean isEsmeDeliveryAcknowledgement(byte esmClass) {
        return isMessageType(esmClass, SMPPConstant.ESMCLS_ESME_DELIVERY_ACK);
    }

    /**
     * Message Type.
     * 
     * @param esmClass
     * @return
     */
    public static final byte composeEsmeDeliveryAcknowledgement(byte esmClass) {
        return composeMessageType(esmClass,
                SMPPConstant.ESMCLS_ESME_DELIVERY_ACK);
    }

    /**
     * Message Type.
     * 
     * @param esmClass
     * @return
     */
    public static final boolean isEsmeManualAcknowledgement(byte esmClass) {
        return isMessageType(esmClass, SMPPConstant.ESMCLS_ESME_MANUAL_ACK);
    }

    /**
     * Message Type.
     * 
     * @param esmClass
     * @return
     */
    public static final byte composeEsmeManualAcknowledgement(byte esmClass) {
        return composeMessageType(esmClass, SMPPConstant.ESMCLS_ESME_MANUAL_ACK);
    }

    /**
     * SMSC Delivery Receipt.
     * 
     * @param registeredDelivery
     * @return
     */
    public static final boolean isSmscDelNotRequested(byte registeredDelivery) {
        return isSmscDeliveryReceipt(registeredDelivery,
                SMPPConstant.REGDEL_SMSC_NO);
    }

    public static final byte composeSmscDelReceiptNotRequested(
            byte registeredDelivery) {
        return composeSmscDelReceipt(registeredDelivery,
                SMPPConstant.REGDEL_SMSC_NO);
    }

    /**
     * SMSC Delivery Receipt.
     * 
     * @param registeredDelivery
     * @return
     */
    public static final boolean isSmscDelReceiptSuccessAndFailureRequested(
            byte registeredDelivery) {
        return isSmscDeliveryReceipt(registeredDelivery,
                SMPPConstant.REGDEL_SMSC_SUCCESS_FAILURE_REQUESTED);
    }

    public static final byte composeSmscDelReceiptSuccessAndFailureRequested(
            byte registeredDelivery) {
        return composeSmscDelReceipt(registeredDelivery,
                SMPPConstant.REGDEL_SMSC_SUCCESS_FAILURE_REQUESTED);
    }

    /**
     * SMSC Delivery Receipt.
     * 
     * @param registeredDelivery
     * @return
     */
    public static final boolean isSmscDelReceiptFailureRequested(
            byte registeredDelivery) {
        return isSmscDeliveryReceipt(registeredDelivery,
                SMPPConstant.REGDEL_SMSC_FAILURE_REQUESTED);
    }

    public static final byte composeSmscDelReceiptFailureRequested(
            byte registeredDelivery) {
        return composeSmscDelReceipt(registeredDelivery,
                SMPPConstant.REGDEL_SMSC_FAILURE_REQUESTED);
    }
}
