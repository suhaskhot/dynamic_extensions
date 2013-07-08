package edu.common.dynamicextensions.nutility;

import static edu.common.dynamicextensions.nutility.XmlUtil.writeElement;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementEnd;
import static edu.common.dynamicextensions.nutility.XmlUtil.writeElementStart;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import edu.common.dynamicextensions.domain.nui.Action;
import edu.common.dynamicextensions.domain.nui.CheckBox;
import edu.common.dynamicextensions.domain.nui.ComboBox;
import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.DatePicker;
import edu.common.dynamicextensions.domain.nui.DisableAction;
import edu.common.dynamicextensions.domain.nui.EnableAction;
import edu.common.dynamicextensions.domain.nui.FileUploadControl;
import edu.common.dynamicextensions.domain.nui.HideAction;
import edu.common.dynamicextensions.domain.nui.Label;
import edu.common.dynamicextensions.domain.nui.ListBox;
import edu.common.dynamicextensions.domain.nui.MultiSelectCheckBox;
import edu.common.dynamicextensions.domain.nui.MultiSelectListBox;
import edu.common.dynamicextensions.domain.nui.NumberField;
import edu.common.dynamicextensions.domain.nui.PermissibleValue;
import edu.common.dynamicextensions.domain.nui.RadioButton;
import edu.common.dynamicextensions.domain.nui.SelectControl;
import edu.common.dynamicextensions.domain.nui.ShowAction;
import edu.common.dynamicextensions.domain.nui.ShowPvAction;
import edu.common.dynamicextensions.domain.nui.SkipCondition;
import edu.common.dynamicextensions.domain.nui.SkipRule;
import edu.common.dynamicextensions.domain.nui.SkipRule.LogicalOp;
import edu.common.dynamicextensions.domain.nui.StringTextField;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.domain.nui.TextArea;
import edu.common.dynamicextensions.domain.nui.TextField;
import edu.common.dynamicextensions.domain.nui.ValidationRule;
import edu.wustl.cab2b.common.exception.RuntimeException;

public class ContainerXmlSerializer implements ContainerSerializer  {
	private static Map<Class<?>, String> actionNameMap = initializeSkipActionNameMap();;
	
	private Map<Class<?>, ControlSerializer> serializerMap = new HashMap<Class<?>, ControlSerializer>();

	private Container container;
	
	private String outDir;
	
	private Writer writer; 
	
	public ContainerXmlSerializer(Container container, String outDir) {
		this.container = container;
		
		createOpDirectory(outDir);
		try {
			String xmlFile = new StringBuilder()
				.append(outDir).append(File.separator)
				.append(container.getName()).append(".xml")
				.toString();
			
			writer = new BufferedWriter(new FileWriter(xmlFile));
		} catch (IOException e) {
			throw new RuntimeException("Error creating output file writer", e);
		}
		
		initializeControlSerializers();
	}

	public void serialize() {
		try {
			emitContainerStart();
									
			emitViewStart();
			serializeView(container);			
			emitViewEnd();
	
			emitSkipRulesStart();
			serializeSkipRules(container);
			emitSkipRulesEnd();
	
			emitContainerEnd();
			
			writer.flush();
		} catch(IOException e) {
			throw new RuntimeException("Error serializing container", e);
		} finally {
			IoUtil.close(writer);
		}
	}

	public void serializeView(Container container) {
		emitContainerProps(container);
		
		Collection<List<Control>> rows = groupControlsByRow(container.getControls());		
		for (List<Control> rowCtrls : rows) {
			emitStartRow();

			for (Control ctrl : rowCtrls) {
				serializeControl(ctrl);
			}
			
			emitEndRow();
		}
	}

	private void serializeControl(Control ctrl) {
		getControlSerializer(ctrl).serialize(ctrl);
	}

	private ControlSerializer getControlSerializer(Control ctrl) {
		ControlSerializer serializer = serializerMap.get(ctrl.getClass());
		
		if (serializer == null) {
			throw new RuntimeException("Unknown control type: " + ctrl.getClass());
		}

		return serializer;
	}
	
	private Collection<List<Control>> groupControlsByRow(Collection<Control> controls) {
		Map<Integer, List<Control>> rows = new TreeMap<Integer, List<Control>>();
		
		for (Control ctrl : controls) {
			List<Control> row = rows.get(ctrl.getSequenceNumber());
			if (row == null) {
				row = new ArrayList<Control>();
				rows.put(ctrl.getSequenceNumber(), row);
			}
			
			int xPos = ctrl.getxPos();
			int i;
			for (i = 0; i < row.size(); ++i) {
				if (row.get(i).getxPos() > xPos) {
					break;
				}
			}
			
			row.add(i, ctrl);
		}
		
		return rows.values();
	}

	private void createOpDirectory(String outDir) {
		File dirFile = new File(outDir);
		
		if(!dirFile.isDirectory()) {
			if (!dirFile.mkdirs()) {
				throw new RuntimeException("Failed to create output directory: " + outDir);
			}
		}		
		this.outDir = outDir;
	}


	private void initializeControlSerializers() {
		serializerMap.put(StringTextField.class,     new StringTextFieldSerializer());
		serializerMap.put(NumberField.class,         new NumberFieldSerializer());
		serializerMap.put(TextArea.class,            new TextAreaSerializer());
		serializerMap.put(Label.class,               new LabelSerializer());
		serializerMap.put(DatePicker.class,          new DatePickerSerializer());
		serializerMap.put(ComboBox.class,            new ComboBoxSerializer());
		serializerMap.put(CheckBox.class,            new CheckBoxSerializer());
		serializerMap.put(MultiSelectCheckBox.class, new MultiSelectCheckBoxSerializer());
		serializerMap.put(RadioButton.class,         new RadioButtonXmlSerializer());
		serializerMap.put(FileUploadControl.class,   new FileUploadSerializer());
		serializerMap.put(MultiSelectListBox.class,  new ListBoxSerializer());
		serializerMap.put(SubFormControl.class,      new SubFormControlSerializer());
	}
	
	private static Map<Class<?>, String> initializeSkipActionNameMap() {
		Map<Class<?>, String> actionNameMap = new HashMap<Class<?>, String>();
		actionNameMap.put(HideAction.class,     "hide");
		actionNameMap.put(ShowAction.class,     "show");
		actionNameMap.put(ShowPvAction.class,   "showPv");
		actionNameMap.put(EnableAction.class,   "enable");
		actionNameMap.put(DisableAction.class,  "disable");
		
		return actionNameMap;
	}

	private void writePvsToFile(List<PermissibleValue> pvs, String filename) {
		writeElement(writer, "optionsFile", createCsvFile(pvs, filename, outDir));
	}

	private abstract class ControlSerializer {
	
		public abstract void serialize(Control ctrl) ;
		
		protected void serializeControlProps(Control ctrl) {
			writeElement(writer, "name", 		ctrl.getName());
			writeElement(writer, "caption", 	ctrl.getCaption());
			writeElement(writer, "customLabel", ctrl.getCustomLabel());
			writeElement(writer, "phi", 		ctrl.isPhi());
			writeElement(writer, "mandatory", 	ctrl.isMandatory());
			writeElement(writer, "toolTip", 	ctrl.getToolTip());
			writeElement(writer, "showLabel",   ctrl.showLabel());
			writeElement(writer, "showInGrid",  ctrl.showInGrid());
		}
	}

	public abstract class TextFieldSerializer extends ControlSerializer {
		
		protected void serializeTextFieldProps(TextField textField) {
			serializeControlProps(textField);
			writeElement(writer, "width",        textField.getNoOfColumns());
			writeElement(writer, "defaultValue", textField.getDefaultValue());
		}
	}
	
	public abstract class SelectControlSerializer extends ControlSerializer {
		
		protected void serializeSelectCtrlProps(SelectControl ctrl) {
			serializeControlProps(ctrl);
			
			writeElementStart(writer, "options");
			
			Date today = Calendar.getInstance().getTime();
			List<PermissibleValue> pvs = ctrl.getPvDataSource().getPermissibleValues(today);			
			writePvValues(pvs, ctrl.getName());
			
			writeElementEnd(writer, "options");
		}
	}

	private class StringTextFieldSerializer extends TextFieldSerializer {
		public void serialize(Control ctrl) {
			StringTextField textField = (StringTextField)ctrl;
			
			writeElementStart(writer, "textField");	
			serializeTextFieldProps(textField);
			
			writeElement(writer, "url",      textField.isUrl());
			writeElement(writer, "password", textField.isPassword());
						
			for (ValidationRule rule : ctrl.getValidationRules()) {
				if (!rule.getName().equals("textLength")) {
					continue;
				}
				
				for (Entry<String, String> ruleParam : rule.getParams().entrySet()) {
					String prop = "";
					if (ruleParam.getKey().equals("min")) {
						prop = "minLength";
					} else if (ruleParam.getKey().equals("max")) {
						prop = "maxLength";
					} 
					
					if (!prop.isEmpty()) {
						writeElement(writer, prop, ruleParam.getValue());
					}
				}
			}
			
			writeElementEnd(writer, "textField");
		}
	}

	private class NumberFieldSerializer extends TextFieldSerializer {
		public void serialize(Control ctrl) {
			NumberField numField = (NumberField)ctrl;
			
			writeElementStart(writer, "numberField");
			serializeTextFieldProps(numField);

			writeElement(writer, "noOfDigits", numField.getNoOfDigits());			
			if (numField.getNoOfDigitsAfterDecimal() != 0) {
				writeElement(writer, "noOfDigitsAfterDecimal", numField.getNoOfDigitsAfterDecimal());
			}
			
			writeElement(writer, "formula", 			   numField.getFormula());
			writeElement(writer, "measurementUnits", 	   numField.getMeasurementUnits());
		
			for (ValidationRule valRule : ctrl.getValidationRules()) {				
				if (!valRule.getName().equals("range")) {
					continue;
				}
				
				for (Entry<String, String> ruleParam : valRule.getParams().entrySet()) {
					String prop = "";
					if (ruleParam.equals("min")) {
						prop = "minValue";
					} else if (ruleParam.equals("max")) {
						prop = "maxValue";
					} 
					
					if (!prop.isEmpty()) {
						writeElement(writer, prop, ruleParam.getValue());
					}
				}
			}
			
			writeElementEnd(writer, "numberField");
		}
	}
	
	private class TextAreaSerializer extends TextFieldSerializer {
		public void serialize(Control ctrl) {
			TextArea textArea = (TextArea) ctrl;
		
			writeElementStart(writer, "textArea");
			serializeTextFieldProps(textArea);
			
			writeElement(writer, "height", textArea.getNoOfRows());			
			writeElementEnd(writer, "textArea");
		}
	}
	
	private class LabelSerializer extends ControlSerializer {
		public void serialize(Control ctrl) {
			Label label = (Label) ctrl;
			
			writeElementStart(writer, "label");
			writeElement(writer, "name", ctrl.getName());

			if (label.isHeading()) {
				writeElement(writer, "heading", label.getCaption());
			} else if (label.isNote()){
				writeElement(writer, "note", label.getCaption());
			} else {
				writeElement(writer, "caption", 	ctrl.getCaption());
			}
			
			writeElement(writer, "customLabel", ctrl.getCustomLabel());
			writeElement(writer, "phi", 		ctrl.isPhi());
			writeElement(writer, "mandatory", 	ctrl.isMandatory());
			writeElement(writer, "toolTip", 	ctrl.getToolTip());
			writeElementEnd(writer, "label");
		}
	}
	
	private class DatePickerSerializer extends ControlSerializer {
		public void serialize(Control ctrl) {
			DatePicker datePicker = new DatePicker();
			
			writeElementStart(writer, "datePicker");
			serializeControlProps(datePicker);
			
			writeElement(writer, "format", 		datePicker.getFormat());
			writeElement(writer, "showCal", 	datePicker.showCalendar());
			
			String defaultDate = "none";
			switch (datePicker.getDefaultDateType()) {
			    case NONE:
			    	defaultDate = "none";
			    	break;
			    	
			    case CURRENT_DATE:
			    	defaultDate = "current_date";
			    	break;
			    	
			    case PREDEFINED:
			    	SimpleDateFormat sdf = new SimpleDateFormat(datePicker.getFormat());
			    	defaultDate = sdf.format(datePicker.getDefaultDate());
			    	break;
			}
			
			writeElement(writer, "default", defaultDate);
			writeElementEnd(writer, "datePicker");
		}
	}
	
	private class ComboBoxSerializer extends SelectControlSerializer {
		public void serialize(Control ctrl) {
			ComboBox dropDown = (ComboBox) ctrl;
			
			writeElementStart(writer, "dropDown");
			serializeSelectCtrlProps(dropDown);
			
			writeElement(writer, "lazyLoad",      dropDown.isLazyPvFetchingEnabled());
			writeElement(writer, "width", 		  dropDown.getNoOfColumns());			
			writeElement(writer, "minQueryChars", dropDown.getMinQueryChars());				
			writeElementEnd(writer, "dropDown");
		}
	}
	
	private class CheckBoxSerializer extends ControlSerializer {
		public void serialize(Control ctrl) {
			CheckBox checkBox = (CheckBox) ctrl;
			
			writeElementStart(writer, "booleanCheckBox");
			serializeControlProps(ctrl);
			
			writeElement(writer, "checked", checkBox.isDefaultValueChecked());			
			writeElementEnd(writer, "booleanCheckBox");
		}
	}
	
	private class MultiSelectCheckBoxSerializer extends SelectControlSerializer {
		public void serialize(Control ctrl) {
			MultiSelectCheckBox msCheckBox = (MultiSelectCheckBox) ctrl;
			
			writeElementStart(writer, "checkBox");			
			serializeSelectCtrlProps(msCheckBox);			
			writeElement(writer, "optionsPerRow", msCheckBox.getOptionsPerRow());			
			writeElementEnd(writer, "checkBox");
		}
	}
	
	private class RadioButtonXmlSerializer extends SelectControlSerializer {
		public void serialize(Control ctrl) {
			RadioButton radioButton = (RadioButton) ctrl;
			
			writeElementStart(writer, "radioButton");
			serializeSelectCtrlProps(radioButton);			
			writeElement(writer, "optionsPerRow", radioButton.getOptionsPerRow());			
			writeElementEnd(writer, "radioButton");
		}
	}
	
	private class ListBoxSerializer extends SelectControlSerializer {
		public void serialize(Control ctrl) {
			ListBox listBox = (ListBox) ctrl;
			
			writeElementStart(writer, "listBox");			
			serializeSelectCtrlProps(listBox);
			
			writeElement(writer, "autoCompleteDropdown", listBox.isAutoCompleteDropdownEnabled());			
			writeElement(writer, "noOfRows", 			 listBox.getNoOfRows());	
			writeElement(writer, "minQueryChars", 		 listBox.getMinQueryChars());	
			
			if (listBox instanceof MultiSelectListBox) {
				writeElement(writer, "multiSelect", true);			
			} else {
				writeElement(writer, "multiSelect", false);
			}
			
			writeElementEnd(writer, "listBox");
		}
	}
	
	private class FileUploadSerializer extends ControlSerializer {
		public void serialize(Control ctrl) {
			FileUploadControl fileCtrl = (FileUploadControl) ctrl;
			
			writeElementStart(writer, "fileUpload");			
			serializeControlProps(fileCtrl);			
			writeElementEnd(writer, "fileUpload");
		}
	}
	
	
	private class SubFormControlSerializer extends ControlSerializer {
		public void serialize(Control ctrl) {
			SubFormControl sfCtrl = (SubFormControl) ctrl;
			
			writeElementStart(writer, "subForm");			
			writeElement(writer, "maxEntries", 		   sfCtrl.getNoOfEntries());		
			writeElement(writer, "showAddMoreLink",    sfCtrl.showAddMoreLink());	
			writeElement(writer, "pasteButtonEnabled", sfCtrl.isPasteButtonEnabled());			

			serializeView(sfCtrl.getSubContainer());			
			writeElementEnd(writer, "subForm");
		}
	}
	
	private void emitContainerStart() {
		writeElementStart(writer, "form");
	}
	
	private void emitContainerProps(Container container) {
		writeElement(writer, "id", 		container.getId());
		writeElement(writer, "name", 	container.getName());
		writeElement(writer, "caption", container.getCaption());
	}

	private void emitContainerEnd() {
		writeElementEnd(writer, "form");
	}
	
	private void emitViewStart() {
		writeElementStart(writer, "view");
	}

	private void emitViewEnd() {
		 writeElementEnd(writer, "view");
	}
	
	private void emitStartRow() {
		writeElementStart(writer, "row");
	}
	
	private void emitEndRow() {
		writeElementEnd(writer, "row");
	}

	private void emitSkipRulesStart() {
		writeElementStart(writer, "skipRules");
	}
	
	private void emitSkipRulesEnd() {
		writeElementEnd(writer, "skipRules");
	}
	
	private class ControlAction {
		Control control;
		Action action;
	}
	
	private void serializeSkipRules(Container container) {
		Map<String, List<ControlAction>> orCondActionMap = new HashMap<String, List<ControlAction>>();
		Map<String, List<ControlAction>> andCondActionMap = new HashMap<String, List<ControlAction>>();

		populateConditionActionMap(container, orCondActionMap, andCondActionMap);		
		serializeSkipRules(orCondActionMap,  LogicalOp.OR);
		serializeSkipRules(andCondActionMap, LogicalOp.AND);
	}
	
	private void populateConditionActionMap(
			Container container, Map<String, List<ControlAction>> orCondActionMap,
			Map<String, List<ControlAction>> andCondActionMap) {
		
		for (Control ctrl :  container.getControls()) {
			if (ctrl instanceof SubFormControl) {
				SubFormControl sfCtrl = (SubFormControl)ctrl;
				populateConditionActionMap(sfCtrl.getSubContainer(), orCondActionMap, andCondActionMap);
			} else {
				for (SkipRule rule : ctrl.getSkipRules()) {
					if (rule.getLogicalOp().equals(LogicalOp.OR)) {
						populateConditionActionMap(ctrl, rule, orCondActionMap);
					} else {
						populateConditionActionMap(ctrl, rule, andCondActionMap);
					}
				}
			}
		}		
	}

	private void populateConditionActionMap(
			Control ctrl, SkipRule rule, 
			Map<String, List<ControlAction>> condActionMap) {

		StringBuilder condKey = new StringBuilder();
		for(SkipCondition condition : rule.getConditions()) {
			String op = condition.getRelationalOp().name();
			String field = container.getControlCanonicalName(condition.getSourceControl());
			String value = condition.getValue();
			condKey.append(field).append(":")
				.append(op).append(":")
				.append(value).append("#");
		}
		
		//
		// Check whether action exists for the same set of Conditions
		// Edition the ConditionActionMap
		//
		ControlAction controlAction = new ControlAction();
		controlAction.action = rule.getAction();
		controlAction.control = ctrl;
		
		List<ControlAction> controlActions = condActionMap.get(condKey.toString());
		if (controlActions == null) {
			controlActions = new ArrayList<ControlAction>();
			condActionMap.put(condKey.toString(), controlActions);
		}
		
		controlActions.add(controlAction);
	}

	private void serializeSkipRules(Map<String, List<ControlAction>> conditionActionMap, LogicalOp logicalOp) {
		
		for(Entry<String, List<ControlAction>> entry : conditionActionMap.entrySet()){
			writeElementStart(writer, "skipRule");
			
			writeElementStart(writer, getLogicalOp(logicalOp));
			for(String condition : entry.getKey().split("#")) {
				writeCondition(condition);  
			}
			writeElementEnd(writer, getLogicalOp(logicalOp));
			
			
			writeElementStart(writer, "actions");
			for(ControlAction ctrlAction : entry.getValue()){
				String action = actionNameMap.get(ctrlAction.action.getClass());
				String field = container.getControlCanonicalName(ctrlAction.control);
				Map<String, String> actionAttrs = Collections.singletonMap("field", field);
				
				if(action.equals("showPv")){
					ShowPvAction pvAction = (ShowPvAction) ctrlAction.action;
					
					writeElementStart(writer, action, actionAttrs);
					writeElementStart(writer, "options");
				
					writePvValues(pvAction.getListOfPvs(), field.concat("-skipLogic"));
					
					writeElementEnd(writer, "options");
					writeElementEnd(writer, action);
				} else {
					writeElement(writer, action, null, actionAttrs);
				}
			}
			
			writeElementEnd(writer, "actions");
			writeElementEnd(writer, "skipRule");
		}		
	}

	private void writePvValues(List<PermissibleValue> pvs, String fileName) {
		if(pvs.size() > 10) {
			writePvsToFile(pvs, fileName);
		} else {
			for (PermissibleValue pv : pvs){
				writeElement(writer, "option", pv.getValue());
			}
		}
	}

	private void writeCondition(String condition) {
		String[] condParts = condition.split(":"); // [ <source_field> <op> <value> ]
		
		Map<String,String> attrs = new HashMap<String, String>();
		attrs.put("field",  condParts[0]);
		attrs.put("op", 	condParts[1]);
		attrs.put("value",  condParts[2]);
		
		writeElement(writer, "condition", null, attrs);
	}

	private String getLogicalOp(LogicalOp logicalOp) {
		String logicalOpStr = "oneOf";
		if(logicalOp.equals(LogicalOp.AND)){
			logicalOpStr = "all";
		}
		
		return logicalOpStr;
	}
	
	private String createCsvFile(
			List<PermissibleValue> permissibleValues, 
			String fileName, String outDir) {
		
		StringBuilder csvFile = null;
		PrintWriter csvWriter = null;
		try {
			StringBuilder pvDir = new StringBuilder().append(outDir).append(File.separator).append("pvs");
			File pvDirFile = new File(pvDir.toString());
			
			if (!pvDirFile.exists() && !pvDirFile.mkdirs()) {
				throw new RuntimeException("Unable to create pv directory");
			}
			
			csvFile =  new StringBuilder(pvDir).append(File.separator).append(fileName).append(".csv");
			csvWriter = new PrintWriter(new FileWriter(csvFile.toString()));
			 
			for(PermissibleValue pv : permissibleValues) {
				csvWriter.println(pv.getValue());
			}
		} catch(IOException e){
			throw new RuntimeException("Error occured while creating .csv file" + csvFile.toString());
		} finally {
			IoUtil.close(csvWriter);
		}
		
		return new StringBuilder().append(fileName).append(".csv").toString();
	}
}