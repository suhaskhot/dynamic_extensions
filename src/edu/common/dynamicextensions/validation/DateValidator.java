
package edu.common.dynamicextensions.validation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.DateAttributeTypeInformation;
import edu.common.dynamicextensions.domaininterface.AttributeMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;

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
	public boolean validate(AttributeMetadataInterface attribute, Object valueObject,
			Map<String, String> parameterMap,String controlCaption) throws DynamicExtensionsValidationException
	{
		boolean valid = true;

		String attributeName = attribute.getName();
		AttributeTypeInformationInterface attributeTypeInformation = attribute
				.getAttributeTypeInformation();

		if (((valueObject != null) && (!((String) valueObject).trim().equals("")))
				&& ((attributeTypeInformation != null) && (attributeTypeInformation instanceof DateAttributeTypeInformation)))
		{
			DateAttributeTypeInformation dateAttributeTypeInformation = (DateAttributeTypeInformation) attributeTypeInformation;
			String dateFormat = dateAttributeTypeInformation.getFormat();
			String value = (String) valueObject;

			try
			{
				SimpleDateFormat sf = new SimpleDateFormat(dateFormat);
				sf.setLenient(false);
				sf.parse(value);
			}
			catch (ParseException parseException)
			{
				valid = false;
			}
			//Validate if year is equal to '0000' or contains '.' symbol
			if (value.endsWith("0000") || value.contains("."))
			{
				valid = false;
			}
			//Validate length of entered date
			if (dateFormat.length() != value.length())
			{
				valid = false;
			}

			if (!valid)
			{
				List<String> placeHolders = new ArrayList<String>();
				placeHolders.add(controlCaption);
				placeHolders.add(dateFormat);
				throw new DynamicExtensionsValidationException("Validation failed", null,
						"dynExtn.validation.Date", placeHolders);
			}
		}
		return valid;
	}
}
