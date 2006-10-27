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
import edu.common.dynamicextensions.ui.interfaces.ControlUIBeanInterface;


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
			ControlUIBeanInterface controlUIBeanInterface) 
	{
		ControlInterface controlInterface  = populateControlInterface(userSelectedControlName, null, controlUIBeanInterface);
		return controlInterface;
	}

	public ControlInterface populateControlInterface(String userSelectedControlName, ControlInterface controlIntf, ControlUIBeanInterface controlUIBeanInterface)
	{
		ControlInterface controlInterface = null;
		if((userSelectedControlName!=null)&&(controlUIBeanInterface!=null))
		{
			if(userSelectedControlName.equalsIgnoreCase(ProcessorConstants.TEXT_CONTROL)) {
				if(controlUIBeanInterface.getLinesType() != null && controlUIBeanInterface.getLinesType().equalsIgnoreCase("MultiLine")) {
					controlInterface = getMultiLineControl(controlIntf, controlUIBeanInterface);	
				} else {
					controlInterface = getTextControl(controlIntf, controlUIBeanInterface);
				}
			} else if(userSelectedControlName.equalsIgnoreCase(ProcessorConstants.COMBOBOX_CONTROL)) {
				controlInterface = getComboBoxControl(controlIntf, controlUIBeanInterface);
			} else if(userSelectedControlName.equalsIgnoreCase(ProcessorConstants.LISTBOX_CONTROL)) {
				controlInterface = getListBoxControl(controlIntf, controlUIBeanInterface);
			} else if(userSelectedControlName.equalsIgnoreCase(ProcessorConstants.CHECKBOX_CONTROL)) {
				controlInterface = getCheckBoxControl(controlIntf, controlUIBeanInterface);
			} else if(userSelectedControlName.equalsIgnoreCase(ProcessorConstants.RADIOBUTTON_CONTROL)) {
				controlInterface = getRadioButtonControl(controlIntf, controlUIBeanInterface);
			} else if(userSelectedControlName.equalsIgnoreCase(ProcessorConstants.DATEPICKER_CONTROL)) {
				controlInterface = getDatePickerControl(controlIntf,controlUIBeanInterface);
			}
		}
		//Load common properties for controls
		if (controlUIBeanInterface != null && controlInterface != null) {
			controlInterface.setAbstractAttribute(controlUIBeanInterface.getAbstractAttribute());
			controlInterface.setCaption(controlUIBeanInterface.getCaption());
			//controlInterface.setCssClass(ProcessorConstants.DEFAULT_CSS_CLASS);
			controlInterface.setTooltip(controlUIBeanInterface.getTooltip());
			controlInterface.setIsHidden(controlUIBeanInterface.getIsHidden());
		}
		return controlInterface;

	}

	/**
	 * @param controlInterface 
	 * @param controlUIBeanInterface
	 * @return
	 */
	private ControlInterface getDatePickerControl(ControlInterface controlInterface, ControlUIBeanInterface controlUIBeanInterface)
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
	 * @param controlUIBeanInterface
	 * @return
	 */
	private ControlInterface getRadioButtonControl(ControlInterface controlInterface, ControlUIBeanInterface controlUIBeanInterface)
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
	 * @param controlUIBeanInterface
	 * @return
	 */
	private ControlInterface getCheckBoxControl(ControlInterface controlInterface, ControlUIBeanInterface controlUIBeanInterface)
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
	 * @param controlUIBeanInterface
	 * @return
	 */
	private ControlInterface getListBoxControl(ControlInterface controlInterface, ControlUIBeanInterface controlUIBeanInterface)
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

		listBoxIntf.setIsMultiSelect(controlUIBeanInterface.getIsMultiSelect());
		//listBoxIntf.setChoiceList(controlInformationInterface.getDisplayChoiceList());

		System.out.println("Is Multiselect = " + listBoxIntf.getIsMultiSelect());
		//System.out.println("Choice List " + listBoxIntf.getChoiceList());
		return listBoxIntf;
	}
	/**
	 * @param controlInterface 
	 * @param controlUIBeanInterface
	 * @return
	 */
	private ControlInterface getComboBoxControl(ControlInterface controlInterface, ControlUIBeanInterface controlUIBeanInterface)
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

		//comboBoxIntf.setChoiceList(controlInformationInterface.getDisplayChoiceList());

		//System.out.println("Choice List " + comboBoxIntf.getChoiceList());
		return comboBoxIntf;
	}
	/**
	 * @param controlInterface 
	 * @param controlUIBeanInterface 
	 * @return
	 */
	private ControlInterface getMultiLineControl(ControlInterface controlInterface, ControlUIBeanInterface controlUIBeanInterface)
	{
		TextAreaInterface textAreaIntf = null;
		if(controlInterface==null)	//If does not exist create it 
		{
			textAreaIntf = DomainObjectFactory.getInstance().createTextArea();
		}
		else
		{
			if(controlInterface instanceof TextAreaInterface)
			{
				textAreaIntf = (TextAreaInterface)controlInterface;
			}
			else
			{
				textAreaIntf = DomainObjectFactory.getInstance().createTextArea();
			}
		}
		textAreaIntf.setColumns(controlUIBeanInterface.getColumns());
		textAreaIntf.setRows(controlUIBeanInterface.getRows());

		System.out.println("No Of rows = " + textAreaIntf.getRows());
		System.out.println("No Of Cols = " + textAreaIntf.getColumns());
		return textAreaIntf;
	}
	/**
	 * Creates a new TextControl if control interface is null
	 * Updates the existing if not null
	 * @param controlInterface 
	 * @param controlUIBeanInterface
	 * @return
	 */
	private ControlInterface getTextControl(ControlInterface controlInterface, ControlUIBeanInterface controlUIBeanInterface)
	{
		TextFieldInterface textFldIntf = null;
		if(controlInterface==null)	//If does not exist create it 
		{
			textFldIntf = DomainObjectFactory.getInstance().createTextField(); 
			System.out.println("Created Text control");
		}
		else
		{
			if(controlInterface instanceof TextFieldInterface)
			{
				textFldIntf = (TextFieldInterface)controlInterface;
			}
			else
			{
				textFldIntf = DomainObjectFactory.getInstance().createTextField();
			}
		}
		textFldIntf.setIsPassword(controlUIBeanInterface.getIsPassword());
		textFldIntf.setColumns(controlUIBeanInterface.getColumns());

		System.out.println("Is Password = " + textFldIntf.getIsPassword());
		System.out.println("Cols = " + textFldIntf.getColumns());
		return textFldIntf;
	}

	/**
	 * This method will populate the ControlUIBeanInterface using the controlInterface so that the 
	 * information of the Control can be shown on the user page using the ControlUIBeanInterface.
	 * @param controlInterface Instance of controlInterface from which to populate the informationInterface.
	 * @param controlUIBeanInterface Instance of ControlUIBeanInterface which will be populated using 
	 * the first parameter that is controlInterface.
	 */
	public void populateControlUIBeanInterface(ControlInterface controlInterface, ControlUIBeanInterface controlUIBeanInterface) {
		if (controlInterface != null && controlUIBeanInterface != null) {
			controlUIBeanInterface.setAbstractAttribute(controlInterface.getAbstractAttribute());
			controlUIBeanInterface.setCssClass(controlInterface.getCssClass());
			controlUIBeanInterface.setTooltip(controlInterface.getTooltip());
			controlUIBeanInterface.setCaption(controlInterface.getCaption());
			controlUIBeanInterface.setIsHidden(controlInterface.getIsHidden());
			controlUIBeanInterface.setSequenceNumber(controlInterface.getSequenceNumber());
		}
		if(controlInterface instanceof TextFieldInterface)
		{
			controlUIBeanInterface.setUserSelectedTool(ProcessorConstants.TEXT_CONTROL);
			controlUIBeanInterface.setHtmlFile( ProcessorConstants.TEXT_CONTROL + ".jsp");
			controlUIBeanInterface.setColumns(((TextFieldInterface)controlInterface).getColumns());
			controlUIBeanInterface.setIsPassword(((TextFieldInterface)controlInterface).getIsPassword());
			controlUIBeanInterface.setLinesType(ProcessorConstants.LINE_TYPE_SINGLELINE);
			//controlUIBeanInterface.setRows(null);
		}
		else if(controlInterface instanceof ComboBoxInterface)
		{
			controlUIBeanInterface.setUserSelectedTool(ProcessorConstants.COMBOBOX_CONTROL);
			controlUIBeanInterface.setHtmlFile( ProcessorConstants.COMBOBOX_CONTROL + ".jsp");
			//controlInformationInterface.setDisplayChoiceList(((ComboBoxInterface)controlInterface).getChoiceList());
		}
		else if(controlInterface instanceof DatePickerInterface)
		{
			controlUIBeanInterface.setUserSelectedTool(ProcessorConstants.DATEPICKER_CONTROL);
			controlUIBeanInterface.setHtmlFile( ProcessorConstants.DATEPICKER_CONTROL + ".jsp");

		}
		else if(controlInterface instanceof TextAreaInterface)
		{
			controlUIBeanInterface.setUserSelectedTool(ProcessorConstants.MULTILINE_CONTROL);
			controlUIBeanInterface.setHtmlFile( ProcessorConstants.TEXT_CONTROL + ".jsp");
			controlUIBeanInterface.setColumns(((TextAreaInterface)controlInterface).getColumns());
			controlUIBeanInterface.setRows(((TextAreaInterface)controlInterface).getRows());
			controlUIBeanInterface.setLinesType(ProcessorConstants.LINE_TYPE_MULTILINE);
		}


	}
}