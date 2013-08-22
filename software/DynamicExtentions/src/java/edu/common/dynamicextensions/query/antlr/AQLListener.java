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
	 * Enter a parse tree produced by {@link AQLParser#StringLiteral}.
	 * @param ctx the parse tree
	 */
	void enterStringLiteral(@NotNull AQLParser.StringLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#StringLiteral}.
	 * @param ctx the parse tree
	 */
	void exitStringLiteral(@NotNull AQLParser.StringLiteralContext ctx);

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
	 * Enter a parse tree produced by {@link AQLParser#NotFilterExpr}.
	 * @param ctx the parse tree
	 */
	void enterNotFilterExpr(@NotNull AQLParser.NotFilterExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#NotFilterExpr}.
	 * @param ctx the parse tree
	 */
	void exitNotFilterExpr(@NotNull AQLParser.NotFilterExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#Field}.
	 * @param ctx the parse tree
	 */
	void enterField(@NotNull AQLParser.FieldContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#Field}.
	 * @param ctx the parse tree
	 */
	void exitField(@NotNull AQLParser.FieldContext ctx);

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
	 * Enter a parse tree produced by {@link AQLParser#FloatLiteral}.
	 * @param ctx the parse tree
	 */
	void enterFloatLiteral(@NotNull AQLParser.FloatLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#FloatLiteral}.
	 * @param ctx the parse tree
	 */
	void exitFloatLiteral(@NotNull AQLParser.FloatLiteralContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#OrFilterExpr}.
	 * @param ctx the parse tree
	 */
	void enterOrFilterExpr(@NotNull AQLParser.OrFilterExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#OrFilterExpr}.
	 * @param ctx the parse tree
	 */
	void exitOrFilterExpr(@NotNull AQLParser.OrFilterExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#IntLiteral}.
	 * @param ctx the parse tree
	 */
	void enterIntLiteral(@NotNull AQLParser.IntLiteralContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#IntLiteral}.
	 * @param ctx the parse tree
	 */
	void exitIntLiteral(@NotNull AQLParser.IntLiteralContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#date_interval}.
	 * @param ctx the parse tree
	 */
	void enterDate_interval(@NotNull AQLParser.Date_intervalContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#date_interval}.
	 * @param ctx the parse tree
	 */
	void exitDate_interval(@NotNull AQLParser.Date_intervalContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#SimpleFilter}.
	 * @param ctx the parse tree
	 */
	void enterSimpleFilter(@NotNull AQLParser.SimpleFilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#SimpleFilter}.
	 * @param ctx the parse tree
	 */
	void exitSimpleFilter(@NotNull AQLParser.SimpleFilterContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#MonthsDiffFunc}.
	 * @param ctx the parse tree
	 */
	void enterMonthsDiffFunc(@NotNull AQLParser.MonthsDiffFuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#MonthsDiffFunc}.
	 * @param ctx the parse tree
	 */
	void exitMonthsDiffFunc(@NotNull AQLParser.MonthsDiffFuncContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#ParensFilterExpr}.
	 * @param ctx the parse tree
	 */
	void enterParensFilterExpr(@NotNull AQLParser.ParensFilterExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#ParensFilterExpr}.
	 * @param ctx the parse tree
	 */
	void exitParensFilterExpr(@NotNull AQLParser.ParensFilterExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#SelectExpr}.
	 * @param ctx the parse tree
	 */
	void enterSelectExpr(@NotNull AQLParser.SelectExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#SelectExpr}.
	 * @param ctx the parse tree
	 */
	void exitSelectExpr(@NotNull AQLParser.SelectExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#YearsDiffFunc}.
	 * @param ctx the parse tree
	 */
	void enterYearsDiffFunc(@NotNull AQLParser.YearsDiffFuncContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#YearsDiffFunc}.
	 * @param ctx the parse tree
	 */
	void exitYearsDiffFunc(@NotNull AQLParser.YearsDiffFuncContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#DateIntervalExpr}.
	 * @param ctx the parse tree
	 */
	void enterDateIntervalExpr(@NotNull AQLParser.DateIntervalExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#DateIntervalExpr}.
	 * @param ctx the parse tree
	 */
	void exitDateIntervalExpr(@NotNull AQLParser.DateIntervalExprContext ctx);

	/**
	 * Enter a parse tree produced by {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void enterFilter(@NotNull AQLParser.FilterContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#filter}.
	 * @param ctx the parse tree
	 */
	void exitFilter(@NotNull AQLParser.FilterContext ctx);

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

	/**
	 * Enter a parse tree produced by {@link AQLParser#AndFilterExpr}.
	 * @param ctx the parse tree
	 */
	void enterAndFilterExpr(@NotNull AQLParser.AndFilterExprContext ctx);
	/**
	 * Exit a parse tree produced by {@link AQLParser#AndFilterExpr}.
	 * @param ctx the parse tree
	 */
	void exitAndFilterExpr(@NotNull AQLParser.AndFilterExprContext ctx);
}