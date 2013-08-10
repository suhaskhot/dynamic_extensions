
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
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (heading ? 1231 : 1237);
		result = prime * result + (note ? 1231 : 1237);
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
		
		Label other = (Label) obj;
		if (heading != other.heading || note != other.note) {
			return false;
		}
		
		return true;
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

			controlHTML
					.append("<table width='100%' style='border-collapse: collapse;border-spacing:0;'><tr><td width='100%' colspan='3' align='left'>")
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
		htmlString.append("<td colspan='100' class='formRequiredLabel_withoutBorder'>").append(innerHTML)
				.append("</td>");
		return htmlString.toString();
	}

	@Override
	public <T> T fromString(String value) {
		return null;
	}
}
