
package edu.common.dynamicextensions.entitymanager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.Map.Entry;

import org.hibernate.HibernateException;

import edu.common.dynamicextensions.domain.BaseAbstractAttribute;
import edu.common.dynamicextensions.domain.CategoryAttribute;
import edu.common.dynamicextensions.domain.CategoryEntity;
import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.PathAssociationRelationInterface;
import edu.common.dynamicextensions.domain.userinterface.Container;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AbstractMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAssociationInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.DynamicExtensionBaseDomainObjectInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.PathInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
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
	 * Instance of entity manager util class
	 */
	EntityManagerUtil entityManagerUtil = new EntityManagerUtil();

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
	 * Method to persist category metadata.
	 * @param categoryInterface interface for Category
	 * @throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	 */
	public CategoryInterface persistCategory(CategoryInterface categoryInterface) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		CategoryInterface category = (CategoryInterface) persistDynamicExtensionObject(categoryInterface);

		// Update the dynamic extension cache for all containers within entitygroup
		CategoryEntityInterface catEntityInterface = category.getRootCategoryElement();
		EntityGroupInterface entityGroupInterface = catEntityInterface.getEntity().getEntityGroup();
		DynamicExtensionsUtility.updateDynamicExtensionsCache(entityGroupInterface.getId());
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

		// Update the dynamic extension cache for all containers within entity group
		CategoryEntityInterface catEntityInterface = category.getRootCategoryElement();
		EntityGroupInterface entityGroupInterface = catEntityInterface.getEntity().getEntityGroup();
		DynamicExtensionsUtility.updateDynamicExtensionsCache(entityGroupInterface.getId());
		return category;
	}

	/**
	 *
	 */
	protected void preProcess(DynamicExtensionBaseDomainObjectInterface dynamicExtensionBaseDomainObject, List reverseQueryList,
			HibernateDAO hibernateDAO, List queryList) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		CategoryInterface category = (CategoryInterface) dynamicExtensionBaseDomainObject;

		getDynamicQueryList(category, reverseQueryList, hibernateDAO, queryList);
	}

	/**
	 * getDynamicQueryList.
	 */
	protected List getDynamicQueryList(CategoryInterface category, List<String> reverseQueryList,
			HibernateDAO hibernateDAO, List<String> queryList) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		List<CategoryEntityInterface> categoryEntityList = new ArrayList<CategoryEntityInterface>();
        DynamicExtensionsUtility.getUnsavedCategoryEntityList(category.getRootCategoryElement(),
				categoryEntityList);
		for (CategoryEntityInterface categoryEntityInterface : categoryEntityList)
		{
			List<String> createQueryList = queryBuilder.getCreateCategoryQueryList(categoryEntityInterface,
					reverseQueryList, hibernateDAO);
			if (createQueryList != null && !createQueryList.isEmpty())
			{
				queryList.addAll(createQueryList);
			}
		}
		for (CategoryEntityInterface categoryEntityInterface : categoryEntityList)
		{
			List<String> createQueryList = queryBuilder.getUpdateCategoryQueryList(categoryEntityInterface,
					reverseQueryList, hibernateDAO);
			if (createQueryList != null && !createQueryList.isEmpty())
			{
				queryList.addAll(createQueryList);
			}
		}
		
		List<CategoryEntityInterface> savedCategoryEntityList = new ArrayList<CategoryEntityInterface>();
        DynamicExtensionsUtility.getSavedCategoryEntityList(category.getRootCategoryElement(),
        		savedCategoryEntityList);
		for (CategoryEntityInterface savedCategoryEntity : savedCategoryEntityList)
		{
			CategoryEntity databaseCopy = (CategoryEntity) DBUtil.loadCleanObj(
					CategoryEntity.class, savedCategoryEntity.getId());

			List<String> updateQueryList = queryBuilder.getUpdateEntityQueryList(
					(CategoryEntity) savedCategoryEntity, databaseCopy,
					reverseQueryList);

			if (updateQueryList != null && !updateQueryList.isEmpty())
			{
				queryList.addAll(updateQueryList);
			}
		}
		return queryList;
	}

	/**
	 * postProcess.
	 */
	protected void postProcess(List queryList, List reverseQueryList, Stack rollbackQueryStack) throws DynamicExtensionsSystemException
	{
		queryBuilder.executeQueries(queryList, reverseQueryList, rollbackQueryStack);
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.entitymanager.CategoryManagerInterface#insertData(edu.common.dynamicextensions.domaininterface.CategoryInterface, java.util.Map)
	 */
	public Long insertData(CategoryInterface category, Map<BaseAbstractAttributeInterface, Object> dataValue)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		List<Map<BaseAbstractAttributeInterface, Object>> dataValueMapList = new ArrayList<Map<BaseAbstractAttributeInterface, Object>>();
		dataValueMapList.add(dataValue);
		List<Long> recordIdList = insertData(category, dataValueMapList);
		return recordIdList.get(0);
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.entitymanager.CategoryManagerInterface#insertData(edu.common.dynamicextensions.domaininterface.CategoryInterface, java.util.List)
	 */
	public List<Long> insertData(CategoryInterface category, List<Map<BaseAbstractAttributeInterface, Object>> categoryDataValueMapList)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		List<Long> recordIdList = new ArrayList<Long>();
		HibernateDAO hibernateDAO = null;
		try
		{
			DAOFactory factory = DAOFactory.getInstance();
			hibernateDAO = (HibernateDAO) factory.getDAO(Constants.HIBERNATE_DAO);
			hibernateDAO.openSession(null);

			for (Map<BaseAbstractAttributeInterface, ?> categoryDataValue : categoryDataValueMapList)
			{
				Long recordId = insertDataForHeirarchy(category.getRootCategoryElement(), categoryDataValue, hibernateDAO);
				recordIdList.add(recordId);
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
	 * @param entity
	 * @param dataValue
	 * @param hibernateDAO
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws HibernateException
	 * @throws SQLException
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 */
	private Long insertDataForHeirarchy(CategoryEntityInterface categoryEntity, Map<BaseAbstractAttributeInterface, ?> dataValue, HibernateDAO hibernateDAO)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException, HibernateException, SQLException, DAOException,
			UserNotAuthorizedException
	{
		List<CategoryEntityInterface> categoryEntityList = getParentEntityList(categoryEntity);
		Map<CategoryEntityInterface, Map> entityValueMap = initialiseEntityValueMap(dataValue);
		Long parentRecordId = null;
		Map testdatamap = new HashMap();
		for (CategoryEntityInterface categoryEntityInterface : categoryEntityList)
		{
			Map valueMap = entityValueMap.get(categoryEntityInterface);
			parentRecordId = insertDataForSingleCategoryEntity(categoryEntityInterface, valueMap, hibernateDAO, parentRecordId,testdatamap);
		}
		return parentRecordId;
	}
	
	/**
	 * @param categoryEntity
	 * @param dataValue
	 * @param hibernateDAO
	 * @param parentRecordId
	 * @param dataMap
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws HibernateException
	 * @throws SQLException
	 * @throws DAOException
	 * @throws UserNotAuthorizedException
	 */
	private Long insertDataForSingleCategoryEntity(CategoryEntityInterface categoryEntity, Map dataValue, HibernateDAO hibernateDAO, Long parentRecordId,Map dataMap)
	throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException, HibernateException, SQLException, DAOException,
	UserNotAuthorizedException
	{
		Long identifier = null;
		Map<String, Long> keyMap = new HashMap<String, Long>();
		Map<String, Long> originalKeyMap = new HashMap<String, Long>();
		
//		Connection conn = DBUtil.getConnection();
		
		if (categoryEntity == null)
		{
			throw new DynamicExtensionsSystemException("Input to insert data is null");
		}
		
		CategoryEntityInterface rootCategoryEntity = categoryEntity.getCategory().getRootCategoryElement();
		String rootCategoryEntityName = rootCategoryEntity.getName();
		
		if (rootCategoryEntity.getCategoryAttributeCollection().size() == 0)
		{
			//If root category element does not have attribute ,then explicitly insert id column for it and put in key map.
			Long entityIdentifier = entityManagerUtil.getNextIdentifier(rootCategoryEntity.getEntity().getTableProperties().getName());
			String entityInsertQuery = "INSERT INTO " + rootCategoryEntity.getEntity().getTableProperties().getName() + " (IDENTIFIER, ACTIVITY_STATUS) VALUES ( "+entityIdentifier+", 'ACTIVE')";
			
			executeUpdateQuery(entityInsertQuery);
			logDebug("insertData", "Query for insert data is : " + entityInsertQuery.toString());
//			PreparedStatement statement = conn.prepareStatement(entityInsertQuery.toString());
//			statement.executeUpdate();
			
			keyMap.put(rootCategoryEntityName, entityIdentifier);
			originalKeyMap.put(rootCategoryEntityName, entityIdentifier);
			
			Long categoryIdentifier = entityManagerUtil.getNextIdentifier(rootCategoryEntity.getTableProperties().getName());
			String categoryEntityInsertQuery ="INSERT INTO " + rootCategoryEntity.getTableProperties().getName() + " (IDENTIFIER, ACTIVITY_STATUS, RECORD_ID) VALUES ( "+categoryIdentifier+", 'ACTIVE', "+entityIdentifier+" )";

			executeUpdateQuery(categoryEntityInsertQuery);
			logDebug("insertData", "Query for insert data is : " + categoryEntityInsertQuery);
//			statement = conn.prepareStatement(categoryTableQuery);
//			statement.executeUpdate();
		}
		
		boolean isMultipleRecords = false;
		boolean isNoCategoryAttributePresent = false;

		generateKeyMap(null, null, null, null, categoryEntity, dataValue, keyMap, originalKeyMap, isMultipleRecords, isNoCategoryAttributePresent, hibernateDAO);

		if (parentRecordId != null)
		{
			identifier = parentRecordId;
		}
		else
		{
			identifier = (Long) keyMap.get(rootCategoryEntity.getName());
		}
		
		return identifier;
	}
	
	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.entitymanager.CategoryManagerInterface#editData(edu.common.dynamicextensions.domaininterface.CategoryEntityInterface, java.util.Map, java.lang.Long)
	 */
	public boolean editData(CategoryEntityInterface rootcategoryEntity, Map<BaseAbstractAttributeInterface, Object> attributeValueMap, Long recordId)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException, SQLException
	{
		Long entityRecordId = getRootEntityRecordId(rootcategoryEntity, recordId);
		
		List<Long> entityRecordIdList = new ArrayList<Long>();
		entityRecordIdList.add(entityRecordId);
		
		Map<AbstractAttributeInterface, Object> rootEntityRecordsMap = new HashMap<AbstractAttributeInterface, Object>();
		populateRootEntityRecordMap(rootcategoryEntity, rootEntityRecordsMap, attributeValueMap);
		
		HibernateDAO hibernateDAO = null;
		
		Boolean isEdited = false;
		
		try
		{
			DAOFactory factory = DAOFactory.getInstance();
			hibernateDAO = (HibernateDAO) factory.getDAO(Constants.HIBERNATE_DAO);
			hibernateDAO.openSession(null);
			
			EntityManagerInterface entityManager = EntityManager.getInstance();
			isEdited = entityManager.editDataForSingleEntity(rootcategoryEntity.getEntity(), rootEntityRecordsMap, entityRecordId, hibernateDAO);
			
			clearCategoryEntityData(rootcategoryEntity, recordId);
			
			if (isEdited)
			{
				Map<String, Long> keyMap = new HashMap<String, Long>();
				Map<String, Long> originalKeyMap = new HashMap<String, Long>();
				keyMap.put(rootcategoryEntity.getName(), entityRecordId);
				originalKeyMap.put(rootcategoryEntity.getName(), entityRecordId);
								
				for (CategoryAttributeInterface categoryAttribute: rootcategoryEntity.getCategoryAttributeCollection())
				{
					attributeValueMap.remove(categoryAttribute);
				}

				boolean isMultipleRecords = false;
				boolean isNoCategoryAttributePresent = false;

				generateKeyMap(null, null, null, null, rootcategoryEntity, attributeValueMap, keyMap, originalKeyMap, isMultipleRecords, isNoCategoryAttributePresent, hibernateDAO);

				hibernateDAO.commit();
			}
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
				
		return isEdited;
	}
	
	/**
	 * @param entityForeignKeyColumnName
	 * @param categoryEntityForeignKeyColumnName
	 * @param sourceCategoryEntityIdentifier
	 * @param sourceEntityIdentifier
	 * @param categoryEntity
	 * @param dataValue
	 * @param keyMap
	 * @param originalKeyMap
	 * @param isMultipleRecords
	 * @param isNoCategoryAttributePresent
	 * @param conn
	 * @param hibernateDAO
	 * @throws DynamicExtensionsSystemException
	 * @throws SQLException
	 * @throws HibernateException
	 * @throws DynamicExtensionsApplicationException
	 * @throws UserNotAuthorizedException
	 * @throws DAOException
	 */
	private void generateKeyMap(String entityForeignKeyColumnName, String categoryEntityForeignKeyColumnName, Long sourceCategoryEntityIdentifier, Long sourceEntityIdentifier, CategoryEntityInterface categoryEntity, Map dataValue, Map<String, Long> keyMap, Map<String, Long> originalKeyMap, boolean isMultipleRecords, boolean isNoCategoryAttributePresent, HibernateDAO hibernateDAO) throws DynamicExtensionsSystemException, SQLException, HibernateException, DynamicExtensionsApplicationException, UserNotAuthorizedException, DAOException
    {  	
    	Object value = null;		
		boolean isCategoryEntityRecordInserted = false;
		
		Set uiColumnSet = dataValue.keySet();
		Iterator uiColumnSetIter = uiColumnSet.iterator();
		
		while (uiColumnSetIter.hasNext())
		{
			BaseAbstractAttribute attribute = (BaseAbstractAttribute) uiColumnSetIter.next();
			value = dataValue.get(attribute);

			if (value == null)
			{
				continue;
			}
			
			if (attribute instanceof CategoryAttributeInterface && !isCategoryEntityRecordInserted)
			{
				String categoryEntityTableName = ((CategoryAttribute)attribute).getCategoryEntity().getTableProperties().getName();
				
				Long entityIdentifier = null;
				EntityManagerInterface entityManager = EntityManager.getInstance();
				
				if (keyMap.get(((CategoryAttribute)attribute).getCategoryEntity().getName())!= null && !isMultipleRecords)
				{
					if (isNoCategoryAttributePresent)
					{
						dataValue = null;
					}
					entityIdentifier =(Long) keyMap.get(((CategoryAttribute)attribute).getCategoryEntity().getName());
					entityManager.editDataForSingleEntity(((CategoryAttribute)attribute).getCategoryEntity().getEntity(), dataValue, entityIdentifier,hibernateDAO);
				}
				else
				{		
					if (isNoCategoryAttributePresent)
					{
						dataValue = null;
					}
					entityIdentifier = entityManager.insertDataForSingleEntity(((CategoryAttribute)attribute).getCategoryEntity().getEntity(), dataValue, hibernateDAO, null);
				}
				
				Long categoryEntityidentifier = entityManagerUtil.getNextIdentifier(categoryEntity.getTableProperties().getName());
				String insertQueryForCategoryEntity = "INSERT INTO "+categoryEntityTableName+" (IDENTIFIER, ACTIVITY_STATUS, RECORD_ID) VALUES ("+categoryEntityidentifier+", 'ACTIVE', "+entityIdentifier+")";

				executeUpdateQuery(insertQueryForCategoryEntity);
				
				if (categoryEntityForeignKeyColumnName != null && entityForeignKeyColumnName != null)
				{
					String updateCategoryEntityQuery = "UPDATE "+categoryEntityTableName +" SET "+ categoryEntityForeignKeyColumnName+ " = "+sourceCategoryEntityIdentifier+ " WHERE IDENTIFIER = "+ categoryEntityidentifier;
					executeUpdateQuery(updateCategoryEntityQuery);

					String updateEntityQuery = "UPDATE "+ ((CategoryAttribute)attribute).getCategoryEntity().getEntity().getTableProperties().getName()+" SET "+ entityForeignKeyColumnName + " = "+sourceEntityIdentifier + " WHERE IDENTIFIER = "+ entityIdentifier;
					executeUpdateQuery(updateEntityQuery);
				}
				
				keyMap.put(((CategoryAttribute)attribute).getCategoryEntity().getName(), entityIdentifier);
				originalKeyMap.put(((CategoryAttribute)attribute).getCategoryEntity().getName(), entityIdentifier);
				
				isCategoryEntityRecordInserted = true;
			}
			else if (attribute instanceof CategoryAssociationInterface)
			{
				CategoryAssociationInterface categoryAssociation = (CategoryAssociationInterface) attribute;

				PathInterface path = categoryAssociation.getTargetCategoryEntity().getPath();
				Collection<PathAssociationRelationInterface> pathAssociationRelation = path.getSortedPathAssociationRelationCollection();
				
				Long sourceEntityId = (Long) originalKeyMap.get(categoryAssociation.getCategoryEntity().getName());
				String foreignKeyColumnName = new String();
								
				String selectQuery = "SELECT IDENTIFIER FROM "+categoryAssociation.getCategoryEntity().getTableProperties().getName()+ " WHERE RECORD_ID = "+sourceEntityId;
				
				List<Long> idList = getResultIDList(selectQuery, "IDENTIFIER");
			
				Long resultId =  null;
				if (idList != null && idList.size() > 0)
				{
					resultId = idList.get(0);
				}
			
				Long sourceCategoryEntityId = resultId;
				
				String categoryForeignKeyColName = categoryAssociation.getConstraintProperties().getTargetEntityKey();

				EntityInterface entity = categoryAssociation.getTargetCategoryEntity().getEntity();
				
				for (PathAssociationRelationInterface par: pathAssociationRelation)
				{
					AssociationInterface association = par.getAssociation();
					
					foreignKeyColumnName = association.getConstraintProperties().getTargetEntityKey();

					if (association.getTargetEntity().getId() != entity.getId())
					{						
						if (originalKeyMap.get(association.getTargetEntity().getName()+"["+par.getTargetInstanceId()+"]") == null)
						{
							Long entityIdentifier = entityManagerUtil.getNextIdentifier(association.getTargetEntity().getTableProperties().getName());
							String insertQuery = "INSERT INTO "+ association.getTargetEntity().getTableProperties().getName()+ "(IDENTIFIER, ACTIVITY_STATUS, "+association.getConstraintProperties().getTargetEntityKey()+") VALUES ("+entityIdentifier+", 'ACTIVE',"+sourceEntityId+")";
							executeUpdateQuery(insertQuery);
							
							sourceEntityId = entityIdentifier;
							
							originalKeyMap.put(association.getTargetEntity().getName()+"["+par.getTargetInstanceId()+"]", sourceEntityId);
							keyMap.put(association.getTargetEntity().getName()+"["+par.getTargetInstanceId()+"]", sourceEntityId);
						}
						else
						{
							sourceEntityId =(Long) originalKeyMap.get(association.getTargetEntity().getName()+"["+par.getTargetInstanceId()+"]");
						}
					}
					else
					{
						sourceEntityId =(Long) originalKeyMap.get(association.getEntity().getName()+"["+par.getSourceInstanceId()+"]");
					}
				}				

				List<Map> listOfMapsForContainedEntity = (List) value;
								
				Map<CategoryAttributeInterface, Object> categoryAttributeMap = new HashMap<CategoryAttributeInterface, Object>();
				Map<CategoryAssociationInterface, Object> categoryAssociationMap = new HashMap<CategoryAssociationInterface, Object>();
				
				for (Map valueMapForContainedEntity : listOfMapsForContainedEntity)
				{
					Set keySet = valueMapForContainedEntity.keySet();
					Iterator iter = keySet.iterator();
					
					while (iter.hasNext())
					{
						Object obj = iter.next();
						if (obj instanceof CategoryAttributeInterface)
						{
							categoryAttributeMap.put((CategoryAttributeInterface) obj, valueMapForContainedEntity.get(obj));
						}
						else
						{
							categoryAssociationMap.put((CategoryAssociationInterface) obj, valueMapForContainedEntity.get(obj));
						}
					}
					
					if (listOfMapsForContainedEntity.size() > 1)
					{
						isMultipleRecords = true;
					}
					else
					{
						isMultipleRecords = false;
					}
					
					// Insert data for category attributes.
					if (categoryAttributeMap.size() == 0)
					{
						isNoCategoryAttributePresent = true;
						CategoryAttributeInterface dummyCategoryAttribute = DomainObjectFactory.getInstance().createCategoryAttribute();
						dummyCategoryAttribute.setCategoryEntity(categoryAssociation.getTargetCategoryEntity());
						categoryAttributeMap.put(dummyCategoryAttribute, "");
						
						generateKeyMap(foreignKeyColumnName, categoryForeignKeyColName, sourceCategoryEntityId, sourceEntityId, categoryAssociation.getTargetCategoryEntity(), categoryAttributeMap, keyMap, originalKeyMap, isMultipleRecords, isNoCategoryAttributePresent, hibernateDAO);
						
						isNoCategoryAttributePresent = false;
					}
					else
					{
						generateKeyMap(foreignKeyColumnName, categoryForeignKeyColName, sourceCategoryEntityId, sourceEntityId, categoryAssociation.getTargetCategoryEntity(), categoryAttributeMap, keyMap, originalKeyMap, isMultipleRecords, isNoCategoryAttributePresent, hibernateDAO);
					}
					categoryAttributeMap.clear();

					// Insert data for category associations.
					generateKeyMap(foreignKeyColumnName, categoryForeignKeyColName, sourceCategoryEntityId, sourceEntityId, categoryAssociation.getTargetCategoryEntity(), categoryAssociationMap, keyMap, originalKeyMap, isMultipleRecords, isNoCategoryAttributePresent, hibernateDAO);
					categoryAssociationMap.clear();
					
					originalKeyMap.putAll(keyMap);
					keyMap.remove(categoryAssociation.getTargetCategoryEntity().getName());
				}
			}
		}
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
		Map<BaseAbstractAttributeInterface, Object> categoryDataValueMap = new HashMap<BaseAbstractAttributeInterface, Object>();
		
		HibernateDAO hibernateDAO = null;
		
		try
		{
			DAOFactory factory = DAOFactory.getInstance();
			hibernateDAO = (HibernateDAO) factory.getDAO(Constants.HIBERNATE_DAO);
			hibernateDAO.openSession(null);
			retrieveRecords(rootCategoryEntity, categoryDataValueMap, recordId);
		}
		catch (Exception e)
		{
			throw (DynamicExtensionsSystemException) handleRollback(e, "Error while retrieving data", hibernateDAO, true);
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

		return categoryDataValueMap;
	}
	
	/**
	 * @param rootCategoryEntity
	 * @param categoryDataValueMap
	 * @param rootCategoryEntityRecordId
	 * @throws SQLException
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	private void retrieveRecords(CategoryEntityInterface rootCategoryEntity, Map<BaseAbstractAttributeInterface, Object> categoryDataValueMap, long rootCategoryEntityRecordId) throws SQLException, DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		Long recordId = null;
		String categoryEntityTableName = "";
		String selectRecordIdQuery = "";
		
		boolean flag = false;
		
		if (rootCategoryEntity.getCategory() != null)
		{
			categoryEntityTableName = rootCategoryEntity.getTableProperties().getName();
			
			selectRecordIdQuery = "SELECT RECORD_ID FROM "+ categoryEntityTableName +" WHERE IDENTIFIER = "+rootCategoryEntityRecordId;
			
			List<Long> idList = getResultIDList(selectRecordIdQuery, "RECORD_ID");
		
			if (idList != null && idList.size() > 0)
			{
				recordId = idList.get(0);
			}
		}
				
		if (recordId == null)
		{
			flag = true;
			recordId = rootCategoryEntityRecordId;
		}
				
		Map<AbstractAttributeInterface, Object> tempEntityRecordsMap = new HashMap<AbstractAttributeInterface, Object>();
		tempEntityRecordsMap.putAll(EntityManager.getInstance().getEntityRecordById(rootCategoryEntity.getEntity(), recordId));
				
		Map<AbstractAttributeInterface, Object> attributeRecods = new HashMap<AbstractAttributeInterface, Object>();
				
		Iterator<AbstractAttributeInterface> mapIterator = tempEntityRecordsMap.keySet().iterator();
		while (mapIterator.hasNext())
		{
			Object obj = mapIterator.next();
			if (obj instanceof AttributeInterface)
			{
				attributeRecods.put((AbstractAttributeInterface) obj, tempEntityRecordsMap.get(obj));
			}
		}
				
		for (CategoryAttributeInterface categoryAttribute: rootCategoryEntity.getAllCategoryAttributes())
		{
			categoryDataValueMap.put(categoryAttribute, attributeRecods.get(categoryAttribute.getAttribute()));
		}
				
		Collection<CategoryAssociationInterface> categoryAssociationCollection = new ArrayList<CategoryAssociationInterface>(rootCategoryEntity.getCategoryAssociationCollection());
				
		for (CategoryAssociationInterface categoryAssociation: categoryAssociationCollection)
		{
			CategoryEntityInterface targetCategoryEntity = categoryAssociation.getTargetCategoryEntity();
					
			categoryEntityTableName = targetCategoryEntity.getTableProperties().getName();
					
			if (flag)
			{
				String query1 = "SELECT IDENTIFIER FROM "+ categoryAssociation.getCategoryEntity().getTableProperties().getName() + " WHERE RECORD_ID = "+recordId;
				
				List<Long> idList = getResultIDList(query1, "IDENTIFIER");
				
				if (idList != null && idList.size() > 0)
				{
					rootCategoryEntityRecordId = idList.get(0);
				}
			}
			
			selectRecordIdQuery = "SELECT RECORD_ID FROM "+ categoryEntityTableName +" WHERE "+ categoryAssociation.getConstraintProperties().getTargetEntityKey()+" = "+rootCategoryEntityRecordId;
					
			List<Map<BaseAbstractAttributeInterface, Object>> innerList = new ArrayList<Map<BaseAbstractAttributeInterface,Object>>();
			categoryDataValueMap.put(categoryAssociation, innerList);
					
			List<Long> recordIdList = getResultIDList(selectRecordIdQuery, "RECORD_ID");
					
			for (Long recId: recordIdList)
			{
				Map<BaseAbstractAttributeInterface, Object> innerMap = new HashMap<BaseAbstractAttributeInterface, Object>();
				innerList.add(innerMap);
			
				retrieveRecords(targetCategoryEntity, innerMap, recId);
			}
		}
	}

	/**
	 * @param entity
	 * @param dataValue
	 * @return
	 */
	private Map<CategoryEntityInterface, Map> initialiseEntityValueMap(Map<BaseAbstractAttributeInterface, ?> dataValue)
	{
		Map<CategoryEntityInterface, Map> categoryEntityMap = new HashMap<CategoryEntityInterface, Map>();

		for (BaseAbstractAttributeInterface baseAbstractAttributeInterface : dataValue.keySet())
		{
			CategoryEntityInterface attributeCategoryEntity = null;
			if (baseAbstractAttributeInterface instanceof CategoryAttributeInterface)
			{
				attributeCategoryEntity = ((CategoryAttributeInterface) baseAbstractAttributeInterface)
						.getCategoryEntity();
			}
			else
			{
				attributeCategoryEntity = ((CategoryAssociationInterface) baseAbstractAttributeInterface)
				.getCategoryEntity();
			}
			Object value = dataValue.get(baseAbstractAttributeInterface);

			Map<BaseAbstractAttributeInterface, Object> entityDataValueMap = (Map) categoryEntityMap.get(attributeCategoryEntity);
			if (entityDataValueMap == null)
			{
				entityDataValueMap = new HashMap<BaseAbstractAttributeInterface, Object>();
				categoryEntityMap.put(attributeCategoryEntity, entityDataValueMap);
			}
			entityDataValueMap.put(baseAbstractAttributeInterface, value);
		}
		
		return categoryEntityMap;
	}
	
	/**
	 * @param categoryEntity
	 * @return
	 */
	private List<CategoryEntityInterface> getParentEntityList(CategoryEntityInterface categoryEntity)
	{
		List<CategoryEntityInterface> categoryEntityList = new ArrayList<CategoryEntityInterface>();
		categoryEntityList.add(categoryEntity);
		while (categoryEntity.getParentCategoryEntity() != null)
		{
			categoryEntityList.add(0, categoryEntity.getParentCategoryEntity());
			categoryEntity = categoryEntity.getParentCategoryEntity();
		}
		
		return categoryEntityList;
	}
	
	/**
	 * 
	 * @param rootCategoryEntity
	 * @param rootEntityRecordsMap
	 * @param attributeValueMap
	 */
	private void populateRootEntityRecordMap(CategoryEntityInterface rootCategoryEntity, Map<AbstractAttributeInterface, Object> rootEntityRecordsMap, Map<BaseAbstractAttributeInterface, Object> attributeValueMap)
	{
		Set<Entry<BaseAbstractAttributeInterface, Object>> categoryDataMapEntries = attributeValueMap.entrySet();

		AbstractAttributeInterface abstractAttribute = null;
		Object entityValue = null;
		CategoryAttributeInterface categoryAttribute;

		BaseAbstractAttributeInterface baseAbstractAttribute;
		
		Object categoryValue;
		for (Entry<BaseAbstractAttributeInterface, Object> entry : categoryDataMapEntries)
		{
			baseAbstractAttribute = entry.getKey();
			categoryValue = entry.getValue();
			if (baseAbstractAttribute instanceof CategoryAttributeInterface)
			{
				categoryAttribute = (CategoryAttributeInterface) baseAbstractAttribute;
				abstractAttribute = categoryAttribute.getAttribute();
				entityValue = categoryValue;
				if (categoryAttribute.getCategoryEntity().getCategory() != null)
				{
					rootEntityRecordsMap.put(abstractAttribute, entityValue);
				}
			}
		}	
		
		for (CategoryAssociationInterface categoryAssociation: rootCategoryEntity.getCategoryAssociationCollection())
		{
			for (AssociationInterface association: categoryAssociation.getCategoryEntity().getEntity().getAssociationCollection())
			{
				rootEntityRecordsMap.put(association, new ArrayList());
			}
		}
	}

	/**
	 * @param categoryEntity
	 * @param recordId
	 * @throws SQLException
	 */
	private void clearCategoryEntityData(CategoryEntityInterface categoryEntity, Long recordId) throws SQLException
	{		
		CategoryEntityInterface catEntity = categoryEntity;
		
		for (CategoryAssociationInterface categoryAssociation: catEntity.getCategoryAssociationCollection())
		{
			if (categoryAssociation.getTargetCategoryEntity().getChildCategories().size() != 0)
			{
				String selectQuery = "SELECT IDENTIFIER FROM "+categoryAssociation.getTargetCategoryEntity().getTableProperties().getName()+ " WHERE "+categoryAssociation.getConstraintProperties().getTargetEntityKey()+ " = "+recordId;
				
				List<Long> recordIdList = getResultIDList(selectQuery, "IDENTIFIER");
				for (Long recId: recordIdList)
				{
					clearCategoryEntityData(categoryAssociation.getTargetCategoryEntity(), recId);
					String deleteQuery = "DELETE FROM "+categoryAssociation.getTargetCategoryEntity().getTableProperties().getName()+ " WHERE IDENTIFIER ="+ recId;
					executeUpdateQuery(deleteQuery);
				}
			}
			else
			{				
				String deleteQuery = "DELETE FROM "+categoryAssociation.getTargetCategoryEntity().getTableProperties().getName()+ " WHERE "+categoryAssociation.getConstraintProperties().getTargetEntityKey()+ " = "+ recordId;
				executeUpdateQuery(deleteQuery);
			}
		}
	}
	
	/**
	 *
	 */
	protected DynamicExtensionBaseQueryBuilder getQueryBuilderInstance()
	{
		return queryBuilder;
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
		query.append(SELECT_KEYWORD + WHITESPACE + "RECORD_ID" + WHITESPACE + FROM_KEYWORD + WHITESPACE
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
	 * @see edu.common.dynamicextensions.entitymanager.CategoryManagerInterface#isPermissibleValuesSubsetValid(edu.common.dynamicextensions.domaininterface.AttributeInterface, java.util.List)
	 */
	public boolean isPermissibleValuesSubsetValid(UserDefinedDEInterface userDefinedDE, List<String> desiredPermissibleValues)
	{
		boolean arePermissibleValuesCorrect = true;

		if (userDefinedDE != null)
		{
			List<String> attributePermissibleValues = new ArrayList<String>();

			for (PermissibleValueInterface pv : userDefinedDE.getPermissibleValueCollection())
			{
				attributePermissibleValues.add(pv.getValueAsObject().toString());
			}

			for (String s : desiredPermissibleValues)
			{
				if (!attributePermissibleValues.contains(s))
				{
					arePermissibleValuesCorrect = false;
				}
			}
		}

		return arePermissibleValuesCorrect;
	}
	
	/**
	 * @param query
	 * @param columnName
	 * @return
	 * @throws SQLException
	 */
	private List<Long> getResultIDList(String query, String columnName) throws SQLException
	{
		List<Long> recordIdList = new ArrayList<Long>();
		Connection conn = null;
		Statement statement = null;
		ResultSet resultSet = null;
		Object value = null;
		
		try
		{
			conn = DBUtil.getConnection();
			statement = conn.createStatement();
			resultSet = statement.executeQuery(query);
					
			while (resultSet.next())
			{
				value = resultSet.getObject(columnName);
				recordIdList.add(new Long(value.toString()));
			}
		}
		catch (Exception e)
		{
			throw new SQLException(e.getMessage());
		}
		finally
		{
			resultSet.close();
			statement.close();
		}
		
		return recordIdList;
	}
	
	/**
	 * @param query
	 * @throws SQLException
	 */
	private void executeUpdateQuery(String query) throws SQLException
	{
		Connection conn = null;
		Statement statement = null;
		
		try
		{
			conn = DBUtil.getConnection();
			statement = conn.createStatement();
			statement.executeUpdate(query);
		}
		catch (Exception e)
		{
			throw new SQLException(e.getMessage());
		}
		finally
		{
			statement.close();
		}
	}

}