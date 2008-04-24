package org.jsmpp.util;

import java.util.Calendar;
import java.util.Date;

import static org.testng.Assert.*;
import org.testng.annotations.Test;


/**
 * @author uudashr
 *
 */
public class DateFormatterTest {
    
    @Test(groups="checkintest")
    public void testStaticAbsoluteFormatter() {
        String formatted = AbsoluteTimeFormatter.format(07, 12, 26, 11, 37, 03, 8, 45, '+');
        assertEquals(formatted, "071226113703845+");
    }
    
    @Test(groups="checkintest")
    public void testStaticRelativeFormatter() {
        String formatted = RelativeTimeFormatter.format(07, 12, 26, 12, 46, 10);
        assertEquals(formatted, "071226124610000R");
    }
    
    @Test(groups="checkintest")
    public void formatNullDate() {
        TimeFormatter timeFormatter = new AbsoluteTimeFormatter();
        assertNull(timeFormatter.format((Date)null));
        assertNull(timeFormatter.format((Calendar)null));
        
        timeFormatter = new RelativeTimeFormatter();
        assertNull(timeFormatter.format((Date)null));
        assertNull(timeFormatter.format((Calendar)null));
    }
    
    @Test(groups="checkintest")
    public void validateAbsoluteDate() throws Exception {
        String formatted = AbsoluteTimeFormatter.format(07, 12, 26, 11, 37, 03, 8, 45, '+');
        StringValidator.validateString(formatted, StringParameter.SCHEDULE_DELIVERY_TIME);
        StringValidator.validateString(formatted, StringParameter.VALIDITY_PERIOD);
        StringValidator.validateString(formatted, StringParameter.FINAL_DATE);
    }
    
    @Test(groups="checkintest")
    public void validateRelativeDate() throws Exception {
        String formatted = RelativeTimeFormatter.format(07, 12, 26, 12, 46, 10);
        StringValidator.validateString(formatted, StringParameter.SCHEDULE_DELIVERY_TIME);
        StringValidator.validateString(formatted, StringParameter.VALIDITY_PERIOD);
        StringValidator.validateString(formatted, StringParameter.FINAL_DATE);
    }
}
