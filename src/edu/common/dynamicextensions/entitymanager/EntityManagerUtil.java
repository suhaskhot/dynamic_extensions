
package edu.common.dynamicextensions.entitymanager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domain.AbstractAttribute;
import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.domain.DateAttributeTypeInformation;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.Constants;
import edu.common.dynamicextensions.util.global.Variables;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.logger.Logger;

public class EntityManagerUtil implements DynamicExtensionsQueryBuilderConstantsInterface
{

	/**
	 * 
	 * @param attribute
	 * @param value
	 * @return
	 */
	public static String getFormattedValue(AbstractAttribute attribute, Object value)
	{
		String formattedvalue = null;
		AttributeTypeInformationInterface attributeInformation = ((Attribute) attribute)
				.getAttributeTypeInformation();
		if (attribute == null)
		{
			formattedvalue = null;
		}

		else if (attributeInformation instanceof StringAttributeTypeInformation)
		{
			formattedvalue = "'" + value + "'";
		}
		else if (attributeInformation instanceof DateAttributeTypeInformation)
		{
			String dateFormat = ((DateAttributeTypeInformation) attributeInformation).getFormat();
			if (dateFormat == null)
			{
				dateFormat = Constants.DATE_PATTERN_MM_DD_YYYY;
			}

			String str = null;
			if (value instanceof Date)
			{
				str = Utility.parseDateToString(((Date) value), dateFormat);
			}
			else
			{
				str = (String) value;
			}

			/*formattedvalue = Variables.strTodateFunction + "('" + str + "','"
			 + variables.datepattern + "')";*/
			formattedvalue = Variables.strTodateFunction + "('" + str + "','"
					+ DynamicExtensionsUtility.getSQLDateFormat(dateFormat) + "')";
		}
		else
		{
			formattedvalue = value.toString();
		}
		Logger.out.debug("getFormattedValue The formatted value for attribute "
				+ attribute.getName() + "is " + formattedvalue);
		return formattedvalue;
	}

	/**
	 * @param query query to be executed
	 * @return 
	 * @throws DynamicExtensionsSystemException 
	 */
	public static ResultSet executeQuery(String query) throws DynamicExtensionsSystemException
	{
		//System.out.println("[DE_QUERY] : " + query);

		Connection conn = null;
		try
		{
			conn = DBUtil.getConnection();
			Statement statement = null;
			statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			return resultSet;
		}

		catch (Exception e)
		{
			try
			{
				conn.rollback();
			}
			catch (SQLException e1)
			{
				throw new DynamicExtensionsSystemException(e.getMessage(), e);
			}
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}

	}

	/**
	 * @param inputList
	 * @return
	 */
	public static String getListToString(List inputList)
	{

		String queryString = inputList.toString();
		queryString = queryString.replace("[", OPENING_BRACKET);
		queryString = queryString.replace("]", CLOSING_BRACKET);

		return queryString;
	}

	/**
	 * @param tableName
	 * @param columnName
	 * @param columnValueCondition
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public static int getNoOfRecord(String query) throws DynamicExtensionsSystemException
	{
		//		StringBuffer query = new StringBuffer();
		//		query.append(SELECT_KEYWORD + COUNT_KEYWORD + "(*)");
		//		query.append(FROM_KEYWORD + tableName);
		//		query.append(WHERE_KEYWORD + columnName + IN_KEYWORD + getListToString(recordIdList));
		//		query.append(" and " +  DynamicExtensionBaseQueryBuilder.getRemoveDisbledRecordsQuery());

		ResultSet resultSet = null;
		try
		{
			resultSet = executeQuery(query);
			resultSet.next();
			return resultSet.getInt(1);
		}
		catch (SQLException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
		finally
		{
			if (resultSet != null)
			{
				try
				{

					resultSet.close();

				}
				catch (SQLException e)
				{
					throw new DynamicExtensionsSystemException(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * @param query query to be executed
	 * @return 
	 * @throws DynamicExtensionsSystemException 
	 */
	public int executeDML(String query) throws DynamicExtensionsSystemException
	{
		System.out.println(query);
		Connection conn = null;
		try
		{
			conn = DBUtil.getConnection();
			Statement statement = null;
			statement = conn.createStatement();
			return statement.executeUpdate(query);
		}
		catch (Exception e)
		{
			try
			{
				conn.rollback();
			}
			catch (SQLException e1)
			{
				throw new DynamicExtensionsSystemException(e.getMessage(), e);
			}
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
	}

	/**
	 * @param queryList
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	public int executeDML(List<String> queryList) throws DynamicExtensionsSystemException
	{
		int result = -1;
		for (String query : queryList)
		{
			result = executeDML(query);
		}
		return result;
	}

	/**
	 * Method generates the next identifier for the table that stores the value of the passes entity.
	 * @param entity
	 * @return
	 * @throws DAOException
	 * @throws ClassNotFoundException
	 */
	synchronized public Long getNextIdentifier(String entityTableName)
			throws DynamicExtensionsSystemException
	{

		StringBuffer queryToGetNextIdentifier = new StringBuffer("SELECT MAX(IDENTIFIER) FROM "
				+ entityTableName);
		try
		{
			ResultSet resultSet = executeQuery(queryToGetNextIdentifier.toString());
			resultSet.next();
			Long identifier = resultSet.getLong(1);
			return identifier + 1;
		}
		catch (SQLException e)
		{
			throw new DynamicExtensionsSystemException(
					"Could not fetch the next identifier for table " + entityTableName);
		}
	}

	/**
	 * This method is used in case result of the query is miltiple records.
	 * 
	 * @param query query to be executed.
	 * @return List of records.
	 * @throws DynamicExtensionsSystemException
	 */
	public List getResultInList(String query) throws DynamicExtensionsSystemException
	{
		List resultList = null;
		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);

		try
		{
			jdbcDAO.openSession(null);
			resultList = jdbcDAO.executeQuery(query, null, false, false, null);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException("Could notexecuting the query ", e);
		}
		catch (ClassNotFoundException e)
		{
			throw new DynamicExtensionsSystemException("Could notexecuting the query ", e);
		}
		return resultList;
	}

	/**
	 * This method returns all the entity groups reachable from given entity.
	 *  
	 * @param entity
	 * @param processedEntities
	 * @param processedEntityGroups
	 */
	public static void getAllEntityGroups(EntityInterface entity,
			Set<EntityInterface> processedEntities, Set<EntityGroupInterface> processedEntityGroups)
	{

		if (processedEntities.contains(entity))
		{
			return;
		}

		processedEntities.add(entity);

		// get all entity Groups of given entity
		for (EntityGroupInterface entityGroup : entity.getEntityGroupCollection())
		{

			if (!processedEntityGroups.contains(entityGroup))
			{
				processedEntityGroups.add(entityGroup);
				// process  all entities of each entity Groups 
				for (EntityInterface anotherEntity : entityGroup.getEntityCollection())
				{
					getAllEntityGroups(anotherEntity, processedEntities, processedEntityGroups);
				}
			}
		}
	}

	/**
	 * @return
	 * @throws DynamicExtensionsSystemException 
	 */
	public static boolean isValuePresent(AttributeInterface attribute, Object value)
			throws DynamicExtensionsSystemException
	{
		return DynamicExtensionBaseQueryBuilder.isValuePresent(attribute, value);
	}
	
	public static Collection filterSystemAttributes(Collection<AbstractAttributeInterface> abstractAttributeCollection)
	{
		Collection<AbstractAttributeInterface> collection = new HashSet(abstractAttributeCollection);
		for (AbstractAttributeInterface abstractAttribute : abstractAttributeCollection)
		{
			if (abstractAttribute instanceof AttributeInterface)
			{
				if (abstractAttribute.getName().equalsIgnoreCase(ID_ATTRIBUTE_NAME))
				{
					collection.remove(abstractAttribute);
				}
			}
		}
		return collection;
	}
}
