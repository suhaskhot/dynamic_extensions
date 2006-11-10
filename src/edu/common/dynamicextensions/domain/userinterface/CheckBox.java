
package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CheckBoxInterface;
import edu.common.dynamicextensions.ui.util.ControlsUtility;

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
		String htmlString = null;
		if (this.value == null)
		{
			isChecked = ControlsUtility.getDefaultValue(this.getAbstractAttribute());
		}

		if (isChecked != null && isChecked.equalsIgnoreCase("checked"))
		{
			htmlString = "<input type='checkbox' class = '" + this.cssClass + "' name = '" + getHTMLComponentName() + "' " + "value = \"true\" "
					+ "id = '" + this.name + "' title = '" + this.tooltip + "' " + isChecked + " >";
		}
		else
		{
			htmlString = "<input type='checkbox' class = '" + this.cssClass + "' name = '" + getHTMLComponentName() + "' " + "value = \"true\" "
					+ "id = '" + this.name + "' title = '" + this.tooltip + "'>";
		}
		//System.out.println("Returning " + htmlString);
		return htmlString;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface#setAttribute(edu.common.dynamicextensions.domaininterface.AttributeInterface)
	 */
	public void setAttribute(AbstractAttributeInterface attributeInterface)
	{
	}

}