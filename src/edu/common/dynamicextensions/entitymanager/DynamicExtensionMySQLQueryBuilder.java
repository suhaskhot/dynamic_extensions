
package edu.common.dynamicextensions.entitymanager;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.AbstractEntityInterface;

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
	/*
	 * cnanged by: pavan
	 * Method signature is different than the DynamicExtensionsBaseQueryBuilder
	 * thus it is changed to match. Thus it can be overriden correctly
	 * 
	 */

	protected String getForeignKeyRemoveConstraintQueryForInheritance(
			AbstractEntityInterface entity, AbstractEntityInterface parentEntity)
	{
		StringBuffer foreignKeyConstraint = new StringBuffer();
		/*changed by :- pavan
		 * fk id is same as constraint name.
		 * not working with fkid
		 * 
		 */
		//String foreignConstraintName = "FK" + "E" + entity.getId() + "E" + parentEntity.getId();
		String foreignConstraintName = entity.getTableProperties().getConstraintName() + UNDERSCORE
				+ parentEntity.getTableProperties().getConstraintName();

		foreignKeyConstraint.append(ALTER_TABLE).append(WHITESPACE).append(
				entity.getTableProperties().getName()).append(WHITESPACE).append(DROP_KEYWORD)
				.append(WHITESPACE).append(FOREIGN_KEY_KEYWORD).append(WHITESPACE).append(
						foreignConstraintName);

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
