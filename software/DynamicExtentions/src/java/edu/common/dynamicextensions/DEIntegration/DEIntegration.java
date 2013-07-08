/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author
 *@version 1.0
 */

package edu.common.dynamicextensions.DEIntegration;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.CacheException;
import edu.common.dynamicextensions.entitymanager.CategoryManager;
import edu.common.dynamicextensions.entitymanager.CategoryManagerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerUtil;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsCacheException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.DEConstants;
import edu.wustl.common.exception.BizLogicException;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.QueryWhereClause;
import edu.wustl.dao.condition.INClause;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;

/**
 *
 * @author shital_lawhale
 * TODO EOL
 */

public class DEIntegration implements IntegrationInterface
{

	/**
	 * entity map.
	 */
	private static Map<Object, Object> entityMap = new HashMap<Object, Object>();
	/**
	 * category entity map.
	 */
	private static Map<String, Long> categoryEntityMap = new HashMap<String, Long>();

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.DEIntegration.IntegrationInterface#addAssociation(java.lang.Long, java.lang.Long, boolean, boolean)
	 */
	public Long addAssociation(Long hookEntityId, Long dynamicEntityId, boolean isEntityFromXmi,
			boolean isCategory) throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException, BizLogicException
	{
		return null;
	}

	/**
	 * This method returns the Category Record Id BasedOn HookEntityRecId
	 * Steps:
	 * 1) it will search for root entity of this category entity
	 * 2) and then it will return record id of category based on root entity rec id
	 * @param categoryContainerId
	 * @param staticRecId
	 * @param hookEntityId
	 * @return the record id of the category depending on hook entity record id.
	 */
	public Collection<Long> getCategoryRecIdBasedOnHookEntityRecId(Long categoryContainerId,
			Long staticRecId, Long hookEntityId) throws DynamicExtensionsSystemException,
			DAOException
	{

		List<Long> staticRecIdList = new ArrayList<Long>();
		staticRecIdList.add(staticRecId);
		Collection<Long> recIdList = getCategoryRecIdBasedOnHookEntityRecId(categoryContainerId,
				staticRecIdList, hookEntityId);
		Collection<Long> catRecIds = new HashSet<Long>();
		updateHashSet(catRecIds, recIdList);
		return catRecIds;
	}

	/**
	 *
	 * @param categoryContainerId category Container Id.
	 * @param staticRecId static Record Id.
	 * @param hookEntityId hookEntityId.
	 * @param jdbcDao jdbcDao.
	 * @return record IDs.
	 * @throws DynamicExtensionsSystemException exception.
	 * @throws DAOException exception.
	 */
	public Collection<Long> getCategoryRecIdBasedOnHookEntityRecId(Long categoryContainerId,
			Long staticRecId, Long hookEntityId, JDBCDAO jdbcDao)
			throws DynamicExtensionsSystemException, DAOException
	{
		Long entityContainerId;
		if (categoryEntityMap.containsKey(categoryContainerId.toString()))
		{
			entityContainerId = categoryEntityMap.get(categoryContainerId.toString());
		}
		else
		{
			entityContainerId = EntityManager.getInstance().getCategoryRootContainerId(
					categoryContainerId);
			categoryEntityMap.put(categoryContainerId.toString(), entityContainerId);
		}

		updateEntityMap(entityContainerId, hookEntityId);
		if (!entityMap.containsKey(categoryContainerId.toString()))
		{
			String catTableName = EntityManager.getInstance().getDynamicTableName(
					categoryContainerId);
			entityMap.put(categoryContainerId.toString(), catTableName);
		}
		Collection<Long> catRecIds;
		try
		{
			String entitySql = "select identifier from "
					+ entityMap.get(entityContainerId.toString()).toString() + " where "
					+ entityMap.get(hookEntityId + "_" + entityContainerId).toString() + " = ?";
			List<ColumnValueBean> queryDataList = new LinkedList<ColumnValueBean>();
			queryDataList.add(new ColumnValueBean(entityMap.get(
					hookEntityId + "_" + entityContainerId).toString(), staticRecId));
			catRecIds = getResultCollection(entitySql, queryDataList, jdbcDao);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException("Error while fetching dynamic record id", e);
		}
		return catRecIds;
	}

	/**
	 *
	 * @param hookEntityId hookentityId
	 * @throws DynamicExtensionsSystemException system exception
	 * @return the container Id of the DE entities that are associated with given static hook entity
	 */
	public Collection getDynamicEntitiesContainerIdFromHookEntity(Long hookEntityId)
			throws DynamicExtensionsSystemException
	{
		/**
		 * get all associated de entities with static entity and get its container id
		 */
		Collection dynamicList = EntityManager.getInstance()
				.getDynamicEntitiesContainerIdFromHookEntity(hookEntityId);
		return dynamicList;
	}

	/**
	 *
	 * @param containerId
	 * @throws DynamicExtensionsSystemException system exception
	 * @return whether this entity is simple DE form /category.
	 */
	public boolean isCategory(Long containerId) throws DynamicExtensionsSystemException
	{
		boolean isCategory = false;
		Long contId;
		if (categoryEntityMap.containsKey((containerId + DEConstants.ISCATEGORY)))
		{
			contId = categoryEntityMap.get(containerId + DEConstants.ISCATEGORY);
		}
		else
		// if(contId == null)
		{
			contId = EntityManager.getInstance().isCategory(containerId);
			categoryEntityMap.put(containerId + DEConstants.ISCATEGORY, contId);
		}
		if (contId != null)
		{
			isCategory = true;
		}
		return isCategory;
	}

	/**
	 *This method returns the entry Record Id BasedOn HookEntityRecId
	 * @param categoryContainerId
	 * @param staticRecId
	 * @param hookEntityId
	 * @throws DynamicExtensionsSystemException system exception
	 * @return the record id of the category depending on hook entity record id.
	 */
	public Collection<Long> getDynamicEntityRecordIdFromHookEntityRecordId(String hookEntityRecId,
			Long containerId, Long hookEntityId) throws DynamicExtensionsSystemException
	{
		Collection<Long> recIdList;
		updateEntityMap(containerId, hookEntityId);
		EntityManagerUtil entityManagerUtil = new EntityManagerUtil();
		String entitySql = "select identifier from "
				+ entityMap.get(containerId.toString()).toString() + " where "
				+ entityMap.get(hookEntityId + "_" + containerId).toString() + "=?";
		List<ColumnValueBean> queryDataList = new LinkedList<ColumnValueBean>();
		queryDataList.add(new ColumnValueBean(entityMap.get(hookEntityId + "_" + containerId)
				.toString(), Long.valueOf(hookEntityRecId)));
		recIdList = entityManagerUtil.getResultInList(entitySql, queryDataList);
		return recIdList;
	}

	/**
	 * @param hookEntityRecId
	 * @param containerId
	 * @param hookEntityId
	 * @param dao
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DAOException
	 */
	public Collection<Long> getDynamicEntityRecordIdFromHookEntityRecordId(String hookEntityRecId,
			Long containerId, Long hookEntityId, JDBCDAO dao)
			throws DynamicExtensionsSystemException, DAOException
	{
		List<Long> hookEntityRecIdList = new ArrayList<Long>();
		hookEntityRecIdList.add(Long.valueOf(hookEntityRecId));
		Collection<Long> recIdList = getDynamicEntityRecordIdFromHookEntityRecordId(
				hookEntityRecIdList, containerId, hookEntityId, dao);
		Collection<Long> dynRecIds = new HashSet<Long>();
		updateHashSet(dynRecIds, recIdList);
		return dynRecIds;
	}

	/**
	 *
	 * @param hookEntityId
	 * @throws DynamicExtensionsSystemException system exception
	 * @return the record id of the category depending on hook entity record id.
	 * @throws DynamicExtensionsCacheException
	 */
	public Collection getCategoriesContainerIdFromHookEntity(Long hookEntityId)
			throws DynamicExtensionsSystemException, DynamicExtensionsCacheException
	{
		Collection dynamicList = EntityManager.getInstance()
				.getCategoriesContainerIdFromHookEntity(hookEntityId);
		return dynamicList;
	}

	/**
	 * Gets the static categories container id.
	 *
	 * @return the static categories container id
	 *
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	public Collection getStaticCategoriesContainerId() throws DynamicExtensionsSystemException
	{
		Collection staticList = CategoryManager.getInstance()
				.getAllStaticCategoryBeans();
		return staticList;
	}

	/**
	 * this method returns DynamicRecord From association id
	 * @param recEntryId recordEntryId
	 * @param containerId containerId
	 * @param hookEntityId hookentityId
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 * @throws CacheException
	 * @throws DAOException
	 */
	public Collection<Long> getDynamicRecordFromStaticId(String recEntryId, Long containerId,
			String hookEntityId) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException, CacheException, DAOException
	{
		Collection<Long> recIdList;
		if (isCategory(containerId))
		{
			recIdList = getCategoryRecIdBasedOnHookEntityRecId(containerId, Long
					.valueOf(recEntryId), Long.valueOf(hookEntityId));
		}
		else
		{
			recIdList = getDynamicEntityRecordIdFromHookEntityRecordId(recEntryId, containerId,
					Long.valueOf(hookEntityId));
		}
		return recIdList;
	}

	/**
	 * @param recEntryId recordEntryId
	 * @param containerId containerId
	 * @param hookEntityId hookEntityId
	 * @param dao Dao
	 * @return
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 * @throws CacheException
	 * @throws DAOException
	 */
	public Collection<Long> getDynamicRecordForCategoryFromStaticId(String recEntryId,
			Long containerId, String hookEntityId, JDBCDAO dao)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException,
			CacheException, DAOException
	{
		Collection<Long> recIdList;
		if (isCategory(containerId))
		{
			recIdList = getCategoryRecIdBasedOnHookEntityRecId(containerId, Long
					.valueOf(recEntryId), Long.valueOf(hookEntityId), dao);
		}
		else
		{
			recIdList = getDynamicEntityRecordIdFromHookEntityRecordId(recEntryId, containerId,
					Long.valueOf(hookEntityId), dao);
		}
		return recIdList;
	}

	/**
	 * This method returns the Category Record_Id i.e. id of its root entity
	 * based on categoryContainerId and dynamicEntityRecordId.
	 * @param categoryContainerId category Container Id
	 * @param dynamicEntityRecordId dynamicEntity record id
	 * @return record id list
	 * @throws DynamicExtensionsSystemException exception.
	 */
	public Collection<Long> getCategoryRecordIdBasedOnCategoryId(Long categoryContainerId,
			Long dynamicEntityRecordId) throws DynamicExtensionsSystemException
	{

		String catTableName;
		/*
		 * Checked for categoryContainerId in the entityMap if present then get the Category Table Name from the Map
		 * else get it from DB.
		 */
		if (entityMap.containsKey(categoryContainerId.toString()))
		{
			catTableName = (String) entityMap.get(categoryContainerId.toString());
		}
		else
		{
			catTableName = EntityManager.getInstance().getDynamicTableName(categoryContainerId);
			entityMap.put(categoryContainerId.toString(), catTableName);
		}
		String catSql = "select RECORD_ID from " + catTableName + " where identifier= ?";
		List<ColumnValueBean> queryDataList = new LinkedList<ColumnValueBean>();
		queryDataList.add(new ColumnValueBean(DEConstants.IDENTIFIER, dynamicEntityRecordId));
		Collection<Long> recIdList;
		try
		{
			recIdList = getResultCollection(catSql, queryDataList, null);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException("Error occured in DAO operations", e);
		}
		return recIdList;
	}

	/**
	 *
	 * @param catSql sql
	 * @param queryDataList queryDataList
	 * @param dao jdbcdao
	 * @return record id list
	 * @throws DynamicExtensionsSystemException exception.
	 * @throws DAOException exception.
	 */
	private Collection<Long> getResultCollection(String catSql,
			List<ColumnValueBean> queryDataList, JDBCDAO dao)
			throws DynamicExtensionsSystemException, DAOException
	{
		ResultSet resultSet = null;
		Collection<Long> recIdList = new HashSet<Long>();
		JDBCDAO jdbcDao = dao;
		try
		{
			if (dao == null)
			{
				jdbcDao = DynamicExtensionsUtility.getJDBCDAO();
			}
			resultSet = jdbcDao.getResultSet(catSql, queryDataList, null); //util.executeQuery(entitySql);
			while (resultSet.next())
			{
				recIdList.add(resultSet.getLong(1));
			}
		}
		catch (SQLException e)
		{
			throw new DynamicExtensionsSystemException("Error while opening session", e);
		}
		finally
		{

			jdbcDao.closeStatement(resultSet);
			if (dao == null)
			{
				DynamicExtensionsUtility.closeDAO(jdbcDao);
			}
		}
		return recIdList;
	}

	/**
	 * @param catRecIds category record IDs.
	 * @param recIdList Record Id List.
	 */
	private void updateHashSet(Collection<Long> catRecIds, Collection<Long> recIdList)
	{
		if (recIdList != null && recIdList.toArray().length > 0
				&& ((List) recIdList.toArray()[0]).size() > 0)
		{
			catRecIds.add(Long.valueOf(((List) recIdList.toArray()[0]).get(0).toString()));

		}
	}

	/**
	 * @param staticRecIdList static Record Id List.
	 * @param entityTableName entity table name.
	 * @param columnName column name.
	 * @param jdbcDao jdbcDao.
	 * @return record IDs
	 * @throws DAOException exception.
	 */
	private Collection<Long> getRecordIdList(List<Long> staticRecIdList, String entityTableName,
			String columnName, JDBCDAO jdbcDao) throws DAOException
	{
		List<ColumnValueBean> colValueBean = new ArrayList<ColumnValueBean>();
		List<Object> columnValues = new ArrayList<Object>();

		String[] columnNames = {"identifier"};

		for (Long colValue : staticRecIdList)
		{
			colValueBean.add(new ColumnValueBean(colValue));
			columnValues.add('?');
		}
		QueryWhereClause queryWhereClause = new QueryWhereClause(entityTableName);
		queryWhereClause.addCondition(new INClause(columnName, columnValues.toArray()));
		//jdbcDao.retrieve(entityTableName, columnNames, queryWhereClause, false);
		return jdbcDao
				.retrieve(entityTableName, columnNames, queryWhereClause, colValueBean, false);

	}

	/**
	 * This method returns the Category Record Id collection BasedOn HookEntityRecId
	 * @param hookEntityRecIdList hookEntity Record Id list.
	 * @param containerId container Id.
	 * @param hookEntityId hook entity Id.
	 * @param dao jdbc dao.
	 * @return record IDs.
	 * @throws DynamicExtensionsSystemException exception.
	 * @throws DAOException exception.
	 */
	public Collection<Long> getDynamicEntityRecordIdFromHookEntityRecordId(
			List<Long> hookEntityRecIdList, Long containerId, Long hookEntityId, JDBCDAO dao)
			throws DynamicExtensionsSystemException, DAOException
	{
		updateEntityMap(containerId, hookEntityId);
		return getRecordIdList(hookEntityRecIdList, entityMap.get(containerId.toString())
				.toString(), entityMap.get(hookEntityId + "_" + containerId).toString(), dao);
	}

	/**
	 * @param recEntryId recordEntry Id.
	 * @param containerId container Id.
	 * @param hookEntityId hook entity Id.
	 * @param dao
	 * @return record IDs.
	 * @throws DynamicExtensionsSystemException exception.
	 * @throws DAOException exception.
	 */
	public Collection<Long> getDynamicRecordForCategoryFromStaticId(List<Long> recEntryId,
			Long containerId, String hookEntityId, JDBCDAO dao)
			throws DynamicExtensionsSystemException, DAOException
	{
		Collection<Long> recIdList;
		if (isCategory(containerId))
		{
			recIdList = getCategoryRecIdBasedOnHookEntityRecId(containerId, recEntryId, Long
					.valueOf(hookEntityId));
		}
		else
		{
			recIdList = getDynamicEntityRecordIdFromHookEntityRecordId(recEntryId, containerId,
					Long.valueOf(hookEntityId), dao);
		}
		return recIdList;
	}

	/**
	 * This method returns the Category Record Id BasedOn HookEntityRecId.
	 * Steps:
	 * 1) it will search for root entity of this category entity
	 * 2) and then it will return record id of category based on root entity record id
	 * @param categoryContainerId category Container Id
	 * @param staticRecId static record Id
	 * @param hookEntityId hook entity Id
	 * @return the record id of the category depending on hook entity record id.
	 * @throws DynamicExtensionsSystemException exception.
	 * @throws DAOException exception.
	 */
	public Collection<Long> getCategoryRecIdBasedOnHookEntityRecId(Long categoryContainerId,
			List<Long> staticRecId, Long hookEntityId) throws DynamicExtensionsSystemException,
			DAOException
	{
		Collection<Long> catRecIds;
		Long entityContainerId;

		if (categoryEntityMap.containsKey(categoryContainerId.toString()))
		{
			entityContainerId = categoryEntityMap.get(categoryContainerId.toString());
		}
		else
		{
			entityContainerId = EntityManager.getInstance().getCategoryRootContainerId(
					categoryContainerId);
			categoryEntityMap.put(categoryContainerId.toString(), entityContainerId);
		}
		updateEntityMap(entityContainerId, hookEntityId);
		if (!entityMap.containsKey(categoryContainerId.toString()))
		{
			String catTableName = EntityManager.getInstance().getDynamicTableName(
					categoryContainerId);
			entityMap.put(categoryContainerId.toString(), catTableName);
		}
		JDBCDAO jdbcDao = null;
		try
		{
			jdbcDao = DynamicExtensionsUtility.getJDBCDAO();
			catRecIds = getRecordIdList(staticRecId, entityMap.get(entityContainerId.toString())
					.toString(), entityMap.get(hookEntityId + "_" + entityContainerId).toString(),
					jdbcDao);
		}
		finally
		{
			DynamicExtensionsUtility.closeDAO(jdbcDao);
		}
		return catRecIds;
	}

	/**
	 * This method will verify whether data associated with the given recordEntryId is properly hooked or
	 * not.
	 * @param containerId container Id.
	 * @param recordEntryId record entry id.
	 * @param recEntryEntityId static entity id.
	 * @return true if data is properly hooked else returns false.
	 * @throws DynamicExtensionsSystemException exception.
	 * @throws DAOException exception.
	 * @throws SQLException exception.
	 */
	public boolean isDataHooked(Long containerId, Long recordEntryId, Long recEntryEntityId)
			throws DynamicExtensionsSystemException, DAOException, SQLException
	{
		boolean isDataHooked = false;
		Collection<Long> entityRecordList = getCategoryRecIdBasedOnHookEntityRecId(containerId,
				recordEntryId, Long.valueOf(recEntryEntityId));

		if (entityRecordList != null && !entityRecordList.isEmpty())
		{
			Long dynamicRecId = entityRecordList.iterator().next();
			String catTableName = EntityManager.getInstance().getDynamicTableName(containerId);
			CategoryManagerInterface categoryManager = CategoryManager.getInstance();
			Long categoryRecordId = categoryManager.getRootCategoryEntityRecordIdByEntityRecordId(
					dynamicRecId, catTableName);
			if (categoryRecordId != null && !"".equalsIgnoreCase(categoryRecordId.toString()))
			{
				isDataHooked = true;
			}
		}
		return isDataHooked;
	}

	/**
	 * This method will verify weather data associated with the given dynamicEntityRecordId is properly
	 * hooked or not.
	 * @param containerId container Id.
	 * @param dynamicEntityRecordId record entry id.
	 * @return true if data is properly hooked else returns false.
	 * @throws DynamicExtensionsSystemException exception.
	 */
	public boolean isDataHooked(Long containerId, Long dynamicEntityRecordId)
			throws DynamicExtensionsSystemException
	{
		boolean isDataHooked = false;
		CategoryManagerInterface categoryManager = CategoryManager.getInstance();
		String catTableName = EntityManager.getInstance().getDynamicTableName(containerId);
		Long categoryRecordId = categoryManager.getRootCategoryEntityRecordIdByEntityRecordId(
				dynamicEntityRecordId, catTableName);
		if (categoryRecordId != null && !"".equalsIgnoreCase(categoryRecordId.toString()))
		{
			isDataHooked = true;
		}
		return isDataHooked;
	}

	private void updateEntityMap(Long containerId, Long hookEntityId)
			throws DynamicExtensionsSystemException
	{
		if (!entityMap.containsKey(containerId.toString()))
		{
			String tableName = EntityManager.getInstance().getDynamicTableName(containerId);
			String columnName = EntityManager.getInstance().getColumnNameForAssociation(
					hookEntityId, containerId);

			entityMap.put(containerId.toString(), tableName);
			entityMap.put(hookEntityId + "_" + containerId, columnName);
		}
	}
}
