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
    private int start;
    private int increment;
    private int maxValue;
    private boolean cycle;

    private int currentValue;

    private boolean hasBeenUsed = false;

    /**
     * @param start is the start/init value of the sequence.
     * @param increment is the incremental value.
     * @param maxValue is the max limit of a sequence value.
     * @param cycle <code>true</code> means if sequence has reach max limit,
     *        then it will cycling and start over from the init value. The
     *        default value is <code>false</code>.
     */
    public Sequence(int start, int increment, int maxValue, boolean cycle) {
        this.start = start;
        this.increment = increment;
        this.maxValue = maxValue;
        this.cycle = cycle;
    }

    /**
     * @param start is the start/init value of the sequence.
     * @param increment is the incremental value.
     * @param maxValue is the max limit of a sequence value.
     */
    public Sequence(int start, int increment, int maxValue) {
        this(start, increment, maxValue, false);
    }

    /**
     * @param start is the start/init value of the sequence.
     * @param increment is the incremental value.
     */
    public Sequence(int start, int increment) {
        this(start, increment, Integer.MAX_VALUE);
    }

    /**
     * @param start is the start/init value of the sequence.
     */
    public Sequence(int start) {
        this(start, 1);
    }

    public Sequence() {
        this(1);
    }

    /**
     * Walk and return the next value.
     * 
     * @return the next value.
     */
    public synchronized int nextValue() {
        if (!hasBeenUsed) {
            hasBeenUsed = true;
            return currentValue = start;
        }

        long tmp = currentValue + increment;
        if (tmp > maxValue) {
            if (cycle)
                return currentValue = start;
            else
                return currentValue;
        } else
            return currentValue += increment;

    }

    /**
     * @return the current value.
     */
    public synchronized int currentValue() {
        return currentValue;
    }

    /**
     * Reset the sequence.
     */
    public synchronized void reset() {
        hasBeenUsed = false;
        currentValue = 0;
    }

    public static void main(String[] args) {
        // Sequence seq = new Sequence(1, 2, 4, true);
        Sequence seq = new Sequence(1, 1, Integer.MAX_VALUE, true);
        int val = 0;
        System.out.println(seq.nextValue());
        System.out.println(seq.nextValue());
        while (val != 1) {
            val = seq.nextValue();
            System.out.println(val);
        }
    }
}
