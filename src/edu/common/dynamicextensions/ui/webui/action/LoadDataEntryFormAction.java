
package edu.common.dynamicextensions.ui.webui.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domain.userinterface.ContainmentAssociationControl;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.LoadDataEntryFormProcessor;
import edu.common.dynamicextensions.ui.webui.actionform.DataEntryForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.ui.webui.util.UserInterfaceiUtility;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;

/**
 * @author sujay_narkar, chetan_patil
 *
 */
public class LoadDataEntryFormAction extends BaseDynamicExtensionsAction
{

	/**
	 * @param mapping ActionMapping mapping
	 * @param form ActionForm form
	 * @param  request HttpServletRequest request
	 * @param response HttpServletResponse response
	 * @return ActionForward forward to next action
	 * @throws DynamicExtensionsSystemException dynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException dynamicExtensionsApplicationException
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		DataEntryForm dataEntryForm = (DataEntryForm) form;

		String mode = request.getParameter(WebUIManagerConstants.MODE_PARAM_NAME);

		String callBackURL = request.getParameter(WebUIManagerConstants.CALLBACK_URL_PARAM_NAME);

		if (callBackURL != null && !callBackURL.equals(""))
		{
			CacheManager.clearCache(request);
			CacheManager.addObjectToCache(request, Constants.CALLBACK_URL, callBackURL);
		}

		ContainerInterface containerInterface = (ContainerInterface) CacheManager
				.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);

		String containerIdentifier = getContainerId(request);
		if (containerIdentifier != null || containerInterface == null)
		{
			UserInterfaceiUtility.clearContainerStack(request);

			containerInterface = DynamicExtensionsUtility
					.getContainerByIdentifier(containerIdentifier);
			CacheManager.addObjectToCache(request, Constants.CONTAINER_INTERFACE,
					containerInterface);
		}

		String recordId = request.getParameter("recordId");

		Stack containerStack = (Stack) CacheManager.getObjectFromCache(request,
				Constants.CONTAINER_STACK);
		Stack valueMapStack = (Stack) CacheManager.getObjectFromCache(request,
				Constants.VALUE_MAP_STACK);
		String dataEntryOperation = dataEntryForm.getDataEntryOperation();
		if (containerStack == null)
		{
			containerStack = new Stack<ContainerInterface>();
			CacheManager.addObjectToCache(request, Constants.CONTAINER_STACK, containerStack);
			valueMapStack = new Stack<Map>();
			CacheManager.addObjectToCache(request, Constants.VALUE_MAP_STACK, valueMapStack);
			UserInterfaceiUtility.addContainerInfo(containerStack, containerInterface,
					valueMapStack, new HashMap<AbstractAttributeInterface, Object>());
		}
		else if (dataEntryOperation != null
				&& dataEntryOperation.equalsIgnoreCase("insertChildData"))
		{
			Map containerValueMap = (Map) valueMapStack.peek();
			String childContainerId = dataEntryForm.getChildContainerId();
			ContainmentAssociationControl associationControl = getAssociationControl(
					(ContainerInterface) containerStack.peek(), childContainerId);
			ContainerInterface childContainer = associationControl.getContainer();
			AssociationInterface association = (AssociationInterface) associationControl
					.getAbstractAttribute();
			List childContainerValueMapList = (List) containerValueMap.get(association);
			Map childContainerValueMap = (Map) childContainerValueMapList.get(Integer
					.parseInt(dataEntryForm.getChildRowId()) - 1);
			UserInterfaceiUtility.addContainerInfo(containerStack, childContainer, valueMapStack,
					childContainerValueMap);
		}
		else if (dataEntryOperation != null
				&& dataEntryOperation.equalsIgnoreCase("insertParentData"))
		{
			UserInterfaceiUtility.removeContainerInfo(containerStack,valueMapStack);
		}

		LoadDataEntryFormProcessor loadDataEntryFormProcessor = LoadDataEntryFormProcessor
				.getInstance();

		loadDataEntryFormProcessor.loadDataEntryForm((AbstractActionForm) form,
				(ContainerInterface) containerStack.peek(), (Map) valueMapStack.peek(), recordId,
				mode);

		clearFormValues(dataEntryForm);

		return mapping.findForward("Success");
	}

	/**
	 * @param request
	 * @return
	 */
	private String getContainerId(HttpServletRequest request)
	{
		String id = "";
		id = request.getParameter("containerIdentifier");
		if (id == null || id.equals(""))
		{
			id = (String) request.getAttribute("containerIdentifier");
		}
		return id;
	}

	/**
	 * @param form
	 */
	private void clearFormValues(ActionForm form)
	{
		DataEntryForm dataEntryForm = (DataEntryForm) form;
		dataEntryForm.setChildRowId("");
		dataEntryForm.setChildContainerId("");
	}

	/**
	 * 
	 * @param containerInterface
	 * @param childContainerId
	 * @return
	 */
	private ContainmentAssociationControl getAssociationControl(
			ContainerInterface containerInterface, String childContainerId)
	{
		Collection<ControlInterface> controlCollection = containerInterface.getControlCollection();
		for (ControlInterface control : controlCollection)
		{
			if (control instanceof ContainmentAssociationControl)
			{
				ContainmentAssociationControl containmentAssociationControl = (ContainmentAssociationControl) control;
				String containmentAssociationControlId = containmentAssociationControl
						.getContainer().getId().toString();
				if (containmentAssociationControlId.equals(childContainerId))
				{
					return containmentAssociationControl;
				}
			}
		}

		return null;
	}
}
