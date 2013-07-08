
package edu.common.dynamicextensions.summary;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import edu.common.dynamicextensions.domain.nui.Control;
import edu.common.dynamicextensions.domain.nui.Page;
import edu.common.dynamicextensions.domain.nui.SurveyContainer;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.common.dynamicextensions.util.global.DEConstants;
import edu.common.dynamicextensions.util.global.Variables;

public class SurveySummaryDataManager extends AbstractSummaryDataManager {

	private final Map<Control, Long> controlPageIdMap = new HashMap<Control, Long>();

	private final PageContext pageContext;

	private final FormData formData;

	public SurveySummaryDataManager(FormData formData, PageContext pageContext) {
		this.pageContext = pageContext;
		this.formData = formData;
		SurveyContainer surveyContainer = (SurveyContainer) formData.getContainer();

		for (Page page : surveyContainer.getPages()) {

			for (Control control : page.getControls()) {
				controlPageIdMap.put(control, page.getId());
			}
		}
	}

	@Override
	protected void populateHeaderList() {
		headerList.add(new ColumnFormatter(SR_NO, ""));
		headerList.add(new ColumnFormatter(QUESTION, "Question"));
		headerList.add(new ColumnFormatter(RESPONSE, "Response"));
		headerList.add(new ColumnFormatter(EDIT, ""));
	}

	@Override
	protected void populateRow(ControlValue controlValue, Map<String, String> data) {
		data.put(QUESTION, controlValue.getControl().getCaption());
		data.put(RESPONSE, formatValue(controlValue));
		data.put(EDIT, getURL(controlValue.getControl()));

	}

	private String getURL(Control control) {
		StringBuffer controlUrl = new StringBuffer();

		controlUrl.append("<a href='").append(((HttpServletRequest) pageContext.getRequest()).getContextPath())
				.append(Variables.resourceMapping.get(WebUIManagerConstants.SURVEY_MODE_JSP)).append("?")
				.append(((HttpServletRequest) pageContext.getRequest()).getQueryString()).append("&pageId=")
				.append(controlPageIdMap.get(control)).append("&controlName=").append(control.getControlName())
				.append("&containerIdentifier=").append(control.getContainer().getId()).append("&")
				.append(DEConstants.UPDATE_RESPONSE).append("=true").append("&" + DEConstants.CALLBACK_URL + "=")
				.append(pageContext.getRequest().getParameter(DEConstants.CALLBACK_URL)).append("&recordIdentifier=")
				.append(formData.getRecordId());

		controlUrl.append("'>Change</a>");
		return controlUrl.toString();
	}

}
