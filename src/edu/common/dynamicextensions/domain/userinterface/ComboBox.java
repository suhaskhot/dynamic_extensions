package edu.common.dynamicextensions.domain.userinterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.userinterface.ComboBoxInterface;
import edu.common.dynamicextensions.ui.webui.util.UIConfigurationConstants;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_COMBOBOX" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class ComboBox extends Control implements ComboBoxInterface{

	List listOfValues = null;
    
    /**
     * 
     *
     */

	public ComboBox(){

	}

    /**
     * 
     */
	public void populateAttributes(Map propertiesMap) {
		super.populateAttributes(propertiesMap);
		if(propertiesMap!=null)
		{
			try {
				//List of values should be a list of String values
				listOfValues  = (List)propertiesMap.get(UIConfigurationConstants.VALUES_LIST);
				System.out.println("List Of values = " + listOfValues);
			} catch (Exception e) {
				e.printStackTrace();
				listOfValues = new ArrayList();
			}
		}
	}

    /**
     * 
     */
	public String generateHTML()
	{
		String htmlString = "<SELECT " +
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

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ComboBoxInterface#getChoiceList()
	 */
	public List getChoiceList()
	{
		return listOfValues;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ComboBoxInterface#setChoiceList(java.util.List)
	 */
	public void setChoiceList(List list)
	{
		listOfValues = list;
		
	}

	

}