
package edu.common.dynamicextensions.summary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.nutility.FormDataUtility;

public abstract class AbstractSummaryDataManager {

	protected List<ColumnFormatter> headerList = new ArrayList<ColumnFormatter>();

	protected List<Map<String, String>> rowData = new ArrayList<Map<String, String>>();

	protected final String SR_NO = "#";

	protected final String QUESTION = "QUESTION";

	protected final String RESPONSE = "RESPONSE";

	protected final String EDIT = "Change";

	public static final String RIGHT = "right";

	public static final String ALIGN = "align";

	protected String[] excludeColumns;

	public List<ColumnFormatter> getHeaderList() {
		return headerList;
	}

	public List<Map<String, String>> getRowData() {
		return rowData;
	}

	public void setRowData(List<Map<String, String>> rowData) {
		this.rowData = rowData;
	}

	public void setHeaderList(List<ColumnFormatter> headerList) {
		this.headerList = headerList;
	}

	public String[] getExcludeColumns() {
		return excludeColumns;
	}

	public void setExcludeColumns(String[] strings) {
		this.excludeColumns = strings;
	}

	protected String formatValue(ControlValue controlValue) {
		StringBuffer value = new StringBuffer();
		if (!(controlValue.getValue() instanceof String[])) {

			return (String) controlValue.getValue();

		}
		for (String string : (String[]) controlValue.getValue()) {
			value.append(string);
			value.append(',');
		}
		if (value.length() > 0) {
			value.deleteCharAt(value.length() - 1);
		}

		return value.toString();
	}

	protected abstract void populateHeaderList();

	protected abstract void populateRow(ControlValue controlValue, Map<String, String> data);

	public void populateData(FormData formData) {
		populateHeaderList();
		filterHeader();
		int rowCounter = 1;
		FormDataUtility.evaluateSkipLogic(formData);

		for (ControlValue controlValue : formData.getFieldValues()) {

			if (!controlValue.isHidden()) {
				Map<String, String> data = new HashMap<String, String>();
				data.put(SR_NO, String.valueOf(rowCounter++));
				populateRow(controlValue, data);
				rowData.add(data);
			}
		}
	}

	private void filterHeader() {
		if (excludeColumns != null) {

			for (String string : excludeColumns) {
				headerList.remove(new ColumnFormatter(string));
			}
		}

	}

}
