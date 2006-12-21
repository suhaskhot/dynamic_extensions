
package edu.common.dynamicextensions.entitymanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import edu.common.dynamicextensions.domain.AbstractAttribute;
import edu.common.dynamicextensions.domain.Association;
import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.domain.BooleanAttributeTypeInformation;
import edu.common.dynamicextensions.domain.DateAttributeTypeInformation;
import edu.common.dynamicextensions.domain.DoubleAttributeTypeInformation;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.FileAttributeTypeInformation;
import edu.common.dynamicextensions.domain.FloatAttributeTypeInformation;
import edu.common.dynamicextensions.domain.IntegerAttributeTypeInformation;
import edu.common.dynamicextensions.domain.LongAttributeTypeInformation;
import edu.common.dynamicextensions.domain.ShortAttributeTypeInformation;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.databaseproperties.ConstraintPropertiesInterface;
import edu.common.dynamicextensions.exception.DataTypeFactoryInitializationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.global.Constants;
import edu.common.dynamicextensions.util.global.Variables;
import edu.common.dynamicextensions.util.global.Constants.AssociationType;
import edu.common.dynamicextensions.util.global.Constants.Cardinality;
import edu.wustl.common.dao.HibernateDAO;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.logger.Logger;

/**
 * This class provides the methods that builds the queries that are required for
 * creation and updation of the tables of the entities.These queries are as per SQL-99 standard.
 * Theses methods can be over-ridden  in the database specific query builder class to 
 * provide any database-specific implemention.
 * 
 * @author Rahul Ner
 */
class DynamicExtensionBaseQueryBuilder
		implements
			EntityManagerConstantsInterface,
			EntityManagerExceptionConstantsInterface
{

	EntityManagerUtil entityManagerUtil = new EntityManagerUtil();

	/**
	 * This method builds the list of all the queries that need to be executed in order to 
	 * create the data table for the entity and its associations.
	 * 
	 * @param entity Entity for which to get the queries.
	 * @param reverseQueryList For every data table query the method builds one more query 
	 * which negates the effect of that data table query. All such reverse queries are added in this list.
	 * @param rollbackQueryStack 
	 * @param hibernateDAO 
	 * 
	 * @return List of all the data table queries
	 * 
	 * @throws DynamicExtensionsSystemException 
	 * @throws DynamicExtensionsApplicationException 
	 */
	public List getCreateEntityQueryList(Entity entity, List reverseQueryList,
			HibernateDAO hibernateDAO, Stack rollbackQueryStack)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		List queryList = new ArrayList();
		//get query to create main table with primitive attributes.
		String mainTableQuery = getCreateMainTableQuery(entity, reverseQueryList);

		// get query to create associations ,it invloves altering source/taget table or creating 
		//middle table depending upon the cardinalities.
		List associationTableQueryList = getCreateAssociationsQueryList(entity, reverseQueryList,
				hibernateDAO, rollbackQueryStack);

		queryList.add(mainTableQuery);
		queryList.addAll(associationTableQueryList);
		return queryList;
	}

	/**
	 * This method is used to execute the data table queries for entity in case of editing the entity.
	 * This method takes each attribute of the entity and then scans for any changes and builds the alter query
	 * for each attribute for the entity.
	 * 
	 * @param entity Entity for which to generate and execute the alter queries.
	 * @param databaseCopy Old database copy of the entity.
	 * @param attributeRollbackQueryList rollback query list.
	 * @return Stack Stack holding the rollback queries in case of any exception
	 * 
	 * @throws DynamicExtensionsSystemException System exception in case of any fatal error
	 * @throws DynamicExtensionsApplicationException Thrown in case of authentication failure or duplicate name.
	 */
	public List getUpdateEntityQueryList(Entity entity, Entity databaseCopy,
			List attributeRollbackQueryList) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		Logger.out.debug("getUpdateEntityQueryList : Entering method");

		//get the query for any attribute that is modified.
		List updateAttributeQueryList = getUpdateAttributeQueryList(entity, databaseCopy,
				attributeRollbackQueryList);

		//get the query for any association that is modified.
		List updateassociationsQueryList = getUpdateAssociationsQueryList(entity, databaseCopy,
				attributeRollbackQueryList);

		List updateQueryList = new ArrayList();
		updateQueryList.addAll(updateAttributeQueryList);
		updateQueryList.addAll(updateassociationsQueryList);

		Logger.out.debug("getUpdateEntityQueryList Exiting method");
		return updateQueryList;
	}

	/**
	 * This method returns association value for the entity's given record.
	 * e.g if user1 is associated with study1 and study2. The method returns the 
	 * list of record ids of study1 and study2 as the return value for the association bet'n user and study
	 * 
	 * @param entity entity
	 * @param recordId recordId
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public Map<Association, List<Long>> getAssociationGetRecordQueryList(EntityInterface entity,
			Long recordId) throws DynamicExtensionsSystemException
	{

		Collection associationCollection = entity.getAssociationCollection();
		Iterator associationIterator = associationCollection.iterator();
		StringBuffer manyToOneAssociationsGetReocrdQuery = new StringBuffer();
		manyToOneAssociationsGetReocrdQuery.append(SELECT_KEYWORD + WHITESPACE);
		List<Association> manyToOneAssociationList = new ArrayList<Association>();
		String comma = "";

		Map<Association, List<Long>> associationValuesMap = new HashMap<Association, List<Long>>();

		while (associationIterator.hasNext())
		{
			Association association = (Association) associationIterator.next();

			String tableName = association.getConstraintProperties().getName();
			String sourceKey = association.getConstraintProperties().getSourceEntityKey();
			String targetKey = association.getConstraintProperties().getTargetEntityKey();
			StringBuffer query = new StringBuffer();

			if (sourceKey != null && targetKey != null && sourceKey.trim().length() != 0
					&& targetKey.trim().length() != 0)
			{ /* for Many to many get values from the middle table*/
				query.append(SELECT_KEYWORD + WHITESPACE + targetKey);
				query.append(WHITESPACE + FROM_KEYWORD + WHITESPACE + tableName + WHITESPACE);
				query
						.append(WHITESPACE + WHERE_KEYWORD + WHITESPACE + sourceKey + EQUAL
								+ recordId);
				associationValuesMap.put(association, getAssociationRecordValues(query.toString()));
			}
			else if (sourceKey != null && sourceKey.trim().length() != 0)
			{
				/* for all Many to one associations of a single entity create a single query to get values for the target 
				 * records. 
				 *  */
				if (manyToOneAssociationList.size() != 0)
				{
					manyToOneAssociationsGetReocrdQuery.append(COMMA);
				}
				manyToOneAssociationsGetReocrdQuery.append(WHITESPACE + sourceKey + WHITESPACE);
				manyToOneAssociationList.add(association);
			}
			else
			{
				/* for one to many or one to one association, get taget reocrd values from the target entity table.*/
				query.append(SELECT_KEYWORD + WHITESPACE + IDENTIFIER);
				query.append(WHITESPACE + FROM_KEYWORD + WHITESPACE + tableName + WHITESPACE);
				query
						.append(WHITESPACE + WHERE_KEYWORD + WHITESPACE + targetKey + EQUAL
								+ recordId);
				associationValuesMap.put(association, getAssociationRecordValues(query.toString()));
			}
		}

		manyToOneAssociationsGetReocrdQuery.append(WHITESPACE + FROM_KEYWORD + WHITESPACE
				+ entity.getTableProperties().getName() + WHITESPACE);
		manyToOneAssociationsGetReocrdQuery.append(WHITESPACE + WHERE_KEYWORD + WHITESPACE
				+ IDENTIFIER + EQUAL + recordId);

		int noOfMany2OneAsso = manyToOneAssociationList.size();
		if (noOfMany2OneAsso != 0)
		{
			try
			{
				ResultSet resultSet = entityManagerUtil
						.executeQuery(manyToOneAssociationsGetReocrdQuery.toString());
				for (int i = 0; i < noOfMany2OneAsso; i++)
				{
					Long targetRecordId = resultSet.getLong(i + 1);
					List<Long> valueList = new ArrayList<Long>();
					valueList.add(targetRecordId);
					associationValuesMap.put(manyToOneAssociationList.get(i), valueList);
				}
			}
			catch (SQLException e)
			{
				throw new DynamicExtensionsSystemException("Exception in query execution", e);
			}
		}
		return associationValuesMap;
	}

	/**
	 * returns the queries to insert data for the association.
	 * 
	 * @param associationInterface
	 * @param recordIdList
	 * @param sourceRecordId
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	public List<String> getAssociationInsertDataQuery(AssociationInterface associationInterface,
			List<Long> recordIdList, Long sourceRecordId)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		List<String> queryList = new ArrayList<String>();
		Association association = (Association) associationInterface;
		verifyCardinalityConstraints(associationInterface, recordIdList);
		String tableName = association.getConstraintProperties().getName();
		String sourceKey = association.getConstraintProperties().getSourceEntityKey();
		String targetKey = association.getConstraintProperties().getTargetEntityKey();
		StringBuffer query = new StringBuffer();
		Long id = entityManagerUtil.getNextIdentifier(tableName);
		if (sourceKey != null && targetKey != null && sourceKey.trim().length() != 0
				&& targetKey.trim().length() != 0)
		{
			//for many to many insert into middle table
			for (int i = 0; i < recordIdList.size(); i++)
			{
				query = new StringBuffer();
				query.append("INSERT INTO " + tableName + " ( ");
				query.append(IDENTIFIER + "," + sourceKey + "," + targetKey);
				query.append(" ) VALUES ( ");
				query.append(id.toString());
				query.append(COMMA);
				query.append(sourceRecordId.toString());
				query.append(COMMA);
				query.append(recordIdList.get(i));
				query.append(CLOSING_BRACKET);
				id++; //TODO this is not thread safe ,so needs to find a another solution.

				queryList.add(query.toString());
			}

		}
		else if (sourceKey != null && sourceKey.trim().length() != 0)
		{
			//many to one : update source entity table
			query.append(UPDATE_KEYWORD);
			query.append(WHITESPACE + tableName);
			query.append(WHITESPACE + SET_KEYWORD + WHITESPACE + sourceKey + EQUAL
					+ recordIdList.get(0) + WHITESPACE);
			query.append(WHERE_KEYWORD + WHITESPACE + IDENTIFIER + EQUAL + sourceRecordId);
			queryList.add(query.toString());

		}
		else
		{ //one to one && onr to many : update target entity table
			String recordIdString = recordIdList.toString();
			recordIdString = recordIdString.replace("[", OPENING_BRACKET);
			recordIdString = recordIdString.replace("]", CLOSING_BRACKET);

			query.append(UPDATE_KEYWORD);
			query.append(WHITESPACE + tableName);
			query.append(WHITESPACE + SET_KEYWORD + WHITESPACE + targetKey + EQUAL + sourceRecordId
					+ WHITESPACE);
			query.append(WHERE_KEYWORD + WHITESPACE + IDENTIFIER + WHITESPACE + IN_KEYWORD
					+ WHITESPACE + recordIdString);
			queryList.add(query.toString());
		}

		return queryList;
	}

	/**
	 *  returns the queries to remove the the association 
	 * @param association
	 * @param recordId
	 * @return
	 */
	public String getAssociationRemoveDataQuery(Association association, Long recordId)
	{
		String tableName = association.getConstraintProperties().getName();
		String sourceKey = association.getConstraintProperties().getSourceEntityKey();
		String targetKey = association.getConstraintProperties().getTargetEntityKey();
		StringBuffer query = new StringBuffer();

		if (association.getSourceRole().getAssociationsType().equals(AssociationType.CONTAINTMENT))
		{
			query.append(DELETE_KEYWORD);
			query.append(WHITESPACE + tableName + WHITESPACE);
			query.append(WHERE_KEYWORD + WHITESPACE + targetKey + EQUAL + recordId);
		}
		else
		{
			if (sourceKey != null && targetKey != null && sourceKey.trim().length() != 0
					&& targetKey.trim().length() != 0)
			{
				//for many to many delete all the records having reffered by this recordId  
				query.append(DELETE_KEYWORD + WHITESPACE + tableName + WHITESPACE + WHERE_KEYWORD
						+ WHITESPACE + sourceKey);
				query.append(WHITESPACE + EQUAL);
				query.append(recordId.toString());
			}
			else if (targetKey != null && targetKey.trim().length() != 0)
			{
				//for one to many and one to one: update  target entities records(set value in target column key = null) 
				//that are reffering to  this redord by setting it to null.
				query.append(UPDATE_KEYWORD);
				query.append(WHITESPACE + tableName);
				query.append(WHITESPACE + SET_KEYWORD + WHITESPACE + targetKey + EQUAL + WHITESPACE
						+ "null" + WHITESPACE);
				query.append(WHERE_KEYWORD + WHITESPACE + targetKey + EQUAL + recordId);
			}
		}
		return query.toString();
	}

	/**
	 * This method returns the main data table CREATE query that is associated with the entity.
	 *
	 * @param entity Entity for which to create the data table query.
	 * @param reverseQueryList Reverse query list which holds the query to negate the data table query.
	 * 
	 * @return String The method returns the "CREATE TABLE" query for the data table query for the entity passed.
	 * 
	 * @throws DynamicExtensionsSystemException 
	 */
	protected String getCreateMainTableQuery(Entity entity, List reverseQueryList)
			throws DynamicExtensionsSystemException
	{
		String dataType = getDataTypeForIdentifier();
		String tableName = entity.getTableProperties().getName();
		StringBuffer query = new StringBuffer(CREATE_TABLE + " " + tableName + " "
				+ OPENING_BRACKET + " " + IDENTIFIER + " " + dataType + COMMA);
		Collection attributeCollection = entity.getAttributeCollection();
		if (attributeCollection != null && !attributeCollection.isEmpty())
		{
			Iterator attributeIterator = attributeCollection.iterator();
			while (attributeIterator.hasNext())
			{
				Attribute attribute = (Attribute) attributeIterator.next();

				if (isAttributeColumnToBeExcluded(attribute))
				{
					//column is not created if it is multi select,file type etc.
					continue;
				}

				String type = "";
				//get column info for attribute
				String attributeQueryPart = getQueryPartForAttribute(attribute, type, true);
				query = query.append(attributeQueryPart);
				query = query.append(COMMA);
			}
		}
		query = query.append(PRIMARY_KEY_CONSTRAINT_FOR_ENTITY_DATA_TABLE + ")"); //identifier set as primary key

		String reverseQuery = getReverseQueryForEntityDataTable(entity);
		reverseQueryList.add(reverseQuery);

		return query.toString();
	}

	/**
	 * This method returns the dabase type for idenitifier.
	 * @return String database type for the identifier.
	 * @throws DynamicExtensionsSystemException exception is thrown if factory is not instanciated properly.
	 */
	protected String getDataTypeForIdentifier() throws DynamicExtensionsSystemException
	{
		DataTypeFactory dataTypeFactory = DataTypeFactory.getInstance();
		return dataTypeFactory.getDatabaseDataType("Integer");
	}

	/**
	 * This method returns true if a column in not to be created for the attribute.
	 * @return
	 */
	protected boolean isAttributeColumnToBeExcluded(AttributeInterface attribute)
	{
		boolean isExclude = false;

		if (attribute.getIsCollection() != null && attribute.getIsCollection())
		{
			isExclude = true;
		}
		else if (attribute.getAttributeTypeInformation() instanceof FileAttributeTypeInformation)
		{
			isExclude = true;
		}

		return isExclude;
	}

	/**
	 * This method builds the query part for the primitive attribute 
	 * @param attribute primitive attribute for which to build the query.
	 * @return String query part of the primitive attribute.
	 * @throws DataTypeFactoryInitializationException 
	 */
	protected String getQueryPartForAttribute(Attribute attribute, String type,
			boolean processConstraints) throws DynamicExtensionsSystemException
	{

		String attributeQuery = null;
		if (attribute != null)
		{
			String columnName = attribute.getColumnProperties().getName();
			String isUnique = "";
			String nullConstraint = "";
			String defaultConstraint = "";
			if (processConstraints)
			{
				if (attribute.getIsPrimaryKey())
				{
					isUnique = CONSTRAINT_KEYWORD + WHITESPACE
							+ attribute.getColumnProperties().getName() + UNDERSCORE
							+ UNIQUE_CONSTRAINT_SUFFIX + WHITESPACE + UNIQUE_KEYWORD;
				}
				nullConstraint = "NULL";

				if (!attribute.getIsNullable())
				{
					nullConstraint = "NOT NULL";
				}

				if (attribute.getAttributeTypeInformation().getDefaultValue() != null
						&& attribute.getAttributeTypeInformation().getDefaultValue()
								.getValueAsObject() != null)
				{
					defaultConstraint = DEFAULT_KEYWORD
							+ WHITESPACE
							+ EntityManagerUtil.getFormattedValue(attribute, attribute
									.getAttributeTypeInformation().getDefaultValue()
									.getValueAsObject());
				}

			}

			attributeQuery = columnName + WHITESPACE + type + WHITESPACE
					+ getDatabaseTypeAndSize(attribute) + WHITESPACE + defaultConstraint
					+ WHITESPACE + isUnique + WHITESPACE + nullConstraint;
		}
		return attributeQuery;
	}

	/**
	 * This method returns the database type and size of the attribute passed to it which becomes the part of the query for that attribute.
	 * @param attribute Attribute object for which to get the database type and size.
	 * @return String that specifies the data base type and size.
	 * @throws DynamicExtensionsSystemException 
	 * @throws DataTypeFactoryInitializationException 
	 */
	protected String getDatabaseTypeAndSize(Attribute attribute)
			throws DynamicExtensionsSystemException

	{
		try
		{
			DataTypeFactory dataTypeFactory = DataTypeFactory.getInstance();
			AttributeTypeInformationInterface attributeInformation = attribute
					.getAttributeTypeInformation();
			if (attributeInformation instanceof StringAttributeTypeInformation)
			{
				return dataTypeFactory.getDatabaseDataType("String");
			}
			else if (attributeInformation instanceof IntegerAttributeTypeInformation)
			{
				return dataTypeFactory.getDatabaseDataType("Integer");
			}
			else if (attributeInformation instanceof DateAttributeTypeInformation)
			{
				return dataTypeFactory.getDatabaseDataType("Date");
			}
			else if (attributeInformation instanceof FloatAttributeTypeInformation)
			{
				return dataTypeFactory.getDatabaseDataType("Float");
			}
			else if (attributeInformation instanceof BooleanAttributeTypeInformation)
			{
				return dataTypeFactory.getDatabaseDataType("Boolean");
			}
			else if (attributeInformation instanceof DoubleAttributeTypeInformation)
			{
				return dataTypeFactory.getDatabaseDataType("Double");
			}
			else if (attributeInformation instanceof LongAttributeTypeInformation)
			{
				return dataTypeFactory.getDatabaseDataType("Long");
			}
			else if (attributeInformation instanceof ShortAttributeTypeInformation)
			{
				return dataTypeFactory.getDatabaseDataType("Short");
			}

		}
		catch (DataTypeFactoryInitializationException e)
		{
			throw new DynamicExtensionsSystemException("Could Not get data type attribute", e);
		}

		return null;
	}

	/**
	 * This method gives the opposite query to negate the effect of "CREATE TABLE" query for the data table for the entity. 
	 * @param entity Entity for which query generation is done.
	 * @return String query that basically holds the "DROP TABLE" query.
	 */
	protected String getReverseQueryForEntityDataTable(Entity entity)
	{
		String query = null;
		if (entity != null && entity.getTableProperties() != null)
		{
			query = "Drop table" + " " + entity.getTableProperties().getName();
		}
		return query;
	}

	/**
	 * This method returns all the CREATE table entries for associations present in the entity.
	 * @param entity Entity object from which to get the associations.
	 * @param reverseQueryList Reverse query list that holds the reverse queries.
	 * @param rollbackQueryStack 
	 * @param hibernateDAO 
	 * @return List of queries
	 * @throws DynamicExtensionsSystemException 
	 * @throws DynamicExtensionsApplicationException 
	 */
	protected List getCreateAssociationsQueryList(Entity entity, List reverseQueryList,
			HibernateDAO hibernateDAO, Stack rollbackQueryStack)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		List associationQueryList = new ArrayList();
		Collection associationCollection = entity.getAssociationCollection();
		if (associationCollection != null && !associationCollection.isEmpty())
		{
			Iterator associationIterator = associationCollection.iterator();
			while (associationIterator.hasNext())
			{
				AssociationInterface association = (AssociationInterface) associationIterator
						.next();
				if (((Association) association).getIsSystemGenerated())
				{ //no need to process system generated association 
					continue;
				}
				boolean isAddAssociationQuery = true;
				String associationQuery = getQueryPartForAssociation(association, reverseQueryList,
						isAddAssociationQuery);
				associationQueryList.add(associationQuery);
			}
		}
		return associationQueryList;
	}

	/**
	 * This method builds the query part for the association.
	 * 
	 * @param association Association object for which to build the query.
	 * @param reverseQueryList rollback query list
	 * @param isAddAssociationQuery boolean indicating whether to create query for 
	 *        add association or remove association.
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	protected String getQueryPartForAssociation(AssociationInterface association,
			List reverseQueryList, boolean isAddAssociationQuery)
			throws DynamicExtensionsSystemException
	{
		Logger.out.debug("getQueryPartForAssociation Entering method");

		StringBuffer query = new StringBuffer();
		EntityInterface sourceEntity = association.getEntity();
		EntityInterface targetEntity = association.getTargetEntity();
		RoleInterface sourceRole = association.getSourceRole();
		RoleInterface targetRole = association.getTargetRole();
		Cardinality sourceMaxCardinality = sourceRole.getMaximumCardinality();
		Cardinality targetMaxCardinality = targetRole.getMaximumCardinality();
		ConstraintPropertiesInterface constraintProperties = association.getConstraintProperties();
		String tableName = "";

		String dataType = getDataTypeForIdentifier();
		if (sourceMaxCardinality == Cardinality.MANY && targetMaxCardinality == Cardinality.MANY)
		{
			//for many-many a middle table is created.
			tableName = constraintProperties.getName();

			query.append(CREATE_TABLE + WHITESPACE + tableName + WHITESPACE + OPENING_BRACKET
					+ WHITESPACE + IDENTIFIER + WHITESPACE + dataType + COMMA);
			query.append(constraintProperties.getSourceEntityKey() + WHITESPACE + dataType + COMMA);
			query.append(constraintProperties.getTargetEntityKey() + WHITESPACE + dataType + COMMA
					+ WHITESPACE);
			query.append(PRIMARY_KEY_CONSTRAINT_FOR_ENTITY_DATA_TABLE + CLOSING_BRACKET);
			String rollbackQuery = DROP_KEYWORD + WHITESPACE + TABLE_KEYWORD + WHITESPACE
					+ tableName;

			if (isAddAssociationQuery)
			{
				reverseQueryList.add(rollbackQuery);
			}
			else
			{
				reverseQueryList.add(query.toString());
				query = new StringBuffer(rollbackQuery);
			}
		}
		else if (sourceMaxCardinality == Cardinality.MANY
				&& targetMaxCardinality == Cardinality.ONE)
		{
			//for many to one, a column is added into source entity table.
			tableName = sourceEntity.getTableProperties().getName();
			String columnName = constraintProperties.getSourceEntityKey();
			query.append(getAddAttributeQuery(tableName, columnName, dataType, reverseQueryList,
					isAddAssociationQuery));
		}
		else
		{
			//for one to one and one to many, a column is added into target entity table.
			tableName = targetEntity.getTableProperties().getName();
			String columnName = constraintProperties.getTargetEntityKey();
			query.append(getAddAttributeQuery(tableName, columnName, dataType, reverseQueryList,
					isAddAssociationQuery));

		}

		Logger.out.debug("getQueryPartForAssociation exiting method");
		return query.toString();
	}

	protected String getAddAttributeQuery(String tableName, String columnName, String dataType,
			List reverseQueryList, boolean isAddAssociationQuery)
	{
		StringBuffer query = new StringBuffer();
		query.append(ALTER_TABLE + WHITESPACE + tableName + WHITESPACE + ADD_KEYWORD + WHITESPACE);
		query.append(columnName + WHITESPACE + dataType + WHITESPACE);
		String rollbackQuery = ALTER_TABLE + WHITESPACE + tableName + WHITESPACE + DROP_KEYWORD
				+ WHITESPACE + COLUMN_KEYWORD + WHITESPACE + columnName;

		//		query.append(REFERENCES_KEYWORD + WHITESPACE + sourceEntity.getTableProperties().getName() + OPENING_BRACKET + IDENTIFIER + CLOSING_BRACKET + COMMA);

		if (isAddAssociationQuery)
		{
			reverseQueryList.add(rollbackQuery);
			return query.toString();
		}
		else
		{
			reverseQueryList.add(query.toString());
			return rollbackQuery;
		}
	}

	/**
	 * returns queries for any attribute that is modified.
	 * @param entity entity
	 * @param databaseCopy its database copy to compare with
	 * @param attributeRollbackQueryList rollback query list
	 * @return query list
	 * 
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	protected List getUpdateAttributeQueryList(Entity entity, Entity databaseCopy,
			List attributeRollbackQueryList) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		Logger.out.debug("getUpdateAttributeQueryList Entering method");
		Collection attributeCollection = entity.getAttributeCollection();
		List attributeQueryList = new ArrayList();

		if (attributeCollection != null && !attributeCollection.isEmpty())
		{
			Iterator attributeIterator = attributeCollection.iterator();

			while (attributeIterator.hasNext())
			{
				Attribute attribute = (Attribute) attributeIterator.next();
				Attribute savedAttribute = (Attribute) databaseCopy
						.getAttributeByIdentifier(attribute.getId());

				if (savedAttribute == null || isAttributeColumnToBeAdded(attribute, savedAttribute))
				{
					//either attribute is newly added or previously excluded(file type/multiselect) attribute
					//modified such that now its column needs to add.
					String attributeQuery = processAddAttribute(attribute,
							attributeRollbackQueryList);
					attributeQueryList.add(attributeQuery);
				}
				else
				{
					//check for other modification in the attributes such a unique constriant change.
					List modifiedAttributeQueryList = processModifyAttribute(attribute,
							savedAttribute, attributeRollbackQueryList);
					attributeQueryList.addAll(modifiedAttributeQueryList);
				}

			}

		}

		processRemovedAttributes(entity, databaseCopy, attributeQueryList,
				attributeRollbackQueryList);

		Logger.out.debug("getUpdateAttributeQueryList Exiting method");
		return attributeQueryList;
	}

	/**
	 * @param entity
	 * @param databaseCopy
	 * @param attributeRollbackQueryList
	 * @return
	 */
	protected List getUpdateAssociationsQueryList(Entity entity, Entity databaseCopy,
			List attributeRollbackQueryList) throws DynamicExtensionsSystemException
	{
		Logger.out.debug("getUpdateAssociationsQueryList Entering method");
		List associationsQueryList = new ArrayList();
		boolean isAddAssociationQuery = true;

		Collection associationCollection = entity.getAssociationCollection();

		if (associationCollection != null && !associationCollection.isEmpty())
		{
			Iterator associationIterator = associationCollection.iterator();

			while (associationIterator.hasNext())
			{
				Association association = (Association) associationIterator.next();
				Association associationDatabaseCopy = (Association) databaseCopy
						.getAttributeByIdentifier(association.getId());

				if (association.getIsSystemGenerated())
				{
					continue;
				}
				if (associationDatabaseCopy == null)
				{
					isAddAssociationQuery = true;
					String newAssociationQuery = getQueryPartForAssociation(association,
							attributeRollbackQueryList, isAddAssociationQuery);
					associationsQueryList.add(newAssociationQuery);
				}
				else
				{
					if (isCardinalityChanged(association, associationDatabaseCopy))
					{
						isAddAssociationQuery = false;
						String savedAssociationRemoveQuery = getQueryPartForAssociation(
								associationDatabaseCopy, attributeRollbackQueryList,
								isAddAssociationQuery);
						associationsQueryList.add(savedAssociationRemoveQuery);

						isAddAssociationQuery = true;
						String newAssociationAddQuery = getQueryPartForAssociation(association,
								attributeRollbackQueryList, isAddAssociationQuery);
						associationsQueryList.add(newAssociationAddQuery);
					}
				}
			}
		}
		processRemovedAssociation(entity, databaseCopy, associationsQueryList,
				attributeRollbackQueryList);

		Logger.out.debug("getUpdateAssociationsQueryList Exiting method");
		return associationsQueryList;
	}

	/**
	 * This method processes all the attributes that previoulsy saved but removed by editing.
	 * @param entity entity
	 * @param databaseCopy databaseCopy
	 * @param attributeQueryList attributeQueryList
	 * @param attributeRollbackQueryList attributeRollbackQueryList
	 * @throws DynamicExtensionsSystemException 
	 */
	protected void processRemovedAttributes(Entity entity, Entity databaseCopy,
			List attributeQueryList, List attributeRollbackQueryList)
			throws DynamicExtensionsSystemException
	{
		Collection savedAttributeCollection = databaseCopy.getAttributeCollection();
		String tableName = entity.getTableProperties().getName();

		if (savedAttributeCollection != null && !savedAttributeCollection.isEmpty())
		{
			Iterator savedAttributeIterator = savedAttributeCollection.iterator();
			while (savedAttributeIterator.hasNext())
			{

				Attribute savedAttribute = (Attribute) savedAttributeIterator.next();
				Attribute attribute = (Attribute) entity.getAttributeByIdentifier(savedAttribute
						.getId());;

				//attribute is removed or modified such that its column need to be removed
				if (attribute == null || isAttributeColumnToBeRemoved(attribute, savedAttribute))
				{
					String columnName = savedAttribute.getColumnProperties().getName();

					String removeAttributeQuery = ALTER_TABLE + WHITESPACE + tableName + WHITESPACE
							+ DROP_KEYWORD + WHITESPACE + COLUMN_KEYWORD + WHITESPACE + columnName;
					String type = "";

					String removeAttributeQueryRollBackQuery = ALTER_TABLE + WHITESPACE + tableName
							+ WHITESPACE + ADD_KEYWORD + WHITESPACE
							+ getQueryPartForAttribute(savedAttribute, type, true);

					attributeQueryList.add(removeAttributeQuery);
					attributeRollbackQueryList.add(removeAttributeQueryRollBackQuery);
				}
			}
		}
	}

	/**
	 * This method returns true if a attribute is changed such that its column needs to be removed.
	 * @param attribute attribute
	 * @param dataBaseCopy dataBaseCopy of the  attribute
	 * @return true if its column to be removed
	 */
	protected boolean isAttributeColumnToBeRemoved(AttributeInterface attribute,
			AttributeInterface dataBaseCopy)
	{
		boolean columnRemoved = false;

		if (attribute.getIsCollection() && !dataBaseCopy.getIsCollection())
		{
			columnRemoved = true;
		}
		else
		{

			AttributeTypeInformationInterface attributeTypeInfo = attribute
					.getAttributeTypeInformation();
			AttributeTypeInformationInterface attributeTypeInfoDatabaseCopy = dataBaseCopy
					.getAttributeTypeInformation();

			if ((attributeTypeInfo instanceof FileAttributeTypeInformation)
					&& !(attributeTypeInfoDatabaseCopy instanceof FileAttributeTypeInformation))
			{
				columnRemoved = true;
			}
		}

		return columnRemoved;
	}

	/**
	 * @param association
	 * @param associationDatabaseCopy
	 * @return
	 */
	protected boolean isCardinalityChanged(Association association,
			Association associationDatabaseCopy)
	{
		Cardinality sourceMaxCardinality = association.getSourceRole().getMaximumCardinality();
		Cardinality targetMaxCardinality = association.getTargetRole().getMaximumCardinality();

		Cardinality sourceMaxCardinalityDatabaseCopy = associationDatabaseCopy.getSourceRole()
				.getMaximumCardinality();
		Cardinality targetMaxCardinalityDatabaseCopy = associationDatabaseCopy.getTargetRole()
				.getMaximumCardinality();

		if (!sourceMaxCardinality.equals(sourceMaxCardinalityDatabaseCopy)
				|| !targetMaxCardinality.equals(targetMaxCardinalityDatabaseCopy))
		{
			return true;
		}
		return false;
	}

	/**
	 * This method processes any associations that are deleted from the entity.
	 * @param entity
	 * @param databaseCopy
	 * @param associationsQueryList
	 * @param attributeRollbackQueryList
	 * @throws DynamicExtensionsSystemException 
	 */
	protected void processRemovedAssociation(Entity entity, Entity databaseCopy,
			List associationsQueryList, List attributeRollbackQueryList)
			throws DynamicExtensionsSystemException
	{
		Logger.out.debug("processRemovedAssociation Entering method");

		Collection savedAssociationCollection = databaseCopy.getAssociationCollection();
		String tableName = entity.getTableProperties().getName();

		if (savedAssociationCollection != null && !savedAssociationCollection.isEmpty())
		{
			Iterator savedAssociationIterator = savedAssociationCollection.iterator();
			while (savedAssociationIterator.hasNext())
			{
				Association savedAssociation = (Association) savedAssociationIterator.next();
				Association association = (Association) entity
						.getAttributeByIdentifier(savedAssociation.getId());;

				// removed ??
				if (association == null)
				{
					boolean isAddAssociationQuery = false;
					String removeAssociationQuery = getQueryPartForAssociation(savedAssociation,
							attributeRollbackQueryList, isAddAssociationQuery);
					associationsQueryList.add(removeAssociationQuery);
				}
			}
		}
		Logger.out.debug("processRemovedAssociation Exiting method");
	}

	/**
	 * This method returns true if a attribute is changed such that its column needs to be added.
	 * 
	 * @param attribute attribute
	 * @param dataBaseCopy dataBaseCopy
	 * @return true is column needs to be added.
	 */
	protected boolean isAttributeColumnToBeAdded(AttributeInterface attribute,
			AttributeInterface dataBaseCopy)
	{
		boolean columnAdd = false;

		if (!attribute.getIsCollection() && dataBaseCopy.getIsCollection())
		{
			//previously multi select, now other.
			columnAdd = true;
		}
		else
		{
			// //previously file attribute select, now other.
			AttributeTypeInformationInterface attributeTypeInfo = attribute
					.getAttributeTypeInformation();
			AttributeTypeInformationInterface attributeTypeInfoDatabaseCopy = dataBaseCopy
					.getAttributeTypeInformation();

			if (!(attributeTypeInfo instanceof FileAttributeTypeInformation)
					&& (attributeTypeInfoDatabaseCopy instanceof FileAttributeTypeInformation))
			{
				columnAdd = true;
			}
		}

		return columnAdd;
	}

	/**
	 * This method takes the edited attribtue and its database copy and then looks for any change
	 * Changes that are tracked in terms of data table query are 
	 * Change in the constraint NOT NULL AND UNIQUE
	 * <BR> Change in the database type of the column.
	 * @param attribute edited Attribute 
	 * @param savedAttribute original database copy of the edited attribute.
	 * @param attributeRollbackQueryList This list is updated with the roll back queries for the actual queries.
	 * @return List list of strings which hold the queries for the changed attribute.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	protected List processModifyAttribute(Attribute attribute, Attribute savedAttribute,
			List attributeRollbackQueryList) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		List modifyAttributeQueryList = new ArrayList();

		if (isAttributeColumnToBeExcluded(attribute))
		{
			return modifyAttributeQueryList;
		}

		String tableName = attribute.getEntity().getTableProperties().getName();
		String columnName = attribute.getColumnProperties().getName();

		String newTypeClass = attribute.getAttributeTypeInformation().getClass().getName();
		String oldTypeClass = savedAttribute.getAttributeTypeInformation().getClass().getName();

		if (!newTypeClass.equals(oldTypeClass))
		{
			modifyAttributeQueryList = getAttributeDataTypeChangedQuery(attribute, savedAttribute,
					attributeRollbackQueryList);
			modifyAttributeQueryList.addAll(modifyAttributeQueryList);
		}

		if (attribute.getIsPrimaryKey() && !savedAttribute.getIsPrimaryKey())
		{

			String uniqueConstraintQuery = ALTER_TABLE + WHITESPACE + tableName + WHITESPACE
					+ ADD_KEYWORD + WHITESPACE + CONSTRAINT_KEYWORD + WHITESPACE + columnName
					+ UNDERSCORE + UNIQUE_CONSTRAINT_SUFFIX + WHITESPACE + UNIQUE_KEYWORD
					+ WHITESPACE + OPENING_BRACKET + columnName + CLOSING_BRACKET;
			String uniqueConstraintRollbackQuery = ALTER_TABLE + WHITESPACE + tableName
					+ WHITESPACE + DROP_KEYWORD + WHITESPACE + CONSTRAINT_KEYWORD + WHITESPACE
					+ columnName + UNDERSCORE + UNIQUE_CONSTRAINT_SUFFIX;

			modifyAttributeQueryList.add(uniqueConstraintQuery);
			attributeRollbackQueryList.add(uniqueConstraintRollbackQuery);

		}
		else if (!attribute.getIsPrimaryKey() && savedAttribute.getIsPrimaryKey())
		{
			String uniqueConstraintQuery = ALTER_TABLE + WHITESPACE + tableName + WHITESPACE
					+ DROP_KEYWORD + WHITESPACE + CONSTRAINT_KEYWORD + WHITESPACE + columnName
					+ UNDERSCORE + UNIQUE_CONSTRAINT_SUFFIX;
			String uniqueConstraintRollbackQuery = ALTER_TABLE + WHITESPACE + tableName
					+ WHITESPACE + ADD_KEYWORD + WHITESPACE + CONSTRAINT_KEYWORD + WHITESPACE
					+ columnName + UNDERSCORE + UNIQUE_CONSTRAINT_SUFFIX + WHITESPACE
					+ UNIQUE_KEYWORD + WHITESPACE + OPENING_BRACKET + columnName + CLOSING_BRACKET;

			modifyAttributeQueryList.add(uniqueConstraintQuery);
			attributeRollbackQueryList.add(uniqueConstraintRollbackQuery);
		}

		return modifyAttributeQueryList;
	}

	/**
	 * This method returns the query for the attribute to modify its data type.
	 * @param attribute
	 * @param savedAttribute
	 * @param modifyAttributeRollbackQuery
	 * @param tableName
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	protected List getAttributeDataTypeChangedQuery(Attribute attribute, Attribute savedAttribute,
			List modifyAttributeRollbackQueryList) throws DynamicExtensionsSystemException
	{
		String tableName = attribute.getEntity().getTableProperties().getName();
		String type = "";
		String modifyAttributeRollbackQuery = "";

		String modifyAttributeQuery = getQueryPartForAttribute(attribute, type, false);
		modifyAttributeQuery = ALTER_TABLE + WHITESPACE + tableName + WHITESPACE + MODIFY_KEYWORD
				+ WHITESPACE + modifyAttributeQuery;

		modifyAttributeRollbackQuery = getQueryPartForAttribute(savedAttribute, type, false);
		modifyAttributeRollbackQuery = ALTER_TABLE + WHITESPACE + tableName + WHITESPACE
				+ MODIFY_KEYWORD + WHITESPACE + modifyAttributeRollbackQuery;

		String nullQueryKeyword = "";
		String nullQueryRollbackKeyword = "";

		if (attribute.getIsNullable() && !savedAttribute.getIsNullable())
		{
			nullQueryKeyword = WHITESPACE + NULL_KEYWORD + WHITESPACE;
			nullQueryRollbackKeyword = WHITESPACE + NOT_KEYWORD + WHITESPACE + NULL_KEYWORD
					+ WHITESPACE;
		}
		else if (!attribute.getIsNullable() && savedAttribute.getIsNullable())
		{
			nullQueryKeyword = WHITESPACE + NOT_KEYWORD + WHITESPACE + NULL_KEYWORD + WHITESPACE;
			nullQueryRollbackKeyword = WHITESPACE + NULL_KEYWORD + WHITESPACE;

		}

		modifyAttributeQuery = modifyAttributeQuery + nullQueryKeyword;
		modifyAttributeRollbackQuery = modifyAttributeRollbackQuery + nullQueryRollbackKeyword;
		modifyAttributeRollbackQueryList.add(modifyAttributeRollbackQuery);

		List modifyAttributeQueryList = new ArrayList();
		modifyAttributeQueryList.add(modifyAttributeQuery);

		return modifyAttributeQueryList;
	}

	/**
	 * This method builds the query part for the newly added attribute.
	 * @param attribute Newly added attribute in the entity.
	 * @param attributeRollbackQueryList This list is updated with the rollback queries for the actual queries.
	 * @return Srting The actual query part for the new attribute.
	 * @throws DynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException
	 */
	protected String processAddAttribute(Attribute attribute, List attributeRollbackQueryList)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{

		String columnName = attribute.getColumnProperties().getName();
		String tableName = attribute.getEntity().getTableProperties().getName();
		String type = "";
		String newAttributeQuery = ALTER_TABLE + WHITESPACE + tableName + WHITESPACE + ADD_KEYWORD
				+ WHITESPACE + getQueryPartForAttribute(attribute, type, true);

		String newAttributeRollbackQuery = ALTER_TABLE + WHITESPACE + tableName + WHITESPACE
				+ DROP_KEYWORD + WHITESPACE + COLUMN_KEYWORD + WHITESPACE + columnName;

		attributeRollbackQueryList.add(newAttributeRollbackQuery);

		return newAttributeQuery;
	}

	/**
	 * This method executes the queries which generate and or manipulate the data table associated with the entity.
	 * @param entity Entity for which the data table queries are to be executed.
	 * @param rollbackQueryStack 
	 * @param reverseQueryList2 
	 * @param queryList2 
	 * @param hibernateDAO 
	 * @param session Hibernate Session through which connection is obtained to fire the queries.
	 * @throws DynamicExtensionsSystemException Whenever there is any exception , this exception is thrown with proper message and the exception is 
	 * wrapped inside this exception.
	 */
	public Stack executeQueries(List queryList, List reverseQueryList, Stack rollbackQueryStack)
			throws DynamicExtensionsSystemException
	{
		Session session = null;
		try
		{
			session = DBUtil.currentSession();
		}
		catch (HibernateException e1)
		{
			throw new DynamicExtensionsSystemException(
					"Unable to exectute the data table queries .....Cannot access sesssion", e1,
					DYEXTN_S_002);
		}

		Iterator reverseQueryListIterator = reverseQueryList.iterator();

		try
		{
			Connection conn = session.connection();
			if (queryList != null && !queryList.isEmpty())
			{
				Iterator queryListIterator = queryList.iterator();
				while (queryListIterator.hasNext())
				{
					String query = (String) queryListIterator.next();
					System.out.println("Query: " + query);
					PreparedStatement statement = null;
					try
					{
						statement = conn.prepareStatement(query);
					}
					catch (SQLException e)
					{
						throw new DynamicExtensionsSystemException(
								"Exception occured while executing the data table query", e);
					}
					try
					{
						statement.executeUpdate();
						if (reverseQueryListIterator.hasNext())
						{
							rollbackQueryStack.push(reverseQueryListIterator.next());
						}
					}
					catch (SQLException e)
					{
						//                        rollbackQueries(rollbackQueryStack, conn, entity);
						throw new DynamicExtensionsSystemException(
								"Exception occured while forming the data tables for entity", e,
								DYEXTN_S_002);
					}
				}
			}
		}
		catch (HibernateException e)
		{
			throw new DynamicExtensionsSystemException(
					"Cannot obtain connection to execute the data query", e, DYEXTN_S_001);
		}

		return rollbackQueryStack;

	}

	/**
	 * This method excute the query that selects record ids of the target entity that are associated
	 * to the source entity for a given association.
	 * @param query
	 * @return List of reocrd ids of the target entity .
	 * @throws DynamicExtensionsSystemException
	 */
	protected List<Long> getAssociationRecordValues(String query)
			throws DynamicExtensionsSystemException
	{
		List<Long> associationRecordValues = new ArrayList();
		try
		{
			ResultSet resultSet = entityManagerUtil.executeQuery(query);
			do
			{
				Long recordId = resultSet.getLong(1);
				associationRecordValues.add(recordId);
			}
			while (resultSet.next());
		}
		catch (Exception e)
		{
			throw new DynamicExtensionsSystemException("Exception in query execution", e);
		}
		return associationRecordValues;
	}

	/**
	 * This method make sure the cardinality constaints are properly 
	 * followed.
	 * e.g 
	 * 1. For One to One association,it checks if target entity's record id is not associated to any other
	 * source entity.
	 *  
	 * @param association for which cardinality to be tested.
	 * @param recordIdList recordIdList (for one to one, it will contain only one entry).
	 * 
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	protected void verifyCardinalityConstraints(AssociationInterface association,
			List<Long> recordIdList) throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException
	{
		EntityInterface targetEntity = association.getTargetEntity();
		Cardinality sourceMaxCardinality = association.getSourceRole().getMaximumCardinality();
		Cardinality targetMaxCardinality = association.getTargetRole().getMaximumCardinality();

		String columnName = "";
		String tableName = "";

		if (targetMaxCardinality == Cardinality.ONE && sourceMaxCardinality == Cardinality.ONE)
		{

			tableName = targetEntity.getTableProperties().getName();
			columnName = association.getConstraintProperties().getTargetEntityKey();

			String query = SELECT_KEYWORD + WHITESPACE + COUNT_KEYWORD + OPENING_BRACKET + "*"
					+ CLOSING_BRACKET + WHITESPACE + FROM_KEYWORD + WHITESPACE + tableName
					+ WHITESPACE + WHERE_KEYWORD + WHITESPACE + columnName + WHITESPACE + EQUAL
					+ WHITESPACE + recordIdList.get(0);
			ResultSet resultSet = entityManagerUtil.executeQuery(query);
			try
			{
				// if another source record is already using target record , throw exception.
				if (resultSet.getInt(1) != 0)
				{
					throw new DynamicExtensionsApplicationException(
							"Cardinality constraint violated", null, DYEXTN_A_005);
				}
			}
			catch (SQLException e)
			{
				throw new DynamicExtensionsSystemException(e.getMessage(), e);
			}

		}
	}

}
