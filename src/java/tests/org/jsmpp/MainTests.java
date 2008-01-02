package org.jsmpp;



import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author uudashr
 *
 */
public class MainTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.jsmpp");
        //$JUnit-BEGIN$
        suite.addTest(org.jsmpp.bean.AllTests.suite());
        suite.addTest(org.jsmpp.extra.AllTests.suite());
        suite.addTest(org.jsmpp.util.AllTests.suite());
        suite.addTest(org.jsmpp.session.AllTests.suite());
        //$JUnit-END$
        return suite;
    }

}
