/**
 *<p>Title: ContainerProcessor</p>
 *<p>Description:  This class acts as a utility class which processes tne container in various ways as needed
 *and provides methods to the UI layer.This processor class is a POJO and not a framework specific class so 
 *it can be used by all types of presentation layers.  </p>
 *@author Deepti Shelar
 *@version 1.0
 */

package edu.common.dynamicextensions.processor;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.ui.interfaces.ContainerUIBeanInterface;

public class ContainerProcessor extends BaseDynamicExtensionsProcessor
{
	/**
	 * Protected constructor for ControlProcessor
	 *
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
		containerInterface.setButtonCss(containerUIBeanInterface.getButtonCss());
		containerInterface.setCaption(containerUIBeanInterface.getFormCaption());
		containerInterface.setMainTableCss(containerUIBeanInterface.getMainTableCss());
		containerInterface.setRequiredFieldIndicatior(containerUIBeanInterface.getRequiredFieldIndicatior());
		containerInterface.setRequiredFieldWarningMessage(containerUIBeanInterface.getRequiredFieldWarningMessage());
		containerInterface.setTitleCss(containerUIBeanInterface.getTitleCss());
	}

	/**
	 * /**
	 * This method will populate the containerInformationInterface using the containerInterface so that the 
	 * information of the Container can be shown on the user page using the EntityUIBeanInterface.
	 * @param containerInterface Instance of containerInterface from which to populate the informationInterface.
	 * @param containerUIBeanInterface Instance of containerInformationInterface which will be populated using 
	 * the first parameter that is ContainerInterface.
	 */

	public void populateContainerInformation(ContainerInterface containerInterface, ContainerUIBeanInterface containerUIBeanInterface)
	{
		containerUIBeanInterface.setButtonCss(containerInterface.getButtonCss());
		containerUIBeanInterface.setFormCaption(containerInterface.getCaption());
		containerUIBeanInterface.setMainTableCss(containerInterface.getMainTableCss());
		containerUIBeanInterface.setRequiredFieldIndicatior(containerInterface.getRequiredFieldIndicatior());
		containerUIBeanInterface.setRequiredFieldWarningMessage(containerInterface.getRequiredFieldWarningMessage());
		containerUIBeanInterface.setTitleCss(containerInterface.getTitleCss());
	}

}
