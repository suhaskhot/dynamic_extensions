package edu.common.dynamicextensions.napi;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.SubFormControl;

public class FormData {
	private Container container;
	
	private Long recordId;
	
	private Map<String, ControlValue> fieldValues = new LinkedHashMap<String, ControlValue>();

	public FormData(Container container) {
		this.container = container;
	}

	public Container getContainer() {
		return container;
	}

	public void setContainer(Container container) {
		this.container = container;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public Collection<ControlValue> getFieldValues() {
		return fieldValues.values();
	}

	public void setFieldValues(Map<String, ControlValue> fieldValues) {
		this.fieldValues = fieldValues;
	}
	
	public void addFieldValue(ControlValue controlValue) {
		fieldValues.put(controlValue.getControl().getName(), controlValue);
	}
	
	public ControlValue getFieldValue(String name) {
		ControlValue cv = fieldValues.get(name);
		if (cv == null) {
			Control ctrl = container.getControl(name);
			if (ctrl != null) {
				cv = new ControlValue(ctrl, null);
				fieldValues.put(name, cv);
			}
		}
		
		return cv;
	}
	

	public ControlValue getFieldValue(Control control, Integer rowNumber) {
		ControlValue fieldValue = null;
		
		if (getContainer().getId().equals(control.getContainer().getId())) {
			fieldValue = getFieldValue(control.getName());
		} else {
			
			for (Control ctl : getContainer().getControls()) {
			
				if (ctl instanceof SubFormControl) {
					Object value = getFieldValue(ctl.getName()).getValue();
					
					if (value != null && ((List<FormData>) value).size() > 0) {
						FormData subFormData = ((List<FormData>) value).get((rowNumber == null ? 0 : rowNumber));
				
						if (subFormData.getContainer().getId().equals(control.getContainer().getId())) {
							fieldValue = subFormData.getFieldValue(control.getName());
							break;
						}
					}
				}
			}
		}

		return fieldValue;
	}

	public ControlValue getFieldValueByHTMLName(String htmlName) {
		ControlValue controlValue = null;
		int sequenceNumber = Control.getSequenceNumber(htmlName);
		int xpos = Control.getXpos(htmlName);
		Long containerId = Control.getContainerId(htmlName);
		if (getContainer().getId().equals(containerId)) {
			controlValue = getControlValue(this, sequenceNumber, xpos);
		} else {
			for (Control control : getContainer().getControls()) {
				if (control instanceof SubFormControl) {
					SubFormControl subFormControl = (SubFormControl) control;
					if (subFormControl.getSubContainer().getId().equals(containerId)) {
						List<FormData> datas = (List<FormData>) getFieldValue(control.getName()).getValue();
						if (subFormControl.isCardinalityOneToMany()) {
							controlValue = getControlValue(datas.get(Control.getRowNumber(htmlName) - 1),
									sequenceNumber,
									xpos);

						} else {
							FormData formData = null;
							if (datas == null || datas.isEmpty()) {
								formData = new FormData(subFormControl.getSubContainer());
								ControlValue subFormCv = new ControlValue(subFormControl, Arrays.asList(formData));
								addFieldValue(subFormCv);

							} else {
								formData = datas.get(0);
							}
							controlValue = getControlValue(formData, sequenceNumber, xpos);

						}
						break;
					}

				}
			}
		}
		return controlValue;
	}

	private ControlValue getControlValue(FormData formData, int sequenceNumber, int xpos) {
		ControlValue controlValue = null;
		for (Control control : formData.getContainer().getControls()) {

			if (control.getSequenceNumber() == sequenceNumber && control.getxPos() == xpos) {
				controlValue = formData.getFieldValue(control.getName());
				break;
			}
		}
		return controlValue;
	}

	/**
	 * Search the form tree for the specified control name separated by '.'
	 * @param controlName e.g Person.Address.city
	 * @param rowNumber 
	 * @return 
	 */
	public ControlValue getFieldValue(String controlName, Integer rowNumber) {
		
		
		FormData formData = this;
		ControlValue fieldValue = formData.getFieldValue(controlName);
		if(fieldValue != null)
		{
			return fieldValue;
		}
		String[] controlNameParts = controlName.split("\\.");
		
		if (controlNameParts.length == 1) { 
			throw new RuntimeException("Invalid control name: " + controlName);
		}
		
		FormData subFormData = null;
		for (int i = 0; i<controlNameParts.length-1;++i) {
			
			ControlValue controlValue = formData.getFieldValue(controlNameParts[i]);
			if(controlValue == null)
			{
				throw new RuntimeException("Invalid control name: " + controlName);
			}
			List<FormData> value = (List<FormData>) controlValue.getValue();
			subFormData = value.get((rowNumber == null ? 0 : rowNumber - 1));
			formData = subFormData;
			
		}
		fieldValue = formData.getFieldValue(controlNameParts[controlNameParts.length-1]);
		
		return fieldValue;

	}
}
