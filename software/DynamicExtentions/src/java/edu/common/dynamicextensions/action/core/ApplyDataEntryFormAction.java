
package edu.common.dynamicextensions.action.core;

import java.io.IOException;
import java.util.Stack;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;

import edu.common.dynamicextensions.domain.nui.UserContext;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.napi.FormData;
import edu.common.dynamicextensions.napi.FormDataManager;
import edu.common.dynamicextensions.napi.impl.FormDataManagerImpl;
import edu.common.dynamicextensions.nutility.HTTPFormDataCollector;
import edu.common.dynamicextensions.nvalidator.DraftValidatorUtil;
import edu.common.dynamicextensions.nvalidator.ValidatorUtil;
import edu.common.dynamicextensions.ui.util.Constants;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.ui.webui.util.WebUIManager;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.common.dynamicextensions.util.global.DEConstants;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.dao.newdao.ActionStatus;

/**
 * It populates the Attribute values entered in the dynamically generated controls.
 * @author chetan_patil, suhas_khot, Kunal
 */
public class ApplyDataEntryFormAction extends HttpServlet {

	private static final String SESSION_DATA = "sessionData";
	/**
	 * 
	 */
	private static final long serialVersionUID = -8750223990174751784L;

	/* (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 * 1: collect values from UI
	 * 2: Update stack
	 * 3: forward 
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {

		ValidatorUtil validator = Boolean.parseBoolean(request.getParameter("isDraft")) ? new DraftValidatorUtil() : new ValidatorUtil();
		HTTPFormDataCollector collector = new HTTPFormDataCollector(request, validator);
		
		try {
			FormData formData = collector.collectFormData();

			if (isMainFormSubmitted(request)) {
				FormDataManager dataManager = new FormDataManagerImpl();
				Long recordId = dataManager.saveOrUpdateFormData(getUserContext(request), formData);
				request.setAttribute(ActionStatus.ACTIONSTAUS, ActionStatus.SUCCESSFUL);
				response.sendRedirect(getCallbackURL(request, response, String.valueOf(recordId),
						WebUIManagerConstants.SUCCESS, formData.getContainer().getId()));

				// clear all session data on successful data submission
				CacheManager.clearCache(request);
			} else {
				defaultForward(request, response);
			}
		} catch (DynamicExtensionsApplicationException applicationException) {
			request.setAttribute(DEConstants.ERRORS_LIST, collector.getErrorList());
			defaultForward(request, response);
		}

	}

	private boolean isMainFormSubmitted(HttpServletRequest request) {
		Stack<FormData> formDatas = (Stack<FormData>) CacheManager.getObjectFromCache(request,
				DEConstants.FORM_DATA_STACK);
		return !isDetailsLinkClicked(request) && formDatas.size() == 1;
	}

	private boolean isDetailsLinkClicked(HttpServletRequest request) {
		return Constants.INSERT_CHILD_DATA.equals(request.getParameter(Constants.DATA_ENTRY_OPERATION));
	}

	private void defaultForward(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		String actionForward;
		actionForward = WebUIManagerConstants.LOAD_DATA_ENTRY_FORM_ACTION_URL;
		RequestDispatcher rd = getServletContext().getRequestDispatcher(actionForward);
		rd.forward(request, response);
	}

	/**
	 * This method gets the Callback URL from cache, reforms it and redirect the response to it.
	 * @param request HttpServletRequest to obtain session
	 * @param response HttpServletResponse to redirect the CallbackURL
	 * @param recordIdentifier Identifier of the record to reconstruct the CallbackURL
	 * @return true if CallbackURL is redirected, false otherwise
	 * @throws IOException
	 */
	private String getCallbackURL(HttpServletRequest request, HttpServletResponse response, String recordIdentifier,
			String webUIManagerConstant, Long containerId) throws IOException {
		String objectFromCache = (String) CacheManager.getObjectFromCache(request, DEConstants.CALLBACK_URL);
		StringBuilder callbackURL = new StringBuilder((objectFromCache != null ? objectFromCache : StringUtils.EMPTY));
		if (!StringUtils.isEmpty(callbackURL.toString())) {
			if (callbackURL.toString().contains("?")) {
				callbackURL.append("&").append(WebUIManager.getRecordIdentifierParameterName()).append("=")
						.append(recordIdentifier).append("&").append(WebUIManager.getOperationStatusParameterName())
						.append("=").append(webUIManagerConstant).append("&containerId=").append(containerId);
			} else {
				callbackURL.append("?").append(WebUIManager.getRecordIdentifierParameterName()).append("=")
						.append(recordIdentifier).append("&").append(WebUIManager.getOperationStatusParameterName())
						.append("=").append(webUIManagerConstant).append("&containerId=").append(containerId);
			}

			if (Boolean.parseBoolean(request.getParameter(WebUIManagerConstants.ISDRAFT))) {
				callbackURL.append("&").append(WebUIManagerConstants.ISDRAFT).append("=")
						.append(request.getParameter(WebUIManagerConstants.ISDRAFT));
			} else if (Boolean.parseBoolean(request.getParameter(WebUIManagerConstants.IS_FORM_LOCKED))) {
				callbackURL.append("&").append(WebUIManagerConstants.IS_FORM_LOCKED).append("=")
						.append(request.getParameter(WebUIManagerConstants.IS_FORM_LOCKED));
			}
		}
		return callbackURL.toString();
	}
	
	private UserContext getUserContext(HttpServletRequest httpReq) {
		final SessionDataBean sessionBean = (SessionDataBean) httpReq.getSession().getAttribute(SESSION_DATA);
		
		return new UserContext() {

			@Override
			public Long getUserId() {
				return sessionBean.getUserId();
			}

			@Override
			public String getUserName() {
				return sessionBean.getUserName();
			}

			@Override
			public String getIpAddress() {
				return sessionBean.getIpAddress();
			}			
		};
	}
}