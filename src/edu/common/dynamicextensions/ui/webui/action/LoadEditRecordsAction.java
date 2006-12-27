/**
 * 
 */

package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.processor.LoadEditRecordsProcessor;
import edu.common.dynamicextensions.ui.webui.actionform.EditRecordsForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.DynamicExtensionsUtility;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * @author chetan_patil
 *
 */
public class LoadEditRecordsAction extends BaseDynamicExtensionsAction
{

	/**
	 * This method overrides the execute method of the Action class.
	 * It forwards the action to the Edit Records page.
	 * @param mapping ActionMapping
	 * @param form ActionForm
	 * @param request HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws Exception on exception
	 * @return ActionForward
	 */
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String containerIdentifier = request.getParameter("containerIdentifier");
		
		ContainerInterface container = DynamicExtensionsUtility.getContainerByIdentifier(containerIdentifier);
		CacheManager.addObjectToCache(request, Constants.CONTAINER_INTERFACE, container);
		
		EditRecordsForm editRecordsForm = (EditRecordsForm) form;
		LoadEditRecordsProcessor loadEditRecordsProcessor = LoadEditRecordsProcessor.getInstance();
		loadEditRecordsProcessor.populateRecordIndex(editRecordsForm, container);
		
		return mapping.findForward(Constants.SHOW_EDIT_RECORDS_PAGE);
	}
	
}
