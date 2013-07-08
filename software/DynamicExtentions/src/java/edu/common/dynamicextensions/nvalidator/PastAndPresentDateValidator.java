
package edu.common.dynamicextensions.nvalidator;

import java.util.Date;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;
import edu.common.dynamicextensions.napi.ControlValue;

public class PastAndPresentDateValidator extends DateValidator {

	public void validate(ControlValue controlValue, Map<String, String> parameterMap)
			throws DynamicExtensionsValidationException {
		DatePicker control = (DatePicker) controlValue.getControl();
		super.validate(controlValue, parameterMap);

		if (control.fromString((String) controlValue.getValue()).after(new Date())) {
			ValidatorUtil.reportInvalidInput(control.getCaption(), "today's date.", "dynExtn.validation.Date.Max");
		}
	}
}
