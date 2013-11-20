/**
 * 
 */

package edu.common.dynamicextensions.domain.nui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FileControlValue;
import edu.common.dynamicextensions.napi.impl.FormRenderer.ContextParameter;
import edu.wustl.common.util.global.ApplicationProperties;

public class FileUploadControl extends Control {

	private static final long serialVersionUID = 7296131725212728756L;

	@Override
	public List<ColumnDef> getColumnDefs() {
		List<ColumnDef> columns = new ArrayList<ColumnDef>();
		columns.add(ColumnDef.get(getDbColumnName() + "_NAME", "VARCHAR(4000)"));
		columns.add(ColumnDef.get(getDbColumnName() + "_TYPE", "VARCHAR(4000)"));
		columns.add(ColumnDef.get(getDbColumnName() + "_CONTENT", "BLOB"));
		return columns;
	}

	@Override
	public <T> T fromString(String value) {
		return null;
	}

	@Override
	public DataType getDataType() {
		return null;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		return super.equals(other);
	}
	
	@Override
	protected String render(String controlname, ControlValue controlValue,
			Map<ContextParameter, String> contextParameter) {
		StringBuffer htmlString = new StringBuffer(210);


		ApplicationProperties.initBundle("ApplicationResources");
		htmlString.append("<span id='").append(controlname).append("_button'>");

		if (controlValue.getValue() == null) {
			htmlString
					.append("<input onchange='this.title=this.value;isDataChanged();' type=\"file\" size='47' width='47' name='")
					.append(controlname).append("' id=\"").append(controlname).append("\"' ");

			if (controlValue.isReadOnly()) {
				htmlString.append("onclick='return false'");
			}
			htmlString.append("/>");
		} else {
			generateHTMLForFileControl(htmlString, controlname, controlValue,
					contextParameter.get(ContextParameter.CONTEXT_PATH));
		}
		htmlString.append("</span>");
		
		return htmlString.toString();
	}

	/**
	 *  This method generates HTML for file attribute
	* @param htmlString
	* @param controlname
	 * @param fileName 
	*/
	private void generateHTMLForFileControl(StringBuffer htmlString, String controlname, ControlValue controlValue,
			String contextPath) {
		FileControlValue fileControlValue = (FileControlValue) controlValue.getValue();
		htmlString.append("<input type='text' disabled name='").append(controlname).append("'_1 id='")
				.append(controlname).append("_1' value='").append(fileControlValue.getFileName())
				.append("'/>&nbsp;&nbsp;");
		if (!controlValue.isReadOnly()) {
			generateHTMLBasedOnRecordId(fileControlValue.getRecordId(), htmlString, controlname, contextPath);

			htmlString
					.append("<img src='images/de/deleteIcon.jpg' style='cursor:pointer' title='Delete File' onClick='updateFileControl(\"")
					.append(controlname).append("\");' /><input type='hidden' id='").append(controlname)
					.append("_hidden' name='").append(controlname).append("_hidden' value='hidden'/>");
		}
	}

	/**
	* @param recordId the record id
	* @param htmlString the html string
	*/
	private void generateHTMLBasedOnRecordId(Long recordId, StringBuffer htmlString, String controlName,
			String contextPath) {
		/** This is the case when file attribute is present in multiple levels in a form
		 *  In this case when file is uploaded for upper level and then sub level is traversed
		 *  then upper level form opens in edit mode. Although the control has value, since
		 *  the form is not yet submitted, recordId is null. Hence Download option is not to be
		 *  provided.
		 */
		if (recordId == null) {
			htmlString.append("<img src='images/uIEnhancementImages/error-green.gif' title='File uploaded'/>");
		} else {
			htmlString.append("<A onclick='appendRecordId(this);' style='text-decoration: none;' href='")
					.append(contextPath).append("/")
					.append("DownloadFileAction.de?controlName=").append(controlName).append("&recordIdentifier=")
					.append(recordId)
					.append("'><img src='images/de/download.bmp' title='Download File'/> </A>&nbsp;&nbsp;");
		}

	}
}
