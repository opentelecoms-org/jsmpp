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
 * This operation is used by an ESME to submit a short message to the MC for onward
 * transmission to a specified short message entity (SME).
 *
 * @author uudashr
 */
public class SubmitSm extends MessageRequest {

    public SubmitSm() {
        super();
    }

    /**
     * Check if the Messaging Mode is Default Mode
     *
     * @return {@code true} if the Messaging Mode is default mode (e.g. Store and Forward)
     */
    public boolean isDefaultMode() {
        return isDefaultMode(esmClass);
    }

    /**
     * Set the Messaging Mode to default mode (Store and Forward)
     */
    public void setDefaultMode() {
        esmClass = composeDefaultMode(esmClass);
    }

    /**
     * Check if the Messaging Mode is Datagram Mode
     *
     * @return {@code true} if the Messaging Mode is Datagram Mode
     */
    public boolean isDatagramMode() {
        return isDatagramMode(esmClass);
    }

    /**
     * Set the Messaging Mode to Datagram Mode.
     */
    public void setDatagramMode() {
        esmClass = composeDatagramMode(esmClass);
    }

    /**
     * Check if the Messaging Mode is Forward (i.e. Transaction) Mode.
     * 
     * @return {@code true} if the Messaging Mode is Forward (i.e. Transaction) Mode
     */
    public boolean isForwardMode() {
        return isForwardMode(esmClass);
    }

    /**
     * Set the Messaging Mode to Forward mode.
     */
    public void setForwardMode() {
        esmClass = composeForwardMode(esmClass);
    }

    /**
     * Check if the Messaging Mode is Store and Forward Mode.
     * 
     * @return {@code true} if the Messaging Mode is Store and Forward Mode.
     */
    public boolean isStoreAndForwardMode() {
        return isStoreAndForwardMode(esmClass);
    }

    /**
     * Set the Messaging Mode to Store and Forward Mode.
     * <p>
     * Use to select Store and Forward mode if default MC Mode is non Store and Forward.
     */
    public void setStoreAndForwardMode() {
        composeStoreAndForwardMode(esmClass);
    }

    /**
     * Check if the Message Type contains ESME Delivery Acknowledgement
     * 
     * @return {@code true} if the Message Type contains ESME Delivery Acknowledgement
     */
    public boolean isEsmeDeliveryAcknowledgement() {
        return isEsmeDeliveryAcknowledgement(esmClass);
    }

    /**
     * Set the Message Type to ESME Delivery Acknowledgement.
     */
    public void setEsmeDeliveryAcknowledgement() {
        esmClass = composeEsmeDeliveryAcknowledgement(esmClass);
    }

    /**
     * Check if the Message Type contains ESME Manual/User Acknowledgement
     * 
     * @return {@code true} if the Message Type contains ESME Manual/User Acknowledgement
     */
    public boolean isEsmeManualAcknowledgement() {
        return isEsmeManualAcknowledgement(esmClass);
    }

    /**
     * Set the Message Type to ESME Manual/User Acknowledgement
     */
    public void setEsmeManualAcknowledgement() {
        esmClass = composeEsmeManualAcknowledgement(esmClass);
    }

    /**
     * Check if the Registered Delivery contains No MC Delivery Receipt requested
     * 
     * @return {@code true} if the MC Delivery Receipt contains No MC Delivery Receipt requested
     */
    public boolean isSmscDelReceiptNotRequested() {
        return isSmscDelNotRequested(registeredDelivery);
    }

    /**
     * Set the Registered Delivery to No MC Delivery Receipt requested
     */
    public void setSmscDelReceiptNotRequested() {
        registeredDelivery = composeSmscDelReceiptNotRequested(registeredDelivery);
    }

    /**
     * Check if the Registered Delivery contains MC Delivery Receipt requested where final delivery outcome is delivery success or failure
     * 
     * @return {@code true} if an MC Delivery Receipt requested where final delivery outcome is delivery success or failure
     */
    public boolean isSmscDelReceiptSuccessAndFailureRequested() {
        return isSmscDelReceiptSuccessAndFailureRequested(registeredDelivery);
    }

    /**
     * Set the MC Delivery Receipt requested where final delivery outcome is delivery success or failure
     */
    public void setSmscDelReceiptSuccessAndFailureRequested() {
        registeredDelivery = composeSmscDelReceiptSuccessAndFailureRequested(registeredDelivery);
    }

    /**
     * Check if the Registered Delivery contains MC Delivery Receipt requested where the final delivery outcome is delivery failure
     * 
     * @return {@code true} if an MC Delivery Receipt requested where the final delivery outcome is delivery failure
     */
    public boolean isSmscDelReceiptFailureRequested() {
        return isSmscDelReceiptFailureRequested(registeredDelivery);
    }

    /**
     * Set the MC Delivery Receipt requested where the final delivery outcome is delivery failure.
     * This includes scenarios where the message was cancelled via the cancel_sm operation.
     */
    public void setSmscDelReceiptFailureRequested() {
        registeredDelivery = composeSmscDelReceiptFailureRequested(registeredDelivery);
    }

    /**
     * Check if the Messaging Mode is the Default MC Mode (Store and Forward).
     * 
     * @param esmClass the ESM class to examine
     * @return {@code true} if the Message Mode is the default mode
     * @deprecated see {@link MessageMode#containedIn(byte)}
     */
    @Deprecated
    public static final boolean isDefaultMode(byte esmClass) {
        return isMessagingMode(esmClass, SMPPConstant.ESMCLS_DEFAULT_MODE);
    }

    /**
     * Set the Messaging Mode to default MC mode (Store and Forward)
     * 
     * @param esmClass the original ESM class
     * @return the modified ESM class
     * @deprecated use {@link MessageMode#compose(byte, MessageMode)}
     */
    @Deprecated
    public static final byte composeDefaultMode(byte esmClass) {
        return composeMessagingMode(esmClass, SMPPConstant.ESMCLS_DEFAULT_MODE);
    }

    /**
     * Check if the Messaging Mode is the Datagram mode in the ESM class.
     * 
     * @param esmClass the ESM class to examine
     * @return {@code true} if the Message Mode is the Datagram mode
     */
    public static final boolean isDatagramMode(byte esmClass) {
        return isMessagingMode(esmClass, SMPPConstant.ESMCLS_DATAGRAM_MODE);
    }

    /**
     * Set the Messaging Mode to the Datagram mode in the ESM class.
     * 
     * @param esmClass the original ESM class
     * @return the modified ESM class
     */
    public static final byte composeDatagramMode(byte esmClass) {
        return composeMessagingMode(esmClass, SMPPConstant.ESMCLS_DATAGRAM_MODE);
    }

    /**
     * Check if the Messaging Mode is the Forward (Transaction) mode in the ESM class.
     * 
     * @param esmClass the ESM class to examine
     * @return {@code true} if the Message Mode is the Forward (Transaction) mode
     */
    public static final boolean isForwardMode(byte esmClass) {
        return isMessagingMode(esmClass, SMPPConstant.ESMCLS_FORWARD_MODE);
    }

    /**
     * Set the Messaging Mode to the Forward (Transaction) mode in the ESM class.
     *
     * @param esmClass the original ESM class
     * @return the modified ESM class
     */
    public static final byte composeForwardMode(byte esmClass) {
        return composeMessagingMode(esmClass, SMPPConstant.ESMCLS_FORWARD_MODE);
    }

    /**
     * Check if the Messaging Mode is the Store and Forward mode for the ESM class.
     *
     * @param esmClass the ESM class to examine
     * @return {@code true} if the Message Mode is the Store and Forward mode
     */
    public static final boolean isStoreAndForwardMode(byte esmClass) {
        return isMessagingMode(esmClass, SMPPConstant.ESMCLS_STORE_FORWARD);
    }

    /**
     * Set the Messaging Mode to the Store and Forward mode in the ESM class.
     *
     * @param esmClass the original ESM class
     * @return the modified ESM class
     */
    public static final byte composeStoreAndForwardMode(byte esmClass) {
        return composeMessagingMode(esmClass, SMPPConstant.ESMCLS_STORE_FORWARD);
    }

    /**
     * Check if the Message Type contains Short Message contains Delivery Acknowledgement in the ESM class.
     *
     * @param esmClass the ESM class to examine
     * @return {@code true} if the Message Type is Short Message contains Delivery Acknowledgement
     */
    public static final boolean isEsmeDeliveryAcknowledgement(byte esmClass) {
        return isMessageType(esmClass, SMPPConstant.ESMCLS_ESME_DELIVERY_ACK);
    }

    /**
     * Set the Message Type to Short Message contains Delivery Acknowledgement in the ESM class.
     *
     * @param esmClass the original ESM class
     * @return the modified ESM class
     */
    public static final byte composeEsmeDeliveryAcknowledgement(byte esmClass) {
        return composeMessageType(esmClass, SMPPConstant.ESMCLS_ESME_DELIVERY_ACK);
    }

    /**
     * Check if the Message Type contains Short Message contains Manual/User Acknowledgement in the ESM class.
     *
     * @param esmClass the ESM class to examine
     * @return {@code true} if the Message Type is Short Message contains Manual/User Acknowledgement
     */
    public static final boolean isEsmeManualAcknowledgement(byte esmClass) {
        return isMessageType(esmClass, SMPPConstant.ESMCLS_ESME_MANUAL_ACK);
    }

    /**
     * Set the Message Type to Short Message contains Manual/User Acknowledgement in the ESM class.
     *
     * @param esmClass the original ESM class
     * @return the modified ESM class
     */
    public static final byte composeEsmeManualAcknowledgement(byte esmClass) {
        return composeMessageType(esmClass, SMPPConstant.ESMCLS_ESME_MANUAL_ACK);
    }

    /**
     * Check if No MC Delivery Receipt requested (default) was set in the Registered Delivery.
     * 
     * @param registeredDelivery the Registered Delivery byte to examine
     * @return {@code true} if the Registered Delivery is No MC Delivery Receipt requested (default)
     */
    public static final boolean isSmscDelNotRequested(byte registeredDelivery) {
        return isSmscDeliveryReceipt(registeredDelivery, SMPPConstant.REGDEL_SMSC_NO);
    }

    /**
     * Set the No MC Delivery Receipt requested (default) in the Registered Delivery.
     *
     * @param registeredDelivery the original Registered Delivery byte
     * @return the modified Registered Delivery
     */
    public static final byte composeSmscDelReceiptNotRequested(byte registeredDelivery) {
        return composeSmscDelReceipt(registeredDelivery, SMPPConstant.REGDEL_SMSC_NO);
    }

    /**
     * Check if MC Delivery Receipt requested where final delivery outcome is delivery success or failure is set in the Registered Delivery.
     * 
     * @param registeredDelivery the Registered Delivery byte to examine
     * @return {@code true} if the Registered Delivery is MC Delivery Receipt requested where final delivery outcome is delivery success or failure
     */
    public static final boolean isSmscDelReceiptSuccessAndFailureRequested(byte registeredDelivery) {
        return isSmscDeliveryReceipt(registeredDelivery, SMPPConstant.REGDEL_SMSC_SUCCESS_FAILURE_REQUESTED);
    }

    /**
     * Set the MC Delivery Receipt requested where the final delivery outcome is delivery failure in the Registered Delivery.
     *
     * @param registeredDelivery the original Registered Delivery byte
     * @return the modified Registered Delivery
     */
    public static final byte composeSmscDelReceiptSuccessAndFailureRequested(byte registeredDelivery) {
        return composeSmscDelReceipt(registeredDelivery, SMPPConstant.REGDEL_SMSC_SUCCESS_FAILURE_REQUESTED);
    }

    /**
     * Check if MC Delivery Receipt requested where the final delivery outcome is delivery failure is set in the Registered Delivery.
     * 
     * @param registeredDelivery the Registered Delivery byte
     * @return {@code true} if the MC Delivery Receipt requested where the final delivery outcome is delivery failure
     */
    public static final boolean isSmscDelReceiptFailureRequested(byte registeredDelivery) {
        return isSmscDeliveryReceipt(registeredDelivery, SMPPConstant.REGDEL_SMSC_FAILURE_REQUESTED);
    }

    /**
     * Set the MC Delivery Receipt requested where the final delivery outcome is delivery failure in the Registered Delivery.
     *
     * @param registeredDelivery the original Registered Delivery byte
     * @return the modified Registered Delivery
     */
    public static final byte composeSmscDelReceiptFailureRequested(byte registeredDelivery) {
        return composeSmscDelReceipt(registeredDelivery, SMPPConstant.REGDEL_SMSC_FAILURE_REQUESTED);
    }
}
