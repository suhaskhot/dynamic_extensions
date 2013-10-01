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
		BOOL=19, YEAR=20, MONTH=21, DAY=22, DIGIT=23, ID=24, FIELD=25, SLITERAL=26, 
		ESC=27, ARITH_OP=28, QUOTE=29;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"','", "WS", "'select'", "'where'", "'months'", "'years'", "'or'", "'and'", 
		"'pand'", "'not'", "'('", "')'", "MOP", "SOP", "EOP", "OP", "INT", "FLOAT", 
		"BOOL", "YEAR", "MONTH", "DAY", "DIGIT", "ID", "FIELD", "SLITERAL", "ESC", 
		"ARITH_OP", "'\"'"
	};
	public static final String[] ruleNames = {
		"T__0", "WS", "SELECT", "WHERE", "MONTHS", "YEARS", "OR", "AND", "PAND", 
		"NOT", "LP", "RP", "MOP", "SOP", "EOP", "OP", "INT", "FLOAT", "BOOL", 
		"YEAR", "MONTH", "DAY", "DIGIT", "ID", "FIELD", "SLITERAL", "ESC", "ARITH_OP", 
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
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\2\37\u011c\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\3\2"+
		"\3\2\3\3\6\3C\n\3\r\3\16\3D\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3"+
		"\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7"+
		"\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3\13\3\13\3"+
		"\f\3\f\3\r\3\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16\177\n\16\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3"+
		"\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\5"+
		"\17\u009d\n\17\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\5\20\u00af\n\20\3\21\3\21\3\21\3\21\3\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\5\21\u00bd\n\21\3\22\5\22\u00c0\n\22\3"+
		"\22\6\22\u00c3\n\22\r\22\16\22\u00c4\3\23\5\23\u00c8\n\23\3\23\6\23\u00cb"+
		"\n\23\r\23\16\23\u00cc\3\23\3\23\6\23\u00d1\n\23\r\23\16\23\u00d2\3\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u00de\n\24\3\25\6\25\u00e1"+
		"\n\25\r\25\16\25\u00e2\3\25\3\25\3\26\6\26\u00e8\n\26\r\26\16\26\u00e9"+
		"\3\26\3\26\3\27\6\27\u00ef\n\27\r\27\16\27\u00f0\3\27\3\27\3\30\3\30\3"+
		"\31\3\31\7\31\u00f9\n\31\f\31\16\31\u00fc\13\31\3\32\3\32\5\32\u0100\n"+
		"\32\3\32\3\32\3\32\3\32\7\32\u0106\n\32\f\32\16\32\u0109\13\32\3\33\3"+
		"\33\3\33\3\33\3\34\3\34\3\34\3\35\3\35\3\36\3\36\7\36\u0116\n\36\f\36"+
		"\16\36\u0119\13\36\3\37\3\37\2 \3\3\1\5\4\2\7\5\1\t\6\1\13\7\1\r\b\1\17"+
		"\t\1\21\n\1\23\13\1\25\f\1\27\r\1\31\16\1\33\17\1\35\20\1\37\21\1!\22"+
		"\1#\23\1%\24\1\'\25\1)\26\1+\27\1-\30\1/\31\1\61\32\1\63\33\1\65\34\1"+
		"\67\35\19\36\1;\2\1=\37\1\3\2\13\5\2\13\f\17\17\"\"\4\2>>@@\4\2[[{{\4"+
		"\2OOoo\4\2FFff\5\2C\\aac|\6\2\62;C\\aac|\4\2$$^^\5\2,-//\61\61\u0132\2"+
		"\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2"+
		"\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2"+
		"\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2"+
		"\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2"+
		"\2\61\3\2\2\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2=\3\2"+
		"\2\2\3?\3\2\2\2\5B\3\2\2\2\7H\3\2\2\2\tO\3\2\2\2\13U\3\2\2\2\r\\\3\2\2"+
		"\2\17b\3\2\2\2\21e\3\2\2\2\23i\3\2\2\2\25n\3\2\2\2\27r\3\2\2\2\31t\3\2"+
		"\2\2\33~\3\2\2\2\35\u009c\3\2\2\2\37\u00ae\3\2\2\2!\u00bc\3\2\2\2#\u00bf"+
		"\3\2\2\2%\u00c7\3\2\2\2\'\u00dd\3\2\2\2)\u00e0\3\2\2\2+\u00e7\3\2\2\2"+
		"-\u00ee\3\2\2\2/\u00f4\3\2\2\2\61\u00f6\3\2\2\2\63\u00ff\3\2\2\2\65\u010a"+
		"\3\2\2\2\67\u010e\3\2\2\29\u0111\3\2\2\2;\u0117\3\2\2\2=\u011a\3\2\2\2"+
		"?@\7.\2\2@\4\3\2\2\2AC\t\2\2\2BA\3\2\2\2CD\3\2\2\2DB\3\2\2\2DE\3\2\2\2"+
		"EF\3\2\2\2FG\b\3\2\2G\6\3\2\2\2HI\7u\2\2IJ\7g\2\2JK\7n\2\2KL\7g\2\2LM"+
		"\7e\2\2MN\7v\2\2N\b\3\2\2\2OP\7y\2\2PQ\7j\2\2QR\7g\2\2RS\7t\2\2ST\7g\2"+
		"\2T\n\3\2\2\2UV\7o\2\2VW\7q\2\2WX\7p\2\2XY\7v\2\2YZ\7j\2\2Z[\7u\2\2[\f"+
		"\3\2\2\2\\]\7{\2\2]^\7g\2\2^_\7c\2\2_`\7t\2\2`a\7u\2\2a\16\3\2\2\2bc\7"+
		"q\2\2cd\7t\2\2d\20\3\2\2\2ef\7c\2\2fg\7p\2\2gh\7f\2\2h\22\3\2\2\2ij\7"+
		"r\2\2jk\7c\2\2kl\7p\2\2lm\7f\2\2m\24\3\2\2\2no\7p\2\2op\7q\2\2pq\7v\2"+
		"\2q\26\3\2\2\2rs\7*\2\2s\30\3\2\2\2tu\7+\2\2u\32\3\2\2\2vw\7k\2\2w\177"+
		"\7p\2\2xy\7p\2\2yz\7q\2\2z{\7v\2\2{|\7\"\2\2|}\7k\2\2}\177\7p\2\2~v\3"+
		"\2\2\2~x\3\2\2\2\177\34\3\2\2\2\u0080\u0081\7e\2\2\u0081\u0082\7q\2\2"+
		"\u0082\u0083\7p\2\2\u0083\u0084\7v\2\2\u0084\u0085\7c\2\2\u0085\u0086"+
		"\7k\2\2\u0086\u0087\7p\2\2\u0087\u009d\7u\2\2\u0088\u0089\7u\2\2\u0089"+
		"\u008a\7v\2\2\u008a\u008b\7c\2\2\u008b\u008c\7t\2\2\u008c\u008d\7v\2\2"+
		"\u008d\u008e\7u\2\2\u008e\u008f\7\"\2\2\u008f\u0090\7y\2\2\u0090\u0091"+
		"\7k\2\2\u0091\u0092\7v\2\2\u0092\u009d\7j\2\2\u0093\u0094\7g\2\2\u0094"+
		"\u0095\7p\2\2\u0095\u0096\7f\2\2\u0096\u0097\7u\2\2\u0097\u0098\7\"\2"+
		"\2\u0098\u0099\7y\2\2\u0099\u009a\7k\2\2\u009a\u009b\7v\2\2\u009b\u009d"+
		"\7j\2\2\u009c\u0080\3\2\2\2\u009c\u0088\3\2\2\2\u009c\u0093\3\2\2\2\u009d"+
		"\36\3\2\2\2\u009e\u009f\7g\2\2\u009f\u00a0\7z\2\2\u00a0\u00a1\7k\2\2\u00a1"+
		"\u00a2\7u\2\2\u00a2\u00a3\7v\2\2\u00a3\u00af\7u\2\2\u00a4\u00a5\7p\2\2"+
		"\u00a5\u00a6\7q\2\2\u00a6\u00a7\7v\2\2\u00a7\u00a8\7\"\2\2\u00a8\u00a9"+
		"\7g\2\2\u00a9\u00aa\7z\2\2\u00aa\u00ab\7k\2\2\u00ab\u00ac\7u\2\2\u00ac"+
		"\u00ad\7v\2\2\u00ad\u00af\7u\2\2\u00ae\u009e\3\2\2\2\u00ae\u00a4\3\2\2"+
		"\2\u00af \3\2\2\2\u00b0\u00bd\t\3\2\2\u00b1\u00b2\7@\2\2\u00b2\u00bd\7"+
		"?\2\2\u00b3\u00b4\7>\2\2\u00b4\u00bd\7?\2\2\u00b5\u00bd\7?\2\2\u00b6\u00b7"+
		"\7#\2\2\u00b7\u00bd\7?\2\2\u00b8\u00b9\7n\2\2\u00b9\u00ba\7k\2\2\u00ba"+
		"\u00bb\7m\2\2\u00bb\u00bd\7g\2\2\u00bc\u00b0\3\2\2\2\u00bc\u00b1\3\2\2"+
		"\2\u00bc\u00b3\3\2\2\2\u00bc\u00b5\3\2\2\2\u00bc\u00b6\3\2\2\2\u00bc\u00b8"+
		"\3\2\2\2\u00bd\"\3\2\2\2\u00be\u00c0\7/\2\2\u00bf\u00be\3\2\2\2\u00bf"+
		"\u00c0\3\2\2\2\u00c0\u00c2\3\2\2\2\u00c1\u00c3\5/\30\2\u00c2\u00c1\3\2"+
		"\2\2\u00c3\u00c4\3\2\2\2\u00c4\u00c2\3\2\2\2\u00c4\u00c5\3\2\2\2\u00c5"+
		"$\3\2\2\2\u00c6\u00c8\7/\2\2\u00c7\u00c6\3\2\2\2\u00c7\u00c8\3\2\2\2\u00c8"+
		"\u00ca\3\2\2\2\u00c9\u00cb\5/\30\2\u00ca\u00c9\3\2\2\2\u00cb\u00cc\3\2"+
		"\2\2\u00cc\u00ca\3\2\2\2\u00cc\u00cd\3\2\2\2\u00cd\u00ce\3\2\2\2\u00ce"+
		"\u00d0\7\60\2\2\u00cf\u00d1\5/\30\2\u00d0\u00cf\3\2\2\2\u00d1\u00d2\3"+
		"\2\2\2\u00d2\u00d0\3\2\2\2\u00d2\u00d3\3\2\2\2\u00d3&\3\2\2\2\u00d4\u00d5"+
		"\7v\2\2\u00d5\u00d6\7t\2\2\u00d6\u00d7\7w\2\2\u00d7\u00de\7g\2\2\u00d8"+
		"\u00d9\7h\2\2\u00d9\u00da\7c\2\2\u00da\u00db\7n\2\2\u00db\u00dc\7u\2\2"+
		"\u00dc\u00de\7g\2\2\u00dd\u00d4\3\2\2\2\u00dd\u00d8\3\2\2\2\u00de(\3\2"+
		"\2\2\u00df\u00e1\5/\30\2\u00e0\u00df\3\2\2\2\u00e1\u00e2\3\2\2\2\u00e2"+
		"\u00e0\3\2\2\2\u00e2\u00e3\3\2\2\2\u00e3\u00e4\3\2\2\2\u00e4\u00e5\t\4"+
		"\2\2\u00e5*\3\2\2\2\u00e6\u00e8\5/\30\2\u00e7\u00e6\3\2\2\2\u00e8\u00e9"+
		"\3\2\2\2\u00e9\u00e7\3\2\2\2\u00e9\u00ea\3\2\2\2\u00ea\u00eb\3\2\2\2\u00eb"+
		"\u00ec\t\5\2\2\u00ec,\3\2\2\2\u00ed\u00ef\5/\30\2\u00ee\u00ed\3\2\2\2"+
		"\u00ef\u00f0\3\2\2\2\u00f0\u00ee\3\2\2\2\u00f0\u00f1\3\2\2\2\u00f1\u00f2"+
		"\3\2\2\2\u00f2\u00f3\t\6\2\2\u00f3.\3\2\2\2\u00f4\u00f5\4\62;\2\u00f5"+
		"\60\3\2\2\2\u00f6\u00fa\t\7\2\2\u00f7\u00f9\t\b\2\2\u00f8\u00f7\3\2\2"+
		"\2\u00f9\u00fc\3\2\2\2\u00fa\u00f8\3\2\2\2\u00fa\u00fb\3\2\2\2\u00fb\62"+
		"\3\2\2\2\u00fc\u00fa\3\2\2\2\u00fd\u0100\5#\22\2\u00fe\u0100\5\61\31\2"+
		"\u00ff\u00fd\3\2\2\2\u00ff\u00fe\3\2\2\2\u0100\u0101\3\2\2\2\u0101\u0102"+
		"\7\60\2\2\u0102\u0107\5\61\31\2\u0103\u0104\7\60\2\2\u0104\u0106\5\61"+
		"\31\2\u0105\u0103\3\2\2\2\u0106\u0109\3\2\2\2\u0107\u0105\3\2\2\2\u0107"+
		"\u0108\3\2\2\2\u0108\64\3\2\2\2\u0109\u0107\3\2\2\2\u010a\u010b\7$\2\2"+
		"\u010b\u010c\5;\36\2\u010c\u010d\7$\2\2\u010d\66\3\2\2\2\u010e\u010f\7"+
		"^\2\2\u010f\u0110\t\t\2\2\u01108\3\2\2\2\u0111\u0112\t\n\2\2\u0112:\3"+
		"\2\2\2\u0113\u0116\5\67\34\2\u0114\u0116\n\t\2\2\u0115\u0113\3\2\2\2\u0115"+
		"\u0114\3\2\2\2\u0116\u0119\3\2\2\2\u0117\u0115\3\2\2\2\u0117\u0118\3\2"+
		"\2\2\u0118<\3\2\2\2\u0119\u0117\3\2\2\2\u011a\u011b\7$\2\2\u011b>\3\2"+
		"\2\2\26\2D~\u009c\u00ae\u00bc\u00bf\u00c4\u00c7\u00cc\u00d2\u00dd\u00e2"+
		"\u00e9\u00f0\u00fa\u00ff\u0107\u0115\u0117";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}