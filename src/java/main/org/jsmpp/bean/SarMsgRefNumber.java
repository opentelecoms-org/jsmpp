package org.jsmpp.bean;

import org.jsmpp.SMPPConstant;

/**
 * @author uudashr
 * 
 */
public class SarMsgRefNumber {
    public static final int MAX_VALUE = 65535;
    private short _value;

    public SarMsgRefNumber(short value) {
        _value = value;
    }

    public SarMsgRefNumber(int value) {
        this((short)value);
    }

    public short getTag() {
        return SMPPConstant.TAG_SAR_MSG_REF_NUM;
    }

    public short getLength() {
        return 2;
    }

    public short getValue() {
        return _value;
    }
}
