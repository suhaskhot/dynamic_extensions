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


	String DIRECTION_SRC_DESTINATION = "SRC_DESTINATION";
	String DIRECTION_BI_DIRECTIONAL = "BI_DIRECTIONAL";

	static final int DATA_TABLE_STATE_CREATED = 1;
	static final int DATA_TABLE_STATE_NOT_CREATED = 2;
	static final int DATA_TABLE_STATE_ALREADY_PRESENT = 3;
	
	String ID_ATTRIBUTE_NAME = "id";	
	String STRING_ATTRIBUTE_TYPE = "string";
	String FLOAT_ATTRIBUTE_TYPE = "float";
	String SHORT_ATTRIBUTE_TYPE = "short";
	String BOOLEAN_ATTRIBUTE_TYPE = "boolean";
	String FILE_ATTRIBUTE_TYPE = "file";
	String DATE_ATTRIBUTE_TYPE = "date";
	String DATE_TIME_ATTRIBUTE_TYPE = "dateTime";	
	String DOUBLE_ATTRIBUTE_TYPE = "double";
	String LONG_ATTRIBUTE_TYPE = "long";
    String INTEGER_ATTRIBUTE_TYPE = "integer";
    String OBJECT_ATTRIBUTE_TYPE = "object";   
}
