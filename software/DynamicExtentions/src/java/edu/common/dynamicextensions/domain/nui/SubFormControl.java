
package edu.common.dynamicextensions.domain.nui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.impl.FormRenderer.ContextParameter;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.wustl.common.util.global.ApplicationProperties;

public class SubFormControl extends Control {

	private static final String EAV_ATT_VIEW_DETAILS = "eav.att.ViewDetails";

	private static final String EAV_ATT_EDIT_DETAILS = "eav.att.EditDetails";

	private static final String EAV_ATT_NO_DATA_TO_VIEW = "eav.att.NoDataToView";

	private static final String EAV_ATT_ENTER_DETAILS = "eav.att.EnterDetails";

	private static final long serialVersionUID = -3383871023946721209L;

	private Container subContainer;

	private int noOfEntries;

	private boolean showAddMoreLink;

	private boolean pasteButtonEnabled;

	private String tableName;
	
	private String parentKeyColumn  = "IDENTIFIER";
	
	private String foreignKeyColumn = "PARENT_RECORD_ID";

	public Container getSubContainer() {
		return subContainer;
	}

	public void setSubContainer(Container subContainer) {
		this.subContainer = subContainer;
	}

	public boolean showAddMoreLink() {
		return showAddMoreLink;
	}

	public void setShowAddMoreLink(boolean showAddMoreLink) {
		this.showAddMoreLink = showAddMoreLink;
	}

	public int getNoOfEntries() {
		return noOfEntries;
	}

	public void setNoOfEntries(int noOfEntries) {
		this.noOfEntries = noOfEntries;
	}

	public boolean isPasteButtonEnabled() {
		return pasteButtonEnabled;
	}

	public void setPasteButtonEnabled(boolean pasteButtonEnabled) {
		this.pasteButtonEnabled = pasteButtonEnabled;
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		List<ColumnDef> columnDefs = new ArrayList<ColumnDef>();
		columnDefs.add(ColumnDef.get("RECORD_ID", "NUMBER"));
		columnDefs.add(ColumnDef.get("SUB_FORM_RECORD_ID", "NUMBER"));
		return columnDefs;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getParentKey() {
		return parentKeyColumn;
	}

	public void setParentKey(String parentKeyColumn) {
		if (parentKeyColumn == null) {
			parentKeyColumn  = "IDENTIFIER";
		}
		
		this.parentKeyColumn = parentKeyColumn;
	}
	
	public String getForeignKey() {
		return foreignKeyColumn;
	}

	public void setForeignKey(String foreignKeyColumn) {
		if (foreignKeyColumn == null) {
			foreignKeyColumn = "PARENT_RECORD_ID";
		}
		
		this.foreignKeyColumn = foreignKeyColumn;
	}

	/**
	 * @param rowNumber
	 * @param controlValue
	 * @param contextParameter
	 * @param generateSubformHTML flag used to decide whether to generate complete sub form HTML or hyper link
	 * @return
	 */
	protected String render(String controlName, ControlValue controlValue,
			Map<ContextParameter, String> contextParameter, boolean generateSubformHTML) {
		String subContainerHTML = "";
		if (generateSubformHTML && !controlValue.isHidden()) {

			if (isCardinalityOneToMany()) {
				List<FormData> valueMapList = (List<FormData>) controlValue.getValue();
				subContainerHTML = subContainer.renderAsGrid(valueMapList, contextParameter, pasteButtonEnabled,
						showLabel());
			} else {
				FormData formData = null;
				if (controlValue.getValue() != null && ((List<FormData>) controlValue.getValue()).size() > 0) {
					formData = ((List<FormData>) controlValue.getValue()).get(0);
				} else {
					formData = new FormData(subContainer);
				}
				subContainerHTML = subContainer.generateContainerHTML(getCaption(), showLabel(), formData,
						contextParameter, false);
			}

		} else {
			String previousLink = generateLink(controlValue, contextParameter.get(ContextParameter.MODE));
			subContainerHTML = getControlHTMLAsARow(this, previousLink);

		}
		return subContainerHTML;
	}

	@Override
	public String renderInGrid(Integer rowNumber, ControlValue controlValue,
			Map<ContextParameter, String> contextParameter) {
		StringBuilder stringBuilder = new StringBuilder(208);
		String detailsString = "Details";
		stringBuilder
				.append("<img src='images/de/ic_det.gif' alt='Details' width='12' height='12' hspace='3' border='0' align='absmiddle'><a href='#' style='cursor:hand' class='set1' onclick='showChildContainerInsertDataPage(")
				.append(controlValue.getControl().getContainer().getId()).append(",this)'>").append(detailsString)
				.append("</a>");
		return stringBuilder.toString();
	}

	@Override
	protected String render(String controlName, ControlValue controlValue,
			Map<ContextParameter, String> contextParameter) {

		return render(controlName, controlValue, contextParameter, false);

	}

	/**
	 * This method returns true if the cardinality of the Containment Association is One to Many.
	 * @return true if cardinality is One to Many, false otherwise.
	 */
	public boolean isCardinalityOneToMany() {
		boolean isOneToMany = false;

		if (noOfEntries == -1) {
			isOneToMany = true;
		}
		return isOneToMany;
	}

	/* (non-Javadoc)
	 * @see edu.common.dynamicextensions.domain.nui.Control#attachLabel(java.lang.String, int)
	 */
	protected String attachLabel(String innerHTML, int rowNumber) {
		return innerHTML;
	}
	
	@Override
	public DataType getDataType() {
		return null;
	}
	
	@Override
	public <T> T fromString(String value) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Gets the control html as a row.
	 *
	 * @param control the control
	 * @param htmlString the html string
	 *
	 * @return the control html as a row
	 */
	public String getControlHTMLAsARow(Control control, String htmlString) {

		StringBuilder controlHtmlAsARow = new StringBuilder(166);
		controlHtmlAsARow.append("<tr><td class='formRequiredNotice_withoutBorder' width='2%'>");
		if (isMandatory()) {
			controlHtmlAsARow.append(getContainer().getRequiredFieldIndicatior());
			controlHtmlAsARow.append("&nbsp;</td><td class='formRequiredLabel_withoutBorder'>");
		} else {
			controlHtmlAsARow.append("&nbsp;</td><td class='formRequiredLabel_withoutBorder'>");
		}

		controlHtmlAsARow.append(control.getCaption());
		controlHtmlAsARow.append("</td><td class='formField_withoutBorder'>");
		controlHtmlAsARow.append(htmlString);
		controlHtmlAsARow.append("</td></tr>");

		return controlHtmlAsARow.toString();
	}

	public String generateLink(ControlValue controlValue, String mode) {

		String details = null;
		if (isControlValueEmpty(controlValue)) {
			if (mode.equals(WebUIManagerConstants.EDIT_MODE)) {
				details = ApplicationProperties.getValue(EAV_ATT_ENTER_DETAILS);
			} else if (mode.equals(WebUIManagerConstants.VIEW_MODE)) {
				details = ApplicationProperties.getValue(EAV_ATT_NO_DATA_TO_VIEW);
			}

		} else {
			if (mode.equals(WebUIManagerConstants.EDIT_MODE)) {
				details = ApplicationProperties.getValue(EAV_ATT_EDIT_DETAILS);
			} else if (mode.equals(WebUIManagerConstants.VIEW_MODE)) {
				details = ApplicationProperties.getValue(EAV_ATT_VIEW_DETAILS);
			}
		}

		StringBuilder StringBuilder = new StringBuilder(208)
				.append("<img src='images/de/ic_det.gif' alt='Details' width='12' height='12' hspace='3' border='0' align='absmiddle'><a href='#' style='cursor:hand' class='set1' onclick='showChildContainerInsertDataPage(")
				.append(getContainer().getId()).append(",this)'>").append(details).append("</a>");

		return StringBuilder.toString();
	}

	private boolean isControlValueEmpty(ControlValue controlValue) {
		boolean isEmpty = true;
		if (controlValue != null && controlValue.getValue() != null) {
			if (controlValue.getValue() instanceof List) {
				if (!((List) controlValue.getValue()).isEmpty()) {
					isEmpty = false;
				}

			} else if (!((FormData) controlValue.getValue()).getFieldValues().isEmpty()) {
				isEmpty = false;
			}
		}
		return isEmpty;
	}
}
