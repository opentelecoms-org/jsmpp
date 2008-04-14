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
