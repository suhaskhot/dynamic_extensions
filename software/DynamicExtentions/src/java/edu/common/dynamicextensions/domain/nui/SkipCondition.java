
package edu.common.dynamicextensions.domain.nui;

import edu.common.dynamicextensions.napi.ControlValue;

public class SkipCondition {

	public static enum RelationalOp {
		LT, LE, EQ, GE, GT
	}

	private Control sourceControl;

	private RelationalOp relationalOp;

	private String value;

	public Control getSourceControl() {
		return sourceControl;
	}

	public void setSourceControl(Control sourceControl) {
		this.sourceControl = sourceControl;
	}

	public RelationalOp getRelationalOp() {
		return relationalOp;
	}

	public void setRelationalOp(RelationalOp relationalOp) {
		this.relationalOp = relationalOp;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean evaluate(ControlValue fieldValue) {
		boolean result = false;
		Control control = fieldValue.getControl();
		if (this.value != null && fieldValue.getValue() != null) {
			if (fieldValue.getValue() instanceof String[]) {
				for (String string : (String[]) fieldValue.getValue()) {
					if (value.equals(string)) {
						result = true;
						break;
					}
				}

			} else {
				Comparable conditionValueObject = control.fromString(value);
				Comparable userValueObject = control.fromString((String) fieldValue.getValue());
				switch (relationalOp) {
					case LT :
						result = (userValueObject.compareTo(conditionValueObject) < 0 ? true : false);
						break;
					case GT :
						result = (userValueObject.compareTo(conditionValueObject) > 0 ? true : false);
						break;
					case EQ :
						result = (userValueObject.compareTo(conditionValueObject) == 0 ? true : false);
						break;
					case LE :
						result = (userValueObject.compareTo(conditionValueObject) <= 0 ? true : false);
						break;
					case GE :
						result = (userValueObject.compareTo(conditionValueObject) >= 0 ? true : false);
						break;
				}
			}
		}
		return result;
	}
}
