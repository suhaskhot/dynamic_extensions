package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.ui.webui.actionform.FormDefinitionForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.ui.webui.util.FormDetailsObject;

/**
 * 
 * @author deepti_shelar
 *
 */
public class ShowHomePageAction extends Action {
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request,
			HttpServletResponse response) {
		FormDefinitionForm formDefinitionForm = (FormDefinitionForm)form;
		clearForm(formDefinitionForm,request);
		return mapping.findForward("success");
	}  
	public void clearForm(FormDefinitionForm formDefinitionForm,HttpServletRequest request) {
		formDefinitionForm.reset();
		formDefinitionForm.setFormName("");
		formDefinitionForm.setDescription("");
		formDefinitionForm.setCreateAs("");
		HttpSession session = request.getSession();
		String sessionId = session.getId();
		CacheManager cacheManager = CacheManager.getInstance();
		FormDetailsObject formDetailsObject = cacheManager.getObjectFromCache(sessionId);
		if (formDetailsObject == null) {
			formDetailsObject = new FormDetailsObject();
		}
		formDetailsObject.updateFormDefinitionForm(formDefinitionForm);
		cacheManager.addObjectToCache(sessionId,formDetailsObject);
		System.out.println(cacheManager.getObjectFromCache(sessionId));
		System.out.println(cacheManager.getObjectFromCache(sessionId).getFormDefinitionForm().getFormName());
	}

}
