
package edu.common.dynamicextensions.ui.webui.action;

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

		LoadDataEntryFormProcessor loadDataEntryFormProcessor = LoadDataEntryFormProcessor
				.getInstance();

		String recordId = request.getParameter("recordId");
		Map<AbstractAttributeInterface, Object> recordMap = loadDataEntryFormProcessor
				.getValueMapFromRecordId(containerInterface.getEntity(), recordId);

		Stack<ContainerInterface> containerStack = (Stack<ContainerInterface>) CacheManager
				.getObjectFromCache(request, Constants.CONTAINER_STACK);
		Stack<Map<AbstractAttributeInterface, Object>> valueMapStack = (Stack<Map<AbstractAttributeInterface, Object>>) CacheManager
				.getObjectFromCache(request, Constants.VALUE_MAP_STACK);
		String dataEntryOperation = dataEntryForm.getDataEntryOperation();
		String showFormPreview = dataEntryForm.getShowFormPreview();
		if (containerStack == null)
		{
			containerStack = new Stack<ContainerInterface>();
			CacheManager.addObjectToCache(request, Constants.CONTAINER_STACK, containerStack);
			valueMapStack = new Stack<Map<AbstractAttributeInterface, Object>>();
			CacheManager.addObjectToCache(request, Constants.VALUE_MAP_STACK, valueMapStack);
			UserInterfaceiUtility.addContainerInfo(containerStack, containerInterface,
					valueMapStack, recordMap);
		}
		else if (dataEntryOperation != null
				&& dataEntryOperation.equalsIgnoreCase("insertChildData"))
		{

			Map<AbstractAttributeInterface, Object> containerValueMap = valueMapStack.peek();
			String childContainerId = dataEntryForm.getChildContainerId();
			ContainmentAssociationControl associationControl = UserInterfaceiUtility
					.getAssociationControl((ContainerInterface) containerStack.peek(),
							childContainerId);
			ContainerInterface childContainer = associationControl.getContainer();

			Map<AbstractAttributeInterface, Object> childContainerValueMap = null;
			
			if (showFormPreview.equals("true"))
			{
				childContainerValueMap = new HashMap<AbstractAttributeInterface, Object>();
			}
			else
			{
				AssociationInterface association = (AssociationInterface) associationControl
						.getAbstractAttribute();
				List<Map<AbstractAttributeInterface, Object>> childContainerValueMapList = (List<Map<AbstractAttributeInterface, Object>>) containerValueMap
						.get(association);

				if (UserInterfaceiUtility.isCardinalityOneToMany(associationControl))
				{
					childContainerValueMap = childContainerValueMapList.get(Integer
							.parseInt(dataEntryForm.getChildRowId()) - 1);
				}
				else
				{
					childContainerValueMap = childContainerValueMapList.get(0);
				}

			}

			UserInterfaceiUtility.addContainerInfo(containerStack, childContainer, valueMapStack,
					childContainerValueMap);
		}
		else if (dataEntryOperation != null
				&& dataEntryOperation.equalsIgnoreCase("insertParentData"))
		{
			UserInterfaceiUtility.removeContainerInfo(containerStack, valueMapStack);
		}

		if (containerStack.size() > 0)
		{
			loadDataEntryFormProcessor.loadDataEntryForm((AbstractActionForm) form, containerStack
				.peek(), valueMapStack.peek(), mode, recordId);
		}
		else
		{
			CacheManager.addObjectToCache(request, Constants.CONTAINER_STACK, null);
			CacheManager.addObjectToCache(request, Constants.VALUE_MAP_STACK, null);
			return mapping.findForward("LoadFormControls");
		}
		

		if (containerStack.size() > 1)
		{
			dataEntryForm.setIsTopLevelEntity(false);
		}
		else
		{
			dataEntryForm.setIsTopLevelEntity(true);
		}
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

}
