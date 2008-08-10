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

import java.util.Calendar;
import java.util.Date;


/**
 * This provide time formatter functionality from a type to a String
 * representation of date that can be recognized based on SMPP protocol.
 * 
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
