package org.jsmpp.test;

import org.jsmpp.bean.test.AllBeanTests;
import org.jsmpp.util.ativity.test.AllActivityTests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * All test for package org.jsmpp
 * 
 * @author uudashr
 * @version 1.0
 * @since 2.0
 *
 */
public class AllTests {

    public static Test suite() {
        TestSuite suite = new TestSuite("Test for org.jsmpp.test");
        //$JUnit-BEGIN$
        suite.addTest(AllBeanTests.suite());
        suite.addTest(AllActivityTests.suite());
        //$JUnit-END$
        return suite;
    }

}
