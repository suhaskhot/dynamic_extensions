package edu.common.dynamicextensions.ui.webui.action;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.EntityProcessor;
import edu.common.dynamicextensions.ui.webui.actionform.FormDefinitionForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * 
 * @author deepti_shelar
 *
 */
public class ApplyFormDefinitionAction extends BaseDynamicExtensionsAction {
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request,
			HttpServletResponse response) {
		FormDefinitionForm formDefinitionForm = (FormDefinitionForm)form;
		String nextOperation = formDefinitionForm.getOperation();
		String target = "";
		if (nextOperation.equalsIgnoreCase(Constants.BUILD_FORM)) {
			CacheManager.addObjectToCache(request, Constants.FORM_DEFINITION_FORM,formDefinitionForm);			
			target = Constants.BUILD_FORM;
		} else {// When we click on save 
			EntityProcessor entityProcessor = EntityProcessor.getInstance();
			try {
				EntityInterface entityInterface = entityProcessor.createAndSaveEntity(formDefinitionForm);
				CacheManager.addObjectToCache(request,Constants.ENTITY_INTERFACE, entityInterface);
				target = Constants.SUCCESS;
			} catch (DynamicExtensionsSystemException systemException) {
				handleException(systemException,new ArrayList());		
				return mapping.findForward(Constants.SYSTEM_EXCEPTION);
			}			/*}   catch (DynamicExtensionsApplicationException applicationException) {
			handleException(applicationException,new ArrayList());
			//return mapping.findForward();*/

		}
		return mapping.findForward(target);
	}  
	
}
