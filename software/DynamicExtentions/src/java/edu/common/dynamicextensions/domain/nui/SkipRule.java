package edu.common.dynamicextensions.domain.nui;

import java.util.ArrayList;
import java.util.List;

public class SkipRule {
	
	public static enum LogicalOp {
		AND, OR
	}
	
	private List<SkipCondition> conditions = new ArrayList<SkipCondition>();
	
	private LogicalOp logicalOp = LogicalOp.AND;
	
	private List<SkipAction> actions = new ArrayList<SkipAction>();
	
	public List<SkipCondition> getConditions() {
		return conditions;
	}

	public void setConditions(List<SkipCondition> conditions) {
		this.conditions = conditions;
	}

	public LogicalOp getLogicalOp() {
		return logicalOp;
	}

	public void setLogicalOp(LogicalOp logicalOp) {
		this.logicalOp = logicalOp;
	}

	public List<SkipAction> getActions() {
		return actions;
	}

	public void setActions(List<SkipAction> actions) {
		this.actions = actions;
	}
}
