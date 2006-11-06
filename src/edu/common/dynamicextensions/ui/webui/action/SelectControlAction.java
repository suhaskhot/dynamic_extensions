
package edu.common.dynamicextensions.ui.webui.action;

/**
 * This class is called when user selects any of the tool(Control) to be added to the form.
 * This will add the object to the form and will forward it to LoadFormControlsAction.
 * @author deepti_shelar
 */

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.Constants;

public class SelectControlAction extends BaseDynamicExtensionsAction
{
	/**
	 * @param mapping ActionMapping mapping
	 * @param form ActionForm form
	 * @param  request HttpServletRequest request
	 * @param response HttpServletResponse response
	 * @return ActionForward forward to next action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		ControlsForm controlsForm = (ControlsForm) form;
		CacheManager.addObjectToCache(request, Constants.CONTROLS_FORM, controlsForm);
		return mapping.findForward(Constants.SUCCESS);
	}

}
