package edu.common.dynamicextensions.query;

import edu.common.dynamicextensions.domain.nui.DataType;

public class DateDiff extends ConditionOperand {
	public static enum DiffType {
		DAY, MONTH, YEAR
	}
	
	private DiffType diffType;
	
	private ConditionOperand leftOperand;
	
	private ConditionOperand rightOperand;

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

	public ConditionOperand getLeftOperand() {
		return leftOperand;
	}

	public void setLeftOperand(ConditionOperand leftOperand) {
		this.leftOperand = leftOperand;
	}

	public ConditionOperand getRightOperand() {
		return rightOperand;
	}

	public void setRightOperand(ConditionOperand rightOperand) {
		this.rightOperand = rightOperand;
	}

}
