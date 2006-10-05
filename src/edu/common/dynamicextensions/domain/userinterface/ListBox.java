package edu.common.dynamicextensions.domain.userinterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.joined-subclass table="DYEXTN_LIST_BOX" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class ListBox extends Control {
	/**
	 * Boolean indicating whether multi selects are allowed in the list box.
	 */
	
	List listOfValues = null;
	private Boolean isMultiSelect =null;

	public ListBox(){

	}

	public void finalize() throws Throwable {
		super.finalize();
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
    
    public String generateHTML()
    {
    	String htmlString = "<ul " +
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
		System.out.println("Returning " + htmlString);;
    	return htmlString;
    }
    
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
}