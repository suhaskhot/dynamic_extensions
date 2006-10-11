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
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.ui.interfaces.ControlInformationInterface;


/**
 * 
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
	public ControlInterface createControl(String userSelectedControlName) {
		ControlInterface controlInterface = null;
		if(userSelectedControlName.equalsIgnoreCase("TextControl")) {
			controlInterface = DomainObjectFactory.getInstance().createTextField();
		}
		if(userSelectedControlName.equalsIgnoreCase("MultilineControl")) {
			controlInterface = DomainObjectFactory.getInstance().createTextArea();
		}
		if(userSelectedControlName.equalsIgnoreCase("ComboboxControl")) {
			controlInterface = DomainObjectFactory.getInstance().createComboBox();
		}
		if(userSelectedControlName.equalsIgnoreCase("ListBoxControl")) {
			controlInterface = DomainObjectFactory.getInstance().createListBox();
		}
		if(userSelectedControlName.equalsIgnoreCase("CheckboxControl")) {
			controlInterface = DomainObjectFactory.getInstance().createCheckBox();
		}
		if(userSelectedControlName.equalsIgnoreCase("RadioButtonControl")) {
			controlInterface = DomainObjectFactory.getInstance().createRadioButton();
		}
		if(userSelectedControlName.equalsIgnoreCase("DateControl")) {
			controlInterface = DomainObjectFactory.getInstance().createDatePicker();
		}
		return controlInterface;
	}
	/**
     * This method populates the given EntityInterface using the given entityInformationInterface.
     * @param entityInterface Instance of EntityInterface which is populated using the informationInterface.
     * @param entityInformationInterface Instance of EntityInformationInterface which is used to populate the entityInterface.
     */
    public void populateControl (ControlInformationInterface controlInformationInterface, ControlInterface controlInterface) {
        if (controlInformationInterface != null && controlInterface != null) {
        }
    }







}
