package org.jsmpp.bean;

import org.jsmpp.SMPPConstant;

/**
 * @author user
 * 
 */
public class SarTotalSegments {
    private byte _value;

    public SarTotalSegments(byte value) {
        _value = value;
    }

    public SarTotalSegments(short value) {
        this((byte)value);
    }

    public short getTag() {
        return SMPPConstant.TAG_SAR_TOTAl_SEGMENTS;
    }

    public short getLength() {
        return 2;
    }

    public short getValue() {
        return _value;
    }
}
