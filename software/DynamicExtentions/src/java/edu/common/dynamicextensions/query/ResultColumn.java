package edu.common.dynamicextensions.query;

import edu.common.dynamicextensions.query.ast.ExpressionNode;
import edu.common.dynamicextensions.query.ast.FieldNode;

public class ResultColumn {
	private ExpressionNode columnExpr;
	
	private int instance;
	
	private Object value;
	
	public ResultColumn(ExpressionNode columnExpr, int instance) {
		this.columnExpr = columnExpr;
		this.instance = instance;
	}
	
	public ResultColumn(ExpressionNode columnExpr, Object value) {
		this.columnExpr = columnExpr;
		this.value = value;
	}
	
	public ExpressionNode getExpression() {
		return columnExpr;
	}
	
	public Object getValue() {
		return value;
	}
	
	public String getColumnLabel(ResultColumnLabelFormatter formatter) {
		String[] captions;
		if (columnExpr.getLabel() != null) {
			captions = new String[] {columnExpr.getLabel()};
		} else if (!(columnExpr instanceof FieldNode)) {
			captions = new String[] {"Column"};
		} else {
			captions = ((FieldNode)columnExpr).getNodeCaptions();
		}
		
		return formatter.format(captions, instance);		
	}
	
	public String[] getCaptions() {
		if (!(columnExpr instanceof FieldNode)) {
			return new String[] {"Column - " + instance};
		}
		
		return ((FieldNode)columnExpr).getNodeCaptions();
	}
}
