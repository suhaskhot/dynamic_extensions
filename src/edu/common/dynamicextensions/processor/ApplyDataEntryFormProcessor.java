/**
 * 
 */

package edu.common.dynamicextensions.processor;

import java.util.Map;

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
	public Long insertDataEntryForm(ContainerInterface containerInterface, Map attributeValueMap) throws DynamicExtensionsApplicationException,
			DynamicExtensionsSystemException
	{
			EntityManagerInterface entityManager = EntityManager.getInstance();
			EntityInterface entityInterface = containerInterface.getEntity();
			Long recordIdentifier = entityManager.insertData(entityInterface, attributeValueMap);
			return recordIdentifier;
	}

}
