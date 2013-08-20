package edu.common.dynamicextensions.query;

public class QueryExpr implements Node {
	private SelectList selectList;
	
	private Node expr;

	public SelectList getSelectList() {
		return selectList;
	}

	public void setSelectList(SelectList selectList) {
		this.selectList = selectList;
	}

	public Node getExpr() {
		return expr;
	}

	public void setExpr(Node expr) {
		this.expr = expr;
	}
}
