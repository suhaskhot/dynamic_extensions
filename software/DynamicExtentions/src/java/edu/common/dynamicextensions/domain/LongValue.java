
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.LongValueInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_LONG_CONCEPT_VALUE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
@SuppressWarnings("unchecked")
public class LongValue extends PermissibleValue implements LongValueInterface
{

	public LongValue(LongValue pv) throws DynamicExtensionsSystemException
	{
		super(pv);
	}

	public LongValue()
	{
		super();
	}
	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = -3038941708523130752L;

	/**
	 * The predefined Long value.
	 */
	protected Long value;

	/**
	 * This method returns the predefined value of LongValue.
	 * @hibernate.property name="value" type="long" column="VALUE"
	 * @return the predefined value of LongValue.
	 */
	public Long getValue()
	{
		return value;
	}

	/**
	 * This method sets the value of LongValue to the given value.
	 * @param value the value to be set.
	 */
	public void setValue(Long value)
	{
		this.value = value;
	}

	/**
	 * This method returns the value of DateValue downcasted to the Object.
	 * @return the value of the DateValue downcasted to the Object.
	 */
	public Object getValueAsObject()
	{
		return value;
	}

	/**
	 * This method type casts the value into Long value and saves it.
	 * This method can throw Cast Class Exception is value is not of type Long
	 * @param value the value
	 * @see edu.common.dynamicextensions.domain.PermissibleValue#setObjectValue(java.lang.Object)
	 */
	public void setObjectValue(Object value)
	{
		Long longValue = (Long) value;
		setValue(longValue);
	}

	/**
	 *
	 */
	public PermissibleValueInterface getObjectCopy()
	{
		LongValueInterface longValueInterface = DomainObjectFactory.getInstance().createLongValue();
		longValueInterface.setValue(value);
		return longValueInterface;
	}

	public boolean equals(Object obj)
	{
		boolean isEqual = false;
		if (obj instanceof LongValue
				&& (value != null && value.equals(((LongValue) obj).getValue())))
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
		Long value1 = ((LongValue) o1).value;
		Long value2 = ((LongValue) o2).value;
		if (value1 < value2)
		{
			return -1;
		}
		else if (value1 > value2)
		{
			return 1;
		}
		return 0;
	}
}
