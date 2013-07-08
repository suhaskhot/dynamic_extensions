package edu.common.dynamicextensions.napi;

import edu.common.dynamicextensions.domain.SemanticProperty;
import edu.common.dynamicextensions.domain.nui.Control;

public interface ControlBuilder {
	ControlBuilder name(String name);
	
	ControlBuilder phi(boolean phi);
	
	ControlBuilder required(boolean required);
	
	ControlBuilder semanticProperty(SemanticProperty semanticProperty);
	
	ControlBuilder defaultValue(String defaultValue);
	
	Control getControl();
}
