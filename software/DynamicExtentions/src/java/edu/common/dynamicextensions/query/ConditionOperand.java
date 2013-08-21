package edu.common.dynamicextensions.query;

import edu.common.dynamicextensions.domain.nui.DataType;

public abstract class ConditionOperand implements Node {
	public abstract DataType getType();
	
	public boolean isDate() {
		return getType() == DataType.DATE;
	}
	
	public boolean isString() {
		return getType() == DataType.STRING;
	}
	
	public boolean isNumber() {
		return getType() == DataType.INTEGER || getType() == DataType.FLOAT || getType() == DataType.BOOLEAN;
	}
}
