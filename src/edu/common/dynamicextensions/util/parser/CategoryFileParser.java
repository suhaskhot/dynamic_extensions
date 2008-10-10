
package edu.common.dynamicextensions.util.parser;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.SemanticPropertyInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.FileReader;
import edu.common.dynamicextensions.validation.category.CategoryValidator;
import edu.wustl.common.util.dbManager.DAOException;

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
	public abstract String getEntityName() throws DynamicExtensionsSystemException, DAOException, ClassNotFoundException;

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
	 * Return a map of rules belonging to a category attribute.
	 * @return
	 */
	public abstract Map<String, Object> getRules(String attributeName) throws DynamicExtensionsSystemException;

	/**
	 * @return list of permissible values
	 * @throws DynamicExtensionsSystemException
	 */
	public abstract Map<String, Collection<SemanticPropertyInterface>> getPermissibleValues() throws DynamicExtensionsSystemException;

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

	/**
	 * @return
	 */
	public abstract boolean hasRelatedAttributes();

	/**
	 * @return
	 */
	public abstract boolean hasInsatanceInformation();

	/**
	 * @return
	 */
	public abstract String getDefaultValueForRelatedAttribute();

	/**
	 * @return
	 */
	public abstract String getRelatedAttributeName();

	/**
	 * @return
	 */
	public abstract String getDefaultValue();
}
