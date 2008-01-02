package org.jsmpp.bean;

import static org.junit.Assert.*;
import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;
import org.junit.Test;

/**
 * @author uudashr
 *
 */
public class DataCodingTest {
	
    @Test
	public void testGeneralDataCoding() {
		GeneralDataCoding dataCoding = new GeneralDataCoding();
		dataCoding.setCompressed(true);
		assertTrue(dataCoding.isCompressed());
		
		dataCoding.setCompressed(false);
		assertFalse(dataCoding.isCompressed());
		
		dataCoding.setContainMessageClass(true);
		assertTrue(dataCoding.isContainMessageClass());
		
		dataCoding.setContainMessageClass(false);
		assertFalse(dataCoding.isContainMessageClass());
		
		dataCoding.setContainMessageClass(true);
		
		dataCoding.setMessageClass(MessageClass.CLASS0);
		assertEquals(MessageClass.CLASS0, dataCoding.getMessageClass());
		dataCoding.setMessageClass(MessageClass.CLASS1);
		assertEquals(MessageClass.CLASS1, dataCoding.getMessageClass());
		dataCoding.setMessageClass(MessageClass.CLASS2);
		assertEquals(MessageClass.CLASS2, dataCoding.getMessageClass());
		dataCoding.setMessageClass(MessageClass.CLASS3);
		assertEquals(MessageClass.CLASS3, dataCoding.getMessageClass());
		
		
		dataCoding.setAlphabet(Alphabet.ALPHA_DEFAULT);
		assertEquals(Alphabet.ALPHA_DEFAULT, dataCoding.getAlphabet());
		dataCoding.setAlphabet(Alphabet.ALPHA_8_BIT);
		assertEquals(Alphabet.ALPHA_8_BIT, dataCoding.getAlphabet());
		dataCoding.setAlphabet(Alphabet.ALPHA_UCS2);
		assertEquals(Alphabet.ALPHA_UCS2, dataCoding.getAlphabet());
		dataCoding.setAlphabet(Alphabet.ALPHA_RESERVED);
		assertEquals(Alphabet.ALPHA_RESERVED, dataCoding.getAlphabet());
		
		assertTrue(GeneralDataCoding.isCompatible(dataCoding.value()));
		assertFalse(GeneralDataCoding.isCompatible((byte)0xff));
	}
	
    @Test
	public void testDataCoding0x1() {
		byte value = (byte)0x11;
		DataCoding dataCoding = DataCoding.newInstance(value);
		assertTrue(dataCoding instanceof GeneralDataCoding);
		
		GeneralDataCoding coding = (GeneralDataCoding)dataCoding;
		assertFalse(coding.isCompressed());
		assertTrue(coding.isContainMessageClass());
		assertEquals(Alphabet.ALPHA_DEFAULT, coding.getAlphabet());
		assertEquals(MessageClass.CLASS1, coding.getMessageClass());
	}
}
