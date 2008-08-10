/*
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License. 
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 */
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
