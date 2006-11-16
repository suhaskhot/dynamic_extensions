/*
 * Created on Nov 14, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.ui.webui.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.CheckBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ComboBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.DatePickerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.FileUploadInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ListBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.RadioButtonInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextAreaInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextFieldInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.ui.util.ControlConfigurationsFactory;
import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.ui.webui.util.ControlInformationObject;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class SelectControlAction extends BaseDynamicExtensionsAction
{

	/**
	 * @param mapping ActionMapping mapping
	 * @param form ActionForm form
	 * @param  request HttpServletRequest request
	 * @param response HttpServletResponse response
	 * @return ActionForward forward to next action
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	{
		try
		{
			//Get controls form
			ControlsForm controlsForm = (ControlsForm) form;
			String userSelectedTool = controlsForm.getUserSelectedTool();
			if (userSelectedTool == null || userSelectedTool.equals(""))
			{
				userSelectedTool = ProcessorConstants.DEFAULT_SELECTED_CONTROL;
			}
			controlsForm.setUserSelectedTool(userSelectedTool);
			
			ControlConfigurationsFactory controlConfigurationsFactory = ControlConfigurationsFactory.getInstance();
			
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

			ContainerInterface containerInterface = (ContainerInterface) CacheManager.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);
			
			//Set Entity Name as root
			if(containerInterface!=null)
			{
				EntityInterface entity = containerInterface.getEntity();
				if (entity != null)
				{
					controlsForm.setRootName(entity.getName());
				}
				else
				{
					controlsForm.setRootName("");
				}
			}
//			Initialize default values for controls
			initializeControlDefaultValues(userSelectedTool,controlsForm);
			
			controlsForm.setChildList(getChildList(containerInterface));
			controlsForm.setControlRuleMap(controlConfigurationsFactory.getRulesMap(userSelectedTool));
			return mapping.findForward("success");
		}
		catch (DynamicExtensionsSystemException e)
		{
			String actionForwardString = catchException(e,request);
			return(mapping.findForward(actionForwardString));
		}
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
			else if (controlInterface instanceof ListBoxInterface)
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

	
}