
package edu.common.dynamicextensions.util;

import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.databaseproperties.TableProperties;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.AbstractDAO;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * @author geetika_bangard
 *
 */
public class QueryInterfaceManager
{

	/**
	 * Static instance of the entity manager.
	 */
	private static EntityManager entityManager = EntityManager.getInstance();
	AbstractDAO daoFactory = DAOFactory.getInstance().getDAO(Constants.HIBERNATE_DAO);

	//    DAOFactory daoFactory = DAOFactory.getInstance();

	/**
	 * This method inserts required metadata in tables while creating an entity.<br>
	 * The tables are: CATISSUE_QUERY_TABLE_DATA and CATISSUE_TABLE_RELATION.<br>
	 * This metadata is required for querying on entities through simple search.
	 * @param entity the entity for which metadata needs to be inserted.
	 * @throws ClassNotFoundException  ClassNotFoundException
	 * @throws DAOException DAOException
	 */
	public static void insertMetdataOnCreateEntity(Entity entity) throws ClassNotFoundException, DAOException
	{

		//----------getting next sequence for CATISSUE_QUERY_TABLE_DATA 
		//        String tableIdentifier = getNextSequence("CATISSUE_QUERY_TABLE_DATA_SEQ");     
		String tableIdentifier = getNextSequence("CATISSUE_QUERY_TABLE_DATA", "TABLE_ID");

		//Inserting data into CATISSUE_QUERY_TABLE_DATA table
		//TODO see this if to be changed 
		String aliasName = "ALIAS_" + entity.getId();
		TableProperties tableProperties = (TableProperties) entity.getTableProperties();
		String entityTableName = tableProperties.getName();
		String queryToInsertDataInQueryTableData = "INSERT INTO CATISSUE_QUERY_TABLE_DATA"
				+ "(TABLE_ID,TABLE_NAME,DISPLAY_NAME,ALIAS_NAME,PRIVILEGE_ID, FOR_SQI) VALUES( " + tableIdentifier + " , '" + entityTableName
				+ "' , '" + entity.getName() + "' , '" + aliasName + "',0,1)";
		Logger.out.debug("[createEntity -- queryToInsertDataInQueryTableData" + queryToInsertDataInQueryTableData);
		fireQuery(queryToInsertDataInQueryTableData, "while inserting data into CATISSUE_QUERY_TABLE_DATA");

		//Generating seq for CATISSUE_TABLE_RELATION
		String identifierForTableRelation = getNextSequence("CATISSUE_TABLE_RELATION", "RELATIONSHIP_ID");

		//Inserting data into CATISSUE_TABLE_RELATION
		String queryToInsertDataInTableRelation = "INSERT INTO CATISSUE_TABLE_RELATION "
				+ "(RELATIONSHIP_ID, PARENT_TABLE_ID, CHILD_TABLE_ID) values ( " + identifierForTableRelation + " , " + tableIdentifier + " , "
				+ tableIdentifier + " )";
		Logger.out.debug("[addAttribute -- queryToInsertDataInQueryTableData" + queryToInsertDataInTableRelation);

		fireQuery(queryToInsertDataInTableRelation, "while inserting data into CATISSUE_TABLE_RELATION");
	}

	/**
	 * Fires query to the database using JDBC DAO.
	 * @param query the query to be fired.
	 * @param msg
	 * @throws DAOException
	 */
	
	static void fireQuery(String query, String msg) throws DAOException
	{
		//        JDBCDAO jdbcDAO = (JDBCDAO)daoFactory.getDAO(Constants.JDBC_DAO);
		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		if (query == null)
		{
			throw new DAOException("Query passed is null");
		}
		try
		{
			jdbcDAO.openSession(null);
			jdbcDAO.createTable(query);
			jdbcDAO.commit();
		}
		catch (DAOException ex)
		{
			ex.printStackTrace();
			Logger.out.error("Exception caught while firing the query in EntityManager: " + ex.getMessage());

			try
			{
				jdbcDAO.rollback();
			}
			catch (DAOException daoEx)
			{
				throw new DAOException("Exception caught while rollback: " + daoEx.getMessage(), daoEx);
			}
			throw new DAOException("Exception " + msg, ex);
		}

	}

	/**
	 * Executes a query and return result set.
	 * @param queryToGetNextIdentifier
	 * @param sessionDataBean
	 * @param isSecureExecute
	 * @param hasConditionOnIdentifiedField
	 * @param queryResultObjectDataMap
	 * @return
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	static List getResultInList(String queryToGetNextIdentifier, SessionDataBean sessionDataBean, boolean isSecureExecute,
			boolean hasConditionOnIdentifiedField, Map queryResultObjectDataMap) throws DAOException, ClassNotFoundException
	{
		List resultList = null;
		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		try
		{
			jdbcDAO.openSession(null);
			resultList = jdbcDAO.executeQuery(queryToGetNextIdentifier, sessionDataBean, isSecureExecute, hasConditionOnIdentifiedField,
					queryResultObjectDataMap);
		}
		catch (DAOException daoException)
		{
			daoException.printStackTrace();
			throw new DAOException("Exception while retrieving the query result", daoException);
		}
		finally
		{
			try
			{
				jdbcDAO.closeSession();
			}
			catch (DAOException daoException)
			{
				throw new DAOException("Exception while closing the jdbc session", daoException);
			}
		}
		return resultList;
	}

	/**
	 * This method inserts required metadata in tables while adding an attribute.<br>
	 * The tables are: CATISSUE_INTERFACE_COLUMN_DATA and CATISSUE_SEARCH_DISPLAY_DATA.<br>
	 * This metadata is required for querying on entities and attributes through simple search.
	 * @param attribute the attribute for which metadata needs to be inserted.
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws DAOException DAOException
	 */
	/* public static void insertMetadataOnAddAttribute(Attribute attribute) throws ClassNotFoundException, DAOException{
	 Entity entity = attribute.getEntity();
	 //      ----------getting next sequence for CATISSUE_INTERFACE_COLUMN_DATA
	 String identifierForIntColData = getNextSequence("CATISSUE_INTERFACE_COLUMN_DATA","IDENTIFIER");  
	 
	 //----------Query to get the table id--------------
	 //The table id in CATISSUE_INTERFACE_COLUMN_DATA is referenced to table id in CATISSUE_QUERY_TABLE_DATA
	 
	 String tableId = getTableId(entity);
	 
	 String relationId = getRelationId(tableId);
	 
	 //Inserting data into CATISSUE_INTERFACE_COLUMN_DATA
	 String dataTypeForInterfaceColumnData = getDataType(attribute.getDataType());   
	 String queryToInsertDataInIntfColData = "INSERT INTO CATISSUE_INTERFACE_COLUMN_DATA" +
	 "(IDENTIFIER,TABLE_ID,COLUMN_NAME,ATTRIBUTE_TYPE) VALUES( "+identifierForIntColData+" , "+ tableId+
	 " , '"+entityManager.getAttributeColumnName(attribute)+"' , '"+dataTypeForInterfaceColumnData+"' )";
	 Logger.out.debug("[addAttribute -- queryToInsertDataInIntfColData" + queryToInsertDataInIntfColData);
	 entityManager.fireQuery(queryToInsertDataInIntfColData,"while inserting data into CATISSUE_INTERFACE_COLUMN_DATA");
	 
	 
	 //Inserting data into CATISSUE_SEARCH_DISPLAY_DATA
	 String queryToInsertDataInSearchDisplay = "INSERT INTO CATISSUE_SEARCH_DISPLAY_DATA " +
	 "(RELATIONSHIP_ID, COL_ID, DISPLAY_NAME) VALUES ( "+relationId+" , "+ identifierForIntColData+
	 " , '"+attribute.getName()+"' )";
	 Logger.out.debug("[addAttribute -- queryToInsertDataInSearchDisplay" + queryToInsertDataInSearchDisplay);
	 entityManager.fireQuery(queryToInsertDataInSearchDisplay,"while inserting data into CATISSUE_SEARCH_DISPLAY_DATA");
	 
	 }*/

	/**
	 * This method deletes required metadata in tables while deleting an attribute.<br>
	 * The tables are: CATISSUE_INTERFACE_COLUMN_DATA and CATISSUE_SEARCH_DISPLAY_DATA.<br>
	 * @param attribute the attribute for which metadata needs to be deleted on deleting an attribute.
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws DAOException DAOException
	 */
	/*public static void deleteMetadataOnDeleteAttribute(Attribute attribute) throws ClassNotFoundException, DAOException{
	 JDBCDAO jdbcDAO = (JDBCDAO)DAOFactory.getDAO(Constants.JDBC_DAO);
	 Entity entity = attribute.getEntity();
	 String tableId = getTableId(entity);

	 //Get the column_id from the INTERFACE_COLUMN_DATA table
	 String queryToGetColumnId = "SELECT IDENTIFIER FROM CATISSUE_INTERFACE_COLUMN_DATA where TABLE_ID = "+tableId+
	 " AND COLUMN_NAME = '"+entityManager.getAttributeColumnName(attribute)+"'";
	 List resultList1 = jdbcDAO.executeQuery(queryToGetColumnId,new SessionDataBean(),false,false,null);
	 if(resultList1 == null){
	 Logger.out.debug("column id from CATISSUE_INTERFACE_COLUMN_DATA could not be fetched");
	 throw new DAOException("column id from CATISSUE_INTERFACE_COLUMN_DATA could not be fetched");
	 }
	 List internalList1 = (List)resultList1.get(0);
	 String columnId =(String)(internalList1.get(0));  
	 
	 //DELETE from CATISSUE_INTERFACE_COLUMN_DATA
	 String queryToDeleteFromIntfColData ="DELETE FROM CATISSUE_INTERFACE_COLUMN_DATA where " +
	 " TABLE_ID = "+tableId + "AND COLUMN_NAME = '"+entityManager.getAttributeColumnName(attribute)+"'";
	 entityManager.fireQuery(queryToDeleteFromIntfColData,"while deleting data from CATISSUE_INTERFACE_COLUMN_DATA");
	 
	 //DELETE from CATISSUE_SEARCH_DISPLAY_DATA
	 String relationId = getRelationId(tableId);
	 
	 String queryToDeleteFromSearchDspData ="DELETE FROM CATISSUE_SEARCH_DISPLAY_DATA where " +
	 " RELATIONSHIP_ID = "+relationId+ " AND COL_ID = "+columnId;
	 entityManager.fireQuery(queryToDeleteFromSearchDspData,"while deleting data from CATISSUE_SEARCH_DISPLAY_DATA");
	 
	 }*/

	/**
	 * This method retrieves the next sequence for the passed sequence.
	 * @param tableSequenceName the table sequence for which next value is to be fetched.
	 * @return the next sequence for the passed sequence.
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws DAOException DAOException
	 */
	/*private  static String getNextSequence(String tableSequenceName) throws ClassNotFoundException, DAOException{
	 JDBCDAO jdbcDAO = (JDBCDAO)DAOFactory.getDAO(Constants.JDBC_DAO);
	 String nextSequence = null;
	 String queryToGetNextIdentifierForTableRelation =" SELECT "+ tableSequenceName +".NEXTVAL FROM DUAL";
	 jdbcDAO.openSession(null);
	 List resultList = jdbcDAO.executeQuery(queryToGetNextIdentifierForTableRelation,new SessionDataBean(),false,false,null);
	 if(resultList == null)
	 Logger.out.debug("Next sequence for the table CATISSUE_TABLE_RELATION could not be fetched");
	 List internalList = (List)resultList.get(0);
	 nextSequence =(String)(internalList.get(0)); 
	 return nextSequence;
	 }*/
	private static String getNextSequence(String tableName, String primaryKeyColumnName) throws ClassNotFoundException, DAOException
	{
		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		String queryToGetNextIdentifier = " SELECT MAX(" + primaryKeyColumnName + ") FROM " + tableName;
		jdbcDAO.openSession(null);
		List resultList = jdbcDAO.executeQuery(queryToGetNextIdentifier, new SessionDataBean(), false, false, null);
		if (resultList == null)
		{
			Logger.out.debug("Next sequence for the table " + tableName + " could not be fetched");
			throw new DAOException("Next sequence for the table " + tableName + " could not be fetched");
		}
		List internalList = (List) resultList.get(0);
		if (internalList == null || internalList.isEmpty())
		{
			throw new DAOException("Next sequence for the table " + tableName + " could not be fetched");
		}
		String idString = (String) (internalList.get(0));
		Long identifier = null;
		if (idString == null || idString.trim().equals(""))
		{
			identifier = new Long(0);
		}
		else
		{
			identifier = new Long(idString);
			if (identifier == null)
			{
				identifier = new Long(0);
			}
		}
		long id = identifier.longValue();
		id++;
		identifier = new Long(id);
		return identifier.toString();
	}

	/**
	 * retrieves table id from CATISSUE_QUERY_TABLE_DATA corresponding to the entity's table name.
	 * @param entity entity for which table id needs to be retrieved.
	 * @return table id from CATISSUE_QUERY_TABLE_DATA corresponding to the entity's table name.
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws DAOException DAOException
	 */
	/* private static String getTableId(Entity entity) throws ClassNotFoundException, DAOException{
	 JDBCDAO jdbcDAO = (JDBCDAO)DAOFactory.getDAO(Constants.JDBC_DAO);
	 String queryTogetTableId = "SELECT TABLE_ID FROM CATISSUE_QUERY_TABLE_DATA WHERE TABLE_NAME LIKE '"
	 +entityManager.getEntityTableName(entity)+"'";
	 jdbcDAO.openSession(null);
	 List resultList1 = jdbcDAO.executeQuery(queryTogetTableId,new SessionDataBean(),false,false,null);
	 if(resultList1 == null){
	 Logger.out.debug("table id from CATISSUE_QUERY_TABLE_DATA could not be fetched");
	 throw new DAOException("table id from CATISSUE_QUERY_TABLE_DATA could not be fetched");
	 }
	 List internalList1 = (List)resultList1.get(0);
	 String tableId =(String)(internalList1.get(0));   
	 return tableId;
	 }*/

	/**
	 * Retrieves the relationship id from CATISSUE_TABLE_RELATION where the parent table id equals the table id passed.
	 * @param tableId the table id for which relationship id needs to be fetched.
	 * @return the relationship id from CATISSUE_TABLE_RELATION where the parent table id equals the table id passed.
	 * @throws ClassNotFoundException ClassNotFoundException
	 * @throws DAOException DAOException
	 */
	/*private static String getRelationId(String tableId) throws ClassNotFoundException, DAOException{
	 JDBCDAO jdbcDAO = (JDBCDAO)DAOFactory.getDAO(Constants.JDBC_DAO);
	 String queryTogetRelationId = "SELECT RELATIONSHIP_ID FROM CATISSUE_TABLE_RELATION WHERE PARENT_TABLE_ID = "+tableId;
	 jdbcDAO.openSession(null);
	 List resultListForRelationId = jdbcDAO.executeQuery(queryTogetRelationId,new SessionDataBean(),false,false,null);
	 if(resultListForRelationId == null){
	 Logger.out.debug("Relationship id could not be fetched from CATISSUE_TABLE_RELATION");
	 throw new DAOException("Relationship id could not be fetched from CATISSUE_TABLE_RELATION");
	 }
	 List internalListForRelationId = (List)resultListForRelationId.get(0);
	 String relationId =(String)(internalListForRelationId.get(0));   
	 return relationId;
	 }*/

	/**
	 * 
	 * @param dataType
	 * @return
	 */
	/* private static String getDataType(String dataType){
	 if(dataType.equalsIgnoreCase("DateTime")){
	 return "date";
	 } else if(dataType.equalsIgnoreCase("number")){
	 return "double";
	 }else if(dataType.equalsIgnoreCase("text")||dataType.equalsIgnoreCase("multiline")||dataType.equalsIgnoreCase("choice")){
	 return "varchar";
	 }
	 return "varchar";
	 }*/

	/**
	 * 
	 * @param entity
	 * @return
	 * @throws ClassNotFoundException
	 * @throws DAOException 
	 */
	/* public static String getAliasNameForEntity(Entity entity) throws ClassNotFoundException, DAOException{
	 
	 String queryToGetAliasName = "SELECT ALIAS_NAME FROM CATISSUE_QUERY_TABLE_DATA WHERE TABLE_NAME LIKE '"+entityManager.getEntityTableName(entity)+"'";
	 List resultList = entityManager.getResultInList(queryToGetAliasName,new SessionDataBean(),false,false,null);
	 if(resultList == null)
	 Logger.out.debug("Alias Name from the table CATISSUE_INTERFACE_COLUMN_DATA could not be fetched");
	 List internalList = (List)resultList.get(0);
	 String aliasName =(String)(internalList.get(0));    
	 return aliasName;
	 }*/

}
