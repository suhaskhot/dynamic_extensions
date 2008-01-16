
package edu.common.dynamicextensions.domaininterface;

import edu.common.dynamicextensions.domaininterface.userinterface.AssociationControlInterface;

public interface CategoryAssociationInterface extends BaseAbstractAttributeInterface 
{
	public CategoryEntityInterface getCategoryEntity();

	public void setCategoryEntity(CategoryEntityInterface categoryEntity);
	

}
