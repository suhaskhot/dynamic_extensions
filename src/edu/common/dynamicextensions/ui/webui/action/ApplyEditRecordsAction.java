/**
 * 
 */

package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.util.global.Constants;

/**
 * @author chetan_patil
 *
 */
public class ApplyEditRecordsAction extends BaseDynamicExtensionsAction
{

	/**
	 * This mathod will forward the request to LoadFormDefinitionAction.java.
	 * 
	 * @param mapping ActionMapping mapping
	 * @param form ActionForm form
	 * @param  request HttpServletRequest request
	 * @param response HttpServletResponse response
	 * @return ActionForward forward to next action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		//EditRecordsForm editRecordsForm = (EditRecordsForm) form;
		return mapping.findForward(Constants.SUCCESS);

	}
}
