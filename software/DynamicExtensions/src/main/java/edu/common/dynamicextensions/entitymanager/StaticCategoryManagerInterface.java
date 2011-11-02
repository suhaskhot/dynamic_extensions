
package edu.common.dynamicextensions.entitymanager;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.StaticCategoryInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * The Interface StaticCategoryManagerInterface.
 *
 * @author suhas_khot
 */

public interface StaticCategoryManagerInterface
{

	/**
	 * Method to persist a staticCategory.
	 * @param staticCategory static Category
	 * @return static category
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 */
	StaticCategoryInterface persistStaticCategory(final StaticCategoryInterface staticCategory)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

	/**
	 * It will return the Category with the id as given identifier in the parameter.
	 * @param identifier id of static category
	 * @return staticCategory with given identifier.
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	StaticCategoryInterface getStaticCategoryById(final Long identifier)
			throws DynamicExtensionsSystemException;

	/**
	 * It will return the Category with the name as given name in the parameter.
	 * @param name Category Name
	 * @return staticCategory with given identifier.
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	StaticCategoryInterface getStaticCategoryByName(final String name)
			throws DynamicExtensionsSystemException;

	/**
	 * It will fetch all the staticCategories present.
	 * @return will return the collection of staticCategories.
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 */
	Collection<StaticCategoryInterface> getAllStaticCategories()
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException;

}
