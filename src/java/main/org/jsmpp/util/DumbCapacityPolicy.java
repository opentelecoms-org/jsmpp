package org.jsmpp.util;

/**
 * It's a dumb capacity policy. It calculate nothing, just return the new
 * capacity same as requeiredCapacity.
 * 
 * @author uudashr
 * 
 */
public class DumbCapacityPolicy implements CapacityPolicy {
    
    /* (non-Javadoc)
     * @see org.jsmpp.util.CapacityPolicy#ensureCapacity(int, int)
     */
    public int ensureCapacity(int requiredCapacity, int currentCapacity) {
        return requiredCapacity;
    }
}
