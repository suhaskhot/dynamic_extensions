
package edu.common.dynamicextensions.processor;

/**
 * This processor class mainly helps the action class to call the related Object driven processors 
 * to update the Actionforms by retriving data form Cache.
 * @author deepti_shelar
 */
import edu.common.dynamicextensions.domaininterface.EntityGroupInterface;
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
	 * @param containerInterface : Container object 
	 * @param actionForm : Form object
	 * @param isActionSave : flag stating whether the object is to be saved to DB
	 * @return ContainerInterface : Container object
	 * @throws DynamicExtensionsApplicationException :Exception thrown by Entity Manager 
	 * @throws DynamicExtensionsSystemException :Exception thrown by Entity Manager
	 */
	public ContainerInterface addEntityToContainer(ContainerInterface containerInterface, FormDefinitionForm actionForm, boolean isActionSave)
			throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
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
			entityInterface = entityProcessor.createAndPopulateEntity(actionForm);
		}
		else
		{
			entityProcessor.populateEntity(actionForm, entityInterface);
		}
		containerInterface.setEntity(entityInterface);
		if (isActionSave)
		{
			containerProcessor.saveContainer(containerInterface);
		}
		else
		{
			containerProcessor.populateContainerInterface(containerInterface, actionForm);
		}
		return containerInterface;
	}

	/**
	 * @param entityGroup : Entity Group containing entity
	 * @param entity : Entity to be associated
	 */
	public void associateEntityToGroup(EntityGroupInterface entityGroup, EntityInterface entity)
	{
		if((entityGroup!=null)&&(entity!=null))
		{
			entityGroup.addEntity(entity);
			entity.addEntityGroupInterface(entityGroup);
		}
	}
}
