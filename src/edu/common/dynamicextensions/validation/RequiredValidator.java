
package edu.common.dynamicextensions.validation;

import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;

/**
 * @author Rahul Ner
 *
 */
public class RequiredValidator implements ValidatorRuleInterface
{

	/** 
	 * @see edu.common.dynamicextensions.validation.ValidatorRuleInterface#validate(edu.common.dynamicextensions.domaininterface.AttributeInterface, java.lang.Object, java.util.Map)
	 * @throws DynamicExtensionsValidationException
	 */
	public boolean validate(AttributeInterface attribute, Object valueObject, Map paramMap)
			throws DynamicExtensionsValidationException
	{
		String attributeName = attribute.getName();
		if (valueObject == null || ((String) valueObject).equals(""))
		{
			throw new DynamicExtensionsValidationException("Validation failed", null,
					"dynExtn.validation.RequiredValidator", attributeName);
		}
		return true;
	}

}
