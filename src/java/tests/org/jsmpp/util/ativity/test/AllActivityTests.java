package org.jsmpp.util.ativity.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllActivityTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.jsmpp.util.ativity.test");
        //$JUnit-BEGIN$
        suite.addTestSuite(ActivityMonitorTest.class);
        //$JUnit-END$
        return suite;
    }

}
