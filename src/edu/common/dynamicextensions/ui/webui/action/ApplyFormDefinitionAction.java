package edu.common.dynamicextensions.ui.webui.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.ContainerProcessor;
import edu.common.dynamicextensions.processor.EntityProcessor;
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
		EntityProcessor entityProcessor = EntityProcessor.getInstance();
		ContainerProcessor containerProcessor = ContainerProcessor.getInstance();
		ContainerInterface containerInterface = (ContainerInterface)CacheManager.getObjectFromCache(request,Constants.CONTAINER_INTERFACE);
		if(containerInterface == null) {
			 containerInterface = containerProcessor.createContainer();
		}
		EntityInterface entityInterface = containerInterface.getEntity();
		if (formDefinitionForm.getOperation().equalsIgnoreCase(Constants.BUILD_FORM)) {
			try {
				if(entityInterface == null) {
					entityInterface = entityProcessor.createAndPopulateEntity(formDefinitionForm);
				} else {
					entityProcessor.populateEntityInformation(entityInterface, formDefinitionForm);
				}
				containerInterface.setEntity(entityInterface);
				//CacheManager.addObjectToCache(request, Constants.ENTITY_INTERFACE,entityInterface);
				CacheManager.addObjectToCache(request,Constants.CONTAINER_INTERFACE, containerInterface);
				target = Constants.BUILD_FORM;
			} catch (DynamicExtensionsSystemException systemException) {
				handleException(systemException,new ArrayList());		
				return mapping.findForward(Constants.SYSTEM_EXCEPTION);
			}	
		} else {// When we click on save 
			try {
				entityInterface = entityProcessor.createAndSaveEntity(formDefinitionForm);
				containerInterface = containerProcessor.createContainer();
				containerInterface.setEntity(entityInterface);
				CacheManager.addObjectToCache(request,Constants.CONTAINER_INTERFACE,containerInterface);
				target = Constants.SUCCESS;
			} catch (DynamicExtensionsSystemException systemException) {
				handleException(systemException,new ArrayList());		
				return mapping.findForward(Constants.SYSTEM_EXCEPTION);
			} catch (DynamicExtensionsApplicationException appException) {
			    request.setAttribute(Constants.ERRORS_LIST,handleException(appException,new ArrayList()));	
			    target = Constants.SUCCESS;
            }			
		}
		return mapping.findForward(target);
	}  

}
