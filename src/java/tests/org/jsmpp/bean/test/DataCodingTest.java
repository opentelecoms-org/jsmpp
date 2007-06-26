package org.jsmpp.bean.test;

import org.jsmpp.bean.Alphabet;
import org.jsmpp.bean.DataCoding;
import org.jsmpp.bean.GeneralDataCoding;
import org.jsmpp.bean.MessageClass;

import junit.framework.TestCase;

/**
 * @author uudashr
 *
 */
public class DataCodingTest extends TestCase {
	
	@Override
	protected void setUp() throws Exception {
	}
	
	@Override
	protected void tearDown() throws Exception {
	}
	
	public void testGeneralDataCoding() {
		GeneralDataCoding dataCoding = new GeneralDataCoding();
		dataCoding = dataCoding.composeCompressed(true);
		assertTrue(dataCoding.isCompressed());
		dataCoding = dataCoding.composeCompressed(false);
		assertFalse(dataCoding.isCompressed());
		
		dataCoding = dataCoding.composeContainMessageClass(true);
		assertTrue(dataCoding.isContainMessageClass());
		
		dataCoding = dataCoding.composeContainMessageClass(false);
		assertFalse(dataCoding.isContainMessageClass());
		
		dataCoding = dataCoding.composeContainMessageClass(true);
		
		dataCoding = dataCoding.composeMessageClass(MessageClass.CLASS0);
		assertEquals(MessageClass.CLASS0, dataCoding.getMessageClass());
		dataCoding = dataCoding.composeMessageClass(MessageClass.CLASS1);
		assertEquals(MessageClass.CLASS1, dataCoding.getMessageClass());
		dataCoding = dataCoding.composeMessageClass(MessageClass.CLASS2);
		assertEquals(MessageClass.CLASS2, dataCoding.getMessageClass());
		dataCoding = dataCoding.composeMessageClass(MessageClass.CLASS3);
		assertEquals(MessageClass.CLASS3, dataCoding.getMessageClass());
		
		
		dataCoding = dataCoding.composeAlphabet(Alphabet.ALPHA_DEFAULT);
		assertEquals(Alphabet.ALPHA_DEFAULT, dataCoding.getAlphabet());
		dataCoding = dataCoding.composeAlphabet(Alphabet.ALPHA_8_BIT);
		assertEquals(Alphabet.ALPHA_8_BIT, dataCoding.getAlphabet());
		dataCoding = dataCoding.composeAlphabet(Alphabet.ALPHA_UCS2);
		assertEquals(Alphabet.ALPHA_UCS2, dataCoding.getAlphabet());
		dataCoding = dataCoding.composeAlphabet(Alphabet.ALPHA_RESERVED);
		assertEquals(Alphabet.ALPHA_RESERVED, dataCoding.getAlphabet());
		
		assertTrue(GeneralDataCoding.isCompatible(dataCoding.value()));
		assertFalse(GeneralDataCoding.isCompatible((byte)0xff));
	}
	
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
