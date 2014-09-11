package edu.common.dynamicextensions.domain.nui.factory;

import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.CheckBox;
import edu.common.dynamicextensions.domain.nui.Control;
import static edu.common.dynamicextensions.nutility.ParserUtil.getBooleanValue;

public class BooleanCheckboxFactory extends AbstractControlFactory {
	public static BooleanCheckboxFactory getInstance() {
		return new BooleanCheckboxFactory();
	}

	@Override
	public String getType() {
		return "booleanCheckBox";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		CheckBox checkBox = new CheckBox();
		setControlProps(checkBox, ele, row, xPos);		
		checkBox.setDefaultValueChecked(getBooleanValue(ele, "checked"));
		return checkBox;
	}
}
