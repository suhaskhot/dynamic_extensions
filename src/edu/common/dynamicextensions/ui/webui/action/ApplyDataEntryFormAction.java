
package edu.common.dynamicextensions.ui.webui.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.AbstractAttributeInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.domaininterface.userinterface.ControlInterface;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * This class is invoked on Apply click action.
 * It populates the Attribute values entered in the dynamically generated controls. * 
 * @author chetan_patil
 */
public class ApplyDataEntryFormAction extends BaseDynamicExtensionsAction
{
	/**
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
	{
		ContainerInterface containerInterface = (ContainerInterface) CacheManager.getObjectFromCache(request, Constants.CONTAINER_INTERFACE);
		Collection controlCollection = containerInterface.getControlCollection();
		AbstractAttributeInterface abstractAttributeInterface = null;
		ControlInterface controlInterface = null;
		
		Map attributeValueMap = new HashMap();		
		String value = null;

		for (int sequence = 1; sequence < controlCollection.size(); sequence++)
		{
			value = request.getParameter("control_" + sequence);

			for (Iterator controlCollectionIterator = controlCollection.iterator(); controlCollectionIterator.hasNext();)
			{
				controlInterface = (ControlInterface) controlCollectionIterator.next();
				if(sequence == controlInterface.getSequenceNumber().intValue())
				{
					abstractAttributeInterface = controlInterface.getAbstractAttribute();
					attributeValueMap.put(abstractAttributeInterface, value);
					break;
				}
			}
		}
		return (mapping.findForward(Constants.SUCCESS));
	}
}
