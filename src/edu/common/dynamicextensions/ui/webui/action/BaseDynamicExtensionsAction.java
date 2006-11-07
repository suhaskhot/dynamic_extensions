
package edu.common.dynamicextensions.ui.webui.action;

/**
 * This is a base class for all action classes under DynamicExtensions project.
 * @author deepti_shelar
 */
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.actions.DispatchAction;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.common.dynamicextensions.util.global.Constants;
import edu.wustl.common.util.logger.Logger;

public class BaseDynamicExtensionsAction extends DispatchAction
{
	/**
	 * This method is called from every action class when any of exception is caught,
	 * Depending upon the type of exception the errorsList will be populated and returned to the action class,
	 * eg : In case of  DynamicExtensionsApplicationException , errorsList will contain error messages.
	 * In case of DynamicExtensionsSystemException , errorsList will be empty.
	 * @param throwable exception reference
	 * @param <String>errorMessagesList list of error messages
	 * @return boolean flag to determine whether this is a systemexception or an applicationException 
	 */
	protected boolean handleException(Throwable throwable, List <String>errorMessagesList)
	{
		Logger.out.error(throwable.getStackTrace(), throwable);
		Logger.out.debug(throwable.getStackTrace(), throwable);
		boolean isSystemException = false;
		if (errorMessagesList == null)
		{
			errorMessagesList = new ArrayList<String>();
		}
		if (throwable instanceof DynamicExtensionsApplicationException)
		{
			DynamicExtensionsApplicationException appException = (DynamicExtensionsApplicationException) throwable;
			String errorCode = appException.getErrorCode();
			errorMessagesList.add(errorCode);
		}
		else if (throwable instanceof DynamicExtensionsSystemException)
		{
			DynamicExtensionsSystemException systemException = (DynamicExtensionsSystemException) throwable;
			String errorCode = systemException.getErrorCode();
			errorMessagesList.add(errorCode);
			isSystemException = true;
		}
		return isSystemException;
	}

	/**
	 * 
	 * @param errorList List<String> list of error messages
	 * @return ActionErrors list of error messages 
	 */
	protected ActionErrors getErrorMessages(List<String> errorList)
	{
		ActionErrors actionErrors = new ActionErrors();
		if (errorList != null && !errorList.isEmpty())
		{
			for (String errorMessage : errorList)
			{
				actionErrors.add(ActionErrors.GLOBAL_ERROR, new ActionError(errorMessage));
			}
		}

		return actionErrors;
	}
	/**
	 * 
	 * @param e Exception e 
	 * @param request HttpServletRequest request
	 */
	protected String catchException(Exception e, HttpServletRequest request)
	{
		List<String> list = new ArrayList<String>();
		boolean isSystemException = handleException(e, list);
		saveErrors(request, getErrorMessages(list));
		String actionForwardString = "";
		if (isSystemException)
		{
			actionForwardString = Constants.SYSTEM_EXCEPTION;
		}
		return actionForwardString;
	}
	
}
