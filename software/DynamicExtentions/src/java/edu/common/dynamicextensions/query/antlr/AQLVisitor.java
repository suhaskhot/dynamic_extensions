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
	 * Visit a parse tree produced by {@link AQLParser#StringLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringLiteral(@NotNull AQLParser.StringLiteralContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#QueryExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitQueryExpr(@NotNull AQLParser.QueryExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#NotFilterExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotFilterExpr(@NotNull AQLParser.NotFilterExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#Field}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitField(@NotNull AQLParser.FieldContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#BasicFilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBasicFilter(@NotNull AQLParser.BasicFilterContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#ArithExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArithExpr(@NotNull AQLParser.ArithExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#FloatLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFloatLiteral(@NotNull AQLParser.FloatLiteralContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#OrFilterExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrFilterExpr(@NotNull AQLParser.OrFilterExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#MvFilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMvFilter(@NotNull AQLParser.MvFilterContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#IntLiteral}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIntLiteral(@NotNull AQLParser.IntLiteralContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#date_interval}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDate_interval(@NotNull AQLParser.Date_intervalContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#MonthsDiffFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMonthsDiffFunc(@NotNull AQLParser.MonthsDiffFuncContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#SimpleFilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSimpleFilter(@NotNull AQLParser.SimpleFilterContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#ParensFilterExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParensFilterExpr(@NotNull AQLParser.ParensFilterExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#SelectExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSelectExpr(@NotNull AQLParser.SelectExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#StringCompFilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStringCompFilter(@NotNull AQLParser.StringCompFilterContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#ExistsFilter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExistsFilter(@NotNull AQLParser.ExistsFilterContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#YearsDiffFunc}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitYearsDiffFunc(@NotNull AQLParser.YearsDiffFuncContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#literal_values}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteral_values(@NotNull AQLParser.Literal_valuesContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#DateIntervalExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDateIntervalExpr(@NotNull AQLParser.DateIntervalExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#LiteralVal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLiteralVal(@NotNull AQLParser.LiteralValContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#ParensArithExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParensArithExpr(@NotNull AQLParser.ParensArithExprContext ctx);

	/**
	 * Visit a parse tree produced by {@link AQLParser#AndFilterExpr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndFilterExpr(@NotNull AQLParser.AndFilterExprContext ctx);
}