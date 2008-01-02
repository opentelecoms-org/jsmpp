package org.jsmpp.bean;

/**
 * Used to request an SMSC delivery receipt and/or SME originated
 * acknowledgments.
 * 
 * @author uudashr
 * 
 */
public enum SMSCDeliveryReceipt {
    /**
     * No SMSC Delivery Receipt requested (default).
     */
    DEFAULT((byte)0x00), 
    /**
     * SMSC Delivery Receipt requested where final delivery outcome is delivery
     * success or failure.
     */
    SUCCESS_FAILURE((byte)0x01), 
    /**
     * SMSC Delivery Receipt requested where the final delivery outcome is
     * delivery failure.
     */
    SUCCESS((byte)0x02);

    public static final byte CLEAR_BYTE = (byte)0xfc; // 11111100
    public static final byte MASK_BYTE = 0x03; // 00000011

    private final byte value;

    private SMSCDeliveryReceipt(byte value) {
        this.value = value;
    }

    public byte value() {
        return value;
    }

    public boolean containedIn(RegisteredDelivery registeredDelivery) {
        return containedIn(registeredDelivery.value());
    }
    
    public boolean containedIn(byte registeredDelivery) {
        return this.value == (byte)(registeredDelivery & MASK_BYTE);
    }
    
    public static byte compose(byte registeredDelivery, SMSCDeliveryReceipt smscDeliveryReceipt) {
        return (byte)(clean(registeredDelivery) | smscDeliveryReceipt.value());
    }
    
    public static byte clean(byte registeredDelivery) {
        return (byte) (registeredDelivery & CLEAR_BYTE);
    }
}
