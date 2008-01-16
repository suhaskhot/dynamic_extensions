
package edu.common.dynamicextensions.domaininterface;

import edu.common.dynamicextensions.domain.CategoryEntity;

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
	public CategoryEntity getCategoryEntity();

	/**
	 * 
	 * @param categoryEntity
	 */
	public void setCategoryEntity(CategoryEntity categoryEntity);
	
	/**
	 * 
	 * @return
	 */
	public CategoryEntity getTargetCategoryEntity();
	
	/**
	 * 
	 * @param targetCategoryEntity
	 */
	public void setTargetCategoryEntity(CategoryEntity targetCategoryEntity);

}
