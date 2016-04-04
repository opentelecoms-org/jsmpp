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


/**
 * Absolute time formatter is a {@link TimeFormatter} implementation referred to in
 * SMPP Protocol Specification v3.4 point 7.1.1.
 * 
 * @author uudashr
 * 
 */
public class AbsoluteTimeFormatter implements TimeFormatter {
    /**
     * Time/Date ASCII format for Absolute Time Format is:
     * 
     * YYMMDDhhmmsstnnp (refer for SMPP Protocol Specification v3.4)
     */
    private static final String DATE_FORMAT = "{0,number,00}{1,number,00}{2,number,00}{3,number,00}{4,number,00}{5,number,00}{6,number,0}{7,number,00}{8}";
    
    public String format(Calendar calendar) {
        if (calendar == null) {
            return null;
        }
        int year = calendar.get(Calendar.YEAR) - 2000;
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        int tenthsOfSecond = calendar.get(Calendar.MILLISECOND) / 100;
        
        int offset = calendar.getTimeZone().getOffset(calendar.getTimeInMillis());
        
        // Get the sign
        char sign;
        if (offset >= 0) {
            sign = '+';
        } else {
            sign = '-';
        }
        
        // Time difference in quarter hours
        int timeDiff = Math.abs(offset) / (15 * 60 * 1000);
        
        return format(year, month, day, hour, minute, second, tenthsOfSecond, timeDiff, sign);
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
            Integer day, Integer hour, Integer minute, Integer second,
            int tenthsOfSecond, int timeDiff, Character sign) {
        Object[] args = new Object[] {year, month, day, hour, minute, second, tenthsOfSecond, timeDiff, sign};
        return MessageFormat.format(DATE_FORMAT, args);
    }
}
