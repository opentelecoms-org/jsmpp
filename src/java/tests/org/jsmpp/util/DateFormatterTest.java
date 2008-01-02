package org.jsmpp.util;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.Date;

import org.jsmpp.util.AbsoluteTimeFormatter;
import org.jsmpp.util.RelativeTimeFormatter;
import org.jsmpp.util.TimeFormatter;
import org.junit.Test;


/**
 * @author uudashr
 *
 */
public class DateFormatterTest {
    
    @Test
    public void testStaticAbsoluteFormatter() {
        String formatted = AbsoluteTimeFormatter.format(07, 12, 26, 11, 37, 03, 8, 45, '+');
        assertEquals("071226113703845+", formatted);
    }
    
    @Test
    public void testStaticRelativeFormatter() {
        String formatted = RelativeTimeFormatter.format(07, 12, 26, 12, 46, 10);
        assertEquals("071226124610000R", formatted);
    }
    
    @Test
    public void formatNullDate() {
        TimeFormatter timeFormatter = new AbsoluteTimeFormatter();
        assertNull(timeFormatter.format((Date)null));
        assertNull(timeFormatter.format((Calendar)null));
        
        timeFormatter = new RelativeTimeFormatter();
        assertNull(timeFormatter.format((Date)null));
        assertNull(timeFormatter.format((Calendar)null));
    }
}
