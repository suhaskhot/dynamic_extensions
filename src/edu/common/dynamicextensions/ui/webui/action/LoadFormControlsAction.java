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
 * This class Loads the controls for BuildForm.jsp
 * @author deepti_shelar
 */
public class LoadFormControlsAction extends BaseDynamicExtensionsAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ControlsForm cacheForm = (ControlsForm)CacheManager.getObjectFromCache(request,Constants.CONTROLS_FORM);
		ControlsForm actionForm =null;
		if(cacheForm != null) {
			actionForm = (ControlsForm)form;
			actionForm.update(cacheForm);
			List toolsList = getToolsList();
			actionForm.setToolsList(toolsList);
			actionForm.setSelectedControlAttributesList(getSelectedControlAttributesList(actionForm.getUserSelectedTool()));
		} else {
			actionForm = (ControlsForm) form;
			List toolsList = getToolsList();
			actionForm.setToolsList(toolsList);
			actionForm.setUserSelectedTool(toolsList.get(0).toString());
			actionForm.setSelectedControlAttributesList(new ArrayList());
		}

		return mapping.findForward(Constants.SHOW_BUILD_FORM_JSP);
	}
	/**
	 * Returns the toolsList from the xml file.
	 * @return
	 */
			
	private List getToolsList(){
		List toolsList = new ArrayList();
		UIControlsConfigurationFactory uiControlsConfigurationFactory = UIControlsConfigurationFactory.getInstance();
		toolsList = uiControlsConfigurationFactory.getControlNames();
		return toolsList;
	}
	/**
	 * Returns the selectedControlAttributesList from the xml file depending upon the tool passed.
	 * @param userSelectedTool
	 * @return
	 */
	private List getSelectedControlAttributesList(String userSelectedTool)
	{
		List selectedControlAttributesList = new ArrayList();
		UIControlsConfigurationFactory uiControlsConfigurationFactory = UIControlsConfigurationFactory.getInstance();
		selectedControlAttributesList = uiControlsConfigurationFactory.getConrolAttributesList(userSelectedTool);
		return selectedControlAttributesList;
	}
}
