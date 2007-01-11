/*
 * Created on Nov 15, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.processor.ApplyGroupDefinitionProcessor;
import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.ui.webui.actionform.GroupForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ApplyGroupDefinitionAction extends BaseDynamicExtensionsAction
{
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		GroupForm groupForm = (GroupForm)form;
		
		ApplyGroupDefinitionProcessor applyGroupDefinitionProcessor = ApplyGroupDefinitionProcessor.getInstance();
		
		EntityGroupInterface entityGroup = null;
		try
		{
			ContainerInterface containerInterface = (ContainerInterface)CacheManager.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);
			entityGroup = applyGroupDefinitionProcessor.saveGroupDetails(groupForm,containerInterface);
			String operationMode = groupForm.getOperationMode();
			if((operationMode!=null)&&(operationMode.equals(Constants.EDIT_FORM)))
			{
				applyGroupDefinitionProcessor.updateEntityGroup(containerInterface,entityGroup,groupForm);
			}
		}
		catch (Exception e)
		{
			String target = catchException(e, request);
			if((target==null)||(target.equals("")))
			{
				return mapping.getInputForward(); 
			}
		}
		if(entityGroup!=null)
		{
			//Add entity group to cache.This will be attached with the entity when the entity is created
			//Till then the object remains in cache
			CacheManager.addObjectToCache(request, Constants.ENTITYGROUP_INTERFACE, entityGroup);	
		}
		//Redirection logic
		return getNextPage(groupForm.getGroupOperation(),mapping);
	}

	/**
	 * @param operationPerformed : Operation performed
	 * @return Action forward for redirection
	 */
	private ActionForward getNextPage(String operationPerformed,ActionMapping mapping)
	{
		ActionForward actionForward = null;
		if(operationPerformed!=null)
		{
			if(operationPerformed.equals(ProcessorConstants.SAVE_GROUP))
			{
				actionForward = mapping.findForward(Constants.SHOW_DYNAMIC_EXTENSIONS_HOMEPAGE);
			}
			else
			{
				actionForward = mapping.findForward(Constants.SUCCESS);
			}
		}
		return actionForward;
	}
}
