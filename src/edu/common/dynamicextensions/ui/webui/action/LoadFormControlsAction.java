package edu.common.dynamicextensions.ui.webui.action;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.ui.webui.util.UIControlsConfigurationFactory;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * 
 * @author deepti_shelar
 */
public class LoadFormControlsAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ControlsForm cacheForm = (ControlsForm)CacheManager.getObjectFromCache(request,Constants.CONTROLS_FORM);
		ControlsForm actionForm =null;
		if(cacheForm != null) {
			actionForm = (ControlsForm)form;
			actionForm.update(cacheForm);
			populateControlsForm(actionForm);
		} else {
			actionForm = (ControlsForm) form;
			List toolsList = new ArrayList();
			UIControlsConfigurationFactory uiControlsConfigurationFactory = UIControlsConfigurationFactory.getInstance();
			toolsList = uiControlsConfigurationFactory.getControlNames();
			actionForm.setToolsList(toolsList);
			actionForm.setUserSelectedTool(toolsList.get(0).toString());
			actionForm.setSelectedControlAttributesList(new ArrayList());
		}
		
		return mapping.findForward(Constants.SHOW_BUILD_FORM_JSP);
	}
	private void populateControlsForm(ControlsForm controlsForm) {
		List toolsList = new ArrayList();
		UIControlsConfigurationFactory uiControlsConfigurationFactory = UIControlsConfigurationFactory.getInstance();
		toolsList = uiControlsConfigurationFactory.getControlNames();
		controlsForm.setToolsList(toolsList);
		controlsForm.setSelectedControlAttributesList(uiControlsConfigurationFactory.getConrolAttributesList(controlsForm.getUserSelectedTool()));
		//controlsForm.setBuildFormTool(toolsList.get(0).toString());
		
	}
	/**
	 * 
	 * @param request
	 *//*
	public void setSessionObject(HttpServletRequest request) {
		HttpSession session = request.getSession();
		EntitySession entitySession = new EntitySession();
		entitySession.setEntityIdentifier("");
        entitySession.setAttributeIdentifier("");
		session.setAttribute("ENTITY_SESSION",entitySession);
	}*/
}
