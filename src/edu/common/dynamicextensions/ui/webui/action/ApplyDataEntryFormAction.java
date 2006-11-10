
package edu.common.dynamicextensions.ui.webui.action;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.processor.ApplyDataEntryFormProcessor;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.ui.webui.util.WebUIManager;
import edu.common.dynamicextensions.util.global.Constants;
import edu.common.dynamicextensions.validation.ValidatorUtil;

/**
 * This class is invoked on Apply click action.
 * It populates the Attribute values entered in the dynamically generated controls. * 
 * @author chetan_patil
 */
public class ApplyDataEntryFormAction extends BaseDynamicExtensionsAction
{
	/**
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		ContainerInterface containerInterface = (ContainerInterface) CacheManager.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);
		Collection<ControlInterface> controlCollection = containerInterface.getControlCollection();
		AbstractAttributeInterface abstractAttributeInterface = null;
		//ControlInterface controlInterface = null;

		Map<AbstractAttributeInterface, String> attributeValueMap = new LinkedHashMap<AbstractAttributeInterface, String>();
		String value = null;

		for (int sequence = 1; sequence <= controlCollection.size(); sequence++)
		{
			value = request.getParameter("Control_" + sequence);
			for (ControlInterface controlInterface : controlCollection)
			{
				if (sequence == controlInterface.getSequenceNumber().intValue())
				{
					abstractAttributeInterface = controlInterface.getAbstractAttribute();
					attributeValueMap.put(abstractAttributeInterface, value);
					break;
				}
			}
		}

		ApplyDataEntryFormProcessor applyDataEntryFormProcessor = ApplyDataEntryFormProcessor.getInstance();

		List<String> errorList;
		try 
		{
			errorList = ValidatorUtil.validateEntity(attributeValueMap);
			Long recordIdentifier = new Long(-1);

			if (errorList.size() != 0)
			{
				saveMessages(request, getErrorMessages(errorList));
			}
			else
			{
				recordIdentifier = applyDataEntryFormProcessor.insertDataEntryForm(containerInterface, attributeValueMap);
				saveMessages(request, getSuccessMessage());
			}

			String calllbackURL = (String) CacheManager.getObjectFromCache(request, Constants.CALLBACK_URL);
			if (calllbackURL != null && !calllbackURL.equals(""))
			{
				calllbackURL = calllbackURL + "?" + WebUIManager.getRecordIdentifierParameterName() + "=" + recordIdentifier ;
				CacheManager.clearCache(request);
				response.sendRedirect(calllbackURL);
				return null;
			}
		}
		catch (Exception e)
		{
			String actionForwardString = catchException(e, request);
			return (mapping.findForward(actionForwardString));
		}
		return (mapping.findForward(Constants.SUCCESS));
	}

	/**
	 * Get messages for successful save of entity
	 * @return ActionMessages ActionMessages
	 */
	private ActionMessages getSuccessMessage()
	{
		ActionMessages actionMessages = new ActionMessages();
		actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("app.successfulDataInsertionMessage"));
		return actionMessages;
	}

	/*public ActionErrors getErrorMessages(List<String> errorList)
	 {
	 ActionErrors errors = new ActionErrors();
	 for (String errorMessage : errorList)
	 {
	 errors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorMessage));
	 }
	 return errors;
	 }*/
}
