
package edu.common.dynamicextensions.ui.webui.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domain.FileAttributeRecordValue;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CheckBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ComboBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.FileUploadInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ListBoxInterface;
import edu.common.dynamicextensions.processor.ApplyDataEntryFormProcessor;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
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
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	{
		DataEntryForm dataEntryForm = (DataEntryForm) form;
		ContainerInterface containerInterface = (ContainerInterface) CacheManager
				.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);
		Collection<ControlInterface> controlCollection = containerInterface.getControlCollection();

		AbstractAttributeInterface abstractAttribute = null;
		Map<AbstractAttributeInterface, Object> attributeValueMap = new LinkedHashMap<AbstractAttributeInterface, Object>();

		try
		{
			for (int sequence = 1; sequence <= controlCollection.size(); sequence++)
			{
				for (ControlInterface control : controlCollection)
				{
					if (control != null)
					{
						Integer controlSequenceNumber = control.getSequenceNumber();
						if (controlSequenceNumber != null
								&& (sequence == controlSequenceNumber.intValue()))
						{
							abstractAttribute = control.getAbstractAttribute();
							if (abstractAttribute instanceof AttributeInterface)
							{
								collectAttributeValues(request, dataEntryForm, sequence, control,
										attributeValueMap);
							}
							else if (abstractAttribute instanceof AssociationInterface)
							{
								collectAssociationValues(request, dataEntryForm, sequence, control,
										attributeValueMap);
							}
							break;
						}
					}
				}
			}

			ApplyDataEntryFormProcessor applyDataEntryFormProcessor = ApplyDataEntryFormProcessor
					.getInstance();

			List<String> errorList = null;
			errorList = ValidatorUtil.validateEntity(attributeValueMap);
			String recordIdentifier = dataEntryForm.getRecordIdentifier();

			if (errorList.size() != 0)
			{
				//saveErrors(request, getErrorMessages(errorList));
				dataEntryForm.setErrorList(errorList);
				return (mapping.findForward(Constants.SUCCESS));
			}
			else if (recordIdentifier != null && !recordIdentifier.equals(""))
			{
				Boolean edited = applyDataEntryFormProcessor.editDataEntryForm(containerInterface,
						attributeValueMap, Long.valueOf(recordIdentifier));
				if (edited.booleanValue())
				{
					saveMessages(request, getSuccessMessage());
				}
			}
			else
			{
				recordIdentifier = applyDataEntryFormProcessor.insertDataEntryForm(
						containerInterface, attributeValueMap);
				saveMessages(request, getSuccessMessage());
			}

			String calllbackURL = (String) CacheManager.getObjectFromCache(request,
					Constants.CALLBACK_URL);
			if (calllbackURL != null && !calllbackURL.equals(""))
			{
				calllbackURL = calllbackURL + "?" + WebUIManager.getRecordIdentifierParameterName()
						+ "=" + recordIdentifier + "&"
						+ WebUIManager.getOperationStatusParameterName() + "="
						+ WebUIManagerConstants.SUCCESS;
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

	private void collectAssociationValues(HttpServletRequest request, DataEntryForm dataEntryForm,
			int sequence, ControlInterface control,
			Map<AbstractAttributeInterface, Object> attributeValueMap)
	{
		AbstractAttributeInterface abstractAttribute = control.getAbstractAttribute();

		List<Long> valueList = new Vector<Long>();
		if (control instanceof ListBoxInterface)
		{
			String[] selectedValues = (String[]) request.getParameterValues("Control_" + sequence);
			if (selectedValues != null)
			{
				for (int counter = 0; counter < selectedValues.length; counter++)
				{
					Long identifier = new Long(selectedValues[counter].trim());
					valueList.add(identifier);
				}
			}
		}
		else if (control instanceof ComboBoxInterface)
		{
			String value = request.getParameter("Control_" + sequence);
			valueList.add(new Long(value.trim()));
		}
		attributeValueMap.put(abstractAttribute, valueList);
	}

	private void collectAttributeValues(HttpServletRequest request, DataEntryForm dataEntryForm,
			int sequence, ControlInterface control,
			Map<AbstractAttributeInterface, Object> attributeValueMap)
			throws FileNotFoundException, IOException
	{
		AbstractAttributeInterface abstractAttribute = control.getAbstractAttribute();

		List<String> valueList = null;

		String value = null;
		if (control instanceof ListBoxInterface)
		{
			String[] selectedListValues = (String[]) request.getParameterValues("Control_"
					+ sequence);
			valueList = new ArrayList<String>();
			if (selectedListValues != null)
			{
				for (int counter = 0; counter < selectedListValues.length; counter++)
				{
					valueList.add(selectedListValues[counter]);
				}
			}
			attributeValueMap.put(abstractAttribute, valueList);
		}
		else if (control instanceof FileUploadInterface)
		{
			FormFile formFile = null;
			formFile = (FormFile) dataEntryForm.getValue("Control_" + sequence);
			FileAttributeRecordValue fileAttributeRecordValue = new FileAttributeRecordValue();
			fileAttributeRecordValue.setFileContent(formFile.getFileData());
			fileAttributeRecordValue.setFileName(formFile.getFileName());
			fileAttributeRecordValue.setContentType(formFile.getContentType());
			attributeValueMap.put(abstractAttribute, fileAttributeRecordValue);
		}
		else
		{
			value = request.getParameter("Control_" + sequence);
			if (control instanceof CheckBoxInterface)
			{
				if (value == null || !value.trim().equals("true"))
				{
					value = "false";
				}

			}
			attributeValueMap.put(abstractAttribute, value);
		}
	}

	/**
	 * Get messages for successful save of entity
	 * @return ActionMessages ActionMessages
	 */
	private ActionMessages getSuccessMessage()
	{
		ActionMessages actionMessages = new ActionMessages();
		actionMessages.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage(
				"app.successfulDataInsertionMessage"));
		return actionMessages;
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	private FileAttributeRecordValue getFileAttributeRecordValue(String fileName)
			throws IOException
	{
		DomainObjectFactory domainObjectFactory = DomainObjectFactory.getInstance();
		FileAttributeRecordValue fileAttributeRecordValue = domainObjectFactory
				.createFileAttributeRecordValue();

		byte[] fileContent = ControlsUtility.getBytesFromFile(new File(fileName));
		fileAttributeRecordValue.setFileContent(fileContent);
		fileAttributeRecordValue.setFileName(fileName);

		return fileAttributeRecordValue;
	}

}
