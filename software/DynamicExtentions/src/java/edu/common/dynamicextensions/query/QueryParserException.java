package edu.common.dynamicextensions.query;

import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class QueryParserException extends RuntimeException {
	private static final long serialVersionUID = 6564470605442099072L;
	
	private NoViableAltException e;
	
	public QueryParserException(ParseCancellationException e) {
		this.e = (NoViableAltException)e.getCause();		
	}
	
	public String getMessage() {
		Token token = e.getOffendingToken();		
		StringBuilder str = new StringBuilder();		
		str.append("No viable alternative at input ").append(token.getText())
		   .append(". Position: ").append(token.getLine()).append(":").append(token.getCharPositionInLine());
		return str.toString();
	}
}
