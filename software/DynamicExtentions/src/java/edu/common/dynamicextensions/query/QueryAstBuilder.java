package edu.common.dynamicextensions.query;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;

import edu.common.dynamicextensions.query.ArithExpression.ArithOp;
import edu.common.dynamicextensions.query.DateDiff.DiffType;
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
    	queryExpr.setExpr(visit(ctx.filter_expr()));
    	return queryExpr;
    }    
    
    @Override
    public SelectList visitSelectExpr(@NotNull AQLParser.SelectExprContext ctx) { 
    	SelectList list = new SelectList();
    	
    	for (int i = 0; i < ctx.arith_expr().size(); ++i) {
    		list.addElement((ConditionOperand)visit(ctx.arith_expr(i)));    		
    	}
    	
    	return list;  
    }

    @Override
    public Expression visitAndFilterExpr(@NotNull AQLParser.AndFilterExprContext ctx) {
        return Expression.andExpr(visit(ctx.filter_expr(0)), visit(ctx.filter_expr(1)));
    }

    @Override
    public Node visitOrFilterExpr(@NotNull AQLParser.OrFilterExprContext ctx) {
        return Expression.orExpr(visit(ctx.filter_expr(0)), visit(ctx.filter_expr(1)));
    }
    
    @Override
    public Node visitNotFilterExpr(@NotNull AQLParser.NotFilterExprContext ctx) {
        return Expression.notExpr(visit(ctx.filter_expr()));
    }

    @Override
    public Expression visitParensFilterExpr(@NotNull AQLParser.ParensFilterExprContext ctx) {
        return Expression.parenExpr(visit(ctx.filter_expr()));
    }

    @Override
    public Node visitFilter(@NotNull AQLParser.FilterContext ctx) {
    	String inputSymbol = ctx.OP().getText();
    	
    	Filter filter = new Filter();
    	filter.setLhs((ConditionOperand)visit(ctx.arith_expr(0)));
    	filter.setRhs((ConditionOperand)visit(ctx.arith_expr(1)));
    	filter.setRelOp(RelationalOp.getBySymbol(inputSymbol));    	
    	return filter;    	
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
    public ArithExpression visitDateIntervalExpr(@NotNull AQLParser.DateIntervalExprContext ctx) {
    	ConditionOperand loperand = (ConditionOperand)visit(ctx.arith_expr());
    	ConditionOperand roperand = (ConditionOperand)visit(ctx.date_interval());    	
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
    public DateDiff visitMonthsDiffFunc(@NotNull AQLParser.MonthsDiffFuncContext ctx) { 
    	DateDiff diff = new DateDiff();
    	diff.setDiffType(DiffType.MONTH);
    	
    	ArithExpression expr = (ArithExpression)visit(ctx.arith_expr());
    	if (expr.getOp() != ArithOp.MINUS) {
    		throw new RuntimeException("Invalid operation inside months: " + expr.getOp());
    	}
    	
    	diff.setLeftOperand(expr.getLeftOperand());
    	diff.setRightOperand(expr.getRightOperand());
    	return diff;
    }

    @Override 
    public DateDiff visitYearsDiffFunc(@NotNull AQLParser.YearsDiffFuncContext ctx) { 
    	DateDiff diff = new DateDiff();
    	diff.setDiffType(DiffType.YEAR);

    	ArithExpression expr = (ArithExpression)visit(ctx.arith_expr());
    	if (expr.getOp() != ArithOp.MINUS) {
    		throw new RuntimeException("Invalid operation inside years: " + expr.getOp());
    	}
    	
    	System.err.println("L:" + expr.getLeftOperand() + "R: " + expr.getRightOperand());
    	diff.setLeftOperand(expr.getLeftOperand());
    	diff.setRightOperand(expr.getRightOperand());
    	return diff;
    }
    
    @Override 
    public Field visitField(@NotNull AQLParser.FieldContext ctx) {
    	Field field = new Field();
    	field.setName(ctx.FIELD().getText());
    	return field; 
    }    
        
    @Override
    public Value visitStringLiteral(@NotNull AQLParser.StringLiteralContext ctx) {
    	Value value = new Value();
    	value.getValues().add(ctx.SLITERAL().getText());
    	return value;
    }
    
    @Override
    public Value visitIntLiteral(@NotNull AQLParser.IntLiteralContext ctx) {
    	Value value = new Value();
    	value.getValues().add(Long.parseLong(ctx.INT().getText()));
    	return value;
    }

    @Override
    public Value visitFloatLiteral(@NotNull AQLParser.FloatLiteralContext ctx) {
    	Value value = new Value();
    	value.getValues().add(Double.parseDouble(ctx.FLOAT().getText()));
    	return value;
    }
    
    @Override 
    public DateInterval visitDate_interval(@NotNull AQLParser.Date_intervalContext ctx) {
    	DateInterval di = new DateInterval();
    	
    	di.setDays(diPart(ctx.DAY()));
    	di.setMonths(diPart(ctx.MONTH()));
    	di.setYears(diPart(ctx.YEAR()));
    	return di;    	
    }
    
    private int diPart(TerminalNode term) {
    	int result = 0;
    	
    	if (term != null && term.getText() != null && !term.getText().isEmpty()) {
    		String text = term.getText().substring(0, term.getText().length() - 1);
    		result = Integer.parseInt(text);
    	}
    	
    	return result;
    }
}