
package edu.common.dynamicextensions.domain.userinterface;

import java.util.Date;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.DatePickerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.common.util.Utility;

/**
 * @version 1.0
 * @created 28-Sep-2006 12:20:07 PM
 * @hibernate.joined-subclass table="DYEXTN_DATEPICKER" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class DatePicker extends Control implements DatePickerInterface
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
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
	protected String generateEditModeHTML() throws DynamicExtensionsSystemException
	{
		AttributeTypeInformationInterface attributeTypeInformationInterface = ((AttributeMetadataInterface) this.getBaseAbstractAttribute())
				.getAttributeTypeInformation();
		String dateFormat = ControlsUtility.getDateFormat(attributeTypeInformationInterface);

		String defaultValue = (String) this.value;
		if (value == null)
		{
			defaultValue = this.getAttibuteMetadataInterface().getDefaultValue();
			if (defaultValue == null)
			{
				if (this.getDateValueType() != null && this.getDateValueType().equals((ProcessorConstants.DATE_VALUE_TODAY)))
				{
					defaultValue = Utility.parseDateToString(new Date(), dateFormat);
				}
				else
				{
					defaultValue = "";
				}
			}
		}

		String htmlComponentName = getHTMLComponentName();
		String output = null;

		if (dateFormat.equals(ProcessorConstants.DATE_ONLY_FORMAT))
		{
			output = "<input class='font_bl_nor' name='"
					+ htmlComponentName
					+ "' id='"
					+ htmlComponentName
					+ "' value='"
					+ defaultValue
					+ "'"
					+ ((this.isReadOnly != null && this.isReadOnly) ? " disabled='" + ProcessorConstants.TRUE : "")
					+ "/>"
					+ "<A onclick=\"showCalendar('"
					+ htmlComponentName
					+ "', "
					+ DynamicExtensionsUtility.getCurrentYear()
					+ ", "
					+ DynamicExtensionsUtility.getCurrentMonth()
					+ ", "
					+ DynamicExtensionsUtility.getCurrentDay()
					+ ", 'MM-dd-yyyy', 'dataEntryForm', '"
					+ htmlComponentName
					+ "', event, 1900, 2020);\" href=\"javascript://\">"
					+ "&nbsp;<IMG alt=\"This is a Calendar\" src=\"images/calendar.gif\" border=0 />&nbsp;<span class='font_gr_s'>[MM-DD-YYYY]</span></A>"
					+ "<DIV id=slcalcod" + htmlComponentName
					+ " style=\"Z-INDEX: 10; LEFT: 100px; VISIBILITY: hidden; POSITION: absolute; TOP: 100px\">";
			output += "<SCRIPT>printCalendar('" + htmlComponentName + "'," + DynamicExtensionsUtility.getCurrentDay() + ","
					+ DynamicExtensionsUtility.getCurrentMonth() + "," + DynamicExtensionsUtility.getCurrentYear() + ");</SCRIPT>" + "</DIV>";
		}
		else if (dateFormat.equals(ProcessorConstants.DATE_TIME_FORMAT))
		{
			output = "<input class='font_bl_nor' name='"
					+ htmlComponentName
					+ "' id='"
					+ htmlComponentName
					+ "' value='"
					+ defaultValue
					+ ((this.isReadOnly != null && this.isReadOnly) ? " disabled='" + ProcessorConstants.TRUE : "")
					+ "/>"
					+ "<A onclick=\"showCalendar('"
					+ htmlComponentName
					+ "', "
					+ DynamicExtensionsUtility.getCurrentYear()
					+ ", "
					+ DynamicExtensionsUtility.getCurrentMonth()
					+ ", "
					+ DynamicExtensionsUtility.getCurrentDay()
					+ ", 'MM-dd-yyyy', 'dataEntryForm', '"
					+ htmlComponentName
					+ "', event, 1900, 2020);\" href=\"javascript://\">"
					+ "&nbsp;<IMG alt=\"This is a Calendar\" src=\"images/calendar.gif\" border=0 />&nbsp;<span class='font_gr_s'>[MM-DD-YYYY HH:MM]</span></A>"
					+ "<DIV id=slcalcod" + htmlComponentName
					+ " style=\"Z-INDEX: 10; LEFT: 100px; VISIBILITY: hidden; POSITION: absolute; TOP: 100px\">" + "<SCRIPT>printTimeCalendar('"
					+ htmlComponentName + "'," + DynamicExtensionsUtility.getCurrentDay() + "," + DynamicExtensionsUtility.getCurrentMonth() + ","
					+ DynamicExtensionsUtility.getCurrentYear() + "," + DynamicExtensionsUtility.getCurrentHours() + ","
					+ DynamicExtensionsUtility.getCurrentMinutes() + ");</SCRIPT>" + "</DIV>";
		}
		else if (dateFormat.equals(ProcessorConstants.MONTH_YEAR_FORMAT))
		{
			output = "<input class='font_bl_nor' name='"
					+ htmlComponentName
					+ "' id='"
					+ htmlComponentName
					+ "' value='"
					+ defaultValue
					+ "'"
					+ ((this.isReadOnly != null && this.isReadOnly) ? " disabled='" + ProcessorConstants.TRUE : "")
					+ "/>"
					+ "<A onclick=\"showCalendar('"
					+ htmlComponentName
					+ "', "
					+ DynamicExtensionsUtility.getCurrentYear()
					+ ", "
					+ DynamicExtensionsUtility.getCurrentMonth()
					+ ", "
					+ 0
					+ ", 'MM-yyyy', 'dataEntryForm', '"
					+ htmlComponentName
					+ "', event, 1900, 2020);\" href=\"javascript://\">&nbsp;<IMG alt=\"This is a Calendar\" src=\"images/calendar.gif\" border=0 />&nbsp;<span class='font_gr_s'>[MM-YYYY]</span></A>"
					+ "<DIV id=slcalcod" + htmlComponentName
					+ " style=\"Z-INDEX: 10; LEFT: 100px; VISIBILITY: hidden; POSITION: absolute; TOP: 100px\">" + "<SCRIPT>printMonthYearCalendar('"
					+ htmlComponentName + "'," + DynamicExtensionsUtility.getCurrentMonth() + "," + DynamicExtensionsUtility.getCurrentYear()
					+ ");</SCRIPT>" + "</DIV>";
		}
		else if (dateFormat.equals(ProcessorConstants.YEAR_ONLY_FORMAT))
		{

			output = "<input class='font_bl_nor' name='"
					+ htmlComponentName
					+ "' id='"
					+ htmlComponentName
					+ "' value='"
					+ defaultValue
					+ "'"
					+ ((this.isReadOnly != null && this.isReadOnly) ? " disabled='" + ProcessorConstants.TRUE : "")
					+ "/>"
					+ "<A onclick=\"showCalendar('"
					+ htmlComponentName
					+ "', "
					+ DynamicExtensionsUtility.getCurrentYear()
					+ ", "
					+ 0
					+ ", "
					+ 0
					+ ", 'yyyy', 'dataEntryForm', '"
					+ htmlComponentName
					+ "', event, 1900, 2020);\" href=\"javascript://\">&nbsp;<IMG alt=\"This is a Calendar\" src=\"images/calendar.gif\" border=0 />&nbsp;<span class='font_gr_s'>[YYYY]</span></A>"
					+ "<DIV id=slcalcod" + htmlComponentName
					+ " style=\"Z-INDEX: 10; LEFT: 100px; VISIBILITY: hidden; POSITION: absolute; TOP: 100px\">";
			output += "<SCRIPT>printYearCalendar('" + htmlComponentName + "'," + DynamicExtensionsUtility.getCurrentYear() + ");</SCRIPT>" + "</DIV>";
		}

		return output;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface#setAttribute(edu.common.dynamicextensions.domaininterface.AttributeInterface)
	 */
	public void setAttribute(AbstractAttributeInterface attributeInterface)
	{
	}

	/**This method returns the dateValueType of the DatePicker.
	* @hibernate.property name="dateValueType" type="string" column="DATE_VALUE_TYPE"
	* @return Returns the dateValueType.
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

	protected String generateViewModeHTML() throws DynamicExtensionsSystemException
	{
		String htmlString = "";
		if (value != null)
		{
			htmlString = "<span class = 'font_bl_nor'> " + this.value.toString() + "</span>";
		}

		return htmlString;
	}

}