package edu.common.dynamicextensions.ui.webui.action;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domain.userinterface.UIControlsConfigurationFactory;
import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;

public class SelectControlAction extends Action {
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request,
			HttpServletResponse response) {
		ControlsForm controlsForm = (ControlsForm)form;
		UIControlsConfigurationFactory uiControlsConfigurationFactory = UIControlsConfigurationFactory.getInstance();
		List controlAttributesList = uiControlsConfigurationFactory.getConrolAttributesList(controlsForm.getUserSelectedTool());
		controlsForm.setSelectedControlAttributesList(controlAttributesList);
		controlsForm.setToolsList(uiControlsConfigurationFactory.getControlNames());
		return mapping.findForward("success");
	}

}
