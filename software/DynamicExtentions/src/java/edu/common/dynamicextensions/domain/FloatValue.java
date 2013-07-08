
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.FloatValueInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_FLOAT_CONCEPT_VALUE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 *
 */
public class FloatValue extends PermissibleValue implements FloatValueInterface
{
	public FloatValue(FloatValue pv) throws DynamicExtensionsSystemException
	{
		super(pv);
	}

	public FloatValue()
	{
		super();
	}

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 8774354583244985478L;

	/**
	 * The predefined Float value.
	 */
	protected Float value;

	/**
	 * This method returns the predefined value of FloatValue.
	 * @hibernate.property name="value" type="float" column="VALUE"
	 * @return Returns the predefined value of FloatValue.
	 */
	public Float getValue()
	{
		return value;
	}

	/**
	 * This method sets the value of FloatValue to the given value.
	 * @param value the value to be set.
	 */
	public void setValue(Float value)
	{
		this.value = value;
	}

	/**
	 * This method returns the value of FloatValue downcasted to the Object.
	 * @return the value of the DateValue downcasted to the Object.
	 */
	public Object getValueAsObject()
	{
		return value;
	}

	/**
	 * This method type casts the value into Float value and saves it.
	 * This method can throw Cast Class Exception is value is not of type Float
	 * @param value the value
	 * @see edu.common.dynamicextensions.domain.PermissibleValue#setObjectValue(java.lang.Object)
	 */
	public void setObjectValue(Object value)
	{
		Float floatValue = (Float) value;
		setValue(floatValue);
	}

	/**
	 *
	 */
	public PermissibleValueInterface getObjectCopy()
	{
		FloatValueInterface floatValueInterface = DomainObjectFactory.getInstance()
				.createFloatValue();
		floatValueInterface.setValue(value);
		return floatValueInterface;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		boolean isEqual = false;
		if (obj instanceof FloatValue
				&& (value != null && value.equals(((FloatValue) obj).getValue())))
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
		Float value1 = ((FloatValue) o1).value;
		Float value2 = ((FloatValue) o2).value;
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
