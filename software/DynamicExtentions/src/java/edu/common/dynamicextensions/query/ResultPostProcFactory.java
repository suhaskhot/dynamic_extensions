package edu.common.dynamicextensions.query;

import edu.common.dynamicextensions.query.ast.QueryExpressionNode;

public interface ResultPostProcFactory {
	public ResultPostProc create(QueryExpressionNode queryExpr);
}
