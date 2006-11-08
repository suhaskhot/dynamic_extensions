
package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.RadioButtonInterface;
import edu.common.dynamicextensions.ui.util.ControlsUtility;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:08 PM
 * @hibernate.joined-subclass table="DYEXTN_RADIOBUTTON" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class RadioButton extends Control implements RadioButtonInterface
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Empty Constructor 
	 */
	public RadioButton()
	{
	}

	/**
	 * This method generates the HTML code for RadioButton control on the HTML form
	 * @return HTML code for RadioButton
	 */
	public String generateHTML()
	{
		String isChecked = this.value;
		if (this.value == null)
		{
			isChecked = ControlsUtility.getDefaultValue(this.getAbstractAttribute());
		}

		if (isChecked.equalsIgnoreCase("true"))
		{
			isChecked = "checked";
		}
		String htmlString = "<input type='radio' " + "class = '" + cssClass + "' " + "name = '" + getHTMLComponentName() + "' " + "value = '" + name + "' " + "id = '"
				+ name + "' " + "title = '" + tooltip + "' " + isChecked + " >";
		
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