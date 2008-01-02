package org.jsmpp.util;

import java.util.Calendar;
import java.util.Date;


/**
 * @author uudashr
 *
 */
public interface TimeFormatter {
    
    /**
     * Format the date to {@link String} representation.
     * 
     * @param calendar is the calendar to format.
     * @return the formatted date or <code>null</code> if the <code>date</code> is <code>null</code>.
     */
    String format(Calendar calendar);
    
    /**
     * Format the date to {@link String} representation.
     * 
     * @param date is the date to format.
     * @return the formatted date or <code>null</code> if the <code>date</code> is <code>null</code>.
     */
    String format(Date date);
}
