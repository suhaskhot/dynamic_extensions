package edu.common.dynamicextensions.domain.nui.factory;

import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.ComboBox;
import edu.common.dynamicextensions.domain.nui.Control;

import static edu.common.dynamicextensions.nutility.ParserUtil.*;

public class DropdownFactory extends AbstractControlFactory {

	public static DropdownFactory getInstance() {
		return new DropdownFactory();
	}
	
	@Override
	public String getType() {
		return "dropDown";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		ComboBox comboBox = new ComboBox();
		setSelectProps(comboBox, ele, row, xPos, props.getProperty("pvDir"));
		
		comboBox.setLazyPvFetchingEnabled(getBooleanValue(ele, "lazyLoad"));
		
		Integer minQueryChars = getIntValue(ele, "minQueryChars", null);
		if (minQueryChars != null) {
			comboBox.setMinQueryChars(minQueryChars);
		}
		
		Integer width = getIntValue(ele, "width", null);
		if (width != null) {
			comboBox.setNoOfColumns(width);
		}
		
		return comboBox;
	}
}
