package edu.common.dynamicextensions.domain.databaseproperties;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:09 PM
 * @hibernate.joined-subclass table="DYEXTN_TABLE_PROPERTIES" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class TableProperties extends DatabaseProperties {
	
	/**
	 * Empty constructor.
	 */
	public TableProperties(){
		
	}
		
	/**
	 * 
	 */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
		// TODO Auto-generated method stub
		
	}
	
}