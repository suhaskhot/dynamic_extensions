/**
 *<p>Title: ControlProcessor</p>
 *<p>Description:  This class acts as a utility class which processes tne control in various ways as needed
 *and provides methods to the UI layer.This processor class is a POJO and not a framework specific class so 
 *it can be used by all types of presentation layers.  </p>
 *<p>Copyright:TODO</p>
 *@author Deepti Shelar
 *@version 1.0
 */ 
package edu.common.dynamicextensions.processor;

import edu.common.dynamicextensions.domain.DomainObjectFactory;
import edu.common.dynamicextensions.domaininterface.userinterface.CheckBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ComboBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.DatePickerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ListBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.RadioButtonInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextAreaInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextFieldInterface;
import edu.common.dynamicextensions.ui.interfaces.ControlInformationInterface;


/**
 * This class processes all the information needed for Control.
 * @author deepti_shelar
 *
 */
public class ControlProcessor extends BaseDynamicExtensionsProcessor
{
	/**
	 * Protected constructor for ControlProcessor
	 *
	 */
	protected  ControlProcessor () {

	}
	/**
	 * this method gets the new instance of the ControlProcessor to the caller.
	 * @return ControlProcessor ControlProcessor instance
	 */
	public static ControlProcessor getInstance () {
		return new ControlProcessor();
	}
	/**
	 * This method returns empty domain object of controlInterface.
	 * @return ControlInterface Returns new instance of ControlInterface from the domain object Factory.
	 */
	public ControlInterface createAndPopulateControl(String userSelectedControlName,
			ControlInformationInterface controlInformationInterface) 
	{
		ControlInterface controlInterface  = populateControlInterface(userSelectedControlName, null, controlInformationInterface);
		return controlInterface;
	}

	public ControlInterface populateControlInterface(String userSelectedControlName, ControlInterface controlIntf, ControlInformationInterface controlInformationInterface)
	{
		ControlInterface controlInterface = null;
		System.out.println("User Selected Control = " + userSelectedControlName);
		if((userSelectedControlName!=null)&&(controlInformationInterface!=null))
		{
			if(userSelectedControlName.equalsIgnoreCase(ProcessorConstants.TEXT_CONTROL)) {
				controlInterface = getTextControl(controlIntf, controlInformationInterface);
			} else if(userSelectedControlName.equalsIgnoreCase(ProcessorConstants.MULTILINE_CONTROL)) {
				controlInterface = getMultiLineControl(controlIntf, controlInformationInterface);
			} else if(userSelectedControlName.equalsIgnoreCase(ProcessorConstants.COMBOBOX_CONTROL)) {
				controlInterface = getComboBoxControl(controlIntf, controlInformationInterface);
			} else if(userSelectedControlName.equalsIgnoreCase(ProcessorConstants.LISTBOX_CONTROL)) {
				controlInterface = getListBoxControl(controlIntf, controlInformationInterface);
			} else if(userSelectedControlName.equalsIgnoreCase(ProcessorConstants.CHECKBOX_CONTROL)) {
				controlInterface = getCheckBoxControl(controlIntf, controlInformationInterface);
			} else if(userSelectedControlName.equalsIgnoreCase(ProcessorConstants.RADIOBUTTON_CONTROL)) {
				controlInterface = getRadioButtonControl(controlIntf, controlInformationInterface);
			} else if(userSelectedControlName.equalsIgnoreCase(ProcessorConstants.DATEPICKER_CONTROL)) {
				controlInterface = getDatePickerControl(controlIntf,controlInformationInterface);
			}
		}
		//Load common properties for controls
		if (controlInformationInterface != null && controlInterface != null) {
			controlInterface.setAbstractAttribute(controlInformationInterface.getAbstractAttribute());
			controlInterface.setCaption(controlInformationInterface.getCaption());
			controlInterface.setCssClass(controlInformationInterface.getCssClass());
			controlInterface.setTooltip(controlInformationInterface.getTooltip());
			controlInterface.setIsHidden(controlInformationInterface.getIsHidden());
			//controlInterface.setSequenceNumber(controlInformationInterface.getSequenceNumber());

			System.out.println("Caption [" + controlInterface.getCaption() + "]");
			System.out.println("Css Class [" + controlInterface.getCssClass() + "]");
			System.out.println("Tooltip [" + controlInterface.getTooltip() + "]");
			System.out.println("Is Hidden [" + controlInterface.getIsHidden() + "]");
			System.out.println("Seq Number [" + controlInterface.getSequenceNumber() + "]");

		}
		return controlInterface;

	}

	/**
	 * @param controlInterface 
	 * @param controlInformationInterface
	 * @return
	 */
	private ControlInterface getDatePickerControl(ControlInterface controlInterface, ControlInformationInterface controlInformationInterface)
	{
		DatePickerInterface datePickerIntf = null;
		if(controlInterface == null)
		{
			datePickerIntf = DomainObjectFactory.getInstance().createDatePicker();
			System.out.println("Created Date Picker Intf");
		}
		else
		{
			datePickerIntf = (DatePickerInterface)controlInterface;
		}

		return datePickerIntf;
	}
	/**
	 * @param controlInterface 
	 * @param controlInformationInterface
	 * @return
	 */
	private ControlInterface getRadioButtonControl(ControlInterface controlInterface, ControlInformationInterface controlInformationInterface)
	{
		RadioButtonInterface radioButtonIntf = null;
		if(controlInterface==null)
		{
			radioButtonIntf = DomainObjectFactory.getInstance().createRadioButton();
			System.out.println("Created Radio Button Intf");
		}
		else
		{
			radioButtonIntf = (RadioButtonInterface) controlInterface;
		}


		return radioButtonIntf;
	}
	/**
	 * @param controlInterface 
	 * @param controlInformationInterface
	 * @return
	 */
	private ControlInterface getCheckBoxControl(ControlInterface controlInterface, ControlInformationInterface controlInformationInterface)
	{
		CheckBoxInterface checkBoxIntf = null;
		if(controlInterface == null)
		{
			checkBoxIntf = DomainObjectFactory.getInstance().createCheckBox();
			System.out.println("Created Check box Intf");
		}
		else
		{
			checkBoxIntf = (CheckBoxInterface)controlInterface;
		}

		return checkBoxIntf;
	}
	/**
	 * @param controlInterface 
	 * @param controlInformationInterface
	 * @return
	 */
	private ControlInterface getListBoxControl(ControlInterface controlInterface, ControlInformationInterface controlInformationInterface)
	{
		ListBoxInterface listBoxIntf = null;

		if(controlInterface==null)
		{
			listBoxIntf = DomainObjectFactory.getInstance().createListBox();
			System.out.println("Created List Intf");
		}
		else
		{
			listBoxIntf = (ListBoxInterface)controlInterface;
		}

		listBoxIntf.setIsMultiSelect(controlInformationInterface.getIsMultiSelect());
		listBoxIntf.setChoiceList(controlInformationInterface.getDisplayChoiceList());

		System.out.println("Is Multiselect = " + listBoxIntf.getIsMultiSelect());
		System.out.println("Choice List " + listBoxIntf.getChoiceList());
		return listBoxIntf;
	}
	/**
	 * @param controlInterface 
	 * @param controlInformationInterface
	 * @return
	 */
	private ControlInterface getComboBoxControl(ControlInterface controlInterface, ControlInformationInterface controlInformationInterface)
	{
		ComboBoxInterface comboBoxIntf = null;
		if(controlInterface==null)
		{
			comboBoxIntf = DomainObjectFactory.getInstance().createComboBox();
			System.out.println("Created combobox Intf ");
		}
		else
		{
			comboBoxIntf = (ComboBoxInterface)controlInterface;
		}

		comboBoxIntf.setChoiceList(controlInformationInterface.getDisplayChoiceList());

		System.out.println("Choice List " + comboBoxIntf.getChoiceList());
		return comboBoxIntf;
	}
	/**
	 * @param controlInterface 
	 * @param controlInformationInterface 
	 * @return
	 */
	private ControlInterface getMultiLineControl(ControlInterface controlInterface, ControlInformationInterface controlInformationInterface)
	{
		TextAreaInterface textAreaIntf = null;
		if(controlInterface==null)	//If does not exist create it 
		{
			textAreaIntf = DomainObjectFactory.getInstance().createTextArea();
			System.out.println("Created Text Area control");
		}
		else
		{
			textAreaIntf = (TextAreaInterface)controlInterface;
		}
		textAreaIntf.setColumns(controlInformationInterface.getColumns());
		textAreaIntf.setRows(controlInformationInterface.getRows());

		System.out.println("No Of rows = " + textAreaIntf.getRows());
		System.out.println("No Of Cols = " + textAreaIntf.getColumns());
		return textAreaIntf;
	}
	/**
	 * Creates a new TextControl if control interface is null
	 * Updates the existing if not null
	 * @param controlInterface 
	 * @param controlInformationInterface
	 * @return
	 */
	private ControlInterface getTextControl(ControlInterface controlInterface, ControlInformationInterface controlInformationInterface)
	{
		TextFieldInterface textFldIntf = null;
		if(controlInterface==null)	//If does not exist create it 
		{
			textFldIntf = DomainObjectFactory.getInstance().createTextField(); 
			System.out.println("Created Text control");
		}
		else
		{
			textFldIntf = (TextFieldInterface)controlInterface;
		}
		textFldIntf.setIsPassword(controlInformationInterface.getIsPassword());
		textFldIntf.setColumns(controlInformationInterface.getColumns());

		System.out.println("Is Password = " + textFldIntf.getIsPassword());
		System.out.println("Cols = " + textFldIntf.getColumns());
		return textFldIntf;
	}

	/**
	 * This method will populate the ControlInformationInterface using the controlInterface so that the 
	 * information of the Control can be shown on the user page using the ControlInformationInterface.
	 * @param controlInterface Instance of controlInterface from which to populate the informationInterface.
	 * @param controlInformationInterface Instance of ControlInformationInterface which will be populated using 
	 * the first parameter that is controlInterface.
	 */
	public void populateControlInformation(ControlInterface controlInterface, ControlInformationInterface controlInformationInterface) {
		if (controlInterface != null && controlInformationInterface != null) {
			controlInformationInterface.setAbstractAttribute(controlInterface.getAbstractAttribute());
			controlInformationInterface.setCssClass(controlInterface.getCssClass());
			controlInformationInterface.setTooltip(controlInterface.getTooltip());
			controlInformationInterface.setCaption(controlInterface.getCaption());
			controlInformationInterface.setIsHidden(controlInterface.getIsHidden());
			controlInformationInterface.setSequenceNumber(controlInterface.getSequenceNumber());
		}
		if(controlInterface instanceof TextFieldInterface)
		{
			controlInformationInterface.setColumns(((TextFieldInterface)controlInterface).getColumns());
			controlInformationInterface.setIsPassword(((TextFieldInterface)controlInterface).getIsPassword());
		}else if(controlInterface instanceof ComboBoxInterface)
		{
			controlInformationInterface.setDisplayChoiceList(((ComboBoxInterface)controlInterface).getChoiceList());
		}
	}
}