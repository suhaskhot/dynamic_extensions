
package edu.common.dynamicextensions.domain.userinterface;

import edu.common.dynamicextensions.domain.DoubleAttributeTypeInformation;
import edu.common.dynamicextensions.domain.LongAttributeTypeInformation;
import edu.common.dynamicextensions.domain.StringAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextFieldInterface;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;

/**
 * This Class represents the TextField (TextBox) of the HTML page. 
 * @version 1.0
 * @created 28-Sep-2006 12:20:09 PM
 * @hibernate.joined-subclass table="DYEXTN_TEXTFIELD" 
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class TextField extends Control implements TextFieldInterface
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Size of the text field to be shown on UI.
	 */
	protected Integer columns;

	/**
	 * Boolean value indicating whether this text field is password field.
	 */
	protected Boolean isPassword;

	/**
	 * Empty Constructor 
	 */
	public TextField()
	{
	}

	/**
	 * @hibernate.property name="columns" type="integer" column="NO_OF_COLUMNS" 
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
	 * @hibernate.property name="isPassword" type="boolean" column="IS_PASSWORD" 
	 * @return Returns the isPassword.
	 */
	public Boolean getIsPassword()
	{
		return isPassword;
	}

	/**
	 * @param isPassword The isPassword to set.
	 */
	public void setIsPassword(Boolean isPassword)
	{
		this.isPassword = isPassword;
	}

	/**
	 * This method generates the HTML code for TextField control on the HTML form
	 * @return HTML code for TextField
	 */
	/**
	 * This method generates the HTML code for TextField control on the HTML form
	 * @return HTML code for TextField
	 */
	public String generateHTML()
	{
		String defaultValue = (String) this.value;
		if (this.value == null)
		{
			defaultValue = ControlsUtility.getDefaultValue(this.getAbstractAttribute());
			if (defaultValue == null || (defaultValue.length() == 0))
			{
				defaultValue = "";
			}
		}

		String htmlComponentName = getHTMLComponentName();
		String htmlString = "<input " + "class = '" + cssClass + "' " + "name = '" + htmlComponentName + "' " + "id = '" + htmlComponentName + "' "
				+ "title = '" + tooltip + "'  " + "value = '" + defaultValue + "' " + "size = '" + columns.intValue() + "' ";

		String measurementUnit = getMeasurementUnit(this.getAbstractAttribute());
		if (measurementUnit != null)
		{
			htmlString += "&nbps;" + measurementUnit;
		}

		if (isPassword != null && isPassword.booleanValue())
		{
			htmlString = htmlString + " type='password' ";
		}
		else
		{
			htmlString = htmlString + " type='text' ";
		}

		int maxChars = 0;
		AttributeInterface attribute = (AttributeInterface) this.getAbstractAttribute();
		if (attribute != null)
		{
			AttributeTypeInformationInterface attributeTypeInformationInterface = attribute.getAttributeTypeInformation();
			if (attributeTypeInformationInterface != null)
			{
				if (attributeTypeInformationInterface instanceof StringAttributeTypeInformation)
				{
					StringAttributeTypeInformation stringAttributeTypeInformation = (StringAttributeTypeInformation) attributeTypeInformationInterface;
					if (stringAttributeTypeInformation != null)
					{
						maxChars = stringAttributeTypeInformation.getSize().intValue();
					}
				}
				/*else if (attributeTypeInformationInterface instanceof LongAttributeTypeInformation)
				{
					LongAttributeTypeInformation longAttributeTypeInformation = (LongAttributeTypeInformation)attributeTypeInformationInterface;
					

				}
				else if (attributeTypeInformationInterface instanceof DoubleAttributeTypeInformation)
				{
					DoubleAttributeTypeInformation doubleAttributeTypeInformation = (DoubleAttributeTypeInformation)attributeTypeInformationInterface;

				}*/
			}
		}
		if (maxChars != 0)
		{
			htmlString += "maxlength='" + maxChars + "'>";
		}
		else
		{
			htmlString += "/>";
		}
		return htmlString;
	}
	/**
	 * This method sets the associated AbstractAttribute of this Control. 
	 * @param abstractAttributeInterface AbstractAttribute to be associated.
	 */
	public void setAttribute(AbstractAttributeInterface abstractAttributeInterface)
	{
	}

	/**
	 * This method returns the measurement units of the numeric attribute associated with this Control.
	 * @param abstractAttribute AbstractAttribute whose measurement units are to be known.
	 * @return the measurement units of the numeric attribute associated with this Control.
	 */
	private String getMeasurementUnit(AbstractAttributeInterface abstractAttribute)
	{
		String measurementUnit = null;
		AttributeTypeInformationInterface attributeTypeInformationInterface = DynamicExtensionsUtility.getAttributeTypeInformation(abstractAttribute);
		if(attributeTypeInformationInterface!=null)
		{
			if (attributeTypeInformationInterface instanceof LongAttributeTypeInformation)
			{
				LongAttributeTypeInformation longAttribute = (LongAttributeTypeInformation) attributeTypeInformationInterface;
				measurementUnit = longAttribute.getMeasurementUnits();
			}
			else if (attributeTypeInformationInterface instanceof DoubleAttributeTypeInformation)
			{
				DoubleAttributeTypeInformation doubleAttribute = (DoubleAttributeTypeInformation) attributeTypeInformationInterface;
				measurementUnit = doubleAttribute.getMeasurementUnits();
			}
		}
		return measurementUnit;
	}
}