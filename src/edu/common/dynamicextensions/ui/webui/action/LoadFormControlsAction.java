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
 * This Action class Loads the Primary Information needed for BuildForm.jsp.
 * This will first check if the object is already present in cache , If yes, it will update
 * the actionForm and If No, It will populate the actionForm with fresh data.  
 * The exception thrown can be of 'Application' type ,in this case the same Screen will be displayed  
 * added with error messages .
 * And The exception thrown can be of 'System' type, in this case user will be directed to Error Page.
 * @author deepti_shelar
 */
public class LoadFormControlsAction extends BaseDynamicExtensionsAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ControlsForm cacheForm = (ControlsForm)CacheManager.getObjectFromCache(request,Constants.CONTROLS_FORM);
		ControlsForm actionForm = (ControlsForm)form;
		List toolsList = getToolsList();
		actionForm.setToolsList(toolsList);
		if(cacheForm != null) {
			actionForm.update(cacheForm);
			actionForm.setSelectedControlAttributesList(getSelectedControlAttributesList(actionForm.getUserSelectedTool()));
		} else {
			actionForm.setUserSelectedTool(toolsList.get(0).toString());
			actionForm.setSelectedControlAttributesList(getSelectedControlAttributesList(actionForm.getUserSelectedTool()));
			actionForm.setDataType("");
			actionForm.setDisplayChoice("");
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
