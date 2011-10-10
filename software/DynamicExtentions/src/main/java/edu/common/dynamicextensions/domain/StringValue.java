
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.StringValueInterface;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_STRING_CONCEPT_VALUE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class StringValue extends PermissibleValue implements StringValueInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 6552718005216538542L;

	/**
	 * The predefined String value.
	 */
	protected String value;

	/**
	 * This method returns the predefined value of StringValue.
	 * @hibernate.property name="value" type="string" column="VALUE"
	 * @return the predefined value of StringValue.
	 */
	public String getValue()
	{
		return value;
	}

	/**
	 * This method sets the value of DateValue to the given value.
	 * @param value the value to be set.
	 */
	public void setValue(String value)
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
		String stringValue = value.toString();
		setValue(stringValue);
	}

	/**
	 *
	 */
	public PermissibleValueInterface getObjectCopy()
	{
		StringValueInterface stringValue = DomainObjectFactory.getInstance().createStringValue();
		stringValue.setValue(value);
		return stringValue;
	}

	/**
	 * This method overrides the equals method of the parent Class.
	 * This method checks the equality of the value .
	 * @return boolean true if the both values objects are equal otherwise false.
	 */
	public boolean equals(Object obj)
	{
		boolean isEqual = false;
		if (obj instanceof StringValue
				&& (value != null && value.equals(((StringValue) obj).getValue())))
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
		String value1 = ((StringValue) o1).value;
		String value2 = ((StringValue) o2).value;
		if (value1.compareTo(value2) < 0)
		{
			return -1;
		}
		else if (value1.compareTo(value2) > 0)
		{
			return 1;
		}
		return 0;
	}
}
