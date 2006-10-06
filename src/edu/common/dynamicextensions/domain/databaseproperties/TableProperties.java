package edu.common.dynamicextensions.domain.databaseproperties;

import edu.common.dynamicextensions.domaininterface.databaseproperties.TablePropertiesInterface;
import edu.common.dynamicextensions.ui.webui.actionform.FormDefinitionForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.exception.AssignDataException;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:09 PM
 * @hibernate.joined-subclass table="DYEXTN_TABLE_PROPERTIES" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class TableProperties extends DatabaseProperties implements TablePropertiesInterface{
	
	/**
	 * Empty constructor.
	 */
	public TableProperties(){
		
	}
	
	/**
     * 
     * @param actionForm
     */
    public  TableProperties(AbstractActionForm actionForm){
        setAllValues(actionForm);
    }
		
	/**
	 * 
	 */
	public void setAllValues(AbstractActionForm abstractForm)  {
		// TODO Auto-generated method stub
	    FormDefinitionForm tablePropertiesForm = (FormDefinitionForm)abstractForm;
        if(tablePropertiesForm.getFormName() != null){
        	this.name = tablePropertiesForm.getFormName();
        }
		
	}
	
}