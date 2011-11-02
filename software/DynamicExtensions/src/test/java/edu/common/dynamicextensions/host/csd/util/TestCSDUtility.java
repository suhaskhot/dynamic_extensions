/**
 *
 */

package edu.common.dynamicextensions.host.csd.util;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;

/**
 * @author chetan_pundhir
 *
 */
public class TestCSDUtility extends DynamicExtensionsBaseTestCase
{

	/**
	 * Test merge string arrays.
	 * @throws DynamicExtensionsSystemException
	 */
	public void testGetAllCategoriesInfo() throws DynamicExtensionsSystemException
	{
		CSDUtility.getAllCategoriesInfo();
	}
}