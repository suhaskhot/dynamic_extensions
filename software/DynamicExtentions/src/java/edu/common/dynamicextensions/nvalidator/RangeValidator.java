
package edu.common.dynamicextensions.nvalidator;

import java.math.BigDecimal;
import java.util.Map;

import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;
import edu.common.dynamicextensions.napi.ControlValue;

public class RangeValidator implements RuleValidator {

	private static final String DYN_EXTN_VALIDATION_RANGE_MAX = "dynExtn.validation.Range.Maximum";

	private static final String DYN_EXTN_VALIDATION_RANGE_MIN = "dynExtn.validation.Range.Minimum";

	public void validate(ControlValue controlValue, Map<String, String> parameterMap)
			throws DynamicExtensionsValidationException {
		String valueString = controlValue.toString();

		if (valueString == null || valueString.isEmpty()) {
			return;
		}
		NumberValidator numberValidator = new NumberValidator();
		numberValidator.validate(controlValue, parameterMap);

		BigDecimal value = controlValue.getControl().fromString(valueString);
		String caption = controlValue.getControl().getCaption();
		String parameterValue = parameterMap.get("min");
		if (parameterValue != null && value.compareTo(new BigDecimal(parameterValue)) < 0) {
			ValidatorUtil.reportInvalidInput(caption, parameterValue, DYN_EXTN_VALIDATION_RANGE_MIN);
		}

		parameterValue = parameterMap.get("max");
		if (parameterValue != null && value.compareTo(new BigDecimal(parameterValue)) > 0) {
			ValidatorUtil.reportInvalidInput(caption, parameterValue, DYN_EXTN_VALIDATION_RANGE_MAX);
		}

	}

}
