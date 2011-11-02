
package edu.common.dynamicextensions.entitymanager;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * This class provides the methods that builds the queries that are specific to ORACLE database
 *
 * @author Rahul Ner,pavan_kalantri
 */
public class DynamicExtensionOracleQueryBuilder extends DynamicExtensionBaseQueryBuilder
{

	/**
	 *This method create the query for altering the column of given attribute to add not null constraint on it
	 * no need to specify not null so returning null
	 *@param attribute on which the constraint is to be applied
	 *@return query
	 */
	@Override
    protected String addNotNullConstraintQuery(AttributeInterface attribute)
			throws DynamicExtensionsSystemException
	{
		/*		StringBuffer query=new StringBuffer();
				query.append(ALTER_TABLE).append(WHITESPACE).append(attribute.getEntity().getTableProperties().getName())
					.append(WHITESPACE).append(MODIFY_KEYWORD).append(WHITESPACE).append(attribute.getColumnProperties().getName())
					.append(WHITESPACE).append(getDataTypeForAttribute(attribute)).append(WHITESPACE)
					.append(NOT_KEYWORD).append(WHITESPACE).append(NULL_KEYWORD);
				return query.toString();*/
		return null;
	}

	/**
	 *This method create the query for altering the column of given attribute to add null constraint on it
	 * no need to specify null so returning null
	 *@param attribute on which the constraint is to be applied
	 *@return query
	 */
	@Override
    protected String dropNotNullConstraintQuery(AttributeInterface attribute)
			throws DynamicExtensionsSystemException
	{
		/*StringBuffer query=new StringBuffer();
		query.append(ALTER_TABLE).append(WHITESPACE).append(attribute.getEntity().getTableProperties().getName())
			.append(WHITESPACE).append(MODIFY_KEYWORD).append(WHITESPACE).append(attribute.getColumnProperties().getName())
			.append(WHITESPACE).append(getDataTypeForAttribute(attribute)).append(WHITESPACE).append(NULL_KEYWORD);
		return query.toString();*/
		return null;
	}

	/**
	 * This method generates the alter table query to drop columns.
	 * @param tableName
	 * @param columnNames
	 * @return String altered query
	 */
	@Override
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
