
package edu.common.dynamicextensions.nutility;

import edu.common.dynamicextensions.domain.nui.NumberField;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.util.parser.FormulaParser;

/**
 *
 * @author Kunal
 *
 */
public class FormulaCalculator {

	private FormulaParser formulaParser = new FormulaParser();

	public FormulaCalculator() {
		formulaParser = new FormulaParser();
	}

	public String evaluateFormula(FormData formData, NumberField calculatedControl, Integer rowNumber) {

		try {
			formulaParser.parseExpression(calculatedControl.getFormula());
		} catch (DynamicExtensionsSystemException e) {
			throw new RuntimeException("Error parsing expression: " + calculatedControl.getFormula(), e);
		}
		boolean allControlPresent = true;
		String evaluatedValue = null;
		for (String symbol : formulaParser.getSymobols()) {
			ControlValue controlValue = formData.getFieldValue(symbol, rowNumber);
			if (controlValue.getValue() == null || controlValue.getValue().toString().isEmpty()) {
				allControlPresent = false;
				break;
			}
			formulaParser.setVariableValue(symbol, calculatedControl.fromString(controlValue.getValue().toString()));
		}
		if (allControlPresent) {
			evaluatedValue = calculatedControl.toString(formulaParser.evaluateExpression());
		}

		return evaluatedValue;
	}

}
