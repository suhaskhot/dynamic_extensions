package edu.common.dynamicextensions.processor;


import java.util.Collection;
import java.util.Iterator;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.processor.AttributeProcessor;


/**
 * 
 * @author deepti_shelar
 *
 */
public class BuildContainerProcessor extends BaseDynamicExtensionsProcessor {
    
	public void createContainer(ContainerInterface containerInterface) {	
		EntityInterface entityInterface = containerInterface.getEntity();
		AttributeProcessor attributeProcessor = AttributeProcessor.getInstance();
		Collection controlCollection = containerInterface.getControlCollection();
		Iterator controlsInIterator = controlCollection.iterator();
		while(controlsInIterator.hasNext()) {
			ControlInterface controlInterface = (ControlInterface)controlsInIterator.next();
			//AttributeInterface attributeInterface = attributeProcessor.createAttribute(controlInterface.getDataType());	
		}

		
		

		/*ControlProcessor controlProcessor = ControlProcessor.getInstance();
		ControlInterface controlInterface = controlProcessor.createControl(containerInterface.getUserSelectedTool());
		controlProcessor.populateControl(containerInterface, controlInterface);



		if(entityInterface!=null)
		{
			//Add attribute to entity
			entityInterface.addAttribute(attributeInterface);

			//Add attribute to control
			controlInterface.setAttribute(attributeInterface);

			//Add control to container
			container.addControl(controlInterface);

			//Add entity to container
			container.setEntity(entityInterface);
*/

		}


	}  


