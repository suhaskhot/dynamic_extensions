/**
 *
 */

package edu.common.dynamicextensions.nvalidator;

import java.util.Map;

import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;
import edu.common.dynamicextensions.napi.ControlValue;

/**
 * TextLengthValidator Class validates the text as per the text length defined for that Control during its creation.
 * If the no text length is provided or the text length is zero, no validation checks are made.
 */
public class TextLengthValidator implements RuleValidator {

	public void validate(ControlValue controlValue, Map<String, String> parameterMap)
			throws DynamicExtensionsValidationException {
		if (controlValue.getValue() != null)
		{
			String value = (String) controlValue.getValue();
			String paramValue = parameterMap.get("max");
			Integer paramIntVal = (paramValue != null ? Integer.valueOf(paramValue) : null);
			String caption = controlValue.getControl().getCaption();

			if (paramIntVal != null && value.length() > paramIntVal) {
				ValidatorUtil.reportInvalidInput(caption, paramValue, "dynExtn.validation.TextLength.max");
			}

			paramValue = parameterMap.get("min");
			paramIntVal = (paramValue != null ? Integer.valueOf(paramValue) : null);

			if (value.length() > 0 && paramIntVal != null
					&& value.length() < paramIntVal) {
				ValidatorUtil.reportInvalidInput(caption, paramValue, "dynExtn.validation.TextLength.min");
			}
		}
				

	}
}
