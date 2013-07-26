package edu.common.dynamicextensions.query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Expression implements Node {
	public static enum Op {
		AND, OR, NOT, PAND, PARENTHESIS;
	}
	
	private List<Node> operands = new ArrayList<Node>();
	
	private Op operator; 
	

    public Op getOperator() {
        return operator;
    }

    public void setOperator(Op operator) {
        this.operator = operator;
    }

    public List<Node> getOperands() {
        return operands;
    }

    public void setOperands(List<Node> operands) {
        this.operands = operands;
    }

    public static Expression notExpr(Node operand) {
        return makeExpression(Op.NOT, new Node[] { operand });
    }

    public static Expression andExpr(Node ... operands)  {
        return makeExpression(Op.AND, operands);
    }

    public static Expression orExpr(Node ... operands)  {
        return makeExpression(Op.OR, operands);
    }

    public static Expression pAndExpr(Node ... operands)  {
        return makeExpression(Op.PAND, operands);
    }

    public static Expression parenExpr(Node operand)  {
        return makeExpression(Op.PARENTHESIS, new Node[] {operand});
    }

    private static Expression makeExpression(Op op, Node operands[])  {
        Expression expr = new Expression();
        expr.setOperator(op);
        expr.getOperands().addAll(Arrays.asList(operands));
        return expr;
    }
}