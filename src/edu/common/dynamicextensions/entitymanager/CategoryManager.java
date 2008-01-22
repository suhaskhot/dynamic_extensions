
package edu.common.dynamicextensions.entitymanager;

import java.util.ArrayList;
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
			DAOFactory factory = DAOFactory.getInstance();
			hibernateDAO = (HibernateDAO) factory.getDAO(Constants.HIBERNATE_DAO);
			hibernateDAO.openSession(null);
			EntityManagerInterface entityManager = EntityManager.getInstance();
			recordIdList = entityManager.insertData(entity, EntityDataValueMapList);

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
	 * 
	 * @param entityDataMap
	 * @return
	 */
	public Map<BaseAbstractAttributeInterface, Object> generateCategoryDataValueMap(CategoryInterface category,
			Map<AbstractAttributeInterface, Object> entityDataMap)
	{
		Map<BaseAbstractAttributeInterface, Object> categoryDataValueMap = new HashMap<BaseAbstractAttributeInterface, Object>();
		CategoryEntityInterface rootCategoryEntity = category.getRootCategoryElement();
		generateCategoryDataValueMapList(rootCategoryEntity, categoryDataValueMap, entityDataMap);
		return categoryDataValueMap;
	}

	/**
	 * 
	 * @param rootCategoryEntity
	 * @param categoryDataValueMap
	 * @param entityDataMap
	 * @return
	 */
	private Map<BaseAbstractAttributeInterface, Object> addCategoryAttributes(CategoryEntityInterface rootCategoryEntity,
			Map<BaseAbstractAttributeInterface, Object> categoryDataValueMap, Map<AbstractAttributeInterface, Object> entityDataMap)
	{
		if (entityDataMap != null)
		{
			for (CategoryAttributeInterface categoryAttribute : rootCategoryEntity.getCategoryAttributeCollection())
			{
				if (entityDataMap.get(categoryAttribute.getAttribute()) == null)
				{
					for (int i = 0; i < rootCategoryEntity.getPath().getSortedPathAssociationRelationCollection().size(); i++)
					{
						if (entityDataMap.containsKey(rootCategoryEntity.getPath().getSortedPathAssociationRelationCollection().get(i)
								.getAssociation()))
						{
							List tempList = ((List) entityDataMap.get(rootCategoryEntity.getPath().getSortedPathAssociationRelationCollection()
									.get(i).getAssociation()));
							Object categoryAttributeValue = ((Map) tempList.get(0)).get(categoryAttribute.getAttribute());
							categoryDataValueMap.put(categoryAttribute, categoryAttributeValue);
							break;
						}
					}
				}
				else
				{
					categoryDataValueMap.put(categoryAttribute, entityDataMap.get(categoryAttribute.getAttribute()));
				}
			}
		}
		return categoryDataValueMap;
	}

	/**
	 * 
	 * @param rootCategoryEntity
	 * @param categoryDataValueMap
	 * @param entityDataMap
	 */
	private void generateCategoryDataValueMapList(CategoryEntityInterface rootCategoryEntity,
			Map<BaseAbstractAttributeInterface, Object> categoryDataValueMap, Map<AbstractAttributeInterface, Object> entityDataMap)
	{
		Map<BaseAbstractAttributeInterface, Object> innerCategoryDataValueMap = new HashMap<BaseAbstractAttributeInterface, Object>();

		for (CategoryAssociationInterface c : rootCategoryEntity.getCategoryAssociationCollection())
		{
			if (c != null)
			{
				addCategoryAttributes(rootCategoryEntity, categoryDataValueMap, entityDataMap);

				CategoryEntityInterface targetCategoryEntity = c.getTargetCategoryEntity();
				PathAssociationRelationInterface pathAssociationRelation = targetCategoryEntity.getPath()
						.getSortedPathAssociationRelationCollection().get(0);

				List<Map<AbstractAttributeInterface, Object>> innerCategoryDataValueMapList = (List) entityDataMap.get(pathAssociationRelation
						.getAssociation());

				List<Map<BaseAbstractAttributeInterface, Object>> outerCategoryDataValueMapList = new ArrayList<Map<BaseAbstractAttributeInterface, Object>>();

				for (CategoryAttributeInterface categoryAttribute : targetCategoryEntity.getCategoryAttributeCollection())
				{
					if (innerCategoryDataValueMapList.get(0).get(categoryAttribute.getAttribute()) == null)
					{
						for (int i = 0; i < targetCategoryEntity.getPath().getSortedPathAssociationRelationCollection().size(); i++)
						{
							if (innerCategoryDataValueMapList.get(0).containsKey(
									targetCategoryEntity.getPath().getSortedPathAssociationRelationCollection().get(i).getAssociation()))
							{
								List tempList = ((List) innerCategoryDataValueMapList.get(0).get(
										targetCategoryEntity.getPath().getSortedPathAssociationRelationCollection().get(i).getAssociation()));
								for (int j = 0; j < tempList.size(); j++)
								{
									outerCategoryDataValueMapList.add(addCategoryAttributes(targetCategoryEntity, innerCategoryDataValueMap,
											innerCategoryDataValueMapList.get(0)));
								}
								break;
							}
						}
					}
					else
					{
						List tempList = ((List) entityDataMap.get(targetCategoryEntity.getPath().getSortedPathAssociationRelationCollection().get(0)
								.getAssociation()));
						for (int k = 0; k < tempList.size(); k++)
						{
							//Map tempMap = new HashMap();
							//tempMap.put(categoryAttribute, innerCategoryDataValueMapList.get(k).get(categoryAttribute.getAttribute()));
							outerCategoryDataValueMapList.add(addCategoryAttributes(targetCategoryEntity, innerCategoryDataValueMap,
									innerCategoryDataValueMapList.get(k)));
							//outerCategoryDataValueMapList.add(tempMap);
						}
					}
				}

				//outerCategoryDataValueMapList.add(addCategoryAttributes(targetCategoryEntity, innerCategoryDataValueMap,
				//innerCategoryDataValueMapList.get(0)));

				entityDataMap = innerCategoryDataValueMapList.get(0);

				categoryDataValueMap.put(c, outerCategoryDataValueMapList);

				generateCategoryDataValueMapList(targetCategoryEntity, innerCategoryDataValueMap, entityDataMap);
			}
		}
	}

}