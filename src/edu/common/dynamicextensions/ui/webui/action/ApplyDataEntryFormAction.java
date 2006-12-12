
package edu.common.dynamicextensions.ui.webui.action;

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
import edu.common.dynamicextensions.processor.ApplyDataEntryFormProcessor;
import edu.common.dynamicextensions.ui.webui.actionform.DataEntryForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.ui.webui.util.WebUIManager;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
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
		DataEntryForm dataEntryForm = (DataEntryForm) form;
		ContainerInterface container = (ContainerInterface) CacheManager.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);

		ApplyDataEntryFormProcessor applyDataEntryFormProcessor = ApplyDataEntryFormProcessor.getInstance();
		
		try
		{
			Map<AbstractAttributeInterface, Object> attributeValueMap = applyDataEntryFormProcessor.generateAttributeValueMap(container, request,
					dataEntryForm);

			List<String> errorList = ValidatorUtil.validateEntity(attributeValueMap);
			
			if (errorList.size() != 0)
			{
				//saveErrors(request, getErrorMessages(errorList));
				dataEntryForm.setErrorList(errorList);
				return (mapping.findForward(Constants.SUCCESS));
			}
			else
			{
				attributeValueMap = applyDataEntryFormProcessor.removeNullValueEntriesFormMap(attributeValueMap);
			}

			String recordIdentifier = dataEntryForm.getRecordIdentifier();
			if (recordIdentifier != null && !recordIdentifier.equals(""))
			{
				Boolean edited = applyDataEntryFormProcessor.editDataEntryForm(container, attributeValueMap, Long.valueOf(recordIdentifier));
				if (edited.booleanValue())
				{
					saveMessages(request, getSuccessMessage());
				}
			}
			else
			{
				recordIdentifier = applyDataEntryFormProcessor.insertDataEntryForm(container, attributeValueMap);
				saveMessages(request, getSuccessMessage());
			}

			String calllbackURL = (String) CacheManager.getObjectFromCache(request, Constants.CALLBACK_URL);
			if (calllbackURL != null && !calllbackURL.equals(""))
			{
				calllbackURL = calllbackURL + "?" + WebUIManager.getRecordIdentifierParameterName() + "=" + recordIdentifier + "&"
						+ WebUIManager.getOperationStatusParameterName() + "=" + WebUIManagerConstants.SUCCESS;
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

//	/**
//	 * 
//	 * @param fileName
//	 * @return
//	 * @throws IOException
//	 */
//	private FileAttributeRecordValue getFileAttributeRecordValue(String fileName) throws IOException
//	{
//		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
//		FileAttributeRecordValue fileAttributeRecordValue = domainObjectFactory.createFileAttributeRecordValue();
//
//		byte[] fileContent = ControlsUtility.getBytesFromFile(new File(fileName));
//		fileAttributeRecordValue.setFileContent(fileContent);
//		fileAttributeRecordValue.setFileName(fileName);
//
//		return fileAttributeRecordValue;
//	}

}
