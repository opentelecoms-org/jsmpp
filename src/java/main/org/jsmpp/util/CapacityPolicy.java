package org.jsmpp.util;

/**
 * It's capacity policy that ensure the new capacity is save for accommodate the
 * new item with new capacity.
 * 
 * @author uudashr
 * 
 */
public interface CapacityPolicy {
    
    /**
     * Ensuring the currentCapacity is save to accommodate new items that
     * totally defined as requiredCapacity.
     * 
     * @param requiredCapacity is the required capacity.
     * @param currentCapacity is the current capacity.
     * @return the new save capacity.
     */
    int ensureCapacity(int requiredCapacity, int currentCapacity);
}
