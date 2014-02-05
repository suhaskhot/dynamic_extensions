package edu.common.dynamicextensions.query.ast;


public class QueryExpressionNode implements Node {
	private SelectListNode selectList;
	
	private FilterExpressionNode filterExpr;
	
	private LimitExprNode limitExpr;

	public SelectListNode getSelectList() {
		return selectList;
	}

	public void setSelectList(SelectListNode selectList) {
		this.selectList = selectList;
	}

	public FilterExpressionNode getFilterExpr() {
		return filterExpr;
	}

	public void setFilterExpr(FilterExpressionNode filterExpr) {
		this.filterExpr = filterExpr;
	}

	public LimitExprNode getLimitExpr() {
		return limitExpr;
	}

	public void setLimitExpr(LimitExprNode limitExpr) {
		this.limitExpr = limitExpr;
	}
}
