/*
 * Created on Oct 14, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.processor;

import java.util.Collection;
import com.sun.org.apache.xml.internal.serializer.ToUnknownStream;

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

				//Default is add
				if((controlOperation==null)||(controlOperation.trim().equals("")))
				{
					controlOperation  = ProcessorConstants.OPERATION_ADD;
				}
				//Add new control
				if(controlOperation.equalsIgnoreCase(ProcessorConstants.OPERATION_ADD))
				{
					//Set Name of the attribute in controlsForm.
					//It is not accepted from UI. It has to be derived from caption
					String attributeName = deriveAttributeNameFromCaption(controlsForm.getCaption());
					System.out.println("Attribute Caption = [" + controlsForm.getCaption() +"] Name = ["+attributeName  +"]");
					controlsForm.setName(attributeName);
					
					//Create Attribute  
					abstractAttributeInterface = attributeProcessor.createAndPopulateAttribute(controlsForm);
					
					//if combobox control has been selected then set the DataElement object for set of permissible values
					String userSelectedControl = controlsForm.getUserSelectedTool(); 
					if((userSelectedControl!=null)&&(userSelectedControl.equalsIgnoreCase(ProcessorConstants.COMBOBOX_CONTROL)))
					{
						if(abstractAttributeInterface instanceof AttributeInterface)
						{
							((AttributeInterface)abstractAttributeInterface).setDataElement(attributeProcessor.getDataElementInterface(controlsForm));
						}
					}
					
					//Set attribute in controlInformationInterface object(controlsForm)
					controlsForm.setAbstractAttribute(abstractAttributeInterface);
					
					//Control Interface : Add control
					controlInterface = controlProcessor.createAndPopulateControl(controlsForm.getUserSelectedTool(),controlsForm);
					Collection controlCollection = containerInterface.getControlCollection();
					if(controlCollection!=null)
					{
						int noOfElts = controlCollection.size();
						controlInterface.setSequenceNumber(new Integer(noOfElts+1));
					}

					//Entity Interface  : Add attribute
					if(entityInterface != null) {
						entityInterface.addAbstractAttribute(abstractAttributeInterface);
						abstractAttributeInterface.setEntity(entityInterface);
					}

					//Container : Add control and entity
					containerInterface.addControl(controlInterface);
					containerInterface.setEntity(entityInterface);

				}else if(controlOperation.equalsIgnoreCase(ProcessorConstants.OPERATION_EDIT))
				{
					//Set Name of the attribute in controlsForm.
					//It is not accepted from UI. It has to be derived from caption
					String attributeName = deriveAttributeNameFromCaption(controlsForm.getCaption());
					System.out.println("Attribute Caption = [" + controlsForm.getCaption() +"] Name = ["+attributeName  +"]");
					controlsForm.setName(attributeName);
					//Get the control from container
					String selectedControlSeqNumber = controlsForm.getSelectedControlId();
					controlInterface = containerInterface.getControlInterfaceBySequenceNumber(selectedControlSeqNumber);

					//Remove old refernces : From Entity
					entityInterface.removeAbstractAttribute(controlInterface.getAbstractAttribute());

					//Create new abstract attribute interface with new datatype
					abstractAttributeInterface = attributeProcessor.createAndPopulateAttribute(controlsForm);
					
					//if combobox control has been selected then set the DataElement object for set of permissible values
					String userSelectedControl = controlsForm.getUserSelectedTool(); 
					if((userSelectedControl!=null)&&(userSelectedControl.equalsIgnoreCase(ProcessorConstants.COMBOBOX_CONTROL)))
					{
						if(abstractAttributeInterface instanceof AttributeInterface)
						{
							((AttributeInterface)abstractAttributeInterface).setDataElement(attributeProcessor.getDataElementInterface(controlsForm));
						}
					}
					//update in entity
					entityInterface.addAbstractAttribute(abstractAttributeInterface);
					//update in control interface
					controlInterface.setAbstractAttribute(abstractAttributeInterface);

					controlsForm.setAbstractAttribute(abstractAttributeInterface);
					
					//update control
					ControlInterface newControlInterface = controlProcessor.populateControlInterface(controlsForm.getUserSelectedTool(), controlInterface, controlsForm);
					//If new control interface is same as old one, do nothing. Else remove old ref from container and add new one
					if((newControlInterface!=null)&&(newControlInterface.equals(controlInterface)))
					{
						System.out.println("Same interface modified");
					}
					else
					{
						
						containerInterface.removeControl(controlInterface);
						
						//Set Sequence number
						newControlInterface.setSequenceNumber(controlInterface.getSequenceNumber());
						
						//Set abstract attriibute
						newControlInterface.setAbstractAttribute(abstractAttributeInterface);
						//add to container
						containerInterface.addControl(newControlInterface);
					}
				}
			}
			catch (Exception e)
			{
				System.out.println("Exception " + e);
			}
		}
	}
	/**
	 * @param caption
	 * @return
	 */
	private String deriveAttributeNameFromCaption(String caption)
	{
		String attributeName = "";
		if(caption!=null)
		{
			String[] wordsInCaption = caption.split(" ");
			String word = null; 
			if(wordsInCaption!=null)
			{
				int noOfWords = wordsInCaption.length;
				char firstCharacter ;
				for(int i=0;i<noOfWords;i++)
				{
					word = wordsInCaption[i];
					if((word!=null)&&(word.trim().equalsIgnoreCase("")==false))
					{
						firstCharacter = word.charAt(0);
						word  = word.replaceFirst(firstCharacter+"", Character.toUpperCase(firstCharacter)+"");
						attributeName = attributeName + word;
					}
				}
			}
		}
		return attributeName;
	}
}
