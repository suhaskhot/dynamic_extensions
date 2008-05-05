
package edu.common.dynamicextensions.domaininterface;

import edu.common.dynamicextensions.domaininterface.databaseproperties.ConstraintPropertiesInterface;


/**
 *
 * @author sujay_narkar
 *
 */
public interface CategoryAssociationInterface extends BaseAbstractAttributeInterface
{
	/**
	 *
	 * @return
	 */
	public CategoryEntityInterface getCategoryEntity();

	/**
	 *
	 * @param categoryEntity
	 */
	public void setCategoryEntity(CategoryEntityInterface categoryEntity);

	/**
	 *
	 * @return
	 */
	public CategoryEntityInterface getTargetCategoryEntity();

	/**
	 *
	 * @param targetCategoryEntity
	 */
	public void setTargetCategoryEntity(CategoryEntityInterface targetCategoryEntity);
	/**
	 * This method returns the ConstraintProperties of the Association.
	 * @return the ConstraintProperties of the Association.
	 */
	public ConstraintPropertiesInterface getConstraintProperties();
	/**
	 * This method sets the constraintProperties to the given ContraintProperties.
	 * @param constraintProperties the constraintProperties to be set.
	 */
	public void setConstraintProperties(ConstraintPropertiesInterface constraintProperties);

}
