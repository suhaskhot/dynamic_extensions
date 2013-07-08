
package edu.common.dynamicextensions.nvalidator;

import java.util.Map;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;
import edu.common.dynamicextensions.napi.ControlValue;

public class NumberValidator implements RuleValidator {

	private static Logger logger = Logger.getLogger(NumberValidator.class);

	public void validate(ControlValue controlValue, Map<String, String> parameterMap)
			throws DynamicExtensionsValidationException {
		Control control = controlValue.getControl();

		try {
			String stringValue = (String) controlValue.getValue();

			if (stringValue == null || stringValue.trim().isEmpty()) {
				return;
			}

			logger.debug(control.getCaption() + ":" + control.fromString(stringValue));
		} catch (NumberFormatException numberFormatException) {
			throw new DynamicExtensionsValidationException("Validation failed", null, "dynExtn.validation.Number",
					control.getCaption());
		}
	}
}
