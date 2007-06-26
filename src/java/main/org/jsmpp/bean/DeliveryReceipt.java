package org.jsmpp.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.jsmpp.util.DeliveryReceiptState;


/**
 * @author uudashr
 * 
 */
public class DeliveryReceipt {
    // attribute of delivery receipt
    public static final String DELREC_ID = "id";
    public static final String DELREC_SUB = "sub";
    public static final String DELREC_DLVRD = "dlvrd";
    public static final String DELREC_SUBMIT_DATE = "submit date";
    public static final String DELREC_DONE_DATE = "done date";
    public static final String DELREC_STAT = "stat";
    public static final String DELREC_ERR = "err";
    public static final String DELREC_TEXT = "text";

    /**
     * Date format for the <b>submit date</b> and <b>done date</b> attribute
     */
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "yyMMddHHmm");

    private String _id;
    private int _submitted;
    private int _delivered;
    private Date _submitDate;
    private Date _doneDate;
    private DeliveryReceiptState _finalStatus;
    private String _error;
    private String _text;

    public DeliveryReceipt() {

    }

    public DeliveryReceipt(String id, int submitted, int delivered,
            Date submitDate, Date doneDate, DeliveryReceiptState finalStatus,
            String error, String text) {
        _id = id;
        _submitted = submitted;
        _delivered = delivered;
        _submitDate = submitDate;
        _doneDate = doneDate;
        _finalStatus = finalStatus;
        _error = error;
        if (text.length() > 20)
            _text = text.substring(0, 20);
        else
            _text = text;
    }

    public DeliveryReceipt(String data) {

    }

    /**
     * @return Returns the delivered.
     */
    public int getDelivered() {
        return _delivered;
    }

    /**
     * @param delivered The delivered to set.
     */
    public void setDelivered(int delivered) {
        _delivered = delivered;
    }

    /**
     * @return Returns the doneDate.
     */
    public Date getDoneDate() {
        return _doneDate;
    }

    /**
     * @param doneDate The doneDate to set.
     */
    public void setDoneDate(Date doneDate) {
        _doneDate = doneDate;
    }

    /**
     * @return Returns the error.
     */
    public String getError() {
        return _error;
    }

    /**
     * @param error The error to set.
     */
    public void setError(String error) {
        _error = error;
    }

    /**
     * @return Returns the finalStatus.
     */
    public DeliveryReceiptState getFinalStatus() {
        return _finalStatus;
    }

    /**
     * @param finalStatus The finalStatus to set.
     */
    public void setFinalStatus(DeliveryReceiptState finalStatus) {
        _finalStatus = finalStatus;
    }

    /**
     * @return Returns the id.
     */
    public String getId() {
        return _id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(String id) {
        _id = id;
    }

    /**
     * @return Returns the submitDate.
     */
    public Date getSubmitDate() {
        return _submitDate;
    }

    /**
     * @param submitDate The submitDate to set.
     */
    public void setSubmitDate(Date submitDate) {
        _submitDate = submitDate;
    }

    /**
     * @return Returns the submitted.
     */
    public int getSubmitted() {
        return _submitted;
    }

    /**
     * @param submitted The submitted to set.
     */
    public void setSubmitted(int submitted) {
        _submitted = submitted;
    }

    /**
     * @return Returns the text.
     */
    public String getText() {
        return _text;
    }

    /**
     * @param text The text to set.
     */
    public void setText(String text) {
        if (text.length() > 20)
            _text = text.substring(0, 20);
        else
            _text = text;
    }

    public String toString() {
        /*
         * id:IIIIIIIIII sub:SSS dlvrd:DDD submit date:YYMMDDhhmm done
         * date:YYMMDDhhmm stat:DDDDDDD err:E Text: . . . . . . . . .
         */
        StringBuffer sBuf = new StringBuffer(120);
        sBuf.append(DELREC_ID + ":" + _id);
        sBuf.append(" ");
        sBuf.append(DELREC_SUB + ":" + intToString(_submitted, 3));
        sBuf.append(" ");
        sBuf.append(DELREC_DLVRD + ":" + intToString(_delivered, 3));
        sBuf.append(" ");
        sBuf.append(DELREC_SUBMIT_DATE + ":" + dateFormat.format(_submitDate));
        sBuf.append(" ");
        sBuf.append(DELREC_DONE_DATE + ":" + dateFormat.format(_doneDate));
        sBuf.append(" ");
        sBuf.append(DELREC_STAT + ":" + _finalStatus);
        sBuf.append(" ");
        sBuf.append(DELREC_STAT + ":" + _error);
        sBuf.append(" ");
        sBuf.append(DELREC_TEXT + ":" + _text);
        return sBuf.toString();
    }

    private static String intToString(int value, int digit) {
        StringBuffer sBuf = new StringBuffer(digit);
        sBuf.append(Integer.toString(value));
        while (sBuf.length() < digit) {
            sBuf.insert(0, "0");
        }
        return sBuf.toString();
    }
}
