package edu.common.dynamicextensions.domain.userinterface;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.userinterface.ComboBoxInterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_COMBOBOX" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class ComboBox extends Control implements ComboBoxInterface
{

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
	public String generateHTML()
	{
		String htmlString = "<SELECT " +
							"class = '" + cssClass + "' " +
							"name = '" + getHTMLComponentName() + "' " +
							"id = '" + getHTMLComponentName() + "' " +
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