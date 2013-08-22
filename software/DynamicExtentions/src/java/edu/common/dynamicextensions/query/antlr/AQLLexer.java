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
		LP=10, RP=11, FIELD=12, INT=13, FLOAT=14, YEAR=15, MONTH=16, DAY=17, DIGIT=18, 
		SLITERAL=19, ESC=20, ID=21, OP=22, ARITH_OP=23, QUOTE=24;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"','", "WS", "'select'", "'where'", "'months'", "'years'", "'or'", "'and'", 
		"'not'", "'('", "')'", "FIELD", "INT", "FLOAT", "YEAR", "MONTH", "DAY", 
		"DIGIT", "SLITERAL", "ESC", "ID", "OP", "ARITH_OP", "'\"'"
	};
	public static final String[] ruleNames = {
		"T__0", "WS", "SELECT", "WHERE", "MONTHS", "YEARS", "OR", "AND", "NOT", 
		"LP", "RP", "FIELD", "INT", "FLOAT", "YEAR", "MONTH", "DAY", "DIGIT", 
		"SLITERAL", "ESC", "ID", "OP", "ARITH_OP", "SGUTS", "QUOTE"
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
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\2\32\u00c8\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\3\2\3\2\3\3\6\39\n\3\r\3\16\3:\3\3\3\3\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3"+
		"\7\3\7\3\7\3\7\3\7\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\n\3\n\3\n\3\n\3\13\3"+
		"\13\3\f\3\f\3\r\3\r\5\rj\n\r\3\r\3\r\3\r\3\r\7\rp\n\r\f\r\16\rs\13\r\3"+
		"\16\5\16v\n\16\3\16\6\16y\n\16\r\16\16\16z\3\17\5\17~\n\17\3\17\6\17\u0081"+
		"\n\17\r\17\16\17\u0082\3\17\3\17\6\17\u0087\n\17\r\17\16\17\u0088\3\20"+
		"\6\20\u008c\n\20\r\20\16\20\u008d\3\20\3\20\3\21\6\21\u0093\n\21\r\21"+
		"\16\21\u0094\3\21\3\21\3\22\6\22\u009a\n\22\r\22\16\22\u009b\3\22\3\22"+
		"\3\23\3\23\3\24\3\24\3\24\3\24\3\25\3\25\3\25\3\26\3\26\7\26\u00ab\n\26"+
		"\f\26\16\26\u00ae\13\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3"+
		"\27\3\27\3\27\5\27\u00bc\n\27\3\30\3\30\3\31\3\31\7\31\u00c2\n\31\f\31"+
		"\16\31\u00c5\13\31\3\32\3\32\2\33\3\3\1\5\4\2\7\5\1\t\6\1\13\7\1\r\b\1"+
		"\17\t\1\21\n\1\23\13\1\25\f\1\27\r\1\31\16\1\33\17\1\35\20\1\37\21\1!"+
		"\22\1#\23\1%\24\1\'\25\1)\26\1+\27\1-\30\1/\31\1\61\2\1\63\32\1\3\2\13"+
		"\5\2\13\f\17\17\"\"\4\2[[{{\4\2OOoo\4\2FFff\4\2$$^^\5\2C\\aac|\6\2\62"+
		";C\\aac|\4\2>>@@\5\2,-//\61\61\u00d9\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2"+
		"\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23"+
		"\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2"+
		"\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2"+
		"\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\63\3\2\2\2\3\65\3\2\2\2\58\3\2"+
		"\2\2\7>\3\2\2\2\tE\3\2\2\2\13K\3\2\2\2\rR\3\2\2\2\17X\3\2\2\2\21[\3\2"+
		"\2\2\23_\3\2\2\2\25c\3\2\2\2\27e\3\2\2\2\31i\3\2\2\2\33u\3\2\2\2\35}\3"+
		"\2\2\2\37\u008b\3\2\2\2!\u0092\3\2\2\2#\u0099\3\2\2\2%\u009f\3\2\2\2\'"+
		"\u00a1\3\2\2\2)\u00a5\3\2\2\2+\u00a8\3\2\2\2-\u00bb\3\2\2\2/\u00bd\3\2"+
		"\2\2\61\u00c3\3\2\2\2\63\u00c6\3\2\2\2\65\66\7.\2\2\66\4\3\2\2\2\679\t"+
		"\2\2\28\67\3\2\2\29:\3\2\2\2:8\3\2\2\2:;\3\2\2\2;<\3\2\2\2<=\b\3\2\2="+
		"\6\3\2\2\2>?\7u\2\2?@\7g\2\2@A\7n\2\2AB\7g\2\2BC\7e\2\2CD\7v\2\2D\b\3"+
		"\2\2\2EF\7y\2\2FG\7j\2\2GH\7g\2\2HI\7t\2\2IJ\7g\2\2J\n\3\2\2\2KL\7o\2"+
		"\2LM\7q\2\2MN\7p\2\2NO\7v\2\2OP\7j\2\2PQ\7u\2\2Q\f\3\2\2\2RS\7{\2\2ST"+
		"\7g\2\2TU\7c\2\2UV\7t\2\2VW\7u\2\2W\16\3\2\2\2XY\7q\2\2YZ\7t\2\2Z\20\3"+
		"\2\2\2[\\\7c\2\2\\]\7p\2\2]^\7f\2\2^\22\3\2\2\2_`\7p\2\2`a\7q\2\2ab\7"+
		"v\2\2b\24\3\2\2\2cd\7*\2\2d\26\3\2\2\2ef\7+\2\2f\30\3\2\2\2gj\5\33\16"+
		"\2hj\5+\26\2ig\3\2\2\2ih\3\2\2\2jk\3\2\2\2kl\7\60\2\2lq\5+\26\2mn\7\60"+
		"\2\2np\5+\26\2om\3\2\2\2ps\3\2\2\2qo\3\2\2\2qr\3\2\2\2r\32\3\2\2\2sq\3"+
		"\2\2\2tv\7/\2\2ut\3\2\2\2uv\3\2\2\2vx\3\2\2\2wy\5%\23\2xw\3\2\2\2yz\3"+
		"\2\2\2zx\3\2\2\2z{\3\2\2\2{\34\3\2\2\2|~\7/\2\2}|\3\2\2\2}~\3\2\2\2~\u0080"+
		"\3\2\2\2\177\u0081\5%\23\2\u0080\177\3\2\2\2\u0081\u0082\3\2\2\2\u0082"+
		"\u0080\3\2\2\2\u0082\u0083\3\2\2\2\u0083\u0084\3\2\2\2\u0084\u0086\7\60"+
		"\2\2\u0085\u0087\5%\23\2\u0086\u0085\3\2\2\2\u0087\u0088\3\2\2\2\u0088"+
		"\u0086\3\2\2\2\u0088\u0089\3\2\2\2\u0089\36\3\2\2\2\u008a\u008c\5%\23"+
		"\2\u008b\u008a\3\2\2\2\u008c\u008d\3\2\2\2\u008d\u008b\3\2\2\2\u008d\u008e"+
		"\3\2\2\2\u008e\u008f\3\2\2\2\u008f\u0090\t\3\2\2\u0090 \3\2\2\2\u0091"+
		"\u0093\5%\23\2\u0092\u0091\3\2\2\2\u0093\u0094\3\2\2\2\u0094\u0092\3\2"+
		"\2\2\u0094\u0095\3\2\2\2\u0095\u0096\3\2\2\2\u0096\u0097\t\4\2\2\u0097"+
		"\"\3\2\2\2\u0098\u009a\5%\23\2\u0099\u0098\3\2\2\2\u009a\u009b\3\2\2\2"+
		"\u009b\u0099\3\2\2\2\u009b\u009c\3\2\2\2\u009c\u009d\3\2\2\2\u009d\u009e"+
		"\t\5\2\2\u009e$\3\2\2\2\u009f\u00a0\4\62;\2\u00a0&\3\2\2\2\u00a1\u00a2"+
		"\7$\2\2\u00a2\u00a3\5\61\31\2\u00a3\u00a4\7$\2\2\u00a4(\3\2\2\2\u00a5"+
		"\u00a6\7^\2\2\u00a6\u00a7\t\6\2\2\u00a7*\3\2\2\2\u00a8\u00ac\t\7\2\2\u00a9"+
		"\u00ab\t\b\2\2\u00aa\u00a9\3\2\2\2\u00ab\u00ae\3\2\2\2\u00ac\u00aa\3\2"+
		"\2\2\u00ac\u00ad\3\2\2\2\u00ad,\3\2\2\2\u00ae\u00ac\3\2\2\2\u00af\u00bc"+
		"\t\t\2\2\u00b0\u00b1\7@\2\2\u00b1\u00bc\7?\2\2\u00b2\u00b3\7>\2\2\u00b3"+
		"\u00bc\7?\2\2\u00b4\u00bc\7?\2\2\u00b5\u00b6\7#\2\2\u00b6\u00bc\7?\2\2"+
		"\u00b7\u00b8\7n\2\2\u00b8\u00b9\7k\2\2\u00b9\u00ba\7m\2\2\u00ba\u00bc"+
		"\7g\2\2\u00bb\u00af\3\2\2\2\u00bb\u00b0\3\2\2\2\u00bb\u00b2\3\2\2\2\u00bb"+
		"\u00b4\3\2\2\2\u00bb\u00b5\3\2\2\2\u00bb\u00b7\3\2\2\2\u00bc.\3\2\2\2"+
		"\u00bd\u00be\t\n\2\2\u00be\60\3\2\2\2\u00bf\u00c2\5)\25\2\u00c0\u00c2"+
		"\n\6\2\2\u00c1\u00bf\3\2\2\2\u00c1\u00c0\3\2\2\2\u00c2\u00c5\3\2\2\2\u00c3"+
		"\u00c1\3\2\2\2\u00c3\u00c4\3\2\2\2\u00c4\62\3\2\2\2\u00c5\u00c3\3\2\2"+
		"\2\u00c6\u00c7\7$\2\2\u00c7\64\3\2\2\2\22\2:iquz}\u0082\u0088\u008d\u0094"+
		"\u009b\u00ac\u00bb\u00c1\u00c3";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}