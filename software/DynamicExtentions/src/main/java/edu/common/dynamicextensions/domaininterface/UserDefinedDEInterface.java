
package edu.common.dynamicextensions.domaininterface;

import java.util.Collection;
import java.util.Date;

/**
 * When the permissible values for an attribute are user defined the data element is of type
 * UserDefinedDE.This type of data element contains collection of user defined permissible values.
 * @author sujay_narkar
 * @version 1.0
 */
public interface UserDefinedDEInterface extends DataElementInterface
{

	/**
	 * This method returns the Collection of PermissibleValues.
	 * @return the Collection of PermissibleValues.
	 */
	Collection<PermissibleValueInterface> getPermissibleValueCollection();

	/**
	 * This method adds a PermissibleValue to the Collection of PermissibleValues.
	 * @param permissibleValue the PermissibleValue to be added.
	 */
	void addPermissibleValue(PermissibleValueInterface permissibleValue);

	/**
	 * clears the permissible value collection
	 */
	void clearPermissibleValues();

	/**
	 * @param permissibleValueColl
	 */
	void addAllPermissibleValues(Collection<PermissibleValueInterface> permissibleValueColl);

	/**
	 *
	 * @return
	 */
	Boolean getIsOrdered();

	/**
	 *
	 * @param isOrdered
	 */
	void setIsOrdered(Boolean isOrdered);

	/**
	 *
	 * @return
	 */
	Collection<PermissibleValueInterface> getPermissibleValues();

	/**
	 *
	 * @return activation date
	 */
	Date getActivationDate();

	/**
	 *
	 * @param activationDate
	 */
	void setActivationDate(Date activationDate);

	/**
	 *
	 * @return default PV collection.
	 */
	Collection<PermissibleValueInterface> getDefaultPermissibleValues();

	/**
	 *
	 * @param defaultPermissibleValueCollection default PV collection to set.
	 */
	void setDefaultPermissibleValues(Collection<PermissibleValueInterface> defaultPermissibleValues);

	/**
	 * Sets the order.
	 * @param order the order to set
	 */
	void setOrder(String order);

	/**
	 * Gets the order.
	 * @return the order
	 */
	String getOrder();

}
