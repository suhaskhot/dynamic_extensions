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
	
	@Override
	public DateDiffFuncNode copy() {
		DateDiffFuncNode copy = new DateDiffFuncNode();
		copy.setLabel(this.getLabel());
		copy.setDiffType(diffType);
		copy.setLeftOperand(leftOperand.copy());
		copy.setRightOperand(rightOperand.copy());
		return copy;
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
		
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result	+ ((diffType == null) ? 0 : diffType.hashCode());
		result = prime * result	+ ((leftOperand == null) ? 0 : leftOperand.hashCode());
		result = prime * result	+ ((rightOperand == null) ? 0 : rightOperand.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null || getClass() != obj.getClass()) { 
			return false;
		}
		
		DateDiffFuncNode other = (DateDiffFuncNode) obj;
		if (diffType != other.diffType) {
			return false;
		} 
		
		if (leftOperand == null && other.leftOperand != null) {
			return false;
		} else if (!leftOperand.equals(other.leftOperand)) {
			return false;
		} 
		
		if (rightOperand == null && other.rightOperand != null) {
			return false;
		} else if (!rightOperand.equals(other.rightOperand)) {
			return false;
		}
		
		return true;
	}	
}