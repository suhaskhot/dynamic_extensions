/**
 * 
 */

package edu.common.dynamicextensions.processor;

import java.util.Map;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;

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

	public Boolean editDataEntryForm(ContainerInterface container, Map<AbstractAttributeInterface, ?> attributeValueMap, Long recordIdentifier)
			throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
		EntityManagerInterface entityManager = EntityManager.getInstance();
		EntityInterface entity = container.getEntity();
		Boolean edited = entityManager.editData(entity, attributeValueMap, recordIdentifier);
		return edited;
	}

}
