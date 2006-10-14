/*
 * Created on Oct 14, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.processor;

import java.util.Collection;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ApplyFormControlsProcessor extends BaseDynamicExtensionsProcessor
{
	/**
	 * Protected constructor for ControlProcessor
	 *
	 */
	protected  ApplyFormControlsProcessor () {

	}
	/**
	 * this method gets the new instance of the ControlProcessor to the caller.
	 * @return ControlProcessor ControlProcessor instance
	 */
	public static ApplyFormControlsProcessor getInstance () {
		return new ApplyFormControlsProcessor();
	}

	public void addControlToForm(ContainerInterface containerInterface, ControlsForm controlsForm)
	{
		System.out.println("Add Control to form");
		if((containerInterface != null)&&(controlsForm!=null))
		{
			try
			{
				EntityInterface entityInterface = containerInterface.getEntity();
				ControlProcessor controlProcessor = ControlProcessor.getInstance();
				AttributeProcessor attributeProcessor = AttributeProcessor.getInstance();

				AbstractAttributeInterface abstractAttributeInterface = null;
				ControlInterface controlInterface = null;
				//Check for operation
				String controlOperation  = controlsForm.getControlOperation();
				System.out.println("Control operation = " + controlOperation);
				if(controlOperation!=null)
				{
					//Add new control
					if(controlOperation.equalsIgnoreCase(ProcessorConstants.ADD))
					{
						System.out.println("Add Control operation");
						//Create Attribute  
						abstractAttributeInterface = attributeProcessor.createAndPopulateAttribute(controlsForm);
						controlsForm.setAbstractAttribute(abstractAttributeInterface);
						//Control Interface : Add control
						controlInterface = controlProcessor.createAndPopulateControl(controlsForm.getUserSelectedTool(),controlsForm);
						Collection controlCollection = containerInterface.getControlCollection();
						if(controlCollection!=null)
						{
							int noOfElts = controlCollection.size();
							controlInterface.setSequenceNumber(new Integer(noOfElts+1));
							System.out.println("Deq Number = " + controlInterface.getSequenceNumber());
						}

					}else if(controlOperation.equalsIgnoreCase(ProcessorConstants.EDIT))
					{
						System.out.println("Edit Control operation");
						//Get the control from container
						String selectedControlSeqNumber = controlsForm.getSelectedControlId();
						System.out.println("Selected Control Seq Number = "  +selectedControlSeqNumber);
						controlInterface = containerInterface.getControlInterfaceBySequenceNumber(selectedControlSeqNumber);

						//Get Attribute interface from control
						abstractAttributeInterface = controlInterface.getAbstractAttribute();
						//update attribute
						if(abstractAttributeInterface instanceof AttributeInterface){
							attributeProcessor.populateAttribute((AttributeInterface) abstractAttributeInterface, controlsForm);
							System.out.println("Polulated attribute");
						}else{
							System.out.println("Error while casting AttributeInterfce expected");
						}
						controlsForm.setAbstractAttribute(abstractAttributeInterface);
						//update control
						controlProcessor.populateControlInterface(controlsForm.getUserSelectedTool(), controlInterface, controlsForm);
					}
				}
				//Entity Interface  : Add attribute
				if(entityInterface != null) {
					System.out.println("Updating entity adding attribte " + abstractAttributeInterface);
					entityInterface.addAbstractAttribute(abstractAttributeInterface);
				}
				//Container : Add control and entity
				System.out.println("Adding control to container");
				containerInterface.addControl(controlInterface);
				
				System.out.println("Adding entity to conntainer");
				containerInterface.setEntity(entityInterface);
			}
			catch (Exception e)
			{
				System.out.println("Exception " + e);
			}

		}
	}
}
