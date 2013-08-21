package edu.common.dynamicextensions.query;

import org.antlr.v4.runtime.misc.NotNull;

import edu.common.dynamicextensions.query.ArithExpression.ArithOp;
import edu.common.dynamicextensions.query.Filter.RelationalOp;
import edu.common.dynamicextensions.query.antlr.AQLBaseVisitor;
import edu.common.dynamicextensions.query.antlr.AQLParser;

public class QueryAstBuilder extends AQLBaseVisitor<Node> {

    public QueryAstBuilder() {
    }
    
    @Override 
    public QueryExpr visitQueryExpr(@NotNull AQLParser.QueryExprContext ctx) {  
    	QueryExpr queryExpr = new QueryExpr();
    	queryExpr.setSelectList((SelectList)visit(ctx.select_list()));
    	queryExpr.setExpr(visit(ctx.expr()));
    	return queryExpr;
    }    
    
    @Override
    public SelectList visitSelectList(@NotNull AQLParser.SelectListContext ctx) { 
    	SelectList list = new SelectList();
    	
    	for (int i = 0; i < ctx.arith_expr().size(); ++i) {
    		list.addElement((ConditionOperand)visit(ctx.arith_expr(i)));    		
    	}
    	
    	return list;  
    }

    @Override
    public Node visitNotExpr(@NotNull AQLParser.NotExprContext ctx) {
        return Expression.notExpr(visit(ctx.expr()));
    }

    @Override
    public Node visitOrExpr(@NotNull AQLParser.OrExprContext ctx) {
        return Expression.orExpr(visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public Expression visitAndExpr(@NotNull AQLParser.AndExprContext ctx) {
        return Expression.andExpr(visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    @Override
    public Expression visitParensExpr(@NotNull AQLParser.ParensExprContext ctx) {
        return Expression.parenExpr(visit(ctx.expr()));
    }

    @Override
    public Node visitCond(@NotNull AQLParser.CondContext ctx) {
    	String inputSymbol = ctx.OP().getText();
    	
    	Filter filter = new Filter();
    	filter.setLhs((ConditionOperand)visit(ctx.arith_expr(0)));
    	filter.setRhs((ConditionOperand)visit(ctx.arith_expr(1)));
    	filter.setRelOp(RelationalOp.getBySymbol(inputSymbol));    	
    	return filter;    	
    }
    
    @Override 
    public Field visitFieldExpr(@NotNull AQLParser.FieldExprContext ctx) {
    	Field field = new Field();
    	field.setName(ctx.FIELD().getText());
    	return field; 
    }    
    
    @Override
    public ArithExpression visitArithExpr(@NotNull AQLParser.ArithExprContext ctx) {
    	ConditionOperand loperand = (ConditionOperand)visit(ctx.arith_expr(0));
    	ConditionOperand roperand = (ConditionOperand)visit(ctx.arith_expr(1));
    	ArithOp op = ArithOp.getBySymbol(ctx.ARITH_OP().getText());
    	
    	ArithExpression expr = new ArithExpression();
    	expr.setLeftOperand(loperand);
    	expr.setRightOperand(roperand);
    	expr.setOp(op);
    	return expr;
    }
    
    @Override 
    public ArithExpression visitParensArithExpr(@NotNull AQLParser.ParensArithExprContext ctx) { 
    	return (ArithExpression)visit(ctx.arith_expr()); 
    }

    @Override
    public Value visitStringExpr(@NotNull AQLParser.StringExprContext ctx) {
    	Value value = new Value();
    	value.getValues().add(ctx.SLITERAL().getText());
    	return value;
    }
    
    @Override
    public Value visitIntExpr(@NotNull AQLParser.IntExprContext ctx) {
    	Value value = new Value();
    	value.getValues().add(Long.parseLong(ctx.INT().getText()));
    	return value;
    }
    
    @Override
    public Value visitFloatExpr(@NotNull AQLParser.FloatExprContext ctx) {
    	Value value = new Value();
    	value.getValues().add(Double.parseDouble(ctx.FLOAT().getText()));
    	return value;
    }
}