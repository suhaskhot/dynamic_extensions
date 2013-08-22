package edu.common.dynamicextensions.query.ast;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domain.nui.DataType;

public class LiteralValueNode extends ExpressionNode {
	private DataType literalType;
	
	private List<Object> values = new ArrayList<Object>();
	
	public LiteralValueNode(DataType literalType) {
		this.literalType = literalType;
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}

	@Override
	public DataType getType() {
		return literalType;
	}	
}