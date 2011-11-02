
package edu.wustl.cab2b.common.util;

import java.io.Serializable;
import java.util.Comparator;

import edu.common.dynamicextensions.domaininterface.PermissibleValueInterface;

public class PermissibleValueComparator implements Comparator<PermissibleValueInterface>,Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1420214417307243915L;

	/**
	 * Compare.
	 * @param permissibleValue1 the permissible value1
	 * @param permissibleValue2 the permissible value2
	 * @return the int
	 */
	public int compare(PermissibleValueInterface permissibleValue1,
			PermissibleValueInterface permissibleValue2)
	{
		String value1 = permissibleValue1.getValueAsObject().toString();
		String value2 = permissibleValue2.getValueAsObject().toString();
		return value1.compareToIgnoreCase(value2);
	}

}
