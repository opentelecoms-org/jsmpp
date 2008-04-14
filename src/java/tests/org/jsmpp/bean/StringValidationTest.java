package org.jsmpp.bean;


import org.jsmpp.PDUStringException;
import org.jsmpp.util.StringParameter;
import org.jsmpp.util.StringValidator;
import static org.testng.Assert.*;
import org.testng.annotations.Test;

/**
 * Test case of string validation.
 * @author uudashr
 * @version 1.0
 *
 */
public class StringValidationTest {
    
    @Test(groups="checkintest")
    public void testValidation() {
        try {
            StringValidator.validateString("", StringParameter.SYSTEM_ID);
        } catch (PDUStringException e) {
            fail("Should be okay inserting empty string");
        }
        
        try {
            StringValidator.validateString((String)null, StringParameter.SYSTEM_ID);
        } catch (PDUStringException e) {
            fail("Should be okay inserting null string");
        }
        
        try {
            StringValidator.validateString("smsgw", StringParameter.SYSTEM_ID);
        } catch (PDUStringException e) {
            fail("Should be okay inserting string that has length less than 16");
        }
        
        try {
            StringValidator.validateString("smsgwsmsgwsmsgw", StringParameter.SYSTEM_ID);
        } catch (PDUStringException e) {
            fail("Should be okay inserting 15 char of string");
        }
        
        try {
            StringValidator.validateString("smsgwsmsgwsmsgwe", StringParameter.SYSTEM_ID);
            fail("Should fail inserting 16 char of string");
        } catch (PDUStringException e) {
        }
        
        try {
            StringValidator.validateString("smsgwsmsgwsmsgwee", StringParameter.SYSTEM_ID);
            fail("Should be fail inserting 16 char of string");
        } catch (PDUStringException e) {
        }
    }
}
