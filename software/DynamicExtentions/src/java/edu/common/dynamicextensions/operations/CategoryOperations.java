/**
 *
 */

package edu.common.dynamicextensions.operations;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.entitymanager.AbstractMetadataManager;
import edu.common.dynamicextensions.entitymanager.CategoryManager;
import edu.common.dynamicextensions.entitymanager.CategoryManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.skiplogic.SkipLogic;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.DBTypes;
import edu.wustl.dao.util.NamedQueryParam;

/**
 * @author Gaurav_mehta
 *
 */
public class CategoryOperations
{

	static
	{
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}

	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getCommonLogger(CategoryOperations.class);

	/** The Constant DYEXTN_S_003. */
	private static final String DYEXTN_S_003 = "DYEXTN_S_003";

	/**
	 * This method will save the Category to DB.
	 * @param isPersistMetadataOnly the is persist metadata only
	 * @param category the category
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 */
	public void saveCategory(boolean isPersistMetadataOnly, CategoryInterface category)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		if (isPersistMetadataOnly)
		{
			saveCategoryMetadata(category);
		}
		else
		{
			final CategoryManagerInterface categoryManager = CategoryManager.getInstance();
			categoryManager.persistDynamicExtensionObjectForCategory(category);
		}
	}

	/**
	 * Save category metadata.
	 * @param category the category
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 */
	private void saveCategoryMetadata(CategoryInterface category)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		try
		{
			CategoryManagerInterface categoryManager = CategoryManager.getInstance();
			categoryManager.persistCategoryMetadata(category);
		}
		catch (DynamicExtensionsSystemException e)
		{
			LOGGER.error(e.getMessage());
			throw new DynamicExtensionsSystemException("ERROR WHILE SAVING A CATEGORY", e);
		}
		catch (DynamicExtensionsApplicationException e)
		{
			LOGGER.error(e.getMessage());
			throw new DynamicExtensionsApplicationException("ERROR WHILE SAVING A CATEGORY", e);
		}
	}

	/**
	 * Persist skip logic.
	 * @param skipLogic the skip logics
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	public void persistSkipLogic(SkipLogic skipLogic) throws DynamicExtensionsSystemException
	{
		HibernateDAO hibernateDAO = null;
		try
		{
			hibernateDAO = DynamicExtensionsUtility.getHibernateDAO();
			hibernateDAO.insert(skipLogic);
			hibernateDAO.commit();
		}
		catch (DynamicExtensionsSystemException e)
		{
			LOGGER.error(e.getMessage());
			throw new DynamicExtensionsSystemException(e.getMessage(), e, DYEXTN_S_003);
		}
		catch (DAOException e)
		{
			LOGGER.error(e.getMessage());
			throw new DynamicExtensionsSystemException(e.getMessage(), e, DYEXTN_S_003);
		}
		finally
		{
			closeDAO(hibernateDAO);
		}
	}

	/**
	 * Delete Skip Logic associated with the Container.
	 * @param skipLogic the skip logic
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	public void deleteSkipLogicObject(SkipLogic skipLogic) throws DynamicExtensionsSystemException
	{
		if (skipLogic != null)
		{
			HibernateDAO hibernateDAO = null;
			try
			{
				hibernateDAO = DynamicExtensionsUtility.getHibernateDAO();
				hibernateDAO.delete(skipLogic);
				hibernateDAO.commit();
			}
			catch (DynamicExtensionsSystemException e)
			{
				LOGGER.error(e.getMessage());
				throw new DynamicExtensionsSystemException(e.getMessage(), e, DYEXTN_S_003);
			}
			catch (DAOException e)
			{
				LOGGER.error(e.getMessage());
				throw new DynamicExtensionsSystemException(e.getMessage(), e, DYEXTN_S_003);
			}
			finally
			{
				closeDAO(hibernateDAO);
			}
		}
	}

	/**
	 * Fetch existing skip logic for control.
	 * @param identifier the identifier
	 * @return the collection< skip logic>
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	@SuppressWarnings("unchecked")
	public void deleteSkipLogic(Long containerIdentifier) throws DynamicExtensionsSystemException
	{
		Map<String, NamedQueryParam> substParams = new HashMap<String, NamedQueryParam>();
		substParams.put("0", new NamedQueryParam(DBTypes.LONG, containerIdentifier));
		Collection<SkipLogic> skipLogics = AbstractMetadataManager.executeHQL("getSkipLogicByContainerId", substParams);
		if(!skipLogics.isEmpty())
		{
			deleteSkipLogicObject(skipLogics.iterator().next());
		}
	}

	/**
	 * Close dao.
	 * @param hibernateDAO the hibernate dao
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	private void closeDAO(HibernateDAO hibernateDAO) throws DynamicExtensionsSystemException
	{
		try
		{
			hibernateDAO.closeSession();
		}
		catch (DAOException e)
		{
			LOGGER.error(e.getMessage());
			throw new DynamicExtensionsSystemException(e.getMessage(), e, DYEXTN_S_003);
		}
	}
}
