package org.jsmpp.bean;

import org.jsmpp.bean.OptionalParameter.Byte;
import org.jsmpp.bean.OptionalParameter.COctetString;
import org.jsmpp.bean.OptionalParameter.Int;
import org.jsmpp.bean.OptionalParameter.Null;
import org.jsmpp.bean.OptionalParameter.OctetString;
import org.jsmpp.bean.OptionalParameter.Short;
import org.jsmpp.bean.OptionalParameter.Tag;

/**
 * @author uudashr
 *
 */
public class OptionalParameters {
    
    /**
     * Create SAR_MESSAGE_REF_NUM TLV instance.
     * 
     * @param value is the value.
     * @return the optional parameter.
     */
    public static OptionalParameter.Short newSarMsgRefNum(short value) {
        return new OptionalParameter.Short(Tag.SAR_MSG_REF_NUM, value);
    }
    
    /**
     * Create SAR_MESSAGE_REF_NUM TLV instance.
     * The value will cast automatically into short type.
     * 
     * @param value is the value.
     * @return the optional parameter.
     */
    public static OptionalParameter.Short newSarMsgRefNum(int value) {
        return newSarMsgRefNum((byte)value);
    }
    
    /**
     * Create SAR_SEGMENT_SEQNUM TLV instance.
     * 
     * @param value is the value.
     * @return the optional parameter.
     */
    public static OptionalParameter.Byte newSarSegmentSeqnum(byte value) {
        return new OptionalParameter.Byte(Tag.SAR_SEGMENT_SEQNUM, value);
    }
    
    /**
     * Create SAR_SEGMENT_SEQNUM TLV instance.
     * The value will cast automatically into byte type.
     * 
     * @param value is the value.
     * @return the optional parameter.
     */
    public static OptionalParameter.Byte newSarSegmentSeqnum(int value) {
        return newSarSegmentSeqnum((byte)value);
    }
    
    /**
     * Create SAR_TOTAL_SEGMENTS TLV instance.
     * 
     * @param value is the value.
     * @return the optional parameter.
     */
    public static OptionalParameter.Byte newSarTotalSegments(byte value) {
        return new OptionalParameter.Byte(Tag.SAR_TOTAL_SEGMENTS, value);
    }
    
    /**
     * Create SAR_TOTAL_SEGMENTS TLV instance.
     * The value will cast automatically into byte type.
     * 
     * @param value is the value.
     * @return the optional parameter.
     */
    public static OptionalParameter.Byte newSarTotalSegments(int value) {
        return newSarSegmentSeqnum((byte)value);
    }
    
    public static OptionalParameter deserialize(short tagCode, byte[] content) {
        Tag tag = Tag.valueOf(tagCode);
        if (Null.class.equals(tag.type)) {
            return new Null(tag);
        }
        if (Byte.class.equals(tag.type)) {
            return new Byte(tag, content);
        }
        if (Short.class.equals(tag.type)) {
            return new Short(tag, content);
        }
        if (Int.class.equals(tag.type)) {
            return new Int(tag, content);
        }
        if (OctetString.class.equals(tag.type)) {
            return new OctetString(tag, content);
        }
        if (COctetString.class.equals(tag.type)) {
            return new COctetString(tag, content);
        }
        throw new IllegalArgumentException("Unsupported tag: " + tagCode);
    }
}
