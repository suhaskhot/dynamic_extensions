
package edu.common.dynamicextensions.processor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;

import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ComboBoxInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.DatePickerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextAreaInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.TextFieldInterface;
import edu.common.dynamicextensions.ui.util.ControlConfigurationsFactory;
import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;
import edu.wustl.common.actionForm.AbstractActionForm;
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
	protected LoadFormControlsProcessor () 
	{

	}

	/**
	 * this method gets the new instance of the entity processor to the caller.
	 * @return EntityProcessor EntityProcessor instance
	 */
	public static LoadFormControlsProcessor getInstance () 
	{
		return new LoadFormControlsProcessor();
	}

	/**
	 * 
	 * @param actionForm
	 * @param containerInterface
	 */
	public void loadFormControls(ControlsForm controlsForm,ContainerInterface containerInterface) 
	{
		if((containerInterface!=null)&&(controlsForm!=null))
		{
			String controlOperation = controlsForm.getControlOperation();
			String userSelectedTool = controlsForm.getUserSelectedTool();
			
			ControlConfigurationsFactory controlConfigurationsFactory = ControlConfigurationsFactory.getInstance();
			if(controlOperation == null || controlOperation.equals("") ||
					controlOperation.equalsIgnoreCase(ProcessorConstants.ADD))
			{
				if(userSelectedTool == null || userSelectedTool.equals(""))
				{
					userSelectedTool = ProcessorConstants.DEFAULT_SELECTED_CONTROL;
				}
				//List of tools/controls
				controlsForm.setToolsList(controlConfigurationsFactory.getListOfControls());


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
			}

			else if(controlOperation.equalsIgnoreCase(ProcessorConstants.EDIT))  
			{
				String selectedControlId = controlsForm.getSelectedControlId();
				ControlProcessor controlProcessor = ControlProcessor.getInstance();
				ControlInterface controlInterface = containerInterface.getControlInterfaceBySequenceNumber(selectedControlId);
				controlProcessor.populateControlUIBeanInterface(controlInterface,controlsForm);

				AttributeProcessor attributeProcessor = AttributeProcessor.getInstance();
				if(controlInterface!=null)
				{
					attributeProcessor.populateAttributeUIBeanInterface(controlInterface.getAbstractAttribute(), controlsForm);
				}

				userSelectedTool = getUserSelectedToolName(controlInterface);
				if(userSelectedTool == null || userSelectedTool.equals(""))
				{
					userSelectedTool = ProcessorConstants.DEFAULT_SELECTED_CONTROL;
				}


			}
			controlsForm.setUserSelectedTool(userSelectedTool);
			controlsForm.setSelectedControlCaption(getSelectedControlCaption(controlConfigurationsFactory.getControlDisplayLabel(userSelectedTool)));
			String jspName = controlConfigurationsFactory.getControlJspName(userSelectedTool);
			if(jspName==null)
			{
				jspName = "";
			}

			controlsForm.setHtmlFile(jspName);
			//Data types for selected control
			controlsForm.setDataTypeList(controlConfigurationsFactory.getControlsDataTypes(userSelectedTool));

			controlsForm.setDisplayChoiceList(getDisplayChoiceList());

			if(controlsForm.getAttributeMultiSelect()==null)
			{
				controlsForm.setAttributeMultiSelect(ProcessorConstants.DEFAULT_LIST_TYPE);
			}
			//Set Entity Name as root
			EntityInterface entity = containerInterface.getEntity();
			if(entity!=null)
			{
				controlsForm.setRootName(entity.getName());
			}
			else 
			{
				controlsForm.setRootName("");
			}
			controlsForm.setChildList(getChildList(containerInterface));
		}
	}

	/**
	 * @param containerInterface
	 * @return
	 */
	private String getUserSelectedToolName(ControlInterface controlInterface)
	{
		if(controlInterface!=null)
		{
			if(controlInterface instanceof TextFieldInterface)
			{
				return ProcessorConstants.TEXT_CONTROL;
			}else if(controlInterface instanceof ComboBoxInterface)
			{
				return ProcessorConstants.COMBOBOX_CONTROL;
			}else if(controlInterface instanceof DatePickerInterface)
			{
				return ProcessorConstants.DATEPICKER_CONTROL;
			}else if(controlInterface instanceof TextAreaInterface)
			{
				return ProcessorConstants.TEXT_CONTROL;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param containerInterface
	 * @return
	 */
	private List getChildList(ContainerInterface containerInterface)
	{
		List childList = new ArrayList();
		Collection controlCollection = containerInterface.getControlCollection();
		Iterator controlIterator = controlCollection.iterator();
		ControlInterface controlInterface = null;
		NameValueBean nameValueBean;
		while(controlIterator.hasNext())
		{
			controlInterface =  (ControlInterface) controlIterator.next();
			if(controlInterface.getCaption() != null && !controlInterface.getCaption().equals(""))
			{
				nameValueBean = new NameValueBean(controlInterface.getCaption(),controlInterface.getSequenceNumber());
				childList.add(nameValueBean);
			}

		}
		return childList;
	}

	private List getDisplayChoiceList()
	{
		List dataTypeList = new ArrayList();
		NameValueBean nameValueBean1 = new NameValueBean("UserDefined","UserDefined");
		dataTypeList.add(nameValueBean1);
		/*
         NameValueBean nameValueBean2 = new NameValueBean("CADSR","CADSR");
         dataTypeList.add(nameValueBean2);*/

		return dataTypeList; 
	}
	public String getSelectedControlCaption(String captionKey)
	{
		if(captionKey!=null)
		{
			ResourceBundle resourceBundle = ResourceBundle.getBundle("ApplicationResources");
			if(resourceBundle!=null)
			{
				return resourceBundle.getString(captionKey);
			}
		}
		return null;
	}
}
