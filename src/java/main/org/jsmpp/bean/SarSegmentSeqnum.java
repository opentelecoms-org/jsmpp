package org.jsmpp.bean;

import org.jsmpp.SMPPConstant;

/**
 * @author user
 * 
 */
public class SarSegmentSeqnum {
    private byte _value;

    public SarSegmentSeqnum(byte value) {
        _value = value;
    }

    public SarSegmentSeqnum(short value) {
        this((byte)value);
    }

    public short getTag() {
        return SMPPConstant.TAG_SAR_SEGMENT_SEQNUM;
    }

    public short getLength() {
        return 2;
    }

    public short getValue() {
        return _value;
    }
}
