package org.jsmpp.bean.test;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllBeanTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.jsmpp.bean.test");
		//$JUnit-BEGIN$
		suite.addTestSuite(DataCodingTest.class);
		suite.addTestSuite(ESMClassTest.class);
		suite.addTestSuite(RegisteredDeliveryTest.class);
		suite.addTestSuite(ReplaceIfPresentFlagTest.class);
		suite.addTestSuite(StringValidationTest.class);
		//$JUnit-END$
		return suite;
	}

}
