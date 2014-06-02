package edu.common.dynamicextensions.query.ast;

import java.io.Serializable;

import edu.common.dynamicextensions.domain.nui.DataType;

public abstract class ExpressionNode implements Node, Serializable {
	private static final long serialVersionUID = -4913352175312731795L;

	private int pos;
	
	private String label;
	
	public abstract DataType getType();
	
	public abstract ExpressionNode copy();
	
	public abstract String[] getFormNames();
	
	public int getPos() {
		return pos;
	}
	
	public void setPos(int pos) {
		this.pos = pos;
	}
	
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
