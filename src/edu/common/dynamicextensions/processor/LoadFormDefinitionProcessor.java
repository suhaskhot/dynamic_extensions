
package edu.common.dynamicextensions.processor;

/**
 * This processor class mainly helps the action class to call the related Object driven processors 
 * to update the Actionforms by retriving data form Cache.
 * @author deepti_shelar
 * @author chetan_patil
 * @version 2.0
 */
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.interfaces.ContainerUIBeanInterface;
import edu.common.dynamicextensions.ui.interfaces.EntityUIBeanInterface;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;

public class LoadFormDefinitionProcessor extends BaseDynamicExtensionsProcessor
{
	/**
	 * Protected constructor for LoadFormDefinitionProcessor
	 */
	protected LoadFormDefinitionProcessor()
	{
	}

	/**
	 * This method returns the new instance of the LoadFormDefinitionProcessor.
	 * @return the new instance of the LoadFormDefinitionProcessor.
	 */
	public static LoadFormDefinitionProcessor getInstance()
	{
		return new LoadFormDefinitionProcessor();
	}

	/**
	 * A call to EntityProcessor will update the actionform with the data from cacheObject. 
	 * @param entityInterface : Entity Interface Domain Object 
	 * @param entityUIBeanInterface : UI Bean object containing entity information added by user on UI
	 */
	private void populateEntityInformation(EntityInterface entityInterface, EntityUIBeanInterface entityUIBeanInterface)
	{
		if (entityInterface != null)
		{
			EntityProcessor entityProcessor = EntityProcessor.getInstance();
			entityProcessor.populateEntityUIBeanInterface(entityInterface, entityUIBeanInterface);
		}
	}

	/**
	 * A call to ContainerProcessor will update the actionform with the data from cacheObject. 
	 * @param containerInterface : Container interface
	 * @param containerUIBeanInterface : container UI Bean Interface object containing information added by user
	 */
	public void populateContainerInformation(ContainerInterface containerInterface, ContainerUIBeanInterface containerUIBeanInterface)
	{
		ContainerProcessor containerProcessor = ContainerProcessor.getInstance();
		if (containerInterface == null)
		{
			containerInterface = containerProcessor.createContainer();
		}
		populateEntityInformation(containerInterface.getEntity(), ((EntityUIBeanInterface) containerUIBeanInterface));
		containerProcessor.populateContainerUIBeanInterface(containerInterface, containerUIBeanInterface);
	}

	/**
	 * This method returns the populated Container instance form the database having corresponding Container identifier.
	 * @param containerIdentifier the Identifier of the Conatiner to be fetched from database.
	 * @throws DynamicExtensionsApplicationException if Application level exception occurs.
	 * @throws DynamicExtensionsSystemException if System level or run-time exception occurs.
	 * @return the populated Container instance.
	 */
	public ContainerInterface getContainerForEditing(String containerIdentifier) throws DynamicExtensionsSystemException,
			DynamicExtensionsApplicationException
	{
		return DynamicExtensionsUtility.getContainerByIdentifier(containerIdentifier);
	}

}
