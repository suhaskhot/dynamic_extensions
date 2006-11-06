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
    String TABLE_NAME_PREFIX = "DYEXTN_ENTITY";
    String UNDERSCORE = "_";
    String COLUMN_NAME_PREFIX = "DYEXTN_ATTRIBUTE";
    String CREATE_TABLE = "CREATE TABLE ";
    String ALTER_TABLE = "ALTER TABLE";
    String DROP_KEYWORD = "DROP";
    String ADD_KEYWORD = "ADD";
    String MODIFY_KEYWORD = "MODIFY";
    String CONSTRAINT_KEYWORD = "CONSTRAINT";
    String UNIQUE_KEYWORD = "UNIQUE";
    String COLUMN_KEYWORD = "COLUMN";
    String NULL_KEYWORD = "NULL";
    String NOT_KEYWORD = "NOT";
    String UNIQUE_CONSTRAINT_SUFFIX = "UC";
    String OPENING_BRACKET = "(";
    String CLOSING_BRACKET = ")";
    String COMMA = ",";
    String WHITESPACE = " ";
    String IDENTIFIER = "IDENTIFIER";
    String PRIMARY_KEY_CONSTRAINT_FOR_ENTITY_DATA_TABLE = "primary key (IDENTIFIER)";
}
