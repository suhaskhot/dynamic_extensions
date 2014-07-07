package edu.common.dynamicextensions.query;

import java.io.Serializable;

import edu.common.dynamicextensions.query.ast.ExpressionNode;
import edu.common.dynamicextensions.query.ast.FieldNode;

public class ResultColumn implements Serializable {
	private static final long serialVersionUID = 9037400363981158491L;

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
	
	public String getColumnLabel() {
		return getColumnLabel(null);
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
		
		return format(formatter, captions, instance);		
	}
	
	public String[] getCaptions() {
		if (!(columnExpr instanceof FieldNode)) {
			return new String[] {"Column - " + instance};
		}
		
		return ((FieldNode)columnExpr).getNodeCaptions();
	}
	
	public boolean isPhi() {
		return columnExpr.isPhi();
	}
	
	public String toString() {
		return getColumnLabel();
	}
	
	private String format(ResultColumnLabelFormatter formatter, String[] captions, int instance) {
		if (formatter != null) {
			return formatter.format(captions, instance);
		} 
		
		StringBuilder result = new StringBuilder();
		for (String caption : captions) {
			result.append(caption).append("#");
		}
		
		result.append(instance);
		return result.toString();
	}
}
