
package edu.common.dynamicextensions.domaininterface;

import edu.common.dynamicextensions.domain.ByteArrayValue;

/**
 * This object stores the permissible value of type byte array.This is a user defined value.
 * @author geetika_bangard
 */
public interface ByteArrayValueInterface extends PermissibleValueInterface
{
	/**
	 * This method returns the value of the ByteArrayValue.
	 * @return the value of the {@link ByteArrayValue}.
	 */
	Byte[] getValue();

	/**
	 * This method sets the value of the ByteArrayValue.
	 * @param value the value to be set.
	 */
	void setValue(Byte[] value);
}
