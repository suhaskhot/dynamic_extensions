package edu.common.dynamicextensions.domain.nui;

import java.util.LinkedHashSet;
import java.util.Set;

import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;

public class SkipRule {
	
	public static enum LogicalOp {
		AND, OR
	}
	
	private Set<SkipCondition> conditions = new LinkedHashSet<SkipCondition>();
	
	private LogicalOp logicalOp = LogicalOp.AND;
	
	private Action action;
		
	public Set<SkipCondition> getConditions() {
		return conditions;
	}

	public void setConditions(Set<SkipCondition> conditions) {
		this.conditions = conditions;
	}

	public LogicalOp getLogicalOp() {
		return logicalOp;
	}

	public void setLogicalOp(LogicalOp logicalOp) {
		this.logicalOp = logicalOp;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}	

	public void evaluate(FormData data, ControlValue targetControl, Integer rowNumber) {
		boolean validate = false;

		for (SkipCondition condition : conditions) {
			ControlValue fieldValue = data.getFieldValue(condition.getSourceControl(), rowNumber);
			validate = condition.evaluate(fieldValue);

			if (validate && logicalOp == LogicalOp.OR) {
				break;
			} else if (!validate && logicalOp == logicalOp.AND) {
				break;
			}
		}

		if (validate) {
			action.perform(targetControl);
		} else {
			action.reset(targetControl);
		}
	}

}
