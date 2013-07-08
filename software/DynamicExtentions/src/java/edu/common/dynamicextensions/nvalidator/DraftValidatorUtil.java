
package edu.common.dynamicextensions.nvalidator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.ValidationRule;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.util.global.DEConstants;

public class DraftValidatorUtil extends ValidatorUtil {

	/**
	 * Validate attribute for given attribute.
	 * @param attributeValueNode Value for attribute.
	 * @param controlCaption Caption of control.
	 * @return errorList List of errors.
	 */
	@Override
	public void validate(ControlValue controlValue, List<String> errorList) {
		Set<ValidationRule> attributeRuleCollection = getRuleCollectionExcludingRequiredRule(controlValue.getControl());
		validate(controlValue, errorList, attributeRuleCollection);
	}

	private Set<ValidationRule> getRuleCollectionExcludingRequiredRule(Control control) {
		Set<ValidationRule> attributeRuleCollection = new HashSet<ValidationRule>();

		for (ValidationRule rule : control.getValidationRules()) {

			if (!DEConstants.REQUIRED.equalsIgnoreCase(rule.getName())) {
				attributeRuleCollection.add(rule);
			}
		}
		return attributeRuleCollection;
	}
}
