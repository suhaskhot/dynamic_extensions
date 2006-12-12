
package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.processor.ApplyFormDefinitionProcessor;
import edu.common.dynamicextensions.ui.webui.actionform.FormDefinitionForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * This Action class handles two situations , 
 * 1. When user selects 'Next' from createForm.jsp. This time a call to ApplyFormDefinitionProcessor
 * will just create an entity and populate it with actionform's data. This entity is then saved to cache.
 * 2. When user selects 'Save' from createForm.jsp. This time a call to ApplyFormDefinitionProcessor 
 * will create an entity and will save it to database. This entity is then saved to cache.
 * The exception thrown can be of 'Application' type ,in this case the same Screen will be displayed  
 * added with error messages .
 * And The exception thrown can be of 'System' type, in this case user will be directed to Error Page.
 * @author deepti_shelar
 */
public class ApplyFormDefinitionAction extends BaseDynamicExtensionsAction
{
	/**
	 * This method will call ApplyFormDefinitionProcessor for actually updating the cache and then
	 * forwards the action to either BuildForm.jsp or CreateForm.jsp depending on the Operation.
	 *
	 * @param mapping ActionMapping mapping
	 * @param form ActionForm form
	 * @param  request HttpServletRequest request
	 * @param response HttpServletResponse response
	 * @return ActionForward forward to next action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		FormDefinitionForm formDefinitionForm = (FormDefinitionForm) form;
		String target = "";
		String operation = "";
		ContainerInterface containerInterface = (ContainerInterface) CacheManager.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);
		ApplyFormDefinitionProcessor applyFormDefinitionProcessor = ApplyFormDefinitionProcessor.getInstance();
		boolean saveEntity = true;
		try
		{
			operation = formDefinitionForm.getOperation();
			if ((operation != null) && (operation.equalsIgnoreCase(Constants.BUILD_FORM)))
			{
				saveEntity = false;
				target = Constants.BUILD_FORM;
			}
			else if ((operation != null) && (operation.equalsIgnoreCase(Constants.EDIT_FORM)))
			{// When we click on Next or Save in Edit Mode 
				saveEntity = true;
				target = Constants.BUILD_FORM;
			}
			else
			{// When we click on save in Add Mode
				saveEntity = true;
				target = Constants.SHOW_DYNAMIC_EXTENSIONS_HOMEPAGE;
			}

			if(saveEntity == true)
			{
				saveMessages(request, getSuccessMessage(formDefinitionForm));
			}
			EntityGroupInterface entityGroup =(EntityGroupInterface) CacheManager.getObjectFromCache(request, Constants.ENTITYGROUP_INTERFACE);
			
			//If not in Edit mode, then save the Container in Database and Add the same to the Cache manager.
			if (operation != null && !operation.equalsIgnoreCase(Constants.EDIT_FORM))
			{
				containerInterface = applyFormDefinitionProcessor.addEntityToContainer(containerInterface, formDefinitionForm, saveEntity,entityGroup);
				CacheManager.addObjectToCache(request, Constants.CONTAINER_INTERFACE, containerInterface);
			}
						
		}
		catch (Exception e)
		{
			target = catchException(e, request);
		}
		return mapping.findForward(target);
	}

	/**
	 * 
	 * @param formDefinitionForm actionform
	 * @return ActionMessages Messages
	 */
	private ActionMessages getSuccessMessage(FormDefinitionForm formDefinitionForm)
	{
		ActionMessages actionMessages = new ActionMessages();
		String formName = formDefinitionForm.getFormName();
		actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("app.entitySaveSuccessMessage", formName));
		return actionMessages;
	}
}
