
package edu.common.dynamicextensions.validation;

import java.text.ParseException;
import java.util.Date;

import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;

/**
 * The Class PastAndPresentDateValidator.
 * This class is called for validation when only past and present date is allowed. This class does everything same 
 * as date validator except for the addition feature of checking whether the date is not future date.
 * @author gaurav_mehta
 *
 */
public class PastAndPresentDateValidator extends DateValidator
{

	/**
	 * Checks if is valid date.
	 * @param value the value
	 * @param dateFormat the date format
	 * @param controlCaption the control caption
	 * @param isFromDateRangeValidator the is from date range validator
	 * @return true, if checks if is valid date
	 * @throws DynamicExtensionsValidationException the dynamic extensions validation exception
	 */
	@Override
	protected boolean isValidDate(String value, String dateFormat, String controlCaption,
			boolean... isFromDateRangeValidator) throws DynamicExtensionsValidationException
	{
		boolean isValid = super.isValidDate(value, dateFormat, controlCaption,
				isFromDateRangeValidator);
		try
		{
			if (isValid
					&& isFromDateRangeValidator.length == 0 && getFormattedDateValue(value,
							dateFormat).after(new Date()))
			{
				ValidatorUtil.reportInvalidInput(controlCaption, "today's date.",
						"dynExtn.validation.Date.Max");
			}
		}
		catch (ParseException parseException)
		{
			isValid = false;
		}
		return isValid;
	}
}
