
package edu.common.dynamicextensions.ui.webui.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.cawebeav.actionForm.EntitySelectionForm;
import edu.common.cawebeav.actionForm.FormDefinitionForm;
import edu.common.cawebeav.bizlogic.BizLogicFactory;
import edu.common.cawebeav.util.EntitySession;
import edu.common.cawebeav.util.global.Constants;
import edu.wustl.common.bizlogic.DefaultBizLogic;
import edu.wustl.common.util.dbManager.DAOException;
import edu.wustl.common.util.logger.Logger;

/**
 * @author deepti_shelar
 *
 */
public class LoadFormDefinitionAction extends Action {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		setSessionObject(request);  
      FormDefinitionForm formDefinitionForm = (FormDefinitionForm) form;
      try {
			populateExistingFormsList(formDefinitionForm);
		} catch (DAOException daoException) {
			Logger.out.debug("excp "+daoException.getMessage());
          Logger.out.error(daoException.getMessage(), daoException);
          return mapping.findForward(new String(Constants.FAILURE));
		}
		return (mapping.findForward(Constants.SUCCESS));
	}
	 /**
     * 
     * @param entitySelectionForm
     */
    public void populateExistingFormsList(FormDefinitionForm formDefinitionForm) throws DAOException {
      
       DefaultBizLogic defaultBizLogic =  (DefaultBizLogic)BizLogicFactory.getBizLogic(formDefinitionForm.getFormId());   
       List existingFormsList =  defaultBizLogic.retrieve("Entity");
       if(existingFormsList == null){
    	   existingFormsList = new ArrayList();
       }
       formDefinitionForm.setExistingFormsList(ActionUtil.getExistingFormsList(existingFormsList));
    }
	/**
	 * 
	 * @param request
	 */
	public void setSessionObject(HttpServletRequest request) {
		HttpSession session = request.getSession();
		EntitySession entitySession = new EntitySession();
		entitySession.setEntityIdentifier("");
        entitySession.setAttributeIdentifier("");
		session.setAttribute("ENTITY_SESSION",entitySession);
	}
}
