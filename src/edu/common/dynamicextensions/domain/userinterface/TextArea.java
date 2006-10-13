package edu.common.dynamicextensions.domain.userinterface;

import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextAreaInterface;
import edu.common.dynamicextensions.ui.webui.util.UIConfigurationConstants;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:09 PM
 * @hibernate.joined-subclass table="DYEXTN_TEXTAREA" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class TextArea extends Control implements TextAreaInterface {

	/**
	 * Number of columns in the text area.
	 */
	protected Integer columns;
	/**
	 * Number of rows in the text area.
	 */
	protected Integer rows;
	/**
     * 
	 *
	 */
	public TextArea(){

	}

	/**
	 * @hibernate.property name="columns" type="integer" column="TEXTAREA_COLUMNS" 
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
	 * @hibernate.property name="rows" type="integer" column="TEXTAREA_ROWS" 
	 * @return Returns the rows.
	 */
	public Integer getRows() {
		return rows;
	}
	/**
	 * @param rows The rows to set.
	 */
	public void setRows(Integer rows) {
		this.rows = rows;
	}

    /**
     * 
     */
	public void populateAttributes(Map propertiesMap) {
		super.populateAttributes(propertiesMap);
		if(propertiesMap!=null)
		{
			try {
				String noOfRows = (String)propertiesMap.get(UIConfigurationConstants.NO_OF_ROWS_ATTRIBUTE);
				if(noOfRows!=null)
				{
					rows = new Integer(noOfRows);
				}
				else
				{
					rows = new Integer(UIConfigurationConstants.DEFAULT_NO_OF_ROWS_TEXT);
				}
			} catch (NumberFormatException e) {
				System.out.println("Error while retrieving no Of rows");
				rows = new Integer(UIConfigurationConstants.DEFAULT_NO_OF_ROWS_TEXT);
			}
			try {
				String noOfCols  = (String)propertiesMap.get(UIConfigurationConstants.NO_OF_COLS_ATTRIBUTE);
				if(noOfCols!=null)
				{
					columns = new Integer(noOfCols);
				}
				else
				{
					columns  = new Integer(UIConfigurationConstants.DEFAULT_NO_OF_COLS_TEXT);
				}
			} catch (NumberFormatException e) {
				System.out.println("Error while retrieving no Of columns");
				e.printStackTrace();
				columns  = new Integer(UIConfigurationConstants.DEFAULT_NO_OF_COLS_TEXT);
			}
		}
		else
		{
			rows = new Integer(UIConfigurationConstants.DEFAULT_NO_OF_ROWS_TEXT);
			columns  = new Integer(UIConfigurationConstants.DEFAULT_NO_OF_COLS_TEXT);
		}
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
		String htmlString = "<textarea " +
		"class = '" + cssClass + "' " +
		"name = '" + name + "' " +
		"id = '" + name + "' " +
		"cols = '" + columns.intValue() + "' " +
		"rows = '" + rows.intValue() + "' " +
		"title = '" + tooltip + "' " +
		"value = '" + value + "' " + 
		" >" ;
		//htmlString = htmlString + defaultvalue
		htmlString = htmlString  + "</textarea>";
		System.out.println("Returning " + htmlString);
		return htmlString;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface#setAttribute(edu.common.dynamicextensions.domaininterface.AttributeInterface)
	 */
	public void setAttribute(AbstractAttributeInterface attributeInterface) {
		// TODO Auto-generated method stub
		
	}
}