package edu.common.dynamicextensions.entitymanager;

import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
*
* @author rajesh_patil
*
*/
public interface CategoryManagerInterface
{
    public CategoryInterface persistCategory(CategoryInterface categoryInterface) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;
    
    public CategoryInterface deleteCategory(CategoryInterface categoryInterface) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;
}
