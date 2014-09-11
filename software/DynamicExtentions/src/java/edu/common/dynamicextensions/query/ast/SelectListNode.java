package edu.common.dynamicextensions.query.ast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SelectListNode implements Node, Serializable {

	private static final long serialVersionUID = -3175328287644413811L;
	
	private List<ExpressionNode> elements = new ArrayList<ExpressionNode>();
	
	public void addElement(ExpressionNode element) {
		elements.add(element);
	}
	
	public List<ExpressionNode> getElements() {
		return elements;
	}
	
	public boolean hasAggregateExpr() {
		for (ExpressionNode element : elements) {
			if (element.isAggregateExpression()) {
				return true;
			}
		}
		
		return false;
	}
	
}
