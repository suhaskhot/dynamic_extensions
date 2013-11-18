
package edu.common.dynamicextensions.nvalidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.ValidationRule;
import edu.common.dynamicextensions.exception.DynamicExtensionsValidationException;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.wustl.common.util.global.ApplicationProperties;

public class ValidatorUtil {

	public void validate(ControlValue controlValue, List<String> errorList) {
		Control control = controlValue.getControl();
		Collection<ValidationRule> validationRules = control.getValidationRules();
		validate(controlValue, errorList, validationRules);
	}

	protected void validate(ControlValue controlValue, List<String> errorList,
			Collection<ValidationRule> validationRules) {

		Control control = controlValue.getControl();
		errorList.addAll(control.validate(controlValue));
		
		if (validationRules == null || validationRules.isEmpty()) {
			return;
		}

		for (ValidationRule rule : validationRules) {

			try {
				String ruleName = rule.getName();
				RuleValidator validatorRule = null;//ControlConfigurationsFactory.getInstance().getValidatorRule(ruleName);
				validatorRule.validate(controlValue, rule.getParams());
			} catch (DynamicExtensionsValidationException e) {
				String errorMessage = ApplicationProperties.getValue(e.getErrorCode(), e.getPlaceHolderList());

				if (!errorList.contains(errorMessage)) {
					errorList.add(errorMessage);
				}
			}
		}
	}

	public static void reportInvalidInput(String placeHolderOne, String placeHolderTwo, String errorKey)
			throws DynamicExtensionsValidationException {
		List<String> placeHolders = new ArrayList<String>();
		placeHolders.add(placeHolderOne);
		placeHolders.add(placeHolderTwo);
		throw new DynamicExtensionsValidationException("Validation failed", null, errorKey, placeHolders);
	}
}