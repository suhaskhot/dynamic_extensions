
package edu.common.dynamicextensions.entitymanager;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;

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
	/**
	 * This method generates the alter table query to drop columns.
	 * @param tableName
	 * @param columnNames
	 * @return String altered query
	 */
	protected String getDropColumnQuery(String tableName, List<String> columnNames)
	{
		StringBuffer alterQuery = new StringBuffer();

		alterQuery.append(ALTER_TABLE);
		alterQuery.append(tableName);
		alterQuery.append(WHITESPACE);
		alterQuery.append(DROP_KEYWORD);
		alterQuery.append(OPENING_BRACKET);

		for (int i = 0; i < columnNames.size(); i++)
		{
			alterQuery.append(columnNames.get(i));
			if (i != columnNames.size() - 1)
			{
				alterQuery.append(COMMA);
			}
		}

		alterQuery.append(CLOSING_BRACKET);

		return alterQuery.toString();
	}
}
