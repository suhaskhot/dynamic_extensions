
package edu.common.dynamicextensions.processor;

/**
 * This processor class mainly helps the action class to call the related Object driven processors 
 * to update the Actionforms by retriving data form Cache.
 * @author deepti_shelar
 *
 */
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.ui.interfaces.ContainerInformationInterface;
import edu.common.dynamicextensions.ui.interfaces.EntityInformationInterface;

public class LoadFormDefinitionProcessor extends BaseDynamicExtensionsProcessor
{
	/**
	 * Protected constructor for LoadFormDefinitionProcessor
	 *
	 */
	protected LoadFormDefinitionProcessor()
	{

	}

	/**
	 * this method gets the new instance of the LoadFormDefinitionProcessor to the caller.
	 * @return LoadFormDefinitionProcessor LoadFormDefinitionProcessor instance
	 */
	public static LoadFormDefinitionProcessor getInstance()
	{
		return new LoadFormDefinitionProcessor();
	}

	/**
	 * A call to EntityProcessor will update the actionform with the data from cacheObject. 
	 * @param entityInterface
	 * @param entityInformationInterface
	 */
	private void populateEntityInformation(EntityInterface entityInterface, EntityInformationInterface entityInformationInterface)
	{
		if (entityInterface != null)
		{
			EntityProcessor entityProcessor = EntityProcessor.getInstance();
			entityProcessor.populateEntityInformation(entityInterface, entityInformationInterface);
		}
	}

	/**
	 * A call to ContainerProcessor will update the actionform with the data from cacheObject. 
	 * @param containerInterface
	 * @param containerInformationInterface
	 */
	public void populateContainerInformation(ContainerInterface containerInterface, ContainerInformationInterface containerInformationInterface)
	{
		if (containerInterface != null)
		{
			ContainerProcessor containerProcessor = ContainerProcessor.getInstance();
			populateEntityInformation(containerInterface.getEntity(), ((EntityInformationInterface) containerInformationInterface));
			containerProcessor.populateContainerInformation(containerInterface, containerInformationInterface);
		}
	}
}
