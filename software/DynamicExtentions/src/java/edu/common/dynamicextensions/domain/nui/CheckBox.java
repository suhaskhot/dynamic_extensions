
package edu.common.dynamicextensions.domain.nui;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.impl.FormRenderer.ContextParameter;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;

public class CheckBox extends Control {

	private static final long serialVersionUID = 6546210572740818515L;

	private boolean defaultValueChecked;

	public boolean isDefaultValueChecked() {
		return defaultValueChecked;
	}

	public void setDefaultValueChecked(boolean defaultValueChecked) {
		this.defaultValueChecked = defaultValueChecked;
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		return Collections.singletonList(ColumnDef.get(getDbColumnName(), "NUMBER"));
	}

	@Override
	public DataType getDataType() {
		return DataType.BOOLEAN;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Boolean fromString(String value) {
		return (value != null) && (value.equals("1") || value.equalsIgnoreCase("true"));
	}
	
	@Override
	public String toString(Object value) {
		return (value == null || value.toString().equals("false") || value.toString().equals("0")) ? "0" : "1";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (defaultValueChecked ? 1231 : 1237);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (!super.equals(obj)) {
			return false;
		}
		
		CheckBox other = (CheckBox) obj;
		if (defaultValueChecked != other.defaultValueChecked) {
			return false;
		}
		
		return true;
	}

	@Override
	protected String render(String controlName, ControlValue controlValue,
			Map<ContextParameter, String> contextParameter) {
		final String checked = (controlValue.getValue() != null ? (String) controlValue.getValue() : String
				.valueOf(defaultValueChecked));

		final StringBuilder htmlString = new StringBuilder();
		String disabled = "";
		//If control is defined as readonly through category CSV file,make it Disabled
		if (controlValue.isReadOnly()) {
			disabled = " disabled='true' ";
		}

		final String htmlComponentName = controlName;
		if (checked != null
				&& (checked.equalsIgnoreCase("true") || checked.equals("1") || checked.equals("y") || checked
						.equals("yes"))) {
			htmlString.append("<input type='checkbox' name='").append(htmlComponentName).append("' checkedValue='")
					.append(DynamicExtensionsUtility.getValueForCheckBox(true)).append("' uncheckedValue='")
					.append(DynamicExtensionsUtility.getValueForCheckBox(false)).append("' value='")
					.append(DynamicExtensionsUtility.getValueForCheckBox(true)).append("' id='")
					.append(htmlComponentName).append("' checked").append(disabled).append(" onchange=\"")
					.append("\" onclick=\"changeValueForCheckBox(this);").append(getOnchangeServerCall(controlName))
					.append("\">");
		} else {
			htmlString.append("<input type='checkbox' name='").append(htmlComponentName).append("' checkedValue='")
					.append(DynamicExtensionsUtility.getValueForCheckBox(true)).append("' uncheckedValue='")
					.append(DynamicExtensionsUtility.getValueForCheckBox(false)).append("' value='")
					.append(DynamicExtensionsUtility.getValueForCheckBox(false)).append("' id='").append(disabled)
					.append(htmlComponentName).append("' onchange=\"")
					.append("\" onclick=\"changeValueForCheckBox(this);").append(getOnchangeServerCall(controlName))
					.append("\">");

		}
		return htmlString.toString();
	}
}
