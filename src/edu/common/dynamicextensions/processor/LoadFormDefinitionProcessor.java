
package edu.common.dynamicextensions.processor;

/**
 * This processor class mainly helps the action class to call the related Object driven processors 
 * to update the Actionforms by retriving data form Cache.
 * @author deepti_shelar
 *
 */
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.ui.interfaces.ContainerUIBeanInterface;
import edu.common.dynamicextensions.ui.interfaces.EntityUIBeanInterface;

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
	 * @param entityUIBeanInterface
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
	 * @param containerInterface
	 * @param containerUIBeanInterface
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
}
