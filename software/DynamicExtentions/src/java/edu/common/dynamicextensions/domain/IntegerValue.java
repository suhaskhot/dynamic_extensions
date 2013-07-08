
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.IntegerValueInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_INTEGER_CONCEPT_VALUE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class IntegerValue extends PermissibleValue implements IntegerValueInterface
{

	public IntegerValue(IntegerValue pv) throws DynamicExtensionsSystemException
	{
		super(pv);
	}

	public IntegerValue()
	{
		super();
	}
	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = -1598647162647981710L;

	/**
	 * The predefined Integer value.
	 */
	protected Integer value;

	/**
	 * This method returns the predefined value of IntegerValue.
	 * @hibernate.property name="value" type="integer" column="VALUE"
	 * @return the predefined value of IntegerValue.
	 */
	public Integer getValue()
	{
		return value;
	}

	/**
	 * This method sets the value of IntegerValue to the given value.
	 * @param value the value to be set.
	 */
	public void setValue(Integer value)
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
	 * This method type casts the value into Integer value and saves it.
	 * This method can throw Cast Class Exception is value is not of type Integer
	 * @param value the value
	 * @see edu.common.dynamicextensions.domain.PermissibleValue#setObjectValue(java.lang.Object)
	 */
	public void setObjectValue(Object value)
	{
		Integer integerValue = (Integer) value;
		setValue(integerValue);
	}

	/**
	 *
	 */
	public PermissibleValueInterface getObjectCopy()
	{
		IntegerValueInterface integerValueInterface = DomainObjectFactory.getInstance()
				.createIntegerValue();
		integerValueInterface.setValue(value);
		return integerValueInterface;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		boolean isEqual = false;
		if (obj instanceof IntegerValue
				&& (value != null && value.equals(((IntegerValue) obj).getValue())))
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
		Integer value1 = ((IntegerValue) o1).value;
		Integer value2 = ((IntegerValue) o2).value;
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
