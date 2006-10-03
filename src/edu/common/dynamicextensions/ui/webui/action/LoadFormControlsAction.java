package edu.common.dynamicextensions.ui.webui.action;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domain.userinterface.UIControlsConfigurationFactory;
import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;

/**
 * 
 * @author deepti_shelar
 */
public class LoadFormControlsAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
	//	setSessionObject(request);  
		ControlsForm controlsForm = (ControlsForm) form;
		
		populateControlsForm(controlsForm);
		//request.setAttribute("toolsList",toolsList);
		return mapping.findForward("showBuildFormJSP");
	}
	private void populateControlsForm(ControlsForm controlsForm) {
		List toolsList = new ArrayList();
		//UIControlsConfigurationFactory uiControlsConfigurationFactory = UIControlsConfigurationFactory.getInstance();
		//toolsList = uiControlsConfigurationFactory.getControlNames();
		//uiControlsConfigurationFactory.getConrolAttributesList(controlName)
		toolsList.add("TextBox");
		toolsList.add("RadioButton");
		toolsList.add("ComboBox");
		controlsForm.setToolsList(ActionUtil.getToolsList(toolsList));
		controlsForm.setSelectedTool("selectedTool");
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
