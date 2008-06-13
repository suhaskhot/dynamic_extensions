
package edu.common.dynamicextensions.util;

import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.util.CategoryHelper;
import edu.common.dynamicextensions.util.CategoryHelperInterface;
import edu.common.dynamicextensions.util.parser.CategoryGenerator;

/**
 * 
 * @author mandar_shidhore
 *
 */
public class CategoryCreator
{
	public static void main(String[] args)
	{
		try
		{
			if (args.length == 0)
			{
				throw new Exception("Please Specify the path for .csv file");
			}
			String filePath = args[0];
			System.out.println("---- The .csv file path is " + filePath + " ----");

			CategoryGenerator categoryFileParser = new CategoryGenerator(filePath);
			CategoryHelperInterface categoryHelper = new CategoryHelper();

			boolean isEdited = true;
			for (CategoryInterface category : categoryFileParser.getCategoryList())
			{

				if(category.getId() == null)
				{
					isEdited = false;					
				}
				categoryHelper.saveCategory(category);
				
				if(isEdited)
				{
					System.out.println("Edited category " + category.getName() +" successfully");						
				}
				else
				{
					System.out.println("Saved category " + category.getName() +  " successfully");
				}
				
			}
		}
		catch (Exception ex)
		{
			System.out.println("Exception: " + ex.getMessage());
			throw new RuntimeException(ex);
		}
	}

}
