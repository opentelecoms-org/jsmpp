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
package org.jsmpp.bean;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import org.jsmpp.util.DeliveryReceiptState;
import org.jsmpp.util.InvalidDeliveryReceiptException;

/**
 * @author uudashr
 *
 */
public class DeliveryReceipt implements DeliveryReceiptInterface<DeliveryReceiptState> {
    // attributes of delivery receipt
    public static final String DELREC_ID = "id";
    public static final String DELREC_SUB = "sub";
    public static final String DELREC_DLVRD = "dlvrd";
    public static final String DELREC_SUBMIT_DATE = "submit date";
    public static final String DELREC_DONE_DATE = "done date";
    public static final String DELREC_STAT = "stat";
    public static final String DELREC_ERR = "err";
    public static final String DELREC_TEXT = "Text";

    /**
     * Date format for the <b>submit date</b> and <b>done date</b> attribute
     */
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMddHHmm");

    private String id;
    private Integer submitted;
    private Integer delivered;
    private Date submitDate;
    private Date doneDate;
    private DeliveryReceiptState finalStatus;
    private String error;
    private String text;

    public DeliveryReceipt() {
    }

    public DeliveryReceipt(String formattedDeliveryReceipt)
            throws InvalidDeliveryReceiptException {
        /*
         * id:IIIIIIIIII sub:SSS dlvrd:DDD submit date:YYMMDDhhmm done
         * date:YYMMDDhhmm stat:DDDDDDD err:E Text: ..........
         */
        try {
            id = getDeliveryReceiptValue(DeliveryReceipt.DELREC_ID, formattedDeliveryReceipt);
            submitted = getDeliveryReceiptIntValue(DeliveryReceipt.DELREC_SUB, formattedDeliveryReceipt);
            delivered = getDeliveryReceiptIntValue(DeliveryReceipt.DELREC_DLVRD, formattedDeliveryReceipt);
            submitDate = string2Date(getDeliveryReceiptValue(
                    DeliveryReceipt.DELREC_SUBMIT_DATE, formattedDeliveryReceipt));
            doneDate = string2Date(getDeliveryReceiptValue(
                    DeliveryReceipt.DELREC_DONE_DATE, formattedDeliveryReceipt));
            finalStatus = DeliveryReceiptState
                    .getByName(getDeliveryReceiptValue(
                        DeliveryReceipt.DELREC_STAT, formattedDeliveryReceipt));
            error = getDeliveryReceiptValue(DeliveryReceipt.DELREC_ERR, formattedDeliveryReceipt);
            text = getDeliveryReceiptTextValue(formattedDeliveryReceipt);
        } catch (Exception e) {
            throw new InvalidDeliveryReceiptException("There is an error found when parsing delivery receipt", e);
        }
    }

    public DeliveryReceipt(String id, int submitted, int delivered,
            Date submitDate, Date doneDate, DeliveryReceiptState finalStatus,
            String error, String text) {
        this.id = id;
        this.submitted = submitted;
        this.delivered = delivered;
        this.submitDate = submitDate;
        this.doneDate = doneDate;
        this.finalStatus = finalStatus;
        this.error = error;
        if (text.length() > 20) {
            this.text = text.substring(0, 20);
        } else {
            this.text = text;
        }
    }

    /**
     * @return Returns the delivered.
     */
    public int getDelivered() {
        return delivered;
    }

    /**
     * @param delivered The delivered to set.
     */
    public void setDelivered(int delivered) {
        this.delivered = delivered;
    }

    /**
     * @return Returns the doneDate.
     */
    public Date getDoneDate() {
        return doneDate;
    }

    /**
     * @param doneDate The doneDate to set.
     */
    public void setDoneDate(Date doneDate) {
        this.doneDate = doneDate;
    }

    /**
     * @return Returns the error.
     */
    public String getError() {
        return error;
    }

    /**
     * @param error The error to set.
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @return Returns the finalStatus.
     */
    public DeliveryReceiptState getFinalStatus() {
        return finalStatus;
    }

    /**
     * @param finalStatus The finalStatus to set.
     */
    public void setFinalStatus(DeliveryReceiptState finalStatus) {
        this.finalStatus = finalStatus;
    }

    /**
     * @return Returns the id.
     */
    public String getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return Returns the submitDate.
     */
    public Date getSubmitDate() {
        return submitDate;
    }

    /**
     * @param submitDate The submitDate to set.
     */
    public void setSubmitDate(Date submitDate) {
        this.submitDate = submitDate;
    }

    /**
     * @return Returns the submitted.
     */
    public int getSubmitted() {
        return submitted;
    }

    /**
     * @param submitted The submitted to set.
     */
    public void setSubmitted(int submitted) {
        this.submitted = submitted;
    }

    /**
     * @return Returns the text.
     */
    public String getText() {
        return text;
    }

    /**
     * Set the text of delivery receipt. Text more than 20 characters will be trim automatically.
     *
     * @param text the text to set.
     */
    public void setText(String text) {
        if (text != null && text.length() > 20) {
            this.text = text.substring(0, 20);
        } else {
            this.text = text;
        }
    }

    @Override
    public String toString() {
        /*
         * id:IIIIIIIIII sub:SSS dlvrd:DDD submit date:YYMMDDhhmm done
         * date:YYMMDDhhmm stat:DDDDDDD err:E Text: . . . . . . . . .
         */
        StringBuilder stringBuilder = new StringBuilder(120);
        stringBuilder.append(DELREC_ID + ":" + id)
                     .append(" ")
                     .append(DELREC_SUB + ":" + intToString(submitted, 3))
                     .append(" ")
                     .append(DELREC_DLVRD + ":" + intToString(delivered, 3))
                     .append(" ")
                     .append(DELREC_SUBMIT_DATE + ":" + dateFormat.format(submitDate))
                     .append(" ")
                     .append(DELREC_DONE_DATE + ":" + dateFormat.format(doneDate))
                     .append(" ")
                     .append(DELREC_STAT + ":" + finalStatus)
                     .append(" ")
                     .append(DELREC_ERR + ":" + error)
                     .append(" ")
                     .append(DELREC_TEXT.toLowerCase() + ":" + text);
        return stringBuilder.toString();
    }

    /**
     * Create String representation of integer. Preceding 0 will be add as
     * needed.
     *
     * @param value is the value.
     * @param digit is the digit should be shown.
     * @return the String representation of int value.
     */
    private static String intToString(int value, int digit) {
        StringBuilder stringBuilder = new StringBuilder(digit);
        stringBuilder.append(value);
        while (stringBuilder.length() < digit) {
            stringBuilder.insert(0, "0");
        }
        return stringBuilder.toString();
    }

    /**
     * Get the delivery receipt attribute value.
     *
     * @param attrName is the attribute name.
     * @param source the original source id:IIIIIIIIII sub:SSS dlvrd:DDD submit
     *        date:YYMMDDhhmm done date:YYMMDDhhmm stat:DDDDDDD err:E
     *        Text:....................
     * @return the value of specified attribute
     */
    private static String getDeliveryReceiptValue(String attrName, String source) {
        String tmpAttr = attrName + ":";
        int startIndex = source.indexOf(tmpAttr);
        if (startIndex < 0) {
            return null;
        }
        startIndex = startIndex + tmpAttr.length();
        int endIndex = source.indexOf(" ", startIndex);
        if (endIndex > 0) {
            return source.substring(startIndex, endIndex);
        }
        return source.substring(startIndex);
    }

    /**
     * Converts the date string to a {@link Date} object.
     *
     * <p>
     * YYMMDDhhmm where:
     * <ul>
     * <li>YY = last two digits of the year (00-99)</li>
     * <li>MM = month (01-12)</li>
     * <li>DD = day (01-31)</li>
     * <li>hh = hour (00-23)</li>
     * <li>mm = minute (00-59)</li>
     * </ul>
     *
     * Java format is (yyMMddHHmm).
     *
     * @param date in {@code String} with YYMMDDhhmm format.
     * @return the Date object
     * @throws NumberFormatException if {@code date} parameter contains non-number.
     * @throws IndexOutOfBoundsException if the date length in {@code }String}
     *         format is less than 10.
     */
    private static Date string2Date(String date) {
        if (date == null) {
          return null;
        }
        int year = Integer.parseInt(date.substring(0, 2));
        int month = Integer.parseInt(date.substring(2, 4));
        int day = Integer.parseInt(date.substring(4, 6));
        int hour = Integer.parseInt(date.substring(6, 8));
        int minute = Integer.parseInt(date.substring(8, 10));
        int second = 0;
        if (date.length() >= 12){
            second = Integer.parseInt(date.substring(10, 12));
        }
        Calendar cal = Calendar.getInstance();
        cal.set(convertTwoDigitYear(year), month - 1, day, hour, minute, second);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    private static int convertTwoDigitYear(int year) {
        if (year >=0 && year <= 37) {
            return 2000 + year;
        } else if (year >= 38 && year <= 99) {
            return 1900 + year;
        } else {
            // should never happen
            return year;
        }
    }

    /**
     * @param source The Delivery Receipt string.
     * @return The text value in the delivery receipt.
     */
    private static String getDeliveryReceiptTextValue(String source) {
        String tmpAttr = DeliveryReceipt.DELREC_TEXT + ":";
        int startIndex = source.indexOf(tmpAttr);
        if (startIndex < 0) {
            tmpAttr = DeliveryReceipt.DELREC_TEXT.toLowerCase() + ":";
            startIndex = source.indexOf(tmpAttr);
        }
        if (startIndex < 0) {
            return null;
        }
        startIndex = startIndex + tmpAttr.length();
        return source.substring(startIndex);
    }

    private static int getDeliveryReceiptIntValue(String attrName, String formattedDeliveryReceipt){
        String value = getDeliveryReceiptValue(attrName, formattedDeliveryReceipt);
        if (value != null) {
            return Integer.parseInt(value);
        }
        return -1;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final DeliveryReceipt that = (DeliveryReceipt) o;
        return Objects.equals(dateFormat, that.dateFormat) &&
            Objects.equals(id, that.id) &&
            Objects.equals(submitted, that.submitted) &&
            Objects.equals(delivered, that.delivered) &&
            Objects.equals(submitDate, that.submitDate) &&
            Objects.equals(doneDate, that.doneDate) &&
            finalStatus == that.finalStatus &&
            Objects.equals(error, that.error) &&
            Objects.equals(text, that.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dateFormat, id, submitted, delivered, submitDate, doneDate, finalStatus, error, text);
    }
}
