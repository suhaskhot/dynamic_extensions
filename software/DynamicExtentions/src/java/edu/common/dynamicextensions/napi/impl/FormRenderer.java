
package edu.common.dynamicextensions.napi.impl;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;

import edu.common.dynamicextensions.domain.nui.Container;
import edu.common.dynamicextensions.domain.nui.SubFormControl;
import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormDataManager;
import edu.common.dynamicextensions.napi.LayoutRenderer;
import edu.common.dynamicextensions.ui.util.Constants;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.common.dynamicextensions.util.global.DEConstants;

public class FormRenderer implements LayoutRenderer {

	private static final String CHILD_ROW_ID = "childRowId";
	public static final String CONTEXT_INFO = "contextInfo";
	private Stack<FormData> formDataStack;
	private HttpServletRequest request;
	private final Container container;
	protected Boolean showAssociationControlsAsLink = false;
	private Map<ContextParameter, String> contextParameter;

	public enum ContextParameter {
		MODE, ACTIVATION_DATE, IS_AJAX, CONTEXT_PATH, MANDATORY_MESSAGE, FORM_LABEL
	}

	public FormRenderer(Stack<FormData> formData, Map<ContextParameter, String> contextParameter) {
		formDataStack = formData;
		container = formDataStack.peek().getContainer();
		this.contextParameter = contextParameter;
	}
	public FormRenderer(HttpServletRequest req) {
		this.request = req;
		initFormState();
		container = formDataStack.peek().getContainer();
		initContextValues();
	}

	private void initContextValues() {
		contextParameter = new EnumMap<ContextParameter, String>(ContextParameter.class);
		contextParameter.put(ContextParameter.ACTIVATION_DATE, request.getParameter(Constants.ENCOUNTER_DATE));
		contextParameter.put(ContextParameter.CONTEXT_PATH, request.getContextPath());
		String mode = request.getParameter(WebUIManagerConstants.MODE_PARAM_NAME);
		contextParameter.put(ContextParameter.MODE, (mode == null ? WebUIManagerConstants.EDIT_MODE : mode));
		contextParameter.put(ContextParameter.MANDATORY_MESSAGE, request.getParameter(Constants.MANDATORY_MESSAGE));
		contextParameter.put(ContextParameter.FORM_LABEL, request.getParameter(WebUIManagerConstants.FORM_LABEL));
		CacheManager.addObjectToCache(request, CONTEXT_INFO, contextParameter);
	}

	@Override
	public String render() {
		//FormDataUtility.evaluateSkipLogic(formDataStack.peek());
		return container.render(contextParameter, formDataStack.peek());

	}

	/**
	 * Initialises the sate of the form depending on the record Id and whether a main form or a subform is opened 
	 */
	private void initFormState() {
		if (request.getParameter(WebUIManagerConstants.CALLBACK_URL_PARAM_NAME) != null) {
			CacheManager.clearCache(request);
			cacheCallbackURL();
		}

		Container container = Container
				.getContainer(Long.valueOf(request.getParameter(Constants.CONTAINER_IDENTIFIER)));
		formDataStack = (Stack<FormData>) CacheManager.getObjectFromCache(request, DEConstants.FORM_DATA_STACK);
		List<String> errorList = (List<String>) request.getAttribute(DEConstants.ERRORS_LIST);

		if (errorList != null && !errorList.isEmpty()) {
			return;
		}

		if (formDataStack == null)//main form  is opened
		{
			formDataStack = new Stack<FormData>();
			CacheManager.addObjectToCache(request, DEConstants.FORM_DATA_STACK, formDataStack);
			String recordId = request.getParameter(WebUIManagerConstants.RECORD_IDENTIFIER_PARAMETER_NAME);
			if (recordId != null && !recordId.isEmpty()) {
				FormDataManager dataManager = new FormDataManagerImpl();
				formDataStack.push(dataManager.getFormData(container, Long.valueOf(recordId)));
			} else {
				formDataStack.push(new FormData(container));
			}

		} else if (isSubformSubmitted()) {
			formDataStack.pop();
		}
		// subform opened
		else {

			final long subformContainerId = Long.valueOf(request.getParameter(Constants.CHILD_CONTAINER_ID));
			ControlValue subformControlValue = getSubformControl(formDataStack.peek(),
					subformContainerId);
			List<FormData> subformValue = (List<FormData>) subformControlValue.getValue();
			if (((SubFormControl) subformControlValue.getControl()).isCardinalityOneToMany()) {
				formDataStack.push(subformValue.get(getRowIndex(subformContainerId)));
			} else {
				formDataStack.push(subformValue.get(0));
			}

		}
	}
	private boolean isSubformSubmitted() {
		return request.getParameter(Constants.CHILD_CONTAINER_ID) == null
				|| request.getParameter(Constants.CHILD_CONTAINER_ID).isEmpty();
	}

	private void cacheCallbackURL() {
		final String callBackURL = request.getParameter(WebUIManagerConstants.CALLBACK_URL_PARAM_NAME);
		if (callBackURL != null && !callBackURL.isEmpty()) {
			CacheManager.addObjectToCache(request, DEConstants.CALLBACK_URL, callBackURL);
		}
	}
	private int getRowIndex(Long containerId) {
		String rowNumberString = request.getParameter(CHILD_ROW_ID);
		int rowNumber = 0;
		if (rowNumberString != null) {
			rowNumber = Integer.parseInt(rowNumberString) - 1;
		}
		return rowNumber;
	}

	/**
	 * @param formData 
	 * @param subformContainerId subform opened
	 * @return controlValue for the given subform
	 */
	private ControlValue getSubformControl(FormData formData, long subformContainerId) {
		ControlValue subformControlValue = null;
		for (ControlValue controlValue : formData.getFieldValues()) {
			if (controlValue.getControl() instanceof SubFormControl) {
				if (((SubFormControl) controlValue.getControl()).getSubContainer().getId() == subformContainerId) {
					subformControlValue = controlValue;
					break;
				}
			}
		}
		return subformControlValue;
	}

	public String getForward() {
		String forward = "/pages/de/dataEntry/dataEntry.jsp";
		if (container != null && container.isSurveyForm()) {
			forward = "/pages/de/surveymode.jsp";
		}
		return forward;
	}
}
