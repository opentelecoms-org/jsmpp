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
 * Relative time formatter is {@link TimeFormatter} implementation referred to SMPP Protocol Specification v3.4 point
 * 7.1.1.
 *
 * @author pmoerenhout
 */
public class RelativeTimeFormatter implements TimeFormatter {

  private static TimeZone utcTimeZone = TimeZone.getTimeZone("UTC");

  /**
   * Time/Date ASCII format for Relative Time Format is:
   *
   * YYMMDDhhmmss000R (refer for SMPP Protocol Specification v3.4)
   */
  private static final String DATE_FORMAT = "{0,number,00}{1,number,00}{2,number,00}{3,number,00}{4,number,00}{5,number,00}000R";

  /**
   * Construct
   */
  public RelativeTimeFormatter() {
  }

  /**
   * Return the relative time against current (SMSC) datetime.
   *
   * @param calendar the datetime.
   * @return The relative time between the calendar date and the SMSC calendar date.
   */
  public String format(Calendar calendar) {
    // As the relative period is calculated on epoch (timeInMillis), no TimeZone information is needed
    Calendar smscCalendar = Calendar.getInstance();
    return format(calendar, smscCalendar);
  }

  /**
   * Return the relative time from the calendar datetime against the SMSC datetime.
   *
   * @param calendar     the date.
   * @param smscCalendar the SMSC date.
   * @return The relative time between the calendar date and the SMSC calendar date.
   */
  public String format(Calendar calendar, Calendar smscCalendar) {
    if (calendar == null || smscCalendar == null) {
      return null;
    }

    long diffTimeInMillis = calendar.getTimeInMillis() - smscCalendar.getTimeInMillis();
    if (diffTimeInMillis < 0) {
      throw new IllegalArgumentException("The requested relative time has already past.");
    }

    // calculate period from epoch, this is not as accurate as Joda-Time Period class or Java 8 Period
    Calendar offsetEpoch = Calendar.getInstance(utcTimeZone);
    offsetEpoch.setTimeInMillis(diffTimeInMillis);
    int years = offsetEpoch.get(Calendar.YEAR) - 1970;
    int months = offsetEpoch.get(Calendar.MONTH);
    int days = offsetEpoch.get(Calendar.DAY_OF_MONTH) - 1;
    int hours = offsetEpoch.get(Calendar.HOUR_OF_DAY);
    int minutes = offsetEpoch.get(Calendar.MINUTE);
    int seconds = offsetEpoch.get(Calendar.SECOND);

    if (years >= 100) {
      throw new IllegalArgumentException("The requested relative time is more then a century (" + years + " years).");
    }

    return format(years, months, days, hours, minutes, seconds);
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
    Object[] args = new Object[]{ year, month, day, hour, minute, second };
    return MessageFormat.format(DATE_FORMAT, args);
  }

}
