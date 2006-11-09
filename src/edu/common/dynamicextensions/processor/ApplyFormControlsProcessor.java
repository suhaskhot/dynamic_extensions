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
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
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

	protected ApplyFormControlsProcessor()
	{

	}

	/**
	 * this method gets the new instance of the ControlProcessor to the caller.
	 * @return ControlProcessor ControlProcessor instance
	 */
	public static ApplyFormControlsProcessor getInstance()
	{
		return new ApplyFormControlsProcessor();
	}
	/**
	 * @param containerInterface : Container object
	 * @param controlsForm : UI Information as form object
	 * @throws DynamicExtensionsSystemException dynamicExtensionsSystemException
	 * @throws DynamicExtensionsApplicationException DynamicExtensionsApplicationException
	 */
	public void addControlToForm(ContainerInterface containerInterface, ControlsForm controlsForm) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException 
	{
		if ((containerInterface != null) && (controlsForm != null))
		{

			EntityInterface entityInterface = containerInterface.getEntity();
			ControlProcessor controlProcessor = ControlProcessor.getInstance();
			AttributeProcessor attributeProcessor = AttributeProcessor.getInstance();

			AbstractAttributeInterface abstractAttributeInterface = null;
			ControlInterface controlInterface = null;
			//Check for operation
			String controlOperation = controlsForm.getControlOperation();

			//Default is add
			if ((controlOperation == null) || (controlOperation.trim().equals("")))
			{
				controlOperation = ProcessorConstants.OPERATION_ADD;
			}
			//Add new control
			if (controlOperation.equalsIgnoreCase(ProcessorConstants.OPERATION_ADD))
			{
				//Set Name of the attribute in controlsForm.
				//It is not accepted from UI. It has to be derived from caption
				String attributeName = deriveAttributeNameFromCaption(controlsForm.getCaption());
				controlsForm.setName(attributeName);

				//Create Attribute  
				abstractAttributeInterface = attributeProcessor.createAndPopulateAttribute(controlsForm);
				
				//Set permisible values
				setPermissibleValues(attributeProcessor,abstractAttributeInterface,controlsForm);
				
				//Set attribute in controlInformationInterface object(controlsForm)
				controlsForm.setAbstractAttribute(abstractAttributeInterface);

				//Control Interface : Add control
				controlInterface = controlProcessor.createAndPopulateControl(controlsForm.getUserSelectedTool(), controlsForm);
				Collection controlCollection = containerInterface.getControlCollection();
				if (controlCollection != null)
				{
					int noOfElts = controlCollection.size();
					controlInterface.setSequenceNumber(new Integer(noOfElts + 1));
				}

				//Entity Interface  : Add attribute
				if ((entityInterface != null)&&(abstractAttributeInterface!=null))
				{
					entityInterface.addAbstractAttribute(abstractAttributeInterface);
					abstractAttributeInterface.setEntity(entityInterface);
				}

				//Container : Add control and entity
				containerInterface.addControl(controlInterface);
				containerInterface.setEntity(entityInterface);

			}
			else if (controlOperation.equalsIgnoreCase(ProcessorConstants.OPERATION_EDIT))
			{
				//Set Name of the attribute in controlsForm.
				//It is not accepted from UI. It has to be derived from caption
				String attributeName = deriveAttributeNameFromCaption(controlsForm.getCaption());
				controlsForm.setName(attributeName);
				//Get the control from container
				String selectedControlSeqNumber = controlsForm.getSelectedControlId();
				controlInterface = containerInterface.getControlInterfaceBySequenceNumber(selectedControlSeqNumber);

				//Remove old refernces : From Entity
				entityInterface.removeAbstractAttribute(controlInterface.getAbstractAttribute());

				//Create new abstract attribute interface with new datatype
				abstractAttributeInterface = attributeProcessor.createAndPopulateAttribute(controlsForm);

				setPermissibleValues(attributeProcessor,abstractAttributeInterface,controlsForm);
				//update in entity
				entityInterface.addAbstractAttribute(abstractAttributeInterface);
				//update in control interface
				controlInterface.setAbstractAttribute(abstractAttributeInterface);

				controlsForm.setAbstractAttribute(abstractAttributeInterface);

				//update control
				ControlInterface newControlInterface = controlProcessor.populateControlInterface(controlsForm.getUserSelectedTool(),
						controlInterface, controlsForm);
				//If new control interface is same as old one, do nothing. Else remove old ref from container and add new one
				if ((newControlInterface != null) && (!newControlInterface.equals(controlInterface)))
				{
					//Remove control from container
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

	}

	/**
	 * @param abstractAttributeInterface
	 * @param controlsForm
	 * @throws DynamicExtensionsApplicationException :Exception
	 */
	private void setPermissibleValues(AttributeProcessor attributeProcessor, AbstractAttributeInterface abstractAttributeInterface, ControlsForm controlsForm) throws DynamicExtensionsApplicationException
	{
		//if combobox/optionbutton control has been selected then set the DataElement object for set of permissible values
		String userSelectedControl = controlsForm.getUserSelectedTool();
		if (userSelectedControl != null)
		{
			if((userSelectedControl.equalsIgnoreCase(ProcessorConstants.COMBOBOX_CONTROL))||(userSelectedControl.equalsIgnoreCase(ProcessorConstants.RADIOBUTTON_CONTROL)))
			{
				if (abstractAttributeInterface instanceof AttributeInterface)
				{
					((AttributeInterface) abstractAttributeInterface)
					.setDataElement(attributeProcessor.getDataElementInterface(controlsForm));
				}
			}
		}
		
	}

	/**
	 * 
	 * @param caption :Caption of the attribute
	 * @return : Name of the attribute derived from the caption. 
	 * The name has all spaces removed and first letter of every word capitalized
	 */
	private String deriveAttributeNameFromCaption(String caption)
	{
		String attributeName = "";
		if (caption != null)
		{
			String[] wordsInCaption = caption.split(" ");
			String word = null;
			if (wordsInCaption != null)
			{
				int noOfWords = wordsInCaption.length;
				char firstCharacter;
				for (int i = 0; i < noOfWords; i++)
				{
					word = wordsInCaption[i];
					if ((word != null) && (!word.trim().equalsIgnoreCase("")))
					{
						firstCharacter = word.charAt(0);
						word = word.replaceFirst(firstCharacter + "", Character.toUpperCase(firstCharacter) + "");
						attributeName = attributeName + word;
					}
				}
			}
		}
		return attributeName;
	}
}
