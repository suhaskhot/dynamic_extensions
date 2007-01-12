
package edu.common.dynamicextensions.ui.webui.action;

/**
 * This Action class Loads the Primary Information needed for CreateForm.jsp.
 * This will first check if the form object is already present in cache , If yes, it will update
 * the actionForm and If No, It will populate the actionForm with fresh data.  
 * The exception thrown can be of 'Application' type ,in this case the same Screen will be displayed  
 * added with error messages .
 * And The exception thrown can be of 'System' type, in this case user will be directed to Error Page.
 * @author deepti_shelar
 */
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domain.userinterface.ContainmentAssociationControl;
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.LoadFormDefinitionProcessor;
import edu.common.dynamicextensions.ui.webui.actionform.FormDefinitionForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.ui.webui.util.WebUIManager;
import edu.common.dynamicextensions.util.global.Constants;

public class LoadFormDefinitionAction extends BaseDynamicExtensionsAction
{

	/**
	 * This method will call LoadFormDefinitionProcessor to load all the information needed for the form.
	 * It will then forward the action to CreateForm.jsp. 
	 * 
	 * @param mapping ActionMapping mapping
	 * @param form ActionForm form
	 * @param  request HttpServletRequest request
	 * @param response HttpServletResponse response
	 * @return ActionForward forward to next action
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		FormDefinitionForm formDefinitionForm = (FormDefinitionForm) form;
		try
		{
			populateContainerInformation(request, formDefinitionForm);
		}
		catch (Exception e)
		{
			String actionForwardString = catchException(e, request);
			if ((actionForwardString == null) || (actionForwardString.equals("")))
			{
				return mapping.getInputForward();
			}
			return (mapping.findForward(actionForwardString));
		}

		return (mapping.findForward(Constants.SUCCESS));
	}

	/**
	 * @param request
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	private void populateContainerInformation(HttpServletRequest request,
			FormDefinitionForm formDefinitionForm) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		LoadFormDefinitionProcessor loadFormDefinitionProcessor = LoadFormDefinitionProcessor
				.getInstance();
		ContainerInterface container = null;

		String operationMode = formDefinitionForm.getOperationMode();

		String containerIdentifier = request.getParameter("containerIdentifier");
		if (operationMode != null && operationMode.equalsIgnoreCase(Constants.ADD_NEW_FORM))
		{
			loadFormDefinitionProcessor.populateContainerInformation(container, formDefinitionForm);
		}
		else if (operationMode != null && operationMode.equalsIgnoreCase(Constants.EDIT_FORM))
		{
			if (containerIdentifier != null)
			{
				container = loadFormDefinitionProcessor.getContainerForEditing(containerIdentifier);
			}
			else
			{
				container = WebUIManager.getCurrentContainer(request);
			}
			loadFormDefinitionProcessor.populateContainerInformation(container, formDefinitionForm);
		}
		else if (operationMode != null
				&& operationMode.equalsIgnoreCase(Constants.ADD_SUB_FORM_OPR))
		{
			loadFormDefinitionProcessor.initializeSubFormAttributes(formDefinitionForm);
		}
		else
		{
			formDefinitionForm.setOperationMode("");
			//container = (ContainerInterface) CacheManager.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);
			container = WebUIManager.getCurrentContainer(request);
			if (container != null)
			{
				loadFormDefinitionProcessor.populateContainerInformation(container,
						formDefinitionForm);
			}
		}
		EntityGroupInterface entityGroup = (EntityGroupInterface) CacheManager.getObjectFromCache(
				request, Constants.ENTITYGROUP_INTERFACE);
		ContainerInterface cachedContainer = (ContainerInterface) CacheManager.getObjectFromCache(
				request, Constants.CONTAINER_INTERFACE);
		String currentContainerName = (String) CacheManager.getObjectFromCache(request,
				Constants.CURRENT_CONTAINER_NAME);
		loadFormDefinitionProcessor.initializeFormAttributes(entityGroup, cachedContainer,
				currentContainerName, formDefinitionForm);

		//Added container and its child container into cache.
		populateChildFormMapInCache(request, cachedContainer);
	}

	/**
	 * This method populates the child sub-form of the cached parent Container into Cache.
	 * @param parentContainer
	 */
	private void populateChildFormMapInCache(HttpServletRequest request,
			ContainerInterface parentContainer)
	{
		if (parentContainer != null)
		{
			String containerName = parentContainer.getCaption();
			CacheManager.addObjectToCache(request, containerName, parentContainer);

			Collection<ControlInterface> controlsCollection = parentContainer
					.getControlCollection();
			if ((controlsCollection != null) || (!controlsCollection.isEmpty()))
			{
				for (ControlInterface control : controlsCollection)
				{
					if ((control != null) && (control instanceof ContainmentAssociationControl))
					{
						ContainerInterface childContainer = ((ContainmentAssociationControl) control).getContainer();
						populateChildFormMapInCache(request, childContainer);
					}
				}
			}
		}
	}
}
