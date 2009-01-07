
package edu.common.dynamicextensions.entitymanager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Blob;
import java.sql.SQLException;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * This class provides the methods that builds the queries that are specific to ORACLE database 
 * 
 * @author Rahul Ner
 */
public class DynamicExtensionOracleQueryBuilder extends DynamicExtensionBaseQueryBuilder
{
	/**
	 * Converts Blob data type to Object data type for Oracle database
	 * @param valueObj
	 * @return
	 * @throws DynamicExtensionsSystemException
	 */
	protected Object convertValueToObject(Object valueObj) throws DynamicExtensionsSystemException
	{
		Object value = "";

		Blob blob = (Blob) valueObj;
		try
		{
			value = new ObjectInputStream(blob.getBinaryStream()).readObject();
		}
		catch (SQLException e)
		{
			throw new DynamicExtensionsSystemException("Error while converting the data", e);
		}
		catch (IOException e)
		{
			throw new DynamicExtensionsSystemException("Error while converting the data", e);
		}
		catch (ClassNotFoundException e)
		{
			throw new DynamicExtensionsSystemException("Error while converting the data", e);
		}
		return value;
	}
}
