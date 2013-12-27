
package edu.common.dynamicextensions.client;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.utility.HTTPSConnection;

/**
 * This class should be the parent of all the classes which needs to communicate to the server from
 * the standalone java programms, eg. categoryCreator, permissibleValuesCreator etc.
 * For using this class child class only have to implement the validate & initializeResources
 * method according to their need & call the method execute to start the process.
 * @author pavan_kalantri
 *
 */
public abstract class AbstractClient
{
	/**
	 * logger for information.
	 */
	protected static final Logger LOGGER = Logger.getLogger(AbstractClient.class);

	/**
	 * Zip file which shoyld be uploaded
	 */
	protected transient File zipFile;

	/**
	 * Server URl to which to connect.
	 */
	protected transient URL serverUrl;

	protected transient URL jbossUrl;

	protected Map<String, Object> paramaterObjectMap;

	@Override
	public String toString()
	{
		return "AbstractClient [paramaterObjectMap=" + paramaterObjectMap + ", serverUrl="
				+ serverUrl + "]";
	}

	/**
	 * This method will initiate the task on server by the URl specified.
	 * @param args The list of arguments this method should be as the child expects
	 * according to is initializeResources method.
	 *
	 */
	public void execute(String[] args)
	{
		HTTPSConnection httpsConnection = HTTPSConnection.getInstance();
		try
		{
			// trust all the https connections
			httpsConnection.acceptAllHttpsConnections();
			//validate all the arguments
			validate(args);
			// Initialize all instance variables
			initializeResources(args);
			// open the servlet connection
			LOGGER.info("AbstractClient :: execute :: Initialisation is Completed");

			URLConnection servletConnection = httpsConnection.openServletConnection(serverUrl);
			performAction(httpsConnection, servletConnection);
			LOGGER.info("AbstractClient :: execute :: performAction is Completed");

			// read the response from server
			processResponse(servletConnection);
			LOGGER.info("AbstractClient :: execute :: processResponse is Completed");

		} catch (Exception e) {
			LOGGER.error("Encountered error performing the action", e);
			throw new RuntimeException(e);
		}
	}

	protected void performAction(HTTPSConnection httpsConnection, URLConnection servletConnection)
	throws IOException
	{
		// upload the Zip file to server
		httpsConnection.uploadFileToServer(servletConnection, zipFile);

	}

	/**
	 * This method will initialize all the instance veriabls required for the process to
	 * complete.
	 * @param args arguments array from to initilize the variables.
	 */
	protected abstract void initializeResources(String[] args);

	/**
	 * It will validate weather the correct number of arguments are passed or not & then
	 * throw exception accordingly.
	 * @param args arguments
	 */
	protected abstract void validate(String[] args);

	/**
	 * This method will process the response recieved from the server .
	 * @param servletConnection connection by which connected to server
	 */
	protected void processResponse(URLConnection servletConnection)
	throws IOException
	{
		HTTPSConnection.getInstance().processResponse(servletConnection);
	}

	public Map<String, Object> getParamaterObjectMap()
	{
		return paramaterObjectMap;
	}

	public void setParamaterObjectMap(Map<String, Object> paramaterObjectMap)
	{
		this.paramaterObjectMap = paramaterObjectMap;
	}

	public URL getServerUrl()
	{
		return serverUrl;
	}

	public void setServerUrl(URL serverUrl)
	{
		this.serverUrl = serverUrl;
	}

	public URL getJbossUrl()
	{
		return jbossUrl;
	}

	public void setJbossUrl(URL jbossUrl)
	{
		this.jbossUrl = jbossUrl;
	}

	/**
	 * Close object input stream.
	 * @param inputStream the input stream
	 */
	public static void closeObjectInputStream(ObjectInputStream inputStream)
	{
		try
		{
			if (inputStream != null)
			{
				inputStream.close();
			}
		}
		catch (IOException e)
		{
			LOGGER.error("Error while closing input stream " + e.getMessage());
			LOGGER.debug("Error while closing input stream ", e);
		}
	}
}
