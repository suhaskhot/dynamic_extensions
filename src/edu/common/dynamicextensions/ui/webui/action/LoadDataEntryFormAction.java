
package edu.common.dynamicextensions.ui.webui.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import edu.common.dynamicextensions.domaininterface.userinterface.ContainerInterface;
import edu.common.dynamicextensions.ui.webui.actionform.DataEntryForm;
import edu.common.dynamicextensions.ui.webui.util.CacheManager;
import edu.common.dynamicextensions.util.global.Constants;

/**
 * @author sujay_narkar
 *
 */
public class LoadDataEntryFormAction extends BaseDynamicExtensionsAction
{
    
    /**
     * 
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) 
    {
        
        ContainerInterface containerInterface = (ContainerInterface)CacheManager.getObjectFromCache(request,Constants.CONTAINER_INTERFACE);
        
        DataEntryForm dataEntryForm = (DataEntryForm)form;
        dataEntryForm.setContainerInterface(containerInterface);
        
        return mapping.findForward("Success");
    }

}
