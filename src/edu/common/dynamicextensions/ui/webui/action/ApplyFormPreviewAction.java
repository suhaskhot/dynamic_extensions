
package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.ui.webui.actionform.DataEntryForm;
import edu.common.dynamicextensions.util.global.Constants;

public class ApplyFormPreviewAction extends BaseDynamicExtensionsAction
{

	/**
	 * This method overrides the execute method of the Action class.
	 * It forwards the action to the FormPreview JSP.
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws Exception on exception
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		DataEntryForm dataEntryForm = (DataEntryForm) form;
		String dataEntryOperation = dataEntryForm.getDataEntryOperation();

		if (dataEntryOperation != null && dataEntryOperation.equals("insertChildData"))
		{
			return mapping.findForward("loadChildContainer");
		}
		else if (dataEntryOperation != null && dataEntryOperation.equals("insertParentData"))
		{
			return mapping.findForward("loadParentContainer");
		}

		return (mapping.findForward(Constants.SUCCESS));
	}
}
