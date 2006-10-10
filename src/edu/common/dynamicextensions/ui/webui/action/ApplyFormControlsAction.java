package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.Constants;


/**
 * 
 * @author deepti_shelar
 *
 */
public class ApplyFormControlsAction extends BaseDynamicExtensionsAction {
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request,
			HttpServletResponse response) {
		ControlsForm actionForm = (ControlsForm) form;
		if(actionForm.getOperation().equalsIgnoreCase(Constants.CONTROL_SELECTED_ACTION)) {
			CacheManager.addObjectToCache(request,Constants.CONTROLS_FORM , actionForm);
			return mapping.findForward(Constants.CONTROL_SELECTED_ACTION);
		}
		if(actionForm.getOperation().equalsIgnoreCase(Constants.SHOW_CREATE_FORM_JSP)) {
			return mapping.findForward(Constants.SHOW_CREATE_FORM_JSP);
		}
		return mapping.findForward(Constants.SUCCESS);
	}  
}
