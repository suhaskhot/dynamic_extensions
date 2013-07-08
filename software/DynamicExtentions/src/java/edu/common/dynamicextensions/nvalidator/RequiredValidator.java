
package edu.common.dynamicextensions.nvalidator;

import java.util.Map;

import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;
import edu.common.dynamicextensions.napi.ControlValue;

public class RequiredValidator implements RuleValidator {

	public void validate(ControlValue controlValue, Map<String, String> parameterMap)
			throws DynamicExtensionsValidationException {

		if (controlValue.isHidden() || controlValue.isReadOnly()) {
			return;
		}
		String controlCaption = controlValue.getControl().getCaption();

		if (controlValue.getValue() == null) {

			throw new DynamicExtensionsValidationException("Validation failed", null,
					"dynExtn.validation.RequiredValidator", controlCaption);
		}

		if (controlValue.getValue() instanceof String[]) {
			String[] values = (String[]) controlValue.getValue();
			if (values.length == 0) {
				throw new DynamicExtensionsValidationException("Validation failed", null,
						"dynExtn.validation.RequiredValidator", controlCaption);
			}
		}

		if (((String) controlValue.getValue()).trim().isEmpty()) {
			throw new DynamicExtensionsValidationException("Validation failed", null,
					"dynExtn.validation.RequiredValidator", controlCaption);
		}
	}
}
