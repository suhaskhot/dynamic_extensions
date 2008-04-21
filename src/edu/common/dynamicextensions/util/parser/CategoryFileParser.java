
package edu.common.dynamicextensions.util.parser;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.FileReader;
import edu.common.dynamicextensions.validation.category.CategoryValidator;

/**
 * @author kunal_kamble
 *
 */
public abstract class CategoryFileParser extends FileReader
{
	protected CategoryValidator categoryValidator;

	/**
	 * @param filePath
	 * @throws DynamicExtensionsSystemException
	 */
	public CategoryFileParser(String filePath) throws DynamicExtensionsSystemException
	{
		super(filePath);
	}

	/**
	 * @return current line number in the category file
	 */
	public abstract long getLineNumber();

	/**
	 * @return false if end of category file 
	 * otherwise reads the next line.
	 * @throws IOException
	 */
	public abstract boolean readNext() throws IOException;

	/**
	 * @return category name
	 */
	public abstract String getCategoryName();

	/**
	 * @return entity group name
	 */
	public abstract String getEntityGroupName();

	/**
	 * @return map with key as entity name and 
	 * value as its complete path from the root 
	 * @throws DynamicExtensionsSystemException
	 */
	public abstract Map<String, List<String>> getPaths() throws DynamicExtensionsSystemException;

	/**
	 * @return form label
	 */
	public abstract String getDisplyLable();

	/**
	 * @return 
	 */
	public abstract boolean isShowCaption();

	/**
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public abstract String getEntityName() throws DynamicExtensionsSystemException;

	/**
	 * @return attribute name
	 */
	public abstract String getAttributeName();

	/**
	 * @return type of control
	 */
	public abstract String getControlType();

	/** 
	 * @return control label
	 */
	public abstract String getControlCaption();

	/**
	 * @return map with key as control property and
	 * value as value for the property
	 */
	public abstract Map<String, String> getControlOptions();

	/**
	 * @return list of prmissible values
	 * @throws DynamicExtensionsSystemException
	 */
	public abstract List<String> getPermissibleValues() throws DynamicExtensionsSystemException;

	/**
	 * @return 
	 */
	public abstract boolean hasDisplayLable();

	/**
	 * @return
	 */
	public abstract boolean hasFormDefination();

	/**
	 * @return
	 */
	public abstract boolean hasSubcategory();

	/**
	 * @return target container caption
	 */
	public abstract String getTargetContainerCaption();

	/**
	 * @return multiplicity
	 */
	/**
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public abstract String getMultiplicity() throws DynamicExtensionsSystemException;

	/**
	 * @return category path
	 */
	public abstract String[] getCategoryPaths();

	/**
	 * @return
	 */
	public CategoryValidator getCategoryValidator()
	{
		return categoryValidator;
	}

	/**
	 * @param categoryValidator
	 */
	public void setCategoryValidator(CategoryValidator categoryValidator)
	{
		this.categoryValidator = categoryValidator;
	}

}
