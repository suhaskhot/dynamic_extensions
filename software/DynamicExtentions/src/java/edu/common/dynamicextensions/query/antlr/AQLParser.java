// Generated from AQL.g4 by ANTLR 4.1
package edu.common.dynamicextensions.query.antlr;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class AQLParser extends Parser {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, WS=2, SELECT=3, WHERE=4, OR=5, AND=6, NOT=7, LP=8, RP=9, FIELD=10, 
		INT=11, FLOAT=12, SLITERAL=13, ESC=14, ID=15, OP=16, QUOTE=17;
	public static final String[] tokenNames = {
		"<INVALID>", "','", "WS", "'select'", "'where'", "'or'", "'and'", "'not'", 
		"'('", "')'", "FIELD", "INT", "FLOAT", "SLITERAL", "ESC", "ID", "OP", 
		"'\"'"
	};
	public static final int
		RULE_query = 0, RULE_select_list = 1, RULE_expr = 2, RULE_cond = 3;
	public static final String[] ruleNames = {
		"query", "select_list", "expr", "cond"
	};

	@Override
	public String getGrammarFileName() { return "AQL.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public AQLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class QueryContext extends ParserRuleContext {
		public QueryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_query; }
	 
		public QueryContext() { }
		public void copyFrom(QueryContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class QueryExprContext extends QueryContext {
		public TerminalNode WHERE() { return getToken(AQLParser.WHERE, 0); }
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode SELECT() { return getToken(AQLParser.SELECT, 0); }
		public Select_listContext select_list() {
			return getRuleContext(Select_listContext.class,0);
		}
		public QueryExprContext(QueryContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterQueryExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitQueryExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitQueryExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final QueryContext query() throws RecognitionException {
		QueryContext _localctx = new QueryContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_query);
		try {
			_localctx = new QueryExprContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(8); match(SELECT);
			setState(9); select_list();
			setState(10); match(WHERE);
			setState(11); expr(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class Select_listContext extends ParserRuleContext {
		public Select_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_select_list; }
	 
		public Select_listContext() { }
		public void copyFrom(Select_listContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SelectListContext extends Select_listContext {
		public TerminalNode FIELD(int i) {
			return getToken(AQLParser.FIELD, i);
		}
		public List<TerminalNode> FIELD() { return getTokens(AQLParser.FIELD); }
		public SelectListContext(Select_listContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterSelectList(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitSelectList(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitSelectList(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Select_listContext select_list() throws RecognitionException {
		Select_listContext _localctx = new Select_listContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_select_list);
		int _la;
		try {
			_localctx = new SelectListContext(_localctx);
			enterOuterAlt(_localctx, 1);
			{
			setState(13); match(FIELD);
			setState(18);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==1) {
				{
				{
				setState(14); match(1);
				setState(15); match(FIELD);
				}
				}
				setState(20);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExprContext extends ParserRuleContext {
		public int _p;
		public ExprContext(ParserRuleContext parent, int invokingState) { super(parent, invokingState); }
		public ExprContext(ParserRuleContext parent, int invokingState, int _p) {
			super(parent, invokingState);
			this._p = _p;
		}
		@Override public int getRuleIndex() { return RULE_expr; }
	 
		public ExprContext() { }
		public void copyFrom(ExprContext ctx) {
			super.copyFrom(ctx);
			this._p = ctx._p;
		}
	}
	public static class NotExprContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode NOT() { return getToken(AQLParser.NOT, 0); }
		public NotExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterNotExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitNotExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitNotExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class AndExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public TerminalNode AND() { return getToken(AQLParser.AND, 0); }
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public AndExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterAndExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitAndExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitAndExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class OrExprContext extends ExprContext {
		public List<ExprContext> expr() {
			return getRuleContexts(ExprContext.class);
		}
		public TerminalNode OR() { return getToken(AQLParser.OR, 0); }
		public ExprContext expr(int i) {
			return getRuleContext(ExprContext.class,i);
		}
		public OrExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterOrExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitOrExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitOrExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ParensExprContext extends ExprContext {
		public ExprContext expr() {
			return getRuleContext(ExprContext.class,0);
		}
		public TerminalNode LP() { return getToken(AQLParser.LP, 0); }
		public TerminalNode RP() { return getToken(AQLParser.RP, 0); }
		public ParensExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterParensExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitParensExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitParensExpr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class CondExprContext extends ExprContext {
		public CondContext cond() {
			return getRuleContext(CondContext.class,0);
		}
		public CondExprContext(ExprContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterCondExpr(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitCondExpr(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitCondExpr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExprContext expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExprContext _localctx = new ExprContext(_ctx, _parentState, _p);
		ExprContext _prevctx = _localctx;
		int _startState = 4;
		enterRecursionRule(_localctx, RULE_expr);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(29);
			switch (_input.LA(1)) {
			case NOT:
				{
				_localctx = new NotExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(22); match(NOT);
				setState(23); expr(2);
				}
				break;
			case LP:
				{
				_localctx = new ParensExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(24); match(LP);
				setState(25); expr(0);
				setState(26); match(RP);
				}
				break;
			case FIELD:
				{
				_localctx = new CondExprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(28); cond();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(39);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			while ( _alt!=2 && _alt!=-1 ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(37);
					switch ( getInterpreter().adaptivePredict(_input,2,_ctx) ) {
					case 1:
						{
						_localctx = new AndExprContext(new ExprContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(31);
						if (!(5 >= _localctx._p)) throw new FailedPredicateException(this, "5 >= $_p");
						setState(32); match(AND);
						setState(33); expr(6);
						}
						break;

					case 2:
						{
						_localctx = new OrExprContext(new ExprContext(_parentctx, _parentState, _p));
						pushNewRecursionContext(_localctx, _startState, RULE_expr);
						setState(34);
						if (!(4 >= _localctx._p)) throw new FailedPredicateException(this, "4 >= $_p");
						setState(35); match(OR);
						setState(36); expr(5);
						}
						break;
					}
					} 
				}
				setState(41);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,3,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public static class CondContext extends ParserRuleContext {
		public TerminalNode OP() { return getToken(AQLParser.OP, 0); }
		public TerminalNode SLITERAL() { return getToken(AQLParser.SLITERAL, 0); }
		public TerminalNode FLOAT() { return getToken(AQLParser.FLOAT, 0); }
		public TerminalNode INT() { return getToken(AQLParser.INT, 0); }
		public TerminalNode FIELD() { return getToken(AQLParser.FIELD, 0); }
		public CondContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cond; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).enterCond(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof AQLListener ) ((AQLListener)listener).exitCond(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof AQLVisitor ) return ((AQLVisitor<? extends T>)visitor).visitCond(this);
			else return visitor.visitChildren(this);
		}
	}

	public final CondContext cond() throws RecognitionException {
		CondContext _localctx = new CondContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_cond);
		try {
			setState(51);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(42); match(FIELD);
				setState(43); match(OP);
				setState(44); match(SLITERAL);
				}
				break;

			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(45); match(FIELD);
				setState(46); match(OP);
				setState(47); match(INT);
				}
				break;

			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(48); match(FIELD);
				setState(49); match(OP);
				setState(50); match(FLOAT);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 2: return expr_sempred((ExprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expr_sempred(ExprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0: return 5 >= _localctx._p;

		case 1: return 4 >= _localctx._p;
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\3\238\4\2\t\2\4\3\t"+
		"\3\4\4\t\4\4\5\t\5\3\2\3\2\3\2\3\2\3\2\3\3\3\3\3\3\7\3\23\n\3\f\3\16\3"+
		"\26\13\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4 \n\4\3\4\3\4\3\4\3\4\3\4"+
		"\3\4\7\4(\n\4\f\4\16\4+\13\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\5\5\5\66"+
		"\n\5\3\5\2\6\2\4\6\b\2\2:\2\n\3\2\2\2\4\17\3\2\2\2\6\37\3\2\2\2\b\65\3"+
		"\2\2\2\n\13\7\5\2\2\13\f\5\4\3\2\f\r\7\6\2\2\r\16\5\6\4\2\16\3\3\2\2\2"+
		"\17\24\7\f\2\2\20\21\7\3\2\2\21\23\7\f\2\2\22\20\3\2\2\2\23\26\3\2\2\2"+
		"\24\22\3\2\2\2\24\25\3\2\2\2\25\5\3\2\2\2\26\24\3\2\2\2\27\30\b\4\1\2"+
		"\30\31\7\t\2\2\31 \5\6\4\2\32\33\7\n\2\2\33\34\5\6\4\2\34\35\7\13\2\2"+
		"\35 \3\2\2\2\36 \5\b\5\2\37\27\3\2\2\2\37\32\3\2\2\2\37\36\3\2\2\2 )\3"+
		"\2\2\2!\"\6\4\2\3\"#\7\b\2\2#(\5\6\4\2$%\6\4\3\3%&\7\7\2\2&(\5\6\4\2\'"+
		"!\3\2\2\2\'$\3\2\2\2(+\3\2\2\2)\'\3\2\2\2)*\3\2\2\2*\7\3\2\2\2+)\3\2\2"+
		"\2,-\7\f\2\2-.\7\22\2\2.\66\7\17\2\2/\60\7\f\2\2\60\61\7\22\2\2\61\66"+
		"\7\r\2\2\62\63\7\f\2\2\63\64\7\22\2\2\64\66\7\16\2\2\65,\3\2\2\2\65/\3"+
		"\2\2\2\65\62\3\2\2\2\66\t\3\2\2\2\7\24\37\')\65";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}