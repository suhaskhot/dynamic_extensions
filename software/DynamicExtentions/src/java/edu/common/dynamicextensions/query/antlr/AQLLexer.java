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
		INT=11, FLOAT=12, SLITERAL=13, ESC=14, ID=15, OP=16, QUOTE=17;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"','", "WS", "'select'", "'where'", "'or'", "'and'", "'not'", "'('", "')'", 
		"FIELD", "INT", "FLOAT", "SLITERAL", "ESC", "ID", "OP", "'\"'"
	};
	public static final String[] ruleNames = {
		"T__0", "WS", "SELECT", "WHERE", "OR", "AND", "NOT", "LP", "RP", "FIELD", 
		"INT", "FLOAT", "SLITERAL", "ESC", "ID", "OP", "SGUTS", "QUOTE"
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
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\2\23\u0094\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\3\2\3\2\3\3\6\3+\n\3\r\3\16\3,\3\3\3\3\3\4\3\4\3\4\3\4"+
		"\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\b\3"+
		"\b\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\5\13O\n\13\3\13\3\13\3\13\3\13\7"+
		"\13U\n\13\f\13\16\13X\13\13\3\f\5\f[\n\f\3\f\6\f^\n\f\r\f\16\f_\3\r\5"+
		"\rc\n\r\3\r\6\rf\n\r\r\r\16\rg\3\r\3\r\6\rl\n\r\r\r\16\rm\3\16\3\16\3"+
		"\16\3\16\3\17\3\17\3\17\3\20\3\20\7\20y\n\20\f\20\16\20|\13\20\3\21\3"+
		"\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\3\21\5\21\u008a\n\21"+
		"\3\22\3\22\7\22\u008e\n\22\f\22\16\22\u0091\13\22\3\23\3\23\2\24\3\3\1"+
		"\5\4\2\7\5\1\t\6\1\13\7\1\r\b\1\17\t\1\21\n\1\23\13\1\25\f\1\27\r\1\31"+
		"\16\1\33\17\1\35\20\1\37\21\1!\22\1#\2\1%\23\1\3\2\7\5\2\13\f\17\17\""+
		"\"\4\2$$^^\5\2C\\aac|\6\2\62;C\\aac|\4\2>>@@\u00a2\2\3\3\2\2\2\2\5\3\2"+
		"\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21"+
		"\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2"+
		"\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2%\3\2\2\2\3\'\3\2\2\2\5*\3"+
		"\2\2\2\7\60\3\2\2\2\t\67\3\2\2\2\13=\3\2\2\2\r@\3\2\2\2\17D\3\2\2\2\21"+
		"H\3\2\2\2\23J\3\2\2\2\25N\3\2\2\2\27Z\3\2\2\2\31b\3\2\2\2\33o\3\2\2\2"+
		"\35s\3\2\2\2\37v\3\2\2\2!\u0089\3\2\2\2#\u008f\3\2\2\2%\u0092\3\2\2\2"+
		"\'(\7.\2\2(\4\3\2\2\2)+\t\2\2\2*)\3\2\2\2+,\3\2\2\2,*\3\2\2\2,-\3\2\2"+
		"\2-.\3\2\2\2./\b\3\2\2/\6\3\2\2\2\60\61\7u\2\2\61\62\7g\2\2\62\63\7n\2"+
		"\2\63\64\7g\2\2\64\65\7e\2\2\65\66\7v\2\2\66\b\3\2\2\2\678\7y\2\289\7"+
		"j\2\29:\7g\2\2:;\7t\2\2;<\7g\2\2<\n\3\2\2\2=>\7q\2\2>?\7t\2\2?\f\3\2\2"+
		"\2@A\7c\2\2AB\7p\2\2BC\7f\2\2C\16\3\2\2\2DE\7p\2\2EF\7q\2\2FG\7v\2\2G"+
		"\20\3\2\2\2HI\7*\2\2I\22\3\2\2\2JK\7+\2\2K\24\3\2\2\2LO\5\27\f\2MO\5\37"+
		"\20\2NL\3\2\2\2NM\3\2\2\2OP\3\2\2\2PQ\7\60\2\2QV\5\37\20\2RS\7\60\2\2"+
		"SU\5\37\20\2TR\3\2\2\2UX\3\2\2\2VT\3\2\2\2VW\3\2\2\2W\26\3\2\2\2XV\3\2"+
		"\2\2Y[\7/\2\2ZY\3\2\2\2Z[\3\2\2\2[]\3\2\2\2\\^\4\62;\2]\\\3\2\2\2^_\3"+
		"\2\2\2_]\3\2\2\2_`\3\2\2\2`\30\3\2\2\2ac\7/\2\2ba\3\2\2\2bc\3\2\2\2ce"+
		"\3\2\2\2df\4\62;\2ed\3\2\2\2fg\3\2\2\2ge\3\2\2\2gh\3\2\2\2hi\3\2\2\2i"+
		"k\7\60\2\2jl\4\62;\2kj\3\2\2\2lm\3\2\2\2mk\3\2\2\2mn\3\2\2\2n\32\3\2\2"+
		"\2op\7$\2\2pq\5#\22\2qr\7$\2\2r\34\3\2\2\2st\7^\2\2tu\t\3\2\2u\36\3\2"+
		"\2\2vz\t\4\2\2wy\t\5\2\2xw\3\2\2\2y|\3\2\2\2zx\3\2\2\2z{\3\2\2\2{ \3\2"+
		"\2\2|z\3\2\2\2}\u008a\t\6\2\2~\177\7@\2\2\177\u008a\7?\2\2\u0080\u0081"+
		"\7>\2\2\u0081\u008a\7?\2\2\u0082\u008a\7?\2\2\u0083\u0084\7#\2\2\u0084"+
		"\u008a\7?\2\2\u0085\u0086\7n\2\2\u0086\u0087\7k\2\2\u0087\u0088\7m\2\2"+
		"\u0088\u008a\7g\2\2\u0089}\3\2\2\2\u0089~\3\2\2\2\u0089\u0080\3\2\2\2"+
		"\u0089\u0082\3\2\2\2\u0089\u0083\3\2\2\2\u0089\u0085\3\2\2\2\u008a\"\3"+
		"\2\2\2\u008b\u008e\5\35\17\2\u008c\u008e\n\3\2\2\u008d\u008b\3\2\2\2\u008d"+
		"\u008c\3\2\2\2\u008e\u0091\3\2\2\2\u008f\u008d\3\2\2\2\u008f\u0090\3\2"+
		"\2\2\u0090$\3\2\2\2\u0091\u008f\3\2\2\2\u0092\u0093\7$\2\2\u0093&\3\2"+
		"\2\2\17\2,NVZ_bgmz\u0089\u008d\u008f";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}