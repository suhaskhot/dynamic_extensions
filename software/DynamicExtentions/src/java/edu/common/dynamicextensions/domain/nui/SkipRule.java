package edu.common.dynamicextensions.domain.nui;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;

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

	public void evaluate(FormData data) {
		boolean result = false;
		
		for (SkipCondition condition : conditions) {
			ControlValue fieldValue = data.getFieldValue(condition.getSourceControl().getName());
			result = condition.evaluate(fieldValue);
			
			if (result && logicalOp == LogicalOp.OR) {
				break;
			} else if (!result && logicalOp == LogicalOp.AND) {
				break;
			}			
		}
		
		for (SkipAction action : actions) {
			List<ControlValue> fieldValues = data.getFieldValue(action.getTargetCtrl());
			if (fieldValues == null) {
				continue;
			}
				
			for (ControlValue fieldValue : fieldValues) {
				if (result) {
					action.perform(fieldValue);
				} else {
					action.reset(fieldValue);
				}				
			}
		}
	}
}
