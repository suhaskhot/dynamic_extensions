
package edu.common.dynamicextensions.ui.webui.action;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.processor.ApplyFormControlsProcessor;
import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * This class is executed when user selects 'Add to Form'.
 * The exception thrown can be of 'Application' type ,in this case the same Screen will be displayed  
 * added with error messages .
 * And The exception thrown can be of 'System' type, in this case user will be directed to Error Page.
 * @author deepti_shelar
 *
 */
public class AddControlsAction extends BaseDynamicExtensionsAction
{
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		System.out.println("Add control action");
		//Get controls form
		ControlsForm controlsForm = (ControlsForm) form;

		if (controlsForm.getShowPreview() != null && !controlsForm.getShowPreview().equals(""))
		{
			return mapping.findForward(Constants.LOAD_FORM_PREVIEW_ACTION);
		}

		//Get container interface from cache
		ContainerInterface containerInterface = (ContainerInterface) CacheManager.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);

		//Add control to form
		ApplyFormControlsProcessor formControlsProcessor = ApplyFormControlsProcessor.getInstance();
		formControlsProcessor.addControlToForm(containerInterface, controlsForm);

		//Add back object to cache
		CacheManager.addObjectToCache(request, Constants.CONTAINER_INTERFACE, containerInterface);
		ActionForward actionForward = mapping.findForward(Constants.SUCCESS);
		try
		{
			response.sendRedirect("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()
					+ actionForward.getPath());
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
