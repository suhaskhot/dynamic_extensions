package edu.common.dynamicextensions.domain.databaseproperties;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_COLUMN_PROPERTIES" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class ColumnProperties extends DatabaseProperties {

    /**
     * Empty constructor
     */
	public ColumnProperties(){

	}
    
    /**
     * 
     */
	public void setAllValues(AbstractActionForm arg0) throws AssignDataException {
		// TODO Auto-generated method stub
		
	}

}