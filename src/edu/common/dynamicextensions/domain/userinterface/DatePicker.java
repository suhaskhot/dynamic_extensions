
package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.DatePickerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_DATEPICKER" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class DatePicker extends Control implements DatePickerInterface
{
	String dateValueType = null;

	/**
	 * Empty Constructor
	 */
	public DatePicker()
	{
	}

	/**
	 * This method generates the HTML code for DatePicker control on the HTML form
	 * @return HTML code for DatePicker
	 * @throws DynamicExtensionsSystemException if couldn't genreate the HTML name for the Control.
	 */
	public String generateHTML() throws DynamicExtensionsSystemException
	{
		String defaultValue = (String)this.value;
		if (value == null)
		{
			defaultValue = ControlsUtility.getDefaultValue(this.getAbstractAttribute());
			if (defaultValue == null)
			{
				defaultValue = "";
			}
		}

		String output = "<input class='" + cssClass + "' " + " name='" + getHTMLComponentName() + "'  id='" + getHTMLComponentName() + "' "
				+ " value='" + defaultValue + "'/> " + "<A onclick=\"showCalendar('" + getHTMLComponentName() + "',"
				+ DynamicExtensionsUtility.getCurrentYear() + "," + DynamicExtensionsUtility.getCurrentMonth() + ","
				+ DynamicExtensionsUtility.getCurrentDay() + ",'MM-dd-yyyy','dataEntryForm','" + getHTMLComponentName()
				+ "',event,1900,2020);\" href=\"javascript://\"><IMG alt=\"This is a Calendar\" src=\"images/calendar.gif\" border=0></A>"
				+ "<DIV id=slcalcod" + getHTMLComponentName()
				+ " style=\"Z-INDEX: 10; LEFT: 100px; VISIBILITY: hidden; POSITION: absolute; TOP: 100px\">" + "<SCRIPT>printCalendar('"
				+ getHTMLComponentName() + "'," + DynamicExtensionsUtility.getCurrentDay() + "," + DynamicExtensionsUtility.getCurrentMonth() + ","
				+ DynamicExtensionsUtility.getCurrentYear() + ");</SCRIPT>" + "</DIV>" + "[MM-DD-YYYY]&nbsp;";

		return output;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface#setAttribute(edu.common.dynamicextensions.domaininterface.AttributeInterface)
	 */
	public void setAttribute(AbstractAttributeInterface attributeInterface)
	{
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.DatePickerInterface#getDateValueType()
	 */
	public String getDateValueType()
	{
		return dateValueType;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.DatePickerInterface#setDateValueType(java.lang.String)
	 */
	public void setDateValueType(String dateValueType)
	{
		this.dateValueType = dateValueType;
	}

}