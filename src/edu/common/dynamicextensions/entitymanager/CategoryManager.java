
package edu.common.dynamicextensions.entitymanager;

import static edu.wustl.common.util.global.Constants.ACTIVITY_STATUS_ACTIVE;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;

import edu.common.dynamicextensions.domain.Category;
import edu.common.dynamicextensions.domain.CategoryAttribute;
import edu.common.dynamicextensions.domain.PathAssociationRelationInterface;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAssociationInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PathInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;

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
	public Long insertData(CategoryInterface category, Map<BaseAbstractAttributeInterface, Object> dataValue)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		List<Map<BaseAbstractAttributeInterface, Object>> dataValueMapList = new ArrayList<Map<BaseAbstractAttributeInterface, Object>>();
		dataValueMapList.add(dataValue);
		List<Long> recordIdList = insertData(category, dataValueMapList);
		return recordIdList.get(0);
	}

	/**
	 *
	 */
	public List<Long> insertData(CategoryInterface category, List<Map<BaseAbstractAttributeInterface, Object>> categoryDataValueMapList)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		List<Map<AbstractAttributeInterface, Object>> EntityDataValueMapList = generateEntityDataValueMapList(categoryDataValueMapList);
		CategoryEntityInterface rootCategoryEntity = category.getRootCategoryElement();
		EntityInterface entity = rootCategoryEntity.getEntity();
		List<Long> recordIdList = new ArrayList<Long>();
		HibernateDAO hibernateDAO = null;
		try
		{
			
			EntityManagerInterface entityManager = EntityManager.getInstance();
			recordIdList = entityManager.insertData(entity, EntityDataValueMapList);

			DAOFactory factory = DAOFactory.getInstance();
			hibernateDAO = (HibernateDAO) factory.getDAO(Constants.HIBERNATE_DAO);
			hibernateDAO.openSession(null);

			EntityManagerUtil entityManagerUtil = new EntityManagerUtil();
			StringBuffer query = new StringBuffer();
			query.append(INSERT_INTO_KEYWORD + WHITESPACE + rootCategoryEntity.getTableProperties().getName() + WHITESPACE + OPENING_BRACKET
					+ "ACTIVITY_STATUS" + COMMA + IDENTIFIER + COMMA + CATEGORY_ROOT_ID + CLOSING_BRACKET + WHITESPACE + VALUES_KEYWORD
					+ OPENING_BRACKET + "'" + ACTIVITY_STATUS_ACTIVE + "'" + COMMA
					+ entityManagerUtil.getNextIdentifier(rootCategoryEntity.getTableProperties().getName()) + COMMA + recordIdList.get(0)
					+ CLOSING_BRACKET);
			Connection conn = DBUtil.getConnection();
			Statement statement =conn.createStatement();
			statement.executeUpdate(query.toString());
			conn.commit();

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
	 * @param categoryDataValueMapList
	 * @return
	 */
	private List<Map<AbstractAttributeInterface, Object>> generateEntityDataValueMapList(
			List<Map<BaseAbstractAttributeInterface, Object>> categoryDataValueMapList)
	{
		List<Map<AbstractAttributeInterface, Object>> entityDataValueMapList = new ArrayList<Map<AbstractAttributeInterface, Object>>();

		for (Map<BaseAbstractAttributeInterface, Object> categoryDataValueMap : categoryDataValueMapList)
		{
			entityDataValueMapList.add(generateEntityDataValueMap(categoryDataValueMap));
		}
		return entityDataValueMapList;
	}

	/**
	 *
	 */
	protected DynamicExtensionBaseQueryBuilder getQueryBuilderInstance()
	{
		return queryBuilder;
	}

	/**
	 * 
	 * @param rootCategoryEntity
	 * @param categoryDataMap
	 * @param existingAssociationsList
	 * @return
	 */
	public Map<AbstractAttributeInterface, Object> generateEntityDataValueMap(Map<BaseAbstractAttributeInterface, Object> categoryDataMap)

	{
		Map<AbstractAttributeInterface, Object> entityDataValueMap = new HashMap<AbstractAttributeInterface, Object>();
		Set<Entry<BaseAbstractAttributeInterface, Object>> categoryDataMapEntries = categoryDataMap.entrySet();

		AbstractAttributeInterface abstractAttributeInterface = null;
		Object entityValue = null;
		CategoryAttributeInterface categoryAttributeInterface;
		CategoryAssociationInterface categoryAssociationInterface;
		BaseAbstractAttributeInterface baseAbstractAttributeInterface;
		Object categoryValue;
		for (Entry<BaseAbstractAttributeInterface, Object> entry : categoryDataMapEntries)
		{
			baseAbstractAttributeInterface = entry.getKey();
			categoryValue = entry.getValue();
			if (baseAbstractAttributeInterface instanceof CategoryAttribute)
			{
				categoryAttributeInterface = (CategoryAttributeInterface) baseAbstractAttributeInterface;
				abstractAttributeInterface = categoryAttributeInterface.getAttribute();
				entityValue = categoryValue;
				entityDataValueMap.put(abstractAttributeInterface, entityValue);
			}
			else
			{
				categoryAssociationInterface = (CategoryAssociationInterface) baseAbstractAttributeInterface;
				populateMapValuesForCategoryAssociation(entityDataValueMap, categoryAssociationInterface, categoryValue);
			}

		}
		return entityDataValueMap;
	}

	/**
	 * 
	 * @param abstractAttributeInterface
	 * @param entityValue
	 * @param baseAbstractAttributeInterface
	 * @param categoryValue
	 */
	private void populateMapValuesForCategoryAssociation(Map<AbstractAttributeInterface, Object> entityDataValueMap,
			CategoryAssociationInterface categoryAssociationInterface, Object categoryValue)
	{
		CategoryEntityInterface catEntityInterface = categoryAssociationInterface.getTargetCategoryEntity();
		PathInterface pathInterface = catEntityInterface.getPath();
		List<PathAssociationRelationInterface> pathAssociationRelationList = pathInterface.getSortedPathAssociationRelationCollection();
		List<AssociationInterface> sortedAssociations = getSortedAssociations(pathAssociationRelationList);
		AbstractAttributeInterface abstractAttributeInterface = sortedAssociations.get(0);

		List<Map<AbstractAttributeInterface, Object>> entityDataValueMapList = new ArrayList<Map<AbstractAttributeInterface, Object>>();
		List<Map<AbstractAttributeInterface, Object>> targetEntityDataValueMapList = createNestedMap(sortedAssociations, entityDataValueMapList);
		List<Map<BaseAbstractAttributeInterface, Object>> categoryDataMapList = (List<Map<BaseAbstractAttributeInterface, Object>>) categoryValue;
		for (Map<BaseAbstractAttributeInterface, Object> categoryDataMap : categoryDataMapList)
		{
			targetEntityDataValueMapList.add(generateEntityDataValueMap(categoryDataMap));

		}
		Object entityValue = entityDataValueMapList;
		entityDataValueMap.put(abstractAttributeInterface, entityValue);
	}

	/**
	 * 
	 * @param sortedAssociations
	 * @param entityDataValueMapList 
	 * @return
	 */
	private List<Map<AbstractAttributeInterface, Object>> createNestedMap(List<AssociationInterface> sortedAssociations,
			List<Map<AbstractAttributeInterface, Object>> entityDataValueMapList)
	{
		AssociationInterface associationInterface;

		List<Map<AbstractAttributeInterface, Object>> newEntityDataMapList;

		Map<AbstractAttributeInterface, Object> entityDataMap;
		for (int counter = 1; counter < sortedAssociations.size(); counter++)
		{
			associationInterface = sortedAssociations.get(counter);
			entityDataMap = new HashMap<AbstractAttributeInterface, Object>();
			newEntityDataMapList = new ArrayList<Map<AbstractAttributeInterface, Object>>();
			entityDataMap.put(associationInterface, newEntityDataMapList);
			entityDataValueMapList.add(entityDataMap);
			entityDataValueMapList = newEntityDataMapList;
		}
		return entityDataValueMapList;
	}

	/**
	 * 
	 * @param pathAssociationRelationList
	 * @return
	 */
	private List<AssociationInterface> getSortedAssociations(List<PathAssociationRelationInterface> pathAssociationRelationList)
	{
		List<AssociationInterface> associationList = new ArrayList<AssociationInterface>();
		for (PathAssociationRelationInterface pathAssociationRelationInterface : pathAssociationRelationList)
		{
			associationList.add(pathAssociationRelationInterface.getAssociation());
		}
		return associationList;
	}

	/**
	 * @param rootCategoryEntity
	 * @param categoryDataValueMap
	 * @param entityDataMap
	 * @return
	 */
	private Map<BaseAbstractAttributeInterface, Object> addCategoryAttributes(CategoryEntityInterface rootCategoryEntity,
			Map<BaseAbstractAttributeInterface, Object> categoryDataValueMap, Map<AbstractAttributeInterface, Object> entityDataMap)
	{
		for (CategoryAttributeInterface categoryAttribute : rootCategoryEntity.getAllCategoryAttributes())
		{
			categoryDataValueMap.put(categoryAttribute, entityDataMap.get(categoryAttribute.getAttribute()));
		}

		return categoryDataValueMap;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.entitymanager.CategoryManagerInterface#populateCategoryDataValueMap(edu.common.dynamicextensions.domaininterface.CategoryEntityInterface, java.util.Map, java.util.Map)
	 */
	public void populateCategoryDataValueMap(CategoryEntityInterface categoryEntityInterface,
			Map<BaseAbstractAttributeInterface, Object> categoryDataMap, Map<AbstractAttributeInterface, Object> entityDataMap)
	{
		addCategoryAttributes(categoryEntityInterface, categoryDataMap, entityDataMap);
		Collection<CategoryAssociationInterface> collection = categoryEntityInterface.getCategoryAssociationCollection();
		if (categoryEntityInterface.getParentCategoryEntity() != null)
		{
			collection.addAll(categoryEntityInterface.getParentCategoryEntity().getCategoryAssociationCollection());
		}

		for (CategoryAssociationInterface associationInterface : collection)
		{
			List<Map<BaseAbstractAttributeInterface, Object>> categoryDataValueMapList = new ArrayList<Map<BaseAbstractAttributeInterface, Object>>();
			categoryDataMap.put(associationInterface, categoryDataValueMapList);

			EntityInterface searchEntity = associationInterface.getTargetCategoryEntity().getEntity();
			List<PathAssociationRelationInterface> SortedPathAssociationRelationCollection = associationInterface.getTargetCategoryEntity().getPath()
					.getSortedPathAssociationRelationCollection();

			List<Map<AbstractAttributeInterface, Object>> dataValueMapList = getList(searchEntity, entityDataMap,
					getSortedAssociations(SortedPathAssociationRelationCollection));
			if (dataValueMapList.size() > 0)
			{
				for (Map<AbstractAttributeInterface, Object> map : dataValueMapList)
				{
					Map<BaseAbstractAttributeInterface, Object> map2 = new HashMap<BaseAbstractAttributeInterface, Object>();
					categoryDataValueMapList.add(map2);
					populateCategoryDataValueMap(associationInterface.getTargetCategoryEntity(), map2, map);
				}
			}

		}

	}

	/**
	 * This method traverse the entity data value map.
	 * Finds the association whose target entity matches with the searchEntity.
	 * @param searchEntity
	 * @param entityMap
	 * @return the list associated with the searched association
	 */
	private List<Map<AbstractAttributeInterface, Object>> getList(EntityInterface searchEntity, Map<AbstractAttributeInterface, Object> entityMap,
			List<AssociationInterface> sortedAssociationList)
	{
		Set<AbstractAttributeInterface> set = entityMap.keySet();
		List<Map<AbstractAttributeInterface, Object>> resultList = null;

		for (AbstractAttributeInterface key : set)
		{
			if (key instanceof AssociationInterface)
			{
				AssociationInterface associationInterface = (AssociationInterface) key;
				if (searchEntity == associationInterface.getTargetEntity())
				{
					resultList = (List<Map<AbstractAttributeInterface, Object>>) entityMap.get(key);
				}
				else if (sortedAssociationList.contains(key))
				{
					resultList = getList(searchEntity, ((List<Map<AbstractAttributeInterface, Object>>) entityMap.get(key)).get(0),
							sortedAssociationList);
				}

			}
		}
		return resultList;
	}

	/**
	 * This method returns the categoryDataValueMap for the given rootCategoryEntity 
	 * and recordId
	 * @param rootCategoryEntity
	 * @param recordId
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws SQLException
	 */
	public Map<BaseAbstractAttributeInterface, Object> getRecordById(CategoryEntityInterface rootCategoryEntity, Long recordId)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException, SQLException
	{
		Map<AbstractAttributeInterface, Object> entityDataValueMap = EntityManager.getInstance().getRecordById(rootCategoryEntity.getEntity(),
				getRootEntityRecordId(rootCategoryEntity, recordId));
		Map<BaseAbstractAttributeInterface, Object> categoryDataValueMap = new HashMap<BaseAbstractAttributeInterface, Object>();
		populateCategoryDataValueMap(rootCategoryEntity, categoryDataValueMap, entityDataValueMap);
		return categoryDataValueMap;
	}

	/**
	 * @param rootCategoryEntityInterface
	 * @param recordId
	 * @return the record id of the hook entity
	 * @throws SQLException
	 * @throws DynamicExtensionsSystemException
	 */
	private Long getRootEntityRecordId(CategoryEntityInterface rootCategoryEntityInterface, Long recordId) throws SQLException,
			DynamicExtensionsSystemException
	{
		StringBuffer query = new StringBuffer();
		query.append(SELECT_KEYWORD + WHITESPACE + CATEGORY_ROOT_ID + WHITESPACE + FROM_KEYWORD + WHITESPACE
				+ rootCategoryEntityInterface.getTableProperties().getName() + WHITESPACE + WHERE_KEYWORD + 
				WHITESPACE + IDENTIFIER + EQUAL+ recordId);
		Long rootEntityRecordId = null;
		
		EntityManagerUtil entityManagerUtil = new EntityManagerUtil();
		List resultList = entityManagerUtil.getResultInList(query.toString());
		if(resultList.size() > 0){
			rootEntityRecordId = (Long) resultList.get(0); 
		}
		return rootEntityRecordId;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.entitymanager.CategoryManagerInterface#editData(edu.common.dynamicextensions.domaininterface.CategoryEntityInterface, java.util.Map, java.lang.Long)
	 */
	public boolean editData(CategoryEntityInterface categoryEntity, Map<BaseAbstractAttributeInterface, Object> attributeValueMap, Long recordId)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException, SQLException
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		Map<AbstractAttributeInterface, Object> entityValueMap = generateEntityDataValueMap(attributeValueMap);
		Long entityRecordId = getRootEntityRecordId(categoryEntity, recordId);

		Boolean isEdited = entityManager.editData(categoryEntity.getEntity(), entityValueMap, entityRecordId);
		return isEdited ;
	}

}