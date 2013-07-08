
package edu.common.dynamicextensions.nvalidator;

import java.util.Date;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;
import edu.common.dynamicextensions.napi.ControlValue;

public class DateRangeValidator implements RuleValidator {

	public void validate(ControlValue controlValue, Map<String, String> parameterMap)
			throws DynamicExtensionsValidationException {
		/* Check for the validity of the date */
		DateValidator dateValidator = new DateValidator();
		dateValidator.validate(controlValue, parameterMap);
		DatePicker control = (DatePicker) controlValue.getControl();
		Date value = control.fromString((String) controlValue.getValue());

		String parameterValue = parameterMap.get("min");
		Date parameterDate = control.fromString(parameterValue);

		if (parameterValue != null && value.before(parameterDate)) {
			ValidatorUtil.reportInvalidInput(control.getCaption(), parameterValue, "dynExtn.validation.Date.Min");
		}

		parameterValue = parameterMap.get("max");
		parameterDate = control.fromString(parameterValue);
		if (parameterValue != null && value.after(parameterDate)) {
			ValidatorUtil.reportInvalidInput(control.getCaption(), parameterValue, "dynExtn.validation.Date.Max");
		}
	}

}
