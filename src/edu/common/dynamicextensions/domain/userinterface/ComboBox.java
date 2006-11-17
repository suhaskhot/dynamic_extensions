
package edu.common.dynamicextensions.domain.userinterface;

import java.util.List;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ComboBoxInterface;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
import edu.wustl.common.beans.NameValueBean;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_COMBOBOX" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class ComboBox extends Control implements ComboBoxInterface
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3062212342005513616L;

	/**
	 * 
	 */
	List listOfValues = null;

	/**
	 * Empty Constructor
	 */
	public ComboBox()
	{
	}

	/**
	 * This method generates the HTML code for ComboxBox control on the HTML form
	 * @return HTML code for ComboBox
	 */
	public String generateHTML()
	{
		List<NameValueBean> nameValueBeanList = null;
		String defaultValue = this.value;

		String htmlString = "<SELECT class = '" + cssClass + "' name = '" + getHTMLComponentName() + "' " + "id = '" + getHTMLComponentName()
				+ "' title = '" + tooltip + "'>";

		if (this.value == null)
		{
			defaultValue = ControlsUtility.getDefaultValue(this.getAbstractAttribute());
		}

		if (listOfValues == null)
		{
			nameValueBeanList = ControlsUtility.populateListOfValues((AttributeInterface) this.getAbstractAttribute());
		}

		for (NameValueBean nameValueBean : nameValueBeanList)
		{
			if (nameValueBean.getValue().equals(defaultValue))
			{
				htmlString += "<OPTION VALUE='" + nameValueBean.getValue() + "' SELECTED>" + nameValueBean.getName();
			}
			else
			{
				htmlString += "<OPTION VALUE='" + nameValueBean.getValue() + "'>" + nameValueBean.getName();
			}
		}
		htmlString = htmlString + "</SELECT>";

		System.out.println("Returning " + htmlString);
		return htmlString;
	}

	/**
	 * 
	 */
	public List getChoiceList()
	{
		return listOfValues;
	}

	/**
	 * 
	 */
	public void setChoiceList(List list)
	{
		listOfValues = list;
	}

}