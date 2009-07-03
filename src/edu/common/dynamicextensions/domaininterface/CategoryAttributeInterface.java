
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.databaseproperties.ColumnPropertiesInterface;
import edu.common.dynamicextensions.domaininterface.validationrules.RuleInterface;

/**
 *
 * @author mandar_shidhore
 *
 */
public interface CategoryAttributeInterface extends BaseAbstractAttributeInterface
{

	/**
	 *
	 * @return
	 */
	AbstractAttributeInterface getAbstractAttribute();

	/**
	 *
	 * @param attribute
	 */
	void setAbstractAttribute(AbstractAttributeInterface attribute);

	/**
	 *
	 * @return
	 */
	CategoryEntityInterface getCategoryEntity();

	/**
	 *
	 * @param categoryEntityInterface
	 */
	void setCategoryEntity(CategoryEntityInterface categoryEntityInterface);

	/**
	 *
	 * @return
	 */
	String getDefaultValue();

	/**
	 *
	 * @param permissibleValueInterface
	 */
	void setDefaultValue(PermissibleValueInterface permissibleValueInterface);

	/**
	 *
	 * @param dataElementInterface
	 */
	void setDataElement(DataElementInterface dataElementInterface);

	/**
	 * This method returns the ColumnProperties of the Attribute.
	 * @return the ColumnProperties of the Attribute.
	 */
	ColumnPropertiesInterface getColumnProperties();

	/**
	 * This method sets the ColumnProperties of the Attribute.
	 * @param columnProperties the ColumnProperties to be set.
	 */
	void setColumnProperties(ColumnPropertiesInterface columnProperties);

	/**
	 *
	 */
	Boolean getIsVisible();

	/**
	 *
	 */
	void setIsVisible(Boolean isVisible);

	Collection<RuleInterface> getRuleCollection();

	/**
	 * @param ruleCollection the ruleCollection to set
	 */
	void setRuleCollection(Collection<RuleInterface> ruleCollection);

	/**
	 *
	 */
	Boolean getIsRelatedAttribute();

	/**
	 *
	 */
	void setIsRelatedAttribute(Boolean isRelatedAttribute);
	
	/**
	 *
	 */
	Boolean getIsCalculated();

	/**
	 *
	 */
	void setIsCalculated(Boolean isCalculated);
	/**
	 * 
	 * @param formulaInterface
	 */
	void setFormula(FormulaInterface formulaInterface);
	/**
	 * This method return the formula.
	 * @return
	 */
	FormulaInterface getFormula();
	/**
	 * 
	 * @return
	 */
	Collection<CategoryAttributeInterface> getCalculatedCategoryAttributeCollection();
	/**
	 * 
	 * @param calculatedCategoryAttributeCollection
	 */
	void setCalculatedCategoryAttributeCollection(
			Collection<CategoryAttributeInterface> calculatedCategoryAttributeCollection);
	/**
	 * 
	 * @return
	 */
	public Collection<CategoryAttributeInterface> getCalculatedDependentCategoryAttributes();
	/**
	 * 
	 * @param calculatedDependentCategoryAttributes
	 */
	public void setCalculatedDependentCategoryAttributes(
			Collection<CategoryAttributeInterface> calculatedDependentCategoryAttributes);
	/**
	 *
	 */
	public void addCalculatedDependentCategoryAttribute(
			CategoryAttributeInterface categoryAttributeInterface);
	/**
	 *
	 */
	public void addCalculatedCategoryAttribute(
			CategoryAttributeInterface categoryAttributeInterface);
	/**
	 * 
	 * @return
	 */
	public PermissibleValueInterface getDefaultValuePermissibleValue();
	/**
	 * This method removes all Calculated Category Attributes.
	 */
	public void removeAllCalculatedDependentCategoryAttributes();
	/**
	 * This method removes all Calculated Category Attributes.
	 */
	public void removeAllCalculatedCategoryAttributes();
}