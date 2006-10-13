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
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ListBoxInterface;
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
			ControlInformationInterface controlInformationInterface) {
		ControlInterface controlInterface = null;
		if(userSelectedControlName.equalsIgnoreCase("TextControl")) {
			controlInterface = DomainObjectFactory.getInstance().createTextField();
			populateControl(controlInformationInterface, controlInterface);
			((TextFieldInterface)controlInterface).setIsPassword(controlInformationInterface.getIsPassword());
			((TextFieldInterface)controlInterface).setColumns(controlInformationInterface.getColumns());
		} else if(userSelectedControlName.equalsIgnoreCase("MultilineControl")) {
			populateControl(controlInformationInterface, controlInterface);
			controlInterface = DomainObjectFactory.getInstance().createTextArea();
			((TextAreaInterface)controlInterface).setColumns(controlInformationInterface.getColumns());
			((TextAreaInterface)controlInterface).setRows(controlInformationInterface.getRows());
		} else if(userSelectedControlName.equalsIgnoreCase("ComboboxControl")) {
			populateControl(controlInformationInterface, controlInterface);
			controlInterface = DomainObjectFactory.getInstance().createComboBox();
		} else if(userSelectedControlName.equalsIgnoreCase("ListBoxControl")) {
			populateControl(controlInformationInterface, controlInterface);
			controlInterface = DomainObjectFactory.getInstance().createListBox();
			((ListBoxInterface)controlInterface).setIsMultiSelect(controlInformationInterface.getIsMultiSelect());
		} else if(userSelectedControlName.equalsIgnoreCase("CheckboxControl")) {
			populateControl(controlInformationInterface, controlInterface);
			controlInterface = DomainObjectFactory.getInstance().createCheckBox();
		} else if(userSelectedControlName.equalsIgnoreCase("RadioButtonControl")) {
			populateControl(controlInformationInterface, controlInterface);
			controlInterface = DomainObjectFactory.getInstance().createRadioButton();
		} else if(userSelectedControlName.equalsIgnoreCase("DateControl")) {
			populateControl(controlInformationInterface, controlInterface);
			controlInterface = DomainObjectFactory.getInstance().createDatePicker();
		}
		return controlInterface;
	}
	/**
	 * This method populates the given ControlInterface using the given ControlInformationInterface.
	 * @param controlInterface Instance of ControlInterface which is populated using the controlInformationInterface.
	 * @param controlInformationInterface Instance of ControlInformationInterface which is used to populate the controlInterface.
	 */
	public void populateControl(ControlInformationInterface controlInformationInterface, ControlInterface controlInterface) {
		if (controlInformationInterface != null && controlInterface != null) {
			controlInterface.setAbstractAttribute(controlInformationInterface.getAbstractAttribute());
			controlInterface.setCaption(controlInformationInterface.getCaption());
			controlInterface.setCssClass(controlInformationInterface.getCssClass());
			controlInterface.setTooltip(controlInformationInterface.getTooltip());
			controlInterface.setIsHidden(controlInformationInterface.getIsHidden());
			controlInterface.setSequenceNumber(controlInformationInterface.getSequenceNumber());
		}
	}
	/**
	 * This method will populate the ControlInformationInterface using the controlInterface so that the 
	 * information of the Control can be shown on the user page using the ControlInformationInterface.
	 * @param controlInterface Instance of controlInterface from which to populate the informationInterface.
	 * @param controlInformationInterface Instance of ControlInformationInterface which will be populated using 
	 * the first parameter that is controlInterface.
	 */
	public void populateControlInformation(String selectedControl ,ControlInterface controlInterface, ControlInformationInterface controlInformationInterface) {
		if (controlInterface != null && controlInformationInterface != null) {
			controlInformationInterface.setAbstractAttribute(controlInterface.getAbstractAttribute());
			controlInformationInterface.setCssClass(controlInterface.getCssClass());
			controlInformationInterface.setTooltip(controlInterface.getTooltip());
			controlInformationInterface.setCaption(controlInterface.getCaption());
			controlInformationInterface.setIsHidden(controlInterface.getIsHidden());
			controlInformationInterface.setSequenceNumber(controlInterface.getSequenceNumber());
		}
	}
}
