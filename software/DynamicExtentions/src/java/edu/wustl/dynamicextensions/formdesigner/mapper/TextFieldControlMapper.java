
package edu.wustl.dynamicextensions.formdesigner.mapper;

import edu.common.dynamicextensions.domain.nui.TextField;

public abstract class TextFieldControlMapper extends DefaultControlMapper {

	/**
	 * @param controlProps
	 * @param control
	 */
	protected void setTextFieldProperties(Properties controlProps, TextField control) {
		setCommonProperties(controlProps, control);
		
		Integer width = controlProps.getInteger("width");
		if (width != null) {
			control.setNoOfColumns(width);
		}
		
		String defaultValue = controlProps.getString("defaultValue");
		if (defaultValue != null) {
			control.setDefaultValue(defaultValue);
		}
	}

	/**
	 * @param controlProps
	 * @param control
	 */
	protected void getTextFieldProperties(Properties controlProps, TextField control) {
		getCommonProperties(controlProps, control);
		controlProps.setProperty("defaultValue", control.getDefaultValue());
		controlProps.setProperty("width", control.getNoOfColumns());
	}

}
