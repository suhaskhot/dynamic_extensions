package edu.common.dynamicextensions.query;

import edu.common.dynamicextensions.query.ast.ExpressionNode;
import edu.common.dynamicextensions.query.ast.FieldNode;

public class ResultColumn {
	private ExpressionNode columnExpr;
	
	private int instance;
	
	public ResultColumn(ExpressionNode columnExpr, int instance) {
		this.columnExpr = columnExpr;
		this.instance = instance;
	}
	
	public ExpressionNode getExpression() {
		return columnExpr;
	}
	
	public String getColumnLabel(ResultColumnLabelFormatter formatter) {
		if (!(columnExpr instanceof FieldNode)) {
			return "Column - " + instance;
		}
		
		FieldNode field = (FieldNode)columnExpr;
		return formatter.format(field.getNodeCaptions(), instance);		
	}
	
	public String[] getCaptions() {
		if (!(columnExpr instanceof FieldNode)) {
			return new String[] {"Column - " + instance};
		}
		
		return ((FieldNode)columnExpr).getNodeCaptions();
	}
}
