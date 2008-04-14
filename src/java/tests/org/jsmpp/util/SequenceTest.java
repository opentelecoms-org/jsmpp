package org.jsmpp.util;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * @author uudashr
 *
 */
public class SequenceTest {
    
    @Test(groups="checkintest")
    public void testSequence() {
        Sequence sequence = new Sequence(1);
        for (int i = 1; i < 100; i++) {
            assertEquals(sequence.nextValue(), i);
        }
    }
    
    @Test(groups="checkintest")
    public void testCycle() {
        Sequence sequence = new Sequence(Integer.MAX_VALUE - 2);
        assertEquals(sequence.nextValue(), Integer.MAX_VALUE - 2);
        assertEquals(sequence.nextValue(), Integer.MAX_VALUE - 1);
        assertEquals(sequence.nextValue(), Integer.MAX_VALUE);
        assertEquals(sequence.nextValue(), 1);
    }
}
