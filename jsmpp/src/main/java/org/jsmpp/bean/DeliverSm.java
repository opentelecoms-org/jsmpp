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
import org.jsmpp.util.InvalidDeliveryReceiptException;

/**
 * The deliver_sm is issued by the MC to send a message to an ESME.
 * Using this command, the MC may route a short message to the ESME for delivery.
 *
 * @author uudashr
 */
public class DeliverSm extends MessageRequest {

	private String id;

	public DeliverSm() {
		super();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
     * Get the short message as {@link DeliveryReceipt}. This method will be
     * valid if the parsed short message valid and Message Type (esm_class)
     * contains SMSC Delivery Receipt.
     * 
     * @return the {@link DeliveryReceipt}.
     * @throws InvalidDeliveryReceiptException if there is an error found while parsing delivery receipt.
     */
	public DeliveryReceipt getShortMessageAsDeliveryReceipt()
            throws InvalidDeliveryReceiptException {
	    return getDeliveryReceipt(DefaultDeliveryReceiptStripper.getInstance());
	}
	
	/**
	 * Get delivery receipt based on specified strategy/stripper.
	 * 
	 * @param stripper An implementation of the {@link DeliveryReceiptStrip} stripper.
   * @param <T> The class returned by the stripper
	 * @return the delivery receipt as an instance of T.
	 * @throws InvalidDeliveryReceiptException if there is an error found while parsing the delivery receipt.
	 */
	public <T> T getDeliveryReceipt(DeliveryReceiptStrip<T> stripper)
            throws InvalidDeliveryReceiptException {
	    return stripper.strip(this);
	}
	
	/**
	 * Check if the ESM class Message Type bits indicates a MC Delivery Receipt
	 *
	 * @return {@code true} if the ESM class Message Type bits indicates an SMSC Delivery Receipt
	 */
	public boolean isSmscDeliveryReceipt() {
		return isSmscDeliveryReceipt(esmClass);
	}
	
	/**
	 * Set the MC Delivery Receipt in the ESM class Message Type bits.
	 */
	public void setSmscDeliveryReceipt() {
		esmClass = composeSmscDeliveryReceipt(esmClass);
	}
	
	/**
	 * Check if the ESM class Message Type bits indicates an SME Manual Acknowledgment.
	 *
	 * @return {@code true} if the ESM class Message Type bits indicates an SME Manual/User Acknowledgment
	 */
	public boolean isSmeManualAcknowledgment() {
		return isSmeManualAcknowledgment(esmClass);
	}
	
	/**
	 * Set the SME Manual/User Acknowledgement in the ESM class Message Type bits.
	 */
	public void setSmeManualAcknowledgment() {
		esmClass = composeSmeManualAcknowledgment(esmClass);
	}
	
	/**
	 * Check if the ESM class indicates an Conversation Abort (ANSI-41 Specific).
	 *
	 * @return {@code true} if the ESM class indicates an Conversation Abort
	 */
	public boolean isConversationAbort() {
		return isConversationAbort(esmClass);
	}
	
	/**
	 * Set the Conversation Abort in the ESM class (ANSI-41 Specific).
	 */
	public void setConversationAbort() {
		esmClass = composeConversationAbort(esmClass);
	}
	
	/**
	 * Check if the ESM class Message Type bits indicates an Intermediate Delivery Notification.
	 *
	 * @return {@code true} is the ESM class Message Type indicates an Intermediate Delivery Notification
	 */
	public boolean isIntermediateDeliveryNotification() {
		return isIntermediateDeliveryNotification(esmClass);
	}
	
	/**
	 * Set the Intermediate Delivery Notification in the ESM class Message Type bits.
	 */
	public void setIntermediateDeliveryNotification() {
		esmClass = composeIntermediateDeliveryNotification(esmClass);
	}
	
	/**
	 * Check if the Registered Delivery SME originated Acknowledgement bits indicates no recipient SME acknowledgment requested.
	 *
	 * @return {@code true} is the Registered Delivery SME originated Acknowledgement bits indicates no recipient SME acknowledgment requested
	 */
	public boolean isSmeAckNotRequested() {
		return isSmeAckNotRequested(registeredDelivery);
	}
	
	/**
	 * Set no recipient SME acknowledgment requested in the Registered Delivery SME originated Acknowledgement bits.
	 */
	public void setSmeAckNotRequested() {
		registeredDelivery = composeSmeAckNotRequested(registeredDelivery);
	}
	
	/**
	 * Check if the Registered Delivery SME originated Acknowledgement bits indicates SME Delivery Acknowledgement requested.
	 *
	 * @return {@code true} is the Registered Delivery SME originated Acknowledgement bits indicates SME Delivery Acknowledgement requested.
	 */
	public boolean isSmeDeliveryAckRequested() {
		return isSmeDeliveryAckRequested(registeredDelivery);
	}
	
	/**
	 * Set SME Delivery Acknowledgement requested in the Registered Delivery SME originated Acknowledgement bits.
	 */
	public void setSmeDeliveryAckRequested() {
		registeredDelivery = composeSmeDeliveryAckRequested(registeredDelivery);
	}
	
	/**
	 * Check if the Registered Delivery SME originated Acknowledgement bits indicates SME Manual/User Acknowledgment requested.
	 *
	 * @return {@code true} is the Registered Delivery SME originated Acknowledgement bits indicates SME Manual/User Acknowledgment requested.
	 */
	public boolean isSmeManualAckRequested() {
		return isSmeManualAckRequested(registeredDelivery);
	}
	
	/**
	 * Set SME Manual/User Acknowledgment requested in the Registered Delivery SME originated Acknowledgement bits.
	 */
	public void setSmeManualAckRequested() {
		registeredDelivery = composeSmeManualAckRequested(registeredDelivery);
	}
	
	/**
	 * Check if the Registered Delivery SME originated Acknowledgement bits indicates SME Delivery and Manual/User Acknowledgment requested.
	 *
	 * @return {@code true} is the Registered Delivery SME originated Acknowledgement bits indicates SME Delivery and Manual/User Acknowledgment requested.
	 */
	public boolean isSmeDeliveryAndManualAckRequested() {
		return isSmeDeliveryAndManualAckRequested(registeredDelivery);
	}
	
	/**
	 * Set SME Delivery and Manual/User Acknowledgment requested in the Registered Delivery SME originated Acknowledgement bits.
	 */
	public void setSmeDeliveryAndManualAckRequested() {
		registeredDelivery = composeSmeDeliveryAndManualAckRequested(registeredDelivery);
	}
	
	/**
	 * Check if the ESM class Message Type bits indicates an MC Delivery Receipt.
	 *
	 * @param esmClass the ESM class to examine
	 * @return {@code true} if esmClass Message Type indicates an MC delivery receipt
	 */
	public static final boolean isSmscDeliveryReceipt(byte esmClass) {
		return isMessageType(esmClass, SMPPConstant.ESMCLS_SMSC_DELIV_RECEIPT);
	}
	
	/**
	 * Set MC Delivery Receipt in the ESM class Message Type bits.
	 *
	 * @param esmClass the original ESM class
	 * @return the modified ESM class
	 */
	public static final byte composeSmscDeliveryReceipt(byte esmClass) {
		return composeMessageType(esmClass, SMPPConstant.ESMCLS_SMSC_DELIV_RECEIPT);
	}
	
	/**
	 * Check if the ESM class Message Type bits indicates an SME Delivery Acknowledgment.
	 *
	 * @param esmClass the original ESM class
	 * @return {@code true} if esmClass Message Type indicates an SME Delivery Acknowledgment.
	 */
	public static final boolean isSmeDeliveryAcknowledgment(byte esmClass) {
		return isMessageType(esmClass, SMPPConstant.ESMCLS_SME_DELIV_ACK);
	}
	
	/**
	 * Set SME Delivery Acknowledgment in the ESM class Message Type bits.
	 *
	 * @param esmClass the original ESM class
	 * @return the modified ESM class
	 */
	public static final byte composeSmeDeliveryAcknowledgment(byte esmClass) {
		return composeMessageType(esmClass, SMPPConstant.ESMCLS_SME_DELIV_ACK);
	}
	
	/**
	 * Check if the ESM class Message Type bits indicates a Manual/User Acknowledgement
	 *
	 * @param esmClass the ESM class to examine
	 * @return {@code true} if the ESM class Message Type bits indicates a SME Manual/User Acknowledgement
	 */
	public static final boolean isSmeManualAcknowledgment(byte esmClass) {
		return isMessageType(esmClass, SMPPConstant.ESMCLS_SME_MANUAL_ACK);
	}
	
	/**
	 * Set the SME Manual/User Acknowledgement in the ESM class Message Type bits.
	 *
	 * @param esmClass the original ESM class
	 * @return the modified ESM class
	 */
	public static final byte composeSmeManualAcknowledgment(byte esmClass) {
		return composeMessageType(esmClass, SMPPConstant.ESMCLS_SME_MANUAL_ACK);
	}
	
	/**
	 * Check if the ESM class ANSI-41 Specific bits indicates a conversion abort (Korean CDMA)
	 *
	 * @param esmClass the ESM class to examine
	 * @return {@code true} if the ESM class ANSI-41 Specific bits indicates a conversion abort
	 */
	public static final boolean isConversationAbort(byte esmClass) {
		return isMessageType(esmClass, SMPPConstant.ESMCLS_CONV_ABORT);
	}
	
	/**
	 * Set the conversation abort (Korean CDMA) in the ESM class ANSI-41 Specific.
	 *
	 * @param esmClass the original ESM class
	 * @return the modified ESM class
	 */
	public static final byte composeConversationAbort(byte esmClass) {
		return composeMessageType(esmClass, SMPPConstant.ESMCLS_CONV_ABORT);
	}
	
	/**
	 * Check the ESM class Message Type if it indicates an intermediate delivery notification
	 *
	 * @param esmClass the original ESM class
	 * @return {@code true} if the ESM class Message Type indicates an intermediate delivery notification
	 */
	public static final boolean isIntermediateDeliveryNotification(byte esmClass) {
		return isMessageType(esmClass, SMPPConstant.ESMCLS_INTRMD_DELIV_NOTIF);
	}
	
	/**
	 * Set the Intermediate Delivery Notification in the ESM class Message Type
	 *
	 * @param esmClass the original ESM class
	 * @return the modified ESM class
	 */
	public static final byte composeIntermediateDeliveryNotification(byte esmClass) {
		return composeMessageType(esmClass, SMPPConstant.ESMCLS_INTRMD_DELIV_NOTIF);
	}

	/**
	 * Check the Registered Delivery SME originated Acknowledgement bits if it indicates SME Manual/User Acknowledgment requested
	 *
	 * @param registeredDelivery The registered delivery parameter
	 * @return {@code true} if the Registered Delivery SME originated Acknowledgement bits indicates SME Manual/User Acknowledgment requested
	 */
	public static final boolean isSmeAckNotRequested(byte registeredDelivery) {
		return isSmeAck(registeredDelivery, SMPPConstant.REGDEL_SME_ACK_NO);
	}

	/**
	 * Set the No recipient SME acknowledgment requested in the Registered Delivery SME originated Acknowledgement bits
	 *
	 * @param registeredDelivery the original Registered Delivery
	 * @return the modified Registered Delivery
	 */
	public static final byte composeSmeAckNotRequested(byte registeredDelivery) {
		return composeSmeAck(registeredDelivery, SMPPConstant.REGDEL_SME_ACK_NO);
	}
	
	/**
	 * Check the Registered Delivery SME originated Acknowledgement bits if it indicates SME Delivery Acknowledgement requested.
	 *
	 * @param registeredDelivery the original Registered Delivery
	 * @return {@code true} if the Registered Delivery SME originated Acknowledgement bits indicates SME Delivery Acknowledgement requested
	 */
	public static final boolean isSmeDeliveryAckRequested(byte registeredDelivery) {
		return isSmeAck(registeredDelivery, SMPPConstant.REGDEL_SME_DELIVERY_ACK_REQUESTED);
	}
	
	/**
	 * Set the SME Delivery Acknowledgement requested in the Registered Delivery SME originated Acknowledgement bits.
	 *
	 * @param registeredDelivery the original Registered Delivery
	 * @return the modified Registered Delivery
	 */
	public static final byte composeSmeDeliveryAckRequested(byte registeredDelivery) {
		return composeSmeAck(registeredDelivery, SMPPConstant.REGDEL_SME_DELIVERY_ACK_REQUESTED);
	}
	
	/**
	 * Check the Registered Delivery SME originated Acknowledgement bits if it indicates SME Manual/User Acknowledgment requested.
	 *
	 * @param registeredDelivery the original Registered Delivery
	 * @return {@code true} if the Registered Delivery SME originated Acknowledgement bits indicates SME Manual/User Acknowledgment requested
	 */
	public static final boolean isSmeManualAckRequested(byte registeredDelivery) {
		return isSmeAck(registeredDelivery, SMPPConstant.REGDEL_SME_MANUAL_ACK_REQUESTED);
	}
	
	/**
	 * Set the SME Manual/User Acknowledgment requested in the Registered Delivery SME originated Acknowledgement bits.
	 *
	 * @param registeredDelivery the original Registered Delivery
	 * @return the modified Registered Delivery
	 */
	public static final byte composeSmeManualAckRequested(byte registeredDelivery) {
		return composeSmeAck(registeredDelivery, SMPPConstant.REGDEL_SME_MANUAL_ACK_REQUESTED);
	}
	
	/**
	 * Check the Registered Delivery SME originated Acknowledgement bits if it indicates SME Delivery and Manual/User Acknowledgment requested bits.
	 *
	 * @param registeredDelivery the Registered Delivery to examine
	 * @return {@code true} if the Registered Delivery SME originated Acknowledgement bits indicates SME Manual/User Acknowledgment requested
	 */
	public static final boolean isSmeDeliveryAndManualAckRequested(byte registeredDelivery) {
		return isSmeAck(registeredDelivery, SMPPConstant.REGDEL_SME_DELIVERY_MANUAL_ACK_REQUESTED);
	}
	
	/**
	 * Set the SME Delivery and Manual/User Acknowledgment requested in the Registered Delivery SME originated Acknowledgement bits.
	 *
	 * @param registeredDelivery the original Registered Delivery
	 * @return the modified Registered Delivery
	 */
	public static final byte composeSmeDeliveryAndManualAckRequested(byte registeredDelivery) {
		return composeSmeAck(registeredDelivery, SMPPConstant.REGDEL_SME_DELIVERY_MANUAL_ACK_REQUESTED);
	}
}
