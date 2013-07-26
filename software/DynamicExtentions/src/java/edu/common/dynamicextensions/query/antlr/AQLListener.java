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
	 * Enter a parse tree produced by {@link AQLParser#query}.
	 * @param ctx the parse tree
	 */
	void enterQuery(@NotNull AQLParser.QueryContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#query}.
	 * @param ctx the parse tree
	 */
	void exitQuery(@NotNull AQLParser.QueryContext ctx);

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
}