
package edu.common.dynamicextensions.ui.webui.action;

/**
 * This is a base class for all action classes under DynamicExtensions project.
 * @author deepti_shelar
 */
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.actions.DispatchAction;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
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
		//TODO check how the logging should be done.
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
	 * @param stringList list of error messages
	 * @param formName name of the form
	 * @return ActionErrors list of error messages 
	 */
	protected ActionErrors getErrorMessages(List stringList, String formName)
	{
		ActionErrors actionErrors = new ActionErrors();
		if (stringList != null && !stringList.isEmpty())
		{
			Iterator iterator = stringList.iterator();
			while (iterator.hasNext())
			{
				actionErrors.add(ActionErrors.GLOBAL_ERROR, new ActionError((String) iterator.next()));
			}
		}

		return actionErrors;
	}
}
