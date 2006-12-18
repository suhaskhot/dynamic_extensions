
package edu.common.dynamicextensions.ui.webui.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domain.userinterface.ContainmentAssociationControl;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.entitymanager.EntityManager;
import edu.common.dynamicextensions.entitymanager.EntityManagerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.LoadFormControlsProcessor;
import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.ui.webui.util.WebUIManager;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.beans.NameValueBean;
import edu.wustl.common.util.logger.Logger;

/**
 * This Action class Loads the Primary Information needed for BuildForm.jsp.
 * This will first check if the object is already present in cache , If yes, it will update
 * the actionForm and If No, It will populate the actionForm with fresh data.  
 * The exception thrown can be of 'Application' type ,in this case the same Screen will be displayed  
 * added with error messages .
 * And The exception thrown can be of 'System' type, in this case user will be directed to Error Page.
 * @author deepti_shelar
 */
public class LoadFormControlsAction extends BaseDynamicExtensionsAction
{
	/**
	 * @param mapping ActionMapping mapping
	 * @param form ActionForm form
	 * @param  request HttpServletRequest request
	 * @param response HttpServletResponse response
	 * @return ActionForward forward to next action
	 * @throws IOException 
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException DynamicExtensionsSystemException
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	throws IOException, DynamicExtensionsApplicationException
	{
		String actionForwardString = null;
		try
		{
			ControlsForm controlsForm = (ControlsForm) form;
			ContainerInterface containerInterface = WebUIManager.getCurrentContainer(request);
			Logger.out.debug("Loading form controls for [" + containerInterface.getCaption() + "]");
			//Code for AJAX
			String operation = request.getParameter("operation");
			if ((operation != null) && (operation.trim().equals("changeGroup")))
			{
				changeGroup(request, response, controlsForm);
				return null;
			}
			if ((operation != null) && (operation.trim().equals("changeForm")))
			{
				changeForm(request, response, controlsForm);
				return null;
			}

			LoadFormControlsProcessor loadFormControlsProcessor = LoadFormControlsProcessor.getInstance();
			
			ControlInterface selectedControl = loadFormControlsProcessor.getSelectedControl(controlsForm, containerInterface);
			if((selectedControl!=null)&&(selectedControl instanceof ContainmentAssociationControl))
			{
				loadContainmentAssociationControl(request,(ContainmentAssociationControl)selectedControl,controlsForm);
				actionForwardString = Constants.EDIT_SUB_FORM_PAGE;
			}
			else
			{
				loadFormControlsProcessor.loadFormControls(controlsForm, containerInterface);
				actionForwardString = Constants.SHOW_BUILD_FORM_JSP;
			}
			if ((controlsForm.getDataType() != null) && (controlsForm.getDataType().equals(ProcessorConstants.DATATYPE_NUMBER)))
			{
				initializeMeasurementUnits(controlsForm);
			}
		}
		catch (DynamicExtensionsSystemException e)
		{
			actionForwardString = catchException(e, request);
			
		}
		return (mapping.findForward(actionForwardString));
	}

	
	/**
	 * @param request
	 * @param selectedControl
	 */
	private void loadContainmentAssociationControl(HttpServletRequest request, ContainmentAssociationControl selectedControl,ControlsForm controlsForm)
	{
		//controlsForm.set
		CacheManager.addObjectToCache(request, selectedControl.getCaption(), selectedControl.getContainer());
		CacheManager.addObjectToCache(request, Constants.CURRENT_CONTAINER_NAME,selectedControl.getCaption());
	}


	/**
	 * @param request
	 * @param response
	 * @param actionForm
	 * @throws IOException 
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	private void changeForm(HttpServletRequest request, HttpServletResponse response, ControlsForm actionForm) throws IOException,
	DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		List<NameValueBean> formAttributes = getAttributesForForm(request.getParameter("frmName"));
		String xmlParentNode = "formAttributes";
		String xmlNodeId = "form-attribute-id";
		String xmlNodeName = "form-attribute-name";
		String responseXML = getResponseXMLString(xmlParentNode, xmlNodeId, xmlNodeName, formAttributes);
		sendResponse(responseXML, response);
	}

	/**
	 * @param parameter
	 * @return
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	private List<NameValueBean> getAttributesForForm(String formId) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		ArrayList<NameValueBean> formAttributesList = new ArrayList<NameValueBean>();
		if (formId != null)
		{
			//ContainerInterface container = EntityManager.getInstance().getContainerByIdentifier(formId);
			ContainerInterface container = DynamicExtensionsUtility.getContainerByIdentifier(formId);
			if (container != null)
			{
				Collection<ControlInterface> controlCollection = container.getControlCollection();
				if (controlCollection != null)
				{
					Iterator<ControlInterface> controlIterator = controlCollection.iterator();
					ControlInterface control = null;
					NameValueBean controlName = null;
					while (controlIterator.hasNext())
					{
						control = controlIterator.next();
						if (control != null)
						{
							//if control contains Attribute interface object then only show on UI. 
							//If control contains association objects do not show in attribute list
							if((control.getAbstractAttribute()!=null)&&(control.getAbstractAttribute() instanceof AttributeInterface))
							{
								controlName = new NameValueBean(control.getCaption(), control.getId());
								formAttributesList.add(controlName);
							}
						}
					}
				}
			}

		}
		/*NameValueBean entityName = null;
		 for(int i=0;i<5;i++)
		 {
		 entityName  = new NameValueBean(formName + "-Attr" + i,i);
		 formAttributesList.add(entityName);
		 }*/
		return formAttributesList;
	}

	/**
	 * @param request
	 * @param actionForm
	 * @throws IOException 
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	private void changeGroup(HttpServletRequest request, HttpServletResponse response, ControlsForm actionForm) throws IOException,
	DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		List<NameValueBean> formNames = getFormNamesForGroup(request.getParameter("grpName"));
		String xmlParentNode = "forms";
		String xmlIdNode = "form-id";
		String xmlNameNode = "form-name";
		String responseXML = getResponseXMLString(xmlParentNode, xmlIdNode, xmlNameNode, formNames);
		sendResponse(responseXML, response);
	}

	/**
	 * @throws IOException 
	 * 
	 */
	private void sendResponse(String responseXML, HttpServletResponse response) throws IOException
	{
		PrintWriter out = response.getWriter();
		response.setContentType("text/xml");
		out.write(responseXML);
	}

	/**
	 * @param xmlParentNode
	 * @param xmlNameNode
	 * @param listValues
	 * @return
	 */
	private String getResponseXMLString(String xmlParentNode, String xmlIdNode, String xmlNameNode, List<NameValueBean> listValues)
	{
		StringBuffer responseXML = new StringBuffer();
		NameValueBean bean = null;
		if ((xmlParentNode != null) && (xmlNameNode != null) && (listValues != null))
		{
			responseXML.append("<node>");
			int noOfValues = listValues.size();
			for (int i = 0; i < noOfValues; i++)
			{
				bean = listValues.get(i);
				if (bean != null)
				{
					responseXML.append("<" + xmlParentNode + ">");
					responseXML.append("<" + xmlIdNode + ">");
					responseXML.append(bean.getValue());
					responseXML.append("</" + xmlIdNode + ">");

					responseXML.append("<" + xmlNameNode + ">");
					responseXML.append(bean.getName());
					responseXML.append("</" + xmlNameNode + ">");
					responseXML.append("</" + xmlParentNode + ">");
				}
			}
			responseXML.append("</node>");

		}
		return responseXML.toString();
	}

	/**
	 * @param groupName
	 * @return
	 * @throws DynamicExtensionsApplicationException 
	 * @throws DynamicExtensionsSystemException 
	 */
	private List<NameValueBean> getFormNamesForGroup(String groupId) throws DynamicExtensionsSystemException, DynamicExtensionsApplicationException
	{
		ArrayList<NameValueBean> formNames = new ArrayList<NameValueBean>();
		if (groupId != null)
		{
			EntityManagerInterface entityManager = EntityManager.getInstance();
			Long iGroupId = null;
			try
			{
				iGroupId = Long.parseLong(groupId);
				Collection<ContainerInterface> containerInterfaceList = entityManager.getAllContainersByEntityGroupId(iGroupId);
				if (containerInterfaceList != null)
				{
					ContainerInterface entityContainer = null;
					//EntityInterface entity = null;
					NameValueBean formName = null;
					Iterator<ContainerInterface> containerIterator = containerInterfaceList.iterator();
					while (containerIterator.hasNext())
					{
						entityContainer = containerIterator.next();
						if (entityContainer != null)
						{
							formName = new NameValueBean(entityContainer.getCaption(), entityContainer.getId());
							formNames.add(formName);
						}
					}
				}
			}
			catch (NumberFormatException e)
			{
				Logger.out.error("Group Id is null..Please check");
			}
		}
		return formNames;
	}

	/**
	 * Initialises MeasurementUnits
	 * @param controlsForm actionform
	 */
	private void initializeMeasurementUnits(ControlsForm controlsForm)
	{
		if ((controlsForm != null) && (controlsForm.getAttributeMeasurementUnits() != null))
		{
			//If value is not contained in the list, make "other" option as selected and value in textbox
			if (!containsValue(controlsForm.getMeasurementUnitsList(), controlsForm.getAttributeMeasurementUnits()))
			{
				controlsForm.setMeasurementUnitOther(controlsForm.getAttributeMeasurementUnits());
				controlsForm.setAttributeMeasurementUnits(ProcessorConstants.MEASUREMENT_UNIT_OTHER);
			}
			else
			{
				controlsForm.setMeasurementUnitOther("");
			}
		}
		else
		{
			controlsForm.setMeasurementUnitOther("");
		}

	}

	/**
	 * Test whether the list contains a value
	 * @param measurementUnitsList :List of strings
	 * @param attributeMeasurementUnit attributeMeasurementUnit
	 * @return boolean whether the list contains a value
	 */
	private boolean containsValue(List measurementUnitsList, String attributeMeasurementUnit)
	{
		String measurementUnit = null;
		if ((measurementUnitsList != null) && (attributeMeasurementUnit != null))
		{
			Iterator iter = measurementUnitsList.iterator();
			if (iter != null)
			{
				while (iter.hasNext())
				{
					measurementUnit = (String) iter.next();
					if (attributeMeasurementUnit.equals(measurementUnit))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

}
