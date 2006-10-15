package edu.common.dynamicextensions.ui.webui.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.taglibs.standard.tag.common.xml.IfTag;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.processor.ApplyFormControlsProcessor;
import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.Constants;


/*This class is executed when user selects 'Add to Form'.
 * The exception thrown can be of 'Application' type ,in this case the same Screen will be displayed  
 * added with error messages .
 * And The exception thrown can be of 'System' type, in this case user will be directed to Error Page.
 * @author deepti_shelar
 *
 */
public class AddControlsAction extends BaseDynamicExtensionsAction {
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println("Add control action");
		//Get controls form
		ControlsForm controlsForm = (ControlsForm)form;
		//Get container interface from cache
		ContainerInterface containerInterface = (ContainerInterface)CacheManager.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);
		//Add control to form
        
        if (controlsForm.getToolBoxClicked() == null || controlsForm.getToolBoxClicked().equals(""))
        {
            ApplyFormControlsProcessor formControlsProcessor = ApplyFormControlsProcessor.getInstance();
            formControlsProcessor.addControlToForm(containerInterface, controlsForm);
        }
		
		//Add back object to cache
		CacheManager.addObjectToCache(request, Constants.CONTAINER_INTERFACE, containerInterface);
        
        String controlOperation = (String)CacheManager.getObjectFromCache(request, Constants.CONTROL_OPERATION);
        if(controlOperation == null){
            CacheManager.addObjectToCache(request, Constants.CONTROL_OPERATION, new String(""));
        }
        controlOperation = controlsForm.getControlOperation();
        CacheManager.addObjectToCache(request, Constants.CONTROL_OPERATION, controlOperation);
        
        String selectedControlId = (String)CacheManager.getObjectFromCache(request, Constants.SELECTED_CONTROL_ID);
        if(selectedControlId == null){
            CacheManager.addObjectToCache(request, Constants.SELECTED_CONTROL_ID, new String(""));
        }
        selectedControlId = controlsForm.getSelectedControlId();
        CacheManager.addObjectToCache(request, Constants.SELECTED_CONTROL_ID, selectedControlId);
        
        String userSelectedTool = (String)CacheManager.getObjectFromCache(request, Constants.USER_SELECTED_TOOL);
        if(userSelectedTool == null){
            CacheManager.addObjectToCache(request, Constants.USER_SELECTED_TOOL, new String(""));
        }
        userSelectedTool = controlsForm.getUserSelectedTool();
        CacheManager.addObjectToCache(request, Constants.USER_SELECTED_TOOL, userSelectedTool);
      
        
          ActionForward actionForward = mapping.findForward(Constants.SUCCESS);
            try 
            {
				response.sendRedirect("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + actionForward .getPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return null;
	}
}
