
package edu.common.dynamicextensions.domaininterface;


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

}
