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
		ControlInterface controlInterface = null;
		System.out.println("User Selected Control = " + userSelectedControlName);
		if((userSelectedControlName!=null)&&(controlInformationInterface!=null))
		{
			if(userSelectedControlName.equalsIgnoreCase(ProcessorConstants.TEXT_CONTROL)) {
				controlInterface = createTextControl(controlInformationInterface);
			} else if(userSelectedControlName.equalsIgnoreCase(ProcessorConstants.MULTILINE_CONTROL)) {
				controlInterface = createMultiLineControl(controlInformationInterface);
			} else if(userSelectedControlName.equalsIgnoreCase(ProcessorConstants.COMBOBOX_CONTROL)) {
				controlInterface = createComboBoxControl(controlInformationInterface);
			} else if(userSelectedControlName.equalsIgnoreCase(ProcessorConstants.LISTBOX_CONTROL)) {
				controlInterface = createListBoxControl(controlInformationInterface);
			} else if(userSelectedControlName.equalsIgnoreCase(ProcessorConstants.CHECKBOX_CONTROL)) {
				controlInterface = createCheckBoxControl(controlInformationInterface);
			} else if(userSelectedControlName.equalsIgnoreCase(ProcessorConstants.RADIOBUTTON_CONTROL)) {
				controlInterface = createRadioButtonControl(controlInformationInterface);
			} else if(userSelectedControlName.equalsIgnoreCase(ProcessorConstants.DATEPICKER_CONTROL)) {
				controlInterface = createDatePickerControl(controlInformationInterface);
			}
		}
		//Load common properties for controls
		if (controlInformationInterface != null && controlInterface != null) {
			controlInterface.setAbstractAttribute(controlInformationInterface.getAbstractAttribute());
			controlInterface.setCaption(controlInformationInterface.getCaption());
			controlInterface.setCssClass(controlInformationInterface.getCssClass());
			controlInterface.setTooltip(controlInformationInterface.getTooltip());
			controlInterface.setIsHidden(controlInformationInterface.getIsHidden());
			controlInterface.setSequenceNumber(controlInformationInterface.getSequenceNumber());
			
			System.out.println("Caption [" + controlInterface.getCaption() + "]");
			System.out.println("Css Class [" + controlInterface.getCssClass() + "]");
			System.out.println("Tooltip [" + controlInterface.getTooltip() + "]");
			System.out.println("Is Hidden [" + controlInterface.getIsHidden() + "]");
			System.out.println("Seq Number [" + controlInterface.getSequenceNumber() + "]");
			
		}
		return controlInterface;
	}
	/**
	 * @param controlInformationInterface
	 * @return
	 */
	private ControlInterface createDatePickerControl(ControlInformationInterface controlInformationInterface)
	{
		DatePickerInterface datePickerIntf = DomainObjectFactory.getInstance().createDatePicker();
		System.out.println("Created Date Picker Intf");
		return datePickerIntf;
	}
	/**
	 * @param controlInformationInterface
	 * @return
	 */
	private ControlInterface createRadioButtonControl(ControlInformationInterface controlInformationInterface)
	{
		RadioButtonInterface radioButtonIntf = DomainObjectFactory.getInstance().createRadioButton();
		System.out.println("Created Radio Button Intf");
		return radioButtonIntf;
	}
	/**
	 * @param controlInformationInterface
	 * @return
	 */
	private ControlInterface createCheckBoxControl(ControlInformationInterface controlInformationInterface)
	{
		CheckBoxInterface checkBoxIntf = DomainObjectFactory.getInstance().createCheckBox();
		System.out.println("Created Check box Intf");
		return checkBoxIntf;
	}
	/**
	 * @param controlInformationInterface
	 * @return
	 */
	private ControlInterface createListBoxControl(ControlInformationInterface controlInformationInterface)
	{
		ListBoxInterface listBoxIntf = DomainObjectFactory.getInstance().createListBox();
		System.out.println("Created List Intf");
		listBoxIntf.setIsMultiSelect(controlInformationInterface.getIsMultiSelect());
		listBoxIntf.setChoiceList(controlInformationInterface.getDisplayChoiceList());
		
		System.out.println("Is Multiselect = " + listBoxIntf.getIsMultiSelect());
		System.out.println("Choice List " + listBoxIntf.getChoiceList());
		return listBoxIntf;
	}
	/**
	 * @param controlInformationInterface
	 * @return
	 */
	private ControlInterface createComboBoxControl(ControlInformationInterface controlInformationInterface)
	{
		ComboBoxInterface comboBoxIntf = DomainObjectFactory.getInstance().createComboBox();
		comboBoxIntf.setChoiceList(controlInformationInterface.getDisplayChoiceList());
		System.out.println("Created combobox Intf ");
		System.out.println("Choice List " + comboBoxIntf.getChoiceList());
		return comboBoxIntf;
	}
	/**
	 * @param controlInformationInterface 
	 * @return
	 */
	private ControlInterface createMultiLineControl(ControlInformationInterface controlInformationInterface)
	{
		TextAreaInterface textAreaInf = DomainObjectFactory.getInstance().createTextArea();
		textAreaInf.setColumns(controlInformationInterface.getColumns());
		textAreaInf.setRows(controlInformationInterface.getRows());
		System.out.println("Created Text Area control");
		System.out.println("No Of rows = " + textAreaInf.getRows());
		System.out.println("No Of Cols = " + textAreaInf.getColumns());
		return textAreaInf;
	}
	/**
	 * @param controlInformationInterface
	 * @return
	 */
	private ControlInterface createTextControl(ControlInformationInterface controlInformationInterface)
	{
		TextFieldInterface textFldIntf = DomainObjectFactory.getInstance().createTextField();
		textFldIntf.setIsPassword(controlInformationInterface.getIsPassword());
		textFldIntf.setColumns(controlInformationInterface.getColumns());
		System.out.println("Created Text control");
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
			
		}
	}
}
