
package edu.common.dynamicextensions.domain.userinterface;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.RadioButtonInterface;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
import edu.wustl.common.beans.NameValueBean;

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
		List<NameValueBean> nameValueBeanList = null;
		String htmlString = "";

		String defaultValue = this.value;
		if (defaultValue == null)
		{
			defaultValue = ControlsUtility.getDefaultValue(this.getAbstractAttribute());
		}

		nameValueBeanList = ControlsUtility.populateListOfValues((AttributeInterface) this.getAbstractAttribute());

		String htmlComponentName = getHTMLComponentName();
		if (defaultValue != null)
		{
			for (NameValueBean nameValueBean : nameValueBeanList)
			{
				if (nameValueBean.getValue().equals(defaultValue))
				{
					htmlString += "<br><input type='radio' " + "class = '" + cssClass + "' " + "name = '" + htmlComponentName + "' " + "value = '"
							+ nameValueBean.getValue() + "' " + "id = '" + name + "' " + "title = '" + tooltip + "' checked> "
							+ nameValueBean.getName() + "</br>";
				}
				else
				{
					htmlString += "<br><input type='radio' " + "class = '" + cssClass + "' " + "name = '" + htmlComponentName + "' " + "value = '"
							+ nameValueBean.getValue() + "' " + "id = '" + name + "' " + "title = '" + tooltip + "'> " + nameValueBean.getName()
							+ "</br>";
				}
			}
		}

		return htmlString;
	}

	/**
	 * This method sets the corresponding Attribute of this control.
	 * @param abstractAttribute the AbstractAttribute to be set. 
	 */
	public void setAttribute(AbstractAttributeInterface abstractAttribute)
	{
	}

}