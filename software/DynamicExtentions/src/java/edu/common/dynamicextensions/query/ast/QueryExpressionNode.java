package edu.common.dynamicextensions.query.ast;


public class QueryExpressionNode implements Node {
	private SelectListNode selectList;
	
	private FilterExpressionNode filterExpr;
	
	private LimitExprNode limitExpr;
	
	private CrosstabNode crosstabSpec;
	
	private String resultPostProc;

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
	
	public CrosstabNode getCrosstabSpec() {
		return crosstabSpec;
	}

	public void setCrosstabSpec(CrosstabNode crosstabSpec) {
		this.crosstabSpec = crosstabSpec;
	}

	public String getResultPostProc() {
		return crosstabSpec != null ? crosstabSpec.getName() : resultPostProc;
	}

	public void setResultPostProc(String resultPostProc) {
		this.resultPostProc = resultPostProc;
	}

	public boolean isAggregateQuery() {
		return selectList.hasAggregateExpr();
	}
	
	public boolean hasResultPostProc() {
		return crosstabSpec != null || resultPostProc != null;
	}
}
