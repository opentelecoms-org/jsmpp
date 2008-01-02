package org.jsmpp.extra;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author uudashr
 *
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.jsmpp.extra.test");
        //$JUnit-BEGIN$
        suite.addTest(new JUnit4TestAdapter(PendingResponseTest.class));
        suite.addTest(new JUnit4TestAdapter(SessionStateTest.class));
        //$JUnit-END$
        return suite;
    }

}
