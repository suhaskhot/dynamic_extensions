
package edu.common.dynamicextensions.validation;

import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domain.DoubleAttributeTypeInformation;
import edu.common.dynamicextensions.domain.LongAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;
import edu.wustl.common.util.global.ApplicationProperties;

/**
 * @author Rahul Ner
 *
 */
public class RangeValidator implements ValidatorRuleInterface
{

	/**
	 * @see edu.common.dynamicextensions.validation.ValidatorRuleInterface#validate(edu.common.dynamicextensions.domaininterface.AttributeInterface, java.lang.Object, java.util.Map)
	 * @throws DynamicExtensionsValidationException
	 */
	public boolean validate(AttributeInterface attribute, Object valueObject, Map<String, String> parameterMap) throws DynamicExtensionsValidationException
	{
		String attributeName = attribute.getName();
		
		if (valueObject == null || ((String) valueObject).trim().equals(""))
		{
			throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.RequiredValidator", attributeName);
		}
		else
		{
			String value = (String)valueObject;
			AttributeTypeInformationInterface attributeTypeInformation = attribute.getAttributeTypeInformation();
			Set<Map.Entry<String, String>> parameterSet = parameterMap.entrySet();
			for(Map.Entry<String, String> parameter : parameterSet)
			{
				String parameterName = parameter.getKey();
				String parameterValue = parameter.getValue();
				
				if(attributeTypeInformation instanceof LongAttributeTypeInformation )
				{
					if(parameterName.equals("min"))
					{
						if(Long.parseLong(value) < Long.parseLong(parameterValue))
						{
							throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.Range.Minimum", attributeName);
						}
					}
					else if(parameterName.equals("max"))
					{
						if(Long.parseLong(value) > Long.parseLong(parameterValue))
						{
							throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.Range.Maximum", attributeName);
						}
					}
				}
				else if(attributeTypeInformation instanceof DoubleAttributeTypeInformation )
				{
					if(parameterName.equals("min"))
					{
						if(Double.parseDouble(value) < Double.parseDouble(parameterValue))
						{
							throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.Range.Minimum", attributeName);
						}
					}
					else if(parameterName.equals("max"))
					{
						if(Double.parseDouble(value) > Double.parseDouble(parameterValue))
						{
							throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.Range.Maximum", attributeName);
						}
					}
				}
			}			
		}
		return true;
	}

}
