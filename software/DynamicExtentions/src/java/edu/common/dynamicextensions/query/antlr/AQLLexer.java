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
		T__0=1, WS=2, SELECT=3, WHERE=4, MONTHS=5, YEARS=6, OR=7, AND=8, NOT=9, 
		LP=10, RP=11, MOP=12, SOP=13, EOP=14, OP=15, INT=16, FLOAT=17, YEAR=18, 
		MONTH=19, DAY=20, DIGIT=21, ID=22, FIELD=23, SLITERAL=24, ESC=25, ARITH_OP=26, 
		QUOTE=27;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"','", "WS", "'select'", "'where'", "'months'", "'years'", "'or'", "'and'", 
		"'not'", "'('", "')'", "MOP", "SOP", "EOP", "OP", "INT", "FLOAT", "YEAR", 
		"MONTH", "DAY", "DIGIT", "ID", "FIELD", "SLITERAL", "ESC", "ARITH_OP", 
		"'\"'"
	};
	public static final String[] ruleNames = {
		"T__0", "WS", "SELECT", "WHERE", "MONTHS", "YEARS", "OR", "AND", "NOT", 
		"LP", "RP", "MOP", "SOP", "EOP", "OP", "INT", "FLOAT", "YEAR", "MONTH", 
		"DAY", "DIGIT", "ID", "FIELD", "SLITERAL", "ESC", "ARITH_OP", "SGUTS", 
		"QUOTE"
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
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\2\35\u0108\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\3\2\3\2\3\3\6\3?\n\3\r\3"+
		"\16\3@\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6"+
		"\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3"+
		"\t\3\t\3\n\3\n\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3"+
		"\r\5\rv\n\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16"+
		"\3\16\3\16\5\16\u0094\n\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\3\17\3\17\3\17\3\17\3\17\5\17\u00a6\n\17\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\5\20\u00b4\n\20\3\21\5\21\u00b7"+
		"\n\21\3\21\6\21\u00ba\n\21\r\21\16\21\u00bb\3\22\5\22\u00bf\n\22\3\22"+
		"\6\22\u00c2\n\22\r\22\16\22\u00c3\3\22\3\22\6\22\u00c8\n\22\r\22\16\22"+
		"\u00c9\3\23\6\23\u00cd\n\23\r\23\16\23\u00ce\3\23\3\23\3\24\6\24\u00d4"+
		"\n\24\r\24\16\24\u00d5\3\24\3\24\3\25\6\25\u00db\n\25\r\25\16\25\u00dc"+
		"\3\25\3\25\3\26\3\26\3\27\3\27\7\27\u00e5\n\27\f\27\16\27\u00e8\13\27"+
		"\3\30\3\30\5\30\u00ec\n\30\3\30\3\30\3\30\3\30\7\30\u00f2\n\30\f\30\16"+
		"\30\u00f5\13\30\3\31\3\31\3\31\3\31\3\32\3\32\3\32\3\33\3\33\3\34\3\34"+
		"\7\34\u0102\n\34\f\34\16\34\u0105\13\34\3\35\3\35\2\36\3\3\1\5\4\2\7\5"+
		"\1\t\6\1\13\7\1\r\b\1\17\t\1\21\n\1\23\13\1\25\f\1\27\r\1\31\16\1\33\17"+
		"\1\35\20\1\37\21\1!\22\1#\23\1%\24\1\'\25\1)\26\1+\27\1-\30\1/\31\1\61"+
		"\32\1\63\33\1\65\34\1\67\2\19\35\1\3\2\13\5\2\13\f\17\17\"\"\4\2>>@@\4"+
		"\2[[{{\4\2OOoo\4\2FFff\5\2C\\aac|\6\2\62;C\\aac|\4\2$$^^\5\2,-//\61\61"+
		"\u011d\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2"+
		"\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3"+
		"\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2"+
		"\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2"+
		"/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\29\3\2\2\2\3;\3\2\2\2"+
		"\5>\3\2\2\2\7D\3\2\2\2\tK\3\2\2\2\13Q\3\2\2\2\rX\3\2\2\2\17^\3\2\2\2\21"+
		"a\3\2\2\2\23e\3\2\2\2\25i\3\2\2\2\27k\3\2\2\2\31u\3\2\2\2\33\u0093\3\2"+
		"\2\2\35\u00a5\3\2\2\2\37\u00b3\3\2\2\2!\u00b6\3\2\2\2#\u00be\3\2\2\2%"+
		"\u00cc\3\2\2\2\'\u00d3\3\2\2\2)\u00da\3\2\2\2+\u00e0\3\2\2\2-\u00e2\3"+
		"\2\2\2/\u00eb\3\2\2\2\61\u00f6\3\2\2\2\63\u00fa\3\2\2\2\65\u00fd\3\2\2"+
		"\2\67\u0103\3\2\2\29\u0106\3\2\2\2;<\7.\2\2<\4\3\2\2\2=?\t\2\2\2>=\3\2"+
		"\2\2?@\3\2\2\2@>\3\2\2\2@A\3\2\2\2AB\3\2\2\2BC\b\3\2\2C\6\3\2\2\2DE\7"+
		"u\2\2EF\7g\2\2FG\7n\2\2GH\7g\2\2HI\7e\2\2IJ\7v\2\2J\b\3\2\2\2KL\7y\2\2"+
		"LM\7j\2\2MN\7g\2\2NO\7t\2\2OP\7g\2\2P\n\3\2\2\2QR\7o\2\2RS\7q\2\2ST\7"+
		"p\2\2TU\7v\2\2UV\7j\2\2VW\7u\2\2W\f\3\2\2\2XY\7{\2\2YZ\7g\2\2Z[\7c\2\2"+
		"[\\\7t\2\2\\]\7u\2\2]\16\3\2\2\2^_\7q\2\2_`\7t\2\2`\20\3\2\2\2ab\7c\2"+
		"\2bc\7p\2\2cd\7f\2\2d\22\3\2\2\2ef\7p\2\2fg\7q\2\2gh\7v\2\2h\24\3\2\2"+
		"\2ij\7*\2\2j\26\3\2\2\2kl\7+\2\2l\30\3\2\2\2mn\7k\2\2nv\7p\2\2op\7p\2"+
		"\2pq\7q\2\2qr\7v\2\2rs\7\"\2\2st\7k\2\2tv\7p\2\2um\3\2\2\2uo\3\2\2\2v"+
		"\32\3\2\2\2wx\7e\2\2xy\7q\2\2yz\7p\2\2z{\7v\2\2{|\7c\2\2|}\7k\2\2}~\7"+
		"p\2\2~\u0094\7u\2\2\177\u0080\7u\2\2\u0080\u0081\7v\2\2\u0081\u0082\7"+
		"c\2\2\u0082\u0083\7t\2\2\u0083\u0084\7v\2\2\u0084\u0085\7u\2\2\u0085\u0086"+
		"\7\"\2\2\u0086\u0087\7y\2\2\u0087\u0088\7k\2\2\u0088\u0089\7v\2\2\u0089"+
		"\u0094\7j\2\2\u008a\u008b\7g\2\2\u008b\u008c\7p\2\2\u008c\u008d\7f\2\2"+
		"\u008d\u008e\7u\2\2\u008e\u008f\7\"\2\2\u008f\u0090\7y\2\2\u0090\u0091"+
		"\7k\2\2\u0091\u0092\7v\2\2\u0092\u0094\7j\2\2\u0093w\3\2\2\2\u0093\177"+
		"\3\2\2\2\u0093\u008a\3\2\2\2\u0094\34\3\2\2\2\u0095\u0096\7g\2\2\u0096"+
		"\u0097\7z\2\2\u0097\u0098\7k\2\2\u0098\u0099\7u\2\2\u0099\u009a\7v\2\2"+
		"\u009a\u00a6\7u\2\2\u009b\u009c\7p\2\2\u009c\u009d\7q\2\2\u009d\u009e"+
		"\7v\2\2\u009e\u009f\7\"\2\2\u009f\u00a0\7g\2\2\u00a0\u00a1\7z\2\2\u00a1"+
		"\u00a2\7k\2\2\u00a2\u00a3\7u\2\2\u00a3\u00a4\7v\2\2\u00a4\u00a6\7u\2\2"+
		"\u00a5\u0095\3\2\2\2\u00a5\u009b\3\2\2\2\u00a6\36\3\2\2\2\u00a7\u00b4"+
		"\t\3\2\2\u00a8\u00a9\7@\2\2\u00a9\u00b4\7?\2\2\u00aa\u00ab\7>\2\2\u00ab"+
		"\u00b4\7?\2\2\u00ac\u00b4\7?\2\2\u00ad\u00ae\7#\2\2\u00ae\u00b4\7?\2\2"+
		"\u00af\u00b0\7n\2\2\u00b0\u00b1\7k\2\2\u00b1\u00b2\7m\2\2\u00b2\u00b4"+
		"\7g\2\2\u00b3\u00a7\3\2\2\2\u00b3\u00a8\3\2\2\2\u00b3\u00aa\3\2\2\2\u00b3"+
		"\u00ac\3\2\2\2\u00b3\u00ad\3\2\2\2\u00b3\u00af\3\2\2\2\u00b4 \3\2\2\2"+
		"\u00b5\u00b7\7/\2\2\u00b6\u00b5\3\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00b9"+
		"\3\2\2\2\u00b8\u00ba\5+\26\2\u00b9\u00b8\3\2\2\2\u00ba\u00bb\3\2\2\2\u00bb"+
		"\u00b9\3\2\2\2\u00bb\u00bc\3\2\2\2\u00bc\"\3\2\2\2\u00bd\u00bf\7/\2\2"+
		"\u00be\u00bd\3\2\2\2\u00be\u00bf\3\2\2\2\u00bf\u00c1\3\2\2\2\u00c0\u00c2"+
		"\5+\26\2\u00c1\u00c0\3\2\2\2\u00c2\u00c3\3\2\2\2\u00c3\u00c1\3\2\2\2\u00c3"+
		"\u00c4\3\2\2\2\u00c4\u00c5\3\2\2\2\u00c5\u00c7\7\60\2\2\u00c6\u00c8\5"+
		"+\26\2\u00c7\u00c6\3\2\2\2\u00c8\u00c9\3\2\2\2\u00c9\u00c7\3\2\2\2\u00c9"+
		"\u00ca\3\2\2\2\u00ca$\3\2\2\2\u00cb\u00cd\5+\26\2\u00cc\u00cb\3\2\2\2"+
		"\u00cd\u00ce\3\2\2\2\u00ce\u00cc\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00d0"+
		"\3\2\2\2\u00d0\u00d1\t\4\2\2\u00d1&\3\2\2\2\u00d2\u00d4\5+\26\2\u00d3"+
		"\u00d2\3\2\2\2\u00d4\u00d5\3\2\2\2\u00d5\u00d3\3\2\2\2\u00d5\u00d6\3\2"+
		"\2\2\u00d6\u00d7\3\2\2\2\u00d7\u00d8\t\5\2\2\u00d8(\3\2\2\2\u00d9\u00db"+
		"\5+\26\2\u00da\u00d9\3\2\2\2\u00db\u00dc\3\2\2\2\u00dc\u00da\3\2\2\2\u00dc"+
		"\u00dd\3\2\2\2\u00dd\u00de\3\2\2\2\u00de\u00df\t\6\2\2\u00df*\3\2\2\2"+
		"\u00e0\u00e1\4\62;\2\u00e1,\3\2\2\2\u00e2\u00e6\t\7\2\2\u00e3\u00e5\t"+
		"\b\2\2\u00e4\u00e3\3\2\2\2\u00e5\u00e8\3\2\2\2\u00e6\u00e4\3\2\2\2\u00e6"+
		"\u00e7\3\2\2\2\u00e7.\3\2\2\2\u00e8\u00e6\3\2\2\2\u00e9\u00ec\5!\21\2"+
		"\u00ea\u00ec\5-\27\2\u00eb\u00e9\3\2\2\2\u00eb\u00ea\3\2\2\2\u00ec\u00ed"+
		"\3\2\2\2\u00ed\u00ee\7\60\2\2\u00ee\u00f3\5-\27\2\u00ef\u00f0\7\60\2\2"+
		"\u00f0\u00f2\5-\27\2\u00f1\u00ef\3\2\2\2\u00f2\u00f5\3\2\2\2\u00f3\u00f1"+
		"\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4\60\3\2\2\2\u00f5\u00f3\3\2\2\2\u00f6"+
		"\u00f7\7$\2\2\u00f7\u00f8\5\67\34\2\u00f8\u00f9\7$\2\2\u00f9\62\3\2\2"+
		"\2\u00fa\u00fb\7^\2\2\u00fb\u00fc\t\t\2\2\u00fc\64\3\2\2\2\u00fd\u00fe"+
		"\t\n\2\2\u00fe\66\3\2\2\2\u00ff\u0102\5\63\32\2\u0100\u0102\n\t\2\2\u0101"+
		"\u00ff\3\2\2\2\u0101\u0100\3\2\2\2\u0102\u0105\3\2\2\2\u0103\u0101\3\2"+
		"\2\2\u0103\u0104\3\2\2\2\u01048\3\2\2\2\u0105\u0103\3\2\2\2\u0106\u0107"+
		"\7$\2\2\u0107:\3\2\2\2\25\2@u\u0093\u00a5\u00b3\u00b6\u00bb\u00be\u00c3"+
		"\u00c9\u00ce\u00d5\u00dc\u00e6\u00eb\u00f3\u0101\u0103";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}