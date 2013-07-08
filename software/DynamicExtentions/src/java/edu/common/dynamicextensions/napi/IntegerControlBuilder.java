package edu.common.dynamicextensions.napi;

public interface IntegerControlBuilder extends ControlBuilder {
	public IntegerControlBuilder minValue(int minValue);
	
	public IntegerControlBuilder maxValue(int maxValue);
	
	public IntegerControlBuilder autoCalculate(boolean autoCalculate);
	
	public IntegerControlBuilder formula(String formula);
}
