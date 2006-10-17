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
import edu.common.dynamicextensions.ui.interfaces.ContainerInformationInterface;

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
	 * This method populates the given ContainerInterface using the given ContainerInformationInterface.
	 * @param containerInterface Instance of containerInterface which is populated using the informationInterface.
	 * @param ContainerInformationInterface Instance of ContainerInformationInterface which is used to populate the containerInterface.
	 */
	public void populateContainerInterface(ContainerInterface containerInterface, ContainerInformationInterface containerInformationInterface)
	{
		containerInterface.setButtonCss(containerInformationInterface.getButtonCss());
		containerInterface.setCaption(containerInformationInterface.getFormCaption());
		containerInterface.setMainTableCss(containerInformationInterface.getMainTableCss());
		containerInterface.setRequiredFieldIndicatior(containerInformationInterface.getRequiredFieldIndicatior());
		containerInterface.setRequiredFieldWarningMessage(containerInformationInterface.getRequiredFieldWarningMessage());
		containerInterface.setTitleCss(containerInformationInterface.getTitleCss());
	}

	/**
	 * /**
	 * This method will populate the containerInformationInterface using the containerInterface so that the 
	 * information of the Container can be shown on the user page using the EntityInformationInterface.
	 * @param containerInterface Instance of containerInterface from which to populate the informationInterface.
	 * @param containerInformationInterface Instance of containerInformationInterface which will be populated using 
	 * the first parameter that is ContainerInterface.
	 */

	public void populateContainerInformation(ContainerInterface containerInterface, ContainerInformationInterface containerInformationInterface)
	{
		containerInformationInterface.setButtonCss(containerInterface.getButtonCss());
		containerInformationInterface.setFormCaption(containerInterface.getCaption());
		containerInformationInterface.setMainTableCss(containerInterface.getMainTableCss());
		containerInformationInterface.setRequiredFieldIndicatior(containerInterface.getRequiredFieldIndicatior());
		containerInformationInterface.setRequiredFieldWarningMessage(containerInterface.getRequiredFieldWarningMessage());
		containerInformationInterface.setTitleCss(containerInterface.getTitleCss());
	}

}
