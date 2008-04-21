
package edu.common.dynamicextensions.validation.category;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.parser.CategoryCSVFileParser;

/**
 * @author kunal_kamble
 *
 */
public class CategoryValidator
{
	EntityGroupInterface entityGroup;

	CategoryCSVFileParser categoryFileParser;

	StringBuffer errorMessage = new StringBuffer();

	/**
	 * @param entityGroup
	 */
	public CategoryValidator(CategoryCSVFileParser categoryFileParser)
	{
		this.categoryFileParser = categoryFileParser;
	}

	/**
	 * @return
	 */
	public EntityGroupInterface getEntityGroup()
	{
		return entityGroup;
	}

	/**
	 * @param entityGroup
	 */
	public void setEntityGroup(EntityGroupInterface entityGroup)
	{
		this.entityGroup = entityGroup;
	}

	/**
	 * @throws DynamicExtensionsSystemException
	 */
	public void validateMultiplicity() throws DynamicExtensionsSystemException
	{
		if (categoryFileParser.readLine()[0].split(":").length < 2)
		{
			throw new DynamicExtensionsSystemException(getErrorMessageStart() + " miltiplicity not defined.");
		}
	}

	/**
	 * @param entityName
	 * @throws DynamicExtensionsSystemException
	 */
	public void validateEntityName(String entityName) throws DynamicExtensionsSystemException
	{
		String errorMessage = getErrorMessageStart() + "Entity with name " + entityName + " does not exist";
		checkForNullRefernce(entityGroup.getEntityByName(entityName), errorMessage);
	}

	/**
	 * 
	 */
	public void validateSubcategoryTag()
	{
		categoryFileParser.getTargetContainerCaption();
	}

	/**
	 * @param object
	 * @param message
	 * @throws DynamicExtensionsSystemException
	 */
	public static void checkForNullRefernce(Object object, String message) throws DynamicExtensionsSystemException
	{
		if (object == null)
		{
			throw new DynamicExtensionsSystemException(message);
		}
	}

	/**
	 * @return
	 */
	private String getErrorMessageStart()
	{
		return "Error at line:" + categoryFileParser.getLineNumber() + " ";
	}

}
