package edu.common.dynamicextensions.domain.nui;

import java.util.List;
import java.util.Map;

public class PageBreak extends Control {
	private static final long serialVersionUID = 8682630795270255415L;

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
	public void getProps(Map<String, Object> props) {
		props.put("type", "pageBreak");	
	}
}
