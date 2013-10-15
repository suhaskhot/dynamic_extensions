package edu.common.dynamicextensions.query.ast;

import edu.common.dynamicextensions.domain.nui.DataType;

public class CurrentDateNode extends ExpressionNode {
	@Override
	public DataType getType() {
		return DataType.DATE;
	}

	@Override
	public ExpressionNode copy() {
		return new CurrentDateNode();
	}
}
