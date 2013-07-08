/**
 * 
 */

package edu.common.dynamicextensions.domain.nui;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domain.DynamicExtensionBaseDomainObject;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.impl.FormRenderer.ContextParameter;

public class Page extends DynamicExtensionBaseDomainObject {

	private static final String PAGE_TITLE = "<tr><th colspan='10'><div class='sm-page-title'>%s</div></th></tr>";

	private static final String SM_PAGE_TABLE = "<table class='sm-page-table'>%s%s</table>";

	private static final String PAGE_CONTENTS_DIV = "<div class='sm-page-contents'>%s</div>";

	private static final long serialVersionUID = 2920126612122931143L;

	private final Map<String, Control> controlsMap = new LinkedHashMap<String, Control>();

	private String caption;

	private String name;

	private SurveyContainer container;


	public Page(String name, String caption) {
		this.name = name;
		this.caption = caption;
	}

	public Page() {
	}

	@Override
	public Long getId() {
		return id;
	}

	public Collection<Control> getControls() {
		return controlsMap.values();
	}

	public void setControls(Set<Control> controls) {
		controls.clear();

		for (Control control : controls) {
			controlsMap.put(control.getName(), control);
		}
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Control getControl(String name) {
		return controlsMap.get(name);
	}

	public SurveyContainer getContainer() {
		return container;
	}

	public void setContainer(SurveyContainer container) {
		this.container = container;
	}

	public void addControl(Control control) {
		if (controlsMap.containsKey(control.getName())) {
			throw new RuntimeException("Control with same name already exists in the page: " + control.getName());
		}
		container.addControl(control);
		controlsMap.put(control.getName(), control);
	}


	public void deleteControl(String controlName) {
		Control existingControl = controlsMap.remove(controlName);

		if (existingControl == null) {
			throw new RuntimeException("Control with name doesn't exist: " + controlName);
		}
		container.deleteControl(controlName);
		controlsMap.remove(controlName);
	}

	public void editControl(String controlName, Control ctrl) {
		container.editControl(controlName, ctrl);
		controlsMap.remove(controlName);
		controlsMap.put(ctrl.getName(), ctrl);
	}


	public void editPage(Page page) {

		this.setCaption(page.getCaption());
		for (Control control : page.getControls()) {
			controlsMap.put(control.getName(), container.getControl(control.getName()));
		}

		for (Control control : controlsMap.values()) {
			if (page.getControl(control.getName()) == null) {
				controlsMap.remove(control.getName());
			}
		}
	}

	public void moveTo(String ctrlName, Page tgtPage) {
		Control control = getControl(ctrlName);
		if (control == null) {
			throw new RuntimeException("Control with name doesn't exist: " + ctrlName);
		}
		moveTo(control, tgtPage);

	}

	public void moveTo(Control control, Page tgtPage) {
		if (getControl(control.getName()) == null) {
			throw new RuntimeException("Control with name doesn't exist: " + control.getName());
		}
		controlsMap.remove(control.getName());
		tgtPage.controlsMap.put(control.getName(), control);
	}

	public String render(FormData formData, Map<ContextParameter, String> contextParamater) {

		String pageTitle = String.format(PAGE_TITLE, caption);

		StringBuilder html = new StringBuilder();

		for (Control control : getControls()) {
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
