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
        return isDefaultMode(_esmClass);
    }

    /**
     * Messaging Mode.
     */
    public void setDefaultMode() {
        _esmClass = composeDefaultMode(_esmClass);
    }

    /**
     * Messaging Mode.
     * 
     * @return
     */
    public boolean isDatagramMode() {
        return isDatagramMode(_esmClass);
    }

    /**
     * Messaging Mode.
     */
    public void setDatagramMode() {
        _esmClass = composeDatagramMode(_esmClass);
    }

    /**
     * Messaging Mode.
     * 
     * @return
     */
    public boolean isForwardMode() {
        return isForwardMode(_esmClass);
    }

    /**
     * Messaging Mode.
     */
    public void setForwardMode() {
        _esmClass = composeForwardMode(_esmClass);
    }

    /**
     * Messaging Mode.
     * 
     * @return
     */
    public boolean isStoreAndForwardMode() {
        return isStoreAndForwardMode(_esmClass);
    }

    /**
     * Messaging Mode.
     */
    public void setStoreAndForwardMode() {
        composeStoreAndForwardMode(_esmClass);
    }

    /**
     * Message Type.
     * 
     * @return
     */
    public boolean isEsmeDeliveryAcknowledgement() {
        return isEsmeDeliveryAcknowledgement(_esmClass);
    }

    /**
     * Message Type.
     */
    public void setEsmeDelivertAcknowledgement() {
        _esmClass = composeEsmeDeliveryAcknowledgement(_esmClass);
    }

    /**
     * Message Type.
     * 
     * @return
     */
    public boolean isEsmeManualAcknowledgement() {
        return isEsmeManualAcknowledgement(_esmClass);
    }

    /**
     * Message Type.
     */
    public void setEsmeManualAcknowledgement() {
        _esmClass = composeEsmeManualAcknowledgement(_esmClass);
    }

    /**
     * SMSC Delivery Receipt.
     * 
     * @return
     */
    public boolean isSmscDelReceiptNotRequested() {
        return isSmscDelNotRequested(_registeredDelivery);
    }

    /**
     * SMSC Delivery Receipt.
     */
    public void setSmscDelReceiptNotRequested() {
        _registeredDelivery = composeSmscDelReceiptNotRequested(_registeredDelivery);
    }

    /**
     * SMSC Delivery Receipt.
     * 
     * @return
     */
    public boolean isSmscDelReceiptSuccessAndFailureRequested() {
        return isSmscDelReceiptSuccessAndFailureRequested(_registeredDelivery);
    }

    /**
     * SMSC Delivery Receipt.
     */
    public void setSmscDelReceiptSuccessAndFailureRequested() {
        _registeredDelivery = composeSmscDelReceiptSuccessAndFailureRequested(_registeredDelivery);
    }

    /**
     * SMSC Delivery Receipt.
     * 
     * @return
     */
    public boolean isSmscDelReceiptFailureRequested() {
        return isSmscDelReceiptFailureRequested(_registeredDelivery);
    }

    /**
     * SMSC Delivery Receipt.
     */
    public void setSmscDelReceiptFailureRequested() {
        _registeredDelivery = composeSmscDelReceiptFailureRequested(_registeredDelivery);
    }

    /**
     * Messaging Mode.
     * 
     * @param esmClass
     * @return
     */
    public static final boolean isDefaultMode(byte esmClass) {
        return isMessagingMode(esmClass, SMPPConstant.ESMCLS_DEFAULT_MODE);
    }

    /**
     * Messaging Mode.
     * 
     * @param esmClass
     * @return
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
