/*
 * Created on Nov 15, 2006
 * @author
 *
 */

package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.ui.util.Constants;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.common.dynamicextensions.util.global.DEConstants;

/**
 * @author preeti_munot
 *
 */
public class DisplayContainerAction extends BaseDynamicExtensionsAction
{

	/* (non-Javadoc)
	 * @see org.apache.struts.actions.DispatchAction#execute(org.apache.struts.action.ActionMapping, org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String identifier = request.getParameter("containerIdentifier");
		StringBuffer actionForward = new StringBuffer("/LoadGroupDefinitionAction.do?operationMode=");
		String callbackUrl = request.getParameter(WebUIManagerConstants.CALLBACK_URL_PARAM_NAME);
		String userId = request.getParameter(WebUIManagerConstants.USER_ID);
		if (identifier == null)
		{
			CacheManager.clearCache(request);
			actionForward.append("AddNewForm");
		}
		else
		{
			 actionForward.append("EditForm&containerIdentifier=").append(identifier);
		}
		HttpSession session=request.getSession();
		String staticEntityId=(String) session.getAttribute(Constants.SELECTED_STATIC_ENTITYID);
		
		actionForward.append(Constants.AMPERSAND).append(Constants.SELECTED_STATIC_ENTITYID)
				.append(Constants.EQUAL).append(staticEntityId);
		CacheManager.addObjectToCache(request, DEConstants.CALLBACK_URL, callbackUrl);
		CacheManager.addObjectToCache(request, WebUIManagerConstants.USER_ID, userId);
		return new ActionForward(actionForward.toString());
	}

}