// Generated from AQL.g4 by ANTLR 4.1
package edu.common.dynamicextensions.query.antlr;
import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link AQLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface AQLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link AQLParser#NotExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotExpr(@NotNull AQLParser.NotExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#QueryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryExpr(@NotNull AQLParser.QueryExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#SelectList}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectList(@NotNull AQLParser.SelectListContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#OrExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpr(@NotNull AQLParser.OrExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#AndExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndExpr(@NotNull AQLParser.AndExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#CondExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCondExpr(@NotNull AQLParser.CondExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#ParensExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParensExpr(@NotNull AQLParser.ParensExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#cond}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCond(@NotNull AQLParser.CondContext ctx);
}