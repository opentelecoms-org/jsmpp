package org.jsmpp.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.jsmpp.PDUStringException;
import org.jsmpp.bean.Bind;
import org.jsmpp.bean.BindResp;
import org.jsmpp.bean.Command;
import org.jsmpp.bean.DeliverSm;
import org.jsmpp.bean.DeliverSmResp;
import org.jsmpp.bean.DeliveryReceipt;
import org.jsmpp.bean.EnquireLink;
import org.jsmpp.bean.EnquireLinkResp;
import org.jsmpp.bean.GenericNack;
import org.jsmpp.bean.MessageState;
import org.jsmpp.bean.OptionalParameter;
import org.jsmpp.bean.Outbind;
import org.jsmpp.bean.PDU;
import org.jsmpp.bean.QuerySm;
import org.jsmpp.bean.QuerySmResp;
import org.jsmpp.bean.SubmitSm;
import org.jsmpp.bean.SubmitSmResp;
import org.jsmpp.bean.Unbind;
import org.jsmpp.bean.UnbindResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default implementation of SMPP PDU PDUDecomposer.
 * 
 * @author uudashr
 * @version 1.0
 * @since 1.0
 * 
 */
public class DefaultDecomposer implements PDUDecomposer {
    private static final Logger logger = LoggerFactory.getLogger(DefaultDecomposer.class);
    private static final PDUDecomposer instance = new DefaultDecomposer();

    public static final PDUDecomposer getInstance() {
        return instance;
    }

    /**
     * Default constructor.
     */
    public DefaultDecomposer() {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUDecomposer#header(byte[])
     */
    public Command header(PDU p) {
        return header(p.getData());
    }

    public Command header(byte[] data) {
        Command pdu = new Command();
        assignHeader(pdu, data);
        return pdu;
    }

    // BIND OPERATION
    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUDecomposer#bind(byte[])
     */
    public Bind bind(PDU p) throws PDUStringException {
        Bind req = new Bind();
        SequentialBytesReader reader = new SequentialBytesReader(p.getData());
        assignHeader(req, reader);
        req.setSystemId(reader.readCString());
        StringValidator.validateString(req.getSystemId(), StringParameter.SYSTEM_ID);
        req.setPassword(reader.readCString());
        StringValidator.validateString(req.getPassword(), StringParameter.PASSWORD);
        req.setSystemType(reader.readCString());
        StringValidator.validateString(req.getSystemType(), StringParameter.SYSTEM_TYPE);
        req.setInterfaceVersion(reader.readByte());
        req.setAddrTon(reader.readByte());
        req.setAddrNpi(reader.readByte());
        req.setAddressRange(reader.readCString());
        StringValidator.validateString(req.getAddressRange(), StringParameter.ADDRESS_RANGE);
        return req;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUDecomposer#bindResp(byte[])
     */
    public BindResp bindResp(PDU pdu) throws PDUStringException {
        BindResp resp = new BindResp();
        SequentialBytesReader reader = new SequentialBytesReader(pdu.getData());
        assignHeader(resp, reader);
        if (resp.getCommandLength() > 16 && resp.getCommandStatus() == 0) {
            resp.setSystemId(reader.readCString());
            StringValidator.validateString(resp.getSystemId(), StringParameter.SYSTEM_ID);
        }
        return resp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUDecomposer#unbind(byte[])
     */
    public Unbind unbind(PDU pdu) {
        Unbind req = new Unbind();
        assignHeader(req, pdu.getData());
        return req;
    }

    public UnbindResp unbindResp(PDU pdu) {
        UnbindResp resp = new UnbindResp();
        assignHeader(resp, pdu.getData());
        return resp;
    }

    public Outbind outbind(PDU pdu) throws PDUStringException {
        Outbind req = new Outbind();
        SequentialBytesReader reader = new SequentialBytesReader(pdu.getData());
        assignHeader(req, reader);
        req.setSystemId(reader.readCString());
        StringValidator.validateString(req.getSystemId(), StringParameter.SYSTEM_ID);
        req.setPassword(reader.readCString());
        StringValidator.validateString(req.getPassword(), StringParameter.PASSWORD);
        return req;
    }

    // ENQUIRE_LINK OPERATION
    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUDecomposer#enquireLink(byte[])
     */
    public EnquireLink enquireLink(PDU pdu) {
        EnquireLink req = new EnquireLink();
        assignHeader(req, pdu.getData());
        return req;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUDecomposer#enquireLinkResp(byte[])
     */
    public EnquireLinkResp enquireLinkResp(PDU pdu) {
        EnquireLinkResp resp = new EnquireLinkResp();
        assignHeader(resp, pdu.getData());
        return resp;
    }

    // GENERICK_NACK OPERATION
    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUDecomposer#genericNack(byte[])
     */
    public GenericNack genericNack(PDU pdu) {
        GenericNack req = new GenericNack();
        assignHeader(req, pdu.getData());
        return req;
    }

    // SUBMIT_SM OPERATION
    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUDecomposer#submitSm(byte[])
     */
    public SubmitSm submitSm(PDU pdu) throws PDUStringException {
        SubmitSm req = new SubmitSm();
        SequentialBytesReader reader = new SequentialBytesReader(pdu.getData());
        assignHeader(req, reader);
        req.setServiceType(reader.readCString());
        StringValidator.validateString(req.getServiceType(), StringParameter.SERVICE_TYPE);

        req.setSourceAddrTon(reader.readByte());
        req.setSourceAddrNpi(reader.readByte());
        req.setSourceAddr(reader.readCString());
        StringValidator.validateString(req.getSourceAddr(), StringParameter.SOURCE_ADDR);

        req.setDestAddrTon(reader.readByte());
        req.setDestAddrNpi(reader.readByte());
        req.setDestAddress(reader.readCString());
        StringValidator.validateString(req.getDestAddress(), StringParameter.DESTINATION_ADDR);

        req.setEsmClass(reader.readByte());
        req.setProtocolId(reader.readByte());
        req.setPriorityFlag(reader.readByte());
        req.setScheduleDeliveryTime(reader.readCString());
        StringValidator.validateString(req.getScheduleDeliveryTime(), StringParameter.SCHEDULE_DELIVERY_TIME);
        req.setValidityPeriod(reader.readCString());
        StringValidator.validateString(req.getValidityPeriod(), StringParameter.VALIDITY_PERIOD);
        req.setRegisteredDelivery(reader.readByte());
        req.setReplaceIfPresent(reader.readByte());
        req.setDataCoding(reader.readByte());
        req.setSmDefaultMsgId(reader.readByte());
        req.setSmLength(reader.readByte());
        // req.setShortMessage(reader.readString(req.getSmLength()));
        req.setShortMessage(reader.readBytes(req.getSmLength()));
        StringValidator.validateString(req.getShortMessage(), StringParameter.SHORT_MESSAGE);
        req.setOptionalParameters(readOptionalParameters(reader));
        return req;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUDecomposer#submitSmResp(byte[])
     */
    public SubmitSmResp submitSmResp(PDU pdu) throws PDUStringException {
        SubmitSmResp req = new SubmitSmResp();
        SequentialBytesReader reader = new SequentialBytesReader(pdu.getData());
        assignHeader(req, reader);
        if (req.getCommandLength() > 16 && req.getCommandStatus() == 0) {
            req.setMessageId(reader.readCString());
            StringValidator.validateString(req.getMessageId(), StringParameter.MESSAGE_ID);
        }
        return req;
    }

    // QUERY_SM OPERATION
    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUDecomposer#querySm(byte[])
     */
    public QuerySm querySm(PDU pdu) throws PDUStringException {
        QuerySm req = new QuerySm();
        SequentialBytesReader reader = new SequentialBytesReader(pdu.getData());
        assignHeader(req, reader);
        req.setMessageId(reader.readCString());
        StringValidator.validateString(req.getMessageId(), StringParameter.MESSAGE_ID);
        req.setSourceAddrTon(reader.readByte());
        req.setSourceAddrNpi(reader.readByte());
        req.setSourceAddr(reader.readCString());
        StringValidator.validateString(req.getSourceAddr(), StringParameter.SOURCE_ADDR);

        return req;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUDecomposer#querySmResp(byte[])
     */
    public QuerySmResp querySmResp(PDU pdu) throws PDUStringException {
        QuerySmResp resp = new QuerySmResp();
        SequentialBytesReader reader = new SequentialBytesReader(pdu.getData());
        assignHeader(resp, reader);
        resp.setMessageId(reader.readCString());
        StringValidator.validateString(resp.getMessageId(), StringParameter.MESSAGE_ID);
        resp.setFinalDate(reader.readCString());
        StringValidator.validateString(resp.getFinalDate(), StringParameter.FINAL_DATE);
        resp.setMessageState(MessageState.valueOf(reader.readByte()));
        resp.setErrorCode(reader.readByte());
        return resp;
    }

    // DELIVER_SM OPERATION
    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUDecomposer#deliverSm(byte[])
     */
    public DeliverSm deliverSm(PDU pdu) throws PDUStringException {
        DeliverSm req = new DeliverSm();
        SequentialBytesReader reader = new SequentialBytesReader(pdu.getData());
        assignHeader(req, reader);
        req.setServiceType(reader.readCString());
        StringValidator.validateString(req.getServiceType(), StringParameter.SERVICE_TYPE);

        req.setSourceAddrTon(reader.readByte());
        req.setSourceAddrNpi(reader.readByte());
        req.setSourceAddr(reader.readCString());
        StringValidator.validateString(req.getSourceAddr(), StringParameter.SOURCE_ADDR);

        req.setDestAddrTon(reader.readByte());
        req.setDestAddrNpi(reader.readByte());
        req.setDestAddress(reader.readCString());
        StringValidator.validateString(req.getDestAddress(), StringParameter.DESTINATION_ADDR);

        req.setEsmClass(reader.readByte());
        req.setProtocolId(reader.readByte());
        req.setPriorityFlag(reader.readByte());
        // FIXME uud: it should be null of c-octet string
        req.setScheduleDeliveryTime(reader.readCString());
        StringValidator.validateString(req.getScheduleDeliveryTime(), StringParameter.SCHEDULE_DELIVERY_TIME);
        // FIXME uud: it should be null of c-octet string
        req.setValidityPeriod(reader.readCString());
        StringValidator.validateString(req.getValidityPeriod(), StringParameter.VALIDITY_PERIOD);
        req.setRegisteredDelivery(reader.readByte());
        // FIXME uud: it should be set to null
        req.setReplaceIfPresent(reader.readByte());
        req.setDataCoding(reader.readByte());
        // FIXME uud: it should be set to null
        req.setSmDefaultMsgId(reader.readByte());
        req.setSmLength(reader.readByte());
        // req.setShortMessage(reader.readString(req.getSmLength()));
        req.setShortMessage(reader.readBytes(req.getSmLength()));
        StringValidator.validateString(req.getShortMessage(), StringParameter.SHORT_MESSAGE);
        req.setOptionalParameters(readOptionalParameters(reader));
        return req;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUDecomposer#deliverSmResp(byte[])
     */
    public DeliverSmResp deliverSmResp(PDU pdu) {
        DeliverSmResp resp = new DeliverSmResp();
        SequentialBytesReader reader = new SequentialBytesReader(pdu.getData());
        assignHeader(resp, reader);
        return resp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUDecomposer#deliveryReceipt(java.lang.String)
     */
    public DeliveryReceipt deliveryReceipt(String data) throws InvalidDeliveryReceiptException {
        /*
         * id:IIIIIIIIII sub:SSS dlvrd:DDD submit date:YYMMDDhhmm done
         * date:YYMMDDhhmm stat:DDDDDDD err:E Text: ..........
         */
        try {
            DeliveryReceipt delRec = new DeliveryReceipt();
            delRec.setId(getDeliveryReceiptValue(DeliveryReceipt.DELREC_ID, data));
            delRec.setSubmitted(Integer.parseInt(getDeliveryReceiptValue(DeliveryReceipt.DELREC_SUB, data)));
            delRec.setDelivered(Integer.parseInt(getDeliveryReceiptValue(DeliveryReceipt.DELREC_DLVRD, data)));
            delRec.setSubmitDate(string2Date(getDeliveryReceiptValue(DeliveryReceipt.DELREC_SUBMIT_DATE, data)));
            delRec.setDoneDate(string2Date(getDeliveryReceiptValue(DeliveryReceipt.DELREC_DONE_DATE, data)));
            delRec.setFinalStatus(DeliveryReceiptState.getByName(getDeliveryReceiptValue(DeliveryReceipt.DELREC_STAT, data)));
            delRec.setError(getDeliveryReceiptValue(DeliveryReceipt.DELREC_ERR, data));
            delRec.setText(getDeliveryReceiptTextValue(data));
            return delRec;
        } catch (Exception e) {
            throw new InvalidDeliveryReceiptException("There is an error found when parsing delivery receipt", e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jsmpp.util.PDUDecomposer#deliveryReceipt(byte[])
     */
    public DeliveryReceipt deliveryReceipt(byte[] data) throws InvalidDeliveryReceiptException {
        return deliveryReceipt(new String(data));
    }

    /**
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
     * @param date
     *            in <tt>String</tt> format.
     * @return
     * @throws NumberFormatException
     *             if there is containt non number on <code>date</code>
     *             parameter.
     * @throws IndexOutOfBoundsException
     *             if the date length in <tt>String</tt> format is less than
     *             10.
     */
    private static Date string2Date(String date) {

        int year = Integer.parseInt(date.substring(0, 2));
        int month = Integer.parseInt(date.substring(2, 4));
        int day = Integer.parseInt(date.substring(4, 6));
        int hour = Integer.parseInt(date.substring(6, 8));
        int minute = Integer.parseInt(date.substring(8, 10));
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day, hour, minute, 0);
        return cal.getTime();
    }

    /**
     * Get the delivery receipt attribute value.
     * 
     * @param attrName
     *            is the attribute name.
     * @param source
     *            the original source id:IIIIIIIIII sub:SSS dlvrd:DDD submit
     *            date:YYMMDDhhmm done date:YYMMDDhhmm stat:DDDDDDD err:E
     *            Text:....................
     * @return the value of specified attribute.
     * @throws IndexOutOfBoundsException
     */
    private static String getDeliveryReceiptValue(String attrName, String source) throws IndexOutOfBoundsException {
        String tmpAttr = attrName + ":";
        int startIndex = source.indexOf(tmpAttr);
        if (startIndex < 0)
            return null;
        startIndex = startIndex + tmpAttr.length();
        int endIndex = source.indexOf(" ", startIndex);
        if (endIndex > 0)
            return source.substring(startIndex, endIndex);
        return source.substring(startIndex);
    }

    /**
     * @param source
     * @return
     * @throws IndexOutOfBoundsException
     */
    private static String getDeliveryReceiptTextValue(String source) {
        String tmpAttr = DeliveryReceipt.DELREC_TEXT + ":";
        int startIndex = source.indexOf(tmpAttr);
        if (startIndex < 0)
            return null;
        startIndex = startIndex + tmpAttr.length();
        return source.substring(startIndex);
    }

    private static void assignHeader(Command pdu, SequentialBytesReader seqBytesReader) {
        int commandLength = seqBytesReader.readInt();
        if (seqBytesReader.getBytes().length != commandLength)
            logger.error("SYSTEM BUGS, the command_length (" + commandLength + ") not equals with the byte array length (" + seqBytesReader.getBytes().length + ")");
        pdu.setCommandLength(commandLength);
        pdu.setCommandId(seqBytesReader.readInt());
        pdu.setCommandStatus(seqBytesReader.readInt());
        pdu.setSequenceNumber(seqBytesReader.readInt());
    }

    private static void assignHeader(Command pdu, byte[] bytes) {
        assignHeader(pdu, new SequentialBytesReader(bytes));
    }

    private OptionalParameter[] readOptionalParameters(SequentialBytesReader reader) {
        if (!reader.hasMoreBytes())
            return null;
        List<OptionalParameter> params = new ArrayList<OptionalParameter>();
        while (reader.hasMoreBytes()) {
            short tag = reader.readShort();
            short length = reader.readShort();
            byte[] content = reader.readBytes(length);
            params.add(OptionalParameter.deserialize(tag, content));
        }
        return params.toArray(new OptionalParameter[params.size()]);
    }

    /**
     * Utility to read value from bytes sequentially.
     * 
     * @author uudashr
     * 
     */
    private static class SequentialBytesReader {
        private byte[] _bytes;
        private int _cursor;

        public SequentialBytesReader(byte[] bytes) {
            _bytes = bytes;
            _cursor = 0;
        }

        public byte readByte() {
            return _bytes[_cursor++];
        }

        public int readInt() {
            int val = OctetUtil.bytesToInt(_bytes, _cursor);
            _cursor += 4;
            return val;
        }

        public short readShort() {
            short value = OctetUtil.bytesToShort(_bytes, _cursor);
            _cursor += 2;
            return value;
        }

        public byte[] readBytesUntilNull() {
            // TODO uud: we can do some improvement here
            int i = _cursor;
            while (_bytes[i++] != (byte) 0) {
            }
            int length = i - 1 - _cursor;
            if (length == 0) {
                _cursor += 1 + length;
                return null;
            }
            byte[] data = new byte[length];
            System.arraycopy(_bytes, _cursor, data, 0, length);
            _cursor += 1 + length;
            return data;
        }

        /**
         * @return <tt>String</tt> value. Nullable.
         */
        public String readCString() {
            // TODO uud: we can do some improvement here
            int i = _cursor;
            while (_bytes[i++] != (byte) 0) {
            }
            int length = i - 1 - _cursor;
            if (length == 0) {
                _cursor += 1 + length;
                return null;
            }
            String val = new String(_bytes, _cursor, length);
            _cursor += 1 + length;
            return val;
        }

        public byte[] readBytes(int length) {
            if (length == 0)
                return null;
            byte[] data = new byte[length];
            System.arraycopy(_bytes, _cursor, data, 0, length);
            _cursor += length;
            return data;
        }

        public byte[] readBytes(byte length) {
            return readBytes(length & 0xff);
        }

        /**
         * @param length
         * @return <tt>String</tt> value. Nullable
         */
        public String readString(int length) {
            if (length == 0)
                return null;
            String val = new String(_bytes, _cursor, length);
            _cursor += length;
            return val;
        }

        /**
         * @param length
         * @return <tt>String</tt> value. Nullable.
         */
        public String readString(byte length) {
            /*
             * if (length == 0) return null;
             */
            /*
             * you have to convert the signed byte into unsined byte (in integer
             * representation) with & operand by 0xff
             */
            return readString(length & 0xff);
            /*
             * String val = new String(_bytes, _cursor, length & 0xff); _cursor +=
             * length; return val;
             */
        }

        public void resetCursor() {
            _cursor = 0;
        }

        public byte[] getBytes() {
            return _bytes;
        }

        public boolean hasMoreBytes() {
            return _cursor < (_bytes.length - 1);
        }
    }
}
