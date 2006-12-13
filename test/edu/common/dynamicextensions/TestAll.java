
package edu.common.dynamicextensions;

/**
 * 
 */

import junit.framework.Test;
import junit.framework.TestSuite;
import edu.common.dynamicextensions.entitymanager.TestEntityManager;
import edu.common.dynamicextensions.entitymanager.TestEntityManagerForAssociations;

/**
 * Test Suite for testing all Query Interface related classes.
 */
public class TestAll
{

	public static void main(String[] args)
	{
		junit.swingui.TestRunner.run(TestAll.class);
	}

	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test suite for Query Interface Classes");
		suite.addTestSuite(TestEntityManager.class);
		suite.addTestSuite(TestEntityManagerForAssociations.class);
		return suite;
	}
}
