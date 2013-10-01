
package edu.common.dynamicextensions.nutility;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

import edu.common.dynamicextensions.domain.nui.Control;
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

		String formula = calculatedControl.getFormula();
		
		try {
			formula = evaluateSum(formula, formData);
			formulaParser.parseExpression(formula);
		} catch (DynamicExtensionsSystemException e) {
			throw new RuntimeException("Error parsing expression: " + formula, e);
		}
		boolean allControlPresent = true;
		String evaluatedValue = null;
		
		for (String symbol : formulaParser.getSymbols()) {
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

	private String evaluateSum(String formula, FormData formData) {
		Pattern pattern = Pattern.compile("sum\\((.*?)\\)");
		Matcher matcher = pattern.matcher(formula);

		while (matcher.find()) {

			BigDecimal result = BigDecimal.ZERO;
			Control control = formData.getContainer().getControl(matcher.group(1), "\\.");

			for (ControlValue controlValue : formData.getFieldValue(control)) {
				String value = (String) controlValue.getValue();

				if (StringUtils.isNotEmpty(value)) {
					result = result.add(new BigDecimal(value));
				} else {
					result = BigDecimal.ZERO;
					break;
				}
			}
			formula = formula.replace(matcher.group(0), result.toPlainString());
		}

		return formula;
	}

}
