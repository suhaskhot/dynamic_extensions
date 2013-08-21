// Generated from AQL.g4 by ANTLR 4.1
package edu.common.dynamicextensions.query.antlr;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link AQLParser}.
 */
public interface AQLListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link AQLParser#FloatExpr}.
	 * @param ctx the parse tree
	 */
	void enterFloatExpr(@NotNull AQLParser.FloatExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#FloatExpr}.
	 * @param ctx the parse tree
	 */
	void exitFloatExpr(@NotNull AQLParser.FloatExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#QueryExpr}.
	 * @param ctx the parse tree
	 */
	void enterQueryExpr(@NotNull AQLParser.QueryExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#QueryExpr}.
	 * @param ctx the parse tree
	 */
	void exitQueryExpr(@NotNull AQLParser.QueryExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#SelectList}.
	 * @param ctx the parse tree
	 */
	void enterSelectList(@NotNull AQLParser.SelectListContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#SelectList}.
	 * @param ctx the parse tree
	 */
	void exitSelectList(@NotNull AQLParser.SelectListContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#FieldExpr}.
	 * @param ctx the parse tree
	 */
	void enterFieldExpr(@NotNull AQLParser.FieldExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#FieldExpr}.
	 * @param ctx the parse tree
	 */
	void exitFieldExpr(@NotNull AQLParser.FieldExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#ArithExpr}.
	 * @param ctx the parse tree
	 */
	void enterArithExpr(@NotNull AQLParser.ArithExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#ArithExpr}.
	 * @param ctx the parse tree
	 */
	void exitArithExpr(@NotNull AQLParser.ArithExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#ParensExpr}.
	 * @param ctx the parse tree
	 */
	void enterParensExpr(@NotNull AQLParser.ParensExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#ParensExpr}.
	 * @param ctx the parse tree
	 */
	void exitParensExpr(@NotNull AQLParser.ParensExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#cond}.
	 * @param ctx the parse tree
	 */
	void enterCond(@NotNull AQLParser.CondContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#cond}.
	 * @param ctx the parse tree
	 */
	void exitCond(@NotNull AQLParser.CondContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#NotExpr}.
	 * @param ctx the parse tree
	 */
	void enterNotExpr(@NotNull AQLParser.NotExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#NotExpr}.
	 * @param ctx the parse tree
	 */
	void exitNotExpr(@NotNull AQLParser.NotExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#AndExpr}.
	 * @param ctx the parse tree
	 */
	void enterAndExpr(@NotNull AQLParser.AndExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#AndExpr}.
	 * @param ctx the parse tree
	 */
	void exitAndExpr(@NotNull AQLParser.AndExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#OrExpr}.
	 * @param ctx the parse tree
	 */
	void enterOrExpr(@NotNull AQLParser.OrExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#OrExpr}.
	 * @param ctx the parse tree
	 */
	void exitOrExpr(@NotNull AQLParser.OrExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#IntExpr}.
	 * @param ctx the parse tree
	 */
	void enterIntExpr(@NotNull AQLParser.IntExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#IntExpr}.
	 * @param ctx the parse tree
	 */
	void exitIntExpr(@NotNull AQLParser.IntExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#CondExpr}.
	 * @param ctx the parse tree
	 */
	void enterCondExpr(@NotNull AQLParser.CondExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#CondExpr}.
	 * @param ctx the parse tree
	 */
	void exitCondExpr(@NotNull AQLParser.CondExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#StringExpr}.
	 * @param ctx the parse tree
	 */
	void enterStringExpr(@NotNull AQLParser.StringExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#StringExpr}.
	 * @param ctx the parse tree
	 */
	void exitStringExpr(@NotNull AQLParser.StringExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#ParensArithExpr}.
	 * @param ctx the parse tree
	 */
	void enterParensArithExpr(@NotNull AQLParser.ParensArithExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#ParensArithExpr}.
	 * @param ctx the parse tree
	 */
	void exitParensArithExpr(@NotNull AQLParser.ParensArithExprContext ctx);
}