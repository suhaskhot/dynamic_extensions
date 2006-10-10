package edu.common.dynamicextensions.ui.webui.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.struts.actions.DispatchAction;

import edu.common.dynamicextensions.exception.DynamicExtensionsApplicationException;
import edu.common.dynamicextensions.exception.DynamicExtensionsSystemException;
import edu.wustl.common.util.logger.Logger;

public class BaseDynamicExtensionsAction extends DispatchAction{
	/**
	 * This method is called from every action class when exception is caught,
	 * Depending upon the type of exception the errorsList will be populated and returned to the action class,
	 * eg : In case of  DynamicExtensionsApplicationException , errorsList will contain error messages.
	 * In case of DynamicExtensionsSystemException , errorsList will be empty.
	 * @param throwable
	 * @param errorMessagesList
	 * @return errorsList
	 */
	protected List handleException(Throwable throwable,List errorMessagesList){
		Logger.out.error(throwable.getStackTrace(), throwable);
		Logger.out.debug(throwable.getStackTrace(), throwable);
		//TODO check how the logging shd be done.
		List errorsList = new ArrayList();
		if (throwable instanceof DynamicExtensionsApplicationException) {
			DynamicExtensionsApplicationException appException = (DynamicExtensionsApplicationException)throwable;
			errorsList.add(appException.getErrorMessage());
		}else if (throwable instanceof DynamicExtensionsSystemException) {
			//TODO
		}
		return errorsList;
	}
}
