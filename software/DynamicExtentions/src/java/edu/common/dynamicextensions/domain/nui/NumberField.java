
package edu.common.dynamicextensions.domain.nui;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public class NumberField extends TextField {
	//
	// TODO: noOfDigits and noOfDigitsAfterDecimal needs to be pushed
	// to rule
	//
	private int noOfDigits;

	private int noOfDigitsAfterDecimal;

	private boolean calculated;

	private String formula;

	private String measurementUnits;

	public int getNoOfDigits() {
		return noOfDigits;
	}

	public void setNoOfDigits(int noOfDigits) {
		this.noOfDigits = noOfDigits;
	}

	public int getNoOfDigitsAfterDecimal() {
		return noOfDigitsAfterDecimal;
	}

	public void setNoOfDigitsAfterDecimal(int noOfDigitsAfterDecimal) {
		this.noOfDigitsAfterDecimal = noOfDigitsAfterDecimal;
	}

	public boolean isCalculated() {
		return calculated;
	}

	public void setCalculated(boolean calculated) {
		this.calculated = calculated;
	}

	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}
	

	@Override
	public boolean isDynamic() {
		return super.isDynamic() || isCalculated();
	}

	public void setMinValue(String minValue) {
		if (StringUtils.isBlank(minValue)) {
			return;
		}

		addValidationRule("range", Collections.singletonMap("min", minValue));
	}
	
	public String getMinValue() {
		return getValidationRuleParam("range", "min");
	}

	public void setMaxValue(String maxValue) {
		if (StringUtils.isBlank(maxValue)) {
			return;
		}

		addValidationRule("range", Collections.singletonMap("max", maxValue));
	}
	
	public String getMaxValue() {
		return getValidationRuleParam("range", "max");
	}

	public String getMeasurementUnits() {
		return measurementUnits;
	}

	public void setMeasurementUnits(String measurementUnits) {
		this.measurementUnits = measurementUnits;
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		return Collections.singletonList(ColumnDef.get(getDbColumnName(), "DECIMAL(19, 6)"));
	}

	@Override
	public DataType getDataType() {
		return noOfDigitsAfterDecimal == 0 ? DataType.INTEGER : DataType.FLOAT;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BigDecimal fromString(String value) {
		if (value == null || value.trim().isEmpty()) {
			return null;
		}

		return new BigDecimal(value);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + noOfDigits;
		result = prime * result + noOfDigitsAfterDecimal;		
		result = prime * result + (calculated ? 1231 : 1237);
		result = prime * result + ((formula == null) ? 0 : formula.hashCode());
		result = prime * result	+ ((measurementUnits == null) ? 0 : measurementUnits.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!super.equals(obj)) {
			return false;
		}
		
		NumberField other = (NumberField) obj;
		if (noOfDigits != other.noOfDigits ||
			noOfDigitsAfterDecimal != other.noOfDigitsAfterDecimal ||
			calculated != other.calculated ||
			!StringUtils.equals(formula, other.formula) ||
			!StringUtils.equals(measurementUnits, other.measurementUnits)) {
			return false;
		}
		
		return true;
	}
	
	@Override
	public String toString(Object value) {
		if (!(value instanceof BigDecimal)) {
			return null;
		}
		BigDecimal numberValue = (BigDecimal) value;

		if (BigDecimal.ZERO.compareTo(numberValue) == 0) {
			numberValue = BigDecimal.ZERO;
		} else {
			numberValue = numberValue.setScale(noOfDigitsAfterDecimal, RoundingMode.HALF_UP).stripTrailingZeros();
		}
		return numberValue.toPlainString();
	}
}
