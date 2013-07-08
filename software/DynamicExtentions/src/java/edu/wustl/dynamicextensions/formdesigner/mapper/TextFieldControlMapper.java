
package edu.wustl.dynamicextensions.formdesigner.mapper;

import edu.common.dynamicextensions.domain.nui.TextField;

public abstract class TextFieldControlMapper extends DefaultControlMapper
{

	/**
	 * @param controlProps
	 * @param control
	 */
	protected void setTextFieldProperties(Properties controlProps, TextField control)
	{
		setCommonProperties(controlProps, control);
		control.setDefaultValue(controlProps.getString("defaultValue"));
		if (controlProps.contains("width"))
		{
			control.setNoOfColumns(controlProps.getInteger("width"));
		}
	}

	/**
	 * @param controlProps
	 * @param control
	 */
	protected void getTextFieldProperties(Properties controlProps, TextField control)
	{
		getCommonProperties(controlProps, control);
		controlProps.setProperty("defaultValue", control.getDefaultValue());
		controlProps.setProperty("width", control.getNoOfColumns());
	}

}
