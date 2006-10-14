/*
 * Created on Oct 14, 2006
 * @author
 *
 */
package edu.common.dynamicextensions.processor;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.EntityInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.ui.webui.actionform.ControlsForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * @author preeti_munot
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class ApplyFormControlsProcessor extends BaseDynamicExtensionsProcessor
{
	/**
	 * Protected constructor for ControlProcessor
	 *
	 */
	protected  ApplyFormControlsProcessor () {

	}
	/**
	 * this method gets the new instance of the ControlProcessor to the caller.
	 * @return ControlProcessor ControlProcessor instance
	 */
	public static ApplyFormControlsProcessor getInstance () {
		return new ApplyFormControlsProcessor();
	}

	public void addControlToForm(ContainerInterface containerInterface, ControlsForm controlsForm)
	{
		if(containerInterface != null) {
			EntityInterface entityInterface = containerInterface.getEntity();
			ControlProcessor controlProcessor = ControlProcessor.getInstance();
			AttributeProcessor attributeProcessor = AttributeProcessor.getInstance();
			try {
				//Create Attribute  
				AbstractAttributeInterface abstractAttributeInterface = attributeProcessor.createAndPopulateAttribute(controlsForm);
				controlsForm.setAbstractAttribute(abstractAttributeInterface);
				//Control Interface : Add control
				ControlInterface controlInterface = controlProcessor.createAndPopulateControl(controlsForm.getUserSelectedTool(),controlsForm);
				//Entity Interface  : Add attribute
				if(entityInterface != null) {
					entityInterface.addAbstractAttribute(abstractAttributeInterface);
				}
				//Container : Add control and entity
				containerInterface.addControl(controlInterface);
				containerInterface.setEntity(entityInterface);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
