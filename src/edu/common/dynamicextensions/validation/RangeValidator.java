
package edu.common.dynamicextensions.validation;

import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domain.DoubleAttributeTypeInformation;
import edu.common.dynamicextensions.domain.LongAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;

/**
 * This Class validates the range of the numeric data entered by the user. It checks whether the value entered is between the 
 * specified range. The extreme values of the range are obtained from the RuleParameter of the Rule instance. 
 * @author chetan_patil
 * @version 1.0
 */
public class RangeValidator implements ValidatorRuleInterface
{

	/**
	 * This method implements the validate method of the ValidatorRuleInterface.
	 * This method validates the numeric data entered by the user against the maximum and the 
	 * minimum values of the rule.
	 * @param attribute the Attribute whose corresponding value is to be verified.
	 * @param valueObject the value entered by the user.
	 * @param parameterMap the parameters of the Rule.
	 * @throws DynamicExtensionsValidationException if the value is not following the range Rule. 
	 */
	public boolean validate(AttributeInterface attribute, Object valueObject, Map<String, String> parameterMap)
			throws DynamicExtensionsValidationException
	{
		boolean valid = true;
		String attributeName = attribute.getName();
		
		if (valueObject != null && !((String) valueObject).trim().equals(""))
		{
			AttributeTypeInformationInterface attributeTypeInformation = attribute.getAttributeTypeInformation();
			if (attributeTypeInformation != null)
			{
				String value = (String) valueObject;
				
				Set<Map.Entry<String, String>> parameterSet = parameterMap.entrySet();
				for (Map.Entry<String, String> parameter : parameterSet)
				{
					if (attributeTypeInformation instanceof LongAttributeTypeInformation)
					{
						checkLongValidation(parameter, attributeName, value);
					}
					else if (attributeTypeInformation instanceof DoubleAttributeTypeInformation)
					{
						checkDoubleValidation(parameter, attributeName, value);
					}
				}
			}
		}
		return valid;
	}

	/**
	 * This method verifies if the number is a proper Long value or not.
	 * @param parameter the parameter of the rule in form of <ParameterName, Value> pair. 
	 * @param attributeName the name of the Attribute.
	 * @param value the value to be verified.
	 * @throws DynamicExtensionsValidationException
	 */
	private void checkLongValidation(Map.Entry<String, String> parameter, String attributeName, String value)
			throws DynamicExtensionsValidationException
	{
		String parameterName = parameter.getKey();
		String parameterValue = parameter.getValue();
		Long longValue = null;

		try
		{
			longValue = Long.parseLong(value);
		}
		catch(NumberFormatException numberFormatException)
		{
			throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.Number", attributeName);
		}
		
		if (parameterName.equals("min"))
		{
			if (longValue < Long.parseLong(parameterValue))
			{
				throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.Range.Minimum", attributeName);
			}
		}
		else if (parameterName.equals("max"))
		{
			if (longValue > Long.parseLong(parameterValue))
			{
				throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.Range.Maximum", attributeName);
			}
		}
	}

	/**
	 * This method verifies if the number is a proper Double value or not.
	 * @param parameter the parameter of the rule in form of <ParameterName, Value> pair. 
	 * @param attributeName the name of the Attribute.
	 * @param value the value to be verified.
	 * @throws DynamicExtensionsValidationException
	 */
	private void checkDoubleValidation(Map.Entry<String, String> parameter, String attributeName, String value)
			throws DynamicExtensionsValidationException
	{
		String parameterName = parameter.getKey();
		String parameterValue = parameter.getValue();
		Double doubleValue = null;

		try
		{
			doubleValue = Double.parseDouble(value);
		}
		catch(NumberFormatException numberFormatException)
		{
			throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.Number", attributeName);
		}
		if (parameterName.equals("min"))
		{
			if (doubleValue < Double.parseDouble(parameterValue))
			{
				throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.Range.Minimum", attributeName);
			}
		}
		else if (parameterName.equals("max"))
		{
			if (doubleValue > Double.parseDouble(parameterValue))
			{
				throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.Range.Maximum", attributeName);
			}
		}
	}

}
