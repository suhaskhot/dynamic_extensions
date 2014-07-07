package edu.common.dynamicextensions.query.ast;

import java.io.Serializable;

import edu.common.dynamicextensions.domain.nui.DataType;

public class CurrentDateNode extends ExpressionNode implements Serializable {
	private static final long serialVersionUID = -503108091981431728L;

	@Override
	public DataType getType() {
		return DataType.DATE;
	}

	@Override
	public ExpressionNode copy() {
		return new CurrentDateNode();
	}

	@Override
	public String[] getFormNames() {
		return new String[0];
	}

	@Override
	public boolean isPhi() {
		return false;
	}
}
