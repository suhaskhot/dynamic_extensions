package edu.common.dynamicextensions.query.ast;

import edu.common.dynamicextensions.domain.nui.DataType;

public abstract class ExpressionNode implements Node {
	private String label;
	
	public abstract DataType getType();
	
	public abstract ExpressionNode copy();
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getLabel() {
		return label;
	}
	
	public boolean isDate() {
		return getType() == DataType.DATE;
	}
	
	public boolean isString() {
		return getType() == DataType.STRING;
	}
	
	public boolean isNumber() {
		return isFloat() || isInteger() || isBoolean();
	}
	
	public boolean isFloat() {
		return getType() == DataType.FLOAT;
	}
	
	public boolean isInteger() {
		return getType() == DataType.INTEGER;
	}
	
	public boolean isBoolean() {
		return getType() == DataType.BOOLEAN;
	}
	
	public boolean isDateInterval() {
		return getType() == DataType.DATE_INTERVAL;
	}
}
