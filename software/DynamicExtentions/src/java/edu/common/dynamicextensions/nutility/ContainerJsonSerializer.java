package edu.common.dynamicextensions.nutility;

import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

import edu.common.dynamicextensions.domain.nui.CheckBox;
import edu.common.dynamicextensions.domain.nui.ComboBox;
import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.domain.nui.FileUploadControl;
import edu.common.dynamicextensions.domain.nui.Label;
import edu.common.dynamicextensions.domain.nui.ListBox;
import edu.common.dynamicextensions.domain.nui.MultiSelectCheckBox;
import edu.common.dynamicextensions.domain.nui.MultiSelectListBox;
import edu.common.dynamicextensions.domain.nui.NumberField;
import edu.common.dynamicextensions.domain.nui.PageBreak;
import edu.common.dynamicextensions.domain.nui.PermissibleValue;
import edu.common.dynamicextensions.domain.nui.PvDataSource;
import edu.common.dynamicextensions.domain.nui.PvDataSource.Ordering;
import edu.common.dynamicextensions.domain.nui.PvVersion;
import edu.common.dynamicextensions.domain.nui.RadioButton;
import edu.common.dynamicextensions.domain.nui.SelectControl;
import edu.common.dynamicextensions.domain.nui.StringTextField;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.domain.nui.TextArea;
import edu.common.dynamicextensions.domain.nui.TextField;

public class ContainerJsonSerializer implements ContainerSerializer {
	private Container container;
	
	private OutputStream out;
	
	private Writer writer;

	public ContainerJsonSerializer(Container container, OutputStream out) {
		this.container = container;
		this.out = out;		
	}
	
	public ContainerJsonSerializer(Container container, Writer writer) {
		this.container = container;
		this.writer = writer;		
	}
	

	@Override
	public void serialize() {
		Map<String, Object> containerProps = new HashMap<String, Object>();
		
		containerProps.put("name", container.getName());
		containerProps.put("caption", container.getCaption());
		putControls(container, containerProps);
		
		try {
			String json = new Gson().toJson(containerProps);
			if (out != null) {
				out.write(json.getBytes());
			} else {
				writer.write(json);
			}			
		} catch (Exception e) {
			throw new RuntimeException("Error writing to output stream");
		}		
	}
	
	private void putControls(Container container, Map<String, Object> containerProps) {
		List<List<Map<String, Object>>> rows = new ArrayList<List<Map<String, Object>>>();
		containerProps.put("rows", rows);
		
		for (List<Control> rowCtrls : container.getControlsGroupedByRow()) {
			List<Map<String, Object>> row = new ArrayList<Map<String, Object>>();
			for (Control ctrl : rowCtrls) {
				row.add(getCtrlProps(ctrl));
			}
			
			rows.add(row);
		}		
	}

	
	private Map<String, Object> getCtrlProps(Control ctrl) {
		Map<String, Object> ctrlProps = new HashMap<String, Object>();
		ctrlProps.put("name", ctrl.getName());
		ctrlProps.put("udn", ctrl.getUserDefinedName());
		ctrlProps.put("caption", ctrl.getCaption());
		ctrlProps.put("customLabel", ctrl.getCustomLabel());
		ctrlProps.put("labelPosition", ctrl.getLabelPosition());
		ctrlProps.put("toolTip", ctrl.getToolTip());
		ctrlProps.put("skipLogicSourceControl", ctrl.isSkipLogicSourceControl());
		ctrlProps.put("skipLogicTargetControl", ctrl.isSkipLogicTargetControl());
		ctrlProps.put("calculatedSourceControl", ctrl.isCalculatedSourceControl());
		ctrlProps.put("conceptCode", ctrl.getConceptCode());
		ctrlProps.put("conceptPreferredName", ctrl.getConceptPreferredName());
		ctrlProps.put("conceptDefinitionSource", ctrl.getConceptDefinitionSource());
		ctrlProps.put("conceptDefinition", ctrl.getConceptDefinition());
		ctrlProps.put("validationRules", ctrl.getValidationRules());
		
		if (ctrl instanceof TextField) {
			putTextFieldProps((TextField)ctrl, ctrlProps);
		} else if (ctrl instanceof SelectControl) {
			putSelectFieldProps((SelectControl)ctrl, ctrlProps);			
		} else if (ctrl instanceof FileUploadControl) {
			ctrlProps.put("type", "fileUpload");
		} else if (ctrl instanceof DatePicker) {
			putDatePickerProps((DatePicker)ctrl, ctrlProps);
		} else if (ctrl instanceof CheckBox) {
			putCheckBoxProps((CheckBox)ctrl, ctrlProps);
		} else if (ctrl instanceof Label) {
			putLabelProps((Label)ctrl, ctrlProps);
		} else if (ctrl instanceof PageBreak) {
			ctrlProps.put("type", "pageBreak");			
		} else if (ctrl instanceof SubFormControl) {
			putSubFormControlProps((SubFormControl)ctrl, ctrlProps);
		}
		
		return ctrlProps;		
	}
	
	private void putTextFieldProps(TextField ctrl, Map<String, Object> ctrlProps) {
		ctrlProps.put("width", ctrl.getNoOfColumns());
		ctrlProps.put("defaultValue", ctrl.getDefaultValue());
		
		if (ctrl instanceof StringTextField) {
			putStringTextFieldProps((StringTextField)ctrl, ctrlProps);
		} else if (ctrl instanceof NumberField) {
			putNumberFieldProps((NumberField)ctrl, ctrlProps);
		} else if (ctrl instanceof TextArea) {
			putTextAreaProps((TextArea)ctrl, ctrlProps);
		} else {
			throw new RuntimeException("Unknown TextField type");
		}
	}
	
	private void putSelectFieldProps(SelectControl ctrl, Map<String, Object> ctrlProps) {
		PvDataSource pvDataSrc =  ctrl.getPvDataSource();
		ctrlProps.put("dataType", pvDataSrc.getDataType());
		ctrlProps.put("dateFormat", pvDataSrc.getDateFormat());
		
		List<PvVersion> pvVersions =pvDataSrc.getPvVersions();
		Ordering ordering = pvDataSrc.getOrdering();

		if (pvVersions != null) {
			PvVersion pvVersion = pvVersions.get(0);
			List<PermissibleValue> permissibleValues = pvVersion.getPermissibleValues();
			if(ordering.equals(Ordering.ASC)) {
				Collections.sort(permissibleValues);
			} else if (ordering.equals(Ordering.DESC)) {
				Collections.sort(permissibleValues);
				Collections.reverse(permissibleValues);
			} 
			ctrlProps.put("pvs", permissibleValues);		
		}
		
		if (ctrl instanceof ComboBox) {
			ctrlProps.put("type", "combobox");			
		} else if (ctrl instanceof RadioButton) {
			ctrlProps.put("type", "radiobutton");
		} else if (ctrl instanceof MultiSelectCheckBox) {
			ctrlProps.put("type", "checkbox"); 
		} else if (ctrl instanceof ListBox) {
			ctrlProps.put("type", "listbox");
		} else if (ctrl instanceof MultiSelectListBox) {
			ctrlProps.put("type", "multiSelectListbox");
		}				
	}
	
	private void putStringTextFieldProps(StringTextField ctrl, Map<String, Object> ctrlProps) {
		ctrlProps.put("type", "stringTextField");
		ctrlProps.put("url", ctrl.isUrl());
		ctrlProps.put("password", ctrl.isPassword());
	}
	
	private void putNumberFieldProps(NumberField ctrl, Map<String, Object> ctrlProps) {
		ctrlProps.put("type", "numberField");
		ctrlProps.put("noOfDigits", ctrl.getNoOfDigits());
		ctrlProps.put("noOfDigitsAfterDecimal", ctrl.getNoOfDigitsAfterDecimal());
		ctrlProps.put("minValue", ctrl.getMinValue());
		ctrlProps.put("maxValue", ctrl.getMaxValue());
		ctrlProps.put("calculated", ctrl.isCalculated());
		ctrlProps.put("formula", ctrl.getFormula());
	}
	
	private void putTextAreaProps(TextArea ctrl, Map<String, Object> ctrlProps) {
		ctrlProps.put("type", "textArea");
		ctrlProps.put("noOfRows", ctrl.getNoOfRows());
		ctrlProps.put("minLength", ctrl.getMinLength());
		ctrlProps.put("maxLength", ctrl.getMaxLength());
	}
	
	private void putDatePickerProps(DatePicker ctrl, Map<String, Object> ctrlProps) {
		ctrlProps.put("type", "datePicker");
		ctrlProps.put("format", ctrl.getFormat());
		ctrlProps.put("showCalendar", ctrl.showCalendar());
		ctrlProps.put("defaultType", ctrl.getDefaultDateType());
		ctrlProps.put("defaultValue", ctrl.getDefaultDate());
	}

	private void putCheckBoxProps(CheckBox ctrl, Map<String, Object> ctrlProps) {
		ctrlProps.put("type", "booleanCheckbox");
		ctrlProps.put("defaultChecked", ctrl.isDefaultValueChecked());
	}
	
	private void putLabelProps(Label ctrl, Map<String, Object> ctrlProps) {
		ctrlProps.put("type", "label");
		ctrlProps.put("heading", ctrl.isHeading());
		ctrlProps.put("note", ctrl.isNote());
	}
	
	private void putSubFormControlProps(SubFormControl ctrl, Map<String, Object> ctrlProps) {
		ctrlProps.put("type", "subForm");
		putControls(ctrl.getSubContainer(), ctrlProps);
	}
	


}
