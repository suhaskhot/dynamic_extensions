
package edu.common.dynamicextensions.nvalidator;

import java.util.Date;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;
import edu.common.dynamicextensions.napi.ControlValue;

public class FutureDateValidator extends DateValidator {

	public void validate(ControlValue controlValue, Map<String, String> parameterMap)
			throws DynamicExtensionsValidationException {
		/* Check for the validity of the future date */
		DatePicker control = (DatePicker) controlValue.getControl();
		String value = (String) controlValue.getValue();
		Date inputDate = null;

		try {
			inputDate = control.fromString(value);
		} catch (Exception parseException) {
			ValidatorUtil.reportInvalidInput(control.getCaption(), control.getFormat(), "dynExtn.validation.Date");
		}

		if (inputDate.before(new Date())) {
			ValidatorUtil.reportInvalidInput(control.getCaption(), "today's date.", "dynExtn.validation.Date.MinDate");
		}
	}
}
