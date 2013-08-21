package edu.common.dynamicextensions.query;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DataType;

public class Field extends ConditionOperand {
	private Control ctrl;
	
	private String name;
	
	private String tabAlias;

	public Control getCtrl() {
		return ctrl;
	}

	public void setCtrl(Control ctrl) {
		this.ctrl = ctrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTabAlias() {
		return tabAlias;
	}

	public void setTabAlias(String tabAlias) {
		this.tabAlias = tabAlias;
	}

	@Override
	public DataType getType() {
		return ctrl != null ? ctrl.getDataType() : null;
	}
}
