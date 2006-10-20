package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextFieldInterface;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:09 PM
 * @hibernate.joined-subclass table="DYEXTN_TEXTFIELD" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class TextField extends Control implements TextFieldInterface
{
	/**
	 * Size of the text field to be shown on UI.
	 */
	protected Integer columns;
	/**
	 * Boolean value indicating whether this text field is password field.
	 */
	protected Boolean isPassword;
    
    /**
     * 
     *
     */

	public TextField(){

	}

	/**
	 * @hibernate.property name="columns" type="integer" column="COLUMNS" 
	 * @return Returns the columns.
	 */
	public Integer getColumns() {
		return columns;
	}
	/**
	 * @param columns The columns to set.
	 */
	public void setColumns(Integer columns) {
		this.columns = columns;
	}
	/**
	 * @hibernate.property name="isPassword" type="boolean" column="PASSWORD" 
	 * @return Returns the isPassword.
	 */
	public Boolean getIsPassword() {
		return isPassword;
	}
	/**
	 * @param isPassword The isPassword to set.
	 */
	public void setIsPassword(Boolean isPassword) {
		this.isPassword = isPassword;
	}

  
    /**
     * 
     */
	public String generateHTML()
	{
		if(value==null)
		{
			value = "";
		}
		//int maxChars = 0;	//Need to be filled from size value of attribute
		
		String htmlString = "<input " + 
							"class = '" + cssClass + "' " +
							"name = '" + getHTMLComponentName() + "' " +
							"id = '" + getHTMLComponentName() + "' " +
							"title = '" + tooltip + "'  " +
							"value = '" + value + "' "  +
							"size = '" + columns.intValue() + "' " ;	//Width of Input Fld  
							//"maxlength = '" + maxChars + "' ";
		if(isPassword != null && isPassword.booleanValue() == true)
		{
			htmlString  = htmlString + " type='password' " ;
		}
		else
		{
			htmlString  = htmlString + " type='text' " ;
		}
		htmlString = htmlString + ">";
		System.out.println("Returning " + htmlString);
		return htmlString;	
	}
    
    
	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface#setAttribute(edu.common.dynamicextensions.domaininterface.AttributeInterface)
	 */
	public void setAttribute(AbstractAttributeInterface attributeInterface)
    {
	}

	
}