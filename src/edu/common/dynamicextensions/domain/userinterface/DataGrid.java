package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.DataGridInterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_DATA_GRID" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class DataGrid extends Control implements DataGridInterface
{

    /**
     * 
     *
     */
	public DataGrid(){

	}
	
    /**
     * 
     */
	public String generateHTML()
    {
    	return "HTML Not Implemented";
    }

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface#setAttribute(edu.common.dynamicextensions.domaininterface.AttributeInterface)
	 */
	public void setAttribute(AbstractAttributeInterface attributeInterface) {
		// TODO Auto-generated method stub
		
	}

	

}