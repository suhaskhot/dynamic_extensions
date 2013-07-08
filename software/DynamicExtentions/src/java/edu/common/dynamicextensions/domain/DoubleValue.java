
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.DoubleValueInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

/**
 * The Class DoubleValue.
 *
 * @author sujay_narkar
 * @hibernate.joined-subclass table="DYEXTN_DOUBLE_CONCEPT_VALUE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 */
public class DoubleValue extends PermissibleValue implements DoubleValueInterface
{

	public DoubleValue(DoubleValue pv) throws DynamicExtensionsSystemException
	{
		super(pv);
	}

	public DoubleValue()
	{
		super();
	}
	/** Serial Version Unique Identifier. */
	private static final long serialVersionUID = 2104021070651742508L;

	/** The predefined Double value. */
	protected Double value;

	/**
	 * This method returns the predefined value of DoubleValue.
	 *
	 * @return the predefined value of DoubleValue.
	 * @hibernate.property name="value" type="double" column="VALUE"
	 */
	public Double getValue()
	{
		return value;
	}

	/**
	 * This method sets the value of DoubleValue to the given value.
	 * @param value the value to be set.
	 */
	public void setValue(Double value)
	{
		this.value = value;
	}

	/**
	 * This method returns the value of DoubleValue downcasted to the Object.
	 * @return the value of DateValue downcasted to the Object.
	 */
	public Object getValueAsObject()
	{
		return value;
	}

	/**
	 * This method type casts the value into Double value and saves it.
	 * This method can throw Cast Class Exception is value is not of type Double
	 * @param value the value
	 * @see edu.common.dynamicextensions.domain.PermissibleValue#setObjectValue(java.lang.Object)
	 */
	@Override
    public void setObjectValue(Object value)
	{
		Double doubleValue = (Double) value;
		setValue(doubleValue);
	}

	/**
	 * This method returns the copy of the Permissible Value Object.
	 * @return the object copy
	 * @see edu.common.dynamicextensions.domaininterface.PermissibleValueInterface#getObjectCopy()
	 */
	public PermissibleValueInterface getObjectCopy()
	{
		DoubleValueInterface doubleValueInterface = DomainObjectFactory.getInstance()
				.createDoubleValue();
		doubleValueInterface.setValue(value);
		return doubleValueInterface;
	}

	/**
	 * Equals.
	 *
	 * @param obj the obj
	 * @return true, if successful
	 * @see edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject#equals(java.lang.Object)
	 */
	@Override
    public boolean equals(Object obj)
	{
		boolean isEqual = false;
		if (obj instanceof DoubleValue
				&& (value != null && value.equals(((DoubleValue) obj).getValue())))
		{
			isEqual = true;
		}
		return isEqual;
	}

	/**
	 * Hash code.
	 *
	 * @return the int
	 * @see edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject#hashCode()
	 */
	@Override
	public int hashCode()
	{
	    int hashcode;
	    if (value != null)
	    {
	        hashcode = value.hashCode();
	    }
	    else
	    {
	        hashcode = super.hashCode();
	    }
	    return hashcode;
	}

	/**
	 * Compare.
	 *
	 * @param o1 the o1
	 * @param o2 the o2
	 * @return the int
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(PermissibleValue o1, PermissibleValue o2)
	{
		Double value1 = ((DoubleValue)o1).value;
		Double value2 = ((DoubleValue)o2).value;
		if(value1 < value2)
		{
			return -1;
		}
		else if(value1 > value2)
		{
			return 1;
		}
		return 0;
	}
}
