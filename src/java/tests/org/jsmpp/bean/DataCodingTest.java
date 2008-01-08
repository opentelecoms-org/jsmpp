package org.jsmpp.bean;

import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * @author uudashr
 *
 */
public class DataCodingTest {
	
    @Test(groups="checkintest")
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
		assertEquals(dataCoding.getMessageClass(), MessageClass.CLASS0);
		
		dataCoding.setMessageClass(MessageClass.CLASS1);
		assertEquals(dataCoding.getMessageClass(), MessageClass.CLASS1);
		
		dataCoding.setMessageClass(MessageClass.CLASS2);
		assertEquals(dataCoding.getMessageClass(), MessageClass.CLASS2);
		
		dataCoding.setMessageClass(MessageClass.CLASS3);
		assertEquals(dataCoding.getMessageClass(), MessageClass.CLASS3);
		
		
		dataCoding.setAlphabet(Alphabet.ALPHA_DEFAULT);
		assertEquals(dataCoding.getAlphabet(), Alphabet.ALPHA_DEFAULT);
		
		dataCoding.setAlphabet(Alphabet.ALPHA_8_BIT);
		assertEquals(dataCoding.getAlphabet(), Alphabet.ALPHA_8_BIT);
		
		dataCoding.setAlphabet(Alphabet.ALPHA_UCS2);
		assertEquals(dataCoding.getAlphabet(), Alphabet.ALPHA_UCS2);
		
		dataCoding.setAlphabet(Alphabet.ALPHA_RESERVED);
		assertEquals(dataCoding.getAlphabet(), Alphabet.ALPHA_RESERVED);
		
		assertTrue(GeneralDataCoding.isCompatible(dataCoding.value()));
		assertFalse(GeneralDataCoding.isCompatible((byte)0xff));
	}
	
    @Test(groups="checkintest")
	public void testDataCoding0x1() {
		byte value = (byte)0x11;
		DataCoding dataCoding = DataCoding.newInstance(value);
		assertTrue(dataCoding instanceof GeneralDataCoding);
		
		GeneralDataCoding coding = (GeneralDataCoding)dataCoding;
		assertFalse(coding.isCompressed());
		assertTrue(coding.isContainMessageClass());
		assertEquals(coding.getAlphabet(), Alphabet.ALPHA_DEFAULT);
		assertEquals(coding.getMessageClass(), MessageClass.CLASS1);
	}
}
