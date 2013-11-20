
package edu.common.dynamicextensions.domain.nui;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.impl.FormRenderer.ContextParameter;
import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.ui.util.Constants;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;

public class TextArea extends TextField {

	private static final long serialVersionUID = 2084367923183300598L;

	private int noOfRows;

	public int getNoOfRows() {
		return noOfRows;
	}

	public void setNoOfRows(int noOfRows) {
		this.noOfRows = noOfRows;
	}

	public int getMinLength() {
		int minLength = 0;		
		String min = getValidationRuleParam("textLength", "min");
		if (min != null && !min.trim().isEmpty()) {
			minLength = Integer.parseInt(min);
		}
		
		return minLength;
	}
		
	public void setMinLength(int minChars) {
		addValidationRule("textLength", Collections.singletonMap("min", String.valueOf(minChars)));
	}

	public int getMaxLength() {
		int maxLength = 0;		
		String max = getValidationRuleParam("textLength", "max");
		if (max != null && !max.trim().isEmpty()) {
			maxLength = Integer.parseInt(max);
		}
		
		return maxLength;		
	}
	
	public void setMaxLength(int maxChars) {
		addValidationRule("textLength", Collections.singletonMap("max", String.valueOf(maxChars)));
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		return Collections.singletonList(ColumnDef.get(getDbColumnName(), "VARCHAR(4000)"));
	}

	@Override
	public DataType getDataType() {
		return DataType.STRING;
	}
	
	@Override
	public String fromString(String value) {
		return value;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + noOfRows;
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
		
		TextArea other = (TextArea) obj;
		if (noOfRows != other.noOfRows) {
			return false;
		}
		
		return true;
	}

	@Override
	protected String render(String conrolName, ControlValue controlValue, Map<ContextParameter, String> contextParameter) {
		StringBuilder htmlString = new StringBuilder(99);
		String value = (controlValue.getValue() == null ? DynamicExtensionsUtility
				.replaceHTMLSpecialCharacters(getDefaultValue()) : (String) controlValue.getValue());


		htmlString.append("<textarea ").append(" name='").append(conrolName).append("' ").append("id='")
				.append(conrolName)
				.append("' onchange=\"")
				.append(getOnchangeServerCall(conrolName))
				.append(";")
				.append((this.isCalculatedSourceControl() ? "updateHTML();\" "
						: "\" "));

		//If control is defined as read only through category CSV file,make it Disabled

		if (controlValue.isReadOnly()) {
			htmlString.append(ProcessorConstants.DISABLED);
		}

		if (getNoOfColumns() > 0) {
			htmlString.append("cols='").append(getNoOfColumns()).append("' ");
		} else {
			htmlString.append("cols='").append(Constants.DEFAULT_COLUMN_SIZE).append("' ");
		}

		if (noOfRows > 0) {
			htmlString.append("rows='").append(noOfRows).append("' ");
		} else {
			htmlString.append("rows='").append(Constants.DEFAULT_ROW_SIZE).append("' ");
		}

		htmlString.append(" wrap='virtual'>");

		if (value == null || (value.length() == 0)) {
			htmlString.append("</textarea>");
		} else {
			htmlString.append(value).append("</textarea>");
		}

		return htmlString.toString();
	}
}
