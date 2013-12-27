package edu.common.dynamicextensions.napi;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.MultiSelectControl;
import edu.common.dynamicextensions.domain.nui.SubFormControl;

public class FormData {
	private Container container;
	
	private Long recordId;
	
	private Map<String, Object> appData = new HashMap<String, Object>();
	
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

	public Map<String, Object> getAppData() {
		return appData;
	}

	public void setAppData(Map<String, Object> appData) {
		this.appData = appData;
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
		return fieldValues.get(name);
	}
	
	public String toJson() {
		return new Gson().toJson(getFieldNameValueMap());
	}
	
	public static FormData fromJson(String json) {
		return fromJson(json, null);
	}

	public static FormData fromJson(String json, Long containerId) {
		Type type = new TypeToken<Map<String, Object>>() {}.getType();
		Map<String, Object> valueMap = new Gson().fromJson(json, type);
		if (valueMap.get("containerId") == null && containerId == null) {
			throw new RuntimeException("Input JSON doesn't have mandatory property: containerId");
		}
				
		if (containerId == null) {
			containerId = ((Double)valueMap.get("containerId")).longValue();		
        }

		Container container = Container.getContainer(containerId);
		if (container == null) {
			throw new RuntimeException("Input JSON specifies invalid container id: " + containerId);
		}
		
		valueMap.remove("containerId");
		FormData formData = getFormData(container, valueMap);
		Map<String, Object> appData = (Map<String, Object>)valueMap.get("appData");
		formData.setAppData(appData);		
		
		if (valueMap.get("recordId") != null) {
			formData.setRecordId(((Double)valueMap.get("recordId")).longValue());
		}
		
		return formData;
	}
	
	public static FormData getFormData(Container container, Map<String, Object> valueMap) {		
		FormData formData = new FormData(container);
		Double recordId = (Double)valueMap.get("id");
		if (recordId != null) {
			formData.setRecordId(recordId.longValue());
		}
		
		for (Map.Entry<String, Object> fieldValue : valueMap.entrySet()) {
			if (fieldValue.getKey().equals("id")) {
				continue;
			}
			
			Control ctrl = container.getControl(fieldValue.getKey());
			if (ctrl instanceof SubFormControl) {
				SubFormControl sfCtrl = (SubFormControl)ctrl;
				List<Map<String, Object>> subValueMapList = (List<Map<String, Object>>)fieldValue.getValue();
				List<FormData> subFormData = new ArrayList<FormData>();
				if (subValueMapList != null) {
					for (Map<String, Object> subValueMap : subValueMapList) {
						subFormData.add(getFormData(sfCtrl.getSubContainer(), subValueMap));
					}
				} 
				
				formData.addFieldValue(new ControlValue(ctrl, subFormData));
			} else if (ctrl instanceof MultiSelectControl) {				
				List<String> values = (List<String>)fieldValue.getValue();
				formData.addFieldValue(new ControlValue(ctrl, values == null ? null : values.toArray(new String[0])));
			} else if (ctrl != null){
				formData.addFieldValue(new ControlValue(ctrl, fieldValue.getValue()));
			}
		}
		
		return formData;
	}
						
	private Map<String, Object> getFieldNameValueMap() {
		Map<String, Object> props = new HashMap<String, Object>();
		props.put("containerId", container.getId());
		props.put("id", recordId);
		
		for (ControlValue fieldValue : getFieldValues()) {
			String fieldName = fieldValue.getControl().getName();
			Object value = fieldValue.getValue();
			
			if (value instanceof FileControlValue) {
				String fileName = ((FileControlValue)value).getFileName();
				props.put(fieldName, fileName);
			} else if (value instanceof List) {
				List<FormData> formDataList = (List<FormData>)value;
				
				List<Map<String, Object>> sfData = new ArrayList<Map<String, Object>>();
				for (FormData formData : formDataList) {
					sfData.add(formData.getFieldNameValueMap());
				}
				
				props.put(fieldName, sfData);
			} else if (value != null) {
				props.put(fieldName, value);
			}			
		}
		
		return props;
	}	
}
