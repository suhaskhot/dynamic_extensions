
package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

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
		return mapping.findForward(Constants.SUCCESS);
	}
}
