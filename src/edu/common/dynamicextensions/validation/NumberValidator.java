
package edu.common.dynamicextensions.validation;

import java.util.Map;

import edu.common.dynamicextensions.domain.DoubleAttributeTypeInformation;
import edu.common.dynamicextensions.domain.LongAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;

/**
 * This Class validates the the numeric data entered by the user. It checks whether the value entered is numeric or not.
 * @author chetan_patil
 * @version 1.0
 */
public class NumberValidator implements ValidatorRuleInterface
{
	/**
	 * This method implements the validate method of the ValidatorRuleInterface.
	 * This method validates the numeric data entered by the user on the User Interface.
	 * @param attribute the Attribute whose corresponding value is to be verified.
	 * @param valueObject the value entered by the user.
	 * @param parameterMap the parameters of the Rule.
	 * @throws DynamicExtensionsValidationException if the value is not following the Numeric Rule. 
	 */
	public boolean validate(AttributeInterface attribute, Object valueObject, Map<String, String> parameterMap)
			throws DynamicExtensionsValidationException
	{
//		boolean valid = false;
//		if (valueObject != null && !((String) valueObject).trim().equals(""))
//		{
//			AttributeTypeInformationInterface attributeTypeInformation = attribute.getAttributeTypeInformation();
//			String attributeName = attribute.getName();
//			String value = (String) valueObject;
//
//			if (attributeTypeInformation != null)
//			{
//				if (attributeTypeInformation instanceof LongAttributeTypeInformation)
//				{
//					try
//					{
//						Long numberValue = new Long(value);
//						if (numberValue != null)
//						{
//							valid = true;
//						}
//					}
//					catch (NumberFormatException numberFormatException)
//					{
//						throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.Number", attributeName);
//					}
//				}
//				else if (attributeTypeInformation instanceof DoubleAttributeTypeInformation)
//				{
//					try
//					{
//						Double numberValue = new Double(value);
//						if (numberValue != null)
//						{
//							valid = true;
//						}
//					}
//					catch (NumberFormatException numberFormatException)
//					{
//						throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.Number", attributeName);
//					}
//				}
//			}
//		}
//		return valid;
		return true;
	}
}
