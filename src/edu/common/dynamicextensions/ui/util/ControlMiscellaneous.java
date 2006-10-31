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
public class ControlMiscellaneous
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
			defaultValue = stringAttribute.getDefaultValue();
		}
		else if (abstractAttribute instanceof BooleanAttributeInterface)
		{
			BooleanAttributeInterface booleanAttribute = (BooleanAttributeInterface) abstractAttribute;
			defaultValue = booleanAttribute.getDefaultValue().toString();
		}
		else if (abstractAttribute instanceof IntegerAttributeInterface)
		{
			IntegerAttributeInterface integerAttribute = (IntegerAttributeInterface) abstractAttribute;
			defaultValue = integerAttribute.getDefaultValue().toString();
		}
		else if (abstractAttribute instanceof LongAttributeInterface)
		{
			LongAttributeInterface longAttribute = (LongAttributeInterface) abstractAttribute;
			defaultValue = longAttribute.getDefaultValue().toString();
		}
		else if (abstractAttribute instanceof DoubleAttributeInterface)
		{
			DoubleAttributeInterface doubleAttribute = (DoubleAttributeInterface) abstractAttribute;
			defaultValue = doubleAttribute.getDefaultValue().toString();
		}
		else if (abstractAttribute instanceof FloatAttributeInterface)
		{
			FloatAttributeInterface floatAttribute = (FloatAttributeInterface) abstractAttribute;
			defaultValue = floatAttribute.getDefaultValue().toString();
		}
		else if (abstractAttribute instanceof ShortAttributeInterface)
		{
			ShortAttributeInterface shortAttribute = (ShortAttributeInterface) abstractAttribute;
			defaultValue = shortAttribute.getDefaultValue().toString();
		}
		else if (abstractAttribute instanceof DateAttributeInterface)
		{
			DateAttributeInterface dateAttribute = (DateAttributeInterface) abstractAttribute;
			defaultValue = new SimpleDateFormat(getDateFormat(dateAttribute)).format(dateAttribute.getDefaultValue());
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
		String dateFormat = ((DateAttributeInterface)dateAttribute).getFormat();
		if(dateFormat == null)
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
		List<NameValueBean> nameValueBeanList = new Vector<NameValueBean>();

		NameValueBean nameValueBean = null;
		DataElementInterface dataElement = attribute.getDataElement();

		if (dataElement != null)
		{
			if (dataElement instanceof UserDefinedDE)
			{
				Collection<PermissibleValueInterface> permissibleValueCollection = ((UserDefinedDE) dataElement).getPermissibleValueCollection();
				for (PermissibleValueInterface permissibleValue : permissibleValueCollection)
				{
					if (permissibleValue instanceof StringValueInterface)
					{
						nameValueBean = getPermissibleStringValue(permissibleValue);
					}
					else if (permissibleValue instanceof DateValueInterface)
					{
						nameValueBean = getPermissibleDateValue(permissibleValue);
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
		return nameValueBeanList;
	}

	private static NameValueBean getPermissibleDateValue(PermissibleValueInterface permissibleValue)
	{
		DateValueInterface dateValue = (DateValueInterface) permissibleValue;
		NameValueBean nameValueBean = new NameValueBean();

		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateValue.getValue());
		nameValueBean.setName(date);
		nameValueBean.setValue(date);

		return nameValueBean;
	}

	private static NameValueBean getPermissibleDoubleValue(PermissibleValueInterface permissibleValue)
	{
		DoubleValueInterface doubleValue = (DoubleValueInterface) permissibleValue;
		NameValueBean nameValueBean = new NameValueBean();

		nameValueBean.setName(doubleValue.getValue().doubleValue());
		nameValueBean.setValue(doubleValue.getValue().doubleValue());

		return nameValueBean;
	}

	private static NameValueBean getPermissibleFloatValue(PermissibleValueInterface permissibleValue)
	{
		FloatValueInterface floatValue = (FloatValueInterface) permissibleValue;
		NameValueBean nameValueBean = new NameValueBean();

		nameValueBean.setName(floatValue.getValue().floatValue());
		nameValueBean.setValue(floatValue.getValue().floatValue());

		return nameValueBean;
	}

	private static NameValueBean getPermissibleLongValue(PermissibleValueInterface permissibleValue)
	{
		LongValueInterface longValue = (LongValueInterface) permissibleValue;
		NameValueBean nameValueBean = new NameValueBean();

		nameValueBean.setName(longValue.getValue().longValue());
		nameValueBean.setValue(longValue.getValue().longValue());

		return nameValueBean;
	}

	private static NameValueBean getPermissibleIntegerValue(PermissibleValueInterface permissibleValue)
	{
		IntegerValueInterface integerValue = (IntegerValueInterface) permissibleValue;
		NameValueBean nameValueBean = new NameValueBean();

		nameValueBean.setName(integerValue.getValue().intValue());
		nameValueBean.setValue(integerValue.getValue().intValue());

		return nameValueBean;
	}

	private static NameValueBean getPermissibleShortValue(PermissibleValueInterface permissibleValue)
	{
		ShortValueInterface shortValue = (ShortValueInterface) permissibleValue;
		NameValueBean nameValueBean = new NameValueBean();

		nameValueBean.setName(shortValue.getValue().shortValue());
		nameValueBean.setValue(shortValue.getValue().shortValue());

		return nameValueBean;
	}

	private static NameValueBean getPermissibleBooleanValue(PermissibleValueInterface permissibleValue)
	{
		BooleanValueInterface booleanValue = (BooleanValueInterface) permissibleValue;
		NameValueBean nameValueBean = new NameValueBean();

		nameValueBean.setName(booleanValue.getValue().booleanValue());
		nameValueBean.setValue(booleanValue.getValue().booleanValue());

		return nameValueBean;
	}

	private static NameValueBean getPermissibleStringValue(PermissibleValueInterface permissibleValue)
	{
		StringValueInterface stringValue = (StringValueInterface) permissibleValue;
		NameValueBean nameValueBean = new NameValueBean();

		nameValueBean.setName(stringValue.getValue().trim());
		nameValueBean.setValue(stringValue.getValue().trim());

		return nameValueBean;
	}

}
