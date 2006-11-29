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
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CheckBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ComboBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.DatePickerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.FileUploadInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ListBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.RadioButtonInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextAreaInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextFieldInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
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
	protected ControlProcessor()
	{

	}

	/**
	 * this method gets the new instance of the ControlProcessor to the caller.
	 * @return ControlProcessor ControlProcessor instance
	 */
	public static ControlProcessor getInstance()
	{
		return new ControlProcessor();
	}

	/**
	 * 
	 * @param userSelectedControlName : Name of the User Selected Control 
	 * @param controlUIBeanInterface : Control UI Information interface containing information added by user on UI
	 * @return : Control interface populated with required information
	 * @throws DynamicExtensionsSystemException 
	 */
	public ControlInterface createAndPopulateControl(String userSelectedControlName, ControlUIBeanInterface controlUIBeanInterface) throws DynamicExtensionsSystemException
	{
		ControlInterface controlInterface = populateControlInterface(userSelectedControlName, null, controlUIBeanInterface);
		return controlInterface;
	}

	/**
	 * 
	 * @param userSelectedControlName : Name of the User Selected Control 
	 * @param controlIntf : Control Interface (Domain Object Interface)
	 * @param controlUIBeanInterface : Control UI Information interface containing information added by user on UI
	 * @return : Control interface populated with required information
	 * @throws DynamicExtensionsSystemException 
	 */
	public ControlInterface populateControlInterface(String userSelectedControlName, ControlInterface controlIntf,
			ControlUIBeanInterface controlUIBeanInterface) throws DynamicExtensionsSystemException
	{
		ControlInterface controlInterface = null;
		if ((userSelectedControlName != null) && (controlUIBeanInterface != null))
		{
			if (userSelectedControlName.equalsIgnoreCase(ProcessorConstants.TEXT_CONTROL))
			{
				if (controlUIBeanInterface.getLinesType() != null && controlUIBeanInterface.getLinesType().equalsIgnoreCase("MultiLine"))
				{
					controlInterface = getMultiLineControl(controlIntf, controlUIBeanInterface);
				}
				else
				{
					controlInterface = getTextControl(controlIntf, controlUIBeanInterface);
				}
			}
			else if (userSelectedControlName.equalsIgnoreCase(ProcessorConstants.COMBOBOX_CONTROL))
			{
				if ((controlUIBeanInterface.getIsMultiSelect() != null) && (controlUIBeanInterface.getIsMultiSelect().booleanValue() == true))
				{
					controlInterface = getListBoxControl(controlIntf, controlUIBeanInterface);
				}
				else
				{
					controlInterface = getComboBoxControl(controlIntf, controlUIBeanInterface);
				}
			}
			else if (userSelectedControlName.equalsIgnoreCase(ProcessorConstants.LISTBOX_CONTROL))
			{
				controlInterface = getListBoxControl(controlIntf, controlUIBeanInterface);
			}
			else if (userSelectedControlName.equalsIgnoreCase(ProcessorConstants.CHECKBOX_CONTROL))
			{
				controlInterface = getCheckBoxControl(controlIntf, controlUIBeanInterface);
			}
			else if (userSelectedControlName.equalsIgnoreCase(ProcessorConstants.RADIOBUTTON_CONTROL))
			{
				controlInterface = getRadioButtonControl(controlIntf, controlUIBeanInterface);
			}
			else if (userSelectedControlName.equalsIgnoreCase(ProcessorConstants.DATEPICKER_CONTROL))
			{
				controlInterface = getDatePickerControl(controlIntf, controlUIBeanInterface);
			}
			else if (userSelectedControlName.equalsIgnoreCase(ProcessorConstants.FILEUPLOAD_CONTROL))
			{
				controlInterface = getFileUploadControl(controlIntf, controlUIBeanInterface);
			}
		}
		//Load common properties for controls
		if (controlUIBeanInterface != null && controlInterface != null)
		{
			controlInterface.setAbstractAttribute(controlUIBeanInterface.getAbstractAttribute());
			controlInterface.setCaption(controlUIBeanInterface.getCaption());
			controlInterface.setIsHidden(controlUIBeanInterface.getIsHidden());
		}

		/*AbstractAttributeInterface abstractAttribute = controlInterface.getAbstractAttribute();
		if (abstractAttribute != null && controlUIBeanInterface.getValidationRules() != null && controlUIBeanInterface.getValidationRules().length == 0)
		{
			Collection<RuleInterface> ruleCollection = abstractAttribute.getRuleCollection();
			Collection tempRuleCollection; 
			if (ruleCollection == null ||  ruleCollection.size() == 0)
			{
				ControlConfigurationsFactory controlConfigurationsFactory = ControlConfigurationsFactory.getInstance();
				if (userSelectedControlName != null)
				{
					tempRuleCollection = controlConfigurationsFactory.getAllImplicitRules(userSelectedControlName.trim());
					Iterator tempRuleIterator = tempRuleCollection.iterator();
					RuleInterface ruleInterface;
					while(tempRuleIterator.hasNext())
					{
						ruleInterface = (RuleInterface) tempRuleIterator.next();
						if(ruleInterface.getName() != null && !ruleInterface.getName().equals(""))
						{
							abstractAttribute.addRule(ruleInterface);
						}
						
					}
					
					
				}
			}
		}*/

		return controlInterface;

	}

	/** 
	 * @param controlIntf : Control Interface (Domain Object Interface)
	 * @param controlUIBeanInterface : Control UI Information interface containing information added by user on UI
	 * @return : Control interface populated with required information for File upload
	 */
	private ControlInterface getFileUploadControl(ControlInterface controlInterface, ControlUIBeanInterface controlUIBeanInterface)
	{
		FileUploadInterface fileUploadInterface = null;
		if (controlInterface == null)
		{
			fileUploadInterface = DomainObjectFactory.getInstance().createFileUploadControl();
		}
		else
		{
			fileUploadInterface = (FileUploadInterface) controlInterface;
		}
		fileUploadInterface.setColumns(controlUIBeanInterface.getColumns());
		
		return fileUploadInterface;
	}

	/**
	 * @param controlInterface : Control Interface (Domain Object Interface)
	 * @param controlUIBeanInterface : Control UI Information interface containing information added by user on UI
	 * @return : Control interface populated with required information for date 
	 */
	private ControlInterface getDatePickerControl(ControlInterface controlInterface, ControlUIBeanInterface controlUIBeanInterface)
	{
		DatePickerInterface datePickerIntf = null;
		if (controlInterface == null)
		{
			datePickerIntf = DomainObjectFactory.getInstance().createDatePicker();
		}
		else
		{
			datePickerIntf = (DatePickerInterface) controlInterface;
		}
		datePickerIntf.setDateValueType(controlUIBeanInterface.getDateValueType());
		return datePickerIntf;
	}

	/**
	 * @param controlInterface : Control Interface (Domain Object Interface)
	 * @param controlUIBeanInterface : Control UI Information interface containing information added by user on UI
	 * @return : Control interface populated with required information for radiobutton
	 */
	private ControlInterface getRadioButtonControl(ControlInterface controlInterface, ControlUIBeanInterface controlUIBeanInterface)
	{
		RadioButtonInterface radioButtonIntf = null;
		if (controlInterface == null)
		{
			radioButtonIntf = DomainObjectFactory.getInstance().createRadioButton();
		}
		else
		{
			radioButtonIntf = (RadioButtonInterface) controlInterface;
		}

		return radioButtonIntf;
	}

	/**
	 * @param controlInterface : Control Interface (Domain Object Interface)
	 * @param controlUIBeanInterface : Control UI Information interface containing information added by user on UI
	 * @return : Control interface populated with required information for checkbox
	 */
	private ControlInterface getCheckBoxControl(ControlInterface controlInterface, ControlUIBeanInterface controlUIBeanInterface)
	{
		CheckBoxInterface checkBox = null;
		if (controlInterface == null)
		{
			checkBox = DomainObjectFactory.getInstance().createCheckBox();
		}
		else
		{
			checkBox = (CheckBoxInterface) controlInterface;
		}

		return checkBox;
	}

	/**
	 * @param controlInterface : Control Interface (Domain Object Interface)
	 * @param controlUIBeanInterface : Control UI Information interface containing information added by user on UI
	 * @return : Control interface populated with required information for list box
	 */
	private ControlInterface getListBoxControl(ControlInterface controlInterface, ControlUIBeanInterface controlUIBeanInterface)
	{
		ListBoxInterface listBoxIntf = null;
		if (controlInterface == null) //If does not exist create it 
		{
			listBoxIntf = DomainObjectFactory.getInstance().createListBox();
		}
		else
		{
			if (controlInterface instanceof ListBoxInterface)
			{
				listBoxIntf = (ListBoxInterface) controlInterface;
			}
			else
			{
				listBoxIntf = DomainObjectFactory.getInstance().createListBox();
			}
		}
		listBoxIntf.setIsMultiSelect(controlUIBeanInterface.getIsMultiSelect());
		listBoxIntf.setNoOfRows(controlUIBeanInterface.getRows());
		//Set isCollection=true in the attribute
		AttributeInterface controlAttribute = (AttributeInterface) controlUIBeanInterface.getAbstractAttribute();
		if (controlAttribute != null)
		{
			controlAttribute.setIsCollection(new Boolean(true));
		}
		return listBoxIntf;
	}

	/**
	 * @param controlInterface : Control Interface (Domain Object Interface)
	 * @param controlUIBeanInterface : Control UI Information interface containing information added by user on UI
	 * @return : Control interface populated with required information for Combobox
	 */
	private ControlInterface getComboBoxControl(ControlInterface controlInterface, ControlUIBeanInterface controlUIBeanInterface)
	{
		/*ComboBoxInterface comboBoxIntf = null;
		 if (controlInterface == null)
		 {
		 comboBoxIntf = DomainObjectFactory.getInstance().createComboBox();
		 }
		 else
		 {
		 comboBoxIntf = (ComboBoxInterface) controlInterface;
		 }
		 return comboBoxIntf;*/

		ComboBoxInterface comboBoxIntf = null;
		if (controlInterface == null) //If does not exist create it 
		{
			comboBoxIntf = DomainObjectFactory.getInstance().createComboBox();
		}
		else
		{
			if (controlInterface instanceof ComboBoxInterface)
			{
				comboBoxIntf = (ComboBoxInterface) controlInterface;
			}
			else
			{
				comboBoxIntf = DomainObjectFactory.getInstance().createComboBox();
			}
		}
		return comboBoxIntf;

	}

	/**
	 * @param controlInterface : Control Interface (Domain Object Interface)
	 * @param controlUIBeanInterface : Control UI Information interface containing information added by user on UI
	 * @return : Control interface populated with required information for Text area(Multiline textbox)
	 */
	private ControlInterface getMultiLineControl(ControlInterface controlInterface, ControlUIBeanInterface controlUIBeanInterface)
	{
		TextAreaInterface textAreaIntf = null;
		if (controlInterface == null) //If does not exist create it 
		{
			textAreaIntf = DomainObjectFactory.getInstance().createTextArea();
		}
		else
		{
			if (controlInterface instanceof TextAreaInterface)
			{
				textAreaIntf = (TextAreaInterface) controlInterface;
			}
			else
			{
				textAreaIntf = DomainObjectFactory.getInstance().createTextArea();
			}
		}
		textAreaIntf.setColumns(controlUIBeanInterface.getColumns());
		textAreaIntf.setRows(controlUIBeanInterface.getRows());
		return textAreaIntf;
	}

	/**
	 * Creates a new TextControl if control interface is null
	 * Updates the existing if not null
	 * @param controlInterface : Control Interface (Domain Object Interface)
	 * @param controlUIBeanInterface : Control UI Information interface containing information added by user on UI
	 * @return : Control interface populated with required information for text box
	 */
	private ControlInterface getTextControl(ControlInterface controlInterface, ControlUIBeanInterface controlUIBeanInterface)
	{
		TextFieldInterface textFldIntf = null;
		if (controlInterface == null) //If does not exist create it 
		{
			textFldIntf = DomainObjectFactory.getInstance().createTextField();
		}
		else
		{
			if (controlInterface instanceof TextFieldInterface)
			{
				textFldIntf = (TextFieldInterface) controlInterface;
			}
			else
			{
				textFldIntf = DomainObjectFactory.getInstance().createTextField();
			}
		}
		textFldIntf.setIsPassword(controlUIBeanInterface.getIsPassword());
		textFldIntf.setColumns(controlUIBeanInterface.getColumns());

		return textFldIntf;
	}

	/**
	 * This method will populate the ControlUIBeanInterface using the controlInterface so that the 
	 * information of the Control can be shown on the user page using the ControlUIBeanInterface.
	 * @param controlInterface Instance of controlInterface from which to populate the informationInterface.
	 * @param controlUIBeanInterface Instance of ControlUIBeanInterface which will be populated using 
	 * the first parameter that is controlInterface.
	 */
	public void populateControlUIBeanInterface(ControlInterface controlInterface, ControlUIBeanInterface controlUIBeanInterface)
	{
		if (controlInterface != null && controlUIBeanInterface != null)
		{
			controlUIBeanInterface.setAbstractAttribute(controlInterface.getAbstractAttribute());
			controlUIBeanInterface.setCaption(controlInterface.getCaption());
			controlUIBeanInterface.setIsHidden(controlInterface.getIsHidden());
			controlUIBeanInterface.setSequenceNumber(controlInterface.getSequenceNumber());
		}
		if (controlInterface instanceof TextFieldInterface)
		{
			controlUIBeanInterface.setColumns(((TextFieldInterface) controlInterface).getColumns());
			controlUIBeanInterface.setIsPassword(((TextFieldInterface) controlInterface).getIsPassword());
			controlUIBeanInterface.setLinesType(ProcessorConstants.LINE_TYPE_SINGLELINE);
		}
		else if (controlInterface instanceof DatePickerInterface)
		{
			controlUIBeanInterface.setDateValueType(((DatePickerInterface) controlInterface).getDateValueType());
		}
		else if (controlInterface instanceof TextAreaInterface)
		{
			controlUIBeanInterface.setColumns(((TextAreaInterface) controlInterface).getColumns());
			controlUIBeanInterface.setRows(((TextAreaInterface) controlInterface).getRows());
			controlUIBeanInterface.setLinesType(ProcessorConstants.LINE_TYPE_MULTILINE);
			controlUIBeanInterface.setIsPassword(((TextAreaInterface) controlInterface).getIsPassword());
		}
		else if (controlInterface instanceof ListBoxInterface)
		{
			controlUIBeanInterface.setIsMultiSelect(((ListBoxInterface) controlInterface).getIsMultiSelect());
			controlUIBeanInterface.setRows(((ListBoxInterface) controlInterface).getNoOfRows());
		}
		/*else if (controlInterface instanceof ComboBoxInterface)
		 {
		 //Do things specific for combobox control
		 }
		 else if (controlInterface instanceof CheckBoxInterface)
		 {

		 }
		 else if (controlInterface instanceof RadioButtonInterface)
		 {

		 }*/
		else if (controlInterface instanceof FileUploadInterface)
		{
			controlUIBeanInterface.setColumns(((FileUploadInterface) controlInterface).getColumns());
		}
	}
}