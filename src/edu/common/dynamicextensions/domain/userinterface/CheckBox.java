
package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CheckBoxInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.util.Constants;
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


	protected String generateEditModeHTML() throws DynamicExtensionsSystemException
	{
		String isChecked = (String) this.value;
		String htmlString = "";
		if (this.value == null)
		{
			isChecked = ControlsUtility.getDefaultValue(this.getAbstractAttribute());
		}

		String htmlComponentName = getHTMLComponentName();
		if (isChecked != null && isChecked.equalsIgnoreCase("checked"))
		{
			htmlString = "<input type='checkbox' class = '" + this.cssClass + "' name = '" + htmlComponentName + "' " + "value = \"true\" "
					+ "id = '" + htmlComponentName + "' title = '" + this.tooltip + "' " + isChecked + " >";
		}
		else
		{
			htmlString = "<input type='checkbox' class = '" + this.cssClass + "' name = '" + htmlComponentName + "' " + "value = \"true\" "
					+ "id = '" + htmlComponentName + "' title = '" + this.tooltip + "'>";
		}

		return htmlString;
		
	}
	
	protected String generateViewModeHTML () throws DynamicExtensionsSystemException
	{
		String htmlString = "&nbsp;";
		if (value != null) {
		htmlString = "<span class = '"+ cssClass+ "'> " + this.value.toString() + "</span>";
		}
		return htmlString;
		
	}

	/**
	 * This method sets the corresponding AbstractAttribute of this Control.
	 * @param abstractAttribute AbstractAttribute to be set. 
	 */
	public void setAttribute(AbstractAttributeInterface abstractAttribute)
	{
	}

}