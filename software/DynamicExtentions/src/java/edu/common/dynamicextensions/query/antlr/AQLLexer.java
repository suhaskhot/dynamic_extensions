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
		LP=10, RP=11, MOP=12, SOP=13, OP=14, INT=15, FLOAT=16, YEAR=17, MONTH=18, 
		DAY=19, DIGIT=20, ID=21, FIELD=22, SLITERAL=23, ESC=24, ARITH_OP=25, QUOTE=26;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"','", "WS", "'select'", "'where'", "'months'", "'years'", "'or'", "'and'", 
		"'not'", "'('", "')'", "MOP", "SOP", "OP", "INT", "FLOAT", "YEAR", "MONTH", 
		"DAY", "DIGIT", "ID", "FIELD", "SLITERAL", "ESC", "ARITH_OP", "'\"'"
	};
	public static final String[] ruleNames = {
		"T__0", "WS", "SELECT", "WHERE", "MONTHS", "YEARS", "OR", "AND", "NOT", 
		"LP", "RP", "MOP", "SOP", "OP", "INT", "FLOAT", "YEAR", "MONTH", "DAY", 
		"DIGIT", "ID", "FIELD", "SLITERAL", "ESC", "ARITH_OP", "SGUTS", "QUOTE"
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
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\2\34\u00f4\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\4\34\t\34\3\2\3\2\3\3\6\3=\n\3\r\3\16\3>\3\3"+
		"\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3"+
		"\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\n"+
		"\3\n\3\n\3\n\3\13\3\13\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\r\5\rt\n"+
		"\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3"+
		"\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3"+
		"\16\5\16\u0092\n\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\17"+
		"\3\17\3\17\5\17\u00a0\n\17\3\20\5\20\u00a3\n\20\3\20\6\20\u00a6\n\20\r"+
		"\20\16\20\u00a7\3\21\5\21\u00ab\n\21\3\21\6\21\u00ae\n\21\r\21\16\21\u00af"+
		"\3\21\3\21\6\21\u00b4\n\21\r\21\16\21\u00b5\3\22\6\22\u00b9\n\22\r\22"+
		"\16\22\u00ba\3\22\3\22\3\23\6\23\u00c0\n\23\r\23\16\23\u00c1\3\23\3\23"+
		"\3\24\6\24\u00c7\n\24\r\24\16\24\u00c8\3\24\3\24\3\25\3\25\3\26\3\26\7"+
		"\26\u00d1\n\26\f\26\16\26\u00d4\13\26\3\27\3\27\5\27\u00d8\n\27\3\27\3"+
		"\27\3\27\3\27\7\27\u00de\n\27\f\27\16\27\u00e1\13\27\3\30\3\30\3\30\3"+
		"\30\3\31\3\31\3\31\3\32\3\32\3\33\3\33\7\33\u00ee\n\33\f\33\16\33\u00f1"+
		"\13\33\3\34\3\34\2\35\3\3\1\5\4\2\7\5\1\t\6\1\13\7\1\r\b\1\17\t\1\21\n"+
		"\1\23\13\1\25\f\1\27\r\1\31\16\1\33\17\1\35\20\1\37\21\1!\22\1#\23\1%"+
		"\24\1\'\25\1)\26\1+\27\1-\30\1/\31\1\61\32\1\63\33\1\65\2\1\67\34\1\3"+
		"\2\13\5\2\13\f\17\17\"\"\4\2>>@@\4\2[[{{\4\2OOoo\4\2FFff\5\2C\\aac|\6"+
		"\2\62;C\\aac|\4\2$$^^\5\2,-//\61\61\u0108\2\3\3\2\2\2\2\5\3\2\2\2\2\7"+
		"\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2"+
		"\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2"+
		"\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2"+
		"\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2\2\2\63\3\2\2\2"+
		"\2\67\3\2\2\2\39\3\2\2\2\5<\3\2\2\2\7B\3\2\2\2\tI\3\2\2\2\13O\3\2\2\2"+
		"\rV\3\2\2\2\17\\\3\2\2\2\21_\3\2\2\2\23c\3\2\2\2\25g\3\2\2\2\27i\3\2\2"+
		"\2\31s\3\2\2\2\33\u0091\3\2\2\2\35\u009f\3\2\2\2\37\u00a2\3\2\2\2!\u00aa"+
		"\3\2\2\2#\u00b8\3\2\2\2%\u00bf\3\2\2\2\'\u00c6\3\2\2\2)\u00cc\3\2\2\2"+
		"+\u00ce\3\2\2\2-\u00d7\3\2\2\2/\u00e2\3\2\2\2\61\u00e6\3\2\2\2\63\u00e9"+
		"\3\2\2\2\65\u00ef\3\2\2\2\67\u00f2\3\2\2\29:\7.\2\2:\4\3\2\2\2;=\t\2\2"+
		"\2<;\3\2\2\2=>\3\2\2\2><\3\2\2\2>?\3\2\2\2?@\3\2\2\2@A\b\3\2\2A\6\3\2"+
		"\2\2BC\7u\2\2CD\7g\2\2DE\7n\2\2EF\7g\2\2FG\7e\2\2GH\7v\2\2H\b\3\2\2\2"+
		"IJ\7y\2\2JK\7j\2\2KL\7g\2\2LM\7t\2\2MN\7g\2\2N\n\3\2\2\2OP\7o\2\2PQ\7"+
		"q\2\2QR\7p\2\2RS\7v\2\2ST\7j\2\2TU\7u\2\2U\f\3\2\2\2VW\7{\2\2WX\7g\2\2"+
		"XY\7c\2\2YZ\7t\2\2Z[\7u\2\2[\16\3\2\2\2\\]\7q\2\2]^\7t\2\2^\20\3\2\2\2"+
		"_`\7c\2\2`a\7p\2\2ab\7f\2\2b\22\3\2\2\2cd\7p\2\2de\7q\2\2ef\7v\2\2f\24"+
		"\3\2\2\2gh\7*\2\2h\26\3\2\2\2ij\7+\2\2j\30\3\2\2\2kl\7k\2\2lt\7p\2\2m"+
		"n\7p\2\2no\7q\2\2op\7v\2\2pq\7\"\2\2qr\7k\2\2rt\7p\2\2sk\3\2\2\2sm\3\2"+
		"\2\2t\32\3\2\2\2uv\7e\2\2vw\7q\2\2wx\7p\2\2xy\7v\2\2yz\7c\2\2z{\7k\2\2"+
		"{|\7p\2\2|\u0092\7u\2\2}~\7u\2\2~\177\7v\2\2\177\u0080\7c\2\2\u0080\u0081"+
		"\7t\2\2\u0081\u0082\7v\2\2\u0082\u0083\7u\2\2\u0083\u0084\7\"\2\2\u0084"+
		"\u0085\7y\2\2\u0085\u0086\7k\2\2\u0086\u0087\7v\2\2\u0087\u0092\7j\2\2"+
		"\u0088\u0089\7g\2\2\u0089\u008a\7p\2\2\u008a\u008b\7f\2\2\u008b\u008c"+
		"\7u\2\2\u008c\u008d\7\"\2\2\u008d\u008e\7y\2\2\u008e\u008f\7k\2\2\u008f"+
		"\u0090\7v\2\2\u0090\u0092\7j\2\2\u0091u\3\2\2\2\u0091}\3\2\2\2\u0091\u0088"+
		"\3\2\2\2\u0092\34\3\2\2\2\u0093\u00a0\t\3\2\2\u0094\u0095\7@\2\2\u0095"+
		"\u00a0\7?\2\2\u0096\u0097\7>\2\2\u0097\u00a0\7?\2\2\u0098\u00a0\7?\2\2"+
		"\u0099\u009a\7#\2\2\u009a\u00a0\7?\2\2\u009b\u009c\7n\2\2\u009c\u009d"+
		"\7k\2\2\u009d\u009e\7m\2\2\u009e\u00a0\7g\2\2\u009f\u0093\3\2\2\2\u009f"+
		"\u0094\3\2\2\2\u009f\u0096\3\2\2\2\u009f\u0098\3\2\2\2\u009f\u0099\3\2"+
		"\2\2\u009f\u009b\3\2\2\2\u00a0\36\3\2\2\2\u00a1\u00a3\7/\2\2\u00a2\u00a1"+
		"\3\2\2\2\u00a2\u00a3\3\2\2\2\u00a3\u00a5\3\2\2\2\u00a4\u00a6\5)\25\2\u00a5"+
		"\u00a4\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\u00a5\3\2\2\2\u00a7\u00a8\3\2"+
		"\2\2\u00a8 \3\2\2\2\u00a9\u00ab\7/\2\2\u00aa\u00a9\3\2\2\2\u00aa\u00ab"+
		"\3\2\2\2\u00ab\u00ad\3\2\2\2\u00ac\u00ae\5)\25\2\u00ad\u00ac\3\2\2\2\u00ae"+
		"\u00af\3\2\2\2\u00af\u00ad\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00b1\3\2"+
		"\2\2\u00b1\u00b3\7\60\2\2\u00b2\u00b4\5)\25\2\u00b3\u00b2\3\2\2\2\u00b4"+
		"\u00b5\3\2\2\2\u00b5\u00b3\3\2\2\2\u00b5\u00b6\3\2\2\2\u00b6\"\3\2\2\2"+
		"\u00b7\u00b9\5)\25\2\u00b8\u00b7\3\2\2\2\u00b9\u00ba\3\2\2\2\u00ba\u00b8"+
		"\3\2\2\2\u00ba\u00bb\3\2\2\2\u00bb\u00bc\3\2\2\2\u00bc\u00bd\t\4\2\2\u00bd"+
		"$\3\2\2\2\u00be\u00c0\5)\25\2\u00bf\u00be\3\2\2\2\u00c0\u00c1\3\2\2\2"+
		"\u00c1\u00bf\3\2\2\2\u00c1\u00c2\3\2\2\2\u00c2\u00c3\3\2\2\2\u00c3\u00c4"+
		"\t\5\2\2\u00c4&\3\2\2\2\u00c5\u00c7\5)\25\2\u00c6\u00c5\3\2\2\2\u00c7"+
		"\u00c8\3\2\2\2\u00c8\u00c6\3\2\2\2\u00c8\u00c9\3\2\2\2\u00c9\u00ca\3\2"+
		"\2\2\u00ca\u00cb\t\6\2\2\u00cb(\3\2\2\2\u00cc\u00cd\4\62;\2\u00cd*\3\2"+
		"\2\2\u00ce\u00d2\t\7\2\2\u00cf\u00d1\t\b\2\2\u00d0\u00cf\3\2\2\2\u00d1"+
		"\u00d4\3\2\2\2\u00d2\u00d0\3\2\2\2\u00d2\u00d3\3\2\2\2\u00d3,\3\2\2\2"+
		"\u00d4\u00d2\3\2\2\2\u00d5\u00d8\5\37\20\2\u00d6\u00d8\5+\26\2\u00d7\u00d5"+
		"\3\2\2\2\u00d7\u00d6\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9\u00da\7\60\2\2"+
		"\u00da\u00df\5+\26\2\u00db\u00dc\7\60\2\2\u00dc\u00de\5+\26\2\u00dd\u00db"+
		"\3\2\2\2\u00de\u00e1\3\2\2\2\u00df\u00dd\3\2\2\2\u00df\u00e0\3\2\2\2\u00e0"+
		".\3\2\2\2\u00e1\u00df\3\2\2\2\u00e2\u00e3\7$\2\2\u00e3\u00e4\5\65\33\2"+
		"\u00e4\u00e5\7$\2\2\u00e5\60\3\2\2\2\u00e6\u00e7\7^\2\2\u00e7\u00e8\t"+
		"\t\2\2\u00e8\62\3\2\2\2\u00e9\u00ea\t\n\2\2\u00ea\64\3\2\2\2\u00eb\u00ee"+
		"\5\61\31\2\u00ec\u00ee\n\t\2\2\u00ed\u00eb\3\2\2\2\u00ed\u00ec\3\2\2\2"+
		"\u00ee\u00f1\3\2\2\2\u00ef\u00ed\3\2\2\2\u00ef\u00f0\3\2\2\2\u00f0\66"+
		"\3\2\2\2\u00f1\u00ef\3\2\2\2\u00f2\u00f3\7$\2\2\u00f38\3\2\2\2\24\2>s"+
		"\u0091\u009f\u00a2\u00a7\u00aa\u00af\u00b5\u00ba\u00c1\u00c8\u00d2\u00d7"+
		"\u00df\u00ed\u00ef";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}