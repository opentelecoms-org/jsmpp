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

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.testng.annotations.Test;


/**
 * @author uudashr
 */
public class DateFormatterTest {

  @Test(groups = "checkintest")
  public void testStaticAbsoluteFormatter() {
    String formatted = AbsoluteTimeFormatter.format(7, 12, 26, 11, 37, 3, 8, 45, '+');
    assertEquals(formatted, "071226113703845+");
  }

  @Test(groups = "checkintest")
  public void testStaticRelativeFormatter() {
    String formatted = RelativeTimeFormatter.format(7, 12, 26, 12, 46, 10);
    assertEquals(formatted, "071226124610000R");
  }

  @Test(groups = "checkintest")
  public void formatNullDate() {
    TimeFormatter timeFormatter = new AbsoluteTimeFormatter();
    assertNull(timeFormatter.format((Date) null));
    assertNull(timeFormatter.format((Calendar) null));

    timeFormatter = new RelativeTimeFormatter();
    assertNull(timeFormatter.format((Date) null));
    assertNull(timeFormatter.format((Calendar) null));
  }

  @Test(groups = "checkintest")
  public void validateAbsoluteDate() throws Exception {
    String formatted = AbsoluteTimeFormatter.format(7, 12, 26, 11, 37, 3, 8, 45, '+');
    StringValidator.validateString(formatted, StringParameter.SCHEDULE_DELIVERY_TIME);
    StringValidator.validateString(formatted, StringParameter.VALIDITY_PERIOD);
    StringValidator.validateString(formatted, StringParameter.FINAL_DATE);
  }

  @Test(groups = "checkintest")
  public void validateRelativeDate() throws Exception {
    String formatted = RelativeTimeFormatter.format(7, 12, 26, 12, 46, 10);
    StringValidator.validateString(formatted, StringParameter.SCHEDULE_DELIVERY_TIME);
    StringValidator.validateString(formatted, StringParameter.VALIDITY_PERIOD);
    StringValidator.validateString(formatted, StringParameter.FINAL_DATE);
  }

  @Test(groups = "checkintest")
  public void formatAbsoluteDate() {
    TimeFormatter timeFormatter = new AbsoluteTimeFormatter();

    GregorianCalendar date = new GregorianCalendar(TimeZone.getTimeZone("Europe/Berlin"), Locale.GERMANY);
    date.set(Calendar.YEAR, 2013);
    date.set(Calendar.MONTH, Calendar.JANUARY);
    date.set(Calendar.DAY_OF_MONTH, 1);
    date.set(Calendar.HOUR_OF_DAY, 1);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);

    assertEquals(timeFormatter.format(date), "130101010000004+");

    date.set(Calendar.MONTH, Calendar.JULY);

    // because of daylight saving time, we have a different offset
    assertEquals(timeFormatter.format(date), "130701010000008+");
  }

  @Test(groups = "checkintest")
  public void formatAbsoluteDateRussia() {
    TimeFormatter timeFormatter = new AbsoluteTimeFormatter();

    GregorianCalendar date = new GregorianCalendar(TimeZone.getTimeZone("Asia/Yekaterinburg"), new Locale("ru", "RU"));
    date.set(Calendar.YEAR, 2013);
    date.set(Calendar.MONTH, Calendar.JANUARY);
    date.set(Calendar.DAY_OF_MONTH, 1);
    date.set(Calendar.HOUR_OF_DAY, 1);
    date.set(Calendar.MINUTE, 0);
    date.set(Calendar.SECOND, 0);
    date.set(Calendar.MILLISECOND, 0);

    assertEquals(timeFormatter.format(date), "130101010000024+");

    date.set(Calendar.MONTH, Calendar.JULY);

    // we have the same offset because of the absent of daylight saving time
    assertEquals(timeFormatter.format(date), "130701010000024+");
  }

}
