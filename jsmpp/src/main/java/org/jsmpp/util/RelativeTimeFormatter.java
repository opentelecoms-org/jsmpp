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

import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Relative time formatter is {@link TimeFormatter} implementation referred to
 * SMPP Protocol Specification v3.4 point 7.1.1.
 * 
 * @author uudashr
 *
 */
public class RelativeTimeFormatter implements TimeFormatter {
    private final TimeZone timezone;
    
    /**
     * Time/Date ASCII format for Absolute Time Format is:
     * 
     * YYMMDDhhmmsstnnp (refer for SMPP Protocol Specification v3.4)
     */
    private static final String DATE_FORMAT = "{0,number,00}{1,number,00}{2,number,00}{3,number,00}{4,number,00}{5,number,00}000R";
    
    /**
     * Construct with default timezone.
     */
    public RelativeTimeFormatter() {
        this(TimeZone.getDefault());
    }
    
    /**
     * Construct with specified SMSC timezone.
     * 
     * @param timezone is the SMSC timezone.
     */
    public RelativeTimeFormatter(TimeZone timezone) {
        this.timezone = timezone;
    }
    
    public String format(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        
        long relativeTime = calendar.getTimeInMillis()
                - calendar.getTimeZone().getOffset(calendar.getTimeInMillis())
                + timezone.getOffset(calendar.getTimeInMillis());
        
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(relativeTime);
        int year = cal.get(Calendar.YEAR) - 2000;
        int month = cal.get(Calendar.MONTH) + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        
        return format(year, month, day, hour, minute, second);
    }
    
    public String format(Date date) {
        if (date == null) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return format(cal);
    }
    
    public static final String format(Integer year, Integer month,
            Integer day, Integer hour, Integer minute, Integer second) {
        Object[] args = new Object[] {year, month, day, hour, minute, second};
        return MessageFormat.format(DATE_FORMAT, args);
    }
}
