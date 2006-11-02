
package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.ui.webui.actionform.FormsIndexForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * This class will forward the request to LoadFormDefinitionAction.java.
 * @author deepti_shelar
 */
public class ApplyFormsIndexAction extends BaseDynamicExtensionsAction
{
	/**
	 * This mathod will forward the request to LoadFormDefinitionAction.java.
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		FormsIndexForm formsIndexForm = (FormsIndexForm) form;
		ActionForward actionForward = null;
		String mode = formsIndexForm.getMode();
		if (mode != null && mode.equalsIgnoreCase(Constants.ADD_NEW_FORM))
		{
			CacheManager.clearCache(request);
			actionForward = mapping.findForward(Constants.SUCCESS);
		}
		else if (mode != null && mode.equalsIgnoreCase(Constants.INSERT_DATA))
		{
			CacheManager.clearCache(request);
			actionForward = mapping.findForward(Constants.INSERT_DATA);
		}
		
		return  mapping.findForward(Constants.SUCCESS);
	}
}
