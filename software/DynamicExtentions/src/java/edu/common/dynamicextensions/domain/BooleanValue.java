
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.BooleanValueInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

// TODO: Auto-generated Javadoc
/**
 * The Class BooleanValue.
 *
 * @hibernate.joined-subclass table="DYEXTN_BOOLEAN_CONCEPT_VALUE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 * @author sujay_narkar
 */
public class BooleanValue extends PermissibleValue implements BooleanValueInterface
{

	/** Serial Version Unique Identifier. */
	private static final long serialVersionUID = 6775534423957386289L;

	/**
	 * The value to be stored.
	 */
	protected Boolean value;

	/**
	 * This method returns the value of the BooleanValue.
	 *
	 * @return the value of the BooleanValue.
	 * @hibernate.property name="value" type="boolean" column="VALUE"
	 */
	public Boolean getValue()
	{
		return value;
	}

	/**
	 * This method sets the value of the BooleanValue.
	 * @param value the value to be set.
	 */
	public void setValue(Boolean value)
	{
		this.value = value;
	}

	/**
	 * This method returns the value of BooleanValue downcasted to the Object.
	 * @return the value of BooleanValue downcasted to the Object.
	 */
	public Object getValueAsObject()
	{
		return value;
	}

	/**
	 * This method type casts the value into Boolean value and saves it.
	 * This method can throw Cast Class Exception is value is not of type Boolean
	 * @param value the value
	 * @see edu.common.dynamicextensions.domain.PermissibleValue#setObjectValue(java.lang.Object)
	 */
	@Override
    public void setObjectValue(Object value)
	{
		Boolean booleanValue = (Boolean) value;
		setValue(booleanValue);
	}

	/**
	 * Gets the object copy.
	 *
	 * @return the object copy
	 * @see edu.common.dynamicextensions.domaininterface.PermissibleValueInterface#getObjectCopy()
	 */
	public PermissibleValueInterface getObjectCopy()
	{
		BooleanValueInterface booleanValueInterface = DomainObjectFactory.getInstance()
				.createBooleanValue();
		booleanValueInterface.setValue(value);
		return booleanValueInterface;
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
		if (obj instanceof BooleanValue
				&& (value != null && value.equals(((BooleanValue) obj).getValue())))
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
	    int hashCode;
	    if (value != null)
	    {
	        hashCode = value.hashCode();
	    }
	    else
	    {
	        hashCode = super.hashCode();
	    }
	    return hashCode;
	}

	/**
	 * Compare.
	 *
	 * @param p1 the p1
	 * @param p2 the p2
	 * @return the int
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare(PermissibleValue p1, PermissibleValue p2)
	{
		Boolean value1 = ((BooleanValue) p1).value;
		Boolean value2 = ((BooleanValue) p2).value;
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
	
	public BooleanValue()
	{
		super();
	}
	public BooleanValue(BooleanValue booleanValue) throws DynamicExtensionsSystemException
	{
		super(booleanValue);
	}
}
