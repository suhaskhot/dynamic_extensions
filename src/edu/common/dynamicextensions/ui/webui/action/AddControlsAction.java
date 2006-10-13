package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.AttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.processor.AttributeProcessor;
import edu.common.dynamicextensions.processor.ControlProcessor;
import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.Constants;


/*This class is executed when user selects 'Add to Form'.
 * The exception thrown can be of 'Application' type ,in this case the same Screen will be displayed  
 * added with error messages .
 * And The exception thrown can be of 'System' type, in this case user will be directed to Error Page.
 * @author deepti_shelar
 *
 */
public class AddControlsAction extends BaseDynamicExtensionsAction {
	/**
	 * 
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,HttpServletRequest request,
			HttpServletResponse response) {
		ControlsForm actionForm = (ControlsForm)form;
		ContainerInterface containerInterface = (ContainerInterface)CacheManager.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);
		if(containerInterface != null) {
			EntityInterface entityInterface = containerInterface.getEntity();
			ControlProcessor controlProcessor = ControlProcessor.getInstance();
			AttributeProcessor attributeProcessor = AttributeProcessor.getInstance();
			try {
				AbstractAttributeInterface abstractAttributeInterface = attributeProcessor.createAndPopulateAttribute(actionForm);
				//attributeProcessor.populateAttribute(actionForm,abstractAttributeInterface);
				actionForm.setAbstractAttribute(abstractAttributeInterface);
				ControlInterface controlInterface = controlProcessor.createAndPopulateControl(actionForm.getUserSelectedTool(),actionForm);
				//controlProcessor.populateControl(actionForm, controlInterface);
				if(entityInterface != null) {
					entityInterface.addAbstractAttribute(abstractAttributeInterface);
					containerInterface.addControl(controlInterface);
					containerInterface.setEntity(entityInterface);
				}

				CacheManager.addObjectToCache(request, Constants.CONTROL_INTERFACE, controlInterface);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return mapping.findForward(Constants.SUCCESS);
	}
}
