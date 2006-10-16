
package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.util.global.Constants;

/**
 * @author sujay_narkar
 *
 */
public class LoadFormPreviewAction extends BaseDynamicExtensionsAction
{
    /**
     * 
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) 
    {
        
        return mapping.findForward(Constants.SHOW_FORM_PREVIEW_JSP);
        
    }

}
