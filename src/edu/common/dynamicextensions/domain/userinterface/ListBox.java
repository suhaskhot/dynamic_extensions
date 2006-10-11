package edu.common.dynamicextensions.domain.userinterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ListBoxInterface;
import edu.common.dynamicextensions.ui.webui.util.UIConfigurationConstants;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.joined-subclass table="DYEXTN_LIST_BOX" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class ListBox extends Control implements ListBoxInterface{
	/**
	 * Boolean indicating whether multi selects are allowed in the list box.
	 */
	
	List listOfValues = null;
    /**
     * 
     */
	private Boolean isMultiSelect =null;
    
    /**
     * 
     *
     */
	public ListBox(){

	}
	

    /**
     * @hibernate.property name="isMultiSelect" type="boolean" column="MULTISELECT" 
     * @return Returns the isMultiSelect.
     */
    public Boolean getIsMultiSelect() {
        return isMultiSelect;
    }
    /**
     * @param isMultiSelect The isMultiSelect to set.
     */
    public void setIsMultiSelect(Boolean isMultiSelect) {
        this.isMultiSelect = isMultiSelect;
    }
    /**
     * 
     */
    public String generateHTML()
    {
    	/*String htmlString = "<ul " +
					    	"class = '" + cssClass + "' " +
							"name = '" + name + "' " +
							"id = '" + name + "' " +
							"title = '" + tooltip + "' " 
							+">";
    	if(listOfValues!=null)
		{
			int noOfEltsInList = listOfValues.size();
			String strValue = null;
			for(int i=0;i<noOfEltsInList;i++)
			{
				strValue = (String)listOfValues.get(i);
				if((strValue!=null)&&(strValue.trim()!=""))
				{
					htmlString = htmlString + "<li id='"+ strValue + "'>" + strValue + "</li>";
				}
			}
		}
		htmlString = htmlString + "</ul>";
		System.out.println("Returning " + htmlString);;*/
    	String strMultiSelect = "";
    	if((isMultiSelect!=null)&&(isMultiSelect.booleanValue()==true))
    	{
    		strMultiSelect = "MULTIPLE " ;
    	}
		String htmlString = "<SELECT " + strMultiSelect + 
							" size = 5" + 
							"class = '" + cssClass + "' " +
							"name = '" + name + "' " +
							"id = '" + name + "' " +
							"title = '" + tooltip + "' " 
							+">";
		if(listOfValues!=null)
		{
			int noOfEltsInList = listOfValues.size();
			String strValue = null,isSelected = "";
			for(int i=0;i<noOfEltsInList;i++)
			{
				strValue = (String)listOfValues.get(i);
				if((strValue!=null)&&(strValue.trim()!=""))
				{
					if(strValue.equals(value))
					{
						isSelected = "SELECTED"; 
					}
					else
					{
						isSelected = "";
					}
					htmlString = htmlString + "<OPTION VALUE='"+ strValue + "' " + isSelected+ " >" + strValue + "</OPTION>";
				}
			}
		}
		htmlString = htmlString + "</SELECT>";
		System.out.println("Returning " + htmlString);
		return htmlString;	
	}
    
    /**
     * 
     */
    public void populateAttributes(Map propertiesMap) {
		super.populateAttributes(propertiesMap);
		if(propertiesMap!=null)
		{
			//List Of values initialization
			try {
				//List of values should be a list of String values
				listOfValues  = (List)propertiesMap.get(UIConfigurationConstants.VALUES_LIST);
				System.out.println("List Of values = " + listOfValues);
			} catch (Exception e) {
				e.printStackTrace();
				listOfValues = new ArrayList();
			}
			
			//Is Multiselect initialization
			String strIsMultiselect = (String)propertiesMap.get(UIConfigurationConstants.ISMULTISELECT_ATTRIBUTE);
			if((strIsMultiselect!=null)&&(strIsMultiselect.equalsIgnoreCase("true")))
			{
				isMultiSelect = new Boolean(true);
			}
			else 
			{
				isMultiSelect = new Boolean(false);
			}
		}
	}


	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface#setAttribute(edu.common.dynamicextensions.domaininterface.AttributeInterface)
	 */
	public void setAttribute(AttributeInterface attributeInterface) {
		// TODO Auto-generated method stub
		
	}
}