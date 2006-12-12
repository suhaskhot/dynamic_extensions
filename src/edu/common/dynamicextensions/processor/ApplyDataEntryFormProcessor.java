/**
 * 
 */

package edu.common.dynamicextensions.processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.upload.FormFile;

import edu.common.dynamicextensions.domain.FileAttributeRecordValue;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.RoleInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CheckBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ComboBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.FileUploadInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ListBoxInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.actionform.DataEntryForm;
import edu.common.dynamicextensions.util.global.Constants.AssociationType;

/**
 * This Class populates the DataEntryForm and saves the same into the Database.
 * @author chetan_patil
 */
public class ApplyDataEntryFormProcessor extends BaseDynamicExtensionsProcessor
{
	/**
	 * Default Constructor
	 */
	public ApplyDataEntryFormProcessor()
	{
	}

	/**
	 * This method returns the instance of ApplyDataEntryFormProcessor.
	 * @return ApplyDataEntryFormProcessor Instance of ApplyDataEntryFormProcessor
	 */
	public static ApplyDataEntryFormProcessor getInstance()
	{
		return new ApplyDataEntryFormProcessor();
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
	public Map<AbstractAttributeInterface, Object> generateAttributeValueMap(ContainerInterface container, HttpServletRequest request,
			DataEntryForm dataEntryForm) throws FileNotFoundException, IOException, DynamicExtensionsSystemException
	{
		Collection<ControlInterface> controlCollection = container.getControlCollection();

		Map<AbstractAttributeInterface, Object> attributeValueMap = new LinkedHashMap<AbstractAttributeInterface, Object>();

		for (int sequence = 1; sequence <= controlCollection.size(); sequence++)
		{
			for (ControlInterface control : controlCollection)
			{
				if (control != null)
				{
					Integer controlSequenceNumber = control.getSequenceNumber();
					if (controlSequenceNumber != null && (sequence == controlSequenceNumber.intValue()))
					{
						String controlSequence = container.getId() + "_" + sequence;
						AbstractAttributeInterface abstractAttribute = control.getAbstractAttribute();
						if (abstractAttribute instanceof AttributeInterface)
						{
							collectAttributeValues(request, dataEntryForm, controlSequence, control, attributeValueMap);
						}
						else if (abstractAttribute instanceof AssociationInterface)
						{
							collectAssociationValues(request, dataEntryForm, controlSequence, control, attributeValueMap);
						}
						break;
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
	private void collectAssociationValues(HttpServletRequest request, DataEntryForm dataEntryForm, String sequence, ControlInterface control,
			Map<AbstractAttributeInterface, Object> attributeValueMap) throws DynamicExtensionsSystemException, FileNotFoundException, IOException
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		AbstractAttributeInterface abstractAttribute = control.getAbstractAttribute();
		AssociationInterface association = (AssociationInterface)abstractAttribute;
		RoleInterface role = association.getTargetRole();
		if (role != null)
		{
			AssociationType associationType = role.getAssociationsType();
			if (associationType != null)
			{
				String associationTypeName = associationType.getValue();
				if (associationTypeName.equals(AssociationType.CONTAINTMENT))
				{
					EntityInterface targetEntity = association.getTargetEntity();
					ContainerInterface targetContainer = entityManager.getContainerByEntityIdentifier(targetEntity.getId());
					Collection<ControlInterface> targetControlCollection = targetContainer.getControlCollection();
					if (targetControlCollection != null && !targetControlCollection.isEmpty())
					{
						Map<AbstractAttributeInterface, Object> tempAttributeValueMap = generateAttributeValueMap(targetContainer, request, dataEntryForm);
						List<Map<AbstractAttributeInterface, Object>> tempList = new ArrayList<Map<AbstractAttributeInterface, Object>>();
						tempList.add(tempAttributeValueMap);
						attributeValueMap.put(abstractAttribute, tempList);
					}
				}
				else if(associationTypeName.equals(AssociationType.ASSOCIATION))
				{
					List<Long> valueList = new Vector<Long>();
					if (control instanceof ListBoxInterface)
					{
						String[] selectedValues = (String[]) request.getParameterValues("Control_" + sequence);
						if (selectedValues != null)
						{
							for (String id: selectedValues)
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
		}
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
	private void collectAttributeValues(HttpServletRequest request, DataEntryForm dataEntryForm, String sequence, ControlInterface control,
			Map<AbstractAttributeInterface, Object> attributeValueMap) throws FileNotFoundException, IOException
	{
		AbstractAttributeInterface abstractAttribute = control.getAbstractAttribute();
		Object attributeValue = null;

		if (control instanceof ListBoxInterface)
		{
			List<String> valueList = new ArrayList<String>();
			String[] selectedListValues = (String[]) request.getParameterValues("Control_" + sequence);

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
	
	/**
	 * 
	 * @param attributeValueMap
	 * @return
	 */
	public Map<AbstractAttributeInterface, Object> removeNullValueEntriesFormMap(Map<AbstractAttributeInterface, Object> attributeValueMap)
	{
		Map<AbstractAttributeInterface, Object> newAttributeValueMap = new LinkedHashMap<AbstractAttributeInterface, Object>();
		Set<Map.Entry<AbstractAttributeInterface, Object>> newAttributeValueSet = newAttributeValueMap.entrySet();

		Set<Map.Entry<AbstractAttributeInterface, Object>> attributeValueSet = attributeValueMap.entrySet();
		for (Map.Entry<AbstractAttributeInterface, Object> attributeValueEntry : attributeValueSet)
		{
			Object value = attributeValueEntry.getValue();
			if (value != null)
			{
				newAttributeValueSet.add(attributeValueEntry);
			}
		}
		return newAttributeValueMap;
	}

	/**
	 * This method will pass the values entered into the controls to EntityManager to insert them in Database.
	 * @param containerInterface The container of who's value of Control are to be populated. 
	 * @param attributeValueMap The Map of Attribute and their corresponding values from controls.
	 * @throws DynamicExtensionsApplicationException on Application exception
	 * @throws DynamicExtensionsSystemException on System exception
	 * @return recordIdentifier Record identifier of the last saved record. 
	 */
	public String insertDataEntryForm(ContainerInterface container, Map<AbstractAttributeInterface, Object> attributeValueMap)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		EntityInterface entity = container.getEntity();
		Long recordIdentifier = entityManager.insertData(entity, attributeValueMap);
		return recordIdentifier.toString();
	}

	/**
	 * This method will pass the changed (modified) values entered into the controls to EntityManager to update them in Database.
	 * @param container
	 * @param attributeValueMap
	 * @param recordIdentifier
	 * @return
	 * @throws DynamicExtensionsApplicationException
	 * @throws DynamicExtensionsSystemException
	 */
	public Boolean editDataEntryForm(ContainerInterface container, Map<AbstractAttributeInterface, Object> attributeValueMap, Long recordIdentifier)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		EntityInterface entity = container.getEntity();
		Boolean edited = entityManager.editData(entity, attributeValueMap, recordIdentifier);
		return edited;
	}

}
