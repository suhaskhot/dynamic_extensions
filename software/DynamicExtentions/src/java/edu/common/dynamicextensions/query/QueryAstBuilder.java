package edu.common.dynamicextensions.query;

import org.antlr.v4.runtime.misc.NotNull;

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
    	
    	for (int i = 0; i < ctx.FIELD().size(); ++i) {
    		String fieldName = ctx.FIELD(i).getText();
    		Field field = new Field();
    		field.setName(fieldName);
    		list.addField(field);
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

    public Expression visitAndExpr(@NotNull AQLParser.AndExprContext ctx) {
        return Expression.andExpr(visit(ctx.expr(0)), visit(ctx.expr(1)));
    }

    public Expression visitParensExpr(@NotNull AQLParser.ParensExprContext ctx) {
        return Expression.parenExpr(visit(ctx.expr()));
    }

    public Node visitCond(@NotNull AQLParser.CondContext ctx) {
        String fieldName = ctx.FIELD().getText();
        Field field = new Field();
        field.setName(fieldName);

        String inputSymbol = ctx.OP().getText();
        String inputValue = null;        
        if (ctx.SLITERAL() != null) {
        	inputValue = ctx.SLITERAL().getText();
        } else if (ctx.FLOAT() != null) {
        	inputValue = ctx.FLOAT().getText();
        } else if (ctx.INT() != null) {
        	inputValue = ctx.INT().getText();
        }
        
        Filter filter = new Filter();        
        filter.setField(field);
        filter.setRelOp(RelationalOp.getBySymbol(inputSymbol));
        filter.getValues().add(inputValue);
        return filter;
    }
}