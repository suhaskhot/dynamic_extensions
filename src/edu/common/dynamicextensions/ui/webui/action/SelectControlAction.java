/*
 * Created on Nov 14, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.ui.webui.action;

import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.processor.ProcessorConstants;
import edu.common.dynamicextensions.ui.util.ControlConfigurationsFactory;
import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;

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
			controlsForm.setSelectedControlCaption(getControlCaption(controlConfigurationsFactory.getControlDisplayLabel(userSelectedTool)));
			String jspName = controlConfigurationsFactory.getControlJspName(userSelectedTool);
			if (jspName == null)
			{
				jspName = "";
			}
			controlsForm.setHtmlFile(jspName);
			//Data types for selected control
			controlsForm.setDataTypeList(controlConfigurationsFactory.getControlsDataTypes(userSelectedTool));
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
