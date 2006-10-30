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

    static final String TABLE_NAME_PREFIX = "DYEXTN_ENTITY";
    
    static final String UNDERSCORE = "_";
    static final String COLUMN_NAME_PREFIX = "DYEXTN_ATTRIBUTE";
    
    static final String CREATE_TABLE = "CREATE TABLE ";
    static final String  ALTER_TABLE  =  "ALTER TABLE";
    static final String  DROP_KEYWORD  =  "DROP";
    static final String  ADD_KEYWORD  =  "ADD";
    static final String  MODIFY_KEYWORD  =  "MODIFY";
    static final String  CONSTRAINT_KEYWORD  =  "CONSTRAINT";
    static final String  UNIQUE_KEYWORD  = "UNIQUE";
    static final String  COLUMN_KEYWORD  = "COLUMN";
    static final String  NULL_KEYWORD  = "NULL";
    static final String  NOT_KEYWORD  = "NOT";
    
    static final String  UNIQUE_CONSTRAINT_SUFFIX  =  "UC" ;
    
    
    
    
    
    
    static final String OPENING_BRACKET = "(";
    static final String CLOSING_BRACKET = ")";
    static final String COMMA = ",";
    static final String WHITESPACE = " ";
    
    
    static final String IDENTIFIER = "IDENTIFIER"; 
    static final String PRIMARY_KEY_CONSTRAINT_FOR_ENTITY_DATA_TABLE = "primary key (IDENTIFIER)";
    
}

