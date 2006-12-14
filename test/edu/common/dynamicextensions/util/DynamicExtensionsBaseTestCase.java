/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */

package edu.common.dynamicextensions.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import junit.framework.TestCase;
import net.sf.hibernate.HibernateException;
import edu.common.dynamicextensions.util.global.Variables;
import edu.wustl.common.util.dbManager.DBUtil;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.common.util.logger.Logger;

public class DynamicExtensionsBaseTestCase extends TestCase
{

	/**
	 * 
	 */
	public DynamicExtensionsBaseTestCase()
	{
		super();
		
	}

	/**
	 * @param arg0 name
	 */
	public DynamicExtensionsBaseTestCase(String arg0)
	{
		super(arg0);
	}

	/**
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp()
	{
		
		Logger.out = org.apache.log4j.Logger.getLogger("dynamicExtensions.logger");
        ApplicationProperties.initBundle("ApplicationResources");
		Variables.containerFlag = false;
		Variables.datePattern = "mm-dd-yyyy";
		Variables.timePattern = "hh-mi-ss";
		Variables.dateFormatFunction = "TO_CHAR";
		Variables.timeFormatFunction = "TO_CHAR";
		Variables.dateTostrFunction = "TO_CHAR";
		Variables.strTodateFunction = "TO_DATE";
	}

	/**
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown()
	{
		Variables.containerFlag = true;
	}

	/**
	 * @param query query to be executed
	 * @return 
	 */
	protected ResultSet executeQuery(String query)
	{
		//      Checking whether the data table is created properly or not.
		Connection conn = null;
		try
		{
			conn = DBUtil.getConnection();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		java.sql.PreparedStatement statement = null;
		try
		{
			statement = conn.prepareStatement(query);
			return statement.executeQuery();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			fail();
		}

		return null;
	}
	
	/**
	 * @param query query to be executed
	 * @return  ResultSetMetaData
	 */
	protected ResultSetMetaData executeQueryForMetadata(String query)
	{
		//      Checking whether the data table is created properly or not.
		Connection conn = null;
		try
		{
			conn = DBUtil.getConnection();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		java.sql.PreparedStatement statement = null;
		try
		{
			statement = conn.prepareStatement(query);
			return statement.executeQuery().getMetaData();
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			fail();
		}

		return null;
	}

	/**
	 * @param query query to be executed
	 * @return  ResultSetMetaData
	 */
	protected ResultSetMetaData executeQueryDDL(String query)
	{
		//      Checking whether the data table is created properly or not.
		Connection conn = null;
		try
		{
			conn = DBUtil.getConnection();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		java.sql.PreparedStatement statement = null;
		try
		{
			statement = conn.prepareStatement(query);
			statement.execute(query);
		}
		catch (SQLException e)
		{
			e.printStackTrace();
			fail();
		}

		return null;
	}
	
	/**
	 * @param tableName
	 * @return
	 */
	protected boolean  isTablePresent(String tableName) {
		Connection conn = null;
		String query = "select * from " + tableName;
		try
		{
			conn = DBUtil.getConnection();
		}
		catch (HibernateException e)
		{
			e.printStackTrace();
		}
		java.sql.PreparedStatement statement = null;
		try
		{
			statement = conn.prepareStatement(query);
			statement.executeQuery();
		}
		catch (SQLException e)
		{
			return false;
		}
		return true;
	}

}
