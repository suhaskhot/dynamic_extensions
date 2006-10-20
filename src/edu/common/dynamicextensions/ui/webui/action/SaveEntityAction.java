/*
 * Created on Oct 18, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.ui.webui.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.processor.ApplyFormControlsProcessor;
import edu.common.dynamicextensions.processor.ContainerProcessor;
import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SaveEntityAction extends BaseDynamicExtensionsAction
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		//Save entity to cache
		System.out.println("Save entity");
		//Get controls form
		ControlsForm controlsForm = (ControlsForm) form;

		//Get container interface from cache
		ContainerInterface containerInterface = (ContainerInterface) CacheManager.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);
		/*//Add control to form
		ApplyFormControlsProcessor formControlsProcessor = ApplyFormControlsProcessor.getInstance();
		formControlsProcessor.addControlToForm(containerInterface, controlsForm);
		
		//Add back object to cache
		CacheManager.addObjectToCache(request, Constants.CONTAINER_INTERFACE, containerInterface);
		
		*///Call container processor save method
        
		ContainerProcessor containerProcessor  = ContainerProcessor.getInstance();
        try
        {
            containerProcessor.saveContainer(containerInterface);
        }
        catch (Exception e)
        {
            List list = new ArrayList();
            handleException(e,list);
            
        }
		
		/*ActionForward actionForward = mapping.findForward(Constants.SUCCESS);
		actionForward.getPath();
		return actionForward;*/
		ActionForward actionForward = mapping.findForward(Constants.SUCCESS);
		try
		{
			response.sendRedirect("http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath()
					+ actionForward.getPath());
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
