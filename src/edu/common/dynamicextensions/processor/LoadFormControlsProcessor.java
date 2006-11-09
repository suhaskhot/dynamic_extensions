
package edu.common.dynamicextensions.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import com.puppycrawl.tools.checkstyle.api.FileSetCheck;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CheckBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ComboBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.DatePickerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.FileUploadInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.RadioButtonInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextAreaInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextFieldInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.util.ControlConfigurationsFactory;
import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;
import edu.common.dynamicextensions.ui.webui.util.ControlInformationObject;
import edu.wustl.common.beans.NameValueBean;

/**
 * @author sujay_narkar
 *
 */
public class LoadFormControlsProcessor
{

	/**
	 * Protected constructor for entity processor
	 *
	 */
	protected LoadFormControlsProcessor()
	{

	}

	/**
	 * this method gets the new instance of the entity processor to the caller.
	 * @return LoadFormControlsProcessor LoadFormControlsProcessor instance
	 */
	public static LoadFormControlsProcessor getInstance()
	{
		return new LoadFormControlsProcessor();
	}

	/**
	 * 
	 * @param controlsForm ControlsForm
	 * @param containerInterface ContainerInterface
	 * @throws DynamicExtensionsSystemException dynamicExtensionsSystemException
	 */
	public void loadFormControls(ControlsForm controlsForm, ContainerInterface containerInterface) throws DynamicExtensionsSystemException
	{
		if ((containerInterface != null) && (controlsForm != null))
		{
			String controlOperation = controlsForm.getControlOperation();


			ControlConfigurationsFactory controlConfigurationsFactory = ControlConfigurationsFactory.getInstance();
			if (controlOperation == null || controlOperation.equals(""))
			{
				controlOperation = ProcessorConstants.OPERATION_ADD;
				controlsForm.setControlOperation(controlOperation);
			}
			if (controlOperation.equalsIgnoreCase(ProcessorConstants.OPERATION_ADD))
			{
				addControl(controlsForm);
			}

			else if (controlOperation.equalsIgnoreCase(ProcessorConstants.OPERATION_EDIT))
			{
				String selectedControlId = controlsForm.getSelectedControlId();
				ControlInterface selectedControl = containerInterface.getControlInterfaceBySequenceNumber(selectedControlId);
				editControl(selectedControl,controlsForm);
			}
			
			String userSelectedTool = controlsForm.getUserSelectedTool();
			
			//Initialize default values for controls
			initializeControlDefaultValues(userSelectedTool,controlsForm);
			
			//List of tools/controls
			controlsForm.setToolsList(controlConfigurationsFactory.getListOfControls());
			controlsForm.setSelectedControlCaption(getControlCaption(controlConfigurationsFactory.getControlDisplayLabel(userSelectedTool)));
			String jspName = controlConfigurationsFactory.getControlJspName(userSelectedTool);
			if (jspName == null)
			{
				jspName = "";
			}
			controlsForm.setHtmlFile(jspName);
			//Data types for selected control
			controlsForm.setDataTypeList(controlConfigurationsFactory.getControlsDataTypes(userSelectedTool));

			//Set Entity Name as root
			EntityInterface entity = containerInterface.getEntity();
			if (entity != null)
			{
				controlsForm.setRootName(entity.getName());
			}
			else
			{
				controlsForm.setRootName("");
			}
			controlsForm.setChildList(getChildList(containerInterface));
			controlsForm.setControlRuleMap(getControlRulesMap(userSelectedTool));
		}
	}

	/**
	 * @param controlsForm
	 */
	private void addControl(ControlsForm controlsForm)
	{
		String userSelectedTool = controlsForm.getUserSelectedTool();
		if (userSelectedTool == null || userSelectedTool.equals(""))
		{
			userSelectedTool = ProcessorConstants.DEFAULT_SELECTED_CONTROL;
		}
		controlsForm.setUserSelectedTool(userSelectedTool);
	}

	/**
	 * 
	 */
	private void editControl(ControlInterface controlInterface,ControlsForm controlsForm)
	{
		ControlProcessor controlProcessor = ControlProcessor.getInstance();
		controlProcessor.populateControlUIBeanInterface(controlInterface, controlsForm);

		AttributeProcessor attributeProcessor = AttributeProcessor.getInstance();
		if (controlInterface != null)
		{
			attributeProcessor.populateAttributeUIBeanInterface(controlInterface.getAbstractAttribute(), controlsForm);
		}

		String userSelectedTool = getControlName(controlInterface);
		if (userSelectedTool == null || userSelectedTool.equals(""))
		{
			userSelectedTool = ProcessorConstants.DEFAULT_SELECTED_CONTROL;
		}
		controlsForm.setUserSelectedTool(userSelectedTool);

	}

	/**
	 * @param userSelectedTool 
	 * @param controlsForm
	 */
	private void initializeControlDefaultValues(String userSelectedTool, ControlsForm controlsForm)
	{
		if((userSelectedTool!=null)&&(controlsForm!=null))
		{
			if(userSelectedTool.equals(ProcessorConstants.TEXT_CONTROL))
			{
				initializeTextControlDefaultValues(controlsForm);
			}else if(userSelectedTool.equals(ProcessorConstants.COMBOBOX_CONTROL))
			{
				initializeComboboxControlDefaultValues(controlsForm);
			}
			else if(userSelectedTool.equals(ProcessorConstants.DATEPICKER_CONTROL))
			{
				initializeDatePickerControlDefaultValues(controlsForm);
			}
			else if(userSelectedTool.equals(ProcessorConstants.CHECKBOX_CONTROL))
			{
				initializeCheckBoxControlDefaultValues(controlsForm);
			}
			else if(userSelectedTool.equals(ProcessorConstants.RADIOBUTTON_CONTROL))
			{
				initializeOptionButtonControlDefaultValues(controlsForm);
			}
			else if(userSelectedTool.equals(ProcessorConstants.FILEUPLOAD_CONTROL))
			{
				initializeFileUploadControlDefaultValues(controlsForm);
			}
			/*controlsForm.setValidationRules(new String[] {""});
			controlsForm.setTempValidationRules(new String[] {""});*/
		}
	}

	/**
	 * 
	 */
	private void initializeFileUploadControlDefaultValues(ControlsForm controlsForm)
	{
		if(controlsForm.getFileFormatsList()==null)
		{
			controlsForm.setFileFormatsList(getFileFormatsList());
		}
	}

	/**
	 * @return
	 */
	private List getFileFormatsList()
	{
		ArrayList fileFormatsList = new ArrayList();
		fileFormatsList.add("bmp");
		fileFormatsList.add("jpeg");
		fileFormatsList.add("gif");
		fileFormatsList.add("doc");
		fileFormatsList.add("xls");
		fileFormatsList.add("pdf");
		return fileFormatsList;
	}

	/**
	 * 
	 */
	private void initializeOptionButtonControlDefaultValues(ControlsForm controlsForm)
	{
		//List of display choices
		if(controlsForm.getDisplayChoiceList()==null)
		{
			controlsForm.setDisplayChoiceList(getDisplayChoiceList());
		}
		//Set default display choice 
		if(controlsForm.getDisplayChoice()==null)
		{
			controlsForm.setDisplayChoice(ProcessorConstants.DEFAULT_DISPLAY_CHOICE_TYPE);
		}
	}

	/**
	 * 
	 */
	private void initializeCheckBoxControlDefaultValues(ControlsForm controlsForm)
	{
		if(controlsForm.getAttributeDefaultValue()==null)
		{
			controlsForm.setAttributeDefaultValue(ProcessorConstants.DEFAULT_CHECKBOX_VALUE);
		}
	}

	/**
	 * 
	 */
	private void initializeDatePickerControlDefaultValues(ControlsForm controlsForm)
	{
		//Date value type
		if(controlsForm.getDateValueType()==null)
		{
			controlsForm.setDateValueType(ProcessorConstants.DEFAULT_DATE_VALUE);
		}
		//Date format
		if(controlsForm.getFormat()==null)
		{
			controlsForm.setFormat(ProcessorConstants.DEFAULT_DATE_FORMAT);
		}
	}

	/**
	 * 
	 */
	private void initializeComboboxControlDefaultValues(ControlsForm controlsForm)
	{
		//List of display choices
		if(controlsForm.getDisplayChoiceList()==null)
		{
			controlsForm.setDisplayChoiceList(getDisplayChoiceList());
		}
		//Set default display choice
		if(controlsForm.getDisplayChoice()==null)
		{
			controlsForm.setDisplayChoice(ProcessorConstants.DEFAULT_DISPLAY_CHOICE_TYPE);
		}
		//Default list type
		if(controlsForm.getAttributeMultiSelect()==null)
		{
			controlsForm.setAttributeMultiSelect(ProcessorConstants.DEFAULT_LIST_TYPE);
		}
	}

	/**
	 * 
	 */
	private void initializeTextControlDefaultValues(ControlsForm controlsForm)
	{
		//Default Data type
		if(controlsForm.getDataType()==null)
		{
			controlsForm.setDataType(ProcessorConstants.DEFAULT_DATA_TYPE);
		}
		//Default single line type
		if(controlsForm.getLinesType()==null)
		{
			controlsForm.setLinesType(ProcessorConstants.DEFAULT_LINE_TYPE);
		}

		//measurement units list
		if(controlsForm.getMeasurementUnitsList()==null)
		{
			controlsForm.setMeasurementUnitsList(getListOfMeasurementUnits());
		}
	}

	/**
	 * Gets List of Measurement Units
	 * @return List<String>
	 */
	private List<String> getListOfMeasurementUnits()
	{
		List<String> measurementUnits = new ArrayList<String>();
		measurementUnits.add("inches");
		measurementUnits.add("cm");
		measurementUnits.add("gms");
		measurementUnits.add("kms");
		measurementUnits.add("kgs");
		measurementUnits.add(ProcessorConstants.MEASUREMENT_UNIT_OTHER);
		return measurementUnits;
	}

	/**
	 * @param controlInterface ControlInterface
	 * @return String ControlName
	 */
	private String getControlName(ControlInterface controlInterface)
	{
		if (controlInterface != null)
		{
			if (controlInterface instanceof TextFieldInterface)
			{
				return ProcessorConstants.TEXT_CONTROL;
			}
			else if (controlInterface instanceof ComboBoxInterface)
			{
				return ProcessorConstants.COMBOBOX_CONTROL;
			}
			else if (controlInterface instanceof DatePickerInterface)
			{
				return ProcessorConstants.DATEPICKER_CONTROL;
			}
			else if (controlInterface instanceof TextAreaInterface)
			{
				return ProcessorConstants.TEXT_CONTROL;
			}
			else if (controlInterface instanceof RadioButtonInterface)
			{
				return ProcessorConstants.RADIOBUTTON_CONTROL;
			}
			else if (controlInterface instanceof CheckBoxInterface)
			{
				return ProcessorConstants.CHECKBOX_CONTROL;
			}
			else if (controlInterface instanceof FileUploadInterface)
			{
				return ProcessorConstants.FILEUPLOAD_CONTROL;
			}

		}
		return null;
	}

	/**
	 *  
	 * @param containerInterface containerInterface
	 * @return List ChildList
	 * @throws DynamicExtensionsSystemException DynamicExtensionsSystemException
	 */
	private List getChildList(ContainerInterface containerInterface) throws DynamicExtensionsSystemException
	{
		List<ControlInformationObject> childList = new ArrayList<ControlInformationObject>();
		Collection controlCollection = containerInterface.getControlCollection();
		Iterator controlIterator = controlCollection.iterator();
		ControlInterface controlInterface = null;
		ControlInformationObject controlInformationObject = null;
		String controlCaption = null, controlDatatype = null, controlSequenceNumber = null, controlName = null;
		ControlConfigurationsFactory controlConfigurationsFactory = ControlConfigurationsFactory.getInstance();
		while (controlIterator.hasNext())
		{
			controlInterface = (ControlInterface) controlIterator.next();
			if (controlInterface.getCaption() != null && !controlInterface.getCaption().equals(""))
			{
				controlCaption = controlInterface.getCaption();
				controlSequenceNumber = controlInterface.getSequenceNumber() + "";
				controlName = getControlName(controlInterface);
				controlDatatype = getControlCaption(controlConfigurationsFactory.getControlDisplayLabel(controlName));
				controlInformationObject = new ControlInformationObject(controlCaption, controlDatatype, controlSequenceNumber);
				childList.add(controlInformationObject);
			}

		}
		return childList;
	}

	/**
	 * 
	 * @return List DisplayChoiceList
	 */
	private List getDisplayChoiceList()
	{
		List<NameValueBean> dataTypeList = new ArrayList<NameValueBean>();
		NameValueBean nameValueBean1 = new NameValueBean("UserDefined", "UserDefined");
		dataTypeList.add(nameValueBean1);
		/*
		 NameValueBean nameValueBean2 = new NameValueBean("CADSR","CADSR");
		 dataTypeList.add(nameValueBean2);*/

		return dataTypeList;
	}

	/**
	 * 
	 * @param captionKey String captionKey
	 * @return String ControlCaption
	 */
	public String getControlCaption(String captionKey)
	{
		if (captionKey != null)
		{
			ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationResources");
			if (resourceBundle != null)
			{
				return resourceBundle.getString(captionKey);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param controlName name of the control
	 * @param dataTypeName name of datatype
	 * @return Map ControlRulesMap
	 * @throws DynamicExtensionsSystemException dynamicExtensionsSystemException
	 */
	private Map getControlRulesMap(String controlName) throws DynamicExtensionsSystemException
	{
		ControlConfigurationsFactory ccf = ControlConfigurationsFactory.getInstance();
		return ccf.getRulesMap(controlName);
	}
}