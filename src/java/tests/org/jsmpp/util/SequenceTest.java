package org.jsmpp.util;

import static org.junit.Assert.*;
import org.jsmpp.util.Sequence;
import org.junit.Test;

/**
 * @author uudashr
 *
 */
public class SequenceTest {
    
    @Test
    public void testSequence() {
        Sequence sequence = new Sequence(1);
        for (int i = 1; i < 100; i++) {
            assertEquals(i, sequence.nextValue());
        }
    }
    
    @Test
    public void testCycle() {
        Sequence sequence = new Sequence(Integer.MAX_VALUE - 2);
        assertEquals(Integer.MAX_VALUE - 2, sequence.nextValue());
        assertEquals(Integer.MAX_VALUE - 1, sequence.nextValue());
        assertEquals(Integer.MAX_VALUE, sequence.nextValue());
        assertEquals(1, sequence.nextValue());
    }
}
