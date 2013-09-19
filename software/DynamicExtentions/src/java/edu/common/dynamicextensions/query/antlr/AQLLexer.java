// Generated from AQL.g4 by ANTLR 4.1
package edu.common.dynamicextensions.query.antlr;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class AQLLexer extends Lexer {
	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, WS=2, SELECT=3, WHERE=4, MONTHS=5, YEARS=6, OR=7, AND=8, PAND=9, 
		NOT=10, LP=11, RP=12, MOP=13, SOP=14, EOP=15, OP=16, INT=17, FLOAT=18, 
		YEAR=19, MONTH=20, DAY=21, DIGIT=22, ID=23, FIELD=24, SLITERAL=25, ESC=26, 
		ARITH_OP=27, QUOTE=28;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"','", "WS", "'select'", "'where'", "'months'", "'years'", "'or'", "'and'", 
		"'pand'", "'not'", "'('", "')'", "MOP", "SOP", "EOP", "OP", "INT", "FLOAT", 
		"YEAR", "MONTH", "DAY", "DIGIT", "ID", "FIELD", "SLITERAL", "ESC", "ARITH_OP", 
		"'\"'"
	};
	public static final String[] ruleNames = {
		"T__0", "WS", "SELECT", "WHERE", "MONTHS", "YEARS", "OR", "AND", "PAND", 
		"NOT", "LP", "RP", "MOP", "SOP", "EOP", "OP", "INT", "FLOAT", "YEAR", 
		"MONTH", "DAY", "DIGIT", "ID", "FIELD", "SLITERAL", "ESC", "ARITH_OP", 
		"SGUTS", "QUOTE"
	};


	public AQLLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "AQL.g4"; }

	@Override
	public String[] getTokenNames() { return tokenNames; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	@Override
	public void action(RuleContext _localctx, int ruleIndex, int actionIndex) {
		switch (ruleIndex) {
		case 1: WS_action((RuleContext)_localctx, actionIndex); break;
		}
	}
	private void WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0: skip();  break;
		}
	}

	public static final String _serializedATN =
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\2\36\u010f\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\3\2\3\2\3\3\6"+
		"\3A\n\3\r\3\16\3B\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5"+
		"\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3"+
		"\b\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3\f\3\f\3\r"+
		"\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16}\n\16\3\17\3\17\3\17"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\5\17\u009b\n\17"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\5\20\u00ad\n\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\5\21\u00bb\n\21\3\22\5\22\u00be\n\22\3\22\6\22\u00c1\n"+
		"\22\r\22\16\22\u00c2\3\23\5\23\u00c6\n\23\3\23\6\23\u00c9\n\23\r\23\16"+
		"\23\u00ca\3\23\3\23\6\23\u00cf\n\23\r\23\16\23\u00d0\3\24\6\24\u00d4\n"+
		"\24\r\24\16\24\u00d5\3\24\3\24\3\25\6\25\u00db\n\25\r\25\16\25\u00dc\3"+
		"\25\3\25\3\26\6\26\u00e2\n\26\r\26\16\26\u00e3\3\26\3\26\3\27\3\27\3\30"+
		"\3\30\7\30\u00ec\n\30\f\30\16\30\u00ef\13\30\3\31\3\31\5\31\u00f3\n\31"+
		"\3\31\3\31\3\31\3\31\7\31\u00f9\n\31\f\31\16\31\u00fc\13\31\3\32\3\32"+
		"\3\32\3\32\3\33\3\33\3\33\3\34\3\34\3\35\3\35\7\35\u0109\n\35\f\35\16"+
		"\35\u010c\13\35\3\36\3\36\2\37\3\3\1\5\4\2\7\5\1\t\6\1\13\7\1\r\b\1\17"+
		"\t\1\21\n\1\23\13\1\25\f\1\27\r\1\31\16\1\33\17\1\35\20\1\37\21\1!\22"+
		"\1#\23\1%\24\1\'\25\1)\26\1+\27\1-\30\1/\31\1\61\32\1\63\33\1\65\34\1"+
		"\67\35\19\2\1;\36\1\3\2\13\5\2\13\f\17\17\"\"\4\2>>@@\4\2[[{{\4\2OOoo"+
		"\4\2FFff\5\2C\\aac|\6\2\62;C\\aac|\4\2$$^^\5\2,-//\61\61\u0124\2\3\3\2"+
		"\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17"+
		"\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2"+
		"\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3"+
		"\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3"+
		"\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\2;\3\2\2\2\3=\3\2\2\2\5"+
		"@\3\2\2\2\7F\3\2\2\2\tM\3\2\2\2\13S\3\2\2\2\rZ\3\2\2\2\17`\3\2\2\2\21"+
		"c\3\2\2\2\23g\3\2\2\2\25l\3\2\2\2\27p\3\2\2\2\31r\3\2\2\2\33|\3\2\2\2"+
		"\35\u009a\3\2\2\2\37\u00ac\3\2\2\2!\u00ba\3\2\2\2#\u00bd\3\2\2\2%\u00c5"+
		"\3\2\2\2\'\u00d3\3\2\2\2)\u00da\3\2\2\2+\u00e1\3\2\2\2-\u00e7\3\2\2\2"+
		"/\u00e9\3\2\2\2\61\u00f2\3\2\2\2\63\u00fd\3\2\2\2\65\u0101\3\2\2\2\67"+
		"\u0104\3\2\2\29\u010a\3\2\2\2;\u010d\3\2\2\2=>\7.\2\2>\4\3\2\2\2?A\t\2"+
		"\2\2@?\3\2\2\2AB\3\2\2\2B@\3\2\2\2BC\3\2\2\2CD\3\2\2\2DE\b\3\2\2E\6\3"+
		"\2\2\2FG\7u\2\2GH\7g\2\2HI\7n\2\2IJ\7g\2\2JK\7e\2\2KL\7v\2\2L\b\3\2\2"+
		"\2MN\7y\2\2NO\7j\2\2OP\7g\2\2PQ\7t\2\2QR\7g\2\2R\n\3\2\2\2ST\7o\2\2TU"+
		"\7q\2\2UV\7p\2\2VW\7v\2\2WX\7j\2\2XY\7u\2\2Y\f\3\2\2\2Z[\7{\2\2[\\\7g"+
		"\2\2\\]\7c\2\2]^\7t\2\2^_\7u\2\2_\16\3\2\2\2`a\7q\2\2ab\7t\2\2b\20\3\2"+
		"\2\2cd\7c\2\2de\7p\2\2ef\7f\2\2f\22\3\2\2\2gh\7r\2\2hi\7c\2\2ij\7p\2\2"+
		"jk\7f\2\2k\24\3\2\2\2lm\7p\2\2mn\7q\2\2no\7v\2\2o\26\3\2\2\2pq\7*\2\2"+
		"q\30\3\2\2\2rs\7+\2\2s\32\3\2\2\2tu\7k\2\2u}\7p\2\2vw\7p\2\2wx\7q\2\2"+
		"xy\7v\2\2yz\7\"\2\2z{\7k\2\2{}\7p\2\2|t\3\2\2\2|v\3\2\2\2}\34\3\2\2\2"+
		"~\177\7e\2\2\177\u0080\7q\2\2\u0080\u0081\7p\2\2\u0081\u0082\7v\2\2\u0082"+
		"\u0083\7c\2\2\u0083\u0084\7k\2\2\u0084\u0085\7p\2\2\u0085\u009b\7u\2\2"+
		"\u0086\u0087\7u\2\2\u0087\u0088\7v\2\2\u0088\u0089\7c\2\2\u0089\u008a"+
		"\7t\2\2\u008a\u008b\7v\2\2\u008b\u008c\7u\2\2\u008c\u008d\7\"\2\2\u008d"+
		"\u008e\7y\2\2\u008e\u008f\7k\2\2\u008f\u0090\7v\2\2\u0090\u009b\7j\2\2"+
		"\u0091\u0092\7g\2\2\u0092\u0093\7p\2\2\u0093\u0094\7f\2\2\u0094\u0095"+
		"\7u\2\2\u0095\u0096\7\"\2\2\u0096\u0097\7y\2\2\u0097\u0098\7k\2\2\u0098"+
		"\u0099\7v\2\2\u0099\u009b\7j\2\2\u009a~\3\2\2\2\u009a\u0086\3\2\2\2\u009a"+
		"\u0091\3\2\2\2\u009b\36\3\2\2\2\u009c\u009d\7g\2\2\u009d\u009e\7z\2\2"+
		"\u009e\u009f\7k\2\2\u009f\u00a0\7u\2\2\u00a0\u00a1\7v\2\2\u00a1\u00ad"+
		"\7u\2\2\u00a2\u00a3\7p\2\2\u00a3\u00a4\7q\2\2\u00a4\u00a5\7v\2\2\u00a5"+
		"\u00a6\7\"\2\2\u00a6\u00a7\7g\2\2\u00a7\u00a8\7z\2\2\u00a8\u00a9\7k\2"+
		"\2\u00a9\u00aa\7u\2\2\u00aa\u00ab\7v\2\2\u00ab\u00ad\7u\2\2\u00ac\u009c"+
		"\3\2\2\2\u00ac\u00a2\3\2\2\2\u00ad \3\2\2\2\u00ae\u00bb\t\3\2\2\u00af"+
		"\u00b0\7@\2\2\u00b0\u00bb\7?\2\2\u00b1\u00b2\7>\2\2\u00b2\u00bb\7?\2\2"+
		"\u00b3\u00bb\7?\2\2\u00b4\u00b5\7#\2\2\u00b5\u00bb\7?\2\2\u00b6\u00b7"+
		"\7n\2\2\u00b7\u00b8\7k\2\2\u00b8\u00b9\7m\2\2\u00b9\u00bb\7g\2\2\u00ba"+
		"\u00ae\3\2\2\2\u00ba\u00af\3\2\2\2\u00ba\u00b1\3\2\2\2\u00ba\u00b3\3\2"+
		"\2\2\u00ba\u00b4\3\2\2\2\u00ba\u00b6\3\2\2\2\u00bb\"\3\2\2\2\u00bc\u00be"+
		"\7/\2\2\u00bd\u00bc\3\2\2\2\u00bd\u00be\3\2\2\2\u00be\u00c0\3\2\2\2\u00bf"+
		"\u00c1\5-\27\2\u00c0\u00bf\3\2\2\2\u00c1\u00c2\3\2\2\2\u00c2\u00c0\3\2"+
		"\2\2\u00c2\u00c3\3\2\2\2\u00c3$\3\2\2\2\u00c4\u00c6\7/\2\2\u00c5\u00c4"+
		"\3\2\2\2\u00c5\u00c6\3\2\2\2\u00c6\u00c8\3\2\2\2\u00c7\u00c9\5-\27\2\u00c8"+
		"\u00c7\3\2\2\2\u00c9\u00ca\3\2\2\2\u00ca\u00c8\3\2\2\2\u00ca\u00cb\3\2"+
		"\2\2\u00cb\u00cc\3\2\2\2\u00cc\u00ce\7\60\2\2\u00cd\u00cf\5-\27\2\u00ce"+
		"\u00cd\3\2\2\2\u00cf\u00d0\3\2\2\2\u00d0\u00ce\3\2\2\2\u00d0\u00d1\3\2"+
		"\2\2\u00d1&\3\2\2\2\u00d2\u00d4\5-\27\2\u00d3\u00d2\3\2\2\2\u00d4\u00d5"+
		"\3\2\2\2\u00d5\u00d3\3\2\2\2\u00d5\u00d6\3\2\2\2\u00d6\u00d7\3\2\2\2\u00d7"+
		"\u00d8\t\4\2\2\u00d8(\3\2\2\2\u00d9\u00db\5-\27\2\u00da\u00d9\3\2\2\2"+
		"\u00db\u00dc\3\2\2\2\u00dc\u00da\3\2\2\2\u00dc\u00dd\3\2\2\2\u00dd\u00de"+
		"\3\2\2\2\u00de\u00df\t\5\2\2\u00df*\3\2\2\2\u00e0\u00e2\5-\27\2\u00e1"+
		"\u00e0\3\2\2\2\u00e2\u00e3\3\2\2\2\u00e3\u00e1\3\2\2\2\u00e3\u00e4\3\2"+
		"\2\2\u00e4\u00e5\3\2\2\2\u00e5\u00e6\t\6\2\2\u00e6,\3\2\2\2\u00e7\u00e8"+
		"\4\62;\2\u00e8.\3\2\2\2\u00e9\u00ed\t\7\2\2\u00ea\u00ec\t\b\2\2\u00eb"+
		"\u00ea\3\2\2\2\u00ec\u00ef\3\2\2\2\u00ed\u00eb\3\2\2\2\u00ed\u00ee\3\2"+
		"\2\2\u00ee\60\3\2\2\2\u00ef\u00ed\3\2\2\2\u00f0\u00f3\5#\22\2\u00f1\u00f3"+
		"\5/\30\2\u00f2\u00f0\3\2\2\2\u00f2\u00f1\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4"+
		"\u00f5\7\60\2\2\u00f5\u00fa\5/\30\2\u00f6\u00f7\7\60\2\2\u00f7\u00f9\5"+
		"/\30\2\u00f8\u00f6\3\2\2\2\u00f9\u00fc\3\2\2\2\u00fa\u00f8\3\2\2\2\u00fa"+
		"\u00fb\3\2\2\2\u00fb\62\3\2\2\2\u00fc\u00fa\3\2\2\2\u00fd\u00fe\7$\2\2"+
		"\u00fe\u00ff\59\35\2\u00ff\u0100\7$\2\2\u0100\64\3\2\2\2\u0101\u0102\7"+
		"^\2\2\u0102\u0103\t\t\2\2\u0103\66\3\2\2\2\u0104\u0105\t\n\2\2\u01058"+
		"\3\2\2\2\u0106\u0109\5\65\33\2\u0107\u0109\n\t\2\2\u0108\u0106\3\2\2\2"+
		"\u0108\u0107\3\2\2\2\u0109\u010c\3\2\2\2\u010a\u0108\3\2\2\2\u010a\u010b"+
		"\3\2\2\2\u010b:\3\2\2\2\u010c\u010a\3\2\2\2\u010d\u010e\7$\2\2\u010e<"+
		"\3\2\2\2\25\2B|\u009a\u00ac\u00ba\u00bd\u00c2\u00c5\u00ca\u00d0\u00d5"+
		"\u00dc\u00e3\u00ed\u00f2\u00fa\u0108\u010a";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}