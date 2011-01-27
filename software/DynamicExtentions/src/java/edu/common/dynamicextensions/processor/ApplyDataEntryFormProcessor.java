
package edu.common.dynamicextensions.processor;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.common.dynamicextensions.domain.Entity;
import edu.common.dynamicextensions.domaininterface.BaseAbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.CategoryEntityInterface;
import edu.common.dynamicextensions.domaininterface.CategoryInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.CategoryManager;
import edu.common.dynamicextensions.entitymanager.CategoryManagerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.entitymanager.FileQueryBean;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.common.beans.SessionDataBean;

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
	 * @deprecated Use {@link #insertDataEntryForm(ContainerInterface,Map<BaseAbstractAttributeInterface, Object>,SessionDataBean)} instead
	 */
	public String insertDataEntryForm(ContainerInterface container,
			Map<BaseAbstractAttributeInterface, Object> attributeValueMap)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
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
	 */
	public String insertDataEntryForm(ContainerInterface container,
			Map<BaseAbstractAttributeInterface, Object> attributeValueMap, SessionDataBean sessionDataBean)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		Long recordIdentifier = null;
		//quick fix: common manager interface should be used here
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
					.getAbstractEntity(), map, null, new ArrayList<FileQueryBean>(), sessionDataBean, userId);
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
	 * @deprecated Use {@link #editDataEntryForm(ContainerInterface,Map<BaseAbstractAttributeInterface, Object>,Long,SessionDataBean)} instead
	 */
	public Boolean editDataEntryForm(ContainerInterface container,
			Map<BaseAbstractAttributeInterface, Object> attributeValueMap, Long recordIdentifier)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException,
			SQLException
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
	 */
	public Boolean editDataEntryForm(ContainerInterface container,
			Map<BaseAbstractAttributeInterface, Object> attributeValueMap, Long recordIdentifier,
			SessionDataBean sessionDataBean) throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException, SQLException
	{
		boolean isEdited;
		//Quick fix:
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

}
