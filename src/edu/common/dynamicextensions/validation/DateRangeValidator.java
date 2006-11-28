
package edu.common.dynamicextensions.validation;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domain.DateAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;
import edu.wustl.common.util.Utility;

/**
 * @author chetan_patil
 *
 */
public class DateRangeValidator implements ValidatorRuleInterface
{

	/**
	 * @see edu.common.dynamicextensions.validation.ValidatorRuleInterface#validate(edu.common.dynamicextensions.domaininterface.AttributeInterface, java.lang.Object, java.util.Map)
	 * @throws DynamicExtensionsValidationException
	 */
	public boolean validate(AttributeInterface attribute, Object valueObject, Map<String, String> parameterMap)
			throws DynamicExtensionsValidationException
	{
		boolean valid = true;

		if (valueObject != null && !((String) valueObject).trim().equals(""))
		{
			AttributeTypeInformationInterface attributeTypeInformation = attribute.getAttributeTypeInformation();
			if (attributeTypeInformation != null)
			{
				Set<Map.Entry<String, String>> parameterSet = parameterMap.entrySet();
				for (Map.Entry<String, String> parameter : parameterSet)
				{
					if (attributeTypeInformation instanceof DateAttributeTypeInformation)
					{
						DateAttributeTypeInformation dateAttributeTypeInformation = (DateAttributeTypeInformation) attributeTypeInformation;
						String dateFormat = dateAttributeTypeInformation.getFormat();

						String parameterName = parameter.getKey();
						String parameterValue = parameter.getValue();

						String attributeName = attribute.getName();
						String value = (String) valueObject;

						if (parameterName.equals("from"))
						{
							checkFromDate(parameterValue, value, dateFormat, attributeName);
						}
						else if (parameterName.equals("to"))
						{
							checkToDate(parameterValue, value, dateFormat, attributeName);
						}
					}
				}
			}
		}
		return valid;
	}

	private void checkFromDate(String parameterValue, String value, String dateFormat, String attributeName)
			throws DynamicExtensionsValidationException
	{
		Date fromDate = null;
		Date valueDate = null;

		try
		{
			fromDate = Utility.parseDate(parameterValue, dateFormat);
			valueDate = Utility.parseDate(value, dateFormat);
		}
		catch (ParseException ParseException)
		{
			List<String> placeHolders = new ArrayList<String>();
			placeHolders.add(attributeName);
			placeHolders.add(dateFormat);
			throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.Date", placeHolders);
		}

		if (fromDate.after(valueDate))
		{
			List<String> placeHolders = new ArrayList<String>();
			placeHolders.add(attributeName);
			placeHolders.add(parameterValue);
			throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.Date.From", placeHolders);
		}
	}

	private void checkToDate(String parameterValue, String value, String dateFormat, String attributeName)
			throws DynamicExtensionsValidationException
	{
		Date toDate = null;
		Date valueDate = null;

		try
		{
			toDate = Utility.parseDate(parameterValue, dateFormat);
			valueDate = Utility.parseDate(value, dateFormat);
		}
		catch (ParseException ParseException)
		{
			List<String> placeHolders = new ArrayList<String>();
			placeHolders.add(attributeName);
			placeHolders.add(dateFormat);
			throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.Date", placeHolders);
		}

		if (toDate.before(valueDate))
		{
			List<String> placeHolders = new ArrayList<String>();
			placeHolders.add(attributeName);
			placeHolders.add(parameterValue);
			throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.Date.To", placeHolders);
		}
	}

}
