package edu.common.dynamicextensions.query.ast;

import edu.common.dynamicextensions.domain.nui.DataType;

public abstract class ExpressionNode implements Node {
	public abstract DataType getType();
	
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
