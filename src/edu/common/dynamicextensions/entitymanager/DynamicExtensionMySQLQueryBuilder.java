
package edu.common.dynamicextensions.entitymanager;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.EntityInterface;

/**
 * This class provides the methods that builds the queries that are specific to ORACLE database 
 * 
 * @author Rahul Ner
 */

public class DynamicExtensionMySQLQueryBuilder extends DynamicExtensionBaseQueryBuilder
{
	/**
	 * This method returns the query to add foreign key constraint in the given child entity
	 * that refers to identifier column of the parent.
	 * @param entity
	 * @return
	 */
	protected String getForeignKeyRemoveConstraintQueryForInheritance(EntityInterface entity)
	{
		StringBuffer foreignKeyConstraint = new StringBuffer();
		EntityInterface parentEntity = entity.getParentEntity();
		String foreignConstraintName = "FK" + "E" + entity.getId() + "E" + parentEntity.getId();

		foreignKeyConstraint.append(ALTER_TABLE).append(WHITESPACE).append(entity.getTableProperties().getName()).append(WHITESPACE).append(
				DROP_KEYWORD).append(WHITESPACE).append(FOREIGN_KEY_KEYWORD).append(WHITESPACE).append(foreignConstraintName);

		return foreignKeyConstraint.toString();
	}

	/**
	 * This method generate the alter table query to drop columns
	 * @param tableName
	 * @param columnName
	 * @return alter query
	 */
	protected String getDropColumnQuery(String tableName, List<String> columnName)
	{
		StringBuffer alterTableQuery = new StringBuffer();

		alterTableQuery.append(ALTER_TABLE);
		alterTableQuery.append(tableName);
		alterTableQuery.append(WHITESPACE);

		for (int i = 0; i < columnName.size(); i++)
		{
			alterTableQuery.append(DROP_KEYWORD);
			alterTableQuery.append(COLUMN_KEYWORD);
			alterTableQuery.append(columnName.get(i));
			if (i != columnName.size() - 1)
			{
				alterTableQuery.append(COMMA);
			}
		}

		return alterTableQuery.toString();
	}

}
