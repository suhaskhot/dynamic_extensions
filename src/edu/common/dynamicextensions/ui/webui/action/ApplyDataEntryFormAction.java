
package edu.common.dynamicextensions.ui.webui.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;

import edu.common.dynamicextensions.domain.FileAttributeRecordValue;
import edu.common.dynamicextensions.domain.userinterface.ContainmentAssociationControl;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CheckBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ComboBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainmentAssociationControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.FileUploadInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ListBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.SelectInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.ApplyDataEntryFormProcessor;
import edu.common.dynamicextensions.ui.webui.actionform.DataEntryForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.ui.webui.util.UserInterfaceiUtility;
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

		Stack containerStack = (Stack) CacheManager.getObjectFromCache(request,
				Constants.CONTAINER_STACK);
		Stack valueMapStack = (Stack) CacheManager.getObjectFromCache(request,
				Constants.VALUE_MAP_STACK);
		ContainerInterface containerInterface = (ContainerInterface) containerStack.peek();
		Map<AbstractAttributeInterface, Object> valueMap = (Map) valueMapStack.peek();

		ApplyDataEntryFormProcessor applyDataEntryFormProcessor = ApplyDataEntryFormProcessor
				.getInstance();
		
		ActionForward forward = null;

		try
		{
			valueMap = generateAttributeValueMap(
					containerInterface, request, dataEntryForm, "", valueMap, true);

			List<String> errorList = ValidatorUtil.validateEntity(valueMap );

			if (errorList.size() != 0)
			{
				//saveErrors(request, getErrorMessages(errorList));
				dataEntryForm.setErrorList(errorList);
				return (mapping.findForward(Constants.SUCCESS));
			}
			else
			{
			applyDataEntryFormProcessor
						.removeNullValueEntriesFormMap(valueMap );
			}
			
			valueMap = (Map<AbstractAttributeInterface, Object>) valueMapStack.firstElement();
			containerInterface = (ContainerInterface) containerStack.firstElement();
			
			String dataEntryOperation = dataEntryForm.getDataEntryOperation();
			if(dataEntryOperation != null && dataEntryOperation.equals("insertChildData"))
			{
				return  mapping.findForward("loadChildContainer");
			} 
			else if(dataEntryOperation != null && dataEntryOperation.equals("insertParentData")) {
				return  mapping.findForward("loadParentContainer");
			}
				
			
			String recordIdentifier = dataEntryForm.getRecordIdentifier();
			
			if (recordIdentifier != null && !recordIdentifier.equals(""))
			{
				Boolean edited = applyDataEntryFormProcessor.editDataEntryForm(containerInterface,
						valueMap , Long.valueOf(recordIdentifier));
				if (edited.booleanValue())
				{
					saveMessages(request, getSuccessMessage());
				}
			}
			else
			{
				recordIdentifier = applyDataEntryFormProcessor.insertDataEntryForm(
						containerInterface, valueMap );
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
			if ((actionForwardString == null) || (actionForwardString.equals("")))
			{
				return mapping.getInputForward();
			}
			return (mapping.findForward(actionForwardString));
		}
		
		UserInterfaceiUtility.clearContainerStack(request);
		
		
		return (mapping.findForward(Constants.SUCCESS));
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
	 * @param container
	 * @param request
	 * @param dataEntryForm
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws DynamicExtensionsSystemException 
	 */
	public Map<AbstractAttributeInterface, Object> generateAttributeValueMap(
			ContainerInterface containerInterface, HttpServletRequest request,
			DataEntryForm dataEntryForm, String rowId,
			Map<AbstractAttributeInterface, Object> attributeValueMap, Boolean processOneToMany)
			throws FileNotFoundException, IOException, DynamicExtensionsSystemException
	{

		Collection<ControlInterface> controlCollection = containerInterface.getControlCollection();
		for (ControlInterface control : controlCollection)
		{
			if (control != null)
			{
				Integer controlSequenceNumber = control.getSequenceNumber();
				if (controlSequenceNumber != null)
				{
					String controlSequence = containerInterface.getId() + "_"
							+ controlSequenceNumber;
					if (rowId != null && !rowId.equals(""))
					{
						controlSequence = controlSequence + "_" + rowId;
					}
					AbstractAttributeInterface abstractAttribute = control.getAbstractAttribute();
					if (abstractAttribute instanceof AttributeInterface)
					{
						collectAttributeValues(request, dataEntryForm, controlSequence, control,
								attributeValueMap);
					}
					else if (abstractAttribute instanceof AssociationInterface)
					{
						collectAssociationValues(request, dataEntryForm, controlSequence, control,
								attributeValueMap, processOneToMany);
					}
				}
			}
		}

		return attributeValueMap;
	}

	/**
	 * 
	 * @param request
	 * @param dataEntryForm
	 * @param sequence
	 * @param control
	 * @param attributeValueMap
	 * @throws DynamicExtensionsSystemException 
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private void collectAssociationValues(HttpServletRequest request, DataEntryForm dataEntryForm,
			String sequence, ControlInterface control,
			Map<AbstractAttributeInterface, Object> attributeValueMap, Boolean processOneToMany)
			throws DynamicExtensionsSystemException, FileNotFoundException, IOException
	{
		AbstractAttributeInterface abstractAttribute = control.getAbstractAttribute();

		if (control instanceof ContainmentAssociationControlInterface && processOneToMany)
		{
			ContainmentAssociationControlInterface containmentAssociationControlInterface = (ContainmentAssociationControlInterface) control;
			ContainerInterface targetContainer = ((ContainmentAssociationControlInterface) control)
					.getContainer();
			if (containmentAssociationControlInterface.isCardinalityOneToMany())
			{
				attributeValueMap.put(abstractAttribute, collectOneToManyContainmentValues(request,
						dataEntryForm, targetContainer.getId().toString(), control));
			}
			else
			{
				Map<AbstractAttributeInterface, Object> tempAttributeValueMap = new HashMap<AbstractAttributeInterface, Object>();
				generateAttributeValueMap(targetContainer, request, dataEntryForm, "",tempAttributeValueMap,true);
				List<Map<AbstractAttributeInterface, Object>> tempList = new ArrayList<Map<AbstractAttributeInterface, Object>>();
				tempList.add(tempAttributeValueMap);
				attributeValueMap.put(abstractAttribute, tempList);
			}
		}
		else if (control instanceof SelectInterface)
		{
			List<Long> valueList = new Vector<Long>();
			if (control instanceof ListBoxInterface)
			{
				String[] selectedValues = (String[]) request.getParameterValues("Control_"
						+ sequence);
				if (selectedValues != null)
				{
					for (String id : selectedValues)
					{
						Long identifier = new Long(id.trim());
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
	}

	/**
	 * 
	 * @param request
	 * @param dataEntryForm
	 * @param sequence
	 * @param control
	 * @param attributeValueMap
	 * @throws IOException 
	 * @throws DynamicExtensionsSystemException 
	 * @throws FileNotFoundException 
	 */
	private List<Map<AbstractAttributeInterface, Object>> collectOneToManyContainmentValues(
			HttpServletRequest request, DataEntryForm dataEntryForm, String containerId,
			ControlInterface control) throws FileNotFoundException,
			DynamicExtensionsSystemException, IOException
	{
		List<Map<AbstractAttributeInterface, Object>> oneToManyContainmentValueList = new ArrayList<Map<AbstractAttributeInterface, Object>>();
		ContainmentAssociationControl containmentAssociationControl = (ContainmentAssociationControl) control;

		String parameterString = containerId + "_rowCount";
		String rowCountString = request.getParameter(parameterString);
		int rowCount = Integer.parseInt(rowCountString);
		Map<AbstractAttributeInterface, Object> attributeValueMap = null;
		for (int counter = 1; counter <= rowCount; counter++)
		{
			attributeValueMap = generateAttributeValueMap(containmentAssociationControl
					.getContainer(), request, dataEntryForm, counter + "",new HashMap<AbstractAttributeInterface, Object>(), false);
			oneToManyContainmentValueList.add(attributeValueMap);
		}

		return oneToManyContainmentValueList;
	}

	/**
	 * 
	 * @param request
	 * @param dataEntryForm
	 * @param sequence
	 * @param control
	 * @param attributeValueMap
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void collectAttributeValues(HttpServletRequest request, DataEntryForm dataEntryForm,
			String sequence, ControlInterface control,
			Map<AbstractAttributeInterface, Object> attributeValueMap)
			throws FileNotFoundException, IOException
	{
		AbstractAttributeInterface abstractAttribute = control.getAbstractAttribute();
		Object attributeValue = null;

		if (control instanceof ListBoxInterface)
		{
			List<String> valueList = new ArrayList<String>();
			String[] selectedListValues = (String[]) request.getParameterValues("Control_"
					+ sequence);

			if (selectedListValues != null)
			{
				for (int counter = 0; counter < selectedListValues.length; counter++)
				{
					valueList.add(selectedListValues[counter]);
				}
			}
			attributeValue = valueList;
		}
		else if (control instanceof FileUploadInterface)
		{
			FormFile formFile = null;
			formFile = (FormFile) dataEntryForm.getValue("Control_" + sequence);
			FileAttributeRecordValue fileAttributeRecordValue = new FileAttributeRecordValue();
			fileAttributeRecordValue.setFileContent(formFile.getFileData());
			fileAttributeRecordValue.setFileName(formFile.getFileName());
			fileAttributeRecordValue.setContentType(formFile.getContentType());
			attributeValue = fileAttributeRecordValue;
		}
		else
		{
			String value = request.getParameter("Control_" + sequence);
			if (control instanceof CheckBoxInterface)
			{
				if (value == null || !value.trim().equals("true"))
				{
					value = "false";
				}
			}
			attributeValue = value;
		}
		attributeValueMap.put(abstractAttribute, attributeValue);
	}

}
