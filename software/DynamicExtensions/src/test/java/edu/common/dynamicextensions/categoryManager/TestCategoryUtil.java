
package edu.common.dynamicextensions.categoryManager;

import edu.common.dynamicextensions.domain.Category;
import edu.common.dynamicextensions.entitymanager.CategoryManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.CategoryUtil;
import edu.common.dynamicextensions.util.DynamicExtensionsBaseTestCase;

public class TestCategoryUtil extends DynamicExtensionsBaseTestCase
{

	/**
	* This  test method is used for deleting category from database using category Id as parameter.
	* @throws DynamicExtensionsSystemException
	*/
	public void testDeleteCategoryById() throws DynamicExtensionsSystemException
	{
		CategoryUtil categoryUtil = new CategoryUtil();
		
		//Fetching category from database by passing its name.
		Category category = (Category) CategoryManager.getInstance()
				.getCategoryByName("TestCase42");
		
		Long categoryId =  category.getId();
		
		boolean result = categoryUtil.deleteCategoryById(categoryId);
		try
		{

			if (result)
			{
				assertTrue("Successfully deleted category...", true);
			}
			else
			{
				assertFalse("Unable to delete category...", true);
			}
		}
		catch (Exception e)
		{
			assertFalse("Error occured while deleting category...", true);
			e.printStackTrace();
		}
	}

	/**
	* This test method is used for deleting category from database using categoryName as a parameter.
	* @throws DynamicExtensionsSystemException
	*/
	public void testDeleteCategoryByName() throws DynamicExtensionsSystemException
	{
		CategoryUtil categoryUtil = new CategoryUtil();
		String categoryName = "TestCase82_negative";
		boolean result = categoryUtil.deleteCategoryByName(categoryName);
		try
		{

			if (result)
			{
				assertTrue("Successfully deleted category...", true);
			}
			else
			{
				assertFalse("Unable to delete category...", true);
			}
		}
		catch (Exception e)
		{
			assertFalse("Error occured while deleting category...", true);
			e.printStackTrace();
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{

	}

}
