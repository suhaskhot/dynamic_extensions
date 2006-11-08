package edu.common.dynamicextensions.ui.webui.util;
/**
 * This class acts as an interface to the external systems.The dynamic extensions can be delivered
 * as a seperate war.
 * This class provides URLs for various action classes as well as parameter names.Those parameters can be 
 * send along with query String.  
 * @author sujay_narkar
 *
 */

public class WebUIManager implements WebUIManagerConstants
{
		
	/**
	 * The URL for action class wich creates container.
	 * @return
	 */
	public static String getCreateContainerURL()
	{
		return CREATE_CONTAINER_URL;
	}
	
	/**
	 * The parameter to be set in the request which specifies the callback URL.
	 * @return
	 */
	public static String getCallbackURLParamName()
	{
		return CALLBACK_URL_PARAM_NAME;		
	}
	
	/**
	 * The URL for action class which returns the different objects in the JESSON format 
	 * depending on operation.
	 * @return
	 */
	public static String getDynamicExtensionsInterfaceActionURL()
	{
		return DYNAMIC_EXTENSIONS_INTERFACE_ACTION_URL;
	}
	
	/**
	 * The parameter to be set in the request which specifies the operation for DynamicExtensionsInterfaceAction
	 * @return
	 */
	public static String getDynamicExtenionsInterfaceActionParamName()
	{
		return DYNAMIC_EXTENSIONS_INTERFACE_ACTION_PARAM_NAME;
	}
	/**
	 * 
	 *The URL for action class which displays the UI for record insertion.
	 */
	public static String getLoadDataEntryFormActionURL()
	{
		return LOAD_DATA_ENTRY_FORM_ACTION_URL;
	}
	
	/**
	 * The parameter to be set in the request which specifies the containerIdentifier for LoadDataEntryFormAction. 
	 * @return
	 */
	public static String getContainerIdentifierParameterName()
	{
		return CONATINER_IDENTIFIER_PARAMETER_NAME;
	}
	/**
	 * The parameter to be set in the request which specifies the record id for LoadDataEntryFormAction.
	 *
	 */
	public static String getRecordIdentifierParameterName()
	{
		return RECORD_IDENTIFIER_PARAMETER_NAME;
	}

}
