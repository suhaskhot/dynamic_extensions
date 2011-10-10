
package edu.common.dynamicextensions.entitymanager;

import java.util.Collection;
import java.util.List;
import java.util.Stack;

import edu.common.dynamicextensions.domain.StaticCategory;
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;
import edu.common.dynamicextensions.domaininterface.StaticCategoryInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * @author suhas_khot
 */
public class StaticCategoryManager extends AbstractMetadataManager
		implements
			StaticCategoryManagerInterface
{

	/**
	 * Static instance of the CategoryManager.
	 */
	private static StaticCategoryManagerInterface staticCategoryManager = null;

	/**
	 * Static instance of the queryBuilder.
	 */
	private static DynamicExtensionBaseQueryBuilder queryBuilder = null;

	/**
	 * Empty Constructor.
	 */
	protected StaticCategoryManager()
	{
		super();
	}

	/**
	 * Returns the instance of the StaticCategoryManager.
	 * @return staticCategoryManager singleton instance of the StaticCategoryManager.
	 */
	public static synchronized StaticCategoryManagerInterface getInstance()
	{
		if (staticCategoryManager == null)
		{
			staticCategoryManager = new StaticCategoryManager();
			queryBuilder = QueryBuilderFactory.getQueryBuilder();
		}

		return staticCategoryManager;
	}

	/**
	 * Method to persist a staticCategory.
	 * @param staticCategory static Category
	 * @return static category
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 */
	public StaticCategoryInterface persistStaticCategory(
			final StaticCategoryInterface staticCategory) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		persistDynamicExtensionObject(staticCategory);
		return staticCategory;
	}

	/**
	 * It will fetch all the staticCategories present.
	 * @return will return the collection of staticCategories.
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 */
	public Collection<StaticCategoryInterface> getAllStaticCategories()
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return getAllObjects(StaticCategoryInterface.class.getName());
	}

	/**
	 * It will return the Category with the id as given identifier in the parameter.
	 * @param identifier identifier of static category
	 * @return staticCategory with given identifier.
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	public StaticCategoryInterface getStaticCategoryById(Long identifier)
			throws DynamicExtensionsSystemException
	{
		StaticCategoryInterface staticCategory = (StaticCategoryInterface) getObjectByIdentifier(
				StaticCategory.class.getName(), identifier.toString());
		return staticCategory;
	}

	/**
	 * It will return the Category with the name as given name in the parameter.
	 * @param name Category Name
	 * @return staticCategory with given identifier.
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	public StaticCategoryInterface getStaticCategoryByName(String name)
			throws DynamicExtensionsSystemException
	{
		StaticCategoryInterface staticCategory = (StaticCategoryInterface) getObjectByName(
				StaticCategoryInterface.class.getName(), name);
		return staticCategory;
	}

	/**
	 * It returns query builder object.
	 *  @return Query Builder Object.
	 */
	protected DynamicExtensionBaseQueryBuilder getQueryBuilderInstance()
	{
		return queryBuilder;
	}

	/**
	 * @param queries queries
	 * @param revQueries reverse queries
	 * @param rlbkQryStack roll back query stack
	 * @throws DynamicExtensionsSystemException Dynamic Extensions System Exception
	 */
	protected void postProcess(List<String> queries, List<String> revQueries,
			Stack<String> rlbkQryStack) throws DynamicExtensionsSystemException
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @param dyExtBsDmnObj base domain object
	 * @param revQueries reverse queries
	 * @param queries queries
	 * @throws DynamicExtensionsSystemException Dynamic Extensions System Exception
	 * @throws DynamicExtensionsApplicationException Dynamic Extensions Application Exception
	 */
	protected void preProcess(DynamicExtensionBaseDomainObjectInterface dyExtBsDmnObj,
			List<String> revQueries, List<String> queries) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		// TODO Auto-generated method stub

	}

	/**
	 * @param exception log the exception
	 * @param abstrMetadata abstract metaData
	 */
	protected void logFatalError(Exception exception, AbstractMetadataInterface abstrMetadata)
	{
		// TODO Auto-generated method stub

	}

}