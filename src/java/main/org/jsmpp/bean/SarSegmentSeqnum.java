package org.jsmpp.bean;

import org.jsmpp.SMPPConstant;

/**
 * @author user
 * 
 */
public class SarSegmentSeqnum {
    private byte value;

    public SarSegmentSeqnum(byte value) {
        this.value = value;
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
        return value;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
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
        final SarSegmentSeqnum other = (SarSegmentSeqnum)obj;
        if (value != other.value)
            return false;
        return true;
    }
    
    
}
