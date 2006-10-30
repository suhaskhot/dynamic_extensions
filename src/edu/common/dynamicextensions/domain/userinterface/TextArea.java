
package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextAreaInterface;
import edu.common.dynamicextensions.ui.util.ControlMiscellaneous;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:09 PM
 * @hibernate.joined-subclass table="DYEXTN_TEXTAREA" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class TextArea extends Control implements TextAreaInterface
{

	/**
	 * Number of columns in the text area.
	 */
	protected Integer columns;
	/**
	 * Number of rows in the text area.
	 */
	protected Integer rows;

	/**
	 * Empty Constructor 
	 */
	public TextArea()
	{
	}

	/**
	 * @hibernate.property name="columns" type="integer" column="TEXTAREA_COLUMNS" 
	 * @return Returns the columns.
	 */
	public Integer getColumns()
	{
		return columns;
	}

	/**
	 * @param columns The columns to set.
	 */
	public void setColumns(Integer columns)
	{
		this.columns = columns;
	}

	/**
	 * @hibernate.property name="rows" type="integer" column="TEXTAREA_ROWS" 
	 * @return Returns the rows.
	 */
	public Integer getRows()
	{
		return rows;
	}

	/**
	 * @param rows The rows to set.
	 */
	public void setRows(Integer rows)
	{
		this.rows = rows;
	}

	/**
	 * This method generates the HTML code for TextArea control on the HTML form
	 * @return HTML code for TextArea
	 */
	public String generateHTML()
	{
		String defaultValue = this.value;
		if (this.value == null)
		{
			defaultValue = ControlMiscellaneous.getDefaultValue(this.getAbstractAttribute());
		}
		
		String htmlString = "<textarea " + "class = '" + this.cssClass + "' " + "name = '" + getHTMLComponentName() + "' " + "id = '" + getHTMLComponentName() + "' " + "cols = '"
				+ columns.intValue() + "' " + "rows = '" + rows.intValue() + "' " + "title = '" + this.tooltip + "'>";
		htmlString += defaultValue + "</textarea>";
		
		System.out.println("Returning " + htmlString);
		return htmlString;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface#setAttribute(edu.common.dynamicextensions.domaininterface.AttributeInterface)
	 */
	public void setAttribute(AbstractAttributeInterface attributeInterface)
	{
		// TODO Auto-generated method stub

	}

	
}