package org.jsmpp.bean;

import org.jsmpp.SMPPConstant;

/**
 * @author user
 * 
 */
public class SarTotalSegments {
    private byte value;

    public SarTotalSegments(byte value) {
        this.value = value;
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
        final SarTotalSegments other = (SarTotalSegments)obj;
        if (value != other.value)
            return false;
        return true;
    }
    
    
}
