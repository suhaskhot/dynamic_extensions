
package edu.common.dynamicextensions.domain;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.common.dynamicextensions.domaininterface.DateValueInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * This Class represents the per-defined Date value of the Attribute.
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_DATE_CONCEPT_VALUE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class DateValue extends PermissibleValue implements DateValueInterface
{

	public DateValue(DateValue pv) throws DynamicExtensionsSystemException
	{
		super(pv);
	}

	public DateValue()
	{
		super();
	}
	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 3381430917859885601L;

	/** The predefined Date value. */
	private Date value;

	/**
	 * This method returns the predefined value of DateValue.
	 * @hibernate.property name="value" type="timestamp" column="VALUE"
	 * @return the predefined value of DateValue.
	 */
	public Date getValue()
	{
		Date defaultDate = null;
		if (value != null)
		{
			defaultDate = new Timestamp(value.getTime());
		}
		return defaultDate;
	}

	/**
	 * This method sets the value of DateValue to the given value.
	 * @param value the value to be set.
	 */
	public void setValue(Date value)
	{
		this.value = value;
	}

	/**
	 * This method returns the value of DateValue downcasted to the Object.
	 * @return the value of the DateValue downcasted to the Object.
	 */
	public Object getValueAsObject()
	{
		Date defaultDate = null;
		if (value != null)
		{
			defaultDate = new Timestamp(value.getTime());
		}
		return defaultDate;
	}

	/**
	 * This method type casts the value into Date value and saves it.
	 * This method can throw Cast Class Exception is value is not of type Date
	 * @param value the value
	 * @throws ParseException
	 * @see edu.common.dynamicextensions.domain.PermissibleValue#setObjectValue(java.lang.Object)
	 */
	public void setObjectValue(Object value) throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat();
		Date dateValue = sdf.parse(value.toString());
		setValue(dateValue);
	}

	/**
	 * This method returns the copy of the Permissible Value Object.
	 * @return the object copy
	 * @see edu.common.dynamicextensions.domaininterface.PermissibleValueInterface#getObjectCopy()
	 */
	public PermissibleValueInterface getObjectCopy()
	{
		DateValueInterface dateValueInterface = DomainObjectFactory.getInstance().createDateValue();
		dateValueInterface.setValue(value);
		return dateValueInterface;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		boolean isEqual = false;
		if (obj instanceof DateValue
				&& (value != null && getValue().equals(((DateValue) obj).getValue())))
		{
			isEqual = true;
		}
		return isEqual;
	}

	@Override
	public int hashCode()
	{
		int hashCodeValue;
		if (value != null)
		{
			hashCodeValue = value.hashCode();
		}
		else
		{
			hashCodeValue = 0;
		}
		return hashCodeValue;
	}

	public int compare(PermissibleValue o1, PermissibleValue o2)
	{
		Date dateValue1 = ((DateValue) o1).value;
		Date dateValue2 = ((DateValue) o2).value;
		if (dateValue1.before(dateValue2))
		{
			return -1;
		}
		if (dateValue1.after(dateValue2))
		{
			return 1;
		}
		return 0;
	}
}
