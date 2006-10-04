package edu.common.dynamicextensions.domain.userinterface;

import java.util.Map;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:09 PM
 * @hibernate.joined-subclass table="DYEXTN_TEXT_FIELD" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class TextField extends Control {
	/**
	 * Size of the text field to be shown on UI.
	 */
	protected Integer columns;
	/**
	 * Boolean value indicating whether this text field is password field.
	 */
	protected Boolean isPassword;

	public TextField(){

	}

	public void finalize() throws Throwable {
		super.finalize();
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

	public void populateAttribute(Map propertiesMap) {
		super.populateAttribute(propertiesMap);
		if(propertiesMap!=null)
		{
			try {
				columns = new Integer((String)propertiesMap.get(UIConfigurationConstants.NO_OF_COLS_ATTRIBUTE));
			} catch (NumberFormatException e) {
				System.out.println("Error while retrieving no Of columns");
				columns  = new Integer(UIConfigurationConstants.DEFAULT_NO_OF_COLS_TEXT);
			}
			String strIsPassword  = (String)propertiesMap.get(UIConfigurationConstants.ISPASSWORD_ATTRIBUTE);
			if((strIsPassword!=null)&&(strIsPassword.equalsIgnoreCase("true")))
			{
				isPassword = new Boolean(true);
			}
			else
			{
				isPassword = new Boolean(false);
			}

		}
		else
		{
			columns  = new Integer(UIConfigurationConstants.DEFAULT_NO_OF_COLS_TEXT);
			isPassword = new Boolean(false);
		}
	}

	public String generateHTML()
	{
		String htmlString = "<input " + 
							"class = '" + cssClass + "' " +
							"name = '" + name + "' " +
							"id = '" + name + "' " +
							"title = '" + tooltip + "'  ";
		if(isPassword.booleanValue()==true)
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
}