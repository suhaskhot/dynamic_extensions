/**
 * 
 */
package edu.common.dynamicextensions.ui.webui.action;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.ui.webui.actionform.FormsIndexForm;
import edu.common.dynamicextensions.util.global.Constants;
import edu.common.dynamicextensions.entitymanager.*;
/**
 * @author chetan_patil
 *
 */
public class LoadFormsIndexAction extends BaseDynamicExtensionsAction
{
	/**
	 * This method overrides the execute method of the Action class.
	 * It forwards the action to the DynamicExtension Home page.
	 * This is the first action called when the dynamicExceptions application is started ,
	 * So it initialises the cache with a new cacheMap. 
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
		HttpSession session = request.getSession();
		Map cacheMap = new HashMap();
		session.setAttribute(Constants.CACHE_MAP,cacheMap);
		
		FormsIndexForm loadFormIndexForm = (FormsIndexForm)form;
		
		Collection entityList = new MockEntityManager().getAllEntities();
		loadFormIndexForm.setEntityList(entityList);
		return mapping.findForward(Constants.SHOW_DYNAMIC_EXTENSIONS_HOMEPAGE);
	}
	
}
