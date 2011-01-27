/**
 *
 */
package edu.common.dynamicextensions.skiplogic;

import java.util.Map;

import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;


/**
 * @author Gaurav_mehta
 *
 */
public interface Condition
{

	/**
	 * Gets the identifier.
	 * @return the identifier
	 */
	Long getIdentifier();

	/**
	 * Sets the identifier.
	 * @param identifier the new identifier
	 */
	void setIdentifier(Long identifier);

	/**
	 * Gets the action.
	 * @return the action
	 */
	Action getAction();

	/**
	 * Sets the action.
	 * @param action the new action
	 */
	void setAction(Action action);

	/**
	 * Check condition.
	 * @param objectValueState the object value state
	 * @return true, if successful
	 * @throws DynamicExtensionsSystemException
	 */
	boolean checkCondition(Map<BaseAbstractAttributeInterface, Object> objectValueState) throws DynamicExtensionsSystemException;



}
