package org.jsmpp.util;

import static org.testng.Assert.*;

import org.jsmpp.bean.DataCodingFactory;
import org.jsmpp.bean.DataCodingFactory00xx;
import org.testng.annotations.Test;

/**
 * @author uudashr
 *
 */
public class DataCodingParserTest {
    
    @Test
    public void testCodingGroups_01() {
        byte valNok1 = (byte)0xC0; // 11000000
        byte valNok2 = (byte)0x80; // 10000000
        byte valNok3 = (byte)0x40; // 01000000
        
        byte valOk1 = (byte)0x00; // 00000000
        byte valOk2 = (byte)0x01; // 00000001
        
        DataCodingFactory dataCodingParser = new DataCodingFactory00xx();
        
        assertFalse(dataCodingParser.isRecognized(valNok1));
        assertFalse(dataCodingParser.isRecognized(valNok2));
        assertFalse(dataCodingParser.isRecognized(valNok3));
        
        assertTrue(dataCodingParser.isRecognized(valOk1));
        assertTrue(dataCodingParser.isRecognized(valOk2));
        
        // TODO uudashr: need more test here
    }
}
