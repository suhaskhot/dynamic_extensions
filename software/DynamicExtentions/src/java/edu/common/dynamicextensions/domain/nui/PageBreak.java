package edu.common.dynamicextensions.domain.nui;

import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.impl.FormRenderer.ContextParameter;

public class PageBreak extends Control {
	private static final long serialVersionUID = 1L;

	@Override
	public DataType getDataType() {
		return null;
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		return null;
	}

	@Override
	public <T> T fromString(String value) {
		return null;
	}

	@Override
	protected String render(String controlName, ControlValue controlValue,
			Map<ContextParameter, String> contextParameter) {
		return null;
	}
}
