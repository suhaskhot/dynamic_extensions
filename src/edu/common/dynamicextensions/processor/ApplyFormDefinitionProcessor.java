package edu.common.dynamicextensions.processor;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.interfaces.EntityInformationInterface;

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
	public ContainerInterface addEntityToContainer(ContainerInterface containerInterface,EntityInformationInterface 
			entityInformationInterface , boolean IsActionSave) {
		ContainerProcessor containerProcessor = ContainerProcessor.getInstance();
		if(containerInterface == null) {
			 containerInterface = containerProcessor.createContainer();
		}
		EntityProcessor entityProcessor = EntityProcessor.getInstance();
		EntityInterface entityInterface = containerInterface.getEntity();
		if(entityInterface == null) {
			try {
				if(IsActionSave) {
					entityInterface = entityProcessor.createAndSaveEntity(entityInformationInterface);
				} else 
					entityInterface = entityProcessor.createAndPopulateEntity(entityInformationInterface);
			} catch (DynamicExtensionsSystemException e) {
				e.printStackTrace();
			} catch (DynamicExtensionsApplicationException e) {
				e.printStackTrace();
			}
		} else {
			entityProcessor.populateEntityInformation(entityInterface, entityInformationInterface);
		}
		containerInterface.setEntity(entityInterface);
		return containerInterface;
	}
	/**
	 * 
	 * @param containerInterface
	 *//*
	public void saveEntityToContainer(ContainerInterface containerInterface,EntityInformationInterface entityInformationInterface) {
		ContainerProcessor containerProcessor = ContainerProcessor.getInstance();
		if(containerInterface == null) {
			 containerInterface = containerProcessor.createContainer();
		}
		EntityProcessor entityProcessor = EntityProcessor.getInstance();
		EntityInterface entityInterface = containerInterface.getEntity();
		if(entityInterface == null) {
			try {
				entityInterface = entityProcessor.createAndSaveEntity(entityInformationInterface);
			} catch (DynamicExtensionsApplicationException e) {
				e.printStackTrace();
			}catch (DynamicExtensionsSystemException e) {
				e.printStackTrace();
			}
		} else {
			entityProcessor.populateEntityInformation(entityInterface, entityInformationInterface);
		}
		containerInterface.setEntity(entityInterface);
	}*/
}
	
