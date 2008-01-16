
package edu.common.dynamicextensions.entitymanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import edu.common.dynamicextensions.domain.Association;
import edu.common.dynamicextensions.domain.Category;
import edu.common.dynamicextensions.domain.CategoryAssociation;
import edu.common.dynamicextensions.domain.CategoryAttribute;
import edu.common.dynamicextensions.domain.PathAssociationRelationInterface;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAssociationInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.util.dbManager.DAOException;

/**
 * @author rajesh_patil
 * @author mandar_shidhore
 * @author kunal_kamble
 */
public class CategoryManager extends AbstractMetadataManager implements CategoryManagerInterface
{
	/**
	 * Static instance of the CategoryManager.
	 */
	private static CategoryManagerInterface categoryManager = null;

	/**
	 * Static instance of the queryBuilder.
	 */
	private static DynamicExtensionBaseQueryBuilder queryBuilder = null;

	/**
	 * Empty Constructor.
	 */
	protected CategoryManager()
	{

	}

	/**
	 * Returns the instance of the Entity Manager.
	 * @return entityManager singleton instance of the Entity Manager.
	 */
	public static synchronized CategoryManagerInterface getInstance()
	{
		if (categoryManager == null)
		{
			categoryManager = new CategoryManager();
			DynamicExtensionsUtility.initialiseApplicationVariables();
			queryBuilder = QueryBuilderFactory.getQueryBuilder();
		}
		return categoryManager;
	}

	/**
	 * LogFatalError.
	 */
	protected void LogFatalError(Exception e, AbstractMetadataInterface abstractMetadata)
	{
		// TODO Auto-generated method stub
	}

	/**
	 * Method to persist categroy metadata.
	 * @param categoryInterface interface for Category
	 * @throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	 */
	public CategoryInterface persistCategory(CategoryInterface categoryInterface) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		CategoryInterface category = (CategoryInterface) persistDynamicExtensionObject(categoryInterface);
		return category;
	}

	/**
	 * Method to persist categroy metadata.
	 * @param categoryInterface interface for Category
	 * @throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	 */
	public CategoryInterface persistCategoryMetadata(CategoryInterface categoryInterface) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		CategoryInterface category = (CategoryInterface) persistDynamicExtensionObjectMetdata(categoryInterface);
		return category;
	}

	/**
	 * Method to delete category metadata.
	 * @param categoryInterface interface for Category
	 * @throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	 */
	public CategoryInterface deleteCategory(CategoryInterface categoryInterface) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		logDebug("deleteCategory", "entering the method");
		Category category = (Category) categoryInterface;

		HibernateDAO hibernateDAO = (HibernateDAO) DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);
		Stack stack = new Stack();

		try
		{
			hibernateDAO.openSession(null);

			if (category.getId() != null)
				hibernateDAO.delete(category);

			hibernateDAO.commit();
		}
		catch (Exception e)
		{
			rollbackQueries(stack, category, e, hibernateDAO);

			if (e instanceof DynamicExtensionsApplicationException)
			{
				throw (DynamicExtensionsApplicationException) e;
			}
			else
			{
				throw new DynamicExtensionsSystemException(e.getMessage(), e);
			}
		}
		finally
		{
			try
			{
				hibernateDAO.closeSession();
			}
			catch (Exception e)
			{
				rollbackQueries(stack, category, e, hibernateDAO);
			}
		}
		logDebug("persistCategory", "exiting the method");
		return category;
	}

	/**
	 *
	 */
	protected void preProcess(DynamicExtensionBaseDomainObjectInterface dynamicExtensionBaseDomainObject, List reverseQueryList,
			HibernateDAO hibernateDAO, List queryList) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		CategoryInterface category = (CategoryInterface) dynamicExtensionBaseDomainObject;

		queryList.addAll(getDynamicQueryList(category, reverseQueryList, hibernateDAO, queryList));
	}

	/**
	 * getDynamicQueryList.
	 */
	public List getDynamicQueryList(CategoryInterface category, List reverseQueryList, HibernateDAO hibernateDAO, List queryList)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		return queryBuilder.getCreateCategoryQueryList((Category) category, reverseQueryList, hibernateDAO);
	}

	/**
	 * postProcess.
	 */
	protected void postProcess(List queryList, List reverseQueryList, Stack rollbackQueryStack) throws DynamicExtensionsSystemException
	{
		queryBuilder.executeQueries(queryList, reverseQueryList, rollbackQueryStack);
	}

	/**
	 *
	 */
	public Long insertData(CategoryInterface category, Map<AbstractMetadataInterface, ?> dataValue) throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException
	{
		List<Map<AbstractMetadataInterface, ?>> dataValueMapList = new ArrayList<Map<AbstractMetadataInterface, ?>>();
		dataValueMapList.add(dataValue);
		List<Long> recordIdList = insertData(category, dataValueMapList);
		return recordIdList.get(0);
	}

	/**
	 *
	 */
	public List<Long> insertData(CategoryInterface category, List<Map<AbstractMetadataInterface, ?>> dataValueMapList)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		List<Long> recordIdList = new ArrayList<Long>();
		HibernateDAO hibernateDAO = null;
		try
		{
			DAOFactory factory = DAOFactory.getInstance();
			hibernateDAO = (HibernateDAO) factory.getDAO(Constants.HIBERNATE_DAO);
			hibernateDAO.openSession(null);
			NewEntityManagerInterface newEntityManager = NewEntityManager.getInstance();
			for (Map<AbstractMetadataInterface, ?> dataValue : dataValueMapList)
			{
				EntityInterface entity = null;
				List<Map<AbstractAttributeInterface, ?>> dataValueMap = new ArrayList<Map<AbstractAttributeInterface, ?>>();
				recordIdList.addAll(newEntityManager.insertData(entity, dataValueMap));
			}

			hibernateDAO.commit();
		}
		catch (DynamicExtensionsApplicationException e)
		{
			throw (DynamicExtensionsApplicationException) handleRollback(e, "Error while inserting data", hibernateDAO, false);
		}
		catch (Exception e)
		{
			throw (DynamicExtensionsSystemException) handleRollback(e, "Error while inserting data", hibernateDAO, true);
		}
		finally
		{
			try
			{
				hibernateDAO.closeSession();
			}
			catch (DAOException e)
			{
				throw (DynamicExtensionsSystemException) handleRollback(e, "Error while closing", hibernateDAO, true);
			}
		}

		return recordIdList;
	}

	/**
	 *
	 */
	protected DynamicExtensionBaseQueryBuilder getQueryBuilderInstance()
	{
		return queryBuilder;
	}

	/**
	 * This method accepts a category data map and converts it to corresponding entity data map.
	 */
	public Map<AbstractAttributeInterface, Object> generateEntityDataValueMap(CategoryEntityInterface rootCategoryEntity,
			Map<AbstractAttributeInterface, Object> entityDataMap, Map<AbstractMetadataInterface, Object> categoryDataMap,
			List<Association> existingAssociationsList)
	{
		Set<AbstractMetadataInterface> dataSet = categoryDataMap.keySet();

		for (AbstractMetadataInterface object : dataSet)
		{
			if (object instanceof CategoryAssociation)
			{
				addDataForEntitiesOnPath(entityDataMap, categoryDataMap, (CategoryAssociation) object, existingAssociationsList);
			}
			else
			{
				entityDataMap.put(((CategoryAttribute) object).getAttribute(), categoryDataMap.get(object));
			}
		}
		return entityDataMap;
	}

	/**
	 * This method adds data for all entities present on the path between two category entities.
	 * @param entityDataMap
	 * @param categoryDataMap
	 * @param categoryAssociationInterface
	 * @param existingAssociationsList
	 */
	private void addDataForEntitiesOnPath(Map<AbstractAttributeInterface, Object> entityDataMap,
			Map<AbstractMetadataInterface, Object> categoryDataMap, CategoryAssociationInterface categoryAssociationInterface,
			List<Association> existingAssociationsList)
	{
		Map<AbstractAttributeInterface, Object> dynamicDataMap = entityDataMap;

		for (PathAssociationRelationInterface associationRelationInterface : categoryAssociationInterface.getCategoryEntity().getPath()
				.getSortedPathAssociationRelationCollection())
		{
			if (associationExists(existingAssociationsList, associationRelationInterface.getAssociation()))
			{
				continue;
			}
			if (associationRelationInterface.getAssociation().getTargetEntity() == categoryAssociationInterface.getCategoryEntity().getEntity())
			{
				List<Map<AbstractMetadataInterface, Object>> oldDataList = (List<Map<AbstractMetadataInterface, Object>>) categoryDataMap
						.get(categoryAssociationInterface);
				List<Map<AbstractAttributeInterface, Object>> newDataList = new ArrayList<Map<AbstractAttributeInterface, Object>>();

				// When an association is added, add the same to existing associations' list
				existingAssociationsList.add(associationRelationInterface.getAssociation());

				populateNewDataList(oldDataList, newDataList, entityDataMap, categoryDataMap, existingAssociationsList);
				dynamicDataMap.put(associationRelationInterface.getAssociation(), newDataList);
			}
			else
			{
				Map<AbstractAttributeInterface, Object> newDataMap = new HashMap<AbstractAttributeInterface, Object>();
				List<Map<AbstractAttributeInterface, Object>> newDataList = new ArrayList<Map<AbstractAttributeInterface, Object>>();
				newDataList.add(newDataMap);
				dynamicDataMap.put(associationRelationInterface.getAssociation(), newDataList);

				// When an association is added, add the same to existing associations' list
				existingAssociationsList.add(associationRelationInterface.getAssociation());

				dynamicDataMap = newDataMap;
			}
		}
	}

	/**
	 * Create a new data list, and populate it with the values from old data list.
	 * @param oldDataList
	 * @param newDataList
	 * @param entityDataMap
	 * @param categoryDataMap
	 * @param existingAssociationsList
	 */
	private void populateNewDataList(List<Map<AbstractMetadataInterface, Object>> oldDataList,
			List<Map<AbstractAttributeInterface, Object>> newDataList, Map<AbstractAttributeInterface, Object> entityDataMap,
			Map<AbstractMetadataInterface, Object> categoryDataMap, List<Association> existingAssociationsList)
	{
		Map<AbstractAttributeInterface, Object> newDataMap = null;
		for (Map<AbstractMetadataInterface, Object> oldDataMap : oldDataList)
		{
			newDataMap = new HashMap<AbstractAttributeInterface, Object>();
			Set<AbstractMetadataInterface> dataSet = oldDataMap.keySet();
			for (AbstractMetadataInterface object : dataSet)
			{
				if (object instanceof CategoryAssociation)
				{
					addDataForEntitiesOnPath(newDataMap, oldDataMap, (CategoryAssociationInterface) object, existingAssociationsList);
				}
				else
				{
					newDataMap.put(((CategoryAttributeInterface) object).getAttribute(), oldDataMap.get(object));
				}
			}
			newDataList.add(newDataMap);
		}
	}

	/**
	 * This method checks if an association has already been added to entity data map.
	 * @param existingAssociationsList
	 * @param association
	 * @return true if an association has already been added, false otherwise.
	 */
	private boolean associationExists(List<Association> existingAssociationsList, Association association)
	{
		for (Association currentAssociation : existingAssociationsList)
		{
			if (currentAssociation.getName().equals(association.getName()))
				return true;
		}
		return false;
	}

}