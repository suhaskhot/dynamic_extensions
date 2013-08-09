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
		WS=1, OR=2, AND=3, NOT=4, LP=5, RP=6, FIELD=7, INT=8, FLOAT=9, SLITERAL=10, 
		ESC=11, ID=12, OP=13, QUOTE=14;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"WS", "'or'", "'and'", "'not'", "'('", "')'", "FIELD", "INT", "FLOAT", 
		"SLITERAL", "ESC", "ID", "OP", "'\"'"
	};
	public static final String[] ruleNames = {
		"WS", "OR", "AND", "NOT", "LP", "RP", "FIELD", "INT", "FLOAT", "SLITERAL", 
		"ESC", "ID", "OP", "SGUTS", "QUOTE"
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
		case 0: WS_action((RuleContext)_localctx, actionIndex); break;
		}
	}
	private void WS_action(RuleContext _localctx, int actionIndex) {
		switch (actionIndex) {
		case 0: skip();  break;
		}
	}

	public static final String _serializedATN =
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\2\20\177\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\3\2\6\2#\n\2\r\2\16"+
		"\2$\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\7\3"+
		"\7\3\b\3\b\5\b:\n\b\3\b\3\b\3\b\3\b\7\b@\n\b\f\b\16\bC\13\b\3\t\5\tF\n"+
		"\t\3\t\6\tI\n\t\r\t\16\tJ\3\n\5\nN\n\n\3\n\6\nQ\n\n\r\n\16\nR\3\n\3\n"+
		"\6\nW\n\n\r\n\16\nX\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r\7\rd\n\r\f"+
		"\r\16\rg\13\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3"+
		"\16\5\16u\n\16\3\17\3\17\7\17y\n\17\f\17\16\17|\13\17\3\20\3\20\2\21\3"+
		"\3\2\5\4\1\7\5\1\t\6\1\13\7\1\r\b\1\17\t\1\21\n\1\23\13\1\25\f\1\27\r"+
		"\1\31\16\1\33\17\1\35\2\1\37\20\1\3\2\7\5\2\13\f\17\17\"\"\4\2$$^^\5\2"+
		"C\\aac|\6\2\62;C\\aac|\4\2>>@@\u008d\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2"+
		"\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23"+
		"\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\37\3\2"+
		"\2\2\3\"\3\2\2\2\5(\3\2\2\2\7+\3\2\2\2\t/\3\2\2\2\13\63\3\2\2\2\r\65\3"+
		"\2\2\2\179\3\2\2\2\21E\3\2\2\2\23M\3\2\2\2\25Z\3\2\2\2\27^\3\2\2\2\31"+
		"a\3\2\2\2\33t\3\2\2\2\35z\3\2\2\2\37}\3\2\2\2!#\t\2\2\2\"!\3\2\2\2#$\3"+
		"\2\2\2$\"\3\2\2\2$%\3\2\2\2%&\3\2\2\2&\'\b\2\2\2\'\4\3\2\2\2()\7q\2\2"+
		")*\7t\2\2*\6\3\2\2\2+,\7c\2\2,-\7p\2\2-.\7f\2\2.\b\3\2\2\2/\60\7p\2\2"+
		"\60\61\7q\2\2\61\62\7v\2\2\62\n\3\2\2\2\63\64\7*\2\2\64\f\3\2\2\2\65\66"+
		"\7+\2\2\66\16\3\2\2\2\67:\5\21\t\28:\5\31\r\29\67\3\2\2\298\3\2\2\2:;"+
		"\3\2\2\2;<\7\60\2\2<A\5\31\r\2=>\7\60\2\2>@\5\31\r\2?=\3\2\2\2@C\3\2\2"+
		"\2A?\3\2\2\2AB\3\2\2\2B\20\3\2\2\2CA\3\2\2\2DF\7/\2\2ED\3\2\2\2EF\3\2"+
		"\2\2FH\3\2\2\2GI\4\62;\2HG\3\2\2\2IJ\3\2\2\2JH\3\2\2\2JK\3\2\2\2K\22\3"+
		"\2\2\2LN\7/\2\2ML\3\2\2\2MN\3\2\2\2NP\3\2\2\2OQ\4\62;\2PO\3\2\2\2QR\3"+
		"\2\2\2RP\3\2\2\2RS\3\2\2\2ST\3\2\2\2TV\7\60\2\2UW\4\62;\2VU\3\2\2\2WX"+
		"\3\2\2\2XV\3\2\2\2XY\3\2\2\2Y\24\3\2\2\2Z[\7$\2\2[\\\5\35\17\2\\]\7$\2"+
		"\2]\26\3\2\2\2^_\7^\2\2_`\t\3\2\2`\30\3\2\2\2ae\t\4\2\2bd\t\5\2\2cb\3"+
		"\2\2\2dg\3\2\2\2ec\3\2\2\2ef\3\2\2\2f\32\3\2\2\2ge\3\2\2\2hu\t\6\2\2i"+
		"j\7@\2\2ju\7?\2\2kl\7>\2\2lu\7?\2\2mu\7?\2\2no\7#\2\2ou\7?\2\2pq\7n\2"+
		"\2qr\7k\2\2rs\7m\2\2su\7g\2\2th\3\2\2\2ti\3\2\2\2tk\3\2\2\2tm\3\2\2\2"+
		"tn\3\2\2\2tp\3\2\2\2u\34\3\2\2\2vy\5\27\f\2wy\n\3\2\2xv\3\2\2\2xw\3\2"+
		"\2\2y|\3\2\2\2zx\3\2\2\2z{\3\2\2\2{\36\3\2\2\2|z\3\2\2\2}~\7$\2\2~ \3"+
		"\2\2\2\17\2$9AEJMRXetxz";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}