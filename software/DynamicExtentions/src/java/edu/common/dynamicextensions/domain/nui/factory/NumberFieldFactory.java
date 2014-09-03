package edu.common.dynamicextensions.domain.nui.factory;

import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.NumberField;
import static edu.common.dynamicextensions.nutility.ParserUtil.*;

public class NumberFieldFactory extends AbstractControlFactory {
	public static NumberFieldFactory getInstance() {
		return new NumberFieldFactory();
	}

	@Override
	public String getType() {
		return "numberField";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		NumberField numberField = new NumberField();
		setControlProps(numberField, ele, row, xPos);
		
		Integer width = getIntValue(ele, "width", null);
		if (width != null) {
			numberField.setNoOfColumns(width);
		}
		String defVal = getTextValue(ele, "defaultValue", "");
		numberField.setDefaultValue(defVal);
		
		Integer digitsAfterDecimal = getIntValue(ele, "noOfDigitsAfterDecimal", null);
		if (digitsAfterDecimal != null) {
			numberField.setNoOfDigitsAfterDecimal(digitsAfterDecimal);
		}
		numberField.setNoOfDigits(getIntValue(ele, "noOfDigits", 19));
		numberField.setMeasurementUnits(getTextValue(ele, "measurementUnits"));
		
		numberField.setFormula(getTextValue(ele, "formula"));
		if (numberField.getFormula() != null && !numberField.getFormula().trim().isEmpty()) {
			numberField.setCalculated(true);
		}
		
		numberField.setMinValue(getTextValue(ele, "minValue"));
		numberField.setMaxValue(getTextValue(ele, "maxValue"));
		return numberField;
	}
}

