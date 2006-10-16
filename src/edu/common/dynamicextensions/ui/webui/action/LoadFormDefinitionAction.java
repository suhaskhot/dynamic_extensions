
package edu.common.dynamicextensions.ui.webui.action;
/**
 * This Action class Loads the Primary Information needed for CreateForm.jsp.
 * This will first check if the form object is already present in cache , If yes, it will update
 * the actionForm and If No, It will populate the actionForm with fresh data.  
 * The exception thrown can be of 'Application' type ,in this case the same Screen will be displayed  
 * added with error messages .
 * And The exception thrown can be of 'System' type, in this case user will be directed to Error Page.
 * @author deepti_shelar
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.processor.LoadFormDefinitionProcessor;
import edu.common.dynamicextensions.ui.webui.actionform.FormDefinitionForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.Constants;


public class LoadFormDefinitionAction extends BaseDynamicExtensionsAction {
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		FormDefinitionForm actionForm = (FormDefinitionForm)form;
		ContainerInterface containerInterface = (ContainerInterface)CacheManager.getObjectFromCache(request,Constants.CONTAINER_INTERFACE);
		if(containerInterface != null) {
			LoadFormDefinitionProcessor loadFormDefinitionProcessor = LoadFormDefinitionProcessor.getInstance();
			loadFormDefinitionProcessor.populateContainerInformation(containerInterface, actionForm);
		}
		return (mapping.findForward(Constants.SUCCESS));
	}
}
