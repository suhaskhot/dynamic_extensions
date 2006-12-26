
package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.processor.LoadFormPreviewProcessor;
import edu.common.dynamicextensions.ui.webui.actionform.PreviewForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * This Class forwards the action to the FormPreview JSP
 * @author sujay_narkar, chetan_patil
 * @version 1.0
 */
public class LoadFormPreviewAction extends BaseDynamicExtensionsAction
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
		PreviewForm previewForm = (PreviewForm) form;
		ContainerInterface containerInterface = (ContainerInterface) CacheManager
				.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);
		if (containerInterface != null)
		{
			LoadFormPreviewProcessor loadFormPreviewProcessor = LoadFormPreviewProcessor
					.getInstance();
			loadFormPreviewProcessor.populatePreviewForm(containerInterface, previewForm);
		}
		return mapping.findForward(Constants.SHOW_FORM_PREVIEW_JSP);
	}

}
