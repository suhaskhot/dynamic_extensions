package edu.common.dynamicextensions.query;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;

import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.query.antlr.AQLBaseVisitor;
import edu.common.dynamicextensions.query.antlr.AQLParser;
import edu.common.dynamicextensions.query.ast.ArithExpressionNode;
import edu.common.dynamicextensions.query.ast.DateDiffFuncNode;
import edu.common.dynamicextensions.query.ast.DateIntervalNode;
import edu.common.dynamicextensions.query.ast.FilterExpressionNode;
import edu.common.dynamicextensions.query.ast.ExpressionNode;
import edu.common.dynamicextensions.query.ast.FieldNode;
import edu.common.dynamicextensions.query.ast.FilterNode;
import edu.common.dynamicextensions.query.ast.FilterNodeMarker;
import edu.common.dynamicextensions.query.ast.LiteralValueNode;
import edu.common.dynamicextensions.query.ast.Node;
import edu.common.dynamicextensions.query.ast.QueryExpressionNode;
import edu.common.dynamicextensions.query.ast.SelectListNode;
import edu.common.dynamicextensions.query.ast.ArithExpressionNode.ArithOp;
import edu.common.dynamicextensions.query.ast.DateDiffFuncNode.DiffType;
import edu.common.dynamicextensions.query.ast.FilterNode.RelationalOp;

public class QueryAstBuilder extends AQLBaseVisitor<Node> {

    public QueryAstBuilder() {
    }
    
    @Override 
    public QueryExpressionNode visitQueryExpr(@NotNull AQLParser.QueryExprContext ctx) {  
    	QueryExpressionNode queryExpr = new QueryExpressionNode();
    	queryExpr.setSelectList((SelectListNode)visit(ctx.select_list()));
    	queryExpr.setFilterExpr((FilterExpressionNode)visit(ctx.filter_expr()));
    	return queryExpr;
    }    
    
    @Override
    public SelectListNode visitSelectExpr(@NotNull AQLParser.SelectExprContext ctx) { 
    	SelectListNode list = new SelectListNode();
    	
    	for (int i = 0; i < ctx.arith_expr().size(); ++i) {
    		list.addElement((ExpressionNode)visit(ctx.arith_expr(i)));    		
    	}
    	
    	return list;  
    }

    @Override
    public FilterExpressionNode visitAndFilterExpr(@NotNull AQLParser.AndFilterExprContext ctx) {
        return FilterExpressionNode.andExpr(
        		(FilterNodeMarker)visit(ctx.filter_expr(0)), 
        		(FilterNodeMarker)visit(ctx.filter_expr(1)));
    }

    @Override
    public Node visitOrFilterExpr(@NotNull AQLParser.OrFilterExprContext ctx) {
        return FilterExpressionNode.orExpr(
        		(FilterNodeMarker)visit(ctx.filter_expr(0)), 
        		(FilterNodeMarker)visit(ctx.filter_expr(1)));
    }
    
    @Override
    public Node visitNotFilterExpr(@NotNull AQLParser.NotFilterExprContext ctx) {
        return FilterExpressionNode.notExpr((FilterNodeMarker)visit(ctx.filter_expr()));
    }

    @Override
    public FilterExpressionNode visitParensFilterExpr(@NotNull AQLParser.ParensFilterExprContext ctx) {
        return FilterExpressionNode.parenExpr((FilterNodeMarker)visit(ctx.filter_expr()));
    }
    
    @Override
    public FilterExpressionNode visitSimpleFilter(@NotNull AQLParser.SimpleFilterContext ctx) {
    	return FilterExpressionNode.identity((FilterNodeMarker)visit(ctx.filter()));
    }

    @Override
    public FilterNode visitFilter(@NotNull AQLParser.FilterContext ctx) {
    	String inputSymbol = ctx.OP().getText();
    	
    	FilterNode filter = new FilterNode();
    	filter.setLhs((ExpressionNode)visit(ctx.arith_expr(0)));
    	filter.setRhs((ExpressionNode)visit(ctx.arith_expr(1)));
    	filter.setRelOp(RelationalOp.getBySymbol(inputSymbol));    	
    	return filter;    	
    }

    @Override
    public ArithExpressionNode visitArithExpr(@NotNull AQLParser.ArithExprContext ctx) {
    	ExpressionNode loperand = (ExpressionNode)visit(ctx.arith_expr(0));
    	ExpressionNode roperand = (ExpressionNode)visit(ctx.arith_expr(1));
    	ArithOp op = ArithOp.getBySymbol(ctx.ARITH_OP().getText());
    	
    	ArithExpressionNode expr = new ArithExpressionNode();
    	expr.setLeftOperand(loperand);
    	expr.setRightOperand(roperand);
    	expr.setOp(op);
    	return expr;
    }

    @Override 
    public ArithExpressionNode visitDateIntervalExpr(@NotNull AQLParser.DateIntervalExprContext ctx) {
    	ExpressionNode loperand = (ExpressionNode)visit(ctx.arith_expr());
    	ExpressionNode roperand = (ExpressionNode)visit(ctx.date_interval());    	
    	ArithOp op = ArithOp.getBySymbol(ctx.ARITH_OP().getText());
    	    	
    	ArithExpressionNode expr = new ArithExpressionNode();
    	expr.setLeftOperand(loperand);
    	expr.setRightOperand(roperand);
    	expr.setOp(op);    	
    	return expr; 
    }

    @Override 
    public ArithExpressionNode visitParensArithExpr(@NotNull AQLParser.ParensArithExprContext ctx) { 
    	return (ArithExpressionNode)visit(ctx.arith_expr()); 
    }

    @Override 
    public DateDiffFuncNode visitMonthsDiffFunc(@NotNull AQLParser.MonthsDiffFuncContext ctx) { 
    	return getDateDiffFuncNode(DiffType.MONTH, (ArithExpressionNode)visit(ctx.arith_expr()));
    }

    @Override 
    public DateDiffFuncNode visitYearsDiffFunc(@NotNull AQLParser.YearsDiffFuncContext ctx) {
    	return getDateDiffFuncNode(DiffType.YEAR, (ArithExpressionNode)visit(ctx.arith_expr()));
    }
    
    @Override 
    public FieldNode visitField(@NotNull AQLParser.FieldContext ctx) {
    	FieldNode field = new FieldNode();
    	field.setName(ctx.FIELD().getText());
    	return field; 
    }    
        
    @Override
    public LiteralValueNode visitStringLiteral(@NotNull AQLParser.StringLiteralContext ctx) {
    	LiteralValueNode value = new LiteralValueNode(DataType.STRING);
    	value.getValues().add(ctx.SLITERAL().getText());
    	return value;
    }
    
    @Override
    public LiteralValueNode visitIntLiteral(@NotNull AQLParser.IntLiteralContext ctx) {
    	LiteralValueNode value = new LiteralValueNode(DataType.INTEGER);
    	value.getValues().add(Long.parseLong(ctx.INT().getText()));
    	return value;
    }

    @Override
    public LiteralValueNode visitFloatLiteral(@NotNull AQLParser.FloatLiteralContext ctx) {
    	LiteralValueNode value = new LiteralValueNode(DataType.FLOAT);
    	value.getValues().add(Double.parseDouble(ctx.FLOAT().getText()));
    	return value;
    }
    
    @Override 
    public DateIntervalNode visitDate_interval(@NotNull AQLParser.Date_intervalContext ctx) {
    	DateIntervalNode di = new DateIntervalNode();
    	
    	di.setDays(diPart(ctx.DAY()));
    	di.setMonths(diPart(ctx.MONTH()));
    	di.setYears(diPart(ctx.YEAR()));
    	return di;    	
    }

    private DateDiffFuncNode getDateDiffFuncNode(DiffType diffType, ArithExpressionNode arithExprNode) {
    	DateDiffFuncNode diff = new DateDiffFuncNode();
    	diff.setDiffType(diffType);
    	
    	if (arithExprNode.getOp() != ArithOp.MINUS) {
    		throw new RuntimeException("Invalid operation inside months: " + arithExprNode.getOp());
    	}
    	
    	diff.setLeftOperand(arithExprNode.getLeftOperand());
    	diff.setRightOperand(arithExprNode.getRightOperand());
    	return diff;
    	
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