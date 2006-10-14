package edu.common.dynamicextensions.ui.webui.action;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.processor.LoadFormControlsProcessor;
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
public class LoadFormControlsAction extends BaseDynamicExtensionsAction 
{
    /**
     * 
     */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) 
    {
        
		ControlsForm actionForm = (ControlsForm)form;
        
        ContainerInterface containerInterface = (ContainerInterface)CacheManager.getObjectFromCache(request,Constants.CONTAINER_INTERFACE);
        LoadFormControlsProcessor loadFormControlsProcessor =   LoadFormControlsProcessor.getInstance();
        
        loadFormControlsProcessor.loadFormControls(actionForm,containerInterface);
        return mapping.findForward(Constants.SHOW_BUILD_FORM_JSP);
        
    }
        
		/*List toolsList = getToolsList();
		actionForm.setToolsList(toolsList);
		if(actionForm.getUserSelectedTool() == null || actionForm.getOperation().equalsIgnoreCase("controlAdded"))
        {
			actionForm.setUserSelectedTool(toolsList.get(0).toString());
		} 
		if(actionForm.getDataType() == null) 
        {
			actionForm.setDataType("");
		}
        
		if(actionForm.getDisplayChoice() == null) 
        {
			actionForm.setDisplayChoice("");
		}
		
		
		actionForm.setSelectedControlAttributesList(getSelectedControlAttributesList(actionForm.getUserSelectedTool()));
		return mapping.findForward(Constants.SHOW_BUILD_FORM_JSP);
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
