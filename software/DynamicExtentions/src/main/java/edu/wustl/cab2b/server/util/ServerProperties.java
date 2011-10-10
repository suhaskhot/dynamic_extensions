
package edu.wustl.cab2b.server.util;

import java.util.Properties;

import edu.wustl.cab2b.common.util.Utility;

/**
 * This class is wrapper around property file server.properties.
 * @author Chandrakant Talele
 */
public final class ServerProperties
{

	/**
	 * Instantiates a new server properties.
	 */
	private ServerProperties()
	{
	}

	/** The Constant props. */
	private static final Properties PROPS = Utility.getPropertiesFromFile("server.properties");

	/**
	 * @return The database IP
	 */
	public static String getDatabaseIp()
	{
		return PROPS.getProperty("database.server.ip");
	}

	/**
	 * @return The database port
	 */
	public static String getDatabasePort()
	{
		return PROPS.getProperty("database.server.port");
	}

	/**
	 * @return the name of the database
	 */
	public static String getDatabaseName()
	{
		return PROPS.getProperty("database.name");
	}

	/**
	 * @return the user to be used for connecting to the database
	 */
	public static String getDatabaseUser()
	{
		return PROPS.getProperty("database.username");
	}

	/**
	 * @return the password of database user
	 */
	public static String getDatabasePassword()
	{
		return PROPS.getProperty("database.password");
	}

	/**
	 * @return The database loader to be used
	 */
	public static String getDatabaseLoader()
	{
		return PROPS.getProperty("database.loader");
	}

	/**
	 * @return The data-source name
	 */
	public static String getDatasourceName()
	{
		return PROPS.getProperty("datasource.name");
	}

	/**
	 *
	 * @param gridType
	 * @return the grid cert location
	 */
	public static String getGridCert(String gridType)
	{
		return PROPS.getProperty(gridType + "_grid_cert_location");
	}

	/**
	 *
	 * @param gridType
	 * @return the grid key location
	 */
	public static String getGridKey(String gridType)
	{
		return PROPS.getProperty(gridType + "_grid_key_location");
	}

}