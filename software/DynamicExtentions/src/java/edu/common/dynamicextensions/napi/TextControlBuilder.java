package edu.common.dynamicextensions.napi;

public interface TextControlBuilder extends ControlBuilder {
	public TextControlBuilder width(int width);
	
	public TextControlBuilder maxLength(int maxLength);
	
	public TextControlBuilder numLines(int numLines);
}
