
package edu.common.dynamicextensions.domain.userinterface;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ListBoxInterface;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
import edu.wustl.common.beans.NameValueBean;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.joined-subclass table="DYEXTN_LIST_BOX" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class ListBox extends Control implements ListBoxInterface
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Boolean indicating whether multi selects are allowed in the list box.
	 */
	List listOfValues = null;

	/**
	 * 
	 */
	private Boolean isMultiSelect = null;
	
	private Integer noOfRows = null;

	public Integer getNoOfRows()
	{
		return this.noOfRows;
	}

	public void setNoOfRows(Integer noOfRows)
	{
		this.noOfRows = noOfRows;
	}

	/**
	 * Default Constructor
	 */
	public ListBox()
	{
	}

	/**
	 * @hibernate.property name="isMultiSelect" type="boolean" column="MULTISELECT" 
	 * @return Returns the isMultiSelect.
	 */
	public Boolean getIsMultiSelect()
	{
		return isMultiSelect;
	}

	/**
	 * @param isMultiSelect The isMultiSelect to set.
	 */
	public void setIsMultiSelect(Boolean isMultiSelect)
	{
		this.isMultiSelect = isMultiSelect;
	}

	/**
	 * This method generates the HTML code to display the ListBox Control on the form.
	 * @return HTML code for ListBox Control
	 */
	public String generateHTML()
	{
		List<NameValueBean> nameValueBeanList = null;
		String defaultValue = this.value;

		String strMultiSelect = "";
		if ((isMultiSelect != null) && (isMultiSelect.booleanValue() == true))
		{
			strMultiSelect = "MULTIPLE ";
		}
		String htmlString = "<SELECT " + strMultiSelect + " size = 5" + "class = '" + cssClass + "' " + "name = '" + this.name + "' " + "id = '" + name
				+ "' " + "title = '" + tooltip + "' " + ">";

		if (this.value == null)
		{
			defaultValue = ControlsUtility.getDefaultValue(this.getAbstractAttribute());
		}

		if (listOfValues == null)
		{
			nameValueBeanList = ControlsUtility.populateListOfValues((AttributeInterface)this.getAbstractAttribute());
		}

		for (NameValueBean nameValueBean : nameValueBeanList)
		{
			if (nameValueBean.getValue().equals(defaultValue))
			{
				htmlString += "<OPTION VALUE='" + nameValueBean.getValue() + "' SELECTED>" + nameValueBean.getName();
			}
			else
			{
				htmlString += "<OPTION VALUE='" + nameValueBean.getValue() + "'>" + nameValueBean.getName();
			}
		}
		htmlString = htmlString + "</SELECT>";
		
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