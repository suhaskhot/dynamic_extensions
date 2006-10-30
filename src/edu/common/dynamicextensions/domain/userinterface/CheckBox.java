
package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CheckBoxInterface;
import edu.common.dynamicextensions.ui.util.ControlMiscellaneous;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_CHECK_BOX" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class CheckBox extends Control implements CheckBoxInterface
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Empty Constructor
	 */
	public CheckBox()
	{
	}

	/**
	 * This method generates the HTML code for CheckBox control on the HTML form
	 * @return HTML code for CheckBox
	 */
	public String generateHTML()
	{
		String isChecked = this.value;
		if (this.value == null)
		{
			isChecked = ControlMiscellaneous.getDefaultValue(this.getAbstractAttribute());
		}

		if (isChecked.equalsIgnoreCase("true"))
		{
			isChecked = "checked";
		}

		String htmlString = "<input type='checkbox' class = '" + this.cssClass + "' name = '" + this.name + "' " + "value = '" + this.name
				+ "' id = '" + this.name + "' title = '" + this.tooltip + "' " + isChecked + " >";
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