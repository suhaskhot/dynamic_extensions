
package edu.common.dynamicextensions.domain.userinterface;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.RadioButtonInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
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
	 * @throws DynamicExtensionsSystemException 
	 */
	public String generateHTML() throws DynamicExtensionsSystemException
	{
		List<NameValueBean> nameValueBeanList = null;
		String htmlString = "";

		String defaultValue = (String) this.value;
		if (defaultValue == null)
		{
			defaultValue = ControlsUtility.getDefaultValue(this.getAbstractAttribute());
		}

		nameValueBeanList = ControlsUtility.populateListOfValues(this);

		String htmlComponentName = getHTMLComponentName();
		if (defaultValue != null)
		{
			if (nameValueBeanList != null && nameValueBeanList.size() > 0)
			{
				for (NameValueBean nameValueBean : nameValueBeanList)
				{
					String optionName = nameValueBean.getName();
					if (nameValueBean.getValue().equals(defaultValue))
					{
						htmlString += "<input type='radio' " + "class = '" + cssClass + "' " + "name = '" + htmlComponentName + "' " + "value = '"
								+ nameValueBean.getValue() + "' " + "id = '" + optionName + "' " + "title = '" + tooltip + "' checked> "
								+ "<label for=\"" + optionName + "\">" + optionName + "</label> ";
					}
					else
					{
						htmlString += "<input type='radio' " + "class = '" + cssClass + "' " + "name = '" + htmlComponentName + "' " + "value = '"
								+ nameValueBean.getValue() + "' " + "id = '" + optionName + "' " + "title = '" + tooltip + "'> " + "<label for=\""
								+ optionName + "\">" + optionName + "</label> ";
					}
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