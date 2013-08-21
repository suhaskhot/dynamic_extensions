package edu.common.dynamicextensions.query;

import java.util.ArrayList;
import java.util.List;

public class SelectList implements Node {
	private List<ConditionOperand> elements = new ArrayList<ConditionOperand>();
	
	public void addElement(ConditionOperand element) {
		elements.add(element);
	}
	
	public List<ConditionOperand> getElements() {
		return elements;
	}
	
}
