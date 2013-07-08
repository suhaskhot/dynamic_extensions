
package edu.common.dynamicextensions.domain.nui;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.impl.FormRenderer.ContextParameter;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;

public class Label extends Control {

	private static final long serialVersionUID = 2176449161292723498L;

	private boolean note;

	private boolean heading;

	public boolean isNote() {
		return note;
	}

	public void setNote(boolean note) {
		this.note = note;
	}

	public boolean isHeading() {
		return heading;
	}

	public void setHeading(boolean heading) {
		this.heading = heading;
	}

	@Override
	public List<ColumnDef> getColumnDefs() {
		return Collections.emptyList();
	}
	
	@Override
	public DataType getDataType() {
		return DataType.STRING;
	}
	

	@Override
	protected String render(String controlName, ControlValue controlValue,
			Map<ContextParameter, String> contextParameter) {
		StringBuilder controlHTML = new StringBuilder();

		if (heading) {
			controlHTML.append("<table width='100%'><tr><td width='100%' colspan='3' align='left'>")
					.append("<div style='width:100%' class='td_color_6e81a6'>").append(getCaption()).append("</div>")
					.append("</td></tr>");
		} else if (note) {

			controlHTML.append("<table width='100%'><tr><td width='100%' colspan='3' align='left'>")
					.append("<div style='width:100%' class='notes'>")
					.append(DynamicExtensionsUtility.replaceHTMLSpecialCharacters(getCaption())).append("</div>")
					.append("</td></tr>");
		} else {
			controlHTML.append("<div style='float:left'><b>" + getCaption() + "</b></div>");

		}

		return controlHTML.toString();
	}

	@Override
	protected String attachLabel(String innerHTML) {
		StringBuilder htmlString = new StringBuilder();
		htmlString.append("<td colspan='100'>").append(innerHTML).append("</td>");
		return htmlString.toString();
	}

	@Override
	public <T> T fromString(String value) {
		return null;
	}
}
