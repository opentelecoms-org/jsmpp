package org.jsmpp.bean;

import org.jsmpp.SMPPConstant;

/**
 * @author uudashr
 * 
 */
public class SarMsgRefNumber {
    public static final int MAX_VALUE = 65535;
    private short value;

    public SarMsgRefNumber(short value) {
        this.value = value;
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
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + value;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SarMsgRefNumber other = (SarMsgRefNumber)obj;
        if (value != other.value)
            return false;
        return true;
    }
    
    
}
