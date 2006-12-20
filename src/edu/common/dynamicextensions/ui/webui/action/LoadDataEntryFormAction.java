
package edu.common.dynamicextensions.ui.webui.action;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.LoadDataEntryFormProcessor;
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
		String mode = request.getParameter(WebUIManagerConstants.MODE_PARAM_NAME);

		String callBackURL = request.getParameter(WebUIManagerConstants.CALLBACK_URL_PARAM_NAME);

		if (callBackURL != null && !callBackURL.equals(""))
		{
			CacheManager.clearCache(request);
			CacheManager.addObjectToCache(request, Constants.CALLBACK_URL, callBackURL);
		}

		ContainerInterface containerInterface = (ContainerInterface) CacheManager
				.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);
		String containerIdentifier = request.getParameter("containerIdentifier");
		if (containerInterface == null || containerIdentifier != null)
		{
			containerInterface = DynamicExtensionsUtility
					.getContainerByIdentifier(containerIdentifier);

		}

		String recordId = request.getParameter("recordId");

		Stack containerStack = (Stack) CacheManager.getObjectFromCache(request,
				Constants.CONTAINER_STACK);
		Stack valueMapStack = (Stack) CacheManager.getObjectFromCache(request,
				Constants.VALUE_MAP_STACK);
		if (containerStack == null)
		{
			containerStack = new Stack<ContainerInterface>();
			CacheManager.addObjectToCache(request, Constants.CONTAINER_STACK, containerStack);
			valueMapStack = new Stack<Map>();
			CacheManager.addObjectToCache(request, Constants.VALUE_MAP_STACK, valueMapStack);
			UserInterfaceiUtility.addContainerInfo(containerStack, containerInterface,
					valueMapStack, new HashMap<AbstractAttributeInterface, Object>());
		}

		LoadDataEntryFormProcessor loadDataEntryFormProcessor = LoadDataEntryFormProcessor
				.getInstance();
		containerInterface = loadDataEntryFormProcessor.loadDataEntryForm(
				(AbstractActionForm) form, (ContainerInterface)containerStack.peek(),(Map)valueMapStack.peek() ,recordId, mode);
		CacheManager.addObjectToCache(request, Constants.CONTAINER_INTERFACE, containerInterface);
		return mapping.findForward("Success");
	}

}
