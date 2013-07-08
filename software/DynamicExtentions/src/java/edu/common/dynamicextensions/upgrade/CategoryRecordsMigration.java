
package edu.common.dynamicextensions.upgrade;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.common.util.logger.LoggerConfig;
import edu.wustl.dao.JDBCDAO;
import edu.wustl.dao.exception.DAOException;
import edu.wustl.dao.query.generator.ColumnValueBean;

/**
 * The Class CategoryRecordsMigration.
 */
public class CategoryRecordsMigration {

	static {
		LoggerConfig.configureLogger(System.getProperty("user.dir"));
	}

	//	/** The Constant LOGGER. */
	private static final Logger LOGGER = Logger.getCommonLogger(CategoryRecordsMigration.class);
	
	private static final String GET_CAT_TNAME_AND_ENTITY_ID   =   
		"select NAME, tabProperties.ABSTRACT_ENTITY_ID " +
		"from DYEXTN_DATABASE_PROPERTIES dbProperties, DYEXTN_TABLE_PROPERTIES tabProperties " +
		"where NAME like 'DE_C_%' AND tabProperties.IDENTIFIER = dbProperties.IDENTIFIER";

	private static final String INSERT_CAT_RECORD	=  
		"insert into DYEXTN_CATEGORY_RECORDS (IDENTIFIER,CATEGORY_ENTITY_ID,RECORD_ID,ACTIVITY_STATUS) " +
		" VALUES (?, ?, ?, ?) ";
	
	
	private static final String GET_CAT_REC_ID_BY_ENTITY_REC_ID  = 
		"select IDENTIFIER FROM DYEXTN_CATEGORY_RECORDS WHERE RECORD_ID = ? AND CATEGORY_ENTITY_ID = ?";

	
	private static final String GET_CAT_ASSOCIATIONS = 
		"select catEntity.IDENTIFIER, dbProperties.NAME, catAssociation.CATEGORY_ENTIY_ID,tempTable.tname " +
		"from " +
		"   DYEXTN_TABLE_PROPERTIES tabProperties inner join DYEXTN_DATABASE_PROPERTIES dbProperties on tabProperties.IDENTIFIER = dbProperties.IDENTIFIER " +
		"   inner join DYEXTN_CATEGORY_ENTITY catEntity on tabProperties.ABSTRACT_ENTITY_ID = catEntity.IDENTIFIER " +
		"   left outer join DYEXTN_CATEGORY_ASSOCIATION catAssociation on catAssociation.IDENTIFIER = catEntity.CATEGORY_ASSOCIATION_ID " +
		"   left outer join (select catEntity.IDENTIFIER ie, dbProperties.NAME tname " +
		"                    from " +
		"                       DYEXTN_TABLE_PROPERTIES tabProperties inner join DYEXTN_DATABASE_PROPERTIES dbProperties on tabProperties.IDENTIFIER = dbProperties.IDENTIFIER " +
		"                       inner join DYEXTN_CATEGORY_ENTITY catEntity on tabProperties.ABSTRACT_ENTITY_ID = catEntity.IDENTIFIER " +
		"                    where " +
		"                       catEntity.CATEGORY_ASSOCIATION_ID is null "+
		"                   ) tempTable on catAssociation.category_entity_id = tempTable.ie ";
	
	
	private static final String UPDATE_PARENT_CAT_REC_ID_BY_REC_ID_AND_CAT_ENT_ID = 
		"update DYEXTN_CATEGORY_RECORDS SET PARENT_CATEGORY_RECORD_ID = ? " +
		"where RECORD_ID = ? AND CATEGORY_ENTITY_ID = ? ";
		
	private static final Long BATCH_SIZE = 25L;
	
	
	private static class CategoryRecord {
		private Long    recordId;
		private String  status;
		
		public CategoryRecord(Long recordId, String status) {
			this.recordId = recordId;
			this.status = status;
		}
	}
	
	private static class CategoryAssociation {
		private Long categoryEntityId;
		private String categoryTab;
		private Long parentCategoryEntityId;
		private String parentCategoryTab;
		
		public CategoryAssociation(Long categoryEntityId, String categoryTab, Long parentCategoryEntityId, String parentCategoryTab) {
			this.categoryEntityId = categoryEntityId;
			this.categoryTab = categoryTab;
			this.parentCategoryEntityId = parentCategoryEntityId;
			this.parentCategoryTab = parentCategoryTab;
		}
	}
	
	/**
	 * The main method.
	 *
	 * @param args arguments to main.
	 *
	 * @throws DAOException the DAO exception
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	public static void main(String[] args) 
	{
		CategoryRecordsMigration upgradeDB = new CategoryRecordsMigration();
		try 
		{
			// Insert the data into DYEXTN_CATEGORY_RECORD
			Map<String, String> catTabParentColumnMap = upgradeDB.insert();
			LOGGER.info("*******************************************************************");
			LOGGER.info("******************* Insertion is Completed ************************");
			LOGGER.info("*******************************************************************");
			
			// Update the PARENT_CATEGORY_RECORD_ID column
			upgradeDB.update(catTabParentColumnMap);
			LOGGER.info("*******************************************************************");
			LOGGER.info("******************* Updation is Completed ************************");
			LOGGER.info("*******************************************************************");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.info("Checking for errors--------"+e.getMessage());
		}
	}

	/*
	 * The insert method
	 * IDENTIFIER,RECORD_ID,CATEGORY_ENTITY_ID,ACTIVITY_STATUS will be populated
	 * into DYEXTN_CATEGORY_RECORDS
	 * 
	 * Return : List of CategoryRecord consisting of category table name with corresponding 
	 * 			ParentCategoryColumnName 
	 * 
	 * @throws DAOException the DynamicExtensionsSystemException
	 * @throws DAOException the DAOException
	 */
	private Map<String, String> insert() 
	throws DynamicExtensionsSystemException, DAOException 
	{
		JDBCDAO jdbcDao = DynamicExtensionsUtility.getJDBCDAO();
		Map<String, Long> catTabNameEntityIdMap = getCatEntityDetails(jdbcDao);
		jdbcDao.closeSession();
		jdbcDao = null;

		Long identifier = (long) 1;
		Map<String, String> catTableParentColumnMap = new HashMap<String, String>();

		for (Entry<String, Long> tabNameCatEntityId : catTabNameEntityIdMap.entrySet()) {
			jdbcDao = DynamicExtensionsUtility.getJDBCDAO();
			
		    String tableName = tabNameCatEntityId.getKey();
		    Long catEntId = tabNameCatEntityId.getValue();
			boolean isTableExist = checkTableExist(tableName);
			if (!isTableExist) {
				continue;
			}
			
			LOGGER.info("CategoryRecordsMigration :: insert :: Current Category Table  ::"+tableName);
			Long maxId = getMaxId(jdbcDao, tableName);
			String query = new StringBuilder("select * FROM ").append(tableName)
			                         .append(" where IDENTIFIER >= ? AND IDENTIFIER <= ?").toString(); 
			String parentCategoryColumnName = getParentCategoryColumnName(jdbcDao, tableName);
			LOGGER.info("CategoryRecordsMigration :: insert :: parentCategoryColumnName ::"+parentCategoryColumnName);

			if (parentCategoryColumnName != null) {
				catTableParentColumnMap.put(tableName, parentCategoryColumnName);
			}
			
			
			
			for(long nextId = 0 ; nextId < maxId ; nextId += BATCH_SIZE) {
				nextId = getNextAvailableId(jdbcDao, tableName, nextId);

				List<CategoryRecord> catRecords = getCategoryRecords(jdbcDao, query, nextId, nextId + BATCH_SIZE);
				for (CategoryRecord catRec : catRecords) {
					LinkedList<ColumnValueBean> params = new LinkedList<ColumnValueBean>();
					params.add(new ColumnValueBean(identifier));
					params.add(new ColumnValueBean(catEntId));
					params.add(new ColumnValueBean(catRec.recordId));
					params.add(new ColumnValueBean(catRec.status));
					
					DynamicExtensionsUtility.executeUpdateQuery(INSERT_CAT_RECORD, null, jdbcDao, params);
					identifier++;
				}
				jdbcDao.commit();
			}
			
			jdbcDao.closeSession();
			jdbcDao = null;
		}
		return catTableParentColumnMap;
	}
	

	/*
	 * The update method 
	 * @param childWithParentColNames arguments to update.
	 * 
	 * For each row in DYEXTN_CATEGORY_RECORDS, if there exist value for
	 * ParentCategoryColumnName, it will be updated 
	 *
	 * @throws Exception
	 */
	private void update(Map<String, String> catTabParentColumnNameMap) 
	throws Exception {
		JDBCDAO jdbcDao = DynamicExtensionsUtility.getJDBCDAO();
		
		List<CategoryAssociation> categoryAssociations = getCategoryAssociations(jdbcDao);
		jdbcDao.closeSession();
		jdbcDao = null;
		
		for (CategoryAssociation catAssociation : categoryAssociations) {
			jdbcDao = DynamicExtensionsUtility.getJDBCDAO();
			
			if (catAssociation.parentCategoryEntityId == null || catAssociation.parentCategoryTab == null) {
				//
				// There is no parent category for this category
				//
				continue;
			}
			
			if (!checkTableExist(catAssociation.categoryTab)) {
				//
				// There is no physical table for this category
				//
				continue;
			}
			
			//
			// lookup parent category column name for this category table
			//
			String parentCatRecIdColumn = catTabParentColumnNameMap.get(catAssociation.categoryTab);
			if (parentCatRecIdColumn == null) {
				LOGGER.error("Error obtaining parent category record id column of table " + catAssociation.categoryTab);
				continue;
			}
			
			String getParentCatRecIdsQuery = new StringBuilder()
				.append("SELECT c.RECORD_ID, p.RECORD_ID FROM ")
				.append(catAssociation.categoryTab).append(" c, ").append(catAssociation.parentCategoryTab).append(" p ")
				.append("WHERE p.IDENTIFIER = c.").append(parentCatRecIdColumn).append(" and c.IDENTIFIER >= ? and c.IDENTIFIER <= ?")
				.toString();
			
			LOGGER.info("***** Query = " + getParentCatRecIdsQuery);

			Long maxId = getMaxId(jdbcDao, catAssociation.categoryTab);
			for (Long lastRecord = 0L; lastRecord < maxId; lastRecord += BATCH_SIZE) {
				long startTime = System.currentTimeMillis();
				lastRecord = getNextAvailableId(jdbcDao, catAssociation.categoryTab, lastRecord);
				
				LinkedList<ColumnValueBean> params = new LinkedList<ColumnValueBean>();
				params.add(new ColumnValueBean(lastRecord));
				params.add(new ColumnValueBean(lastRecord + BATCH_SIZE));
				ResultSet rs = jdbcDao.getResultSet(getParentCatRecIdsQuery, params, null);
				
				LOGGER.info("Table = " + catAssociation.categoryTab + ", " + lastRecord + ", " + (lastRecord + BATCH_SIZE));
				long accRowTime = 0;
				int rowCount = 0;
				while (rs.next()) {
					long t1 = System.currentTimeMillis();
					Long parentCatRecId = getCategoryRecIdFromEntityRecId(jdbcDao, rs.getLong(2), catAssociation.parentCategoryEntityId);
					updateParentCatRecIdByRecIdAndCatEntityId(jdbcDao, parentCatRecId, rs.getLong(1), catAssociation.categoryEntityId);
					accRowTime += (System.currentTimeMillis() - t1);
					rowCount++;
				}
					
				closeResultSet(jdbcDao,rs);
				jdbcDao.commit();
				
				LOGGER.info("Time to update parent record ids, table = " + catAssociation.categoryTab + ", time = " + (System.currentTimeMillis() - startTime) +
				         ", avgRowUpdateTime = " + ((rowCount != 0) ? accRowTime / rowCount : 0) + ", rowCount = " + rowCount);
			}
			
			jdbcDao.closeSession();
			jdbcDao = null;
		}
		
		DynamicExtensionsUtility.closeDAO(jdbcDao);
	}

	/*
	 * The checkTableExist method 
	 * @param tableName arguments to checkTableExist.
	 * 
	 * Return true  : If table exist.
	 * 		  false : If there is no table
	 */
	private boolean checkTableExist(String tableName) {
		JDBCDAO jdbcDao = null;
		ResultSet rs = null;

		try {
			jdbcDao = DynamicExtensionsUtility.getJDBCDAO();
			String query = "SELECT * FROM " + tableName + " WHERE 0 = 1";
			rs = jdbcDao.getResultSet(query, null, null);
			return true;
		} catch (Exception e) {
			// we assume the exception is because of non-existence of the table
			LOGGER.error("------------------------------------------");
			LOGGER.error("Database exception : Table " + tableName + " does not exist");
			LOGGER.error("------------------------------------------");
			return false;
		} finally {
			try {
				// Close statement only if resultSet is not null.
				if (rs != null) {
					jdbcDao.closeStatement(rs);
				}
				DynamicExtensionsUtility.closeDAO(jdbcDao);
			} catch (Exception e) {
				throw new RuntimeException("Error closing database resources", e);
			}
		}
	}

	/*
	 * The getMaxId method 
	 * @param tableName arguments to getMaxId.
	 * 
	 * Return the maximum(IDENTIFIER) 
	 */
	private Long getMaxId(JDBCDAO jdbcDao, String tableName) {
		ResultSet resultSet = null;
		String query = "select max(IDENTIFIER) from " + tableName;
		Long maxId = 0L;
		try {
			resultSet = jdbcDao.getResultSet(query, null, null);
			while (resultSet.next()) {
				maxId = resultSet.getLong(1);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error obtaining max identifier of table " + tableName, e);
		} finally {
			closeResultSet(jdbcDao,resultSet);
		}
		
		return maxId;
	}
	
	private Long getNextAvailableId(JDBCDAO jdbcDao, String tableName, Long lastSeenId) {
		
		String query = new StringBuilder("SELECT MIN(IDENTIFIER) FROM ")
			.append(tableName).append(" WHERE IDENTIFIER > ").append(lastSeenId).toString();
		
		ResultSet resultSet = null;
		try {
			Long nextId = null;
			resultSet = jdbcDao.getResultSet(query, null, null);
			if (resultSet != null && resultSet.next()) {
				nextId = resultSet.getLong(1);
			}
			
			return nextId;
		} catch (Exception e) {
			throw new RuntimeException("Error obtaining max identifier of table " + tableName, e);
		} finally {
			closeResultSet(jdbcDao, resultSet);
		}
	}

	/*
	 * The getCategoryRecords method 
	 * @param query argument to getCategoryRecords.
	 * @param startId, endId arguments to getCategoryRecord Select in BATCH from the table
	 * where startId <= IDENTIFIER < endId
	 * 
	 * Returns the List of CategoryRecord(ACTIVITY_STATUS,RECORD_ID)
	 */
	private List<CategoryRecord> getCategoryRecords(JDBCDAO jdbcDao, String query, Long startId, Long endId) {
		ResultSet rs = null;
		List<CategoryRecord> catRecs = new ArrayList<CategoryRecord>();
		
		try {
			List<ColumnValueBean> params = new ArrayList<ColumnValueBean>();
			params.add(new ColumnValueBean(startId));
			params.add(new ColumnValueBean(endId));
			
			rs = jdbcDao.getResultSet(query, params, null);
			while (rs.next()) {
				catRecs.add(new CategoryRecord(rs.getLong(3), rs.getString(1)));
			}
			
			return catRecs;
		} catch (Exception e) {
			throw new RuntimeException("Error executing query: " + query + " start = " + startId + " endId = " + endId, e);
		} finally {
			closeResultSet(jdbcDao, rs);
		}
	}
	
	/*
	 * The getParentCategoryColumnName method 
	 * @param tableName argument to getParentCategoryColumnName.
	 *
	 * Return the column name, which will point to parent category records.
	 */
	private String getParentCategoryColumnName(JDBCDAO jdbcDao, String tableName) {
		ResultSet resultSet = null;
		ResultSetMetaData rsmd = null;
		String parentCatColName = null;
		StringBuilder query = new StringBuilder("SELECT * FROM ").append(tableName).append(" WHERE 1 = 0");
		try {
			resultSet = jdbcDao.getResultSet(query.toString(), null, null);
			rsmd = resultSet.getMetaData();
			if(rsmd.getColumnCount() >= 4) {
				for (int i = 4; i <= rsmd.getColumnCount(); ++i) {
					if (rsmd.getColumnName(i).startsWith("DE_E_T_")) {
						parentCatColName = rsmd.getColumnName(i);
						break;
					}
				}
			}
			return parentCatColName;
		} catch (Exception e) {
			throw new RuntimeException("Error fetching parent category column name of table " + tableName, e);
		} finally {
			closeResultSet(jdbcDao, resultSet);
		}
	}
	
	/*
	 * The closeResultSet method 
	 * @param ResultSet, JEBCDAO argument to closeResultSet.
	 *
	 * Return the column name, which will point to parent category records.
	 */
	private void closeResultSet(JDBCDAO jdbcDao, ResultSet resultSet) {
		try {
			if(resultSet != null) {
				jdbcDao.closeStatement(resultSet);
			}
		} catch (Exception e) {
			throw new RuntimeException("Error closing database resources", e);
		}
	}

	private List<CategoryAssociation> getCategoryAssociations(JDBCDAO jdbcDao) {
		ResultSet resultSet = null;
		List<CategoryAssociation> categoryAssociations = new ArrayList<CategoryAssociation>();
		
		try {
			resultSet = jdbcDao.getResultSet(GET_CAT_ASSOCIATIONS, null, null);
			while (resultSet.next()) {
				//
				// tuple of {catEntityId, catTable, parentCatEntityId, parentCatTable}
				//
				categoryAssociations.add(new CategoryAssociation(resultSet.getLong(1), resultSet.getString(2), resultSet.getLong(3), resultSet.getString(4)));
			}
		} catch (Exception e) {
			throw new RuntimeException("Error obtaining category associations", e);
		} finally {
			closeResultSet(jdbcDao,resultSet);
		}
		
		return categoryAssociations;
	}


	/*
	 * The getCatEntityDetails method 
	 *
	 * Return : Map of category table name to category entity id
	 */
	private static Map<String, Long> getCatEntityDetails(JDBCDAO jdbcDao) {
		ResultSet resultSet = null;
		Map<String, Long> results = new HashMap<String, Long>();
		try {
			resultSet = jdbcDao.getResultSet(GET_CAT_TNAME_AND_ENTITY_ID, null, null);
			while (resultSet.next()) {
				//
				// tuple of {catTable, catEntityId}
				//
				results.put(resultSet.getString(1), resultSet.getLong(2));
			}
		} catch (Exception e) {
			throw new RuntimeException("Error obtaining category entity details", e);
		} finally {
			try {
				jdbcDao.closeStatement(resultSet);
			} catch (Exception e) {
				// should not occur. if it occurs we fail entire process
				throw new RuntimeException("Error closing database resources", e);
			}
		}
		return results;
	}


	/*
	 * The getCategoryRecIdFromEntityRecId method 
 	 * @param recordId, catEntityId argument to getCategoryRecIdFromEntityRecId.
	 * Return : rootCategoryRecordId
	 * 
     * @throws Exception
	 */
	private Long getCategoryRecIdFromEntityRecId(JDBCDAO jdbcDao, Long recordId, Long catEntityId) 
	throws Exception {
		LinkedList<ColumnValueBean> params = new LinkedList<ColumnValueBean>();
		params.add(new ColumnValueBean(recordId));
		params.add(new ColumnValueBean(catEntityId));

		List<Long> results = getResultIDList(jdbcDao, GET_CAT_REC_ID_BY_ENTITY_REC_ID, "IDENTIFIER", params);
		Long rootCERecId = null;
		if (results != null && !results.isEmpty()) {
			rootCERecId = results.get(0);
		}
		return rootCERecId;
	}
	
	/*
	 * The updateParentCatRecIdByRecIdAndParRecId method 
 	 * @param parentCatRecId, entRecId, catEntityId argument to updateParentCatRecIdByRecIdAndParRecId.
	 */
	private void updateParentCatRecIdByRecIdAndCatEntityId(JDBCDAO jdbcDao, Long parentCatRecId, Long entRecId, Long catEntityId) 
	throws DynamicExtensionsSystemException {
		LinkedList<ColumnValueBean> params = new LinkedList<ColumnValueBean>();
		params.add(new ColumnValueBean(parentCatRecId));
		params.add(new ColumnValueBean(entRecId));
		params.add(new ColumnValueBean(catEntityId));

		DynamicExtensionsUtility.executeUpdateQuery(UPDATE_PARENT_CAT_REC_ID_BY_REC_ID_AND_CAT_ENT_ID, null, jdbcDao, params);
	}


	/*
	 * The getResultIDList method 
 	 * @param query, columnName argument to getResultIDList.
 	 * 
 	 * Return : IDENTIFIER selected from the query
 	 * 
     * @throws Exception 
	 */
	private List<Long> getResultIDList(JDBCDAO jdbcDao, String query, String columnName, List<ColumnValueBean> params)
	throws Exception {
		final List<Long> ids = new ArrayList<Long>();
		ResultSet resultSet = null;

		try {
			resultSet = jdbcDao.getResultSet(query, params, null);

			while (resultSet.next()) {
				Object value = resultSet.getObject(columnName);
				ids.add(Long.valueOf(value.toString()));
			}
		} catch (Exception e) {
			throw new Exception("Error executing query: " + query, e);
		} finally {
			closeResultSet(jdbcDao,resultSet);
		}
		
		return ids;
	}
}