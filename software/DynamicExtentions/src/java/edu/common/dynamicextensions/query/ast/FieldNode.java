package edu.common.dynamicextensions.query.ast;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DataType;

public class FieldNode extends ExpressionNode {
	private Control ctrl;
	
	private String name;
	
	private String tabAlias;
	
	private String[] nodeCaptions;

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

	public String[] getNodeCaptions() {
		return nodeCaptions;
	}

	public void setNodeCaptions(String[] nodeCaptions) {
		this.nodeCaptions = nodeCaptions;
	}

	@Override
	public DataType getType() {
		return ctrl != null ? ctrl.getDataType() : null;
	}
}
