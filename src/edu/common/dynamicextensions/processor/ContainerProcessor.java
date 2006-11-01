
package edu.common.dynamicextensions.processor;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.interfaces.ContainerUIBeanInterface;
import edu.wustl.common.util.Utility;

/**
 *<p>Title: ContainerProcessor</p>
 *<p>Description:  This class acts as a utility class which processes tne container.
 * 1. It creates a new container.
 * 2. Populates the containerInterface (Cache) object.
 * 3. Populates the information to UIBean taking form Cache.
 * This processor class is a POJO and not a framework specific class so it can be used by 
 * all types of presentation layers.  </p>
 *@author Deepti Shelar
 *@version 1.0
 */
public class ContainerProcessor extends BaseDynamicExtensionsProcessor
{
	/**
	 * This is a singleton class so we have a protected constructor , We are providing getInstance method 
	 * to return the ContainerProcessor's instance.
	 */
	protected ContainerProcessor()
	{

	}

	/**
	 * this method gets the new instance of the ControlProcessor to the caller.
	 * @return ControlProcessor ControlProcessor instance
	 */
	public static ContainerProcessor getInstance()
	{
		return new ContainerProcessor();
	}

	/**
	 * This method returns empty domain object of ContainerInterface.
	 * @return ContainerInterface
	 */
	public ContainerInterface createContainer()
	{
		return DomainObjectFactory.getInstance().createContainer();
	}

	/**
	 * This method populates the given ContainerInterface using the given ContainerUIBeanInterface.
	 * @param containerInterface Instance of containerInterface which is populated using the informationInterface.
	 * @param ContainerUIBeanInterface Instance of ContainerUIBeanInterface which is used to populate the containerInterface.
	 */
	public void populateContainerInterface(ContainerInterface containerInterface, ContainerUIBeanInterface containerUIBeanInterface)
	{
		if (containerInterface != null && containerUIBeanInterface != null)
		{
			containerInterface.setButtonCss(containerUIBeanInterface.getButtonCss());
			containerInterface.setCaption(containerUIBeanInterface.getFormName());
			containerInterface.setMainTableCss(containerUIBeanInterface.getMainTableCss());
			containerInterface.setRequiredFieldIndicatior(containerUIBeanInterface.getRequiredFieldIndicatior());
			containerInterface.setRequiredFieldWarningMessage(containerUIBeanInterface.getRequiredFieldWarningMessage());
			containerInterface.setTitleCss(containerUIBeanInterface.getTitleCss());
		}
	}

	/**
	 * /**
	 * This method will populate the containerInformationInterface using the containerInterface so that the 
	 * information of the Container can be shown on the user page using the EntityUIBeanInterface.
	 * @param containerInterface Instance of containerInterface from which to populate the informationInterface.
	 * @param containerUIBeanInterface Instance of containerInformationInterface which will be populated using 
	 * the first parameter that is ContainerInterface.
	 */

	public void populateContainerUIBeanInterface(ContainerInterface containerInterface, ContainerUIBeanInterface containerUIBeanInterface)
	{
		if (containerInterface != null && containerUIBeanInterface != null)
		{
			containerUIBeanInterface.setButtonCss(Utility.toString(containerInterface.getButtonCss()));
			//containerUIBeanInterface.setFormCaption(Utility.toString(containerInterface.getCaption()));
			containerUIBeanInterface.setFormName(containerInterface.getCaption());
			containerUIBeanInterface.setMainTableCss(Utility.toString(containerInterface.getMainTableCss()));
			containerUIBeanInterface.setRequiredFieldIndicatior(Utility.toString(containerInterface.getRequiredFieldIndicatior()));
			containerUIBeanInterface.setRequiredFieldWarningMessage(Utility.toString(containerInterface.getRequiredFieldWarningMessage()));
			containerUIBeanInterface.setTitleCss(Utility.toString(containerInterface.getTitleCss()));
		}
	}

	public void saveContainer(ContainerInterface containerInterface) throws DynamicExtensionsApplicationException, DynamicExtensionsSystemException
	{
	if (containerInterface.getId() == null) {        
    EntityManager.getInstance().createContainer(containerInterface);
    } else {
        EntityManager.getInstance().editContainer(containerInterface);
    }
                        
	}
}
