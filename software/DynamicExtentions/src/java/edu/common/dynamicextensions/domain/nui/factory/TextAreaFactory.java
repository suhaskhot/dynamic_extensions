package edu.common.dynamicextensions.domain.nui.factory;

import java.util.Properties;

import org.w3c.dom.Element;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.TextArea;
import static edu.common.dynamicextensions.nutility.ParserUtil.*;

public class TextAreaFactory extends AbstractControlFactory {
	public static TextAreaFactory getInstance() {
		return new TextAreaFactory();
	}

	@Override
	public String getType() {
		return "textArea";
	}

	@Override
	public Control parseControl(Element ele, int row, int xPos, Properties props) {
		TextArea textArea = new TextArea();
		setControlProps(textArea, ele, row, xPos);
		
		Integer width = getIntValue(ele, "width", null);
		if (width != null) {
			textArea.setNoOfColumns(width);
		}
		String defVal = getTextValue(ele, "defaultValue", "");
		textArea.setDefaultValue(defVal);
		
		Integer height = getIntValue(ele, "height", null);
		if (height != null) {
			textArea.setNoOfRows(height);
		}
		
		Integer minLen = getIntValue(ele, "minLength", null);
		if (minLen != null) {
			textArea.setMinLength(minLen);
		}

		Integer maxLen = getIntValue(ele, "maxLength", null);
		if (maxLen != null) {
			textArea.setMaxLength(maxLen);
		}

		return textArea;
	}
}
