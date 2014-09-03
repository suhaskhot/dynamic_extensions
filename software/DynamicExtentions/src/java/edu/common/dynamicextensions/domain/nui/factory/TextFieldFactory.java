package edu.common.dynamicextensions.domain.nui.factory;

import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.StringTextField;
import static edu.common.dynamicextensions.nutility.ParserUtil.*;

public class TextFieldFactory extends AbstractControlFactory {

	public static TextFieldFactory getInstance() {
		return new TextFieldFactory();
	}
	
	@Override
	public String getType() {
		return "textField";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		StringTextField textField = new StringTextField();
		setControlProps(textField, ele, row, xPos);
		
		Integer width = getIntValue(ele, "width", null);
		if (width != null) {
			textField.setNoOfColumns(width);
		}
		String defVal = getTextValue(ele, "defaultValue", "");
		textField.setDefaultValue(defVal);
				
		textField.setUrl(getBooleanValue(ele, "url"));
		textField.setPassword(getBooleanValue(ele, "password"));
		
		Integer minLen = getIntValue(ele, "minLength", null);
		if (minLen != null) {
			textField.setMinLength(minLen);
		}
		
		Integer maxLen = getIntValue(ele, "maxLength", null);
		if (maxLen != null) {
			textField.setMaxLength(maxLen);
		}

		return textField;
	}
}