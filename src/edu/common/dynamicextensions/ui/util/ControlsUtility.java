
package edu.common.dynamicextensions.ui.util;

/**
 * This class defines miscellaneous methods that are commonly used by many Control objects. * 
 * @author chetan_patil
 */
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.BooleanTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.BooleanValueInterface;
import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.common.dynamicextensions.domaininterface.DateTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.DateValueInterface;
import edu.common.dynamicextensions.domaininterface.DoubleTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.DoubleValueInterface;
import edu.common.dynamicextensions.domaininterface.FloatTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.FloatValueInterface;
import edu.common.dynamicextensions.domaininterface.IntegerTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.IntegerValueInterface;
import edu.common.dynamicextensions.domaininterface.LongTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.LongValueInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.ShortTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.ShortValueInterface;
import edu.common.dynamicextensions.domaininterface.StringTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.StringValueInterface;
import edu.common.dynamicextensions.domaininterface.UserDefinedDEInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.wustl.common.beans.NameValueBean;

public class ControlsUtility
{
	/**
	 * This method returns the default value of the PrimitiveAttribute for displaying in corresponding controls on UI. 
	 * @param abstractAttribute the PrimitiveAttribute
	 * @return the Default Value of the PrimitiveAttribute
	 */
	public static String getDefaultValue(AbstractAttributeInterface abstractAttribute)
	{
		String defaultValue = null;
		AttributeTypeInformationInterface abstractAttributeType = null;

		if (abstractAttribute != null)
		{
			abstractAttributeType = ((AttributeInterface) abstractAttribute).getAttributeTypeInformation();
		}
		if (abstractAttributeType != null)
		{
			if (abstractAttributeType instanceof StringTypeInformationInterface)
			{
				StringTypeInformationInterface stringAttribute = (StringTypeInformationInterface) abstractAttributeType;
				if (stringAttribute != null)
				{
					StringValueInterface stringValue = (StringValueInterface) stringAttribute.getDefaultValue();
					if (stringValue != null)
					{
						defaultValue = stringValue.getValue();
					}
				}
			}
			else if (abstractAttributeType instanceof BooleanTypeInformationInterface)
			{
				BooleanTypeInformationInterface booleanAttribute = (BooleanTypeInformationInterface) abstractAttributeType;
				if (booleanAttribute != null)
				{
					BooleanValueInterface booleanValue = (BooleanValueInterface) booleanAttribute.getDefaultValue();
					if (booleanValue != null)
					{
						Boolean defaultBoolean = booleanValue.getValue();
						if (defaultBoolean != null)
						{
							defaultValue = defaultBoolean.toString();
						}
					}
				}
			}
			else if (abstractAttributeType instanceof IntegerTypeInformationInterface)
			{
				IntegerTypeInformationInterface integerAttribute = (IntegerTypeInformationInterface) abstractAttributeType;
				if (integerAttribute != null)
				{
					IntegerValueInterface integerValue = (IntegerValueInterface) integerAttribute.getDefaultValue();
					if (integerValue != null)
					{
						Integer defaultInteger = integerValue.getValue();
						if (defaultInteger != null)
						{
							defaultValue = defaultInteger.toString();
						}
					}
				}
			}
			else if (abstractAttributeType instanceof LongTypeInformationInterface)
			{
				LongTypeInformationInterface longAttribute = (LongTypeInformationInterface) abstractAttributeType;
				if (longAttribute != null)
				{
					LongValueInterface longValue = (LongValueInterface) longAttribute.getDefaultValue();
					if (longValue != null)
					{
						Long defaultLong = longValue.getValue();
						if (defaultLong != null)
						{
							defaultValue = defaultLong.toString();
						}
					}
				}
			}
			else if (abstractAttributeType instanceof DoubleTypeInformationInterface)
			{
				DoubleTypeInformationInterface doubleAttribute = (DoubleTypeInformationInterface) abstractAttributeType;
				if (doubleAttribute != null)
				{
					DoubleValueInterface doubleValue = (DoubleValueInterface) doubleAttribute.getDefaultValue();
					if (doubleValue != null)
					{
						Double defaultDouble = doubleValue.getValue();
						if (defaultDouble != null)
						{
							defaultValue = defaultDouble.toString();
						}
					}
				}
			}
			else if (abstractAttributeType instanceof FloatTypeInformationInterface)
			{
				FloatTypeInformationInterface floatAttribute = (FloatTypeInformationInterface) abstractAttributeType;
				if (floatAttribute != null)
				{
					FloatValueInterface floatValue = (FloatValueInterface) floatAttribute.getDefaultValue();
					if (floatValue != null)
					{
						Float defaultFloat = floatValue.getValue();
						if (defaultFloat != null)
						{
							defaultValue = defaultFloat.toString();
						}
					}
				}
			}
			else if (abstractAttributeType instanceof ShortTypeInformationInterface)
			{
				ShortTypeInformationInterface shortAttribute = (ShortTypeInformationInterface) abstractAttributeType;
				if (shortAttribute != null)
				{
					ShortValueInterface shortValue = (ShortValueInterface) shortAttribute.getDefaultValue();
					if (shortValue != null)
					{
						Short defaultShort = shortValue.getValue();
						if (defaultShort != null)
						{
							defaultValue = defaultShort.toString();
						}
					}
				}
			}
			else if (abstractAttributeType instanceof DateTypeInformationInterface)
			{
				DateTypeInformationInterface dateAttribute = (DateTypeInformationInterface) abstractAttributeType;
				if (dateAttribute != null)
				{
					DateValueInterface dateValue = (DateValueInterface) dateAttribute.getDefaultValue();
					if (dateValue != null)
					{
						Date defaultDate = dateValue.getValue();
						if (defaultDate != null)
						{
							defaultValue = new SimpleDateFormat(getDateFormat(dateAttribute)).format(defaultDate);
						}
					}
				}
			}
		}
		return defaultValue;
	}

	/**
	 * This method returns the prescribed date format for the given DateAttributeTypeInformation
	 * @param attribute the DateAttributeTypeInformation
	 * @return the date format String
	 */
	public static String getDateFormat(AttributeTypeInformationInterface dateAttribute)
	{
		String dateFormat = ((DateTypeInformationInterface) dateAttribute).getFormat();
		if (dateFormat == null)
		{
			dateFormat = "";
		}
		return dateFormat;
	}

	/**
	 * This method populates the List of Values of the ListBox in the NameValueBean Collection.
	 * @return List of pair of Name and its corresponding Value.
	 */
	public static List<NameValueBean> populateListOfValues(AttributeInterface attribute)
	{
		List<NameValueBean> nameValueBeanList = null;
		NameValueBean nameValueBean = null;
		DataElementInterface dataElement = attribute.getAttributeTypeInformation().getDataElement();

		if (dataElement != null)
		{
			if (dataElement instanceof UserDefinedDEInterface)
			{
				Collection<PermissibleValueInterface> permissibleValueCollection = ((UserDefinedDEInterface) dataElement)
						.getPermissibleValueCollection();
				if (permissibleValueCollection != null)
				{
					nameValueBeanList = new Vector<NameValueBean>();
					for (PermissibleValueInterface permissibleValue : permissibleValueCollection)
					{
						if (permissibleValue instanceof StringValueInterface)
						{
							nameValueBean = getPermissibleStringValue(permissibleValue);
						}
						else if (permissibleValue instanceof DateValueInterface)
						{
							DateTypeInformationInterface dateAttribute = (DateTypeInformationInterface) attribute;
							nameValueBean = getPermissibleDateValue(permissibleValue, dateAttribute);
						}
						else if (permissibleValue instanceof DoubleValueInterface)
						{
							nameValueBean = getPermissibleDoubleValue(permissibleValue);
						}
						else if (permissibleValue instanceof FloatValueInterface)
						{
							nameValueBean = getPermissibleFloatValue(permissibleValue);
						}
						else if (permissibleValue instanceof LongValueInterface)
						{
							nameValueBean = getPermissibleLongValue(permissibleValue);
						}
						else if (permissibleValue instanceof IntegerValueInterface)
						{
							nameValueBean = getPermissibleIntegerValue(permissibleValue);
						}
						else if (permissibleValue instanceof ShortValueInterface)
						{
							nameValueBean = getPermissibleShortValue(permissibleValue);
						}
						else if (permissibleValue instanceof BooleanValueInterface)
						{
							nameValueBean = getPermissibleBooleanValue(permissibleValue);
						}
						nameValueBeanList.add(nameValueBean);
					}
				}
			}
		}
		return nameValueBeanList;
	}

	private static NameValueBean getPermissibleDateValue(PermissibleValueInterface permissibleValue, DateTypeInformationInterface dateAttribute)
	{
		DateValueInterface dateValue = (DateValueInterface) permissibleValue;
		NameValueBean nameValueBean = null;

		if (dateValue != null && dateValue.getValue() != null)
		{
			nameValueBean = new NameValueBean();
			String date = new SimpleDateFormat(getDateFormat(dateAttribute)).format(dateValue.getValue());
			nameValueBean.setName(date);
			nameValueBean.setValue(date);
		}
		return nameValueBean;
	}

	private static NameValueBean getPermissibleDoubleValue(PermissibleValueInterface permissibleValue)
	{
		DoubleValueInterface doubleValue = (DoubleValueInterface) permissibleValue;
		NameValueBean nameValueBean = null;

		if (doubleValue != null && doubleValue.getValue() != null)
		{
			nameValueBean = new NameValueBean();
			nameValueBean.setName(doubleValue.getValue().doubleValue());
			nameValueBean.setValue(doubleValue.getValue().doubleValue());
		}

		return nameValueBean;
	}

	private static NameValueBean getPermissibleFloatValue(PermissibleValueInterface permissibleValue)
	{
		FloatValueInterface floatValue = (FloatValueInterface) permissibleValue;
		NameValueBean nameValueBean = null;

		if (floatValue != null & floatValue.getValue() != null)
		{
			nameValueBean = new NameValueBean();
			nameValueBean.setName(floatValue.getValue().floatValue());
			nameValueBean.setValue(floatValue.getValue().floatValue());
		}
		return nameValueBean;
	}

	private static NameValueBean getPermissibleLongValue(PermissibleValueInterface permissibleValue)
	{
		LongValueInterface longValue = (LongValueInterface) permissibleValue;
		NameValueBean nameValueBean = null;

		if (longValue != null && longValue.getValue() != null)
		{
			nameValueBean = new NameValueBean();
			nameValueBean.setName(longValue.getValue().longValue());
			nameValueBean.setValue(longValue.getValue().longValue());
		}
		return nameValueBean;
	}

	private static NameValueBean getPermissibleIntegerValue(PermissibleValueInterface permissibleValue)
	{
		IntegerValueInterface integerValue = (IntegerValueInterface) permissibleValue;
		NameValueBean nameValueBean = null;

		if (integerValue != null && integerValue.getValue() != null)
		{
			nameValueBean = new NameValueBean();
			nameValueBean.setName(integerValue.getValue().intValue());
			nameValueBean.setValue(integerValue.getValue().intValue());
		}
		return nameValueBean;
	}

	private static NameValueBean getPermissibleShortValue(PermissibleValueInterface permissibleValue)
	{
		ShortValueInterface shortValue = (ShortValueInterface) permissibleValue;
		NameValueBean nameValueBean = null;

		if (shortValue != null && shortValue.getValue() != null)
		{
			nameValueBean = new NameValueBean();
			nameValueBean.setName(shortValue.getValue().shortValue());
			nameValueBean.setValue(shortValue.getValue().shortValue());
		}
		return nameValueBean;
	}

	private static NameValueBean getPermissibleBooleanValue(PermissibleValueInterface permissibleValue)
	{
		BooleanValueInterface booleanValue = (BooleanValueInterface) permissibleValue;
		NameValueBean nameValueBean = null;

		if (booleanValue != null && booleanValue.getValue() != null)
		{
			nameValueBean = new NameValueBean();
			nameValueBean.setName(booleanValue.getValue().booleanValue());
			nameValueBean.setValue(booleanValue.getValue().booleanValue());
		}
		return nameValueBean;
	}

	private static NameValueBean getPermissibleStringValue(PermissibleValueInterface permissibleValue)
	{
		StringValueInterface stringValue = (StringValueInterface) permissibleValue;
		NameValueBean nameValueBean = null;

		if (stringValue != null && stringValue.getValue() != null)
		{
			nameValueBean = new NameValueBean();
			nameValueBean.setName(stringValue.getValue().trim());
			nameValueBean.setValue(stringValue.getValue().trim());
		}
		return nameValueBean;
	}


	/**
	 * 
	 * @param entityInterface
	 * @param sequenceNumbers
	 */
	public static void applySequenceNumbers(ContainerInterface containerInterface, String[] sequenceNumbers)
	{
		Collection controlCollection = containerInterface.getControlCollection();
		ControlInterface controlInterface;
		int sequenceNumber;
		if (controlCollection != null && controlCollection.size() > 0 && sequenceNumbers != null && sequenceNumbers.length > 0)
		{
			for (int counter = 0; counter < sequenceNumbers.length - 1; counter++)
			{
				sequenceNumber = new Integer(sequenceNumbers[counter]).intValue();
				controlInterface = DynamicExtensionsUtility.getControlBySequenceNumber(controlCollection, sequenceNumber);
				controlInterface.setSequenceNumber(new Integer(counter + 1));
			}
			deleteControls(containerInterface, sequenceNumbers.length);
			DynamicExtensionsUtility.resetSequenceNumberChanged(controlCollection);
		}
	}

	/**
	 * 
	 * @param controlCollection
	 */
	public static  void deleteControls(ContainerInterface containerInterface, int sequenceNumbersCount)
	{
		Collection controlCollection = containerInterface.getControlCollection();
		if (sequenceNumbersCount == 1)
		{
			containerInterface.getControlCollection().retainAll(new HashSet());

		}
		else
		{
			Iterator controlIterator = controlCollection.iterator();
			ControlInterface controlInterface;
			while (controlIterator.hasNext())
			{
				controlInterface = (ControlInterface) controlIterator.next();
				if (!controlInterface.getSequenceNumberChanged())
				{
					controlIterator.remove();
				}

			}

		}
	}

}
