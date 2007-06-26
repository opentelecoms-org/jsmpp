package org.jsmpp.util;

/**
 * @author uudashr
 * @version 1.0
 * 
 */
public enum DeliveryReceiptState {
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
    REJECTD(7);

    private int value;

    private DeliveryReceiptState(int value) {
        this.value = value;
    }

    public static DeliveryReceiptState getByName(String name) {
        return valueOf(DeliveryReceiptState.class, name);
    }

    public static DeliveryReceiptState valueOf(int value)
            throws IllegalArgumentException {
        for (DeliveryReceiptState item : values()) {
            if (item.value() == value) {
                return item;
            }
        }
        throw new IllegalArgumentException(
                "No enum const DeliveryReceiptState with value " + value);
    }

    public int value() {
        return value;
    }
}
