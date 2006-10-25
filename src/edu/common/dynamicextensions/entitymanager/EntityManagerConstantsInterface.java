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
    static final String OPENING_BRACKET = "(";
    static final String IDENTIFIER = "IDENTIFIER"; 
    static final String COMMA = ","; 
    static final String PRIMARY_KEY_CONSTRAINT_FOR_ENTITY_DATA_TABLE = "primary key (IDENTIFIER)";
}

