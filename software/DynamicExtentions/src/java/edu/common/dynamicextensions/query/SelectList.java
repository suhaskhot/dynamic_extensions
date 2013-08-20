package edu.common.dynamicextensions.query;

import java.util.ArrayList;
import java.util.List;

public class SelectList implements Node {
	private List<Field> fields = new ArrayList<Field>();
	
	public void addField(Field field) {
		fields.add(field);
	}
	
	public List<Field> getFields() {
		return fields;
	}
	
}
