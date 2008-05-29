package org.jsmpp.util;

import static org.testng.Assert.*;

import org.testng.annotations.Test;

/**
 * Test the {@link SequentialBytesReader}.
 * 
 * @author uudashr
 *
 */
public class SequentialByteReaderTest {
    
    /**
     * Test reading byte until null (0). 
     */
    @Test
    public void testReadUntilNull() {
        byte[] bytes = "Hello".getBytes();
        byte[] nullDelimitedBytes = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, nullDelimitedBytes, 0, bytes.length);
        assertEquals(nullDelimitedBytes[bytes.length], (byte)0);
        
        SequentialBytesReader reader = new SequentialBytesReader(nullDelimitedBytes);
        byte[] readedBytes = reader.readBytesUntilNull();
        assertEquals(readedBytes, bytes);
    }
}
