package edu.common.dynamicextensions.napi;

public interface NumberControlBuilder extends ControlBuilder {
	public NumberControlBuilder precision(int digits);
	
	public NumberControlBuilder minValue(double minValue); // change data type
		
	public NumberControlBuilder maxValue(double maxValue);
		
	public NumberControlBuilder autoCalculate(boolean autoCalculate);
		
	public NumberControlBuilder formula(String formula);
}
