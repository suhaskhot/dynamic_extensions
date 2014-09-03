package edu.common.dynamicextensions.domain.nui.factory;

import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;

public interface ControlFactory {
	public String getType();
	
	public Control parseControl(Element ele, int row, int xPos, Properties props);
}
