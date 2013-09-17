package edu.common.dynamicextensions.query.ast;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domain.nui.DataType;

public class LiteralValueListNode extends ExpressionNode {
	
	private List<LiteralValueNode> literalVals = new ArrayList<LiteralValueNode>();

	@Override
	public DataType getType() {
		DataType type = null;
		if (literalVals.size() > 0) {
			type = literalVals.get(0).getType();
		}
		return type;
	}
	
	public void addLiteralVa(LiteralValueNode literal) {
		this.literalVals.add(literal);
	}
	
	public List<LiteralValueNode> getLiteralVals() {
		return literalVals;
	}
}
