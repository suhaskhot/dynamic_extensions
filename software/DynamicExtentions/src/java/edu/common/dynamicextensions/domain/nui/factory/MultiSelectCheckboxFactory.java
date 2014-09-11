package edu.common.dynamicextensions.domain.nui.factory;

import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.MultiSelectCheckBox;

import static edu.common.dynamicextensions.nutility.ParserUtil.*;

public class MultiSelectCheckboxFactory extends AbstractControlFactory {

	public static MultiSelectCheckboxFactory getInstance() {
		return new MultiSelectCheckboxFactory();
	}
	
	@Override
	public String getType() {
		return "checkBox";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		MultiSelectCheckBox msCheckBox = new MultiSelectCheckBox();
		setSelectProps(msCheckBox, ele, row, xPos, props.getProperty("pvDir"));
		
		Integer optionsPerRow = getIntValue(ele, "optionsPerRow", null);
		if (optionsPerRow != null) {
			msCheckBox.setOptionsPerRow(optionsPerRow);
		}
		
		msCheckBox.setParentKey(getTextValue(ele, "parentKey"));
		msCheckBox.setForeignKey(getTextValue(ele, "foreignKey"));
		msCheckBox.setTableName(getTextValue(ele, "table"));
		
		return msCheckBox;
	}

}
