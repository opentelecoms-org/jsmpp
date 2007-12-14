package org.jsmpp.extra.test;

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
        suite.addTestSuite(PendingResponseTest.class);
        //$JUnit-END$
        return suite;
    }

}
