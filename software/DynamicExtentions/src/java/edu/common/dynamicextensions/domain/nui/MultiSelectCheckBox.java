
package edu.common.dynamicextensions.domain.nui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.impl.FormRenderer.ContextParameter;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;

public class MultiSelectCheckBox extends SelectControl implements MultiSelectControl {

	private static final long serialVersionUID = 1605599700117979266L;

	private String tableName;
	
	private int optionsPerRow = 3;

	public int getOptionsPerRow() {
		return optionsPerRow;
	}

	public void setOptionsPerRow(int optionsPerRow) {
		this.optionsPerRow = optionsPerRow;
	}

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
	protected String render(String controlName, ControlValue controlValue,
			Map<ContextParameter, String> contextParameter) {
		StringBuilder htmlString = new StringBuilder();
		List<PermissibleValue> pvList = null;
		String disabled = "";
		//If control is defined as readonly through category CSV file,make it Disabled
		if (controlValue.isReadOnly()) {
			disabled = " disabled='true' ";
		}
		String identifier = "";
		if (getId() != null) {
			identifier = getId().toString();
		}

		pvList = getPVList(ControlsUtility.getFormattedDate(contextParameter.get(ContextParameter.ACTIVATION_DATE)),
				controlValue);;
		List<String> selectedPvs = null;
		
		if (controlValue.getValue() != null) {
			selectedPvs = Arrays.asList((String[]) controlValue.getValue());
		}

		if (pvList != null && !pvList.isEmpty()) {
			htmlString.append("<table cellspacing='3'>");
			int columnNum = 0;
			for (PermissibleValue pv : pvList) {
				if (columnNum % optionsPerRow == 0) {
					columnNum = 0;
					htmlString.append("<tr>");
				}
				
				if (isPVSelected(selectedPvs, pv)) {
					htmlString
							.append("<td valign='center'><input type='checkbox'  name='")
							.append(controlName)
							.append("' checkedValue='")
							.append(DynamicExtensionsUtility.getValueForCheckBox(true))
							.append("' uncheckedValue='")
							.append(DynamicExtensionsUtility.getValueForCheckBox(false))
							.append("'")
							.append(" value='")
							.append(pv.getValue())
							.append("' ")
							.append("id='")
							.append(pv.getValue())
							.append("'")
							.append(" checked")
							.append(disabled)
							.append(" onchange=\"")
							.append("\" ondblclick=\"changeValueForAllCheckBoxes(this);")
							.append((this.isSkipLogicSourceControl() ? "getSkipLogicControl('" + controlName + "','"
									+ identifier + "','" + getContainer().getId() + "');" : ""))
							.append("\" onclick=\"changeValueForMultiSelectCheckBox(this);")
							.append(getOnchangeServerCall(controlName))
							.append((this.isSkipLogicSourceControl() ? "getSkipLogicControl('" + controlName + "','"
									+ identifier + "','" + getContainer().getId() + "');" : ""))
							.append("\" /></td><td class='formRequiredLabel_withoutBorder'>").append("<label for=\"")
							.append(controlName).append("\">")
							.append(DynamicExtensionsUtility.getUnEscapedStringValue(pv.getValue()))
							.append("</label> </td>");
				} else {
					htmlString
							.append("<td valign='center'><input type='checkbox'  name='")
							.append(controlName)
							.append("' checkedValue='")
							.append(DynamicExtensionsUtility.getValueForCheckBox(true))
							.append("' uncheckedValue='")
							.append(DynamicExtensionsUtility.getValueForCheckBox(false))
							.append("'")
							.append(" value='")
							.append(pv.getValue())
							.append("' ")
							.append(disabled)
							.append("id='")
							.append(pv.getValue())
							.append("'")
							.append(" onchange=\"")
							.append("\" ondblclick=\"changeValueForAllCheckBoxes(this);")
							.append((this.isSkipLogicSourceControl() ? "getSkipLogicControl('" + controlName + "','"
									+ identifier + "','" + getContainer().getId() + "');" : ""))
							.append("\" onclick=\"changeValueForMultiSelectCheckBox(this);")
							.append(getOnchangeServerCall(controlName))
							.append((this.isSkipLogicSourceControl() ? "getSkipLogicControl('" + controlName + "','"
									+ identifier + "','" + getContainer().getId() + "');" : ""))
							.append("\" /></td><td class='formRequiredLabel_withoutBorder' >").append("<label for=\"")
							.append(controlName).append("\">").append(pv.getValue()).append("</label> </td>");
				}
				if (columnNum % optionsPerRow == optionsPerRow - 1) {
					htmlString.append("</tr>");
				}
				columnNum++;

			}
			if (columnNum % optionsPerRow < optionsPerRow - 1) {
				htmlString.append("</tr>");
			}

		}
		htmlString.append("</table>");

		return htmlString.toString();
	}

	private boolean isPVSelected(List<String> selectedPvs, PermissibleValue pv) {
		boolean isPVSelected = false;
		
		if (selectedPvs != null) {
			
			if (selectedPvs.contains(DynamicExtensionsUtility.getUnEscapedStringValue(pv.getValue()))) {
					isPVSelected = true;
				}

		} else if (getDefaultValue() != null) {
			
			if (getDefaultValue().getValue().equals(DynamicExtensionsUtility.getUnEscapedStringValue(pv.getValue()))) {
				isPVSelected = true;
			}
		}
		return isPVSelected;
	}

}
