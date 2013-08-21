package edu.common.dynamicextensions.query;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.domain.nui.DataType;

public class Value extends ConditionOperand {
	private List<Object> values = new ArrayList<Object>();

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}

	@Override
	public DataType getType() {
		Object value = values != null && !values.isEmpty() ? values.get(0) : null;
		
		DataType result = null;
		if (value instanceof Long || value instanceof Integer) {
			result = DataType.INTEGER;
		} else if (value instanceof String) {
			result = DataType.STRING;
		} else if (value instanceof Double || value instanceof Float) {
			result = DataType.FLOAT;
		} else if (value != null){
			throw new RuntimeException("Unknown type: " + value.getClass());
		}
		
		return result;
	}
}