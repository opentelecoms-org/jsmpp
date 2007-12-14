package org.jsmpp.util;


/**
 * Sequence number that able to rolling.
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 * 
 */
public class Sequence {
    private int value;

    /**
     * @param start is the start/init value of the sequence.
     */
    public Sequence(int start) {
        value = start;
    }

    /**
     * Return the next value.
     * 
     * @return the next value.
     */
    public synchronized int nextValue() {
        int curValue = value++;
        if (curValue == Integer.MAX_VALUE) {
            value = 1;
        }
        return curValue;
    }

    /**
     * @return the current value.
     */
    public synchronized int currentValue() {
        return value;
    }
}
