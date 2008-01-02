package org.jsmpp.session;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author uudashr
 *
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.jsmpp.session");
        //$JUnit-BEGIN$
        suite.addTest(new JUnit4TestAdapter(BindRequestTest.class));
        suite.addTest(new JUnit4TestAdapter(BindRequestReceiverTest.class));
        //$JUnit-END$
        return suite;
    }

}
