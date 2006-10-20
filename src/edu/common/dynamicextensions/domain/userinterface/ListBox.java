package edu.common.dynamicextensions.domain.userinterface;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.userinterface.ListBoxInterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.joined-subclass table="DYEXTN_LIST_BOX" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class ListBox extends Control implements ListBoxInterface
{
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
    
  


	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ListBoxInterface#getChoiceList()
	 */
	public List getChoiceList()
	{
		return listOfValues;
	}


	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ListBoxInterface#setChoiceList(java.util.List)
	 */
	public void setChoiceList(List list)
	{
		listOfValues = list;
	}


	


	
}