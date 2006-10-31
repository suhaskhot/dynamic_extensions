/**
 * 
 */

package edu.common.dynamicextensions.ui.util;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import edu.common.dynamicextensions.domain.UserDefinedDE;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.BooleanAttributeInterface;
import edu.common.dynamicextensions.domaininterface.BooleanValueInterface;
import edu.common.dynamicextensions.domaininterface.DataElementInterface;
import edu.common.dynamicextensions.domaininterface.DateAttributeInterface;
import edu.common.dynamicextensions.domaininterface.DateValueInterface;
import edu.common.dynamicextensions.domaininterface.DoubleAttributeInterface;
import edu.common.dynamicextensions.domaininterface.DoubleValueInterface;
import edu.common.dynamicextensions.domaininterface.FloatAttributeInterface;
import edu.common.dynamicextensions.domaininterface.FloatValueInterface;
import edu.common.dynamicextensions.domaininterface.IntegerAttributeInterface;
import edu.common.dynamicextensions.domaininterface.IntegerValueInterface;
import edu.common.dynamicextensions.domaininterface.LongAttributeInterface;
import edu.common.dynamicextensions.domaininterface.LongValueInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.ShortAttributeInterface;
import edu.common.dynamicextensions.domaininterface.ShortValueInterface;
import edu.common.dynamicextensions.domaininterface.StringAttributeInterface;
import edu.common.dynamicextensions.domaininterface.StringValueInterface;
import edu.wustl.common.beans.NameValueBean;

/**
 * This class defines miscellaneous methods that are commonly used by many Control objects. * 
 * @author chetan_patil
 */
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
		if (abstractAttribute instanceof StringAttributeInterface)
		{
			StringAttributeInterface stringAttribute = (StringAttributeInterface) abstractAttribute;
			if (stringAttribute != null && stringAttribute.getDefaultValue() != null)
			{
				defaultValue = stringAttribute.getDefaultValue();
			}
		}
		else if (abstractAttribute instanceof BooleanAttributeInterface)
		{
			BooleanAttributeInterface booleanAttribute = (BooleanAttributeInterface) abstractAttribute;
			if (booleanAttribute != null && booleanAttribute.getDefaultValue() != null)
			{
				defaultValue = booleanAttribute.getDefaultValue().toString();
			}
		}
		else if (abstractAttribute instanceof IntegerAttributeInterface)
		{
			IntegerAttributeInterface integerAttribute = (IntegerAttributeInterface) abstractAttribute;
			if (integerAttribute != null && integerAttribute.getDefaultValue() != null)
			{
				defaultValue = integerAttribute.getDefaultValue().toString();
			}
		}
		else if (abstractAttribute instanceof LongAttributeInterface)
		{
			LongAttributeInterface longAttribute = (LongAttributeInterface) abstractAttribute;
			if (longAttribute != null && longAttribute.getDefaultValue() != null)
			{
				defaultValue = longAttribute.getDefaultValue().toString();
			}
		}
		else if (abstractAttribute instanceof DoubleAttributeInterface)
		{
			DoubleAttributeInterface doubleAttribute = (DoubleAttributeInterface) abstractAttribute;
			if (doubleAttribute != null && doubleAttribute.getDefaultValue() != null)
			{
				defaultValue = doubleAttribute.getDefaultValue().toString();
			}
		}
		else if (abstractAttribute instanceof FloatAttributeInterface)
		{
			FloatAttributeInterface floatAttribute = (FloatAttributeInterface) abstractAttribute;
			if (floatAttribute != null && floatAttribute.getDefaultValue() != null)
			{
				defaultValue = floatAttribute.getDefaultValue().toString();
			}
		}
		else if (abstractAttribute instanceof ShortAttributeInterface)
		{
			ShortAttributeInterface shortAttribute = (ShortAttributeInterface) abstractAttribute;
			if (shortAttribute != null && shortAttribute.getDefaultValue() != null)
			{
				defaultValue = shortAttribute.getDefaultValue().toString();
			}
		}
		else if (abstractAttribute instanceof DateAttributeInterface)
		{
			DateAttributeInterface dateAttribute = (DateAttributeInterface) abstractAttribute;
			if (dateAttribute != null && dateAttribute.getDefaultValue() != null)
			{
				defaultValue = new SimpleDateFormat(getDateFormat(dateAttribute)).format(dateAttribute.getDefaultValue());
			}
		}
		return defaultValue;
	}

	/**
	 * This method returns the prescribed date format for the given DateAttribute
	 * @param attribute the DateAttribute
	 * @return the date format String
	 */
	public static String getDateFormat(AttributeInterface dateAttribute)
	{
		String dateFormat = ((DateAttributeInterface) dateAttribute).getFormat();
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
	@SuppressWarnings("unchecked")
	public static List<NameValueBean> populateListOfValues(AttributeInterface attribute)
	{
		List<NameValueBean> nameValueBeanList = null;
		NameValueBean nameValueBean = null;
		DataElementInterface dataElement = attribute.getDataElement();

		if (dataElement != null)
		{
			if (dataElement instanceof UserDefinedDE)
			{
				Collection<PermissibleValueInterface> permissibleValueCollection = ((UserDefinedDE) dataElement).getPermissibleValueCollection();
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
							DateAttributeInterface dateAttribute = (DateAttributeInterface) attribute;
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

	private static NameValueBean getPermissibleDateValue(PermissibleValueInterface permissibleValue, DateAttributeInterface dateAttribute)
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
		NameValueBean nameValueBean =  null;

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
		NameValueBean nameValueBean =  null;
		
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
		NameValueBean nameValueBean =  null;
		
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

}
