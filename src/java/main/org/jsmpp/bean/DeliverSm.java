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
import org.jsmpp.util.DefaultDecomposer;
import org.jsmpp.util.InvalidDeliveryReceiptException;

/**
 * @author uudashr
 *
 */
public class DeliverSm extends MessageRequest {
    
	public DeliverSm() {
		super();
	}
	
	/**
     * Get the short message as {@link DeliveryReceipt}. This method will be
     * valid if the parsed short message valid and Message Type (esm_class)
     * contains SMSC Delivery Receipt.
     * 
     * @return the {@link DeliveryReceipt}.
     * @throws InvalidDeliveryReceiptException
     */
	public DeliveryReceipt getShortMessageAsDeliveryReceipt()
            throws InvalidDeliveryReceiptException {
	    if (MessageType.SMSC_DEL_RECEIPT.containedIn(getEsmClass())) {
	        return DefaultDecomposer.getInstance().
                    deliveryReceipt(getShortMessage());
	    } else {
	        throw new InvalidDeliveryReceiptException("deliver_sm is not a Delivery Receipt since ems_class value = " + getEsmClass());
	    }
	    
	}
	
	/**
	 * Message Type.
	 * @return
	 */
	public boolean isSmscDeliveryReceipt() {
		return isSmscDeliveryReceipt(esmClass);
	}
	
	/**
	 * Message Type.
	 * @param value
	 */
	public void setSmscDeliveryReceipt() {
		esmClass = composeSmscDeliveryReceipt(esmClass);
	}
	
	/**
	 * Message Type.
	 * @return
	 */
	public boolean isSmeManualAcknowledgment() {
		return isSmeManualAcknowledgment(esmClass);
	}
	
	/**
	 * Message Type.
	 */
	public void setSmeManualAcknowledgment() {
		esmClass = composeSmeManualAcknowledment(esmClass);
	}
	
	/**
	 * Message Type.
	 * @return
	 */
	public boolean isConversationAbort() {
		return isConversationAbort(esmClass);
	}
	
	/**
	 * Message Type.
	 */
	public void setConversationAbort() {
		esmClass = composeConversationAbort(esmClass);
	}
	
	/**
	 * Message Type.
	 * @return
	 */
	public boolean isIntermedietDeliveryNotification() {
		return isIntermedietDeliveryNotification(esmClass);
	}
	
	/**
	 * Message Type.
	 */
	public void setIntermedietDeliveryNotification() {
		esmClass = composeIntermedietDeliveryNotification(esmClass);
	}
	
	/**
	 * SME originated Acknowledgment.
	 * @return
	 */
	public boolean isSmeAckNotRequested() {
		return isSmeAckNotRequested(registeredDelivery);
	}
	
	/**
	 * SME originated Acknowledgment.
	 */
	public void setSmeAckNotRequested() {
		registeredDelivery = composeSmeAckNotRequested(registeredDelivery);
	}
	
	/**
	 * SME originated Acknowledgment.
	 * @return
	 */
	public boolean isSmeDeliveryAckRequested() {
		return isSmeDeliveryAckRequested(registeredDelivery);
	}
	
	/**
	 * SME originated Acknowledgment.
	 */
	public void setSmeDeliveryAckRequested() {
		registeredDelivery = composeSmeDeliveryAckRequested(registeredDelivery);
	}
	
	/**
	 * SME originated Acknowledgement.
	 * @return
	 */
	public boolean isSmeManualAckRequested() {
		return isSmeManualAckRequested(registeredDelivery);
	}
	
	/**
	 * 
	 * SME originated Acknowledgement.
	 */
	public void setSmeManualAckRequested() {
		registeredDelivery = composeSmeManualAckRequested(registeredDelivery);
	}
	
	/**
	 * SME originated Acknowledgement.
	 * @return
	 */
	public boolean isSmeDeliveryAndManualAckRequested() {
		return isSmeDeliveryAndManualAckRequested(registeredDelivery);
	}
	
	/**
	 * SME originated Acknowledgement.
	 */
	public void setSmeDeliveryAndManualAckRequested() {
		registeredDelivery = composeSmeDeliveryAndManualAckRequested(registeredDelivery);
	}
	
	/**
	 * Message Type.
	 * Return esm_class of deliver_sm or data_sm indicate delivery receipt or not.
	 * @param esmClass
	 * @return <tt>true</tt> if esmClass indicate delivery receipt
	 */
	public static final boolean isSmscDeliveryReceipt(byte esmClass) {
		return isMessageType(esmClass, SMPPConstant.ESMCLS_SMSC_DELIV_RECEIPT);
	}
	
	/**
	 * Message Type.
	 * Set esm_class as delivery receipt
	 * @param esmClass
	 * @return
	 */
	public static final byte composeSmscDeliveryReceipt(byte esmClass) {
		return composeMessageType(esmClass, SMPPConstant.ESMCLS_SMSC_DELIV_RECEIPT);
	}
	
	/**
	 * Message Type.
	 * @param esmClass
	 * @return
	 */
	public static final boolean isSmeDeliveryAcknowledgment(byte esmClass) {
		return isMessageType(esmClass, SMPPConstant.ESMCLS_SME_DELIV_ACK);
	}
	
	/**
	 * Message Type.
	 * @param esmClass
	 * @return
	 */
	public static final byte composeSmeDeliveryAcknowledment(byte esmClass) {
		return composeMessageType(esmClass, SMPPConstant.ESMCLS_SME_DELIV_ACK);
	}
	
	/**
	 * Message Type.
	 * @param esmClass
	 * @return
	 */
	public static final boolean isSmeManualAcknowledgment(byte esmClass) {
		return isMessageType(esmClass, SMPPConstant.ESMCLS_SME_MANUAL_ACK);
	}
	
	/**
	 * Message Type.
	 * @param esmClass
	 * @return
	 */
	public static final byte composeSmeManualAcknowledment(byte esmClass) {
		return composeMessageType(esmClass, SMPPConstant.ESMCLS_SME_MANUAL_ACK);
	}
	
	/**
	 * Message Type.
	 * @param esmClass
	 * @return
	 */
	public static final boolean isConversationAbort(byte esmClass) {
		return isMessageType(esmClass, SMPPConstant.ESMCLS_CONV_ABORT);
	}
	
	/**
	 * Message Type.
	 * @param esmClass
	 * @return
	 */
	public static final byte composeConversationAbort(byte esmClass) {
		return composeMessageType(esmClass, SMPPConstant.ESMCLS_CONV_ABORT);
	}
	
	/**
	 * Message Type.
	 * @param esmClass
	 * @return
	 */
	public static final boolean isIntermedietDeliveryNotification(byte esmClass) {
		return isMessageType(esmClass, SMPPConstant.ESMCLS_INTRMD_DELIV_NOTIF);
	}
	
	/**
	 * Message Type.
	 * @param esmClass
	 * @return
	 */
	public static final byte composeIntermedietDeliveryNotification(byte esmClass) {
		return composeMessageType(esmClass, SMPPConstant.ESMCLS_INTRMD_DELIV_NOTIF);
	}
	
	/*
	 * SME originated Acknowledgement.
	 */
	
	/**
	 * SME originated Acknowledgement.
	 * @param registeredDeliery
	 * @return
	 */
	public static final boolean isSmeAckNotRequested(byte registeredDeliery) {
		return isSmeAck(registeredDeliery, SMPPConstant.REGDEL_SME_ACK_NO);
	}
	
	/**
	 * SME originated Acknowledgment.
	 * @param registeredDelivery
	 * @return
	 */
	public static final byte composeSmeAckNotRequested(byte registeredDelivery) {
		return composeSmeAck(registeredDelivery, SMPPConstant.REGDEL_SME_ACK_NO);
	}
	
	/**
	 * SME originated Acknowledgment.
	 * @param registeredDeliery
	 * @return
	 */
	public static final boolean isSmeDeliveryAckRequested(byte registeredDeliery) {
		return isSmeAck(registeredDeliery, SMPPConstant.REGDEL_SME_DELIVERY_ACK_REQUESTED);
	}
	
	/**
	 * SME originated Acknowledgment.
	 * @param registeredDelivery
	 * @return
	 */
	public static final byte composeSmeDeliveryAckRequested(byte registeredDelivery) {
		return composeSmeAck(registeredDelivery, SMPPConstant.REGDEL_SME_DELIVERY_ACK_REQUESTED);
	}
	
	/**
	 * SME originated Acknowledgment.
	 * @param registeredDelivery
	 * @return
	 */
	public static final boolean isSmeManualAckRequested(byte registeredDelivery) {
		return isSmeAck(registeredDelivery, SMPPConstant.REGDEL_SME_MANUAL_ACK_REQUESTED);
	}
	
	/**
	 * SME originated Acknowledgment.
	 * @param registeredDelivery
	 * @return
	 */
	public static final byte composeSmeManualAckRequested(byte registeredDelivery) {
		return composeSmeAck(registeredDelivery, SMPPConstant.REGDEL_SME_MANUAL_ACK_REQUESTED);
	}
	
	/**
	 * SME originated Acknowledgment.
	 * @param registeredDelivery
	 * @return
	 */
	public static final boolean isSmeDeliveryAndManualAckRequested(byte registeredDelivery) {
		return isSmeAck(registeredDelivery, SMPPConstant.REGDEL_SME_DELIVERY_MANUAL_ACK_REQUESTED);
	}
	
	/**
	 * SME originated Acknowledgment.
	 * @param registeredDelivery
	 * @return
	 */
	public static final byte composeSmeDeliveryAndManualAckRequested(byte registeredDelivery) {
		return composeSmeAck(registeredDelivery, SMPPConstant.REGDEL_SME_DELIVERY_MANUAL_ACK_REQUESTED);
	}
}
