package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


/**
 * 
 * @author deepti_shelar
 *
 */
public class ApplyFormControlsAction extends Action {
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request,
			HttpServletResponse response) {
		//FormDefinitionForm formDefinitionForm = (FormDefinitionForm)form;
		/*String nextOperation = formDefinitionForm.getOperation();
		String target = "";
		if (nextOperation.equalsIgnoreCase("addControlsToForm")) {
			target = "addControlsToForm";
		} else {
			EntityManager entityManager = EntityManager.getInstance(); 
			String entityIdentifier = formDefinitionForm .getEntityIdentifier();
			Entity entity = null;
			try {
				if(entityIdentifier != null && entityIdentifier.equals("")) {
					entity = new Entity(formDefinitionForm );
					entityManager.createEntity(entity);
				}
				request.setAttribute("entityIdentifier",entity.getId().toString());
				target = "success";
			}  catch (Exception entityCreationException) {
				Logger.out.debug("excp "+ entityCreationException.getMessage());
				Logger.out.error(entityCreationException.getMessage(), entityCreationException);
				return mapping.findForward(new String(Constants.FAILURE));
			} 
		}*/
		return mapping.findForward("success");
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
