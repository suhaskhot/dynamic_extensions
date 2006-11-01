/*
 * Created on Oct 18, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.ui.webui.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.processor.ContainerProcessor;
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
		ActionForward actionForward = null;
		//Get container interface from cache
		ContainerInterface containerInterface = (ContainerInterface) CacheManager.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);
		//Call container processor save method
        ContainerProcessor containerProcessor  = ContainerProcessor.getInstance();
        String formName = "";
        try
        {
            containerProcessor.saveContainer(containerInterface);
            
            if((containerInterface!=null)&&(containerInterface.getEntity()!=null))
            {
            	formName = containerInterface.getEntity().getName();
            }
            saveMessages(request, getSuccessMessage(formName));
            actionForward = mapping.findForward(Constants.SUCCESS);
         }
        catch (Exception e)
        {
            List list = new ArrayList();
            boolean isSystemException = handleException(e,list);
            saveErrors(request,getErrorMessages(list,formName));
            if (isSystemException) {
            actionForward = mapping.findForward(Constants.SYSTEM_EXCEPTION);
            }
        }
        return actionForward;
	}
	
	/**
	 * Get messages for successful save of entity
	 */
	private ActionMessages getSuccessMessage(String formName)
	{
		ActionMessages actionMessages = new ActionMessages();
        actionMessages.add(ActionMessages.GLOBAL_MESSAGE,new ActionMessage("app.entitySaveSuccessMessage",formName));
        return actionMessages;
     }
}
