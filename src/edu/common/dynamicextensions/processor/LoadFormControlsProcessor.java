
package edu.common.dynamicextensions.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

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
	 * @return EntityProcessor EntityProcessor instance
	 */
	public static LoadFormControlsProcessor getInstance()
	{
		return new LoadFormControlsProcessor();
	}

	/**
	 * 
	 * @param actionForm
	 * @param containerInterface
	 * @throws DynamicExtensionsSystemException dynamicExtensionsSystemException
	 */
	public void loadFormControls(ControlsForm controlsForm, ContainerInterface containerInterface) throws DynamicExtensionsSystemException
	{
		if ((containerInterface != null) && (controlsForm != null))
		{
			String controlOperation = controlsForm.getControlOperation();
			String userSelectedTool = controlsForm.getUserSelectedTool();

			ControlConfigurationsFactory controlConfigurationsFactory = ControlConfigurationsFactory.getInstance();
			if (controlOperation == null || controlOperation.equals(""))
			{
				controlOperation = ProcessorConstants.OPERATION_ADD;
				controlsForm.setControlOperation(controlOperation);
			}
			if (controlOperation.equalsIgnoreCase(ProcessorConstants.OPERATION_ADD))
			{
				if (userSelectedTool == null || userSelectedTool.equals(""))
				{
					userSelectedTool = ProcessorConstants.DEFAULT_SELECTED_CONTROL;
				}
				//Default Data type
				controlsForm.setDataType(ProcessorConstants.DEFAULT_DATA_TYPE);

				//Set default display choice 
				controlsForm.setDisplayChoice(ProcessorConstants.DEFAULT_DISPLAY_CHOICE_TYPE);

				//Default single line type
				controlsForm.setLinesType(ProcessorConstants.DEFAULT_LINE_TYPE);

				//Default list type
				controlsForm.setAttributeMultiSelect(ProcessorConstants.DEFAULT_LIST_TYPE);

				//Date value type
				controlsForm.setDateValueType(ProcessorConstants.DEFAULT_DATE_VALUE);
				//Date format
				controlsForm.setFormat(ProcessorConstants.DEFAULT_DATE_FORMAT);

				controlsForm.setControlRuleMap(getControlRulesMap(userSelectedTool));
				//TODO check if need to be changed
			}

			else if (controlOperation.equalsIgnoreCase(ProcessorConstants.OPERATION_EDIT))
			{
				String selectedControlId = controlsForm.getSelectedControlId();
				ControlProcessor controlProcessor = ControlProcessor.getInstance();
				ControlInterface controlInterface = containerInterface.getControlInterfaceBySequenceNumber(selectedControlId);
				controlProcessor.populateControlUIBeanInterface(controlInterface, controlsForm);

				AttributeProcessor attributeProcessor = AttributeProcessor.getInstance();
				if (controlInterface != null)
				{
					attributeProcessor.populateAttributeUIBeanInterface(controlInterface.getAbstractAttribute(), controlsForm);
				}

				userSelectedTool = getControlName(controlInterface);
				if (userSelectedTool == null || userSelectedTool.equals(""))
				{
					userSelectedTool = ProcessorConstants.DEFAULT_SELECTED_CONTROL;
				}
			}
			controlsForm.setUserSelectedTool(userSelectedTool);
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

			controlsForm.setDisplayChoiceList(getDisplayChoiceList());

			if (controlsForm.getAttributeMultiSelect() == null)
			{
				controlsForm.setAttributeMultiSelect(ProcessorConstants.DEFAULT_LIST_TYPE);
			}
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
			//measurement units list
			if (controlsForm.getMeasurementUnitsList() == null)
			{
				controlsForm.setMeasurementUnitsList(getListOfMeasurementUnits());
			}
		}
	}

	/**
	 * @return
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
	 * @param containerInterface
	 * @return
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
	 * @param containerInterface
	 * @return
	 * @throws DynamicExtensionsSystemException 
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
	 * @param controlName
	 * @param dataTypeName
	 * @return
	 * @throws DynamicExtensionsSystemException dynamicExtensionsSystemException
	 */
	private Map getControlRulesMap(String controlName) throws DynamicExtensionsSystemException
	{
		ControlConfigurationsFactory ccf = ControlConfigurationsFactory.getInstance();
		return ccf.getRulesMap(controlName);
	}
}