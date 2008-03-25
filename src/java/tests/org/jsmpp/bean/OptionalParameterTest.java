package org.jsmpp.bean;

import org.jsmpp.bean.OptionalParameter.OctetString;
import org.jsmpp.bean.OptionalParameter.Tag;
import org.jsmpp.util.OctetUtil;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

public class OptionalParameterTest {

    @Test(groups="checkintest")
    public byte[] stringParameterSerialisation() {
        OptionalParameter param = new OptionalParameter.OctetString(Tag.dest_subaddress, "jeah");
        byte[] serialised = param.serialize();
        assertEquals(serialised.length, 2 + 2 + 4);
        assertEquals(OctetUtil.bytesToShort(serialised, 0), Tag.dest_subaddress.value());
        assertEquals(OctetUtil.bytesToShort(serialised, 2), "jeah".getBytes().length);
        assertEquals(new String(serialised, 4, serialised.length - 4), "jeah");
        return serialised;
    }
    
    @Test(groups="checkintest")
    public void stringParameterDeserialisation() {
        OptionalParameter param = OptionalParameter.deserialize(Tag.dest_subaddress.value(), "jeah".getBytes());
        assertTrue(param instanceof OptionalParameter.OctetString);
        OptionalParameter.OctetString stringParam = (OctetString) param;
        assertEquals(stringParam.value, "jeah");
    }
}