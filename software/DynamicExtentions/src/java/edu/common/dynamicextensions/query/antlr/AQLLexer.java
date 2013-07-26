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
		ESC=11, ID=12, NOP=13, SOP=14, QUOTE=15;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] tokenNames = {
		"<INVALID>",
		"WS", "'or'", "'and'", "'not'", "'('", "')'", "FIELD", "INT", "FLOAT", 
		"SLITERAL", "ESC", "ID", "NOP", "SOP", "'\"'"
	};
	public static final String[] ruleNames = {
		"WS", "OR", "AND", "NOT", "LP", "RP", "FIELD", "INT", "FLOAT", "SLITERAL", 
		"ESC", "ID", "NOP", "SOP", "SGUTS", "QUOTE"
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
		"\3\uacf5\uee8c\u4f5d\u8b0d\u4a45\u78bd\u1b2f\u3378\2\21\u0086\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\3\2\6"+
		"\2%\n\2\r\2\16\2&\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5"+
		"\3\6\3\6\3\7\3\7\3\b\3\b\5\b<\n\b\3\b\3\b\3\b\3\b\7\bB\n\b\f\b\16\bE\13"+
		"\b\3\t\5\tH\n\t\3\t\6\tK\n\t\r\t\16\tL\3\n\5\nP\n\n\3\n\6\nS\n\n\r\n\16"+
		"\nT\3\n\3\n\6\nY\n\n\r\n\16\nZ\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\r\3\r"+
		"\7\rf\n\r\f\r\16\ri\13\r\3\16\3\16\3\16\3\16\3\16\3\16\3\16\3\16\5\16"+
		"s\n\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\5\17|\n\17\3\20\3\20\7\20\u0080"+
		"\n\20\f\20\16\20\u0083\13\20\3\21\3\21\2\22\3\3\2\5\4\1\7\5\1\t\6\1\13"+
		"\7\1\r\b\1\17\t\1\21\n\1\23\13\1\25\f\1\27\r\1\31\16\1\33\17\1\35\20\1"+
		"\37\2\1!\21\1\3\2\7\5\2\13\f\17\17\"\"\4\2$$^^\5\2C\\aac|\6\2\62;C\\a"+
		"ac|\4\2>>@@\u0095\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13"+
		"\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2"+
		"\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2!\3\2\2\2\3$\3"+
		"\2\2\2\5*\3\2\2\2\7-\3\2\2\2\t\61\3\2\2\2\13\65\3\2\2\2\r\67\3\2\2\2\17"+
		";\3\2\2\2\21G\3\2\2\2\23O\3\2\2\2\25\\\3\2\2\2\27`\3\2\2\2\31c\3\2\2\2"+
		"\33r\3\2\2\2\35{\3\2\2\2\37\u0081\3\2\2\2!\u0084\3\2\2\2#%\t\2\2\2$#\3"+
		"\2\2\2%&\3\2\2\2&$\3\2\2\2&\'\3\2\2\2\'(\3\2\2\2()\b\2\2\2)\4\3\2\2\2"+
		"*+\7q\2\2+,\7t\2\2,\6\3\2\2\2-.\7c\2\2./\7p\2\2/\60\7f\2\2\60\b\3\2\2"+
		"\2\61\62\7p\2\2\62\63\7q\2\2\63\64\7v\2\2\64\n\3\2\2\2\65\66\7*\2\2\66"+
		"\f\3\2\2\2\678\7+\2\28\16\3\2\2\29<\5\21\t\2:<\5\31\r\2;9\3\2\2\2;:\3"+
		"\2\2\2<=\3\2\2\2=>\7\60\2\2>C\5\31\r\2?@\7\60\2\2@B\5\31\r\2A?\3\2\2\2"+
		"BE\3\2\2\2CA\3\2\2\2CD\3\2\2\2D\20\3\2\2\2EC\3\2\2\2FH\7/\2\2GF\3\2\2"+
		"\2GH\3\2\2\2HJ\3\2\2\2IK\4\62;\2JI\3\2\2\2KL\3\2\2\2LJ\3\2\2\2LM\3\2\2"+
		"\2M\22\3\2\2\2NP\7/\2\2ON\3\2\2\2OP\3\2\2\2PR\3\2\2\2QS\4\62;\2RQ\3\2"+
		"\2\2ST\3\2\2\2TR\3\2\2\2TU\3\2\2\2UV\3\2\2\2VX\7\60\2\2WY\4\62;\2XW\3"+
		"\2\2\2YZ\3\2\2\2ZX\3\2\2\2Z[\3\2\2\2[\24\3\2\2\2\\]\7$\2\2]^\5\37\20\2"+
		"^_\7$\2\2_\26\3\2\2\2`a\7^\2\2ab\t\3\2\2b\30\3\2\2\2cg\t\4\2\2df\t\5\2"+
		"\2ed\3\2\2\2fi\3\2\2\2ge\3\2\2\2gh\3\2\2\2h\32\3\2\2\2ig\3\2\2\2js\t\6"+
		"\2\2kl\7@\2\2ls\7?\2\2mn\7>\2\2ns\7?\2\2os\7?\2\2pq\7#\2\2qs\7?\2\2rj"+
		"\3\2\2\2rk\3\2\2\2rm\3\2\2\2ro\3\2\2\2rp\3\2\2\2s\34\3\2\2\2t|\7?\2\2"+
		"uv\7#\2\2v|\7?\2\2wx\7n\2\2xy\7k\2\2yz\7m\2\2z|\7g\2\2{t\3\2\2\2{u\3\2"+
		"\2\2{w\3\2\2\2|\36\3\2\2\2}\u0080\5\27\f\2~\u0080\n\3\2\2\177}\3\2\2\2"+
		"\177~\3\2\2\2\u0080\u0083\3\2\2\2\u0081\177\3\2\2\2\u0081\u0082\3\2\2"+
		"\2\u0082 \3\2\2\2\u0083\u0081\3\2\2\2\u0084\u0085\7$\2\2\u0085\"\3\2\2"+
		"\2\20\2&;CGLOTZgr{\177\u0081";
	public static final ATN _ATN =
		ATNSimulator.deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}