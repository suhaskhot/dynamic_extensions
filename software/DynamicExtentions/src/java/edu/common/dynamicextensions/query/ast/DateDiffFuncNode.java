package edu.common.dynamicextensions.query.ast;

import edu.common.dynamicextensions.domain.nui.DataType;

public class DateDiffFuncNode extends ExpressionNode {
	public static enum DiffType {
		DAY, MONTH, YEAR
	}
	
	private DiffType diffType;
	
	private ExpressionNode leftOperand;
	
	private ExpressionNode rightOperand;

	@Override
	public DataType getType() {
		return DataType.FLOAT;
	}

	public DiffType getDiffType() {
		return diffType;
	}

	public void setDiffType(DiffType diffType) {
		this.diffType = diffType;
	}

	public ExpressionNode getLeftOperand() {
		return leftOperand;
	}

	public void setLeftOperand(ExpressionNode leftOperand) {
		this.leftOperand = leftOperand;
	}

	public ExpressionNode getRightOperand() {
		return rightOperand;
	}

	public void setRightOperand(ExpressionNode rightOperand) {
		this.rightOperand = rightOperand;
	}
}