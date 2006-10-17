
package edu.common.dynamicextensions.processor;

/**
 * This processor class mainly helps the action class to call the related Object driven processors 
 * to update the Actionforms by retriving data form Cache.
 * @author deepti_shelar
 */
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.webui.actionform.FormDefinitionForm;

public class ApplyFormDefinitionProcessor extends BaseDynamicExtensionsProcessor
{
	/**
	 * Default Constructor.
	 */
	protected ApplyFormDefinitionProcessor()
	{
	}

	/**
	 * Returns the instance of ApplyFormDefinitionProcessor.
	 * @return ApplyFormDefinitionProcessor
	 */
	public static ApplyFormDefinitionProcessor getInstance()
	{
		return new ApplyFormDefinitionProcessor();
	}

	/**
	 * This method creates a Container if not present in cache. Then it will call to ContainerProcessor will
	 * populate this Object with the data from actionform.Then EntityProcessor's methods will be called to either create and Populate
	 * or create and save the entity, Then finally this entity is added to the container. 
	 * @param containerInterface
	 * @param actionForm
	 * @param IsActionSave
	 * @return ContainerInterface
	 */
	public ContainerInterface addEntityToContainer(ContainerInterface containerInterface, FormDefinitionForm actionForm, boolean IsActionSave)
	{
		ContainerProcessor containerProcessor = ContainerProcessor.getInstance();
		if (containerInterface == null)
		{
			containerInterface = containerProcessor.createContainer();
		}
		containerProcessor.populateContainerInterface(containerInterface, actionForm);
		EntityProcessor entityProcessor = EntityProcessor.getInstance();
		EntityInterface entityInterface = containerInterface.getEntity();
		if (entityInterface == null)
		{
			try
			{
				if (IsActionSave)
				{
					entityInterface = entityProcessor.createAndSaveEntity(actionForm);
				}
				else
					entityInterface = entityProcessor.createAndPopulateEntity(actionForm);
			}
			catch (DynamicExtensionsSystemException e)
			{
				e.printStackTrace();
			}
			catch (DynamicExtensionsApplicationException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			entityProcessor.populateEntity(actionForm,entityInterface);
		}
		containerInterface.setEntity(entityInterface);
		return containerInterface;
	}
}
