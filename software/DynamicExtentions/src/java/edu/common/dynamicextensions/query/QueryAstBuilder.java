package edu.common.dynamicextensions.query;

import java.util.ArrayList;
import java.util.List;

import edu.common.dynamicextensions.query.ast.*;

import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.TerminalNode;

import edu.common.dynamicextensions.domain.nui.DataType;
import edu.common.dynamicextensions.query.antlr.AQLBaseVisitor;
import edu.common.dynamicextensions.query.antlr.AQLParser;
import edu.common.dynamicextensions.query.antlr.AQLParser.LiteralContext;
import edu.common.dynamicextensions.query.ast.AggregateNode.AGG_FN;
import edu.common.dynamicextensions.query.ast.ArithExpressionNode.ArithOp;
import edu.common.dynamicextensions.query.ast.DateDiffFuncNode.DiffType;
import edu.common.dynamicextensions.query.ast.FilterNode.RelationalOp;

public class QueryAstBuilder extends AQLBaseVisitor<Node> {

    public QueryAstBuilder() {
    }
    
    @Override 
    public QueryExpressionNode visitQueryExpr(@NotNull AQLParser.QueryExprContext ctx) {  
    	QueryExpressionNode queryExpr = new QueryExpressionNode();

    	SelectListNode selectList = new SelectListNode();
    	if (ctx.select_list() != null) {
    		selectList = (SelectListNode)visit(ctx.select_list());
    	} 
    	queryExpr.setSelectList(selectList);    	
    	queryExpr.setFilterExpr((FilterExpressionNode)visit(ctx.filter_expr()));
    	
    	if (ctx.limit_expr() != null) {
    		LimitExprNode limitExpr = (LimitExprNode)visit(ctx.limit_expr());
    		queryExpr.setLimitExpr(limitExpr);
    	}
    	
    	if (ctx.crosstab_expr() != null) {
    		CrosstabNode crosstabSpec = (CrosstabNode)visit(ctx.crosstab_expr());
    		queryExpr.setCrosstabSpec(crosstabSpec);
    	} else if (ctx.ID() != null) {
    		queryExpr.setResultPostProc(ctx.ID().getText());
    	}
    	    	    	
    	return queryExpr;
    }    
    
    @Override
    public SelectListNode visitSelectExpr(@NotNull AQLParser.SelectExprContext ctx) { 
    	SelectListNode list = new SelectListNode();    	    	
    	for (int i = 0; i < ctx.select_element().size(); ++i) {
    		list.addElement((ExpressionNode)visit(ctx.select_element(i)));    		
    	}
    	
    	return list;  
    }
    
    @Override
    public LimitExprNode visitLimitExpr(@NotNull AQLParser.LimitExprContext ctx) {
    	LimitExprNode limitExpr = new LimitExprNode();
    	if (ctx.INT().size() == 2) {
    		limitExpr.setStartAt(Integer.parseInt(ctx.INT(0).getText()));
    		limitExpr.setNumRecords(Integer.parseInt(ctx.INT(1).getText()));
    	} else {
    		limitExpr.setNumRecords(Integer.parseInt(ctx.INT(0).getText()));
    	}
    	
    	return limitExpr;
    }
    
    @Override
    public ExpressionNode visitSelectElement(@NotNull AQLParser.SelectElementContext ctx) {
    	ExpressionNode expr = (ExpressionNode)visit(ctx.arith_expr());
    	if (ctx.SLITERAL() != null) {
    		String text = ctx.SLITERAL().getText();
    		expr.setLabel(text.substring(1, text.length() - 1));
    	}
    	
    	return expr;
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
    public Node visitPandFilterExpr(@NotNull AQLParser.PandFilterExprContext ctx) {
    	return FilterExpressionNode.pAndExpr(
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
    public FilterExpressionNode visitNthChildFilterExpr(@NotNull AQLParser.NthChildFilterExprContext ctx) { 
        return FilterExpressionNode.nthChildExpr((FilterNodeMarker)visit(ctx.filter_expr()));
    }

    @Override
    public FilterExpressionNode visitSimpleFilter(@NotNull AQLParser.SimpleFilterContext ctx) {
    	return FilterExpressionNode.identity((FilterNodeMarker)visit(ctx.filter()));
    }
    
    @Override
    public FilterNode visitBasicFilter(@NotNull AQLParser.BasicFilterContext ctx) {
    	FilterNode filter = new FilterNode();
    	filter.setLhs((ExpressionNode)visit(ctx.arith_expr(0)));
    	filter.setRhs((ExpressionNode)visit(ctx.arith_expr(1)));
    	filter.setRelOp(RelationalOp.getBySymbol(ctx.OP().getText()));
    	return filter;    	
    }
    
    @Override
    public FilterNode visitMvFilter(@NotNull AQLParser.MvFilterContext ctx) {
    	FilterNode filter = new FilterNode();
    	filter.setLhs((ExpressionNode)visit(ctx.arith_expr()));
    	filter.setRhs((ExpressionNode)visit(ctx.literal_values()));
    	filter.setRelOp(RelationalOp.getBySymbol(ctx.MOP().getText()));
    	return filter;    	
    }
    
    @Override
    public FilterNode visitStringCompFilter(@NotNull AQLParser.StringCompFilterContext ctx) {
    	FilterNode filter = new FilterNode();
    	
    	FieldNode field = new FieldNode();
    	field.setName(ctx.FIELD().getText());    	
    	filter.setLhs(field);
    	
    	LiteralValueNode value = new LiteralValueNode(DataType.STRING);
    	value.getValues().add(ctx.SLITERAL().getText());
    	filter.setRhs(value);
    	
    	filter.setRelOp(RelationalOp.getBySymbol(ctx.SOP().getText()));
    	return filter;
    }
    
    @Override
    public FilterNode visitExistsFilter(@NotNull AQLParser.ExistsFilterContext ctx) {
    	FilterNode filter = new FilterNode();
    	
    	FieldNode field = new FieldNode();
    	field.setName(ctx.FIELD().getText());
    	filter.setLhs(field);
    	
    	filter.setRelOp(RelationalOp.getBySymbol(ctx.EOP().getText()));
    	return filter;
    }

	@Override
	public FilterNode visitBetweenFilter(@NotNull AQLParser.BetweenFilterContext ctx) {
		FilterNode filter = new FilterNode();

		BetweenNode betweenNode = new BetweenNode();	
		FieldNode field = new FieldNode();
		field.setName(ctx.FIELD().getText());
		betweenNode.setLhs(field);
		betweenNode.setMinNode((ExpressionNode)visit(ctx.arith_expr(0)));
		betweenNode.setMaxNode((ExpressionNode)visit(ctx.arith_expr(1)));

		filter.setRelOp(RelationalOp.BETWEEN);
		filter.setLhs(betweenNode);
		return filter;
	}
    
    public LiteralValueListNode visitLiteral_values(@NotNull AQLParser.Literal_valuesContext ctx) {
    	LiteralValueListNode literals = new LiteralValueListNode();
    	for (LiteralContext literalCtx : ctx.literal()) {
    		literals.addLiteralVal((LiteralValueNode)visit(literalCtx));
    	}
    	
    	return literals;
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
    public ExpressionNode visitParensArithExpr(@NotNull AQLParser.ParensArithExprContext ctx) { 
    	return (ExpressionNode)visit(ctx.arith_expr()); 
    }
    
    @Override 
    public DateDiffFuncNode visitMonthsDiffFunc(@NotNull AQLParser.MonthsDiffFuncContext ctx) { 
    	ExpressionNode leftOperand = (ExpressionNode)visit(ctx.arith_expr(0));
    	ExpressionNode rightOperand = (ExpressionNode)visit(ctx.arith_expr(1));
    	return getDateDiffFuncNode(DiffType.MONTH, leftOperand, rightOperand);
    }

    @Override 
    public DateDiffFuncNode visitYearsDiffFunc(@NotNull AQLParser.YearsDiffFuncContext ctx) {
    	ExpressionNode leftOperand = (ExpressionNode)visit(ctx.arith_expr(0));
    	ExpressionNode rightOperand = (ExpressionNode)visit(ctx.arith_expr(1));
    	return getDateDiffFuncNode(DiffType.YEAR, leftOperand, rightOperand);
    }

	@Override
	public DateDiffFuncNode visitMinsDiffFunc(@NotNull AQLParser.MinsDiffFuncContext ctx) {
		ExpressionNode leftOperand = (ExpressionNode)visit(ctx.arith_expr(0));
		ExpressionNode rightOperand = (ExpressionNode)visit(ctx.arith_expr(1));
		return getDateDiffFuncNode(DiffType.MINUTES, leftOperand, rightOperand);
	}
    
    @Override
    public CurrentDateNode visitCurrentDateFunc(@NotNull AQLParser.CurrentDateFuncContext ctx) {
    	return new CurrentDateNode();
    }
    
    @Override
    public AggregateNode visitAggExpr(@NotNull AQLParser.AggExprContext ctx) {
    	return (AggregateNode)visit(ctx.agg_expr());
    }
    
    @Override
    public AggregateNode visitAggFunc(@NotNull AQLParser.AggFuncContext ctx) {
    	AggregateNode countNode = new AggregateNode();
    	if (ctx.COUNT() != null) {
    		countNode.setAggFn(AGG_FN.COUNT);
    	} else if (ctx.SUM() != null) {
    		countNode.setAggFn(AGG_FN.SUM);
    	} else if (ctx.MIN() != null) {
    		countNode.setAggFn(AGG_FN.MIN);
    	} else if (ctx.MAX() != null) {
    		countNode.setAggFn(AGG_FN.MAX);
    	} else if (ctx.AVG() != null) {
    		countNode.setAggFn(AGG_FN.AVG);
    	} else {
    		throw new IllegalArgumentException("Unknown aggregate function");
    	}
    	    	
    	if (ctx.DISTINCT() != null) {
    		countNode.setDistinct(true);
    	}
    	
    	FieldNode field = new FieldNode();
    	field.setName(ctx.FIELD().getText());
    	countNode.setField(field);    	
    	return countNode; 
    }
    
    public RoundOffNode visitRoundFunc(@NotNull AQLParser.RoundFuncContext ctx) {
    	RoundOffNode node = new RoundOffNode();
    	node.setExprNode((ExpressionNode)visit(ctx.arith_expr()));
    	node.setNoOfDigitsAfterDecimal(Integer.parseInt(ctx.INT().getText()));
    	return node;
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
    public LiteralValueNode visitBoolLiteral(@NotNull AQLParser.BoolLiteralContext ctx) {
    	LiteralValueNode value = new LiteralValueNode(DataType.BOOLEAN);
    	value.getValues().add(Boolean.parseBoolean(ctx.BOOL().getText()));
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
    
    @Override
    public CrosstabNode visitCrossTabExpr(@NotNull AQLParser.CrossTabExprContext ctx) {
    	CrosstabNode crosstabSpec = new CrosstabNode();
    	crosstabSpec.setName(ctx.CROSSTAB().getText());
    	
    	List<Integer> rowCols = new ArrayList<Integer>();
    	for (Token rowIdx : ctx.row) {
    		rowCols.add(getInt(rowIdx));
    	}    	
    	crosstabSpec.setRowGroupByColumns(rowCols);
    	
    	crosstabSpec.setColGroupByColumn(getInt(ctx.col));

    	List<Integer> valCols = new ArrayList<Integer>();
    	for (Token valIdx : ctx.value) {
    		valCols.add(getInt(valIdx));
    	}
    	crosstabSpec.setMeasureColumns(valCols);
    	if (ctx.BOOL() != null) {
    		crosstabSpec.setIncludeSubTotals(Boolean.parseBoolean(ctx.BOOL().getText()));
    	}
    	
    	return crosstabSpec; 
    }    
    
    private DateDiffFuncNode getDateDiffFuncNode(DiffType diffType, ExpressionNode leftOperand, ExpressionNode rightOperand) {
    	DateDiffFuncNode diff = new DateDiffFuncNode();
    	diff.setDiffType(diffType);    	
    	diff.setLeftOperand(leftOperand);
    	diff.setRightOperand(rightOperand);
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
    
//    private int getInt(TerminalNode node) {
//    	return Integer.parseInt(node.getText());
//    }
    
    private int getInt(Token token) {
    	return Integer.parseInt(token.getText());
    }
}
