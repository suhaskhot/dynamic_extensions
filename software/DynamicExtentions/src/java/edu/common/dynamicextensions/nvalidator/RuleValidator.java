
package edu.common.dynamicextensions.nvalidator;

import java.util.Map;

import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;
import edu.common.dynamicextensions.napi.ControlValue;

public interface RuleValidator {

	void validate(ControlValue controlValue, Map<String, String> parameterMap)
			throws DynamicExtensionsValidationException;
}
