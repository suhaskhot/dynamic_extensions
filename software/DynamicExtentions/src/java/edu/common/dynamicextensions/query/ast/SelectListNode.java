package edu.common.dynamicextensions.query.ast;

import java.util.ArrayList;
import java.util.List;


public class SelectListNode implements Node {
	private List<ExpressionNode> elements = new ArrayList<ExpressionNode>();
	
	public void addElement(ExpressionNode element) {
		elements.add(element);
	}
	
	public List<ExpressionNode> getElements() {
		return elements;
	}
	
}
