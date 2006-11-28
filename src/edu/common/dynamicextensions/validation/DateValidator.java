
package edu.common.dynamicextensions.validation;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.DateAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;
import edu.wustl.common.util.Utility;

/**
 * @author chetan_patil
 *
 */
public class DateValidator implements ValidatorRuleInterface
{

	/**
	 * @see edu.common.dynamicextensions.validation.ValidatorRuleInterface#validate(edu.common.dynamicextensions.domaininterface.AttributeInterface, java.lang.Object, java.util.Map)
	 * @throws DynamicExtensionsValidationException
	 */
	public boolean validate(AttributeInterface attribute, Object valueObject, Map<String, String> parameterMap) throws DynamicExtensionsValidationException
	{
		boolean valid = false;
		String attributeName = attribute.getName();

		if (valueObject != null && !((String) valueObject).trim().equals(""))
		{
			AttributeTypeInformationInterface attributeTypeInformation = attribute.getAttributeTypeInformation();
			if (attributeTypeInformation != null)
			{
				String value = (String) valueObject;

				if (attributeTypeInformation instanceof DateAttributeTypeInformation)
				{
					DateAttributeTypeInformation dateAttributeTypeInformation = (DateAttributeTypeInformation) attributeTypeInformation;
					String dateFormat = dateAttributeTypeInformation.getFormat();

					try
					{
						Date date = null;
						date = Utility.parseDate(value, dateFormat);
						if(date != null)
						{
							valid = true;
						}
					}
					catch (ParseException parseException)
					{
						List<String> placeHolders = new ArrayList<String>();
						placeHolders.add(attributeName);
						placeHolders.add(dateFormat);
						throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.Date", placeHolders);
					}
				}
			}
		}
		return valid;
	}

}
