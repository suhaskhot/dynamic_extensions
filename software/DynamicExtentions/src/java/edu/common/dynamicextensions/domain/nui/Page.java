package edu.common.dynamicextensions.domain.nui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.impl.FormRenderer.ContextParameter;

public class Page extends DynamicExtensionBaseDomainObject {

	//private static final String PAGE_TITLE = "<tr><th colspan='10'><div class='sm-page-title'>%s</div></th></tr>";

	private static final String SM_PAGE_TABLE = "<table class='sm-page-table'>%s%s</table>";

	private static final String PAGE_CONTENTS_DIV = "<div class='sm-page-contents'>%s</div>";

	private static final long serialVersionUID = 2920126612122931143L;

	private List<Control> controls = new ArrayList<Control>();

	public Page() {
	}

	@Override
	public Long getId() {
		return id;
	}
	
	public boolean isEmptyPage() {
		return controls.isEmpty();
	}

	public List<Control> getControls() {
		return controls;
	}

	public void setControls(List<Control> controls) {
		this.controls = controls;
	}
	
	public void addControl(Control ctrl) {
		this.controls.add(ctrl);
	}

	public String render(FormData formData, Map<ContextParameter, String> contextParamater) {
		String pageTitle = StringUtils.EMPTY;
		StringBuilder html = new StringBuilder();

		List<Control> controls = new ArrayList<Control>(getControls());
		Collections.sort(controls);

		for (Control control : controls) {
			ControlValue fieldValue = formData.getFieldValue(control.getName());
			html.append(getControlHTML(control, fieldValue, contextParamater));
		}
		
		String htmlWrapper = String.format(SM_PAGE_TABLE, pageTitle, html.toString());
		return String.format(PAGE_CONTENTS_DIV, htmlWrapper);
	}

	private String getControlHTML(Control control, ControlValue controlValue,
			Map<ContextParameter, String> contextParameter) {

		StringBuilder controlHTML = new StringBuilder();
		String controlName = control.getControlName();

		controlHTML.append("<tr");

		if (controlValue.isHidden()) {
			controlHTML.append(" style='display:none'");
		} else {
			controlHTML.append(" style='display:row'");
		}
		controlHTML.append('>');

		if (control.isDynamic()) {
			controlHTML.append("<input type='hidden' name='dynamicControl' id='dynamicControl' value = '")
					.append(controlName).append("_tbody' />");
		}

		String htmlWrapper = "<tr><td height='7'></td></tr><tbody id='%s_tbody'> %s%s</table></td></tr></tbody>";

		if (control.getxPos() > 1) {
			htmlWrapper = "<tr><td height='7'></td></tr>%s%s</td></tr>";
		}
		return String.format(htmlWrapper, controlName, controlHTML.toString(),
				control.render(controlValue, contextParameter));
	}
}
