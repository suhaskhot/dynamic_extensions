
package edu.wustl.dynamicextensions.formdesigner.mapper;

import java.util.HashMap;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.CheckBox;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.domain.nui.ListBox;
import edu.common.dynamicextensions.domain.nui.MultiSelectCheckBox;
import edu.common.dynamicextensions.domain.nui.MultiSelectListBox;
import edu.common.dynamicextensions.domain.nui.NumberField;
import edu.common.dynamicextensions.domain.nui.RadioButton;
import edu.common.dynamicextensions.domain.nui.StringTextField;
import edu.common.dynamicextensions.domain.nui.TextArea;
import edu.common.dynamicextensions.domain.nui.TextField;
import edu.wustl.dynamicextensions.formdesigner.utility.CSDConstants;

/**
 * @author Sanjay
 *
 */
public class ControlMapper
{

	private final Map<String, DefaultControlMapper> CONTROL_MAPPER_MAP = new HashMap<String, DefaultControlMapper>()
	{

		private static final long serialVersionUID = 1252410916139427174L;

		{
			put(CSDConstants.STRING_TEXT_FIELD, new StringTextFieldMapper());
			put(CSDConstants.NUMERIC_FIELD, new NumericFieldMapper());
			put(CSDConstants.TEXT_AREA, new TextAreaMapper());
			put(CSDConstants.RADIO_BUTTON, new RadioButtonMapper());
			put(CSDConstants.CHECK_BOX, new CheckBoxMapper());
			put(CSDConstants.LIST_BOX, new ListBoxMapper());
			put(CSDConstants.MULTISELECT_BOX, new MultiSelectBoxMapper());
			put(CSDConstants.MULTISELECT_CHECK_BOX, new MultiSelectCheckBoxMapper());
			put(CSDConstants.DATE_PICKER, new DatePickerMapper());
		}
	};

	/**
	 * @param control
	 * @return
	 */
	private String getControlType(Control control)
	{
		String type = "";
		if (control instanceof StringTextField)
		{
			type = CSDConstants.STRING_TEXT_FIELD;
		}
		else if (control instanceof NumberField)
		{
			type = CSDConstants.NUMERIC_FIELD;
		}
		else if (control instanceof DatePicker)
		{
			type = CSDConstants.DATE_PICKER;
		}
		else if (control instanceof TextArea)
		{
			type = CSDConstants.TEXT_AREA;
		}
		else if (control instanceof RadioButton)
		{
			type = CSDConstants.RADIO_BUTTON;
		}
		else if (control instanceof CheckBox)
		{
			type = CSDConstants.CHECK_BOX;
		}
		else if (control instanceof ListBox)
		{
			type = CSDConstants.LIST_BOX;
		}
		else if (control instanceof MultiSelectListBox)
		{
			type = CSDConstants.MULTISELECT_BOX;
		}
		else if (control instanceof MultiSelectCheckBox)
		{
			type = CSDConstants.MULTISELECT_CHECK_BOX;
		}
		return type;
	}

	public Control propertiesToControl(Properties controlProps) throws Exception
	{

		String type = controlProps.getString(CSDConstants.CONTROL_TYPE);
		return ((DefaultControlMapper) CONTROL_MAPPER_MAP.get(type))
				.propertiesToControl(controlProps);
	}

	public Properties controlToProperties(Control control)
	{
		return CONTROL_MAPPER_MAP.get(getControlType(control)).controlToProperties(control);
	}

	private class StringTextFieldMapper extends TextFieldControlMapper
	{

		@Override
		public Control propertiesToControl(Properties controlProps)
		{
			StringTextField control = new StringTextField();
			setTextFieldProperties(controlProps, control);
			return control;
		}

		@Override
		public Properties controlToProperties(Control control)
		{
			Properties controlProps = new Properties();
			controlProps.setProperty(CSDConstants.CONTROL_TYPE, CSDConstants.STRING_TEXT_FIELD);
			getTextFieldProperties(controlProps, (TextField) control);
			return controlProps;
		}
	}

	private class NumericFieldMapper extends TextFieldControlMapper
	{

		@Override
		public Control propertiesToControl(Properties controlProps)
		{
			NumberField control = new NumberField();
			setTextFieldProperties(controlProps, control);
			if (controlProps.contains("noOfDigits"))
			{
				control.setNoOfDigits(controlProps.getInteger("noOfDigits"));
			}
			if (controlProps.contains("noOfDigitsAfterDecimal"))
			{
				control.setNoOfDigitsAfterDecimal(controlProps.getInteger("noOfDigitsAfterDecimal"));
			}
			if (controlProps.contains("minimumValue"))
			{
				control.setMinValue(controlProps.getString("minimumValue"));
			}
			if (controlProps.contains("maximumValue"))
			{
				control.setMaxValue(controlProps.getString("maximumValue"));
			}
			control.setCalculated(controlProps.getBoolean("isCalculated"));

			return control;
		}

		@Override
		public Properties controlToProperties(Control control)
		{
			Properties controlProps = new Properties();
			controlProps.setProperty(CSDConstants.CONTROL_TYPE, CSDConstants.NUMERIC_FIELD);
			getTextFieldProperties(controlProps, (TextField) control);
			controlProps.setProperty("noOfDigits", ((NumberField) control).getNoOfDigits());
			controlProps.setProperty("noOfDigitsAfterDecimal",
					((NumberField) control).getNoOfDigitsAfterDecimal());
			/*controlProps.setProperty("minimumValue",
					((NumberField) control).get);
			controlProps.setProperty("maximumValue",
					((NumberField) control).getNoOfDigitsAfterDecimal());*/
			controlProps.setProperty("isCalculated", ((NumberField) control).isCalculated());

			return controlProps;
		}
	}

	private class TextAreaMapper extends TextFieldControlMapper
	{

		@Override
		public Control propertiesToControl(Properties controlProps)
		{
			TextArea control = new TextArea();
			setTextFieldProperties(controlProps, (TextField) control);
			controlProps.setProperty(CSDConstants.CONTROL_TYPE, CSDConstants.TEXT_AREA);
			if (controlProps.contains("noOfRows"))
			{
				control.setNoOfRows(controlProps.getInteger("noOfRows"));
			}
			return control;
		}

		@Override
		public Properties controlToProperties(Control control)
		{
			Properties controlProps = new Properties();
			controlProps.setProperty(CSDConstants.CONTROL_TYPE, CSDConstants.TEXT_AREA);
			getCommonProperties(controlProps, control);
			return controlProps;
		}
	}

	private class RadioButtonMapper extends DefaultControlMapper
	{

		@Override
		public Control propertiesToControl(Properties controlProps) throws Exception
		{
			RadioButton control = new RadioButton();
			setCommonProperties(controlProps, control);
			control.setPvDataSource(PvMapper.propertiesToPvDataSource(controlProps));
			return control;
		}

		@Override
		public Properties controlToProperties(Control control)
		{
			Properties controlProps = new Properties();
			controlProps.setProperty(CSDConstants.CONTROL_TYPE, CSDConstants.RADIO_BUTTON);
			getCommonProperties(controlProps, control);
			PvMapper.pVDataSourcetoProperties(((RadioButton) control).getPvDataSource(),
					controlProps);
			return controlProps;
		}
	}

	private class CheckBoxMapper extends DefaultControlMapper
	{

		@Override
		public Control propertiesToControl(Properties controlProps)
		{
			CheckBox control = new CheckBox();
			setCommonProperties(controlProps, control);
			return control;
		}

		@Override
		public Properties controlToProperties(Control control)
		{
			Properties controlProps = new Properties();
			controlProps.setProperty(CSDConstants.CONTROL_TYPE, CSDConstants.CHECK_BOX);
			getCommonProperties(controlProps, control);
			return controlProps;
		}
	}

	private class ListBoxMapper extends DefaultControlMapper
	{

		@Override
		public Control propertiesToControl(Properties controlProps) throws Exception
		{
			ListBox control = new ListBox();
			setCommonProperties(controlProps, control);
			control.setPvDataSource(PvMapper.propertiesToPvDataSource(controlProps));
			return control;
		}

		@Override
		public Properties controlToProperties(Control control)
		{
			Properties controlProps = new Properties();
			controlProps.setProperty(CSDConstants.CONTROL_TYPE, CSDConstants.LIST_BOX);
			getCommonProperties(controlProps, control);
			PvMapper.pVDataSourcetoProperties(((ListBox) control).getPvDataSource(), controlProps);
			return controlProps;
		}
	}

	private class MultiSelectBoxMapper extends DefaultControlMapper
	{

		@Override
		public Control propertiesToControl(Properties controlProps) throws Exception
		{
			MultiSelectListBox control = new MultiSelectListBox();
			setCommonProperties(controlProps, control);
			control.setPvDataSource(PvMapper.propertiesToPvDataSource(controlProps));
			return control;
		}

		@Override
		public Properties controlToProperties(Control control)
		{
			Properties controlProps = new Properties();
			controlProps.setProperty(CSDConstants.CONTROL_TYPE, CSDConstants.MULTISELECT_BOX);
			getCommonProperties(controlProps, control);
			PvMapper.pVDataSourcetoProperties(((MultiSelectListBox) control).getPvDataSource(),
					controlProps);
			return controlProps;
		}
	}

	private class MultiSelectCheckBoxMapper extends DefaultControlMapper
	{

		@Override
		public Control propertiesToControl(Properties controlProps) throws Exception
		{
			MultiSelectCheckBox control = new MultiSelectCheckBox();
			setCommonProperties(controlProps, control);
			control.setPvDataSource(PvMapper.propertiesToPvDataSource(controlProps));
			return control;
		}

		@Override
		public Properties controlToProperties(Control control)
		{
			Properties controlProps = new Properties();
			controlProps.setProperty(CSDConstants.CONTROL_TYPE, CSDConstants.MULTISELECT_CHECK_BOX);
			getCommonProperties(controlProps, control);
			PvMapper.pVDataSourcetoProperties(((MultiSelectCheckBox) control).getPvDataSource(),
					controlProps);
			return controlProps;
		}
	}

	private class DatePickerMapper extends DefaultControlMapper
	{

		@Override
		public Control propertiesToControl(Properties controlProps)
		{
			DatePicker control = new DatePicker();
			setCommonProperties(controlProps, control);
			control.setFormat(controlProps.getString("format"));
			return control;
		}

		@Override
		public Properties controlToProperties(Control control)
		{
			Properties controlProps = new Properties();
			controlProps.setProperty(CSDConstants.CONTROL_TYPE, CSDConstants.DATE_PICKER);
			getCommonProperties(controlProps, control);
			return controlProps;
		}
	}
}
