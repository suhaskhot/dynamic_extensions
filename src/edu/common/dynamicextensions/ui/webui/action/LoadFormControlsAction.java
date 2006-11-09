
package edu.common.dynamicextensions.ui.webui.action;

import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.LoadFormControlsProcessor;
import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.Constants;

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
	 * @throws DynamicExtensionsSystemException DynamicExtensionsSystemException
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws DynamicExtensionsSystemException
	{
		ControlsForm actionForm = (ControlsForm) form;
		ContainerInterface containerInterface = (ContainerInterface) CacheManager.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);

		LoadFormControlsProcessor loadFormControlsProcessor = LoadFormControlsProcessor.getInstance();
		loadFormControlsProcessor.loadFormControls(actionForm, containerInterface);

		if ((actionForm.getDataType() != null) && (actionForm.getDataType().equals(ProcessorConstants.DATATYPE_NUMBER)))
		{
			initializeMeasurementUnits(actionForm);
		}
		return mapping.findForward(Constants.SHOW_BUILD_FORM_JSP);

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
