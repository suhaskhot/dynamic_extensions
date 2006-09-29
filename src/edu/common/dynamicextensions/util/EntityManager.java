package edu.common.dynamicextensions.util;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.bizlogic.QueryBizLogic;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.security.exceptions.UserNotAuthorizedException;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * This is a singleton class that manages operations related to dynamic entity creation,attributes creation,
 * adding data into them and retrieving data from them.
 * @author geetika_bangard
 */
public class EntityManager {
    
    /**
     * Static instance of the entity manager.
     */
    private static EntityManager entityManager = null;
  //  DAOFactory daoFactory = DAOFactory.getInstance();
    
    /**
     * Empty Constructor.
     */
    protected EntityManager(){
    }
    
    /**
     * Returns the instance of the Entity Manager.
     * @return entityManager singleton instance of the Entity Manager.
     */
    public static synchronized EntityManager getInstance(){
        if(entityManager == null){
            entityManager = new EntityManager();
        }
        return entityManager;
    }/*
    
    *//**
     * Creates table to store values of attributes of the entity passed.
     * The table name that store entity related data is EAV_ENTITY + (id of the entity).
     * @param entity the entity domain object passed.
     * @throws AttributeCreationException
     * @throws EntityCreationException
     *//*
    public void createEntityWithAttributes(Entity entity) throws EntityCreationException, AttributeCreationException  {
        //Creates the table that in next step will be populated with attributes.
        createEntity(entity);
        Collection attributeCollection = entity.getAttributeCollection();
        if(attributeCollection  != null){
            Iterator attributeCollectionIterator = attributeCollection.iterator();
            while(attributeCollectionIterator.hasNext()){
                Attribute attribute = (Attribute)attributeCollectionIterator.next();
                //Adding the attribute columns to the table.
                addAttribute(attribute);
            }
        }
        
    }
    
    
    *//**
     * Creates table to store values of attributes of the entity passed.Only table is created with
     * the primary key.Columns are not added for the attributes in this method.
     * @param entity the entity domain object passed.
     * @throws EntityCreationException 
     *//*
    public void createEntity(Entity entity) throws EntityCreationException{
        HibernateDAO hibernateDAO = (HibernateDAO)daoFactory.getDAO(Constants.HIBERNATE_DAO);
        try {
            if(entity == null ) {
            	throw new EntityCreationException("while createEntity : Entity is Null");
            }
            hibernateDAO.openSession(null);
            hibernateDAO.insert(entity,null,false,false);
            String entityName = entity.getName();
            
            //Query to create the table with the identifier
            StringBuffer query = new  StringBuffer("CREATE TABLE "+ getEntityTableName(entity) + "( IDENTIFIER number(19,0) not null, "); 
            query = query.append("primary key (IDENTIFIER))");
            Logger.out.debug("[createEntity]Query formed is: "+query.toString());
            
            fireQuery(query.toString(),"while creating an entity table for "+entityName);
            //Creating sequence for the table formed.
            
            StringBuffer queryToGenerateSeq = new StringBuffer("CREATE SEQUENCE EAV_ENTITY" + entity.getId().toString()+"_SEQ START WITH 1 INCREMENT BY 1 MINVALUE 1");
            Logger.out.debug("[createEntity -- sequence query ]Query formed is: "+queryToGenerateSeq.toString());
            fireQuery(queryToGenerateSeq.toString(),"while creating a sequence for entity "+entityName);
            
            //Entering metadata in tables related to entity that will be used for search
            QueryInterfaceManager.insertMetdataOnCreateEntity(entity);
            hibernateDAO.commit();
        } catch (DAOException daoException){
            daoException.printStackTrace();
            
            try
            {  
                hibernateDAO.rollback();
            }
            catch(DAOException daoEx)
            {
                throw new EntityCreationException("Exception while hibernate rollback: "+daoEx.getMessage(), daoEx);
            }
            
            throw new EntityCreationException("Exception while creating Entity: "+daoException.getMessage(),daoException);
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
            throw new EntityCreationException(classNotFoundException);
        } catch (UserNotAuthorizedException userNotAuthorizedException) {
            userNotAuthorizedException.printStackTrace();
            throw new EntityCreationException(userNotAuthorizedException);
        }finally
        {
            try
            {
                hibernateDAO.closeSession();
            }
            catch(DAOException daoEx)
            {
                throw new EntityCreationException("Exception while closing the session");
            }
        }
        
    }
    
    *//**
     * Adds one column to the existing EAV table.
     * @param entity The entity for which attribute is ato be added.
     * @param attribute MName of the attribute to be added.
     * @throws ClassNotFoundException ClassNotFoundException
     * @throws AttributeCreationException 
     *//*
    public void addAttribute(Attribute attribute)  throws AttributeCreationException{
        HibernateDAO hibernateDAO = (HibernateDAO)daoFactory.getDAO(Constants.HIBERNATE_DAO);
        try {
            if(attribute == null){
            	throw new AttributeCreationException("Attribute passed is null");
            }
            hibernateDAO.openSession(null);
            hibernateDAO.insert(attribute,null,false,false);
            
            Entity entity = attribute.getEntity();
            if(entity == null){
            	throw new AttributeCreationException("Entity retrieved from the attribute passed is null");
            }
            String attributeName = getAttributeColumnName(attribute);
            if(attributeName == null){
            	throw new AttributeCreationException("attributeName retrieved from the attribute passed is null");
            }
            
            //Get the database specific data type for the attribute
            String dataType = getDataType(attribute);
            Logger.out.debug("Data type returned is :"+ dataType);
            StringBuffer queryToAddAttribute = new StringBuffer("ALTER TABLE " + getEntityTableName(entity)+ " ADD "+attributeName+ " " +dataType);
            Logger.out.debug("[addAttribute]Query formed is: "+queryToAddAttribute.toString());
            fireQuery(queryToAddAttribute.toString(),"While adding an attribute");
            
            //Entering metadata in tables related to attribute that will be used for search
            QueryInterfaceManager.insertMetadataOnAddAttribute(attribute);
            hibernateDAO.commit();
            QueryBizLogic.initializeQueryData();
        } catch(DAOException daoException) {
            daoException.printStackTrace();
            try
            {  
                hibernateDAO.rollback();
            }
            catch(DAOException daoEx)
            {
                throw new AttributeCreationException("Exception while hibernate rollback: "+daoEx.getMessage(), daoEx);
            }
            
            throw new AttributeCreationException("Exception while adding Attribute: "+daoException.getMessage(),daoException);
        } catch (UserNotAuthorizedException userNotAuthorizedException) {
            userNotAuthorizedException.printStackTrace();
            try
            {  
                hibernateDAO.rollback();
            }
            catch(DAOException daoEx)
            {
                throw new AttributeCreationException("Exception while hibernate rollback: "+daoEx.getMessage(), daoEx);
            }
            throw new AttributeCreationException(userNotAuthorizedException);
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
            try
            {  
                hibernateDAO.rollback();
            }
            catch(DAOException daoEx)
            {
                throw new AttributeCreationException("Exception while hibernate rollback: "+daoEx.getMessage(), daoEx);
            }
            throw new AttributeCreationException(classNotFoundException);
        } catch (DataTypeFactoryInitializationException dataTypeFactoryInitializationException) {
            dataTypeFactoryInitializationException.printStackTrace();
            try
            {  
                hibernateDAO.rollback();
            }
            catch(DAOException daoEx)
            {
                throw new AttributeCreationException("Exception while hibernate rollback: "+daoEx.getMessage(), daoEx);
            }
            throw new AttributeCreationException(dataTypeFactoryInitializationException);
        }finally
        {
            try
            {
                hibernateDAO.closeSession();
            }
            catch(DAOException daoEx)
            {
                throw new AttributeCreationException("Exception while closing the session");
            }
        }
        
    }
    
    *//**
     * Deletes the specified attribute from the existing EAV table.
     * @param entity the entity from which attribute column needs to be deleted.
     * @param attribute The attribute to be deleted.
     * @throws AttributeCreationException
     *//*
    public void deleteAttribute(Attribute attribute) throws AttributeDeletionException{
        HibernateDAO hibernateDAO = (HibernateDAO)daoFactory.getDAO(Constants.HIBERNATE_DAO);
        if(attribute == null){
        	throw new AttributeDeletionException("Attribute passed is null");
        }
        try{
            hibernateDAO.openSession(null);
            hibernateDAO.delete(attribute);
            hibernateDAO.closeSession();
            Entity entity = attribute.getEntity();
            if(entity == null){
            	throw new AttributeDeletionException("Entity retrieved from the attribute passed is null");
            }
            String attributeName = getAttributeColumnName(attribute);//eliminateSpaces(attribute.getName());
            if(attributeName == null){
            	throw new AttributeDeletionException("attributeName retrieved from the attribute passed is null");
            }
            StringBuffer query = new StringBuffer("ALTER TABLE "+ getEntityTableName(entity) + " DROP COLUMN "+attributeName);
            Logger.out.debug("[deleteAttribute]Query formed is: "+query.toString());
            fireQuery(query.toString(),"while deleting an attribute "+attribute.getName()+"for entity "+ entity.getName());
            //Deleting data from metadata tables for this attribute
            QueryInterfaceManager.deleteMetadataOnDeleteAttribute(attribute);  
            hibernateDAO.commit();
        }catch(DAOException daoException) {
            daoException.printStackTrace();
            try
            {  
                hibernateDAO.rollback();
            }
            catch(DAOException daoEx)
            {
                throw new AttributeDeletionException("Exception while hibernate rollback: "+daoEx.getMessage(), daoEx);
            }
            
            throw new AttributeDeletionException("Exception while creating Attribute: "+daoException.getMessage(),daoException);
        }  catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
            try
            {  
                hibernateDAO.rollback();
            }
            catch(DAOException daoEx)
            {
                throw new AttributeDeletionException("Exception while hibernate rollback: "+daoEx.getMessage(), daoEx);
            }
            throw new AttributeDeletionException(classNotFoundException);
        }finally
        {
            try
            {
                hibernateDAO.closeSession();
            }
            catch(DAOException daoEx)
            {
                throw new AttributeDeletionException("Exception while closing the session");
            }
        }
    }
    
    
    
    *//**
     * Inserts data of the attributes present in the entity.
     * @param entity Entity for which attribute data need to be entered.
     * @param dataValue Map conatining key as the column name and value as the data to be entered for that column.
     * @throws DataInsertionException
     *//*
    public void insertData(Entity entity, Map dataValue) throws DataInsertionException {//ParseException, DAOException, ClassNotFoundException {
        JDBCDAO jdbcDAO = (JDBCDAO)daoFactory.getDAO(Constants.JDBC_DAO);
        if(entity == null){
        	throw new DataInsertionException("Entity retrieved from the attribute passed is null");
        }
        if(dataValue ==  null){
        	throw new DataInsertionException("The data map passed is null");
        }
        String entityId = entity.getId().toString();
        LinkedHashMap columnsMap = new LinkedHashMap();
        //Map contains the mapping of the attribute name as shown on UI and the corresponding column name in database 
        //for that attribute
        Map colNameMapping = new LinkedHashMap();
        
        //Map contains key as the database column name of the attribute and value as the data type of that attribute
        HashMap attrNameDataTypeMap = new LinkedHashMap();
        
        ArrayList attributes = new ArrayList(entity.getAttributeCollection());
        if(attributes == null){
        	throw new DataInsertionException("attributes retrieved from the entity passed is null");
        }
        Iterator attributeIterator = attributes.iterator();
        
        while(attributeIterator.hasNext()){
            Attribute attribute = (Attribute)attributeIterator.next();
            attrNameDataTypeMap.put(getAttributeColumnName(attribute),attribute.getDataType());
            colNameMapping.put(attribute.getName(),getAttributeColumnName(attribute));
        }
        
        
        Set uiColumnSet = dataValue.keySet(); // Get the set of columns (attribute names) as shown on UI
        Iterator uiColumnSetIter = uiColumnSet.iterator();
        //Get the database column name corresponding to UI column name and populate columnsMap
        //in which the key is database column name and value as the data that is to be inserted for that column.
        while(uiColumnSetIter.hasNext()){
            String column = (String)uiColumnSetIter.next();
            String colunNameInTable = (String)colNameMapping.get(column);
            columnsMap.put(colunNameInTable,dataValue.get(column));
        }
   
        Long identifier;
        try {
            identifier = getNextIdentifier(entity);
        } catch (DAOException daoException) {
            daoException.printStackTrace();
            Logger.out.error(daoException.getMessage());
            throw new DataInsertionException("Exception while inserting data for entity",daoException);
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
            Logger.out.error(classNotFoundException.getMessage());
            throw new DataInsertionException("Exception while inserting data for entity",classNotFoundException);
        }
        Set columnSet = columnsMap.keySet();
        Iterator columnSetIterator = columnSet.iterator();
        
        //Create the INSERT query using the columnsMap
        StringBuffer query = new StringBuffer("INSERT INTO EAV_ENTITY"+entityId+" ( IDENTIFIER ,"); 
        while(columnSetIterator.hasNext()){
            String column = (String) columnSetIterator.next();
            query.append(column);
            if(columnSetIterator.hasNext())
                query.append(" , "); 
            else
                query.append(") VALUES(");
        }
        query.append(identifier + " , ");
        columnSetIterator=null;
        
        columnSetIterator = columnSet.iterator();
        while(columnSetIterator.hasNext()){
            String columnName = ((String) columnSetIterator.next());
            String columnValue = (String)columnsMap.get(columnName);
            //Get the data type of the attribute and form the query further accordingly
            String dataType = (String)attrNameDataTypeMap.get(columnName);
            System.out.println("data Type is _----------------"+dataType+"-------columnName is ----"+columnName+"--------columnValue is "+columnValue);
            if(dataType.equalsIgnoreCase("text") || dataType.equalsIgnoreCase("multiline")|| dataType.equalsIgnoreCase("choice")){
                query.append( "'"+ columnValue +"'" );
            }else if(dataType.equalsIgnoreCase("number")){
                query.append(columnValue);
            }else if(dataType.equalsIgnoreCase("datetime")){
                String dateStr = null;
                try {
                    dateStr = Utility.getDateInString(Utility.parseDate(columnValue));
                } catch (ParseException parseException) {
                    parseException.printStackTrace();
                    Logger.out.error(parseException.getMessage());
                    throw new DataInsertionException("Exception while inserting data for entity",parseException);
                }
                //              query.append( "TO_DATE('"+ dateStr +"','"+Constants.DATE_PATTERN_MM_DD_YYYY+"')" );
                query.append( Variables.strTodateFunction+"('"+ dateStr +"','"+Constants.DATE_PATTERN_MM_DD_YYYY+"')" );
            }
            
            if(columnSetIterator.hasNext())
                query.append(" , "); 
            else
                query.append(" )");
        }
        
        Logger.out.debug("INSERT QUERY FORMED IS :=== " + query.toString());
        try{
            fireQuery(query.toString(),"While inserting data into Entity table");
        }catch(DAOException daoException){
            daoException.printStackTrace();
            Logger.out.error("Exception caught while firing the query in EntityManager: "+ daoException.getMessage());
            throw new DataInsertionException("Exception while inserting data into Entity table",daoException);
        }finally
        {
            try
            {
                jdbcDAO.closeSession();
            }
            catch(DAOException daoException)
            {
                throw new DataInsertionException("Exception while closing the session",daoException);
            }
        }
        
//      }      
    }
    
    *//**
     * Retrieve data for the columns in the entity table.<br>
     * Returns map with key as the column name(to be shown on UI) and value as the data fetched for that column.
     * @param entity Entity for which attribute data is retrieved.
     * @return Map with key as the column name(on UI) and value as the data fetched for that column.
     * @throws DataRetrievalException
     *//*
    public List getRecords(Entity entity) throws DataRetrievalException{
        List resultList = new ArrayList();
        Map dataMap = null;
        if(entity == null){
        	throw new DataRetrievalException("Entity passed is null");
        }
        ArrayList attributes = new ArrayList(entity.getAttributeCollection());
        if(attributes == null || attributes.size() < 1){
            return new ArrayList();
        }
        //Map containing key as database column name and value as UI column name.
        Map colNameMapping = new LinkedHashMap();
        Iterator attributeIterator = attributes.iterator();
        //List containing database column names for the attributes.
        ArrayList columnNames = new ArrayList();
        while(attributeIterator.hasNext()){
            Attribute attribute = (Attribute)attributeIterator.next();
            columnNames.add(getAttributeColumnName(attribute));
            colNameMapping.put(getAttributeColumnName(attribute),attribute.getName());
        }
        //Forming the query 
        StringBuffer query = new StringBuffer(" SELECT ");
        Iterator columnNamesIter = columnNames.iterator();  
        while(columnNamesIter.hasNext()){
            String columnName = (String)columnNamesIter.next();
            query.append(columnName);
            if(columnNamesIter.hasNext())
                query.append(" , "); 
            
        }
        query.append(" FROM " + getEntityTableName(entity));
        Logger.out.debug("QUERY TO GET RECORDS IS : "+ query.toString());
        List result = new ArrayList();
        try {
            result = getResultInList(query.toString(),new SessionDataBean(),false,false,null);
        }catch(DAOException daoException){
            daoException.printStackTrace();
            Logger.out.error(daoException.getMessage());
            throw new DataRetrievalException("Exception while retrieving data for entity",daoException);
            
        } catch (ClassNotFoundException classNotFoundException) {
            classNotFoundException.printStackTrace();
            Logger.out.error(classNotFoundException.getMessage());
            throw new DataRetrievalException("Exception while retrieving data for entity",classNotFoundException);
        }
        //Populating the map with key as column name to be shown on Ui and value as value for that column.
        //In result set we get database column names, so we need to get the UI column name for that using colNameMapping Map.
        ArrayList internalList = null;
        if(result != null && !result.isEmpty()){
            for(int i=0;i<result.size();i++) { 
                dataMap = new LinkedHashMap();
                internalList = (ArrayList)result.get(i);
                if(internalList != null && !internalList.isEmpty()){
                    Iterator internalListIterator = internalList.iterator();
                    int size = columnNames.size();
                    int j = 0;
                    while(internalListIterator.hasNext() && j < size){
                        String value = (String)internalListIterator.next();
                        String uiColumnName = (String)colNameMapping.get(columnNames.get(j));
                        dataMap.put(uiColumnName,value);
                        j++;
                    }
                    resultList.add(dataMap);
                }   
            }
        }
        Logger.out.debug("FINAL LIST OF GET RECORDS IS :" + resultList);
        return resultList;
    }
    
    
    *//**
     * Returns the Oracle database type corresponding to the data base type of the attribute.
     * For e.g. if attribute data type is text, it will return VARCHAR2(size of attribute)
     *          if attribute data type is choice, it will return VARCHAR2(1000)
     *          if attribute data type is multiline, it will return VARCHAR2(size of attribute * 50)
     *          if attribute data type is datetime, it will return DATE
     *          if attribute data type is number, it will return NUMBER(size of attribute,2)
     * @param attribute The attribute passed.
     * @return
     * @throws DataTypeFactoryInitializationException
     * @throws AttributeCreationException
     *//*
    String getDataType(Attribute attribute) throws DataTypeFactoryInitializationException, AttributeCreationException{
        Long  size = attribute.getAttributeSize();
        if(size == null){
        	throw new AttributeCreationException("Size of the attribute is null");
        }
        String attrDataType = attribute.getDataType();
        if(attrDataType == null){
        	throw new AttributeCreationException("Data Type of the attribute is null");
        }
        DataTypeFactory dataTypeFactory = DataTypeFactory.getInstance();
        String dataType = dataTypeFactory.getDatabaseDataType(attrDataType);
        if(dataType == null){
        	throw new DataTypeFactoryInitializationException("Data type from factory is null");
        }
        if(dataType.equalsIgnoreCase("date")){
            return " DATE ";
        } else if(dataType.equalsIgnoreCase("number")){
            int scale = 0;
            if(attribute.getScale()!=null){
                scale = attribute.getScale().intValue();
            }
            int dataSize;
            if(size!=null){
                dataSize = size.intValue()- (attribute.getScale().intValue());
                return " NUMBER("+dataSize+","+scale+") ";
            }  
        }else if(dataType.equalsIgnoreCase("varchar2")){
            if(attrDataType.equalsIgnoreCase("text"))
                return " VARCHAR2("+size.toString()+")  ";
            else if(attrDataType.equalsIgnoreCase("multiline")){
                long calculatedSize = size.longValue() * 50;
                return " VARCHAR2("+calculatedSize+")  ";
            }   
            else if(attrDataType.equalsIgnoreCase("choice"))
                return " VARCHAR2(1000) " ;
        }
        return " VARCHAR(100) ";
    }
    
    
    *//**
     * Fires query to the database using JDBC DAO.
     * @param query the query to be fired.
     * @throws DAOException
     *//*
    void fireQuery(String query, String msg) throws DAOException{
        JDBCDAO jdbcDAO = (JDBCDAO)daoFactory.getDAO(Constants.JDBC_DAO);
        if(query == null){
    		throw new DAOException("Query passed is null");
    	}
        try{
            jdbcDAO.openSession(null);
            jdbcDAO.createTable(query);
            jdbcDAO.commit();
        }catch(DAOException ex){
            ex.printStackTrace();
            Logger.out.error("Exception caught while firing the query in EntityManager: "+ ex.getMessage());
            
            try
            {
                jdbcDAO.rollback();
            }
            catch(DAOException daoEx)
            {
                throw new DAOException("Exception caught while rollback: "+daoEx.getMessage(), daoEx);
            }
            throw new DAOException("Exception "+msg,ex);
        }
        
    }
    
    *//**
     * Executes a query and return result set.
     * @param queryToGetNextIdentifier
     * @param sessionDataBean
     * @param isSecureExecute
     * @param hasConditionOnIdentifiedField
     * @param queryResultObjectDataMap
     * @return
     * @throws DAOException
     * @throws ClassNotFoundException
     *//*
    List getResultInList(String queryToGetNextIdentifier, SessionDataBean sessionDataBean, boolean isSecureExecute, boolean hasConditionOnIdentifiedField, Map queryResultObjectDataMap) throws DAOException, ClassNotFoundException{
        List resultList = null;
        JDBCDAO jdbcDAO = (JDBCDAO)daoFactory.getDAO(Constants.JDBC_DAO);
        try {
            jdbcDAO.openSession(null);
            resultList = jdbcDAO.executeQuery(queryToGetNextIdentifier,sessionDataBean,isSecureExecute,hasConditionOnIdentifiedField,queryResultObjectDataMap);
        } catch (DAOException daoException) {
            daoException.printStackTrace();
            throw new DAOException("Exception while retrieving the query result",daoException);
        } finally{
            try
            {
                jdbcDAO.closeSession();
            }
            catch(DAOException daoException)
            {
                throw new DAOException("Exception while closing the jdbc session",daoException);
            }
        }
        return resultList;
    }
    
    
    *//**
     * Returns the name of the values tabnle related to the entity.
     * @param entity the entity.
     * @return the name of the values tabnle related to the entity.
     *//*
    String getEntityTableName(Entity entity){
        return("EAV_ENTITY"+entity.getId());
    }
    
    *//**
     * Returns the name of the table column for the attribute.
     * @param attribute the attribute.
     * @return the name of the table column for the attribute.
     *//*
    String getAttributeColumnName(Attribute attribute){
        return ("ATTRIBUTE"+attribute.getId());
    }
    
    *//**
     * Method generates the next identifier for the table that stores the value of the passes entity.
     * @param entity
     * @return
     * @throws DAOException
     * @throws ClassNotFoundException
     *//*
    public Long getNextIdentifier(Entity entity)throws DAOException, ClassNotFoundException{
        
        String entityTableName = getEntityTableName(entity);
        StringBuffer queryToGetNextIdentifier = new StringBuffer("SELECT MAX(IDENTIFIER) FROM "+entityTableName);
        List resultList = getResultInList(queryToGetNextIdentifier.toString(),new SessionDataBean(),false,false,null);
        if(resultList == null){
            throw new DAOException("Could not fetch the next identifier for table "+entityTableName);
        }
        List internalList = (List)resultList.get(0);
        if(internalList == null || internalList.isEmpty()){
            throw new DAOException("Could not fetch the next identifier for table "+entityTableName); 
        }
        String idString = (String)(internalList.get(0));
        Long identifier = null;
        if(idString == null || idString.trim().equals("")){
            identifier = new Long(0); 
        } else{
            identifier = new Long(idString);
            if(identifier == null){
                identifier = new Long(0);
            }
        }
        long id = identifier.longValue();
        id++;
        identifier = new Long(id);
        return identifier;
    }*/
    
}
