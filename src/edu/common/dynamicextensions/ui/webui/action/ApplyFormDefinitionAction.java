package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.ui.webui.actionform.FormDefinitionForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.ui.webui.util.FormDetailsObject;
import edu.common.dynamicextensions.util.EntityManager;
import edu.wustl.common.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

/**
 * 
 * @author deepti_shelar
 *
 */
public class ApplyFormDefinitionAction extends Action {
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request,
			HttpServletResponse response) {
		FormDefinitionForm formDefinitionForm = (FormDefinitionForm)form;
		String nextOperation = formDefinitionForm.getOperation();
		String target = "";
		if (nextOperation.equalsIgnoreCase("addControlsToForm")) {
			HttpSession session = request.getSession();
			String sessionId = session.getId();
			CacheManager cacheManager = CacheManager.getInstance();
			FormDetailsObject formDetailsObject = cacheManager.getObjectFromCache(sessionId);
			if (formDetailsObject == null) {
				formDetailsObject = new FormDetailsObject();
			}
			formDetailsObject.updateFormDefinitionForm(formDefinitionForm);
			cacheManager.addObjectToCache(sessionId,formDetailsObject);
			target = "addControlsToForm";
		} else {
			EntityManager entityManager = EntityManager.getInstance(); 
			String entityIdentifier = formDefinitionForm .getEntityIdentifier();
			Entity entity = null;
			try {
				if(entityIdentifier != null && entityIdentifier.equals("")) {
					entity = new Entity();
					entity.setId(new Long("2"));
					entity.setName("EntityName");
					//entityManager.createEntity(entity);
				}
				request.setAttribute("entityIdentifier",entity.getId().toString());
				target = "success";
			}  catch (Exception entityCreationException) {
				Logger.out.debug("excp "+ entityCreationException.getMessage());
				Logger.out.error(entityCreationException.getMessage(), entityCreationException);
				return mapping.findForward(new String(Constants.FAILURE));
			} 
		}
		return mapping.findForward(target);
	}  
	/**
	 * 
	 * @param request
	 * @return
	 *//*
	protected SessionDataBean getSessionData(HttpServletRequest request) {
		Object obj = request.getSession().getAttribute(Constants.SESSION_DATA);
		if(obj!=null)
		{
			SessionDataBean sessionData = (SessionDataBean) obj;
			return  sessionData;
		}
		return null;
		//return (String) request.getSession().getAttribute(Constants.SESSION_DATA);
	}*/

}
