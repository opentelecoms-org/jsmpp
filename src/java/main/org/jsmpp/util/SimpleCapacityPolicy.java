package org.jsmpp.util;

/**
 * Simple implementation of {@link CapacityPolicy}.
 * <p> 
 * The calculation looks like
 * <code>
 *  int newCapcity = (currentCapacity * 3) / 2 + 1
 * </code>
 * and it's only apply if currentCapacity is not greater or equals than requiredCapacity.
 * </p>
 * @author uudashr
 *
 */
public class SimpleCapacityPolicy implements CapacityPolicy {
    
    /* (non-Javadoc)
     * @see org.jsmpp.util.CapacityPolicy#ensureCapacity(int, int)
     */
    public int ensureCapacity(int requiredCapacity, int currentCapacity) {
        if (requiredCapacity > currentCapacity) {
            int newCapacity = (currentCapacity * 3) / 2 + 1;
            if (newCapacity < requiredCapacity) {
                newCapacity = requiredCapacity;
            }
            return newCapacity;
        } else {
            return currentCapacity;
        }
    }
}
