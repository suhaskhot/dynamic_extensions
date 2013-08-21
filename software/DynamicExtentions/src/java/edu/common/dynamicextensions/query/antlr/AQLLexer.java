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
		T__0=1, WS=2, SELECT=3, WHERE=4, OR=5, AND=6, NOT=7, LP=8, RP=9, FIELD=10, 
		INT=11, FLOAT=12, SLITERAL=13, ESC=14, ID=15, OP=16, ARITH_OP=17, QUOTE=18;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"','", "WS", "'select'", "'where'", "'or'", "'and'", "'not'", "'('", "')'", 
		"FIELD", "INT", "FLOAT", "SLITERAL", "ESC", "ID", "OP", "ARITH_OP", "'\"'"
	};
	public static final String[] ruleNames = {
		"T__0", "WS", "SELECT", "WHERE", "OR", "AND", "NOT", "LP", "RP", "FIELD", 
		"INT", "FLOAT", "SLITERAL", "ESC", "ID", "OP", "ARITH_OP", "SGUTS", "QUOTE"
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
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\2\24\u0098\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\3\2\3\2\3\3\6\3-\n\3\r\3\16\3.\3\3\3\3\3\4\3"+
		"\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7"+
		"\3\7\3\b\3\b\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\5\13Q\n\13\3\13\3\13\3"+
		"\13\3\13\7\13W\n\13\f\13\16\13Z\13\13\3\f\5\f]\n\f\3\f\6\f`\n\f\r\f\16"+
		"\fa\3\r\5\re\n\r\3\r\6\rh\n\r\r\r\16\ri\3\r\3\r\6\rn\n\r\r\r\16\ro\3\16"+
		"\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\7\20{\n\20\f\20\16\20~\13\20"+
		"\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\5\21\u008c"+
		"\n\21\3\22\3\22\3\23\3\23\7\23\u0092\n\23\f\23\16\23\u0095\13\23\3\24"+
		"\3\24\2\25\3\3\1\5\4\2\7\5\1\t\6\1\13\7\1\r\b\1\17\t\1\21\n\1\23\13\1"+
		"\25\f\1\27\r\1\31\16\1\33\17\1\35\20\1\37\21\1!\22\1#\23\1%\2\1\'\24\1"+
		"\3\2\b\5\2\13\f\17\17\"\"\4\2$$^^\5\2C\\aac|\6\2\62;C\\aac|\4\2>>@@\5"+
		"\2,-//\61\61\u00a6\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13"+
		"\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2"+
		"\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2"+
		"!\3\2\2\2\2#\3\2\2\2\2\'\3\2\2\2\3)\3\2\2\2\5,\3\2\2\2\7\62\3\2\2\2\t"+
		"9\3\2\2\2\13?\3\2\2\2\rB\3\2\2\2\17F\3\2\2\2\21J\3\2\2\2\23L\3\2\2\2\25"+
		"P\3\2\2\2\27\\\3\2\2\2\31d\3\2\2\2\33q\3\2\2\2\35u\3\2\2\2\37x\3\2\2\2"+
		"!\u008b\3\2\2\2#\u008d\3\2\2\2%\u0093\3\2\2\2\'\u0096\3\2\2\2)*\7.\2\2"+
		"*\4\3\2\2\2+-\t\2\2\2,+\3\2\2\2-.\3\2\2\2.,\3\2\2\2./\3\2\2\2/\60\3\2"+
		"\2\2\60\61\b\3\2\2\61\6\3\2\2\2\62\63\7u\2\2\63\64\7g\2\2\64\65\7n\2\2"+
		"\65\66\7g\2\2\66\67\7e\2\2\678\7v\2\28\b\3\2\2\29:\7y\2\2:;\7j\2\2;<\7"+
		"g\2\2<=\7t\2\2=>\7g\2\2>\n\3\2\2\2?@\7q\2\2@A\7t\2\2A\f\3\2\2\2BC\7c\2"+
		"\2CD\7p\2\2DE\7f\2\2E\16\3\2\2\2FG\7p\2\2GH\7q\2\2HI\7v\2\2I\20\3\2\2"+
		"\2JK\7*\2\2K\22\3\2\2\2LM\7+\2\2M\24\3\2\2\2NQ\5\27\f\2OQ\5\37\20\2PN"+
		"\3\2\2\2PO\3\2\2\2QR\3\2\2\2RS\7\60\2\2SX\5\37\20\2TU\7\60\2\2UW\5\37"+
		"\20\2VT\3\2\2\2WZ\3\2\2\2XV\3\2\2\2XY\3\2\2\2Y\26\3\2\2\2ZX\3\2\2\2[]"+
		"\7/\2\2\\[\3\2\2\2\\]\3\2\2\2]_\3\2\2\2^`\4\62;\2_^\3\2\2\2`a\3\2\2\2"+
		"a_\3\2\2\2ab\3\2\2\2b\30\3\2\2\2ce\7/\2\2dc\3\2\2\2de\3\2\2\2eg\3\2\2"+
		"\2fh\4\62;\2gf\3\2\2\2hi\3\2\2\2ig\3\2\2\2ij\3\2\2\2jk\3\2\2\2km\7\60"+
		"\2\2ln\4\62;\2ml\3\2\2\2no\3\2\2\2om\3\2\2\2op\3\2\2\2p\32\3\2\2\2qr\7"+
		"$\2\2rs\5%\23\2st\7$\2\2t\34\3\2\2\2uv\7^\2\2vw\t\3\2\2w\36\3\2\2\2x|"+
		"\t\4\2\2y{\t\5\2\2zy\3\2\2\2{~\3\2\2\2|z\3\2\2\2|}\3\2\2\2} \3\2\2\2~"+
		"|\3\2\2\2\177\u008c\t\6\2\2\u0080\u0081\7@\2\2\u0081\u008c\7?\2\2\u0082"+
		"\u0083\7>\2\2\u0083\u008c\7?\2\2\u0084\u008c\7?\2\2\u0085\u0086\7#\2\2"+
		"\u0086\u008c\7?\2\2\u0087\u0088\7n\2\2\u0088\u0089\7k\2\2\u0089\u008a"+
		"\7m\2\2\u008a\u008c\7g\2\2\u008b\177\3\2\2\2\u008b\u0080\3\2\2\2\u008b"+
		"\u0082\3\2\2\2\u008b\u0084\3\2\2\2\u008b\u0085\3\2\2\2\u008b\u0087\3\2"+
		"\2\2\u008c\"\3\2\2\2\u008d\u008e\t\7\2\2\u008e$\3\2\2\2\u008f\u0092\5"+
		"\35\17\2\u0090\u0092\n\3\2\2\u0091\u008f\3\2\2\2\u0091\u0090\3\2\2\2\u0092"+
		"\u0095\3\2\2\2\u0093\u0091\3\2\2\2\u0093\u0094\3\2\2\2\u0094&\3\2\2\2"+
		"\u0095\u0093\3\2\2\2\u0096\u0097\7$\2\2\u0097(\3\2\2\2\17\2.PX\\adio|"+
		"\u008b\u0091\u0093";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}