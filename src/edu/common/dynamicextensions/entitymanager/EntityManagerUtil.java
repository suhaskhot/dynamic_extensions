
package edu.common.dynamicextensions.entitymanager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.AbstractAttribute;
import edu.common.dynamicextensions.domain.Attribute;
import edu.common.dynamicextensions.domain.DateAttributeTypeInformation;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.global.Constants;
import edu.common.dynamicextensions.util.global.Variables;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.dao.DAOFactory;
import edu.wustl.common.dao.JDBCDAO;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.logger.Logger;

public class EntityManagerUtil
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
			String format = ((DateAttributeTypeInformation) attributeInformation).getFormat();
			if (format == null)
			{
				format = Constants.DATE_PATTERN_MM_DD_YYYY;
			}
			String str = null;
			if (value instanceof Date)
			{
				str = Utility.parseDateToString(((Date) value), format);
			}
			else
			{
				str = (String) value;
			}

			formattedvalue = Variables.strTodateFunction + "('" + str + "','"
					+ Variables.datePattern + "')";
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
	public ResultSet executeQuery(String query) throws DynamicExtensionsSystemException
	{

		Connection conn = null;
		try
		{
			conn = DBUtil.getConnection();
			Statement statement = null;
			statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			resultSet.next();
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
	 * @param query query to be executed
	 * @return 
	 * @throws DynamicExtensionsSystemException 
	 */
	public int executeDML(String query) throws DynamicExtensionsSystemException
	{

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
		List resultList = null;
		try
		{
			resultList = getResultInList(queryToGetNextIdentifier.toString(),
					new SessionDataBean(), false, false, null);
		}
		catch (DAOException e)
		{
			throw new DynamicExtensionsSystemException(
					"Could not fetch the next identifier for table " + entityTableName);
		}
		catch (ClassNotFoundException e)
		{
			throw new DynamicExtensionsSystemException(
					"Could not fetch the next identifier for table " + entityTableName);
		}

		if (resultList == null)
		{
			throw new DynamicExtensionsSystemException(
					"Could not fetch the next identifier for table " + entityTableName);
		}
		List internalList = (List) resultList.get(0);
		if (internalList == null || internalList.isEmpty())
		{
			throw new DynamicExtensionsSystemException(
					"Could not fetch the next identifier for table " + entityTableName);
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
		}

		long id = identifier.longValue();
		id++;
		identifier = new Long(id);
		return identifier;
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
	private List getResultInList(String queryToGetNextIdentifier, SessionDataBean sessionDataBean,
			boolean isSecureExecute, boolean hasConditionOnIdentifiedField,
			Map queryResultObjectDataMap) throws DAOException, ClassNotFoundException
	{
		List resultList = null;
		JDBCDAO jdbcDAO = (JDBCDAO) DAOFactory.getInstance().getDAO(Constants.JDBC_DAO);
		try
		{
			jdbcDAO.openSession(null);
			resultList = jdbcDAO.executeQuery(queryToGetNextIdentifier, sessionDataBean,
					isSecureExecute, hasConditionOnIdentifiedField, queryResultObjectDataMap);
		}
		catch (DAOException daoException)
		{
			daoException.printStackTrace();
			throw new DAOException("Exception while retrieving the query result", daoException);
		}
//		finally
//		{
//			try
//			{
//				jdbcDAO.closeSession();
//			}
//			catch (DAOException daoException)
//			{
//				throw new DAOException("Exception while closing the jdbc session", daoException);
//			}
//		}
		return resultList;
	}

}
