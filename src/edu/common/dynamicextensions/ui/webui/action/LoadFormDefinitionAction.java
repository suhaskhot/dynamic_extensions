
package edu.common.dynamicextensions.ui.webui.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.actionform.FormDefinitionForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * @author deepti_shelar
 *
 */
public class LoadFormDefinitionAction extends BaseDynamicExtensionsAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		FormDefinitionForm actionForm = (FormDefinitionForm)form;
		FormDefinitionForm cacheForm = (FormDefinitionForm)CacheManager.getObjectFromCache(request,Constants.FORM_DEFINITION_FORM);
		if(cacheForm != null) {
			actionForm.update(cacheForm);
		} 
		try {
			populateExistingFormsList(actionForm,request);
		} catch (DynamicExtensionsApplicationException applicationException) {
			List errorsList = handleException(applicationException,new ArrayList());
			actionForm.setErrorsList(errorsList);
		} catch (DynamicExtensionsSystemException systemException) {
			handleException(systemException,new ArrayList());		
			return mapping.findForward(Constants.SYSTEM_EXCEPTION);
		}
		return (mapping.findForward(Constants.SUCCESS));
	}
	/**
	 * 
	 * @param entitySelectionForm
	 */
	public void populateExistingFormsList(FormDefinitionForm formDefinitionForm,HttpServletRequest request) 
	throws DynamicExtensionsApplicationException ,DynamicExtensionsSystemException{

		/*DefaultBizLogic defaultBizLogic =  (DefaultBizLogic)BizLogicFactory.getBizLogic(formDefinitionForm.getFormId());   
       List existingFormsList =  defaultBizLogic.retrieve("Entity");
       if(existingFormsList == null){
    	   existingFormsList = new ArrayList();
       }*/
		Entity entity = new Entity();
		entity.setId(new Long("1"));
		entity.setName("Entity");
		Entity entity1 = new Entity();
		entity1.setId(new Long("3"));
		entity1.setName("Entity3");
		List existingFormsList = new ArrayList();
		existingFormsList.add(entity);
		existingFormsList.add(entity1);

		formDefinitionForm.setExistingFormsList(ActionUtil.getExistingFormsList(existingFormsList));
	}

}
