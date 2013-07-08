
package edu.common.dynamicextensions.domain.nui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;

public class MultiSelectListBox extends ListBox implements MultiSelectControl {

	private static final long serialVersionUID = 3003089628345200684L;

	private String tableName;

	@Override
	public String getTableName() {
		return tableName;
	}

	@Override
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		List<ColumnDef> columnDefs = new ArrayList<ColumnDef>();
		columnDefs.add(ColumnDef.get("VALUE", getDbType()));
		columnDefs.add(ColumnDef.get("RECORD_ID", "NUMBER"));

		return columnDefs;
	}

	@Override
	protected boolean isPVSelected(ControlValue controlValue, PermissibleValue pv) {
		boolean isPVSelected = false;
		
		if (controlValue.getValue() != null) {
			List<String> values = Arrays.asList((String[]) controlValue.getValue());
			
			if (values.contains(DynamicExtensionsUtility.getUnEscapedStringValue(pv.getValue()))) {
					isPVSelected = true;
			}
		} else if (getDefaultValue() != null) {
			
			if (getDefaultValue().getValue().equals(DynamicExtensionsUtility.getUnEscapedStringValue(pv.getValue()))) {
				isPVSelected = true;
			}
		}
		return isPVSelected;
	}

	@Override
	protected String getMultiselectString() {

		return "MULTIPLE";
	}

}
