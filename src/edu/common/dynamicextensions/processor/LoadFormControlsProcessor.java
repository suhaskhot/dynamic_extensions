
package edu.common.dynamicextensions.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.ui.util.ControlConfigurationsFactory;
import edu.common.dynamicextensions.ui.util.ControlsUtility;
import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
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
				editControl(selectedControl, controlsForm);
			}

			String userSelectedTool = controlsForm.getUserSelectedTool();

			//Initialize default values for controls
			initializeControlDefaultValues(userSelectedTool, controlsForm);

			//List of tools/controls
			controlsForm.setToolsList(controlConfigurationsFactory.getListOfControls());
			controlsForm.setSelectedControlCaption(ControlsUtility.getControlCaption(controlConfigurationsFactory.getControlDisplayLabel(userSelectedTool)));
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
			controlsForm.setChildList(ControlsUtility.getChildList(containerInterface));
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
	private void editControl(ControlInterface controlInterface, ControlsForm controlsForm)
	{
		ControlProcessor controlProcessor = ControlProcessor.getInstance();
		controlProcessor.populateControlUIBeanInterface(controlInterface, controlsForm);

		AttributeProcessor attributeProcessor = AttributeProcessor.getInstance();
		if (controlInterface != null)
		{
			attributeProcessor.populateAttributeUIBeanInterface(controlInterface.getAbstractAttribute(), controlsForm);
		}

		String userSelectedTool = DynamicExtensionsUtility.getControlName(controlInterface);
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
		if ((userSelectedTool != null) && (controlsForm != null))
		{
			if (userSelectedTool.equals(ProcessorConstants.TEXT_CONTROL))
			{
				initializeTextControlDefaultValues(controlsForm);
			}
			else if (userSelectedTool.equals(ProcessorConstants.COMBOBOX_CONTROL))
			{
				initializeComboboxControlDefaultValues(controlsForm);
			}
			else if (userSelectedTool.equals(ProcessorConstants.DATEPICKER_CONTROL))
			{
				initializeDatePickerControlDefaultValues(controlsForm);
			}
			else if (userSelectedTool.equals(ProcessorConstants.CHECKBOX_CONTROL))
			{
				initializeCheckBoxControlDefaultValues(controlsForm);
			}
			else if (userSelectedTool.equals(ProcessorConstants.RADIOBUTTON_CONTROL))
			{
				initializeOptionButtonControlDefaultValues(controlsForm);
			}
			else if (userSelectedTool.equals(ProcessorConstants.FILEUPLOAD_CONTROL))
			{
				initializeFileUploadControlDefaultValues(controlsForm);
			}
		}
	}

	/**
	 * 
	 */
	private void initializeFileUploadControlDefaultValues(ControlsForm controlsForm)
	{
		if (controlsForm.getSupportedFileFormatsList() == null)
		{
			controlsForm.setSupportedFileFormatsList(getFileFormatsList());
		}
		initializeFileFormats(controlsForm);
	}
	/**
	 * This method will separate out the file formats explicitly specified by the user
	 * from the supported file format list. 
	 * @param controlsForm
	 */
	private void initializeFileFormats(ControlsForm controlsForm)
	{
		String unsupportedFileFormatList = null;
		List<String> supportedFileFormats = controlsForm.getSupportedFileFormatsList();
		String[] userSelectedFileFormats = controlsForm.getFileFormats();
		if(userSelectedFileFormats!=null)
		{
			int noOfSelectedFormats = userSelectedFileFormats.length;
			String selectedFileFormat = null;
			for(int i=0;i<noOfSelectedFormats;i++)
			{
				selectedFileFormat = userSelectedFileFormats[i];
				if(!DynamicExtensionsUtility.isStringInList(selectedFileFormat, supportedFileFormats))
				{
					if((unsupportedFileFormatList==null)||(unsupportedFileFormatList.equals("")))
					{
						unsupportedFileFormatList = selectedFileFormat;
					}
					else
					{
						unsupportedFileFormatList = unsupportedFileFormatList + ProcessorConstants.FILE_FORMATS_SEPARATOR  + selectedFileFormat;
					}
				}
			}
		}
		controlsForm.setFormat(unsupportedFileFormatList);
	}

	/**
	 * @return list of file formats available for file control
	 */
	private List getFileFormatsList()
	{
		ArrayList<String> fileFormatsList = new ArrayList<String>();
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
		//Set default display choice 
		if (controlsForm.getDisplayChoice() == null)
		{
			controlsForm.setDisplayChoice(ProcessorConstants.DEFAULT_DISPLAY_CHOICE_TYPE);
		}
	}

	/**
	 * 
	 */
	private void initializeCheckBoxControlDefaultValues(ControlsForm controlsForm)
	{
		if((controlsForm.getAttributeDefaultValue()==null)||((controlsForm.getAttributeDefaultValue().trim().equals(""))))
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
		if (controlsForm.getDateValueType() == null)
		{
			controlsForm.setDateValueType(ProcessorConstants.DEFAULT_DATE_VALUE);
		}
		//Date format
		if (controlsForm.getFormat() == null)
		{
			controlsForm.setFormat(ProcessorConstants.DEFAULT_DATE_FORMAT);
		}
	}

	/**
	 * 
	 */
	private void initializeComboboxControlDefaultValues(ControlsForm controlsForm)
	{
		//Set default display choice
		if (controlsForm.getDisplayChoice() == null)
		{
			controlsForm.setDisplayChoice(ProcessorConstants.DEFAULT_DISPLAY_CHOICE_TYPE);
		}
		//Default list type
		if (controlsForm.getAttributeMultiSelect() == null)
		{
			controlsForm.setAttributeMultiSelect(ProcessorConstants.DEFAULT_LIST_TYPE);
		}
		if (controlsForm.getFormTypeForLookup() == null)
		{
			controlsForm.setFormTypeForLookup(ProcessorConstants.DEFAULT_LOOKUP_TYPE);
		}
	}

	/**
	 * @return
	 */
	private List getGroupNamesList()
	{
		ArrayList<String> groupNamesList = new ArrayList<String>();
		groupNamesList.add("Group1");
		groupNamesList.add("Group2");
		groupNamesList.add("Group3");
		groupNamesList.add("Group4");
		groupNamesList.add("Group5");
		return groupNamesList;
	}

	/**
	 * 
	 */
	private void initializeTextControlDefaultValues(ControlsForm controlsForm)
	{
		//Default Data type
		if (controlsForm.getDataType() == null)
		{
			controlsForm.setDataType(ProcessorConstants.DEFAULT_DATA_TYPE);
		}
		//Default single line type
		if (controlsForm.getLinesType() == null)
		{
			controlsForm.setLinesType(ProcessorConstants.DEFAULT_LINE_TYPE);
		}

		//measurement units list
		if (controlsForm.getMeasurementUnitsList() == null)
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
	 * 
	 * @return List DisplayChoiceList
	 */
	private List getOptionSpecificationMethods()
	{
		List<NameValueBean> optionSpecificationMethods = new ArrayList<NameValueBean>();
		NameValueBean nameValueBean1 = new NameValueBean("UserDefined", "User Defined");
		optionSpecificationMethods.add(nameValueBean1);
		
		 NameValueBean nameValueBean2 = new NameValueBean("CADSR","CDE");
		 optionSpecificationMethods.add(nameValueBean2);
		 
		 NameValueBean nameValueBean3 = new NameValueBean("Lookup","Look Up");
		 optionSpecificationMethods.add(nameValueBean3);

		return optionSpecificationMethods;
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