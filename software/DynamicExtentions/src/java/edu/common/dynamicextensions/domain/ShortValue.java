
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.domaininterface.ShortValueInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_SHORT_CONCEPT_VALUE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class ShortValue extends PermissibleValue implements ShortValueInterface
{

	public ShortValue(ShortValue pv) throws DynamicExtensionsSystemException
	{
		super(pv);
	}

	public ShortValue()
	{
		// TODO Auto-generated constructor stub
	}
	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = 5377962679920380224L;

	/**
	 * The predefined Date value.
	 */
	protected Short value;

	/**
	 * This method returns the predefined value of ShortValue.
	 * @hibernate.property name="value" type="short" column="VALUE"
	 * @return the predefined value of ShortValue.
	 */
	public Short getValue()
	{
		return value;
	}

	/**
	 * This method sets the value of ShortValue to the given value.
	 * @param value the value to be set.
	 */
	public void setValue(Short value)
	{
		this.value = value;
	}

	/**
	 * This method returns the value of ShortValue downcasted to the Object.
	 * @return the value of the DateValue downcasted to the Object.
	 */
	public Object getValueAsObject()
	{
		return value;
	}

	/**
	 * This method type casts the value into Short value and saves it.
	 * This method can throw Cast Class Exception is value is not of type Short
	 * @param value the value
	 * @see edu.common.dynamicextensions.domain.PermissibleValue#setObjectValue(java.lang.Object)
	 */
	public void setObjectValue(Object value)
	{
		Short shortValue = (Short) value;
		setValue(shortValue);
	}

	/**
	 *
	 */
	public PermissibleValueInterface getObjectCopy()
	{
		ShortValueInterface shortValueInterface = DomainObjectFactory.getInstance()
				.createShortValue();
		shortValueInterface.setValue(value);
		return shortValueInterface;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject#equals(java.lang.Object)
	 */
	public boolean equals(Object obj)
	{
		boolean isEqual = false;
		if (obj instanceof ShortValue
				&& (value != null && value.equals(((ShortValue) obj).getValue())))
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
		Short value1 = ((ShortValue) o1).value;
		Short value2 = ((ShortValue) o2).value;
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
