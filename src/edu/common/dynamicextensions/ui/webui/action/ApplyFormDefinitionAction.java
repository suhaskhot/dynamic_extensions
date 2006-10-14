package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.processor.ApplyFormDefinitionProcessor;
import edu.common.dynamicextensions.ui.webui.actionform.FormDefinitionForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * This Action class handles two situations , 
 * 1. When user selects 'Next' from createForm.jsp. This time a call to EntityProcessor will just create an
 * entity and populate it with actionform's data. This entity is then saved to cache.
 * 2. When user selects 'Save' from createForm.jsp. This time a call to EntityProcessor will create an
 * entity and will save it to database. This entity is then saved to cache.
 * The exception thrown can be of 'Application' type ,in this case the same Screen will be displayed  
 * added with error messages .
 * And The exception thrown can be of 'System' type, in this case user will be directed to Error Page.
 * @author deepti_shelar
 */
public class ApplyFormDefinitionAction extends BaseDynamicExtensionsAction {
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request,
			HttpServletResponse response) {
		FormDefinitionForm formDefinitionForm = (FormDefinitionForm)form;
		String target = "";
		ContainerInterface containerInterface = (ContainerInterface)CacheManager.getObjectFromCache(request,Constants.CONTAINER_INTERFACE);
		ApplyFormDefinitionProcessor applyFormDefinitionProcessor = ApplyFormDefinitionProcessor.getInstance();
		if (formDefinitionForm.getOperation().equalsIgnoreCase(Constants.BUILD_FORM)) {
			containerInterface = applyFormDefinitionProcessor.addEntityToContainer(containerInterface, formDefinitionForm,false);		
			target = Constants.BUILD_FORM;
		} else {// When we click on save 
			containerInterface = applyFormDefinitionProcessor.addEntityToContainer(containerInterface, formDefinitionForm,true);
			target = Constants.SUCCESS;
		}
		CacheManager.addObjectToCache(request,Constants.CONTAINER_INTERFACE, containerInterface);
		return mapping.findForward(target);
	}  
}
