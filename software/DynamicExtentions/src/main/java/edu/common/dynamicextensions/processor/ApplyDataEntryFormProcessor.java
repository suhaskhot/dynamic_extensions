
package edu.common.dynamicextensions.processor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import edu.common.dynamicextensions.client.DataEditClient;
import edu.common.dynamicextensions.client.DataEntryClient;
import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domain.FileAttributeRecordValue;
import edu.common.dynamicextensions.domain.FileAttributeTypeInformation;
import edu.common.dynamicextensions.domain.FileExtension;
import edu.common.dynamicextensions.domain.integration.AbstractFormContext;
import edu.common.dynamicextensions.domain.userinterface.AbstractContainmentControl;
import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AssociationInterface;
import edu.common.dynamicextensions.domaininterface.AssociationMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AttributeMetadataInterface;
import edu.common.dynamicextensions.domaininterface.AttributeTypeInformationInterface;
import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.AbstractContainmentControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CheckBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ComboBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.FileUploadInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ListBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.MultiSelectInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.SelectInterface;
import edu.common.dynamicextensions.entitymanager.CategoryManager;
import edu.common.dynamicextensions.entitymanager.CategoryManagerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManagerUtil;
import edu.common.dynamicextensions.entitymanager.FileQueryBean;
import edu.common.dynamicextensions.entitymanager.FileUploadManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
import edu.common.dynamicextensions.ui.webui.actionform.DataEntryForm;
import edu.common.dynamicextensions.ui.webui.util.WebUIManagerConstants;
import edu.common.dynamicextensions.util.DataValueMapUtility;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.DEConstants;
import edu.common.dynamicextensions.util.global.Variables;
import edu.wustl.common.beans.SessionDataBean;
import edu.wustl.common.util.global.ApplicationProperties;
import edu.wustl.dao.HibernateDAO;
import edu.wustl.dao.exception.DAOException;

/**
 * This Class populates the DataEntryForm and saves the same into the Database.
 * @author chetan_patil
 */
public class ApplyDataEntryFormProcessor extends BaseDynamicExtensionsProcessor
{

	/**
	 * Default Constructor
	 */

	private Long userId;

	/**
	 * This method returns the instance of ApplyDataEntryFormProcessor.
	 * @return ApplyDataEntryFormProcessor Instance of ApplyDataEntryFormProcessor
	 */
	public static ApplyDataEntryFormProcessor getInstance()
	{
		return new ApplyDataEntryFormProcessor();
	}

	public Map<BaseAbstractAttributeInterface, Object> generateAttributeValueMap(
			ContainerInterface containerInterface, HttpServletRequest request,
			DataEntryForm dataEntryForm, String rowId,
			Map<BaseAbstractAttributeInterface, Object> attributeValueMap,
			Boolean processOneToMany, List<String> errorList) throws FileNotFoundException,
			IOException, DynamicExtensionsSystemException
	{

		List<String> xssViolations = null;
		if (request.getAttribute(DEConstants.VIOLATING_PROPERTY_NAMES) instanceof List
				&& ((List<String>) request.getAttribute(DEConstants.VIOLATING_PROPERTY_NAMES))
						.size() > 0)
		{
			xssViolations = (List<String>) request
					.getAttribute(DEConstants.VIOLATING_PROPERTY_NAMES);
		}
		Collection<ControlInterface> controlCollection = containerInterface
				.getAllControlsUnderSameDisplayLabel();
		for (ControlInterface control : controlCollection)
		{
			if ((control != null) && (control.getBaseAbstractAttribute() != null))
			{
				Integer controlSequenceNumber = control.getSequenceNumber();
				if (controlSequenceNumber != null)
				{
					String controlName = control.getHTMLComponentName();

					if ((rowId != null) && !rowId.equals(""))
					{
						controlName = controlName + "_" + rowId;
					}
					if (xssViolations != null && xssViolations.contains(controlName))
					{
						List<String> placeHolder = new ArrayList<String>();
						placeHolder.add(control.getCaption());
						errorList.add(ApplicationProperties.getValue(
								DEConstants.INVALID_CONTROL_VALUE, placeHolder));
					}
					BaseAbstractAttributeInterface abstractAttribute = control
							.getBaseAbstractAttribute();
					if (abstractAttribute instanceof AttributeMetadataInterface)
					{
						if (abstractAttribute instanceof CategoryAttributeInterface)
						{
							CategoryAttributeInterface categoryAttribute = (CategoryAttributeInterface) abstractAttribute;
							if (categoryAttribute.getAbstractAttribute() instanceof AssociationMetadataInterface)
							{
								collectAssociationValues(request, dataEntryForm, controlName,
										control, attributeValueMap, processOneToMany, errorList);
							}
							else
							{
								collectAttributeValues(request, controlName, control,
										attributeValueMap, errorList);
							}
						}
						else
						{
							collectAttributeValues(request, controlName, control,
									attributeValueMap, errorList);
						}
					}
					else if (abstractAttribute instanceof AssociationMetadataInterface)
					{
						collectAssociationValues(request, dataEntryForm, controlName, control,
								attributeValueMap, processOneToMany, errorList);
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
	 * @param controlName
	 * @param control
	 * @param attributeValueMap
	 * @param errorList
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws DynamicExtensionsSystemException
	 */
	private void collectAttributeValues(HttpServletRequest request, String controlName,
			ControlInterface control,
			Map<BaseAbstractAttributeInterface, Object> attributeValueMap, List<String> errorList)
			throws FileNotFoundException, IOException, DynamicExtensionsSystemException
	{
		BaseAbstractAttributeInterface abstractAttribute = control.getBaseAbstractAttribute();
		Object attributeValue = null;

		if (control instanceof ListBoxInterface)
		{
			String selectedListValue = request.getParameter(controlName);
			attributeValue = selectedListValue;
			attributeValueMap.put(abstractAttribute, attributeValue);
		}
		else if (control instanceof FileUploadInterface)
		{
			String fileName = "";
			String contentType = "";
			long fileId = 0;
			if (request.getParameter(controlName) != null && !"".equals(request.getParameter(controlName)))
			{
				fileId = Long.valueOf(request.getParameter(controlName));
				fileName = request.getParameter(controlName + "_hidden");
				contentType = request.getParameter(controlName + "_contentType");
			}

			if (fileName != null && fileName.length() == 0 || contentType.length() == 0
					|| fileId == 0)
			{
				if (request.getParameter(controlName + "_hidden") == null)
				{
					attributeValueMap.put(abstractAttribute, null);
				}
				else
				{
					attributeValueMap.put(abstractAttribute, control.getValue());
				}
			}
			else
			{
				FileUploadManager fileUploadManager = new FileUploadManager();
				byte[] fileContent = null;
				try
				{
					fileContent = fileUploadManager.getFilefromDatabase(fileId);
					boolean isValidExtension = true;
					isValidExtension = checkValidFormat(control, fileName, errorList);
					if (isValidExtension && ((fileName != null) && !fileName.equals("")))
					{
						FileAttributeRecordValue fileAttributeRecordValue = new FileAttributeRecordValue();
						fileAttributeRecordValue.setFileContent(fileContent);
						fileAttributeRecordValue.setFileName(fileName);
						fileAttributeRecordValue.setContentType(contentType);
						attributeValue = fileAttributeRecordValue;
						attributeValueMap.put(abstractAttribute, attributeValue);
						fileUploadManager.deleteRecord(fileId);
					}
				}
				catch (SQLException e)
				{
					throw new DynamicExtensionsSystemException(
							"Error while fetching file from Database", e);
				}
				catch (DAOException e)
				{
					throw new DynamicExtensionsSystemException(
							"Error while fetching file from Database", e);
				}
			}
		}
		else if (control instanceof ComboBoxInterface)
		{
			String value = request.getParameter("combo" + controlName);
			if ((value != null) && value.equalsIgnoreCase("undefined"))
			{
				value = "1";
			}
			else if (value == null)
			{
				value = ((AttributeMetadataInterface) abstractAttribute).getDefaultValue(null);
			}
			attributeValue = DynamicExtensionsUtility.getEscapedStringValue(value);
			attributeValueMap.put(abstractAttribute, attributeValue);
		}

		else
		{
			String value = request.getParameter(controlName);

			value = DynamicExtensionsUtility.getEscapedStringValue(value);

			if ((value != null) && value.equalsIgnoreCase("undefined"))
			{
				value = "1";
			}
			if ((value != null)
					&& ((value.equalsIgnoreCase(ProcessorConstants.DATE_ONLY_FORMAT))
							|| (value.equalsIgnoreCase(ProcessorConstants.DATE_TIME_FORMAT))
							|| (value.equalsIgnoreCase(ProcessorConstants.MONTH_YEAR_FORMAT)) || (value
							.equalsIgnoreCase(ProcessorConstants.YEAR_ONLY_FORMAT))))
			{
				value = "";
			}
			if (control instanceof CheckBoxInterface)
			{
				if (DynamicExtensionsUtility.isCheckBoxChecked(value))
				{
					//value = "unchecked";
					value = DynamicExtensionsUtility.getValueForCheckBox(true);
				}
				else
				{
					value = DynamicExtensionsUtility.getValueForCheckBox(false);
				}
			}

			attributeValue = value;
			attributeValueMap.put(abstractAttribute, attributeValue);
		}
	}

	/**
	 * This method is used to check for the valid File Extensions.
	 * @param dataEntryForm
	 * @param control
	 * @param selectedFile
	 * @param selectedFileSize
	 * @param errorList
	 * @return true if valid file format, false otherwise
	 */
	private boolean checkValidFormat(ControlInterface control, String selectedFile,
			List<String> errorList)
	{
		String validFileExtension = "";
		String selectedfileExt = "";
		String allFileExtension = "";

		boolean isValidExtension = false;
		if (errorList == null)
		{
			errorList = new ArrayList<String>();
		}

		AttributeTypeInformationInterface attributeTypeInformation = ((AttributeMetadataInterface) control
				.getBaseAbstractAttribute()).getAttributeTypeInformation();

		if (attributeTypeInformation instanceof FileAttributeTypeInformation)
		{
			FileAttributeTypeInformation fileAttibuteInformation = (FileAttributeTypeInformation) attributeTypeInformation;
			Collection<FileExtension> fileExtensionsCollection = fileAttibuteInformation
					.getFileExtensionCollection();

			if ((fileExtensionsCollection == null) || fileExtensionsCollection.isEmpty())
			{
				isValidExtension = true;
			}
			else
			{
				for (FileExtension fileExtensionsIterator : fileExtensionsCollection)
				{
					validFileExtension = fileExtensionsIterator.getFileExtension();
					selectedfileExt = selectedFile.substring(selectedFile.lastIndexOf('.') + 1,
							selectedFile.length());
					allFileExtension = validFileExtension + "," + allFileExtension;

					if (selectedfileExt.equalsIgnoreCase(validFileExtension))
					{
						isValidExtension = true;
						break;
					}

				}
			}
			if (allFileExtension.length() > 0)
			{
				allFileExtension = allFileExtension.substring(0, allFileExtension.length() - 1);
			}
			if (!isValidExtension && !"".equals(allFileExtension))
			{
				List<String> parameterList = new ArrayList<String>();
				parameterList.add(allFileExtension);
				parameterList.add(control.getCaption());
				errorList.add(ApplicationProperties.getValue("app.selectProperFormat",
						parameterList));
			}

		}
		return isValidExtension;
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
	private List<Map<BaseAbstractAttributeInterface, Object>> collectOneToManyContainmentValues(
			HttpServletRequest request, DataEntryForm dataEntryForm, String containerId,
			ControlInterface control,
			List<Map<BaseAbstractAttributeInterface, Object>> oneToManyContainmentValueList,
			List<String> errorList) throws FileNotFoundException, DynamicExtensionsSystemException,
			IOException
	{
		AbstractContainmentControl containmentAssociationControl = (AbstractContainmentControl) control;
		int currentSize = oneToManyContainmentValueList.size();

		String parameterString = containerId + "_rowCount";
		String rowCountString = request.getParameter(parameterString);
		int rowCount = Integer.parseInt(rowCountString);

		for (int counter = 0; counter < rowCount; counter++)
		{
			Map<BaseAbstractAttributeInterface, Object> attributeValueMapForSingleRow = null;

			String counterStr = String.valueOf(counter + 1);
			if (counter < currentSize)
			{
				attributeValueMapForSingleRow = oneToManyContainmentValueList.get(counter);
			}
			else
			{
				attributeValueMapForSingleRow = new HashMap<BaseAbstractAttributeInterface, Object>();
				oneToManyContainmentValueList.add(attributeValueMapForSingleRow);
			}
			generateAttributeValueMap(containmentAssociationControl.getContainer(), request,
					dataEntryForm, counterStr, attributeValueMapForSingleRow, false, errorList);
		}

		return oneToManyContainmentValueList;
	}

	/**
	 *
	 * @param request
	 * @param dataEntryForm
	 * @param controlName
	 * @param control
	 * @param attributeValueMap
	 * @throws DynamicExtensionsSystemException
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void collectAssociationValues(HttpServletRequest request, DataEntryForm dataEntryForm,
			String controlName, ControlInterface control,
			Map<BaseAbstractAttributeInterface, Object> attributeValueMap,
			Boolean processOneToMany, List<String> errorList)
			throws DynamicExtensionsSystemException, FileNotFoundException, IOException
	{
		BaseAbstractAttributeInterface abstractAttribute = control.getBaseAbstractAttribute();
		List<Map<BaseAbstractAttributeInterface, Object>> associationValueMaps = (List<Map<BaseAbstractAttributeInterface, Object>>) attributeValueMap
				.get(abstractAttribute);

		if (associationValueMaps == null)
		{
			associationValueMaps = new ArrayList<Map<BaseAbstractAttributeInterface, Object>>();
		}

		if ((control instanceof AbstractContainmentControlInterface) && processOneToMany)
		{
			AbstractContainmentControlInterface associationControl = (AbstractContainmentControlInterface) control;
			ContainerInterface targetContainer = ((AbstractContainmentControlInterface) control)
					.getContainer();
			if (associationControl.isCardinalityOneToMany())
			{
				associationValueMaps = collectOneToManyContainmentValues(request, dataEntryForm,
						targetContainer.getId().toString(), control, associationValueMaps,
						errorList);
			}
			else
			{
				Map<BaseAbstractAttributeInterface, Object> oneToOneValueMap = null;

				if (!associationValueMaps.isEmpty() && (associationValueMaps.get(0) != null))
				{
					oneToOneValueMap = associationValueMaps.get(0);
				}
				else
				{
					oneToOneValueMap = new HashMap<BaseAbstractAttributeInterface, Object>();
					associationValueMaps.add(oneToOneValueMap);
				}

				generateAttributeValueMap(targetContainer, request, dataEntryForm, "",
						oneToOneValueMap, false, errorList);
			}

			attributeValueMap.put(abstractAttribute, associationValueMaps);
		}
		else if ((control instanceof SelectInterface) || (control instanceof MultiSelectInterface))
		{
			AssociationInterface association = null;
			List valueList = new ArrayList();
			if (control instanceof MultiSelectInterface)
			{
				String[] selectedValues = request.getParameterValues(controlName);
				MultiSelectInterface multiSelectInt = (MultiSelectInterface) control;
				association = multiSelectInt.getBaseAbstractAttributeAssociation();
				if (association != null)
				{
					if (association.getIsCollection())
					{
						if (selectedValues == null)
						{
							valueList.add(new HashMap());

						}
						else
						{
							Collection<AbstractAttributeInterface> attributes = association
									.getTargetEntity().getAllAbstractAttributes();
							Collection<AbstractAttributeInterface> filteredAttributes = EntityManagerUtil
									.filterSystemAttributes(attributes);
							List<AbstractAttributeInterface> attributesList = new ArrayList<AbstractAttributeInterface>(
									filteredAttributes);
							for (String id : selectedValues)
							{
								Map dataMap = new HashMap();
								dataMap.put(attributesList.get(0), id);
								valueList.add(dataMap);
							}
						}
					}
					else
					{
						if (selectedValues != null)
						{
							for (String id : selectedValues)
							{
								Long identifier = Long.valueOf(id.trim());
								valueList.add(identifier);
							}
						}
					}
				}

			}
			else if (control instanceof ComboBoxInterface)
			{
				String selectedValue = request.getParameter(controlName);

				if ((selectedValue != null) && (!selectedValue.trim().equals("")))
				{
					valueList.add(Long.valueOf(selectedValue.trim()));
				}
			}
			if (!valueList.isEmpty())
			{
				attributeValueMap.put(abstractAttribute, valueList);
			}
		}
	}

	/**
	 *
	 * @param attributeValueMap
	 * @return
	 */
	public Map<BaseAbstractAttributeInterface, Object> removeNullValueEntriesFormMap(
			Map<BaseAbstractAttributeInterface, Object> attributeValueMap)
	{
		Set<Map.Entry<BaseAbstractAttributeInterface, Object>> attributeValueSet = attributeValueMap
				.entrySet();
		Iterator attributeValueSetIterator = attributeValueSet.iterator();
		while (attributeValueSetIterator.hasNext())
		{
			Map.Entry<BaseAbstractAttributeInterface, Object> attributeValueEntry = (Map.Entry<BaseAbstractAttributeInterface, Object>) attributeValueSetIterator
					.next();

			Object value = attributeValueEntry.getValue();
			if (value == null)
			{
				attributeValueSetIterator.remove();
			}
			else if (value instanceof List && ((List) value).isEmpty())
			{
				attributeValueSetIterator.remove();

			}
		}
		return attributeValueMap;
	}

	/**
	 * This method will pass the values entered into the controls to EntityManager to insert them in Database.
	 * @param containerInterface The container of who's value of Control are to be populated.
	 * @param attributeValueMap The Map of Attribute and their corresponding values from controls.
	 * @throws DynamicExtensionsApplicationException on Application exception
	 * @throws DynamicExtensionsSystemException on System exception
	 * @return recordIdentifier Record identifier of the last saved record.
	 * @throws MalformedURLException
	 * @deprecated Use {@link #insertDataEntryForm(ContainerInterface,Map<BaseAbstractAttributeInterface, Object>,SessionDataBean)} instead
	 */
	public String insertDataEntryForm(ContainerInterface container,
			Map<BaseAbstractAttributeInterface, Object> attributeValueMap)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException,
			MalformedURLException
	{
		return insertDataEntryForm(container, attributeValueMap, null);
	}

	/**
	 * This method will pass the values entered into the controls to EntityManager to insert them in Database.
	 * @param attributeValueMap The Map of Attribute and their corresponding values from controls.
	 * @param sessionDataBean
	 * @param containerInterface The container of who's value of Control are to be populated.
	 * @throws DynamicExtensionsApplicationException on Application exception
	 * @throws DynamicExtensionsSystemException on System exception
	 * @return recordIdentifier Record identifier of the last saved record.
	 * @throws MalformedURLException
	 */
	public String insertDataEntryForm(ContainerInterface container,
			Map<BaseAbstractAttributeInterface, Object> attributeValueMap,
			SessionDataBean sessionDataBean) throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException, MalformedURLException
	{
		Long recordIdentifier = null;

		if (container.getAbstractEntity() instanceof CategoryEntityInterface)
		{
			CategoryInterface categoryInterface = ((CategoryEntityInterface) container
					.getAbstractEntity()).getCategory();
			CategoryManagerInterface categoryManager = CategoryManager.getInstance();
			Long categoryRecordId = categoryManager.insertData(categoryInterface,
					attributeValueMap, sessionDataBean, userId);
			recordIdentifier = categoryManager.getEntityRecordIdByRootCategoryEntityRecordId(
					categoryRecordId, categoryInterface.getRootCategoryElement()
							.getTableProperties().getName());
		}
		else
		{
			Map map = attributeValueMap;
			EntityManagerInterface entityManagerInterface = EntityManager.getInstance();
			recordIdentifier = entityManagerInterface.insertData((EntityInterface) container
					.getAbstractEntity(), map, null, new ArrayList<FileQueryBean>(),
					sessionDataBean, userId);
		}

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
	 * @throws SQLException
	 * @throws MalformedURLException
	 * @deprecated Use {@link #editDataEntryForm(ContainerInterface,Map<BaseAbstractAttributeInterface, Object>,Long,SessionDataBean)} instead
	 */
	public Boolean editDataEntryForm(ContainerInterface container,
			Map<BaseAbstractAttributeInterface, Object> attributeValueMap, Long recordIdentifier)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException,
			SQLException, MalformedURLException
	{
		return editDataEntryForm(container, attributeValueMap, recordIdentifier, null);
	}

	/**
	 * This method will pass the changed (modified) values entered into the controls to EntityManager to update them in Database.
	 *
	 * @param container the container
	 * @param attributeValueMap the attribute value map
	 * @param recordIdentifier the record identifier
	 * @param sessionDataBean the session data bean
	 * @return true, if edits the data entry form
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws SQLException the SQL exception
	 * @throws MalformedURLException
	 */
	public Boolean editDataEntryForm(ContainerInterface container,
			Map<BaseAbstractAttributeInterface, Object> attributeValueMap, Long recordIdentifier,
			SessionDataBean sessionDataBean) throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException, SQLException, MalformedURLException
	{
		boolean isEdited;

		if (container.getAbstractEntity() instanceof EntityInterface)
		{
			EntityManagerInterface entityManager = EntityManager.getInstance();
			EntityInterface entity = (Entity) container.getAbstractEntity();
			//Correct this:
			Map map = attributeValueMap;

			isEdited = entityManager.editData(entity, map, recordIdentifier, null,
					new ArrayList<FileQueryBean>(), sessionDataBean, userId);
		}
		else
		{
			CategoryInterface categoryInterface = ((CategoryEntityInterface) container
					.getAbstractEntity()).getCategory();
			CategoryManagerInterface categoryManager = CategoryManager.getInstance();
			Long categoryRecordId = categoryManager.getRootCategoryEntityRecordIdByEntityRecordId(
					recordIdentifier, categoryInterface.getRootCategoryElement()
							.getTableProperties().getName());
			isEdited = CategoryManager.getInstance().editData(
					(CategoryEntityInterface) container.getAbstractEntity(), attributeValueMap,
					categoryRecordId, sessionDataBean, userId);
		}
		return isEdited;
	}

	public Long getUserId()
	{
		return userId;
	}

	public void setUserId(Long userId)
	{
		this.userId = userId;
	}

	/**
	 * Generate attribute value map.
	 *
	 * @param containerIdSet the container id set
	 * @param request the request
	 *
	 * @return the map< abstract form context, map< base abstract attribute interface, object>>
	 *
	 * @throws FileNotFoundException the file not found exception
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 */
	public Map<AbstractFormContext, Map<BaseAbstractAttributeInterface, Object>> generateAttributeValueMap(
			Set<AbstractFormContext> containerIdSet, HttpServletRequest request)
			throws FileNotFoundException, DynamicExtensionsSystemException, IOException,
			DynamicExtensionsApplicationException
	{
		Map<AbstractFormContext, Map<BaseAbstractAttributeInterface, Object>> containerIdValueMap = new HashMap<AbstractFormContext, Map<BaseAbstractAttributeInterface, Object>>();
		for (AbstractFormContext abstractFrmContext : containerIdSet)
		{

			ContainerInterface containerInterface = DynamicExtensionsUtility
					.getClonedContainerFromCache(abstractFrmContext.getContainerId().toString());
			containerInterface.getContainerValueMap().clear();
			DynamicExtensionsUtility.cleanContainerControlsValue(containerInterface);

			int sequence = 1;
			ControlsUtility.updateSequencesOfBaseContainers(containerInterface, sequence);

			List<String> errorList = new ArrayList<String>();
			Map<BaseAbstractAttributeInterface, Object> attributeValueMap = new HashMap<BaseAbstractAttributeInterface, Object>();
			attributeValueMap = generateAttributeValueMap(containerInterface, request, null, "",
					attributeValueMap, true, errorList);
			containerIdValueMap.put(abstractFrmContext, attributeValueMap);
		}
		return containerIdValueMap;

	}

	/**
	 * Insert update data entry form collection.
	 *
	 * @param contextDataValueMap the context data value map
	 * @param contextRecordIdMap the context record id map
	 * @param hibernateDao the hibernate dao
	 *
	 * @return the map< abstract form context, long>
	 *
	 * @throws DynamicExtensionsSystemException the dynamic extensions system exception
	 * @throws DynamicExtensionsApplicationException the dynamic extensions application exception
	 * @throws SQLException the SQL exception
	 * @throws MalformedURLException
	 * @throws NumberFormatException
	 */
	public Map<AbstractFormContext, Long> insertUpdateDataEntryFormCollection(
			Map<AbstractFormContext, Map<BaseAbstractAttributeInterface, Object>> contextDataValueMap,
			Map<AbstractFormContext, Long> contextRecordIdMap, HibernateDAO hibernateDao)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException,
			SQLException, MalformedURLException
	{
		Map<AbstractFormContext, Long> frmContextVsRecordIdMap = new HashMap<AbstractFormContext, Long>();
		for (AbstractFormContext abstractFrmContext : contextDataValueMap.keySet())
		{
			ContainerInterface containerInterface = DynamicExtensionsUtility
					.getClonedContainerFromCache(abstractFrmContext.getContainerId().toString());
			containerInterface.getContainerValueMap().clear();
			DynamicExtensionsUtility.cleanContainerControlsValue(containerInterface);

			int sequence = 1;
			ControlsUtility.updateSequencesOfBaseContainers(containerInterface, sequence);
			DataValueMapUtility.updateDataValueMapForDataEntry(contextDataValueMap
					.get(abstractFrmContext), containerInterface);

			//Map dataValueMap = contextDataValueMap.get(abstractFrmContext);
			//EntityManagerInterface entityManager = EntityManager.getInstance();
			if (contextRecordIdMap.get(abstractFrmContext) == null)
			{
				//Add case
				/*Long recordIdentifier = entityManager.insertData(
						(EntityInterface) containerInterface.getAbstractEntity(), dataValueMap,
						hibernateDao, new ArrayList<FileQueryBean>(), userId);*/

				Long recordIdentifier = Long.valueOf(insertDataEntryForm(containerInterface,
						contextDataValueMap.get(abstractFrmContext), null));
				frmContextVsRecordIdMap.put(abstractFrmContext, recordIdentifier);
			}
			else
			{
				//Edit Case
				boolean isEdited = editDataEntryForm(containerInterface, contextDataValueMap.get(abstractFrmContext),
						contextRecordIdMap.get(abstractFrmContext), null);
				/*EntityInterface entity = (Entity) containerInterface.getAbstractEntity();
				boolean isEdited = entityManager.editData(entity, dataValueMap, contextRecordIdMap
						.get(abstractFrmContext), hibernateDao, new ArrayList<FileQueryBean>(),
						userId);*/
				if (isEdited)
				{
					frmContextVsRecordIdMap.put(abstractFrmContext, contextRecordIdMap
							.get(abstractFrmContext));
				}
			}
		}
		return frmContextVsRecordIdMap;
	}
}
