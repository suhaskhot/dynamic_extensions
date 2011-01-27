
package edu.common.dynamicextensions.domain;

import edu.common.dynamicextensions.domaininterface.ByteArrayValueInterface;
import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;

/**
 * @hibernate.joined-subclass table="DYEXTN_BARR_CONCEPT_VALUE"
 * @hibernate.joined-subclass-key column="IDENTIFIER"
 * @author sujay_narkar
 *
 */
public class ByteArrayValue extends PermissibleValue implements ByteArrayValueInterface
{

	/**
	 * Serial Version Unique Identifier
	 */
	private static final long serialVersionUID = -5535531891478617220L;

	/** The value. */
	private Byte[] value;

	/**
	 * This method returns the value of the ByteArrayValue.
	 * @hibernate.property name="value" type="boolean" column="VALUE"
	 * @return the value of the {@link ByteArrayValue}.
	 */
	public Byte[] getValue()
	{
		return value;
	}

	/**
	 * This method sets the value of the ByteArrayValue.
	 * @param value the value to be set.
	 */
	public void setValue(Byte[] value)
	{
		this.value = value;
	}

	/**
	 * This method returns the value of ByteArrayValue downcasted to the Object.
	 * @return the value of ByteArrayValue downcasted to the Object.
	 */
	public Object getValueAsObject()
	{
		return value;
	}

	/**
	 * This method type casts the value into Byte array and saves it.
	 * This method can throw Cast Class Exception is value is not of type Byte Array
	 * @param value the value
	 * @see edu.common.dynamicextensions.domain.PermissibleValue#setObjectValue(java.lang.Object)
	 */
	public void setObjectValue(Object value)
	{
		Byte[] byteValue = (Byte[]) value;
		setValue(byteValue);
	}

	/**
	 * This method returns the copy of the Permissible Value Object.
	 * @return the object copy
	 * @see edu.common.dynamicextensions.domaininterface.PermissibleValueInterface#getObjectCopy()
	 */
	public PermissibleValueInterface getObjectCopy()
	{
		ByteArrayValueInterface byteValue = DomainObjectFactory.getInstance()
				.createByteArrayValue();
		byteValue.setValue(value);
		return byteValue;
	}

	public int compare(PermissibleValue o1, PermissibleValue o2)
	{
		return 0;
	}
}
