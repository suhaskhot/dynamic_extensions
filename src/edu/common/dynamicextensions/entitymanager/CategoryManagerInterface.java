package edu.common.dynamicextensions.entitymanager;

import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
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
	/**
	 *
	 * @param categoryInterface
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
    CategoryInterface persistCategory(CategoryInterface categoryInterface) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;
    /**
     *
     * @param categoryInterface
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    CategoryInterface persistCategoryMetadata(CategoryInterface categoryInterface) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;
    /**
     *
     * @param entity
     * @param dataValue
     * @return
     * @throws DynamicExtensionsApplicationException
     * @throws DynamicExtensionsSystemException
     */
    Long insertData(CategoryInterface category, Map<AbstractMetadataInterface, ?> dataValue)
            throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException;

    /**
     *
     * @param category
     * @param dataValueMapList
     * @return
     * @throws DynamicExtensionsApplicationException
     * @throws DynamicExtensionsSystemException
     */
    List<Long> insertData(CategoryInterface category, List<Map<AbstractMetadataInterface, ?>> dataValueMapList)
            throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException;
    /**
     *
     * @param category
     * @return
     * @throws DynamicExtensionsSystemException
     * @throws DynamicExtensionsApplicationException
     */
    CategoryInterface deleteCategory(CategoryInterface categoryInterface) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;
}
