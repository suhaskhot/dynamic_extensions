
package edu.common.dynamicextensions.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import edu.common.dynamicextensions.dao.impl.DynamicExtensionDAO;
import edu.common.dynamicextensions.dao.impl.DynamicExtensionDBFactory;
import edu.common.dynamicextensions.dao.impl.IDEDBUtility;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.common.util.logger.Logger;
import edu.wustl.dao.daofactory.DAOConfigFactory;

/**
 * Used for cleaning or dropping all the table created previously.
 *
 * @author pavan_kalantri
 */
public final class DatabaseCleaner
{


	private DatabaseCleaner()
	{

	}

	/** The Constant DRIVER_NOT_FOUND. */
	private static final String DRIVER_NOT_FOUND = "Driver Not found";

	/** The Constant ORACLE_DRIVER. */
	private static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";

	/** The Constant MYSQL_DRIVER. */
	private static final String MYSQL_DRIVER = "org.gjt.mm.mysql.Driver";

	/** The Constant DB2_DRIVER. */
	private static final String DB2_DRIVER = "com.ibm.db2.jcc.DB2Driver";

	/** The Constant MSSQLSERVER_DRIVER. */
	private static final String MSSQLSERVER_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	/**
	 * The main method.
	 *
	 * @param args args[0]=Databae.type,args[1]=connection url
	 * args[2]=database.username,args[3]=database.password
	 */
	public static void main(String[] args)
	{
		try
		{
			String appName=DynamicExtensionDAO.getInstance().getAppName();
			String dbType=DAOConfigFactory.getInstance().getDAOFactory(appName).getDataBaseType();
			IDEDBUtility dbUtility=DynamicExtensionDBFactory.getInstance().getDbUtility(dbType);
			dbUtility.cleanDatabase(args);
		}
		catch (DynamicExtensionsSystemException e)
		{
			Logger.out.info("Can not clean the database");
			Logger.out.error(e.getMessage());
		}

	}

	/**
	 * Opens connection For jdbc.
	 *
	 * @param db_driver the db_driver
	 * @param url the url
	 * @param userName the user name
	 * @param password the password
	 *
	 * @return the connection
	 *
	 * @throws ClassNotFoundException the class not found exception
	 * @throws SQLException the SQL exception
	 */
	private static Connection getConnection(String db_driver, String url, String userName,
			String password) throws ClassNotFoundException, SQLException
	{
		Class.forName(db_driver);
		return DriverManager.getConnection(url, userName, password);
	}

	/**
	 * Closes the jdbc connection.
	 *
	 * @param conn the conn
	 */
	private static void closeConnection(Connection conn)
	{
		try
		{
			if (conn != null)
			{
				conn.close();
			}
		}
		catch (SQLException e)
		{
			Logger.out.error("The cause of the exception is - " + e.getMessage());
		}
	}

	/**
	 * It will execute the given query using provided connection.
	 *
	 * @param query the query
	 * @param conn the conn
	 *
	 * @return the result set
	 *
	 * @throws SQLException the SQL exception
	 */
	private static ResultSet executeQuery(String query, Connection conn) throws SQLException
	{
		Statement statement = conn.createStatement();
		return statement.executeQuery(query);
	}

	/**
	 * It will execute the update or ddl query  using provided connection.
	 *
	 * @param query the query
	 * @param conn the conn
	 *
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	private static void executeUpdate(String query, Connection conn)
			throws DynamicExtensionsSystemException
	{
		try
		{
			PreparedStatement statement = conn.prepareStatement(query);
			statement.executeUpdate();
			statement.close();
		}
		catch (SQLException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
	}

	/**
	 * It will execute given query & also executes all the queries generated
	 * by that query's resultset.
	 *
	 * @param query the query
	 * @param conn the conn
	 *
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	private static void executeAllQueries(String query, Connection conn)
			throws DynamicExtensionsSystemException
	{
		try
		{
			ResultSet resultSet = executeQuery(query, conn);
			while (resultSet.next())
			{
				String resultQuery = resultSet.getString(1);
				executeUpdate(resultQuery, conn);
			}
			resultSet.close();
		}
		catch (SQLException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}

	}

	/**
	 * Clean the Db2 database or drop all tables in db2 database.
	 *
	 * @param args the args
	 *
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	public static void cleanDb2(String[] args) throws DynamicExtensionsSystemException
	{
		Connection conn = null;
		try
		{
			conn = getConnection(DB2_DRIVER, args[1], args[2], args[3]);
			String query = "Select concat('DROP TABLE ', tabname) from syscat.tables where tabschema='DB2ADMIN'";
			executeAllQueries(query, conn);

		}
		catch (SQLException e)
		{
			throw new DynamicExtensionsSystemException("Failed to Execute the drop table query ", e);
		}
		catch (ClassNotFoundException e)
		{
			throw new DynamicExtensionsSystemException(DRIVER_NOT_FOUND, e);
		}
		finally
		{
			closeConnection(conn);
		}

	}

	/**
	 * Clean the Oracle database or drop all tables in Oracle userspace.
	 *
	 * @param args the args
	 *
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	public static void cleanOracle(String[] args) throws DynamicExtensionsSystemException
	{
		Connection conn = null;

		try
		{
			conn = getConnection(ORACLE_DRIVER, args[1], args[2], args[3]);
			String query = "SELECT 'DROP TABLE '||table_name||' CASCADE CONSTRAINTS'FROM user_tables";
			executeAllQueries(query, conn);
			query = "SELECT 'DROP sequence '||sequence_name FROM user_sequences";
			executeAllQueries(query, conn);

		}
		catch (SQLException e)
		{
			throw new DynamicExtensionsSystemException(e.getMessage(), e);
		}
		catch (ClassNotFoundException e)
		{
			throw new DynamicExtensionsSystemException(DRIVER_NOT_FOUND, e);
		}
		finally
		{
			closeConnection(conn);
		}

	}

	/**
	 * Drop database in mysql & recreates it.
	 *
	 * @param args the args
	 *
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	public static void cleanMysql(String[] args) throws DynamicExtensionsSystemException
	{
		Connection conn = null;

		int index = args[1].lastIndexOf('/');
		String databaseName = args[1].substring(index + 1);
		String url = args[1].substring(0, index);
		Logger.out.info(url + " " + databaseName);

		try
		{
			conn = getConnection(MYSQL_DRIVER, url + "/de_temp", args[2], args[3]);
			String query = "drop database " + databaseName;
			executeUpdate(query, conn);
			query = "create database " + databaseName;
			executeUpdate(query, conn);
		}
		catch (SQLException e)
		{
			throw new DynamicExtensionsSystemException(
					"failed to execute the create datatbase query", e);
		}
		catch (ClassNotFoundException e)
		{
			throw new DynamicExtensionsSystemException(DRIVER_NOT_FOUND, e);
		}
		finally
		{
			closeConnection(conn);
		}

	}

	/**
	 * Drop database in mssqlserver & recreates it.
	 *
	 * @param args the args
	 *
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 */
	public static void cleanMsSqlServer(String[] args) throws DynamicExtensionsSystemException
	{
		Connection conn = null;

		int index = args[1].lastIndexOf('=');
		String databaseName = args[1].substring(index + 1);
		String url = args[1].substring(0, index);

		try
		{
			conn = getConnection(MSSQLSERVER_DRIVER, url + "=de_temp", args[2], args[3]);
			String query = "drop database " + databaseName;
			executeUpdate(query, conn);
			query = "create database " + databaseName;
			executeUpdate(query, conn);
		}
		catch (SQLException e)
		{
			throw new DynamicExtensionsSystemException(
					"failed to execute the create datatbase query", e);
		}
		catch (ClassNotFoundException e)
		{
			throw new DynamicExtensionsSystemException(DRIVER_NOT_FOUND, e);
		}
		finally
		{
			closeConnection(conn);
		}

	}
}
