
package edu.common.dynamicextensions.napi.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import edu.common.dynamicextensions.domain.nui.CheckBox;
import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.Label;
import edu.common.dynamicextensions.domain.nui.MultiSelectControl;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;

public class ReadOnlyFormRenderer {


	private static final String FORM_TABLE = "<table border='0'  summary='' cellpadding='3' cellspacing='3' align='center' width='100%%'>%s</table>";
	private static final String FORM_HEADER = "<tr ><td align='left' colspan='100' "
			+ "class='td_color_6e81a6'>%s</td></tr>";
	private static final String FORM_ROW_HTML = "<tr valign='center' ><td width='2%%' valign='center'"
			+ " align='right' class='formRequiredNotice_withoutBorder'><span class='font_red'>%s </span> </td>"
			+ " <td width='40%%' title='Enter user name' class='formRequiredLabel_withoutBorder'>"
			+ "<div class='control_caption'>%s</div>" + "</td><td><table><tr>%s</tr></table></td></tr>";
	private static final String FORM_ROW_HTML_CONTROLS = "<td class='formRequiredLabel_withoutBorder'>%s</td>%s";
	private static final String CONTROLS_ON_SAME_LINE_HTML = "<td class='formRequiredLabel_withoutBorder'><div class='control_caption'>%s</div></td><td class='formRequiredLabel_withoutBorder'>%s</td>";

	private static final String SUBFORM_GRID_CELL = "<td valign='middle' nowrap='true'>%s</td>";
	private static final String SUBFORM_HEADER_ROW = "<tr class='formLabel_withoutBorder' width='100%%'>%s</tr>";
	private static final String SUBFORM_HEADER_COLUMN = "<th><span class='font_bl_nor'>%s</span></th>";
	private static final String SUBFORM_GRID_ROW = "<tr class='%s' width='100%%'>%s</tr>";
	private static final String SUBFORM_HTML = "<tr width='100%%'><td class='formFieldContainer_withoutBorder' align='center' colspan='100'>"
			+ "<table border='0'  summary='' cellpadding='3' cellspacing='3' align='center' width='100%%'>%s</table></td></tr>";

	private static final String NOTE_HTML = "<tr><td width='100%%'  align='left' colspan='100'><div class='notes' style='width:100%%'>%s</div></td></tr>";
	private static final String HEADING_HTML = "<tr><td width='100%%' align='left' colspan='100'><div class='td_color_6e81a6' style='width:100%%'>%s</div></td></tr>";
	private static final String CHECKBOX_HTML = "<input type='checkbox' %s disabled/>";
	private static final String BR = "<br/>";

	public String render(Long containerId, Long recordId) {
		FormData formData = new FormDataManagerImpl().getFormData(containerId, recordId);
		return renderContainer(formData);
	}

	private String renderContainer(FormData formData) {
		Container container = formData.getContainer();
		StringBuilder containerHtml = new StringBuilder();
		containerHtml.append(String.format(FORM_HEADER, container.getCaption()));
		Map<Integer, List<Control>> rowSeperatedControl = divideControlsInRows(container);

		for (Map.Entry<Integer, List<Control>> controlRow : rowSeperatedControl.entrySet()) {
			String formRowHTML = StringUtils.EMPTY;
			formRowHTML = renderRow(formData, controlRow);
			containerHtml.append(formRowHTML);
		}
		return String.format(FORM_TABLE, containerHtml.toString());
	}

	private String renderRow(FormData formData, Map.Entry<Integer, List<Control>> controlRow) {
		List<Control> controls = controlRow.getValue();
		String formRowHTML = StringUtils.EMPTY;
		Control firstControl = controls.get(0);

		if (firstControl instanceof SubFormControl) {
			formRowHTML = renderSubform(formData, (SubFormControl) firstControl);
		} else if (firstControl instanceof Label) {
			formRowHTML = renderLabel((Label) firstControl);
		} else {
			String sameLineControlsHtml = StringUtils.EMPTY;
			//For handing single line controls. 
			sameLineControlsHtml = renderHTMLForControlsInSameLine(controls, formData);
			sameLineControlsHtml = String.format(FORM_ROW_HTML_CONTROLS,
					renderControl(formData.getFieldValue(firstControl.getName())), sameLineControlsHtml);
			formRowHTML = String.format(FORM_ROW_HTML, renderMandatorySymbol(firstControl), firstControl.getCaption(),
					sameLineControlsHtml);
		}
		return formRowHTML;
	}

	private String renderControl(ControlValue controlValue) {

		if (controlValue.getValue() == null) {
			return StringUtils.EMPTY;
		}

		Control control = controlValue.getControl();
		StringBuilder controlHTML = new StringBuilder();

		if (control instanceof CheckBox) {
			controlHTML.append(String.format(CHECKBOX_HTML,
					DynamicExtensionsUtility.getCheckboxSelectionValue((String) controlValue.getValue())));
		} else if (control instanceof MultiSelectControl) {
			for (String pv : (String[]) controlValue.getValue()) {
				controlHTML.append(pv).append(BR);
			}
		} else {
			controlHTML.append(controlValue.getValue());
		}

		return controlHTML.toString();
	}

	private String renderSubform(FormData formData, SubFormControl subFormControl) {
		List<FormData> formDataList = (List<FormData>) formData.getFieldValue(subFormControl.getName()).getValue();

		if (subFormControl.isCardinalityOneToMany()) {
			return renderContainerAsGrid(subFormControl.getSubContainer(), formDataList);

		} else {
			FormData subformData = formDataList.get(0);
			return renderContainer(subformData);
		}
	}

	private String renderLabel(Label label) {
		String formRowHTML;

		if (label.isNote()) {
			formRowHTML = String.format(NOTE_HTML, label.getCaption());
		} else {
			formRowHTML = String.format(HEADING_HTML, label.getCaption());
		}
		return formRowHTML;
	}

	private String renderMandatorySymbol(Control firstControl) {
		return (firstControl.isMandatory() ? "*" : StringUtils.EMPTY);
	}

	private String renderHTMLForControlsInSameLine(List<Control> controls, FormData formData) {
		StringBuilder rowHtml = new StringBuilder();

		for (int i = 1; i < controls.size(); i++) {
			Control control = controls.get(i);
			ControlValue controlValue = formData.getFieldValue(control.getName());
			String caption = (control.showLabel() ? control.getCaption() : StringUtils.EMPTY);
			rowHtml.append(String.format(CONTROLS_ON_SAME_LINE_HTML, caption, renderControl(controlValue)));
		}

		return rowHtml.toString();
	}

	private Map<Integer, List<Control>> divideControlsInRows(Container container) {
		Map<Integer, List<Control>> controlsPerRowMap = new LinkedHashMap<Integer, List<Control>>();

		List<Control> allControls = container.getAllControls();
		Collections.sort(allControls);
		for (Control control : allControls) {
			List<Control> row = controlsPerRowMap.get(control.getSequenceNumber());

			if (row == null) {
				row = new ArrayList<Control>();
				controlsPerRowMap.put(control.getSequenceNumber(), row);
			}
			row.add(control);
		}
		return controlsPerRowMap;
	}

	private String renderContainerAsGrid(Container subContainer, List<FormData> formDataList) {
		List<Control> labels = new ArrayList<Control>();
		List<Control> dataControls = new ArrayList<Control>();
		StringBuilder containerHtml = new StringBuilder();
		containerHtml.append(String.format(FORM_HEADER, subContainer.getCaption()));

		List<Control> allControls = subContainer.getAllControls();
		Collections.sort(allControls);
		for (Control control : allControls) {

			if (control instanceof Label) {
				labels.add(control);
			} else {
				dataControls.add(control);
			}
		}

		for (Control label : labels) {
			containerHtml.append(renderLabel((Label) label));
		}
		containerHtml.append(renderSubformTableHeader(dataControls));

		String rowClass;
		int rowIndex = 0;

		for (FormData formData : formDataList) {
			rowIndex++;
			rowClass = rowIndex % 2 == 0 ? "td_color_f0f2f6" : "formField_withoutBorder";
			StringBuilder subformRowHtml = new StringBuilder();

			for (Control control : dataControls) {
				subformRowHtml.append(String.format(SUBFORM_GRID_CELL,
						renderControl(formData.getFieldValue(control.getName()))));
			}
			containerHtml.append(String.format(SUBFORM_GRID_ROW, rowClass, subformRowHtml));
		}
		return String.format(SUBFORM_HTML, containerHtml.toString());
	}

	private String renderSubformTableHeader(List<Control> dataControls) {
		StringBuilder subformHeaderRow = new StringBuilder();

		for (Control control : dataControls) {
			subformHeaderRow.append(String.format(SUBFORM_HEADER_COLUMN, control.getCaption()));
		}
		return String.format(SUBFORM_HEADER_ROW, subformHeaderRow.toString());
	}

}
