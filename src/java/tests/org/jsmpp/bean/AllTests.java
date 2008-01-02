package org.jsmpp.bean;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author uudashr
 *
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.jsmpp.bean.test");
        //$JUnit-BEGIN$
        suite.addTest(new JUnit4TestAdapter(DataCodingTest.class));
        suite.addTest(new JUnit4TestAdapter(ESMClassTest.class));
        suite.addTest(new JUnit4TestAdapter(RegisteredDeliveryTest.class));
        suite.addTest(new JUnit4TestAdapter(ReplaceIfPresentFlagTest.class));
        suite.addTest(new JUnit4TestAdapter(StringValidationTest.class));
        //$JUnit-END$
        return suite;
    }

}
