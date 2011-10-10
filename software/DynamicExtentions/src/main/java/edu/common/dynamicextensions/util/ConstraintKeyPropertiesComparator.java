
package edu.common.dynamicextensions.util;

import java.io.Serializable;
import java.util.Comparator;

import edu.common.dynamicextensions.domaininterface.databaseproperties.ConstraintKeyPropertiesInterface;

/**
 * This class is used as comparator to compare ConstraintKeyProperties objects
 * @author pavan_kalantri
 *
 */
public class ConstraintKeyPropertiesComparator
		implements
			Comparator<ConstraintKeyPropertiesInterface>, Serializable
{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -5257147587074026659L;

	/**
	 * it will compare the constraintKeyProperties object in natural sort order of the column names of there primaryKeyAttribute
	 * @return
	 */
	public int compare(ConstraintKeyPropertiesInterface src, ConstraintKeyPropertiesInterface tgt)
	{

		return src.getSrcPrimaryKeyAttribute().getColumnProperties().getName().compareTo(
				tgt.getSrcPrimaryKeyAttribute().getColumnProperties().getName());
	}

}
