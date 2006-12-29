
package edu.common.dynamicextensions.validation;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.DoubleAttributeTypeInformation;
import edu.common.dynamicextensions.domain.LongAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;

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
	public boolean validate(AttributeInterface attribute, Object valueObject,
			Map<String, String> parameterMap) throws DynamicExtensionsValidationException
	{
		boolean isValid = false;
		AttributeTypeInformationInterface attributeTypeInformation = attribute
				.getAttributeTypeInformation();
		String attributeName = attribute.getName();
		if (valueObject != null && DynamicExtensionsUtility.isNumeric((String) valueObject))
		{
			String value = (String) valueObject;

			if (attributeTypeInformation != null)
			{
				List<String> placeHolders = null;
				if (attributeTypeInformation instanceof LongAttributeTypeInformation)
				{
					try
					{
						BigInteger numberValue = new BigInteger(value);
						String strLongMin = (new Long(Long.MIN_VALUE)).toString();
						String strLongMax = (new Long(Long.MAX_VALUE)).toString();
						BigInteger longMin = new BigInteger(strLongMin);
						BigInteger longMax = new BigInteger(strLongMax);

						if (numberValue.compareTo(longMin) < 0
								|| numberValue.compareTo(longMax) > 0)
						{
							placeHolders = new ArrayList<String>();
							placeHolders.add(attributeName);
							placeHolders.add(strLongMin);
							placeHolders.add(strLongMax);
							throw new DynamicExtensionsValidationException("Validation failed",
									null, "dynExtn.validation.Number.Range", placeHolders);
						}
					}
					catch (NumberFormatException numberFormatException)
					{
						throw new DynamicExtensionsValidationException("Validation failed", null,
								"dynExtn.validation.Number", attributeName);
					}
				}
				else if (attributeTypeInformation instanceof DoubleAttributeTypeInformation)
				{
					try
					{
						BigDecimal numberValue = new BigDecimal(value);
						String strDoubleMin = (new Double(Double.MIN_VALUE)).toString();
						String strDoubleMax = (new Double(Double.MAX_VALUE)).toString();
						BigDecimal doubleMin = new BigDecimal(strDoubleMin);
						BigDecimal doubleMax = new BigDecimal(strDoubleMax);

						if (numberValue.compareTo(doubleMin) < 0
								|| numberValue.compareTo(doubleMax) > 0)
						{
							placeHolders = new ArrayList<String>();
							placeHolders.add(attributeName);
							placeHolders.add(strDoubleMin);
							placeHolders.add(strDoubleMax);
							throw new DynamicExtensionsValidationException("Validation failed",
									null, "dynExtn.validation.Number.Range", placeHolders);
						}
					}
					catch (NumberFormatException numberFormatException)
					{
						throw new DynamicExtensionsValidationException("Validation failed", null,
								"dynExtn.validation.Number", attributeName);
					}
				}
			}
		}
		else
		{
			throw new DynamicExtensionsValidationException("Validation failed", null,
					"dynExtn.validation.Number", attributeName);
		}
		return isValid;
	}
}
