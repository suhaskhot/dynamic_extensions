package edu.common.dynamicextensions.domain.nui.factory;

import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.RadioButton;

import static edu.common.dynamicextensions.nutility.ParserUtil.*;

public class RadioButtonFactory extends AbstractControlFactory {

	public static RadioButtonFactory getInstance() {
		return new RadioButtonFactory();
	}
	
	@Override
	public String getType() {
		return "radioButton";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		RadioButton radioButton = new RadioButton();
		setSelectProps(radioButton, ele, row, xPos, props.getProperty("pvDir"));
		
		Integer optionsPerRow = getIntValue(ele, "optionsPerRow", null);
		if (optionsPerRow != null) {
			radioButton.setOptionsPerRow(optionsPerRow);
		}
		return radioButton;
	}
}
