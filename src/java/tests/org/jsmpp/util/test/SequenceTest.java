package org.jsmpp.util.test;

import org.jsmpp.util.Sequence;

import junit.framework.TestCase;

/**
 * @author uudashr
 *
 */
public class SequenceTest extends TestCase {
    public void testSequence() {
        Sequence sequence = new Sequence(1);
        for (int i = 1; i < 100; i++) {
            assertEquals(i, sequence.nextValue());
        }
    }
    
    public void testCycle() {
        Sequence sequence = new Sequence(Integer.MAX_VALUE - 2);
        assertEquals(Integer.MAX_VALUE - 2, sequence.nextValue());
        assertEquals(Integer.MAX_VALUE - 1, sequence.nextValue());
        assertEquals(Integer.MAX_VALUE, sequence.nextValue());
        assertEquals(1, sequence.nextValue());
    }
}
