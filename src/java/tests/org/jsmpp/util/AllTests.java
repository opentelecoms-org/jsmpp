package org.jsmpp.util;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author uudashr
 *
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.jsmpp.util.test");
        //$JUnit-BEGIN$
        suite.addTest(new JUnit4TestAdapter(SequenceTest.class));
        suite.addTest(new JUnit4TestAdapter(DateFormatterTest.class));
        //$JUnit-END$
        return suite;
    }

}
