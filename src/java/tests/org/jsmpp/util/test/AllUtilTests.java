package org.jsmpp.util.test;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author uudashr
 *
 */
public class AllUtilTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.jsmpp.util");
        //$JUnit-BEGIN$
        suite.addTestSuite(SequenceTest.class);
        //$JUnit-END$
        return suite;
    }

}
