/**
 *<p>Title: </p>
 *<p>Description:  </p>
 *<p>Copyright:TODO</p>
 *@author Vishvesh Mulay
 *@version 1.0
 */

package edu.common.dynamicextensions.entitymanager;

public interface EntityManagerConstantsInterface
{

	String TABLE_NAME_PREFIX = "DE_E";
	String UNDERSCORE = "_";
	String COLUMN_NAME_PREFIX = "DE_AT";
	String ASSOCIATION_NAME_PREFIX = "DE_AS";
	String ASSOCIATION_COLUMN_PREFIX = "DE_E";

	String CREATE_TABLE = "CREATE TABLE ";
	String ALTER_KEYWORD = "ALTER";
	String ALTER_TABLE = ALTER_KEYWORD + " TABLE";
	String DROP_KEYWORD = "DROP";
	String TABLE_KEYWORD = "TABLE";
	String ADD_KEYWORD = "ADD";
	String MODIFY_KEYWORD = "MODIFY";
	String DEFAULT_KEYWORD = "DEFAULT";
	String DELETE_KEYWORD = "DELETE FROM";
	String WHERE_KEYWORD = "WHERE";
	String SELECT_KEYWORD = "SELECT";
	String COUNT_KEYWORD = "COUNT";
	String FROM_KEYWORD = "FROM";
	String UPDATE_KEYWORD = "UPDATE";
	String CONSTRAINT_KEYWORD = "CONSTRAINT";
	String UNIQUE_KEYWORD = "UNIQUE";
	String COLUMN_KEYWORD = "COLUMN";
	String NULL_KEYWORD = "NULL";
	String SET_KEYWORD = "SET";
	String NOT_KEYWORD = "NOT";
	String UNIQUE_CONSTRAINT_SUFFIX = "UC";
	String OPENING_BRACKET = "(";
	String CLOSING_BRACKET = ")";
	String COMMA = ",";
	String EQUAL = "=";
	String IN_KEYWORD = "IN";
	String WHITESPACE = " ";
	String IDENTIFIER = "IDENTIFIER";
	String PRIMARY_KEY_CONSTRAINT_FOR_ENTITY_DATA_TABLE = "primary key (IDENTIFIER)";
	String ALTER_COLUMN_KEYWORD = "ALTER " + COLUMN_KEYWORD;
	String REFERENCES_KEYWORD = "REFERENCES";
	String FOREIGN_KEY_KEYWORD = " foreign key "; 

	String DIRECTION_SRC_DESTINATION = "SRC_DESTINATION";
	String DIRECTION_BI_DIRECTIONAL = "BI_DIRECTIONAL";

	static final int DATA_TABLE_STATE_CREATED = 1;
	static final int DATA_TABLE_STATE_NOT_CREATED = 2;
	static final int DATA_TABLE_STATE_ALREADY_PRESENT = 3;
	
	String STRING_ATTRIBUTE_TYPE = "string";
	String FLOAT_ATTRIBUTE_TYPE = "float";
	String SHORT_ATTRIBUTE_TYPE = "short";
	String BOOLEAN_ATTRIBUTE_TYPE = "boolean";
	String FILE_ATTRIBUTE_TYPE = "file";
	String DATE_ATTRIBUTE_TYPE = "date";
	String DOUBLE_ATTRIBUTE_TYPE = "double";
	String LONG_ATTRIBUTE_TYPE = "long";
	String INTEGER_ATTRIBUTE_TYPE = "integer";
	
}
