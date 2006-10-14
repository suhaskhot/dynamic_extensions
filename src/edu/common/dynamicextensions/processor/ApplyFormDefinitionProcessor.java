package edu.common.dynamicextensions.processor;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.interfaces.ContainerInformationInterface;
import edu.common.dynamicextensions.ui.interfaces.EntityInformationInterface;
import edu.common.dynamicextensions.ui.webui.actionform.FormDefinitionForm;

public class ApplyFormDefinitionProcessor extends BaseDynamicExtensionsProcessor{
	/**
	 * 
	 */
	protected ApplyFormDefinitionProcessor() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * 
	 * @return
	 */
	public static ApplyFormDefinitionProcessor getInstance() {
		return new ApplyFormDefinitionProcessor();
	}
	/**
	 * 
	 * @param containerInterface
	 */
	public ContainerInterface addEntityToContainer(ContainerInterface containerInterface,FormDefinitionForm actionForm 
			,boolean IsActionSave) {
		ContainerProcessor containerProcessor = ContainerProcessor.getInstance();
		if(containerInterface == null) {
			 containerInterface = containerProcessor.createContainer();
		}
		containerProcessor.populateContainerInterface(containerInterface, actionForm);
		EntityProcessor entityProcessor = EntityProcessor.getInstance();
		EntityInterface entityInterface = containerInterface.getEntity();
		if(entityInterface == null) {
			try {
				if(IsActionSave) {
					entityInterface = entityProcessor.createAndSaveEntity(actionForm);
				} else 
					entityInterface = entityProcessor.createAndPopulateEntity(actionForm);
			} catch (DynamicExtensionsSystemException e) {
				e.printStackTrace();
			} catch (DynamicExtensionsApplicationException e) {
				e.printStackTrace();
			}
		} else {
			entityProcessor.populateEntityInformation(entityInterface, actionForm);
		}
		containerInterface.setEntity(entityInterface);
		return containerInterface;
	}
}
	