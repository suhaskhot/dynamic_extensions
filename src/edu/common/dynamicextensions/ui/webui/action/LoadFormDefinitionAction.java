
package edu.common.dynamicextensions.ui.webui.action;
/**
 * This Action class Loads the Primary Information needed for CreateForm.jsp.
 * This will first check if the form object is already present in cache , If yes, it will update
 * the actionForm and If No, It will populate the actionForm with fresh data.  
 * The exception thrown can be of 'Application' type ,in this case the same Screen will be displayed  
 * added with error messages .
 * And The exception thrown can be of 'System' type, in this case user will be directed to Error Page.
 * @author deepti_shelar
 */
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.EntityProcessor;
import edu.common.dynamicextensions.ui.webui.actionform.FormDefinitionForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.Constants;


public class LoadFormDefinitionAction extends BaseDynamicExtensionsAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		FormDefinitionForm actionForm = (FormDefinitionForm)form;
		EntityInterface cacheObject = (EntityInterface)CacheManager.getObjectFromCache(request,Constants.ENTITY_INTERFACE);
		if(cacheObject != null) {
			EntityProcessor entityProcessor = EntityProcessor.getInstance();
			entityProcessor.populateEntityInformation(cacheObject, actionForm);
		} 
		try {
			populateExistingFormsList(actionForm,request);
		} catch (DynamicExtensionsApplicationException applicationException) {
			List errorsList = handleException(applicationException,new ArrayList());
		} catch (DynamicExtensionsSystemException systemException) {
			handleException(systemException,new ArrayList());		
			return mapping.findForward(Constants.SYSTEM_EXCEPTION);
		}
		return (mapping.findForward(Constants.SUCCESS));
	}
	/**
	 * This method populates the actionform with the existing forms list.
	 * @param entitySelectionForm
	 */
	public void populateExistingFormsList(FormDefinitionForm actionForm,HttpServletRequest request) 
	throws DynamicExtensionsApplicationException ,DynamicExtensionsSystemException{

		/*DefaultBizLogic defaultBizLogic =  (DefaultBizLogic)BizLogicFactory.getBizLogic(formDefinitionForm.getFormId());   
		List existingFormsList =  defaultBizLogic.retrieve("Entity");
		if(existingFormsList == null){
			existingFormsList = new ArrayList();
		}*/
		Entity entity = new Entity();
		entity.setId(new Long("1"));
		entity.setName("Entity");
		List existingFormsList = new ArrayList();
		existingFormsList.add(entity);
		actionForm.setExistingFormsList(ActionUtil.getExistingFormsList(existingFormsList));
	}

}
