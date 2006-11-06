
package edu.common.dynamicextensions.validation;

import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;

/**
 * @author Rahul Ner
 *
 */
public class UniqueValidator implements ValidatorRuleInterface
{

	/**
	 * @see edu.common.dynamicextensions.validation.ValidatorRuleInterface#validate(edu.common.dynamicextensions.domaininterface.AttributeInterface, java.lang.Object, java.util.Map)
	 */
	public boolean validate(AttributeInterface attribute, Object valueObject, Map paramMap)
			throws DynamicExtensionsValidationException
	{

		return true;
	}

}
